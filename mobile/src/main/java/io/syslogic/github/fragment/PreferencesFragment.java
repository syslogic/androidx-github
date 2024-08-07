package io.syslogic.github.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;

import java.io.File;
import java.util.Objects;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import io.syslogic.github.BuildConfig;
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
    private static final String LOG_TAG = PreferencesFragment.class.getSimpleName();

    /** Debug Output */
    static final boolean mDebug = BuildConfig.DEBUG;

    /** Layout resource ID. */
    private static final int resId = R.xml.preferences_general;

    /** {@link SharedPreferences} */
    @Nullable protected SharedPreferences prefs;

    /** Constructor */
    public PreferencesFragment() {}

    /** Called when the fragment is first attached to its context. */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        this.prefs.registerOnSharedPreferenceChangeListener(this);
    }

    /** Called when the fragment is being detached from its context. */
    @Override
    public void onDetach() {
        super.onDetach();
        if (this.prefs != null) {
            this.prefs.unregisterOnSharedPreferenceChangeListener(this);
        }
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

        /* Preference: Account Manager */
        Preference pref = this.findPreference(Constants.PREFERENCE_KEY_ACCOUNT_SETTINGS);
        if (pref != null) {
            String accessToken = TokenHelper.getAccessToken(requireActivity());
            //if (accessToken != null) {pref.setSummary(R.string.summary_personal_access_token_alt);}
            pref.setOnPreferenceClickListener(preference -> {

                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                /* Depending whether there is an access token... */
                if (accessToken != null) {
                    /* It will either open the sync-settings. */
                    String[] authorities = {Constants.ACCOUNT_TYPE + ".content.RepositoryProvider"};
                    intent.putExtra(Settings.EXTRA_AUTHORITIES, authorities);
                    intent.setAction(Settings.ACTION_SYNC_SETTINGS);
                } else {
                    /* Or it will add an account. */
                    String[] accountTypes = new String[] {Constants.ACCOUNT_TYPE};
                    intent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, accountTypes);
                    intent.setAction(Settings.ACTION_ADD_ACCOUNT);
                }
                try {
                    requireActivity().startActivityFromFragment(this, intent, 100);
                    return true;
                } catch (ActivityNotFoundException e) {
                    if (mDebug) {Log.e(LOG_TAG, "ActivityNotFoundException: " + e.getMessage());}
                    return false;
                }
            });
        }

        /* Preference: Workspace Directory */
        Preference pref2 = this.findPreference(Constants.PREFERENCE_KEY_WORKSPACE_DIRECTORY);
        if (pref2 != null) {
            if (pref2.getSummary() == null) {
                assert this.prefs != null;
                String directory = this.prefs.getString(Constants.PREFERENCE_KEY_WORKSPACE_DIRECTORY, Environment.getExternalStorageDirectory() + "/Workspace");
                File defaultDir = new File(directory);
                if (! defaultDir.exists()) {
                    if (! defaultDir.mkdir()) {

                        /* Try internal storage instead */
                        if (mDebug) {Log.w(LOG_TAG, "workspace not created: " + defaultDir.getAbsolutePath());}
                        defaultDir = new File(Objects.requireNonNull(requireContext().getExternalFilesDir(null)).toURI());
                        if (mDebug) {Log.d(LOG_TAG, "workspace using internal storage: " + defaultDir.getAbsolutePath());}
                    }
                }
                if (! directory.equals(defaultDir.getAbsolutePath())) {
                    this.prefs.edit().putString(Constants.PREFERENCE_KEY_WORKSPACE_DIRECTORY, defaultDir.getAbsolutePath()).apply();
                }
                pref2.setSummary(defaultDir.getAbsolutePath());
            }

            pref2.setOnPreferenceClickListener(preference -> {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // chooseDirectory();
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                 }
                return true;
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
     * @param preferences The {@link SharedPreferences} that received the change.
     * @param key         The key of the preference that was changed, added, or removed. Apps targeting
     *                    {@link Build.VERSION_CODES#R} on devices running OS versions
     *                    {@link Build.VERSION_CODES#R Android R} or later, will receive
     *                    a {@code null} value when preferences are cleared.
     */
    @Override
    public void onSharedPreferenceChanged(@NonNull SharedPreferences preferences, @Nullable String key) {
        if (mDebug) {Log.d(LOG_TAG, "onSharedPreferenceChanged " + key);}
    }

    private void chooseDirectory() {

        String defaultDir = Environment.getExternalStorageDirectory() + "/Workspace/";
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        Uri uri = Uri.parse(defaultDir);
        intent.setDataAndType(uri, DocumentsContract.Document.MIME_TYPE_DIR);
        try {
            requestFileChooserResult.launch(intent);
        } catch (ActivityNotFoundException e) {
            Log.e(LOG_TAG, "ActivityNotFoundException: " + e.getMessage());
        }
    }

    /** Register the permission-request callback. */
    final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // chooseDirectory();
                }
            });

    /** Register the file-chooser callback. */
    final ActivityResultLauncher<Intent> requestFileChooserResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {

                    // There are no request codes
                    Intent data = result.getData();

                } else if(result.getResultCode() == Activity.RESULT_CANCELED) {

                }
            });
}
