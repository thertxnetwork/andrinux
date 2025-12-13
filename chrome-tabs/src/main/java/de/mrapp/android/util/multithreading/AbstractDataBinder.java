package de.mrapp.android.util.multithreading;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LruCache;

/**
 * An abstract data binder for loading data asynchronously and binding it to views.
 *
 * @param <DataType> The type of data
 * @param <KeyType>  The type of key
 * @param <ViewType> The type of view
 * @param <ParamType> The type of parameters
 */
public abstract class AbstractDataBinder<DataType, KeyType, ViewType, ParamType> {

    /**
     * A listener interface for data binding events.
     *
     * @param <DataType>  The type of data
     * @param <KeyType>   The type of key
     * @param <ViewType>  The type of view
     * @param <ParamType> The type of parameters
     */
    public interface Listener<DataType, KeyType, ViewType, ParamType> {

        /**
         * Called when data binding has started.
         *
         * @param dataBinder The data binder
         * @param key        The key
         * @param view       The view
         * @param params     The parameters
         * @return True to proceed with loading, false to cancel
         */
        boolean onLoadData(@NonNull AbstractDataBinder<DataType, KeyType, ViewType, ParamType> dataBinder,
                          @NonNull KeyType key, @NonNull ViewType view, @NonNull ParamType... params);

        /**
         * Called when data has been loaded and bound.
         *
         * @param dataBinder The data binder
         * @param key        The key
         * @param data       The loaded data
         * @param view       The view
         * @param params     The parameters
         */
        void onFinished(@NonNull AbstractDataBinder<DataType, KeyType, ViewType, ParamType> dataBinder,
                       @NonNull KeyType key, @Nullable DataType data, @NonNull ViewType view,
                       @NonNull ParamType... params);

        /**
         * Called when data loading was canceled.
         *
         * @param dataBinder The data binder
         */
        void onCanceled(@NonNull AbstractDataBinder<DataType, KeyType, ViewType, ParamType> dataBinder);
    }

    private final Context context;
    private final LruCache<KeyType, DataType> cache;
    private final Handler mainHandler;
    private boolean canceled;
    private Listener<DataType, KeyType, ViewType, ParamType> listener;

    /**
     * Creates a new data binder.
     *
     * @param context The context
     * @param cache   The cache
     */
    public AbstractDataBinder(@NonNull Context context, @NonNull LruCache<KeyType, DataType> cache) {
        this.context = context;
        this.cache = cache;
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.canceled = false;
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
     * Gets the cache.
     *
     * @return The cache
     */
    @NonNull
    protected LruCache<KeyType, DataType> getCache() {
        return cache;
    }

    /**
     * Sets the listener.
     *
     * @param listener The listener
     */
    public void setListener(@Nullable Listener<DataType, KeyType, ViewType, ParamType> listener) {
        this.listener = listener;
    }

    /**
     * Gets the listener.
     *
     * @return The listener
     */
    @Nullable
    public Listener<DataType, KeyType, ViewType, ParamType> getListener() {
        return listener;
    }

    /**
     * Cancels all pending operations.
     */
    public void cancel() {
        canceled = true;
        if (listener != null) {
            listener.onCanceled(this);
        }
    }

    /**
     * Checks if canceled.
     *
     * @return Whether canceled
     */
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * Loads data for a key and binds it to a view.
     *
     * @param key    The key
     * @param view   The view
     * @param params The parameters
     */
    @SafeVarargs
    public final void load(@NonNull KeyType key, @NonNull ViewType view, @NonNull ParamType... params) {
        canceled = false;

        // Notify listener
        if (listener != null && !listener.onLoadData(this, key, view, params)) {
            return;
        }
        
        // Check cache first
        DataType cachedData = cache.get(key);
        if (cachedData != null) {
            onPostExecute(view, cachedData, params);
            if (listener != null) {
                listener.onFinished(this, key, cachedData, view, params);
            }
            return;
        }

        // Pre-execute on main thread
        onPreExecute(view, params);

        // Execute in background
        new AsyncTask<Void, Void, DataType>() {
            @Override
            protected DataType doInBackground(Void... voids) {
                if (canceled) return null;
                return AbstractDataBinder.this.doInBackground(key, params);
            }

            @Override
            protected void onPostExecute(DataType data) {
                if (canceled) return;
                if (data != null) {
                    cache.put(key, data);
                }
                AbstractDataBinder.this.onPostExecute(view, data, params);
                if (listener != null) {
                    listener.onFinished(AbstractDataBinder.this, key, data, view, params);
                }
            }
        }.execute();
    }

    /**
     * Called before background execution on the main thread.
     *
     * @param view   The view
     * @param params The parameters
     */
    @SafeVarargs
    protected void onPreExecute(@NonNull ViewType view, @NonNull ParamType... params) {
        // Default implementation does nothing
    }

    /**
     * Called in the background to load data.
     *
     * @param key    The key
     * @param params The parameters
     * @return The loaded data
     */
    @Nullable
    @SafeVarargs
    protected abstract DataType doInBackground(@NonNull KeyType key, @NonNull ParamType... params);

    /**
     * Called after background execution on the main thread.
     *
     * @param view   The view
     * @param data   The loaded data
     * @param params The parameters
     */
    @SafeVarargs
    protected void onPostExecute(@NonNull ViewType view, @Nullable DataType data, 
                                 @NonNull ParamType... params) {
        // Default implementation does nothing
    }

    /**
     * Clears the cache.
     */
    public void clearCache() {
        cache.evictAll();
    }
}
