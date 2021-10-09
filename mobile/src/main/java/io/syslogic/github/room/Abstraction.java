package io.syslogic.github.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.model.Repository;
import io.syslogic.github.model.Topic;

/**
 * {@link RoomDatabase} Abstraction
 * @author Martin Zeitler
 */
@Database(version = 1, entities = {Topic.class, Repository.class})
public abstract class Abstraction extends RoomDatabase {

    @NonNull
    protected static final String LOG_TAG = Abstraction.class.getSimpleName();

    protected static final boolean mDebug = BuildConfig.DEBUG;

    private static Abstraction sInstance;

    private static final String fileName = "room.db";

    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    @NonNull
    public abstract RepositoriesDao repositoriesDao();

    @NonNull
    public abstract TopicsDao topicsDao();

    @NonNull
    public static Abstraction getInstance(@NonNull Context context) {
        if (sInstance == null) {
            Builder<Abstraction> builder = Room
                .databaseBuilder(context.getApplicationContext(), Abstraction.class, fileName)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                    }
                });

            /* Asset `src/main/assets/room.db` must match the current schema! */
            //builder.createFromAsset(fileName);
            sInstance = builder.build();
        }
        return sInstance;
    }
}