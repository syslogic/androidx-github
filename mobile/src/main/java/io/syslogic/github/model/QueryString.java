package io.syslogic.github.model;

import android.content.ContentValues;

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

import io.syslogic.github.Constants;
import io.syslogic.github.content.IContentProvider;

/**
 * Model: Query-String
 * @author Martin Zeitler
 */
@Entity(tableName = Constants.TABLE_QUERY_STRINGS)
public class QueryString extends BaseModel implements IContentProvider {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "title")
    private String title = null;

    @ColumnInfo(name = "topic")
    private String topic = null;

    @ColumnInfo(name = "condition")
    private String condition = "pushed";

    @ColumnInfo(name = "operator")
    private String operator = ">";

    @ColumnInfo(name = "days")
    private int days = 30;

    @ColumnInfo(name = "is_raw")
    private boolean isRaw = false;

    /** Constructor */
    public QueryString() {}

    /** Constructor, used by QueryStringAdapter */
    @Ignore
    public QueryString(@NonNull Long id, @NonNull String title, @NonNull String queryString) {
        this.setId(id);
        this.setTitle(title);
        this.setTopic(queryString);
    }

    /** Constructor */
    @Ignore
    public QueryString(@NonNull Long id, @NonNull String title, @NonNull String condition, @NonNull String operator, int days) {
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
    @Nullable
    public String getTitle() {
        return this.title;
    }

    @Bindable
    @Nullable
    public String getTopic() {
        return this.topic;
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
    public int getDays() {
        return this.days;
    }

    @Bindable
    public boolean getIsRaw() {
        return this.isRaw;
    }

    @Bindable
    public void setId(long value) {
        this.id = value;
    }

    @Bindable
    public void setTitle(@Nullable String value) {
        this.title = value;
    }

    @Bindable
    public void setTopic(@Nullable String value) {
        this.topic = value;
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
    public void setDays(int value) {
        this.days = value;
    }

    @Bindable
    public void setIsRaw(boolean value) {
        this.isRaw = value;
    }


    /** It generates a dated query-string. */
    @NonNull
    public String toQueryString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, - this.days);
        String isodate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
        return this.topic + "+" + this.condition + ":" + this.operator + isodate;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("id", this.getId());
        values.put("title", this.getTitle());
        values.put("condition", this.getCondition());
        values.put("operator", this.getOperator());
        values.put("days", this.getDays());
        values.put("raw", this.toQueryString());
        return values;
    }

    @Override
    public QueryString fromContentValues(@NonNull ContentValues values) {
        this.setId(values.getAsLong("id"));
        this.setTitle(values.getAsString("title"));
        this.setCondition(values.getAsString("condition"));
        this.setOperator(values.getAsString("operator"));
        this.setDays(values.getAsInteger("days"));
        return this;
    }
}
