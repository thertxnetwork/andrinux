package de.mrapp.android.util.gesture;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;

/**
 * A helper class for handling drag gestures.
 */
public class DragHelper {

    private int touchSlop;
    private float startX;
    private float startY;
    private float deltaX;
    private float deltaY;
    private float dragStartPosition;
    private float dragDistance;
    private float minDragDistance;
    private float maxDragDistance;
    private boolean dragging;
    private boolean isReset;

    /**
     * Creates a new drag helper.
     *
     * @param threshold The drag threshold in pixels
     */
    public DragHelper(int threshold) {
        this.touchSlop = threshold;
        this.minDragDistance = Float.MIN_VALUE;
        this.maxDragDistance = Float.MAX_VALUE;
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
        dragStartPosition = 0;
        dragDistance = 0;
        dragging = false;
        isReset = true;
    }

    /**
     * Resets the drag helper with a new threshold.
     *
     * @param threshold The new drag threshold
     */
    public void reset(int threshold) {
        this.touchSlop = threshold;
        reset();
    }

    /**
     * Returns whether the drag helper is in reset state.
     *
     * @return True if reset, false otherwise
     */
    public boolean isReset() {
        return isReset;
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
     * Updates the drag helper with a position.
     *
     * @param position The drag position
     */
    public void update(float position) {
        if (isReset) {
            dragStartPosition = position;
            isReset = false;
        }
        dragDistance = position - dragStartPosition;
        dragDistance = Math.max(minDragDistance, Math.min(maxDragDistance, dragDistance));
        
        if (!dragging && Math.abs(dragDistance) > touchSlop) {
            dragging = true;
        }
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
            dragStartPosition = x;
            isReset = false;
        }

        deltaX = x - startX;
        deltaY = y - startY;
        dragDistance = deltaX;

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
     * Gets the drag start position.
     *
     * @return The drag start position
     */
    public float getDragStartPosition() {
        return dragStartPosition;
    }

    /**
     * Gets the drag distance.
     *
     * @return The drag distance
     */
    public float getDragDistance() {
        return dragDistance;
    }

    /**
     * Gets the drag distance (alias for getDistance).
     *
     * @return The distance
     */
    public float getDistance() {
        return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Sets the minimum drag distance.
     *
     * @param minDragDistance The minimum drag distance
     */
    public void setMinDragDistance(float minDragDistance) {
        this.minDragDistance = minDragDistance;
    }

    /**
     * Sets the maximum drag distance.
     *
     * @param maxDragDistance The maximum drag distance
     */
    public void setMaxDragDistance(float maxDragDistance) {
        this.maxDragDistance = maxDragDistance;
    }
}
