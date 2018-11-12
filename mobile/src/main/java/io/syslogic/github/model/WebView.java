package io.syslogic.github.model;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;

public class WebView extends BaseObservable {

    @BindingAdapter({"loadUrl"})
    public static void loadUrl(@NonNull android.webkit.WebView view, @NonNull String url) {
        view.loadUrl(url);
    }

}