package de.mrapp.android.util.logging;

/**
 * Enum representing log levels.
 */
public enum LogLevel {
    ALL(0),
    VERBOSE(1),
    DEBUG(2),
    INFO(3),
    WARN(4),
    ERROR(5),
    OFF(6);

    private final int priority;

    LogLevel(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isEnabled(LogLevel level) {
        return this.priority <= level.priority;
    }
}
