package io.syslogic.github.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.syslogic.github.R;

/**
 * The Main {@link NavHostFragment}.
 * @author Martin Zeitler
 * @version 1.0.0
**/
public class NavHostFragment extends androidx.navigation.fragment.NavHostFragment {

    public NavHostFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navhost, container, false);
    }
}
