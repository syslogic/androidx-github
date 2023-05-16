package io.syslogic.github.dialog;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.R;

/**
 * Base {@link DialogFragment}
 *
 * @author Martin Zeitler
 */
abstract public class BaseDialogFragment extends DialogFragment {

    /** Debug Output */
    protected static final boolean mDebug = BuildConfig.DEBUG;

    @Nullable protected ViewDataBinding mDataBinding = null;
    protected SharedPreferences prefs;
    @Nullable protected Dialog dialog;

    /** By default the dialog can be closed by touching outside of it */
    protected boolean cancelOnTouchOutSide = true;

    public BaseDialogFragment() {}

    /**
     * onCreate()
     * @param savedInstanceState reference to the Bundle object, which is being passed into the method.
    **/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity());
    }

    /** classes which extend this class must implement method onCreateView, due to data-binding: */
    @Nullable
    abstract public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * onCreateDialog()
     * @param savedInstanceState reference to the Bundle object, which is being passed into the method.
    **/
    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        this.dialog = new Dialog(this.requireContext());
        this.dialog.setCanceledOnTouchOutside(this.cancelOnTouchOutSide);
        if (this.dialog.getWindow() != null) {
            this.dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.dialog.getWindow().setNavigationBarColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
        }
        return this.dialog;
    }
}
