package io.syslogic.github.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.syslogic.github.R;

/**
 * {@link NavHostFragment}
 *
 * @author Martin Zeitler
 */
public class NavHostFragment extends androidx.navigation.fragment.NavHostFragment {

    /** Constructor */
    public NavHostFragment() {}

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nav_host, container, false);
    }
}
