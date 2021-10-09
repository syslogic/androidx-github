package io.syslogic.github.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.syslogic.github.constants.Constants;

/**
 * Model: Topic
 * @author Martin Zeitler
 */
@Entity(tableName = Constants.TABLE_TOPICS)
public class Topic extends BaseModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "title")
    private String title = null;

    @ColumnInfo(name = "condition")
    private String condition = "pushed";

    @ColumnInfo(name = "operator")
    private String operator = ">";

    @ColumnInfo(name = "days")
    private int days = -90;

    @ColumnInfo(name = "query_string")
    private String queryString = null;

    /** Constructor */
    public Topic() {}

    /** Constructor */
    @Ignore
    public Topic(@NonNull Long id, @NonNull String title, @NonNull String queryString) {
        this.setId(id);
        this.setTitle(title);
        this.setQueryString(queryString);
    }

    /** Constructor */
    @Ignore
    public Topic(@NonNull Long id, @NonNull String title, @NonNull String condition, @NonNull String operator, int days) {
        this.setId(id);
        this.setTitle(title);
        this.setCondition(condition);
        this.setOperator(operator);
        this.setDays(days);
    }

    @Bindable
    public long getId() {
        return this.id;
    }

    @Bindable
    public int getDays() {
        return this.days;
    }

    @Bindable
    @Nullable
    public String getTitle() {
        return this.title;
    }

    @NonNull
    @Bindable
    public String getCondition() {
        return this.condition;
    }

    @NonNull
    @Bindable
    public String getOperator() {
        return this.operator;
    }

    @Bindable
    @Nullable
    public String getQueryString() {
        return this.queryString;
    }

    @Bindable
    public void setId(long value) {
        this.id = value;
    }

    @Bindable
    public void setDays(int value) {
        this.days = value;
    }

    @Bindable
    public void setTitle(@Nullable String value) {
        this.title = value;
    }

    @Bindable
    public void setCondition(@Nullable String value) {
        this.condition = value;
    }

    @Bindable
    public void setOperator(@Nullable String value) {
        this.operator = value;
    }

    @Bindable
    public void setQueryString(@Nullable String value) {
        this.queryString = value;
    }

    @NonNull
    public String toQueryString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, this.days);
        String isodate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
        return this.queryString + "+" + this.condition + ":" + this.operator + isodate;
    }
}
