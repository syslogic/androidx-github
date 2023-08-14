package io.syslogic.github.api.utils;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import com.google.gson.Gson;

/**
 * String[] {@link TypeConverter} for Room.
 *
 * @author Martin Zeitler
 */
public class StringArrayConverter {

    @NonNull
    @TypeConverter
    public String[] fromJson(@NonNull String value) {
        return new Gson().fromJson(value, String[].class);
    }

    @NonNull
    @TypeConverter
    public String toJson(@NonNull String[] value) {
        return new Gson().toJson(value);
    }
}
