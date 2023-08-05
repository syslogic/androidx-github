package io.syslogic.github.api.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

/**
 * String[] {@link TypeConverter} for Room.
 *
 * @author Martin Zeitler
 */
public class StringArrayConverter {

    @TypeConverter
    public String[] fromJson(String value) {
        return new Gson().fromJson(value, String[].class);
    }

    @TypeConverter
    public String toJson(String[] value) {
        return new Gson().toJson(value);
    }
}
