package de.mrapp.android.util.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import java.util.Comparator;

import de.mrapp.android.util.logging.LogLevel;

/**
 * A view recycler that keeps track of attached views.
 *
 * @param <ItemType>  The type of items
 * @param <ParamType> The type of parameters
 */
public class AttachedViewRecycler<ItemType, ParamType> extends ViewRecycler<ItemType, ParamType> {

    private Comparator<ItemType> comparator;
    private ViewGroup parent;
    private boolean useParentPadding;
    private LogLevel logLevel;

    /**
     * Creates a new attached view recycler.
     *
     * @param context The context
     */
    public AttachedViewRecycler(@NonNull Context context) {
        super(context);
        this.logLevel = LogLevel.OFF;
    }

    /**
     * Creates a new attached view recycler with parent and inflater.
     *
     * @param parent   The parent view group
     * @param inflater The layout inflater
     */
    public AttachedViewRecycler(@NonNull ViewGroup parent, @NonNull LayoutInflater inflater) {
        super(inflater.getContext());
        this.parent = parent;
        this.logLevel = LogLevel.OFF;
    }

    /**
     * Creates a new attached view recycler with parent, inflater, and comparator.
     *
     * @param parent     The parent view group
     * @param inflater   The layout inflater
     * @param comparator The comparator for ordering items
     */
    public AttachedViewRecycler(@NonNull ViewGroup parent, @NonNull LayoutInflater inflater,
                                @Nullable Comparator<ItemType> comparator) {
        super(inflater.getContext());
        this.parent = parent;
        this.comparator = comparator;
        this.logLevel = LogLevel.OFF;
    }

    /**
     * Sets the comparator for ordering items.
     *
     * @param comparator The comparator
     */
    public void setComparator(@Nullable Comparator<ItemType> comparator) {
        this.comparator = comparator;
    }

    /**
     * Gets the comparator.
     *
     * @return The comparator
     */
    @Nullable
    public Comparator<ItemType> getComparator() {
        return comparator;
    }

    /**
     * Sets the parent view group.
     *
     * @param parent The parent view group
     */
    public void setParent(@Nullable ViewGroup parent) {
        this.parent = parent;
    }

    /**
     * Gets the parent view group.
     *
     * @return The parent view group
     */
    @Nullable
    public ViewGroup getParent() {
        return parent;
    }

    /**
     * Sets whether to use the parent's padding.
     *
     * @param useParentPadding Whether to use the parent's padding
     */
    public void setUseParentPadding(boolean useParentPadding) {
        this.useParentPadding = useParentPadding;
    }

    /**
     * Gets whether to use the parent's padding.
     *
     * @return Whether to use the parent's padding
     */
    public boolean isUseParentPadding() {
        return useParentPadding;
    }

    @Override
    public void setLogLevel(@NonNull LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    @NonNull
    public LogLevel getLogLevel() {
        return logLevel;
    }

    /**
     * Gets the view for an item.
     *
     * @param item The item
     * @return The view or null if not found
     */
    @Nullable
    public View getView(@NonNull ItemType item) {
        return getActiveViews().get(item);
    }

    @Override
    @NonNull
    public Pair<View, Boolean> inflate(@NonNull ItemType item,
                                       @Nullable ViewGroup parent,
                                       @NonNull ParamType... params) {
        ViewGroup effectiveParent = parent != null ? parent : this.parent;
        return super.inflate(item, effectiveParent, params);
    }

    @Override
    protected void onRemoveView(@NonNull View view) {
        super.onRemoveView(view);
        if (view.getParent() instanceof ViewGroup) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    /**
     * Inflates all items.
     */
    public void inflateAll() {
        // Implementation depends on specific use case
    }

    /**
     * Removes all views from the parent.
     */
    public void removeAllViews() {
        removeAll();
        if (parent != null) {
            parent.removeAllViews();
        }
    }

    /**
     * Gets the number of active views.
     *
     * @return The count
     */
    public int getCount() {
        return getActiveViews().size();
    }
}
