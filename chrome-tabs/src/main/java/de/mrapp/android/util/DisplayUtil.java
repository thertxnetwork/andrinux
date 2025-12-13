package de.mrapp.android.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.NonNull;

/**
 * Utility class for display-related operations.
 */
public final class DisplayUtil {

    /**
     * Enum representing device types.
     */
    public enum DeviceType {
        PHONE,
        TABLET
    }

    /**
     * Enum representing screen orientations.
     */
    public enum Orientation {
        PORTRAIT,
        LANDSCAPE
    }

    private DisplayUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Gets the device type based on screen size.
     *
     * @param context The context
     * @return The device type
     */
    @NonNull
    public static DeviceType getDeviceType(@NonNull Context context) {
        boolean isTablet = (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        return isTablet ? DeviceType.TABLET : DeviceType.PHONE;
    }

    /**
     * Gets the current screen orientation.
     *
     * @param context The context
     * @return The orientation
     */
    @NonNull
    public static Orientation getOrientation(@NonNull Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_LANDSCAPE 
                ? Orientation.LANDSCAPE : Orientation.PORTRAIT;
    }

    /**
     * Converts dp to pixels.
     *
     * @param context The context
     * @param dp      The dp value
     * @return The pixel value
     */
    public static int dpToPixels(@NonNull Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    /**
     * Gets the display width in pixels.
     *
     * @param context The context
     * @return The width in pixels
     */
    public static int getDisplayWidth(@NonNull Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(metrics);
        }
        return metrics.widthPixels;
    }

    /**
     * Gets the display height in pixels.
     *
     * @param context The context
     * @return The height in pixels
     */
    public static int getDisplayHeight(@NonNull Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(metrics);
        }
        return metrics.heightPixels;
    }
}
