package io.syslogic.github.model;

public class SpinnerItem {

    private int id;
    private String name;
    private String value;

    public SpinnerItem(int id, String name, String value) {
        this.setId(id);
        this.setName(name);
        this.setValue(value);
    }

    /* Setters */
    public void setId(int value) {
        this.id = value;
    }
    public void setName(String value) {
        this.name = value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    /* Getters */
    public long getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public String getValue() {
        return this.value;
    }
}
