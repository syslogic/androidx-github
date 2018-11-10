package io.syslogic.github.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;

public class WebView extends BaseObservable {

    @BindingAdapter({"loadUrl"})
    public static void loadUrl(android.webkit.WebView view, String url) {
        view.loadUrl(url);
    }

}