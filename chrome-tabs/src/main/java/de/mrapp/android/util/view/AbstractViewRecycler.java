package de.mrapp.android.util.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstract view recycler for recycling views.
 *
 * @param <ItemType>  The type of items
 * @param <ParamType> The type of parameters
 */
public abstract class AbstractViewRecycler<ItemType, ParamType> {

    /**
     * An abstract adapter for the view recycler.
     *
     * @param <ItemType>  The type of items
     * @param <ParamType> The type of parameters
     */
    public static abstract class Adapter<ItemType, ParamType> {

        /**
         * Inflates a view for an item.
         *
         * @param inflater The layout inflater
         * @param parent   The parent view group
         * @param item     The item
         * @param index    The index
         * @param params   The parameters
         * @return The inflated view
         */
        @NonNull
        public abstract View onInflateView(@NonNull LayoutInflater inflater,
                                           @Nullable ViewGroup parent,
                                           @NonNull ItemType item,
                                           int index,
                                           @NonNull ParamType... params);

        /**
         * Called when a view is shown.
         *
         * @param context  The context
         * @param view     The view
         * @param item     The item
         * @param inflated Whether the view was just inflated
         * @param params   The parameters
         */
        public abstract void onShowView(@NonNull Context context,
                                        @NonNull View view,
                                        @NonNull ItemType item,
                                        boolean inflated,
                                        @NonNull ParamType... params);

        /**
         * Called when a view is removed.
         *
         * @param view The view
         */
        public void onRemoveView(@NonNull View view) {
            // Default implementation does nothing
        }

        /**
         * Called to save the state of a view.
         *
         * @param view The view
         * @param item The item
         * @return The saved state bundle, or null
         */
        @Nullable
        public android.os.Bundle onSaveInstanceState(@NonNull View view, @NonNull ItemType item) {
            return null;
        }

        /**
         * Called to restore the state of a view.
         *
         * @param view     The view
         * @param item     The item
         * @param savedState The saved state bundle
         */
        public void onRestoreInstanceState(@NonNull View view, @NonNull ItemType item,
                                           @NonNull android.os.Bundle savedState) {
            // Default implementation does nothing
        }
    }

    private final Context context;
    private final LayoutInflater inflater;
    private final Map<ItemType, View> activeViews;

    /**
     * Creates a new view recycler.
     *
     * @param context The context
     */
    public AbstractViewRecycler(@NonNull Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.activeViews = new HashMap<>();
    }

    /**
     * Gets the context.
     *
     * @return The context
     */
    @NonNull
    public Context getContext() {
        return context;
    }

    /**
     * Gets the layout inflater.
     *
     * @return The layout inflater
     */
    @NonNull
    protected LayoutInflater getInflater() {
        return inflater;
    }

    /**
     * Gets the active views.
     *
     * @return The active views map
     */
    @NonNull
    protected Map<ItemType, View> getActiveViews() {
        return activeViews;
    }

    /**
     * Inflates or recycles a view for an item.
     *
     * @param item   The item
     * @param parent The parent view group
     * @param params The parameters
     * @return A pair of the view and whether it was newly inflated
     */
    @NonNull
    public abstract Pair<View, Boolean> inflate(@NonNull ItemType item, 
                                                @Nullable ViewGroup parent,
                                                @NonNull ParamType... params);

    /**
     * Inflates or recycles a view for an item.
     *
     * @param item   The item
     * @param params The parameters
     * @return A pair of the view and whether it was newly inflated
     */
    @NonNull
    public Pair<View, Boolean> inflate(@NonNull ItemType item, @NonNull ParamType... params) {
        return inflate(item, null, params);
    }

    /**
     * Removes a view for an item.
     *
     * @param item The item
     */
    public void remove(@NonNull ItemType item) {
        View view = activeViews.remove(item);
        if (view != null) {
            onRemoveView(view);
        }
    }

    /**
     * Removes all views.
     */
    public void removeAll() {
        for (View view : activeViews.values()) {
            onRemoveView(view);
        }
        activeViews.clear();
    }

    /**
     * Called when a view is removed.
     *
     * @param view The view
     */
    protected void onRemoveView(@NonNull View view) {
        // Default implementation does nothing
    }

    /**
     * Gets the adapter.
     *
     * @return The adapter
     */
    @Nullable
    public abstract Adapter<ItemType, ParamType> getAdapter();
}
