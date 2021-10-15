package io.syslogic.github.content;

import android.content.ContentValues;

import androidx.annotation.NonNull;

import io.syslogic.github.model.BaseModel;

public interface IContentProvider {

    ContentValues toContentValues();

    BaseModel fromContentValues(@NonNull ContentValues values);
}
