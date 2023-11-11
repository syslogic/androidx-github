package io.syslogic.github.api.model;

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

import io.syslogic.github.api.Constants;

/**
 * Model: Query-String
 *
 * @author Martin Zeitler
 */
@Entity(tableName = Constants.TABLE_QUERY_STRINGS)
public class QueryString extends BaseModel implements IContentProvider {

    /** ID */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    /** Title */
    @ColumnInfo(name = "title")
    private String title = null;

    /** Search */
    @ColumnInfo(name = "search")
    private String search = null;

    /** Condition */
    @ColumnInfo(name = "condition")
    private String condition = "pushed";

    /** Operator */
    @ColumnInfo(name = "operator")
    private String operator = ">";

    /** Parameter */
    @ColumnInfo(name = "parameter")
    private int parameter = 7;

    /** Is raw? */
    @ColumnInfo(name = "is_raw")
    private boolean isRaw = false;

    /** Constructor */
    public QueryString() {}

    /**
     * Constructor, used by the QueryStringAdapter.
     * @param id the id of the query-string.
     * @param title the title of the query-string.
     * @param search the query-string itself.
     */
    @Ignore
    public QueryString(@NonNull Long id, @NonNull String title, @NonNull String search) {
        this.setId(id);
        this.setTitle(title);
        this.setSearch(search);
    }

    /**
     * Constructor
     * @param id the id of the query-string.
     * @param title the title of the query-string.
     * @param condition the query-string condition.
     * @param operator the query-string operator.
     * @param parameter the query-string parameter.
     */
    @Ignore
    public QueryString(@NonNull Long id, @NonNull String title, @NonNull String condition, @NonNull String operator, int parameter) {
        this.setId(id);
        this.setTitle(title);
        this.setCondition(condition);
        this.setOperator(operator);
        this.setParameter(parameter);
    }

    /**
     * Getter for id.
     * @return the id of the query-string.
     */
    @Bindable
    public long getId() {
        return this.id;
    }

    /**
     * Getter for title.
     * @return the title of the query-string.
     */
    @Bindable
    @Nullable
    public String getTitle() {
        return this.title;
    }

    /**
     * Getter for search.
     * @return the search of the query-string.
     */
    @Bindable
    @Nullable
    public String getSearch() {
        return this.search;
    }

    /**
     * Getter for condition.
     * @return the condition of the query-string.
     */
    @NonNull
    @Bindable
    public String getCondition() {
        return this.condition;
    }

    /**
     * Getter for operator.
     * @return the operator of the query-string.
     */
    @NonNull
    @Bindable
    public String getOperator() {
        return this.operator;
    }

    /**
     * Getter for parameter.
     * @return the parameter of the query-string.
     */
    @Bindable
    public int getParameter() {
        return this.parameter;
    }

    /**
     * Getter for isRaw.
     * @return is raw query-string.
     */
    @Bindable
    public boolean getIsRaw() {
        return this.isRaw;
    }

    /**
     * Setter for id.
     * @param value the id of the query-string.
     */
    @Bindable
    public void setId(long value) {
        this.id = value;
    }

    /**
     * Setter for title.
     * @param value the title of the query-string.
     */
    @Bindable
    public void setTitle(@Nullable String value) {
        this.title = value;
    }

    /**
     * Setter for search.
     * @param value the search of the query-string.
     */
    @Bindable
    public void setSearch(@Nullable String value) {
        this.search = value;
    }

    /**
     * Setter for condition.
     * @param value the condition of the query-string.
     */
    @Bindable
    public void setCondition(@Nullable String value) {
        this.condition = value;
    }

    /**
     * Setter for operator.
     * @param value the operator of the query-string.
     */
    @Bindable
    public void setOperator(@Nullable String value) {
        this.operator = value;
    }

    /**
     * Setter for parameter.
     * @param value the parameter of the query-string.
     */
    @Bindable
    public void setParameter(int value) {
        this.parameter = value;
    }

    /**
     * Setter for isRaw.
     * @param value is raw.
     */
    @Bindable
    public void setIsRaw(boolean value) {
        this.isRaw = value;
    }


    /**
     * It generates a dated query-string.
     * @return the generated query-string.
     */
    @NonNull
    public String toQueryString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -this.getParameter());
        return this.getSearch() + this.getDateFilter();
    }

    /**
     * It generates the date-filter string.
     * @return the generated date-filter string.
     */
    @NonNull
    public String getDateFilter() {
        if (this.getIsRaw() || this.getParameter() == 0) {return "";}
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -this.getParameter());
        String isodate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
        return "+" + this.getCondition() + ":" + this.getOperator() + isodate;
    }

    @NonNull
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

    @NonNull
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
