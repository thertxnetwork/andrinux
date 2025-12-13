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
    public abstract AbstractViewHolderAdapter<ItemType, ParamType, ?> getAdapter();
}
