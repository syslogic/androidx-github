package io.syslogic.github.model;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

/**
 * Model: WebView
 * @author Martin Zeitler
 */
public class WebView {

    @BindingAdapter({"loadUrl"})
    public static void loadUrl(@NonNull android.webkit.WebView view, @NonNull String url) {
        view.loadUrl(url);
    }
}
