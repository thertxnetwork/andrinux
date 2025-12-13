package de.mrapp.android.util;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Utility class for condition checking and validation.
 */
public final class Condition {

    private Condition() {
        // Private constructor to prevent instantiation
    }

    /**
     * Ensures that a value is not null.
     *
     * @param value   The value to check
     * @param message The exception message if null
     * @param <T>     The type of the value
     * @return The non-null value
     */
    @NonNull
    public static <T> T ensureNotNull(@Nullable T value, @NonNull String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /**
     * Ensures that a value is not null, throwing a custom exception type.
     *
     * @param value          The value to check
     * @param message        The exception message if null
     * @param exceptionClass The exception class to throw
     * @param <T>            The type of the value
     * @return The non-null value
     */
    @NonNull
    public static <T> T ensureNotNull(@Nullable T value, @NonNull String message,
                                       @NonNull Class<? extends RuntimeException> exceptionClass) {
        if (value == null) {
            try {
                throw exceptionClass.getConstructor(String.class).newInstance(message);
            } catch (ReflectiveOperationException e) {
                throw new IllegalArgumentException(message);
            }
        }
        return value;
    }

    /**
     * Ensures that a CharSequence is not empty.
     *
     * @param value   The CharSequence to check
     * @param message The exception message if empty
     * @return The non-empty CharSequence
     */
    @NonNull
    public static CharSequence ensureNotEmpty(@Nullable CharSequence value, @NonNull String message) {
        if (TextUtils.isEmpty(value)) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /**
     * Ensures that a string is not empty.
     *
     * @param value   The string to check
     * @param message The exception message if empty
     * @return The non-empty string
     */
    @NonNull
    public static String ensureNotEmpty(@Nullable String value, @NonNull String message) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /**
     * Ensures that a value is at least a minimum value.
     *
     * @param value   The value to check
     * @param min     The minimum value
     * @param message The exception message if less than min
     * @return The value
     */
    public static int ensureAtLeast(int value, int min, @NonNull String message) {
        if (value < min) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /**
     * Ensures that a value is at least a minimum value.
     *
     * @param value   The value to check
     * @param min     The minimum value
     * @param message The exception message if less than min
     * @return The value
     */
    public static long ensureAtLeast(long value, long min, @NonNull String message) {
        if (value < min) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /**
     * Ensures that a value is at least a minimum value.
     *
     * @param value   The value to check
     * @param min     The minimum value
     * @param message The exception message if less than min
     * @return The value
     */
    public static float ensureAtLeast(float value, float min, @NonNull String message) {
        if (value < min) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /**
     * Ensures that a condition is true.
     *
     * @param condition The condition to check
     * @param message   The exception message if false
     */
    public static void ensureTrue(boolean condition, @NonNull String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Ensures that two values are not equal.
     *
     * @param value1  The first value
     * @param value2  The second value
     * @param message The exception message if equal
     */
    public static void ensureNotEqual(int value1, int value2, @NonNull String message) {
        if (value1 == value2) {
            throw new IllegalArgumentException(message);
        }
    }
}
