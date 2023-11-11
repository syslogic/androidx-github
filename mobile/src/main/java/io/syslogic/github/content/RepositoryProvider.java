package io.syslogic.github.content;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.syslogic.github.Constants;
import io.syslogic.github.api.model.Repository;
import io.syslogic.github.api.room.Abstraction;
import io.syslogic.github.api.room.RepositoriesDao;

/**
 * Repository {@link ContentProvider}
 *
 * @author Martin Zeitler
 */
public class RepositoryProvider extends ContentProvider {

    private RepositoriesDao dao;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(Constants.ACCOUNT_TYPE + ".content.RepositoryProvider", "repositories", 1);
        uriMatcher.addURI(Constants.ACCOUNT_TYPE + ".content.RepositoryProvider", "repositories/#", 2);
    }

    @Override
    public boolean onCreate() {
        if (getContext() != null) {
            this.dao = Abstraction.getInstance(getContext()).repositoriesDao();
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
        /*
         * Choose the table to query and a sort order based on the code returned for the incoming
         * URI. Here, too, only the statements for table 3 are shown.
         */
        switch (uriMatcher.match(uri)) {


            // If the incoming URI was for all of table3
            case 1 -> {
                return dao.selectAll();
            }

            // If the incoming URI was for a single row
            case 2 -> {

                /*
                 * Because this URI was for a single row, the _ID value part is
                 * present. Get the last path segment from the URI; this is the _ID value.
                 * Then, append the value to the WHERE clause for the query.
                 */
                return dao.getCursor(Long.valueOf(Objects.requireNonNull(uri.getLastPathSegment())));
            }
            default -> throw new IllegalArgumentException("uriMatcher.match must be 1 or 2");
        }
    }

    @NonNull
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (values != null) {
            Repository item = new Repository().fromContentValues(values);
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
            Repository item = new Repository().fromContentValues(values);
            return dao.update(item);
        } else {
            return 0;
        }
    }
}