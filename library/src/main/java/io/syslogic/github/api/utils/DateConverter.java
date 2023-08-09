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

    @TypeConverter
    public Date fromLong(Long value) {
        return new Date(value);
    }

    @TypeConverter
    public Long toLong(@NonNull Date value) {
        return value.getTime();
    }
}
