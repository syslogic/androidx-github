package io.syslogic.github.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import io.syslogic.github.Constants;
import io.syslogic.github.R;
import io.syslogic.github.network.TokenHelper;

/**
 * General {@link PreferenceFragmentCompat}
 *
 * @author Martin Zeitler
 */
public class PreferencesFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    /** Log Tag */
    @SuppressWarnings("unused")
    private static final String LOG_TAG = PreferencesFragment.class.getSimpleName();

    private static final int resId = R.xml.preferences_general;

    /** {@link SharedPreferences} */
    @Nullable protected SharedPreferences prefs;

    /** Constructor */
    public PreferencesFragment() {}

    /** Called when a fragment is first attached to its context. */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        this.prefs.registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * Called during {@link #onCreate(Bundle)} to supply the preferences for this fragment.
     * Subclasses are expected to call {@link #setPreferenceScreen(PreferenceScreen)} either
     * directly or via helper methods such as {@link #addPreferencesFromResource(int)}.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     * @param rootKey If non-null, this preference fragment should be rooted at the {@link PreferenceScreen} with this key.
     */
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {

        this.setPreferencesFromResource(resId, rootKey);
        Preference pref = this.findPreference(Constants.PREFERENCE_KEY_ACCOUNT_SETTINGS);
        if (pref != null) {

            String accessToken = TokenHelper.getAccessToken(requireContext());
            if (accessToken != null) {
                pref.setSummary(R.string.summary_personal_access_token);
            }

            pref.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, new String[] {Constants.ACCOUNT_TYPE});

                if (accessToken != null) {
                    intent.setAction(Settings.ACTION_SYNC_SETTINGS);
                } else {
                    intent.setAction(Settings.ACTION_ADD_ACCOUNT);
                }
                try {
                    requireActivity().startActivityFromFragment(this, intent, 100);
                    return true;
                } catch (ActivityNotFoundException e) {
                    Log.e(LOG_TAG, e.getMessage());
                    return false;
                }
            });
        }
    }

    /**
     * Called when a shared preference is changed, added, or removed. This
     * may be called even if a preference is set to its existing value.
     *
     * <p>This callback will be run on your main thread.
     *
     * <p><em>Note: This callback will not be triggered when preferences are cleared
     * via {@link SharedPreferences.Editor#clear()}, unless targeting {@link Build.VERSION_CODES#R}
     * on devices running OS versions {@link Build.VERSION_CODES#R Android R} or later.</em>
     *
     * @param sharedPreferences The {@link SharedPreferences} that received the change.
     * @param key               The key of the preference that was changed, added, or removed. Apps targeting
     *                          {@link Build.VERSION_CODES#R} on devices running OS versions
     *                          {@link Build.VERSION_CODES#R Android R} or later, will receive
     *                          a {@code null} value when preferences are cleared.
     */
    @Override
    public void onSharedPreferenceChanged(@NonNull SharedPreferences sharedPreferences, @NonNull String key) {

    }
}
