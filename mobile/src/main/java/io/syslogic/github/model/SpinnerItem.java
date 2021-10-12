package io.syslogic.github.model;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

/**
 * Model: Spinner Item
 * @author Martin Zeitler
 */
public class SpinnerItem {

    private Integer id;
    private String name;
    private String value;

    /** Constructor */
    public SpinnerItem() {}

    /** Constructor */
    @Ignore
    public SpinnerItem(@NonNull Integer id, @NonNull String name, @NonNull String value) {
        this.setId(id);
        this.setName(name);
        this.setValue(value);
    }

    /* Setters */
    public void setId(@NonNull Integer value) {
        this.id = value;
    }
    public void setName(@NonNull String value) {
        this.name = value;
    }
    public void setValue(@NonNull String value) {
        this.value = value;
    }

    /* Getters */
    @NonNull
    public Integer getId() {
        return this.id;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public String getValue() {
        return this.value;
    }
}
