package io.syslogic.github.content;

import android.content.ContentProvider;
import android.content.ContentValues;

import androidx.annotation.NonNull;

import io.syslogic.github.model.BaseModel;

/**
 * Model Interface for {@link ContentProvider} items.
 *
 * @author Martin Zeitler
 */
public interface IContentProvider {

    @NonNull ContentValues toContentValues();

    @NonNull BaseModel fromContentValues(@NonNull ContentValues values);
}
