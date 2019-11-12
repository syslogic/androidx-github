package io.syslogic.github.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.ViewDataBinding;

import io.syslogic.github.R;
import io.syslogic.github.constants.Constants;
import io.syslogic.github.databinding.FragmentRepositoryBinding;
import io.syslogic.github.model.User;
import io.syslogic.github.task.DownloadListener;
import io.syslogic.github.retrofit.GithubClient;
import io.syslogic.github.model.Branch;
import io.syslogic.github.model.Repository;
import io.syslogic.github.task.DownloadTask;

import okhttp3.Headers;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import io.syslogic.github.constants.Constants;

/**
 * Repository Fragment
 * @author Martin Zeitler
 * @version 1.0.0
**/
public class RepositoryFragment extends BaseFragment implements DownloadListener {

    /** Log Tag */
    private static final String LOG_TAG = RepositoryFragment.class.getSimpleName();

    /** Data Binding */
    private FragmentRepositoryBinding mDataBinding;

    private Long itemId = 0L;

    public RepositoryFragment() {

    }

    @NonNull
    public static RepositoryFragment newInstance(@NonNull Long itemId) {
        RepositoryFragment fragment = new RepositoryFragment();
        fragment.setItemId(itemId);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                this.mDataBinding.toolbarDownload.buttonDownload.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                    }
                });
            }
        }
        return layout;
    }

    private void downloadBranchAsZip(@Nullable String branch) {
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

    /** needs to run on UiThread */
    @Override
    public void OnFileSize(@NonNull final String fileName, @NonNull final Long fileSize) {
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(getActivity() != null) {

                        String text;
                        if (fileSize > 0) {
                            text = String.format(Locale.getDefault(), getActivity().getResources().getString(R.string.text_file_size_known), fileSize);
                        } else {
                            text = getActivity().getResources().getString(R.string.text_file_size_unknown);
                        }

                        getDataBinding().toolbarDownload.textDownloadFilename.setText(fileName);
                        getDataBinding().toolbarDownload.textDownloadStatus.setText(text);
                        if (mDebug) {
                            Log.d(LOG_TAG, text);
                        }
                    }
                    switchToolbarView(1);
                }
            });
        }
    }

    @Override
    public void OnProgress(@NonNull final String fileName, @NonNull final Integer progress, @NonNull final Long fileSize) {
        if(getActivity() != null) {
            String text;
            if(fileSize > 0) {
                text = String.format(Locale.getDefault(), getActivity().getResources().getString(R.string.text_file_progress), progress, fileSize);
            } else {
                text = String.format(Locale.getDefault(), getActivity().getResources().getString(R.string.text_file_size_known), progress);
            }
            mDataBinding.toolbarDownload.textDownloadStatus.setText(text);
            // if (mDebug) {Log.d(LOG_TAG, text);}
        }
    }

    /** needs to run on UiThread */
    @Override
    public void OnFileExists(@NonNull final String fileName, @NonNull final Long fileSize) {
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(getActivity() != null) {

                        String text = String.format(Locale.getDefault(), getActivity().getResources().getString(R.string.text_file_size_known), fileSize);
                        getDataBinding().toolbarDownload.textDownloadFilename.setText(fileName);
                        getDataBinding().toolbarDownload.textDownloadStatus.setText(text);
                        if (mDebug) {Log.d(LOG_TAG, text);}

                        /* would need to be delayed */
                        switchToolbarView(0);
                    }
                }
            });
        }
    }

    @Override
    public void OnComplete(@NonNull final String fileName, @NonNull final Long fileSize, @NonNull Boolean success) {
        if(getActivity() != null ) {
            Resources res = getActivity().getResources();
            String text;
            if(success) {
                text = res.getString(R.string.text_download_complete);
            } else {
                text = res.getString(R.string.text_download_failed);
            }

            mDataBinding.toolbarDownload.textDownloadStatus.setText(text);
            if (mDebug) {Log.d(LOG_TAG, text);}

            /* would need to be delayed */
            switchToolbarView(0);
        }
    }

    @Override
    public void OnException(@NonNull String fileName, @NonNull final Exception e) {
        if (mDebug) {Log.e(LOG_TAG, "failed to save " + fileName + ".");}
        if(getActivity() != null) {
            if(("" + e.getMessage()).equals("write failed: ENOSPC (No space left on device)")) {
                String text = getActivity().getResources().getString(R.string.text_out_of_space);
                mDataBinding.toolbarDownload.textDownloadStatus.setText(text);
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        /* would need to be delayed */
                        switchToolbarView(0);
                    }
                });
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
                                mDataBinding.layoutRepository.notify();
                                setBranches(item);
                            }
                            break;
                        }

                        case 403: {
                            if (response.errorBody() != null) {
                                try {
                                    String errors = response.errorBody().string();
                                    JsonObject jsonObject = (new JsonParser()).parse(errors).getAsJsonObject();
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
                                Log.d(LOG_TAG, String.format(getContext().getResources().getString(R.string.debug_branch_list), repoName, items.size(), String.join(", ", elements)));
                            }

                            /* attempting to select branch master */
                            int defaultIndex = -1;
                            for(int i=0; i < items.size(); i++) {
                                if( items.get(i).getName().equals("master")) {
                                    if (mDebug) {Log.d(LOG_TAG, String.format(getContext().getResources().getString(R.string.debug_branch_master), repoName, i));}
                                    defaultIndex=i;
                                }
                            }

                            /* debug output */
                            if(mDebug && defaultIndex == -1) {
                                Log.d(LOG_TAG, repoName + " has no master branch.");
                            }

                            if(getActivity() != null && defaultIndex > 0) {
                                final int index = defaultIndex;
                                getDataBinding().toolbarDownload.spinnerBranch.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getDataBinding().toolbarDownload.spinnerBranch.setSelection(index, false);
                                    }
                                }, 100);
                            }
                        }
                        break;
                    }

                    case 403: {
                        if (response.errorBody() != null) {
                            try {
                                String errors = response.errorBody().string();
                                JsonObject jsonObject = (new JsonParser()).parse(errors).getAsJsonObject();
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

    private void downloadRepository(final Repository item, String archiveFormat, String branch) {
        final RepositoryFragment fragment = this;
        Call<ResponseBody> api = GithubClient.getArchiveLink(item.getOwner().getLogin(), item.getName(), archiveFormat, branch);
        if (mDebug) {Log.w(LOG_TAG, api.request().url() + "");}

        api.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.raw().code() == 200) {

                    Headers headers = response.headers();
                    String hostname = headers.get("Access-Control-Allow-Origin");
                    String filename = Objects.requireNonNull(headers.get("Content-Disposition")).split("=")[1];
                    Uri uri = Uri.parse(hostname + "/" + filename);

                    if (mDebug) {Log.w(LOG_TAG, uri.toString());}
                    DownloadTask task = new DownloadTask(filename, fragment);
                    task.execute(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                if (mDebug) {Log.e(LOG_TAG, "" + t.getMessage());}
            }
        });
    }

    private void downloadZipball(Repository item, String branch) {
        downloadRepository(item, "zipball", branch);
    }

    private void downloadTarball(Repository item, String branch) {
        downloadRepository(item, "tarball", branch);
    }

    private void switchToolbarView(Integer childIndex) {
        ViewFlipper view = mDataBinding.toolbarDownload.viewflipperDownload;
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
