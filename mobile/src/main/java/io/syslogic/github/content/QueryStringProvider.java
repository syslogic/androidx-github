package io.syslogic.github.content;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.syslogic.github.api.model.QueryString;
import io.syslogic.github.api.room.Abstraction;
import io.syslogic.github.api.room.QueryStringsDao;

/**
 * Query-String {@link ContentProvider}
 *
 * @author Martin Zeitler
 */
public class QueryStringProvider extends ContentProvider {

    private QueryStringsDao dao;

    @Override
    public boolean onCreate() {
        if (getContext() != null) {
            this.dao = Abstraction.getInstance(getContext()).queryStringsDao();
        }
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @NonNull
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selectionClause, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return dao.selectAll();
    }

    @NonNull
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (values != null) {
            QueryString item = new QueryString().fromContentValues(values);
            long id = dao.insert(item);
            return ContentUris.withAppendedId(uri, id);
        } else {
            return uri;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selectionClause, @Nullable String[] selectionArgs) {
        if (selectionArgs != null && selectionArgs.length > 0) {
            return dao.deleteById(Long.valueOf(selectionArgs[0]));
        } else {
            return 0;
        }
    }

    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selectionClause, @Nullable String[] selectionArgs) {
        if (values != null) {
            QueryString item = new QueryString().fromContentValues(values);
            return dao.update(item);
        } else {
            return 0;
        }
    }
}