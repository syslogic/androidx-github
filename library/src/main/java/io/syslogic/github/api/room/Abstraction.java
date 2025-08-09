package io.syslogic.github.api.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.syslogic.github.api.model.License;
import io.syslogic.github.api.model.Owner;
import io.syslogic.github.api.model.QueryString;
import io.syslogic.github.api.model.Repository;
import io.syslogic.github.api.model.Workflow;

/**
 * {@link RoomDatabase} Abstraction
 *
 * @author Martin Zeitler
 */
@Database(version = 1, entities = {QueryString.class, Repository.class, Workflow.class, License.class, Owner.class})
public abstract class Abstraction extends RoomDatabase {

    /** The log tag. */
    @NonNull
    protected static final String LOG_TAG = Abstraction.class.getSimpleName();

    private static final int NUMBER_OF_THREADS = 4;

    /** The {@link ExecutorService} being used for all database operations. */
    public static final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /** The file-name of the database in local storage. */
    private static final String fileName = "room.db";

    /** {@link Room} database instance handle. */
    private static Abstraction sInstance;

    /**
     * Abstract {@link androidx.room.Dao} for {@link QueryString}.
     * @return in instance of {@link QueryStringsDao}.
     */
    @Nullable public abstract QueryStringsDao queryStringsDao();

    /**
     * Abstract {@link androidx.room.Dao} for {@link Repository}.
     * @return in instance of {@link RepositoriesDao}.
     */
    @Nullable public abstract RepositoriesDao repositoriesDao();

    /**
     * Abstract {@link androidx.room.Dao} for {@link Workflow}.
     * @return in instance of {@link WorkflowsDao}.
     */
    @Nullable public abstract WorkflowsDao workflowsDao();

    /**
     * Abstract {@link androidx.room.Dao} for {@link License}.
     * @return in instance of {@link LicensesDao}.
     */
    @Nullable public abstract LicensesDao licensesDao();

    /**
     * Abstract {@link androidx.room.Dao} for {@link Owner}.
     * @return in instance of {@link OwnersDao}.
     */
    @Nullable public abstract OwnersDao ownersDao();

    /**
     * Asset `src/main/assets/room.db` must match the current schema version,
     * else this will also result in: "Room cannot verify the data integrity".
     * @param context any Content class.
     * @return an instance of {@link Room} database.
     */
    @NonNull
    public static Abstraction getInstance(@NonNull Context context) {
        if (sInstance == null) {
            Builder<Abstraction> builder = Room
                .databaseBuilder(context.getApplicationContext(), Abstraction.class, fileName)
                .createFromAsset(fileName);
            sInstance = builder.build();
        }
        return sInstance;
    }
}
