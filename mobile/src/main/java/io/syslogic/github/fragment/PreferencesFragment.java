package io.syslogic.github.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import io.syslogic.github.R;

/**
 * Preferences Fragment
 *
 * @author Martin Zeitler
 */
public class PreferencesFragment extends PreferenceFragmentCompat {

    /** Log Tag */
    @SuppressWarnings("unused")
    private static final String LOG_TAG = PreferencesFragment.class.getSimpleName();

    private static final int resId = R.xml.preferences_account;

    /** Constructor */
    public PreferencesFragment() {}

    /**
     * Called during {@link #onCreate(Bundle)} to supply the preferences for this fragment.
     * Subclasses are expected to call {@link #setPreferenceScreen(PreferenceScreen)} either
     * directly or via helper methods such as {@link #addPreferencesFromResource(int)}.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state.
     * @param rootKey            If non-null, this preference fragment should be rooted at the
     *                           {@link PreferenceScreen} with this key.
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        this.setPreferencesFromResource(resId, rootKey);
    }
}
