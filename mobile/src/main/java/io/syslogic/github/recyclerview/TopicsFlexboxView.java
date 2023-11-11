package io.syslogic.github.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Topics {@link RecyclerView}
 *
 * @author Martin Zeitler
 */
public class TopicsFlexboxView extends RecyclerView {

    /** Constructor */
    public TopicsFlexboxView(@NonNull Context context) {
        super(context);
    }

    /** Constructor */
    public TopicsFlexboxView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        layoutManager.setJustifyContent(JustifyContent.FLEX_END);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        this.setLayoutManager(layoutManager);
    }
}
