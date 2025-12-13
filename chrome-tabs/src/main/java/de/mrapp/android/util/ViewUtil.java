package de.mrapp.android.util;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Utility class for view-related operations.
 */
public final class ViewUtil {

    private ViewUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Sets the background of a view.
     *
     * @param view       The view
     * @param background The background drawable
     */
    public static void setBackground(@NonNull View view, @Nullable Drawable background) {
        view.setBackground(background);
    }

    /**
     * Sets the visibility of a view.
     *
     * @param view    The view
     * @param visible Whether the view should be visible
     */
    public static void setVisible(@Nullable View view, boolean visible) {
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Sets the enabled state of a view.
     *
     * @param view    The view
     * @param enabled Whether the view should be enabled
     */
    public static void setEnabled(@Nullable View view, boolean enabled) {
        if (view != null) {
            view.setEnabled(enabled);
        }
    }

    /**
     * Removes a view from its parent.
     *
     * @param view The view to remove
     */
    public static void removeFromParent(@Nullable View view) {
        if (view != null && view.getParent() instanceof ViewGroup) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    /**
     * Gets the measured width of a view.
     *
     * @param view The view
     * @return The measured width or 0 if view is null
     */
    public static int getMeasuredWidth(@Nullable View view) {
        return view != null ? view.getMeasuredWidth() : 0;
    }

    /**
     * Gets the measured height of a view.
     *
     * @param view The view
     * @return The measured height or 0 if view is null
     */
    public static int getMeasuredHeight(@Nullable View view) {
        return view != null ? view.getMeasuredHeight() : 0;
    }

    /**
     * Sets the alpha of a view.
     *
     * @param view  The view
     * @param alpha The alpha value (0.0f to 1.0f)
     */
    public static void setAlpha(@Nullable View view, float alpha) {
        if (view != null) {
            view.setAlpha(alpha);
        }
    }

    /**
     * Sets margins on a view.
     *
     * @param view   The view
     * @param left   Left margin
     * @param top    Top margin
     * @param right  Right margin
     * @param bottom Bottom margin
     */
    public static void setMargins(@NonNull View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = 
                    (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(left, top, right, bottom);
            view.setLayoutParams(params);
        }
    }

    /**
     * Removes an OnGlobalLayoutListener from a ViewTreeObserver.
     *
     * @param observer The ViewTreeObserver
     * @param listener The listener to remove
     */
    public static void removeOnGlobalLayoutListener(@NonNull ViewTreeObserver observer,
                                                    @NonNull ViewTreeObserver.OnGlobalLayoutListener listener) {
        observer.removeOnGlobalLayoutListener(listener);
    }
}
