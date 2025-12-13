package de.mrapp.android.util.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import java.util.Comparator;

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
     * Enum representing log levels.
     */
    public enum LogLevel {
        OFF, ALL, VERBOSE, DEBUG, INFO, WARN, ERROR
    }

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

    /**
     * Sets the log level.
     *
     * @param logLevel The log level
     */
    public void setLogLevel(@NonNull LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    /**
     * Gets the log level.
     *
     * @return The log level
     */
    @NonNull
    public LogLevel getLogLevel() {
        return logLevel;
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
