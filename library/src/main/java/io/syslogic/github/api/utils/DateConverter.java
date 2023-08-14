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

    @NonNull
    @TypeConverter
    public Date fromLong(@NonNull Long value) {
        return new Date(value);
    }

    @NonNull
    @TypeConverter
    public Long toLong(@NonNull Date value) {
        return value.getTime();
    }
}
