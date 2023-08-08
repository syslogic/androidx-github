package io.syslogic.github.fragment;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.ViewDataBinding;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.syslogic.github.Constants;
import io.syslogic.github.R;
import io.syslogic.github.api.GithubClient;
import io.syslogic.github.api.model.Branch;
import io.syslogic.github.api.model.Repository;
import io.syslogic.github.api.model.User;
import io.syslogic.github.databinding.FragmentRepositoryBinding;
import io.syslogic.github.dialog.ProgressDialogFragment;
import io.syslogic.github.network.TokenCallback;

import okhttp3.Headers;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository {@link BaseFragment}
 *
 * @author Martin Zeitler
 */
public class RepositoryFragment extends BaseFragment implements TokenCallback {

    /** Log Tag */
    @SuppressWarnings("unused")
    private static final String LOG_TAG = RepositoryFragment.class.getSimpleName();

    /** Layout resource ID kept for reference. */
    @SuppressWarnings("unused") private static final int resId = R.layout.fragment_repository;

    /** Data-Binding */
    FragmentRepositoryBinding mDataBinding;
    ProgressDialogFragment currentDialog;
    Long itemId = -1L;

    /** Constructor */
    public RepositoryFragment() {}

    @NonNull
    public static RepositoryFragment newInstance(long itemId) {
        RepositoryFragment fragment = new RepositoryFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.ARGUMENT_ITEM_ID, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.registerBroadcastReceiver();
        Bundle args = this.getArguments();
        if (args != null) {
            this.setItemId(args.getLong(Constants.ARGUMENT_ITEM_ID));
        }
    }

    @NonNull
    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.setDataBinding(FragmentRepositoryBinding.inflate(inflater, container, false));

        if (this.getContext() != null) {
            if (! isNetworkAvailable(this.getContext())) {
                this.onNetworkLost();
            } else {

                this.mDataBinding.webview.getSettings().setJavaScriptEnabled(true);
                this.mDataBinding.webview.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageCommitVisible (WebView view, String url) {
                        contentLoaded = true;
                        if (getDataBinding().viewflipperContent.getDisplayedChild() == 0) {
                            getDataBinding().viewflipperContent.showNext();
                        }
                    }
                });

                this.setRepository();
                this.mDataBinding.toolbarDownload.spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    int count = 0;
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (count > 0) {

                            WebView webview = getDataBinding().webview;
                            AppCompatSpinner spinner = getDataBinding().toolbarDownload.spinnerBranch;
                            String branch = spinner.getSelectedItem().toString();

                            String url = webview.getUrl();
                            if (url.equals("https://github.com/" + getDataBinding().getRepository().getFullName())) {
                                url += "/tree/" + branch;
                            } else {
                                Uri uri = Uri.parse(url);
                                String token = uri.getLastPathSegment();
                                if (token != null) {
                                    url = url.replace(token, branch);
                                }
                            }
                            url = url.replace("/tree/master", "");
                            if (mDebug) {Log.d(LOG_TAG, url);}
                            webview.loadUrl(url);
                        }
                        count++;
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

                /* The download button; TODO: consider tarball? */
                this.mDataBinding.toolbarDownload.buttonDownload.setOnClickListener(view -> {
                    String branch = getDataBinding().toolbarDownload.spinnerBranch.getSelectedItem().toString();
                    downloadBranchAsZip(branch);
                });

