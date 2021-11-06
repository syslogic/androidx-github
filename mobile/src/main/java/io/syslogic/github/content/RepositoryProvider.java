package io.syslogic.github.content;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

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
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selectionClause, String[] selectionArgs, String sortOrder) {
        return dao.selectAll();
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Repository item = new Repository().fromContentValues(values);
        long id = dao.insert(item);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selectionClause, String[] selectionArgs) {
        dao.deleteById(Long.valueOf(selectionArgs[0]));
        return 1;
    }

    public int update(Uri uri, ContentValues values, String selectionClause, String[] selectionArgs) {
        Repository item = new Repository().fromContentValues(values);
        dao.update(item);
        return 1;
    }
}