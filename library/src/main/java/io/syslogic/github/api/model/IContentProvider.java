package io.syslogic.github.api.model;

import android.content.ContentProvider;
import android.content.ContentValues;

import androidx.annotation.NonNull;

import io.syslogic.github.api.model.BaseModel;

/**
 * Model Interface for {@link ContentProvider} items.
 *
 * @author Martin Zeitler
 */
public interface IContentProvider {

    /**
     * {@link BaseModel} from {@link ContentValues}.
     * @param values an instance of {@link ContentValues}.
     * @return an instance of {@link BaseModel}.
     */
    @NonNull BaseModel fromContentValues(@NonNull ContentValues values);

    /**
     * {@link BaseModel} to {@link ContentValues}.
     * @return an instance of {@link ContentValues}.
     */
    @NonNull ContentValues toContentValues();
}
