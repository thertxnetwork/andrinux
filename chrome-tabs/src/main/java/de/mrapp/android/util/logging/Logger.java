package de.mrapp.android.util.logging;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A simple logger implementation.
 */
public class Logger {

    private final String tag;
    private LogLevel logLevel;

    /**
     * Creates a new logger with a log level.
     *
     * @param logLevel The log level
     */
    public Logger(@NonNull LogLevel logLevel) {
        this.tag = "Logger";
        this.logLevel = logLevel;
    }

    /**
     * Creates a new logger.
     *
     * @param tag The log tag
     */
    public Logger(@NonNull String tag) {
        this.tag = tag;
        this.logLevel = LogLevel.ALL;
    }

    /**
     * Creates a new logger with a specific log level.
     *
     * @param tag      The log tag
     * @param logLevel The log level
     */
    public Logger(@NonNull String tag, @NonNull LogLevel logLevel) {
        this.tag = tag;
        this.logLevel = logLevel;
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
     * Logs a verbose message.
     *
     * @param message The message
     */
    public void logVerbose(@NonNull String message) {
        if (logLevel.isEnabled(LogLevel.VERBOSE)) {
            Log.v(tag, message);
        }
    }

    /**
     * Logs a verbose message with a class tag.
     *
     * @param clazz   The class for tagging
     * @param message The message
     */
    public void logVerbose(@NonNull Class<?> clazz, @NonNull String message) {
        if (logLevel.isEnabled(LogLevel.VERBOSE)) {
            Log.v(clazz.getSimpleName(), message);
        }
    }

    /**
     * Logs a debug message.
     *
     * @param message The message
     */
    public void logDebug(@NonNull String message) {
        if (logLevel.isEnabled(LogLevel.DEBUG)) {
            Log.d(tag, message);
        }
    }

    /**
     * Logs a debug message with a class tag.
     *
     * @param clazz   The class for tagging
     * @param message The message
     */
    public void logDebug(@NonNull Class<?> clazz, @NonNull String message) {
        if (logLevel.isEnabled(LogLevel.DEBUG)) {
            Log.d(clazz.getSimpleName(), message);
        }
    }

    /**
     * Logs an info message.
     *
     * @param message The message
     */
    public void logInfo(@NonNull String message) {
        if (logLevel.isEnabled(LogLevel.INFO)) {
            Log.i(tag, message);
        }
    }

    /**
     * Logs an info message with a class tag.
     *
     * @param clazz   The class for tagging
     * @param message The message
     */
    public void logInfo(@NonNull Class<?> clazz, @NonNull String message) {
        if (logLevel.isEnabled(LogLevel.INFO)) {
            Log.i(clazz.getSimpleName(), message);
        }
    }

    /**
     * Logs a warning message.
     *
     * @param message The message
     */
    public void logWarn(@NonNull String message) {
        if (logLevel.isEnabled(LogLevel.WARN)) {
            Log.w(tag, message);
        }
    }

    /**
     * Logs a warning message with a class tag.
     *
     * @param clazz   The class for tagging
     * @param message The message
     */
    public void logWarn(@NonNull Class<?> clazz, @NonNull String message) {
        if (logLevel.isEnabled(LogLevel.WARN)) {
            Log.w(clazz.getSimpleName(), message);
        }
    }

    /**
     * Logs an error message.
     *
     * @param message The message
     */
    public void logError(@NonNull String message) {
        if (logLevel.isEnabled(LogLevel.ERROR)) {
            Log.e(tag, message);
        }
    }

    /**
     * Logs an error message with a class tag.
     *
     * @param clazz   The class for tagging
     * @param message The message
     */
    public void logError(@NonNull Class<?> clazz, @NonNull String message) {
        if (logLevel.isEnabled(LogLevel.ERROR)) {
            Log.e(clazz.getSimpleName(), message);
        }
    }

    /**
     * Logs an error message with a throwable.
     *
     * @param message   The message
     * @param throwable The throwable
     */
    public void logError(@NonNull String message, @Nullable Throwable throwable) {
        if (logLevel.isEnabled(LogLevel.ERROR)) {
            Log.e(tag, message, throwable);
        }
    }

    /**
     * Logs an error message with a class tag and throwable.
     *
     * @param clazz     The class for tagging
     * @param message   The message
     * @param throwable The throwable
     */
    public void logError(@NonNull Class<?> clazz, @NonNull String message, @Nullable Throwable throwable) {
        if (logLevel.isEnabled(LogLevel.ERROR)) {
            Log.e(clazz.getSimpleName(), message, throwable);
        }
    }
}
