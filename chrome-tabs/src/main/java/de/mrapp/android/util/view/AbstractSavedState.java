package de.mrapp.android.util.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * An abstract implementation of a saved state for custom views.
 */
public abstract class AbstractSavedState extends View.BaseSavedState {

    /**
     * Creates a new saved state.
     *
     * @param superState The super state
     */
    protected AbstractSavedState(@Nullable Parcelable superState) {
        super(superState);
    }

    /**
     * Creates a new saved state from a parcel.
     *
     * @param source The parcel
     */
    protected AbstractSavedState(@NonNull Parcel source) {
        super(source);
    }

    /**
     * Creates a new saved state from a parcel with a class loader.
     *
     * @param source      The parcel
     * @param classLoader The class loader
     */
    protected AbstractSavedState(@NonNull Parcel source, @Nullable ClassLoader classLoader) {
        super(source, classLoader);
    }
}
