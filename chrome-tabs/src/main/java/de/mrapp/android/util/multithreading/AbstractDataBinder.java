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

    private final Context context;
    private final LruCache<KeyType, DataType> cache;
    private final Handler mainHandler;
    private boolean canceled;

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
     * Cancels all pending operations.
     */
    public void cancel() {
        canceled = true;
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
        
        // Check cache first
        DataType cachedData = cache.get(key);
        if (cachedData != null) {
            onPostExecute(view, cachedData, params);
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
