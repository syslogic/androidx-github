package io.syslogic.github.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
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
import android.widget.ViewFlipper;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.ViewDataBinding;

import io.syslogic.github.R;
import io.syslogic.github.constants.Constants;
import io.syslogic.github.databinding.FragmentRepositoryBinding;
import io.syslogic.github.model.User;
import io.syslogic.github.retrofit.GithubClient;
import io.syslogic.github.model.Branch;
import io.syslogic.github.model.Repository;

import okhttp3.Headers;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository Fragment
 * @author Martin Zeitler
 */
public class RepositoryFragment extends BaseFragment {

    /** Log Tag */
    private static final String LOG_TAG = RepositoryFragment.class.getSimpleName();

    /** Data Binding */
    FragmentRepositoryBinding mDataBinding;

    private Long itemId = 0L;

    public RepositoryFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.registerBroadcastReceiver();
        Bundle args = this.getArguments();
        if(itemId == 0 && args != null) {
            this.setItemId(args.getLong(Constants.ARGUMENT_ITEM_ID));
        }
    }

    @NonNull
    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.setDataBinding(FragmentRepositoryBinding.inflate(inflater, container, false));
        View layout = this.mDataBinding.getRoot();

        if(this.getContext() != null) {

            if(! isNetworkAvailable(this.getContext())) {
                this.onNetworkLost();
            } else {

                this.mDataBinding.webview.getSettings().setJavaScriptEnabled(true);
                this.mDataBinding.webview.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageCommitVisible (WebView view, String url) {
                        contentLoaded = true;
                        if(getDataBinding().viewflipperContent.getDisplayedChild() == 0) {
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
                            if(url.equals("https://github.com/" + getDataBinding().getRepository().getFullName())) {
                                url += "/tree/" + branch;
                            } else {
                                Uri uri = Uri.parse(url);
                                String token = uri.getLastPathSegment();
                                if(token != null) {
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

                /* the download button; TODO: consider tarball. */
                this.mDataBinding.toolbarDownload.buttonDownload.setOnClickListener(view -> {

                    Activity activity = getActivity();
                    if (activity != null) {
                        String branch = getDataBinding().toolbarDownload.spinnerBranch.getSelectedItem().toString();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                downloadBranchAsZip(branch);
                            } else {
                                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUESTCODE_DOWNLOAD_ZIPBALL);
                            }
                        } else {
                            downloadBranchAsZip(branch);
                        }
                    }
                });
            }
        }
        return layout;
    }

    void downloadBranchAsZip(@Nullable String branch) {
        if(branch == null) {branch ="master";}
        downloadZipball(this.getDataBinding().getRepository(), branch);
    }

    @NonNull
    private Long getItemId() {
        return this.itemId;
    }

    private void setItemId(@NonNull Long value) {
        this.itemId = value;
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
    protected void setCurrentUser(@Nullable User value) {
        this.currentUser = value;
    }

    @Override
    public void onNetworkAvailable() {
        super.onNetworkAvailable();
        if(this.getContext() != null) {

            String token = this.getAccessToken(this.getContext());
            if (getCurrentUser() == null && token != null) {
                this.setUser(token, this);
            }

            if(this.mDataBinding != null && !this.contentLoaded) {
                setRepository();
            }
        }
    }

    @Override
    public void onNetworkLost() {
        super.onNetworkLost();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            AppCompatSpinner spinner = getDataBinding().toolbarDownload.spinnerBranch;
            Repository repo = getDataBinding().getRepository();
            String branch = "master";

            if(spinner.getAdapter().getCount() > 0) {
                branch = spinner.getSelectedItem().toString();
            }
            switch(requestCode) {
                case 501: this.downloadZipball(repo, branch); break;
                case 502: this.downloadTarball(repo, branch);  break;
            }
        }
    }

    private void setRepository() {

        if(this.itemId != 0) {

            Call<Repository> api = GithubClient.getRepository(this.itemId);
            if (mDebug) {Log.w(LOG_TAG, api.request().url() + "");}

            api.enqueue(new Callback<Repository>() {

                @Override
                public void onResponse(@NonNull Call<Repository> call, @NonNull Response<Repository> response) {
                    switch(response.code()) {

                        case 200: {
                            if (response.body() != null) {
                                Repository item = response.body();
                                mDataBinding.setRepository(item);
                                setBranches(item);
                            }
                            break;
                        }

                        case 403: {
                            if (response.errorBody() != null) {
                                try {
                                    String errors = response.errorBody().string();
                                    JsonObject jsonObject = new JsonParser().parse(errors).getAsJsonObject();
                                    String message = jsonObject.get("message").toString();
                                    if(mDebug) {
                                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                        Log.e(LOG_TAG, message);
                                    }
                                } catch (IOException e) {
                                    if(mDebug) {Log.e(LOG_TAG, "" + e.getMessage());}
                                }
                            }
                            break;
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Repository> call, @NonNull Throwable t) {
                    if (mDebug) {Log.e(LOG_TAG, "" + t.getMessage());}
                }
            });
        }
    }

    public void setBranches(@NonNull Repository item) {

        Call<ArrayList<Branch>> api = GithubClient.getBranches(item.getOwner().getLogin(), item.getName());
        if (mDebug) {Log.w(LOG_TAG, api.request().url() + "");}
        final String repoName = item.getName();

        api.enqueue(new Callback<ArrayList<Branch>>() {

            @Override
            public void onResponse(@NonNull Call<ArrayList<Branch>> call, @NonNull Response<ArrayList<Branch>> response) {
                switch(response.code()) {

                    case 200: {
                        if (response.body() != null && getContext() != null) {

                            /* updating the branches */
                            ArrayList<Branch> items = response.body();
                            getDataBinding().setBranches(items);

                            /* debug output */
                            if (mDebug && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                ArrayList<String> elements = new ArrayList<>();
                                for(int i=0; i < items.size(); i++) {
                                    String name = items.get(i).getName();
                                    elements.add(i, name);
                                }
                                String formatString = getContext().getResources().getString(R.string.debug_branch_list);
                                Log.d(LOG_TAG, String.format(formatString, repoName, items.size(), String.join(", ", elements)));
                            }

                            /* attempting to select branch master */
                            int defaultIndex = -1;
                            for(int i=0; i < items.size(); i++) {
                                if( items.get(i).getName().equals("master")) {
                                    if (mDebug) {
                                        String formatString = getContext().getResources().getString(R.string.debug_branch_master);
                                        Log.d(LOG_TAG, String.format(formatString, repoName, i));
                                    }
                                    defaultIndex=i;
                                }
                            }

                            /* debug output */
                            if(mDebug && defaultIndex == -1) {
                                Log.d(LOG_TAG, repoName + " has no master branch.");
                            }

                            if(getActivity() != null && defaultIndex > 0) {
                                final int index = defaultIndex;
                                getDataBinding().toolbarDownload.spinnerBranch.postDelayed(() ->
                                    getDataBinding().toolbarDownload.spinnerBranch.setSelection(index, false),
                                   100
                                );
                            }
                        }
                        break;
                    }

                    case 403: {
                        if (response.errorBody() != null) {
                            try {
                                String errors = response.errorBody().string();
                                JsonObject jsonObject = new JsonParser().parse(errors).getAsJsonObject();
                                String message = jsonObject.get("message").toString();
                                if(mDebug) {
                                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                    Log.e(LOG_TAG, message);
                                }
                            } catch (IOException e) {
                                if(mDebug) {Log.e(LOG_TAG, "" + e.getMessage());}
                            }
                        }
                        break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Branch>> call, @NonNull Throwable t) {
                if (mDebug) {Log.e(LOG_TAG, "" + t.getMessage());}
            }
        });
    }

    void downloadZipball(Repository item, String branch) {
        downloadRepository(item, "zipball", branch);
    }

    void downloadTarball(Repository item, String branch) {
        downloadRepository(item, "tarball", branch);
    }

    void downloadRepository(@NonNull final Repository item, final String archiveFormat, String branch) {

        Call<ResponseBody> api = GithubClient.getArchiveLink(getAccessToken(getContext()), item.getOwner().getLogin(), item.getName(), archiveFormat, branch);
        if (mDebug) {Log.d(LOG_TAG, api.request().url() + "");}

        api.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Headers headers = response.headers();
                switch(response.raw().code()) {

                    case 200: /* OkHttp will only enter this branch with redirects enabled (useless for the DownloadManager). */
                        break;

                    case 302: /* OkHttp needs redirects disabled, in order to enter this branch (as it should be). */
                        inspectDownload(headers.get("location"), archiveFormat);
                        break;

                    default:
                        if (mDebug) {Log.e(LOG_TAG, response.raw().code() + " " + response.raw().message());}
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                if (mDebug) {Log.e(LOG_TAG, "" + t.getMessage());}
            }
        });
    }

    /* The DownloadManager needs the filename from the Content-Disposition. */
    void inspectDownload(final String url, final String archiveFormat) {

        Call<Void> api = GithubClient.getHead(url);
        if (mDebug) {Log.d(LOG_TAG, api.request().url() + "");}

        api.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                switch(response.raw().code()) {

                    case 200:
                        Headers headers = response.headers();
                        String mimeType = "application/" + (archiveFormat.equals("zipball") ? "zip" : "tar+gzip");
                        String filename = URLUtil.guessFileName(url, headers.get("Content-Disposition"), mimeType);
                        enqueueDownload(url, filename, archiveFormat);
                        break;

                    default:
                        if (mDebug) {Log.e(LOG_TAG, response.raw().code() + " " + response.raw().message());}
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                if (mDebug) {Log.e(LOG_TAG, "" + t.getMessage());}
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

        if (getActivity() != null) {
            DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            if (downloadManager != null) {downloadManager.enqueue(request);}
        }
    }

    void registerBroadcastReceiver() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, @NonNull Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    if(getActivity() != null) {
                        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                        if(downloadManager != null) {
                            Cursor c = downloadManager.query(query);
                            if (c.moveToFirst()) {
                                if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                                    String uri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                                    if (mDebug) {Log.d(LOG_TAG, "" + uri);                                   }
                                }
                            }
                        }
                    }
                }
            }
        };

        if(getActivity() != null) {
            getActivity().registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
    }

    public void showDownloads() {
        Intent intent = new Intent();
        intent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(intent);
    }

    private void switchToolbarView(@NonNull Integer childIndex) {
        ViewFlipper view = this.mDataBinding.toolbarDownload.viewflipperDownload;
        int index = view.getDisplayedChild();
        switch(childIndex) {
            case 0: if(index != childIndex) {view.showPrevious();} break;
            case 1: if(index != childIndex) {view.showNext();} break;
        }
    }

    @Override
    public void onLogin(@NonNull User item) {

    }
}
