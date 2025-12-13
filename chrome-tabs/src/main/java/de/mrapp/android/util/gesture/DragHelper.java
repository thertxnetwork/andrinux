package de.mrapp.android.util.gesture;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;

/**
 * A helper class for handling drag gestures.
 */
public class DragHelper {

    private final int touchSlop;
    private float startX;
    private float startY;
    private float deltaX;
    private float deltaY;
    private boolean dragging;

    /**
     * Creates a new drag helper.
     *
     * @param threshold The drag threshold in pixels
     */
    public DragHelper(int threshold) {
        this.touchSlop = threshold;
        reset();
    }

    /**
     * Creates a new drag helper using the system touch slop.
     *
     * @param context The context
     */
    public DragHelper(@NonNull Context context) {
        this(ViewConfiguration.get(context).getScaledTouchSlop());
    }

    /**
     * Resets the drag helper.
     */
    public void reset() {
        startX = 0;
        startY = 0;
        deltaX = 0;
        deltaY = 0;
        dragging = false;
    }

    /**
     * Updates the drag helper with a touch event.
     *
     * @param event The motion event
     */
    public void update(@NonNull MotionEvent event) {
        update(event.getX(), event.getY());
    }

    /**
     * Updates the drag helper with coordinates.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public void update(float x, float y) {
        if (startX == 0 && startY == 0) {
            startX = x;
            startY = y;
        }

        deltaX = x - startX;
        deltaY = y - startY;

        if (!dragging) {
            float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            dragging = distance > touchSlop;
        }
    }

    /**
     * Gets whether a drag is occurring.
     *
     * @return Whether dragging
     */
    public boolean hasThresholdBeenReached() {
        return dragging;
    }

    /**
     * Gets the x delta.
     *
     * @return The x delta
     */
    public float getDeltaX() {
        return deltaX;
    }

    /**
     * Gets the y delta.
     *
     * @return The y delta
     */
    public float getDeltaY() {
        return deltaY;
    }

    /**
     * Gets the start x position.
     *
     * @return The start x
     */
    public float getStartX() {
        return startX;
    }

    /**
     * Gets the start y position.
     *
     * @return The start y
     */
    public float getStartY() {
        return startY;
    }

    /**
     * Gets the drag distance.
     *
     * @return The distance
     */
    public float getDistance() {
        return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
