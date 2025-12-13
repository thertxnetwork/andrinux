package com.github.wrdlbrnft.sortedlistadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback

/**
 * A RecyclerView.Adapter backed by a SortedList for efficient sorted data management.
 * This is a reimplementation of the wrdlbrnft SortedListAdapter library.
 */
abstract class SortedListAdapter<T : SortedListAdapter.ViewModel>(
    context: Context,
    private val itemClass: Class<T>,
    private val comparator: Comparator<T>
) : RecyclerView.Adapter<SortedListAdapter.ViewHolder<out T>>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var callback: Callback? = null

    private val sortedListCallback = object : SortedListAdapterCallback<T>(this) {
        override fun compare(a: T, b: T): Int = comparator.compare(a, b)

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
            oldItem.isContentTheSameAs(newItem)

        override fun areItemsTheSame(item1: T, item2: T): Boolean =
            item1.isSameModelAs(item2)
    }

    private val sortedList: SortedList<T> = SortedList(itemClass, sortedListCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<out T> {
        return onCreateViewHolder(inflater, parent, viewType)
    }

    abstract fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<out T>

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ViewHolder<out T>, position: Int) {
        val item = sortedList[position]
        (holder as ViewHolder<T>).performBind(item)
    }

    override fun getItemCount(): Int = sortedList.size()

    fun getItem(position: Int): T = sortedList[position]

    fun edit(): Editor<T> = EditorImpl()

    fun addCallback(callback: Callback) {
        this.callback = callback
    }

    fun removeCallback(callback: Callback) {
        if (this.callback == callback) {
            this.callback = null
        }
    }

    /**
     * Callback interface for edit events.
     */
    interface Callback {
        fun onEditStarted()
        fun onEditFinished()
    }

    /**
     * Interface for view models that can be compared for identity and content equality.
     */
    interface ViewModel {
        fun <T> isSameModelAs(t: T): Boolean
        fun <T> isContentTheSameAs(t: T): Boolean
    }

    /**
     * ViewHolder for items in the SortedListAdapter.
     */
    abstract class ViewHolder<T : ViewModel>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun performBind(item: T)
    }

    /**
     * Editor interface for batch modifications to the adapter.
     */
    interface Editor<T : ViewModel> {
        fun add(item: T): Editor<T>
        fun add(items: List<T>): Editor<T>
        fun remove(item: T): Editor<T>
        fun remove(items: List<T>): Editor<T>
        fun replaceAll(items: List<T>): Editor<T>
        fun removeAll(): Editor<T>
        fun commit()
    }

    private inner class EditorImpl : Editor<T> {
        private val pendingAdditions = mutableListOf<T>()
        private val pendingRemovals = mutableListOf<T>()
        private var replaceAll: List<T>? = null
        private var removeAll = false

        override fun add(item: T): Editor<T> {
            pendingAdditions.add(item)
            return this
        }

        override fun add(items: List<T>): Editor<T> {
            pendingAdditions.addAll(items)
            return this
        }

        override fun remove(item: T): Editor<T> {
            pendingRemovals.add(item)
            return this
        }

        override fun remove(items: List<T>): Editor<T> {
            pendingRemovals.addAll(items)
            return this
        }

        override fun replaceAll(items: List<T>): Editor<T> {
            replaceAll = items
            return this
        }

        override fun removeAll(): Editor<T> {
            removeAll = true
            return this
        }

        override fun commit() {
            callback?.onEditStarted()
            sortedList.beginBatchedUpdates()
            try {
                if (removeAll) {
                    sortedList.clear()
                }

                replaceAll?.let { items ->
                    sortedList.clear()
                    sortedList.addAll(items)
                }

                pendingRemovals.forEach { item ->
                    sortedList.remove(item)
                }

                pendingAdditions.forEach { item ->
                    sortedList.add(item)
                }
            } finally {
                sortedList.endBatchedUpdates()
            }
            callback?.onEditFinished()
        }
    }

    /**
     * Builder for creating comparators.
     */
    class ComparatorBuilder<T : ViewModel> {
        private val comparators = mutableListOf<Comparator<T>>()

        @Suppress("UNCHECKED_CAST")
        fun <M : T> setOrderForModel(clazz: Class<M>, comparator: (M, M) -> Int): ComparatorBuilder<T> {
            comparators.add(Comparator { a, b ->
                if (clazz.isInstance(a) && clazz.isInstance(b)) {
                    comparator(a as M, b as M)
                } else {
                    0
                }
            })
            return this
        }

        fun build(): Comparator<T> {
            return Comparator { a, b ->
                for (comp in comparators) {
                    val result = comp.compare(a, b)
                    if (result != 0) return@Comparator result
                }
                0
            }
        }
    }
}
