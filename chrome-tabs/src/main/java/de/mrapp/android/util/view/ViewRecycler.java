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

import de.mrapp.android.util.logging.LogLevel;

/**
 * A view recycler for recycling views.
 *
 * @param <ItemType>  The type of items
 * @param <ParamType> The type of parameters
 */
public class ViewRecycler<ItemType, ParamType> extends AbstractViewRecycler<ItemType, ParamType> {

    private Adapter<ItemType, ParamType> adapter;
    private LogLevel logLevel = LogLevel.OFF;

    /**
     * Creates a new view recycler.
     *
     * @param context The context
     */
    public ViewRecycler(@NonNull Context context) {
        super(context);
    }

    /**
     * Creates a new view recycler with a layout inflater.
     *
     * @param inflater The layout inflater
     */
    public ViewRecycler(@NonNull LayoutInflater inflater) {
        super(inflater.getContext());
    }

    /**
     * Sets the adapter.
     *
     * @param adapter The adapter
     */
    public void setAdapter(@Nullable Adapter<ItemType, ParamType> adapter) {
        this.adapter = adapter;
    }

    @Override
    @Nullable
    public Adapter<ItemType, ParamType> getAdapter() {
        return adapter;
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

    /**
     * Clears the view cache.
     */
    public void clearCache() {
        removeAll();
    }

    @Override
    @NonNull
    public Pair<View, Boolean> inflate(@NonNull ItemType item, 
                                       @Nullable ViewGroup parent,
                                       @NonNull ParamType... params) {
        View view = getActiveViews().get(item);
        boolean inflated = false;
        
        if (view == null && adapter != null) {
            view = adapter.onInflateView(getInflater(), parent, item, 0, params);
            getActiveViews().put(item, view);
            inflated = true;
        }
        
        if (view != null && adapter != null) {
            adapter.onShowView(getContext(), view, item, inflated, params);
        }
        
        return new Pair<>(view, inflated);
    }

    @Override
    protected void onRemoveView(@NonNull View view) {
        if (adapter != null) {
            adapter.onRemoveView(view);
        }
    }
}
