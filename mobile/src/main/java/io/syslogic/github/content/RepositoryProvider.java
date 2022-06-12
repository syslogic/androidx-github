package io.syslogic.github.content;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;

import io.syslogic.github.model.Repository;
import io.syslogic.github.room.Abstraction;
import io.syslogic.github.room.RepositoriesDao;

/**
 * Repository {@link ContentProvider}
 *
 * @author Martin Zeitler
 */
public class RepositoryProvider extends ContentProvider {

    private RepositoriesDao dao;

    @Override
    public boolean onCreate() {
        this.dao = Abstraction.getInstance(getContext()).repositoriesDao();
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @NonNull
    @Override
    public Cursor query(@NonNull Uri uri, @NonNull String[] projection, @NonNull String selectionClause, @NonNull String[] selectionArgs, @NonNull String sortOrder) {
        return dao.selectAll();
    }

    @NonNull
    @Override
    public Uri insert(@NonNull Uri uri, @NonNull ContentValues values) {
        Repository item = new Repository().fromContentValues(values);
        long id = dao.insert(item);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @NonNull String selectionClause, @NonNull String[] selectionArgs) {
        dao.deleteById(Long.valueOf(selectionArgs[0]));
        return 1;
    }

    public int update(@NonNull Uri uri, @NonNull ContentValues values, @NonNull String selectionClause, @NonNull String[] selectionArgs) {
        Repository item = new Repository().fromContentValues(values);
        dao.update(item);
        return 1;
    }
}