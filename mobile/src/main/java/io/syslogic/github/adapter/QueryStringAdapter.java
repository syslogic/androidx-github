package io.syslogic.github.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import io.syslogic.github.model.SpinnerItem;
import io.syslogic.github.model.QueryString;
import io.syslogic.github.room.Abstraction;
import io.syslogic.github.room.QueryStringsDao;

/**
 * Query-String Adapter
 * @author Martin Zeitler
 */
public class QueryStringAdapter extends BaseArrayAdapter {

    @NonNull
    static final String LOG_TAG = QueryStringAdapter.class.getSimpleName();

    public QueryStringAdapter(@NonNull Context context) {
        super(context);
        QueryStringsDao dao = Abstraction.getInstance(context).queryStringsDao();
        Abstraction.executorService.execute(() -> {
            final Activity activity = ((Activity) context);
            try {
                List<QueryString> items = dao.getItems();
                for (QueryString queryString : items) {
                    if (queryString.getTitle() != null) {
                        mItems.add(new SpinnerItem((int) queryString.getId(), queryString.getTitle(), queryString.toQueryString()));
                    }
                }
                activity.runOnUiThread(this::notifyDataSetChanged);
            } catch (IllegalStateException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        });
    }
}
