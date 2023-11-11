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

    /**
     * @param value a JSON string.
     * @return an instance of {@link String[]}.
     */
    @NonNull
    @TypeConverter
    public String[] fromJson(@NonNull String value) {
        return new Gson().fromJson(value, String[].class);
    }

    /**
     * @param value a string-array.
     * @return a JSON string.
     */
    @NonNull
    @TypeConverter
    public String toJson(@NonNull String[] value) {
        return new Gson().toJson(value);
    }
}
