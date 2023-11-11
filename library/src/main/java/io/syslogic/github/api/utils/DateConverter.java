package io.syslogic.github.api.utils;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Date {@link TypeConverter} for Room.
 *
 * @author Martin Zeitler
 */
public class DateConverter {

    /**
     * @param value a long date.
     * @return an instance of {@link Date}.
     */
    @NonNull
    @TypeConverter
    public Date fromLong(@NonNull Long value) {
        return new Date(value);
    }

    /**
     * @param value an instance of {@link Date}.
     * @return a long date.
     */
    @NonNull
    @TypeConverter
    public Long toLong(@NonNull Date value) {
        return value.getTime();
    }
}
