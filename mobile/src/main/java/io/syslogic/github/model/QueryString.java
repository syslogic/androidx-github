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
 *
 * @author Martin Zeitler
 */
@Entity(tableName = Constants.TABLE_QUERY_STRINGS)
public class QueryString extends BaseModel implements IContentProvider {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "title")
    private String title = null;

    @ColumnInfo(name = "search")
    private String search = null;

    @ColumnInfo(name = "condition")
    private String condition = "pushed";

    @ColumnInfo(name = "operator")
    private String operator = ">";

    @ColumnInfo(name = "parameter")
    private int parameter = 7;

    @ColumnInfo(name = "is_raw")
    private boolean isRaw = false;

    /** Constructor */
    public QueryString() {}

    /** Constructor, used by the QueryStringAdapter */
    @Ignore
    public QueryString(@NonNull Long id, @NonNull String title, @NonNull String queryString) {
        this.setId(id);
        this.setTitle(title);
        this.setSearch(queryString);
    }

    /** Constructor */
    @Ignore
    public QueryString(@NonNull Long id, @NonNull String title, @NonNull String condition, @NonNull String operator, int parameter) {
        this.setId(id);
        this.setTitle(title);
        this.setCondition(condition);
        this.setOperator(operator);
        this.setParameter(parameter);
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
    public String getSearch() {
        return this.search;
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
    public int getParameter() {
        return this.parameter;
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
    public void setSearch(@Nullable String value) {
        this.search = value;
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
    public void setParameter(int value) {
        this.parameter = value;
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
        calendar.add(Calendar.DAY_OF_YEAR, -this.getParameter());
        String isodate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
        return this.getSearch() + this.getDateFilter();
    }

    @NonNull
    public String getDateFilter() {
        if (this.getIsRaw() || this.getParameter() == 0) {return "";}
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -this.getParameter());
        String isodate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
        return "+" + this.getCondition() + ":" + this.getOperator() + isodate;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("id", this.getId());
        values.put("title", this.getTitle());
        values.put("condition", this.getCondition());
        values.put("operator", this.getOperator());
        values.put("parameter", this.getParameter());
        values.put("raw", this.toQueryString());
        return values;
    }

    @Override
    public QueryString fromContentValues(@NonNull ContentValues values) {
        this.setId(values.getAsLong("id"));
        this.setTitle(values.getAsString("title"));
        this.setCondition(values.getAsString("condition"));
        this.setOperator(values.getAsString("operator"));
        this.setParameter(values.getAsInteger("parameter"));
        return this;
    }
}
