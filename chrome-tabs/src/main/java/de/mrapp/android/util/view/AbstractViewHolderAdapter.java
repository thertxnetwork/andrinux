package de.mrapp.android.util.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * An abstract adapter for creating and binding views.
 *
 * @param <ItemType>      The type of items
 * @param <ParamType>     The type of parameters
 * @param <ViewHolderType> The type of view holder
 */
public abstract class AbstractViewHolderAdapter<ItemType, ParamType, ViewHolderType> {

    private final Context context;
    private final LayoutInflater inflater;

    /**
     * Creates a new adapter.
     *
     * @param context The context
     */
    public AbstractViewHolderAdapter(@NonNull Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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
     * Called to create a view.
     *
     * @param inflater The layout inflater
     * @param parent   The parent view group
     * @param item     The item
     * @param index    The index
     * @param params   The parameters
     * @return The created view
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
     * @param context The context
     * @param view    The view
     * @param item    The item
     * @param inflated Whether the view was just inflated
     * @param params  The parameters
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
}
