package io.syslogic.github.adapter;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import io.syslogic.github.model.SpinnerItem;
import io.syslogic.github.model.Topic;
import io.syslogic.github.room.Abstraction;
import io.syslogic.github.room.TopicsDao;

/**
 * Topic Adapter
 * @author Martin Zeitler
 */
public class TopicAdapter extends BaseArrayAdapter {

    public TopicAdapter(@NonNull Context context) {
        super(context);
        TopicsDao dao = Abstraction.getInstance(context).topicsDao();
        Abstraction.executorService.execute(() -> {
            for (Topic topic : dao.getItems()) {
                assert topic.getTitle() != null;
                mItems.add(new SpinnerItem((int) topic.getId(), topic.getTitle(), topic.toQueryString()));
            }
            ((Activity) context).runOnUiThread(this::notifyDataSetChanged);
        });
    }
}
