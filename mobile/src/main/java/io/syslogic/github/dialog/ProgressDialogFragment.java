package io.syslogic.github.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.eclipse.jgit.lib.ProgressMonitor;

import io.syslogic.github.databinding.DialogProgressBinding;
import io.syslogic.github.R;

/**
 * Progress {@link BaseDialogFragment}
 *
 * @author Martin Zeitler
 */
public class ProgressDialogFragment extends BaseDialogFragment implements ProgressMonitor {

    /** Log Tag */
    public static final String LOG_TAG = ProgressDialogFragment.class.getSimpleName();

    /** layout resId kept for reference */
    @SuppressWarnings("unused") private final int resId = R.layout.dialog_progress;

    /** The Data-Binding */
    DialogProgressBinding mDataBinding;

    private String repoName = null;

    private boolean taskCancelled = false;
    private int totalTasks = 0;
    private int currentTask = 0;
    private int totalWork = 0;
    private int currentWork = 0;
    public ProgressDialogFragment() {}

    /** Inflate the data-binding */
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mDataBinding = DialogProgressBinding.inflate(inflater, container, false);
        this.mDataBinding.repositoryName.setText(this.repoName);
        this.mDataBinding.buttonCancel.setOnClickListener((view) -> this.taskCancelled = true);
        return this.mDataBinding.getRoot();
    }

    /** Being called early on... */
    public void setRepositoryName(String repoName) {
        this.repoName = repoName;
    }

    /** ProgressMonitor: Advise the monitor of the total number of subtasks. */
    @Override
    public void start(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    /** ProgressMonitor: Begin processing a single task. */
    @SuppressLint("SetTextI18n")
    @Override
    public void beginTask(String title, int totalWork) {
        this.currentTask++;
        this.totalWork = totalWork;
        this.currentWork = 0;
        if (mDebug) {
            Log.d(LOG_TAG, "beginTask " + this.currentTask + ": " + title + ": " + totalWork + " items");
        }
        requireActivity().runOnUiThread(() ->
                this.mDataBinding.textCurrentTaskTitle.setText(title));
    }

    /** ProgressMonitor: Denote that some work units have been completed. */
    @SuppressLint("SetTextI18n")
    @Override
    public void update(int completed) {
        this.currentWork += completed;
        String message = this.currentWork + " / " + this.totalWork;
        if (mDebug) {Log.d(LOG_TAG, "items complete: " + message);}
        requireActivity().runOnUiThread(() ->
                this.mDataBinding.textDownloadStatus.setText(message)
        );
    }

    /** ProgressMonitor: Finish the current task, so the next can begin. */
    @Override
    public void endTask() {
        if (mDebug) {Log.d(LOG_TAG, "endTask " + this.currentTask);}
        if (this.currentTask == this.totalTasks) {this.dismiss();}
    }

    /** Interface: ProgressMonitor. Check for user task cancellation. */
    @Override
    public boolean isCancelled() {
        if (this.taskCancelled) {this.dismiss();}
        return this.taskCancelled;
    }
}
