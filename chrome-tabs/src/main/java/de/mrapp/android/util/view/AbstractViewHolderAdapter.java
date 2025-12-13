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

    private Context context;
    private LayoutInflater inflater;
    private View currentParentView;

    /**
     * Creates a new adapter.
     */
    public AbstractViewHolderAdapter() {
        // Default constructor
    }

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
     * Sets the context.
     *
     * @param context The context
     */
    public void setContext(@NonNull Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * Gets the context.
     *
     * @return The context
     */
    @Nullable
    public Context getContext() {
        return context;
    }

    /**
     * Gets the layout inflater.
     *
     * @return The layout inflater
     */
    @Nullable
    protected LayoutInflater getInflater() {
        return inflater;
    }

    /**
     * Sets the current parent view.
     *
     * @param view The parent view
     */
    protected void setCurrentParentView(@Nullable View view) {
        this.currentParentView = view;
    }

    /**
     * Gets the current parent view.
     *
     * @return The parent view
     */
    @Nullable
    protected View getCurrentParentView() {
        return currentParentView;
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
