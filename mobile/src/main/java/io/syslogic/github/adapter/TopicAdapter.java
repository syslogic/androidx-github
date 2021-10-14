package io.syslogic.github.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import io.syslogic.github.model.SpinnerItem;
import io.syslogic.github.model.Topic;
import io.syslogic.github.room.Abstraction;
import io.syslogic.github.room.TopicsDao;

/**
 * Topic Adapter
 * @author Martin Zeitler
 */
public class TopicAdapter extends BaseArrayAdapter {

    @NonNull
    protected static final String LOG_TAG = TopicAdapter.class.getSimpleName();

    public TopicAdapter(@NonNull Context context) {
        super(context);
        TopicsDao dao = Abstraction.getInstance(context).topicsDao();
        Abstraction.executorService.execute(() -> {
            final Activity activity = ((Activity) context);
            try {
                List<Topic> items = dao.getItems();
                for (Topic topic : items) {
                    if (topic.getTitle() != null) {
                        mItems.add(new SpinnerItem((int) topic.getId(), topic.getTitle(), topic.toQueryString()));
                    }
                }
                activity.runOnUiThread(this::notifyDataSetChanged);
            } catch (IllegalStateException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        });
    }
}
