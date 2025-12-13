package de.mrapp.android.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

/**
 * Utility class for theme-related operations.
 */
public final class ThemeUtil {

    private ThemeUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Gets a color from a theme attribute.
     *
     * @param context The context
     * @param attr    The attribute resource ID
     * @return The color value
     */
    public static int getColor(@NonNull Context context, @AttrRes int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }

    /**
     * Gets a dimension from a theme attribute.
     *
     * @param context The context
     * @param attr    The attribute resource ID
     * @return The dimension value
     */
    public static int getDimensionPixelSize(@NonNull Context context, @AttrRes int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return TypedValue.complexToDimensionPixelSize(typedValue.data, 
                context.getResources().getDisplayMetrics());
    }

    /**
     * Gets a resource ID from a theme attribute.
     *
     * @param context The context
     * @param attr    The attribute resource ID
     * @return The resource ID
     */
    public static int getResourceId(@NonNull Context context, @AttrRes int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.resourceId;
    }

    /**
     * Gets a drawable from a theme attribute.
     *
     * @param context The context
     * @param attr    The attribute resource ID
     * @return The drawable or null if not found
     */
    @Nullable
    public static Drawable getDrawable(@NonNull Context context, @AttrRes int attr) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(attr, typedValue, true)) {
            return context.getResources().getDrawable(typedValue.resourceId, context.getTheme());
        }
        return null;
    }

    /**
     * Gets a boolean from a theme attribute.
     *
     * @param context      The context
     * @param attr         The attribute resource ID
     * @param defaultValue The default value if not found
     * @return The boolean value
     */
    public static boolean getBoolean(@NonNull Context context, @AttrRes int attr, boolean defaultValue) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(attr, typedValue, true)) {
            return typedValue.data != 0;
        }
        return defaultValue;
    }

    /**
     * Gets an integer from a theme attribute.
     *
     * @param context      The context
     * @param attr         The attribute resource ID
     * @param defaultValue The default value if not found
     * @return The integer value
     */
    public static int getInt(@NonNull Context context, @AttrRes int attr, int defaultValue) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(attr, typedValue, true)) {
            return typedValue.data;
        }
        return defaultValue;
    }

    /**
     * Gets a text from a theme attribute.
     *
     * @param context The context
     * @param attr    The attribute resource ID
     * @return The text or null if not found
     */
    public static CharSequence getText(@NonNull Context context, @AttrRes int attr) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(attr, typedValue, true)) {
            return typedValue.coerceToString();
        }
        return null;
    }
}