                /* The git clone button (experimental). */
                this.mDataBinding.toolbarDownload.buttonClone.setOnClickListener(view -> {
                    assert this.prefs != null;
                    String directory = this.prefs.getString(Constants.PREFERENCE_KEY_WORKSPACE_DIRECTORY, Environment.getExternalStorageDirectory() + "/Workspace");
                    String name = getDataBinding().getRepository().getName();
                    File destination = new File(directory + "/" + name);

                    // directory should be empty.
                    if (destination.exists()) {
                        if (! destination.delete()) {
                            Log.e(LOG_TAG, "destination directory not deleted");
                            return;
                        }
                    }
                    if (! destination.exists()) {
                        if (! destination.mkdir()) {
                            Log.e(LOG_TAG, "destination directory not created");
                            return;
                        }
                    }
                    if (destination.exists() && destination.isDirectory()) {
                        String branch = getDataBinding().toolbarDownload.spinnerBranch.getSelectedItem().toString();
                        gitClone(destination, branch);
                    }
                });
            }
        }
        return this.mDataBinding.getRoot();
    }

    /** git clone. */
    private void gitClone(@NonNull File destination, @Nullable String branch) {

        /* Attempting to display the checkout progress. */
        this.currentDialog = new ProgressDialogFragment();
        this.currentDialog.show(getChildFragmentManager(), ProgressDialogFragment.LOG_TAG);
        this.currentDialog.setRepositoryName(getRepoName());
        this.currentDialog.setLocalPath(destination.getAbsolutePath());

        Thread thread = new Thread(() -> {
            CloneCommand cmd = Git.cloneRepository()
                    .setURI(getRepoUrl())
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(getAccessToken(), ""))
                    .setProgressMonitor((ProgressMonitor) currentDialog)
                    .setDirectory(destination)
                    .setRemote("github");

            if (branch == null) {
                cmd.setCloneAllBranches(true);
            } else {
                cmd.setBranch(branch);
            }

            if (mDebug) {Log.d(LOG_TAG, "Cloning git repository " + getRepoName() + "...");}
            try {
                cmd.call();
            } catch (GitAPIException | JGitInternalException | NoSuchMethodError e) {
                String message = e.getMessage();
                if (mDebug) {Log.e(LOG_TAG, e.getMessage(), e);}
                requireActivity().runOnUiThread(() -> {
                    this.currentDialog.dismiss();
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
                });
            }
        });
        thread.start();
    }

    void downloadBranchAsZip(@Nullable String branch) {
        if (branch == null) {branch ="master";}
        downloadZipball(this.getDataBinding().getRepository(), branch);
    }

    private void setItemId(@NonNull Long value) {
        this.itemId = value;
    }

    @NonNull
    private String getRepoName() {
        return getDataBinding().getRepository().getFullName();
    }

    @NonNull
    private String getRepoUrl() {
        return getDataBinding().getRepository().getUrl()
                .replace("api.github.com/repos", "github.com") + ".git";
    }

    @NonNull
    public FragmentRepositoryBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    protected void setDataBinding(@NonNull ViewDataBinding binding) {
        this.mDataBinding = (FragmentRepositoryBinding) binding;
    }

    @Override
    public void onNetworkAvailable() {
        super.onNetworkAvailable();
        if (this.getContext() != null) {

            String token = this.getAccessToken();
            if (getCurrentUser() == null && token != null) {
                this.setUser(token, this);
            }

            if (this.mDataBinding != null && !this.contentLoaded) {
                setRepository();
            }
        }
    }

    @Override
    public void onNetworkLost() {
        super.onNetworkLost();
        if (this.getContext() != null) {
            Toast.makeText(getContext(), "device is offline", Toast.LENGTH_SHORT).show();
        }
    }

    private void setRepository() {

        if (this.itemId != 0) {

            Call<Repository> api = GithubClient.getRepository(this.itemId);
            if (mDebug) {Log.w(LOG_TAG, api.request().url() + "");}

            api.enqueue(new Callback<>() {

                @Override
                public void onResponse(@NonNull Call<Repository> call, @NonNull Response<Repository> response) {
                    switch (response.code()) {
                        case 200 -> {
                            if (response.body() != null) {
                                Repository item = response.body();
                                mDataBinding.setRepository(item);
                                setBranches(item);
                            }
                        }
                        case 403 -> {
                            if (response.errorBody() != null) {
                                try {
                                    String errors = response.errorBody().string();
                                    JsonObject jsonObject = JsonParser.parseString(errors).getAsJsonObject();
                                    String message = jsonObject.get("message").toString();
                                    if (mDebug) {
                                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                        Log.e(LOG_TAG, message);
                                    }
                                } catch (IOException e) {
                                    if (mDebug) {Log.e(LOG_TAG, "" + e.getMessage());}
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Repository> call, @NonNull Throwable t) {
                    if (mDebug) {
                        Log.e(LOG_TAG, "" + t.getMessage());
                    }
                }
            });
        }
    }

    public void setBranches(@NonNull Repository item) {

        Call<ArrayList<Branch>> api = GithubClient.getBranches(item.getOwner().getLogin(), item.getName());
        if (mDebug) {Log.w(LOG_TAG, api.request().url() + "");}
        final String repoName = item.getName();

        api.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<ArrayList<Branch>> call, @NonNull Response<ArrayList<Branch>> response) {
                switch (response.code()) {
                    case 200 -> {
                        if (response.body() != null && getContext() != null) {

                            /* Updating the branches */
                            ArrayList<Branch> items = response.body();
                            getDataBinding().setBranches(items);

                            /* Debug output */
                            if (mDebug && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                ArrayList<String> elements = new ArrayList<>();
                                for (int i = 0; i < items.size(); i++) {
                                    String name = items.get(i).getName();
                                    elements.add(i, name);
                                }
                                String formatString = getContext().getResources().getString(R.string.debug_branch_list);
                                Log.d(LOG_TAG, String.format(formatString, repoName, items.size(), String.join(", ", elements)));
                            }

                            /* Attempting to select branch master */
                            int defaultIndex = -1;
                            for (int i = 0; i < items.size(); i++) {
                                if (items.get(i).getName().equals("main") || items.get(i).getName().equals("master")) {
                                    if (mDebug) {
                                        String formatString = getContext().getResources().getString(R.string.debug_branch_master);
                                        Log.d(LOG_TAG, String.format(formatString, repoName, i));
                                    }
                                    defaultIndex = i;
                                }
                            }

                            /* Debug output */
                            if (mDebug && defaultIndex == -1) {
                                Log.d(LOG_TAG, repoName + " has no master branch.");
                            }

                            if (getActivity() != null && defaultIndex > 0) {
                                final int index = defaultIndex;
                                getDataBinding().toolbarDownload.spinnerBranch.postDelayed(() -> getDataBinding().toolbarDownload.spinnerBranch.setSelection(index, false), 100);
                            }
                        }
                    }
                    case 403 -> {
                        if (response.errorBody() != null) {
                            try {
                                String errors = response.errorBody().string();
                                JsonObject jsonObject = JsonParser.parseString(errors).getAsJsonObject();
                                String message = jsonObject.get("message").toString();
                                if (mDebug) {
                                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                    Log.e(LOG_TAG, message);
                                }
                            } catch (IOException e) {
                                if (mDebug) {
                                    Log.e(LOG_TAG, "" + e.getMessage());
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Branch>> call, @NonNull Throwable t) {
                if (mDebug) {
                    Log.e(LOG_TAG, "" + t.getMessage());
                }
            }
        });
    }

    void downloadZipball(Repository item, String branch) {
        downloadRepository(item, "zipball", branch);
    }

    @SuppressWarnings("unused")
    void downloadTarball(Repository item, String branch) {
        downloadRepository(item, "tarball", branch);
    }

    void downloadRepository(@NonNull final Repository item, final String archiveFormat, String branch) {
        String token = getAccessToken();
        assert token != null;
        Call<ResponseBody> api = GithubClient.getArchiveLink(token, item.getOwner().getLogin(), item.getName(), archiveFormat, branch);
        if (mDebug) {Log.d(LOG_TAG, api.request().url() + "");}

        api.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Headers headers = response.headers();
                switch (response.raw().code()) {

                    case 200: /* OkHttp will only enter this branch with redirects enabled (useless for the DownloadManager). */
                        break;

                    case 302: /* OkHttp needs redirects disabled, in order to enter this branch (as it should be). */
                        inspectDownload(headers.get("location"), archiveFormat);
                        break;

                    default:
                        if (mDebug) {
                            Log.e(LOG_TAG, response.raw().code() + " " + response.raw().message());
                        }
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                if (mDebug) {
                    Log.e(LOG_TAG, "" + t.getMessage());
                }
            }
        });
    }

    /* The DownloadManager needs the filename from the Content-Disposition. */
    void inspectDownload(final String url, final String archiveFormat) {

        Call<Void> api = GithubClient.getHead(url);
        if (mDebug) {Log.d(LOG_TAG, api.request().url() + "");}

        api.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.raw().code() == 200) {
                    Headers headers = response.headers();
                    String mimeType = "application/" + (archiveFormat.equals("zipball") ? "zip" : "tar+gzip");
                    String filename = URLUtil.guessFileName(url, headers.get("Content-Disposition"), mimeType);
                    enqueueDownload(url, filename, archiveFormat);
                } else {
                    if (mDebug) {
                        Log.e(LOG_TAG, response.raw().code() + " " + response.raw().message());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                if (mDebug) {
                    Log.e(LOG_TAG, "" + t.getMessage());
                }
            }
        });
    }

    /* The DownloadManager will deliver a Broadcast. */
    void enqueueDownload(final String url, final String filename, final String mimeType) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
            .setTitle(filename)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
            .addRequestHeader("Accept", mimeType)
            .setMimeType(mimeType);

        DownloadManager downloadManager = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {downloadManager.enqueue(request);}
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    void registerBroadcastReceiver() {
        if (getActivity() != null) {
            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, @NonNull Intent intent) {
                    String action = intent.getAction();
                    if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(downloadId);
                        if (getActivity() != null) {
                            DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                            if (downloadManager != null) {
                                Cursor c = downloadManager.query(query);
                                if (c.moveToFirst()) {
                                    int colStatus = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                                    int colLocalUri = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                                    if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(colStatus)) {
                                        String uri = c.getString(colLocalUri);
                                        if (mDebug) {Log.d(LOG_TAG, "" + uri);}
                                    }
                                }
                            }
                        }
                    }
                }
            };
            getActivity().registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
    }

    @SuppressWarnings("unused")
    public void showDownloads() {
        Intent intent = new Intent();
        intent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(intent);
    }

    @Override
    public void onLogin(@NonNull User item) {}
}
