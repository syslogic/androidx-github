package io.syslogic.github.content;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import io.syslogic.github.model.QueryString;
import io.syslogic.github.room.Abstraction;
import io.syslogic.github.room.QueryStringsDao;

/**
 * Query-String {@link ContentProvider}
 *
 * @author Martin Zeitler
 */
public class QueryStringProvider extends ContentProvider {

    private QueryStringsDao dao;

    @Override
    public boolean onCreate() {
        this.dao = Abstraction.getInstance(getContext()).queryStringsDao();
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
        QueryString item = new QueryString().fromContentValues(values);
        long id = dao.insert(item);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selectionClause, String[] selectionArgs) {
        dao.deleteById(Long.valueOf(selectionArgs[0]));
        return 1;
    }

    public int update(Uri uri, ContentValues values, String selectionClause, String[] selectionArgs) {
        QueryString item = new QueryString().fromContentValues(values);
        dao.update(item);
        return 1;
    }
}