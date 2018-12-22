package io.syslogic.github.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import io.syslogic.github.R;
import io.syslogic.github.constants.Constants;
import io.syslogic.github.databinding.RepositoryFragmentBinding;
import io.syslogic.github.task.IDownloadTask;
import io.syslogic.github.network.GithubClient;
import io.syslogic.github.model.Repository;
import io.syslogic.github.task.DownloadTask;

import okhttp3.Headers;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepositoryFragment extends BaseFragment implements IDownloadTask {

    /** {@link Log} Tag */
    private static final String LOG_TAG = RepositoryFragment.class.getSimpleName();

    /** {@link RepositoryFragmentBinding} */
    private RepositoryFragmentBinding mDataBinding;

    private Boolean contentLoaded = false;

    private AppCompatImageButton mDownload;

    private BottomSheetBehavior<View> mBottomSheet;
    private AppCompatTextView mFileName;
    private AppCompatTextView mStatus;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.registerNetworkCallback(this.getContext(), this);
        } else {
            this.registerBroadcastReceiver(this.getContext());
        }
    }

    @NonNull
    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mDataBinding = RepositoryFragmentBinding.inflate(inflater, container, false);
        View layout = this.mDataBinding.getRoot();
        if(this.getContext() != null) {

            /* download button */
            this.mDownload = layout.findViewById(R.id.button_download);

            /* bottom sheet */
            View bottomSheet = layout.findViewById(R.id.dialog_bottom_sheet);
            this.mBottomSheet = BottomSheetBehavior.from(bottomSheet);
            this.mBottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);

            /* bottom sheet items */
            this.mFileName = layout.findViewById(R.id.text_download_filename);
            this.mStatus   = layout.findViewById(R.id.text_download_status);

            if(! isNetworkAvailable(this.getContext())) {
                this.onNetworkLost();
            } else {

                this.mDataBinding.webview.getSettings().setJavaScriptEnabled(true);
                this.mDataBinding.webview.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageCommitVisible (WebView view, String url) {
                        contentLoaded = true;
                    }
                });
                this.setRepository();

                /* the download button */
                this.mDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getActivity() != null) {
                            view.setClickable(false);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                                    downloadZipball(mDataBinding.getRepository());

                                } else {
                                    getActivity().requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUESTCODE_DOWNLOAD_ZIPBALL);
                                }
                            } else {
                                downloadZipball(mDataBinding.getRepository());
                            }
                        }
                    }
                });
            }
        }
        return layout;
    }

    private void setItemId(@NonNull Long value) {
        this.itemId = value;
    }

    @NonNull
    public Long getItemId() {
        return this.itemId;
    }

    @NonNull
    public RepositoryFragmentBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    public void onNetworkAvailable() {
        super.onNetworkAvailable();
        if(this.mDataBinding != null && this.mDataBinding.webview != null && !this.contentLoaded) {
            setRepository();
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
            if(requestCode == Constants.REQUESTCODE_DOWNLOAD_ZIPBALL) {
                this.downloadZipball(this.mDataBinding.getRepository());
            }
        }
    }

    @Override
    public void OnFileSize(final String fileName, final Long fileSize) {
        if(getActivity() != null) {
            String text;
            if(fileSize > 0) {
                text = String.format(Locale.getDefault(), getActivity().getResources().getString(R.string.text_file_size_known), fileSize);
            } else {
                text = getActivity().getResources().getString(R.string.text_file_size_unknown);
            }
            if (mDebug) {Log.d(LOG_TAG, text);}
            mFileName.setText(fileName);
            mStatus.setText(text);
            mBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void OnProgress( final String fileName, final Integer progress,  final Long fileSize) {
        if(getActivity() != null) {
            String text;
            if(fileSize > 0) {
                text = String.format(Locale.getDefault(), getActivity().getResources().getString(R.string.text_file_progress), progress, fileSize);
            } else {
                text = String.format(Locale.getDefault(), getActivity().getResources().getString(R.string.text_file_size_known), progress);
            }
            // if (mDebug) {Log.d(LOG_TAG, text);}
            mStatus.setText(text);
        }
    }

    @Override
    public void OnFileExists(final String fileName, final Long fileSize) {
        this.mDownload.setClickable(true);
        if(getActivity() != null) {
            /* needs to run on UiThread */
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                    String text = String.format(Locale.getDefault(), getActivity().getResources().getString(R.string.text_file_size_known), fileSize);
                    if (mDebug) {Log.d(LOG_TAG, text);}
                    mFileName.setText(fileName);
                    mStatus.setText(text);
                }
            });
        }
    }

    @Override
    public void OnComplete(final String fileName, final Long fileSize, Boolean success) {
        this.mDownload.setClickable(true);
        if(getActivity() != null && success) {
            mBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
            String text = getActivity().getResources().getString(R.string.text_file_downloaded);
            if (mDebug) {Log.d(LOG_TAG, text);}
            mStatus.setText(text);
        }
    }

    @Override
    public void OnException(String fileName, final Exception e) {
        if (mDebug) {Log.e(LOG_TAG, "failed to save " + fileName + ".", e);}
        this.mDownload.setClickable(true);
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                    mStatus.setText(e.getMessage());
                }
            });
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
                                getDataBinding().setRepository(item);
                                getDataBinding().notifyChange();
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
                                    if(mDebug) {Log.e(LOG_TAG, e.getMessage());}
                                }
                            }
                            break;
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Repository> call, @NonNull Throwable t) {
                    if (mDebug) {Log.e(LOG_TAG, t.getMessage());}
                }
            });
        }
    }

    private void downloadZipball(Repository item) {
        downloadRepository(item, "zipball", "master");
    }

    private void downloadZipball(Repository item, String gitRef) {
        downloadRepository(item, "zipball", gitRef);
    }

    private void downloadTarball(Repository item) {
        downloadRepository(item, "tarball", "master");
    }

    private void downloadTarball(Repository item, String gitRef) {
        downloadRepository(item, "tarball", gitRef);
    }

    private void downloadRepository(final Repository item, String archiveFormat, String gitRef) {

        this.mDownload.setClickable(false);

        final RepositoryFragment fragment = this;
        Call<ResponseBody> api = GithubClient.getArchiveLink(item.getOwner().getLogin(), item.getName(), archiveFormat, gitRef);
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (mDebug) {Log.e(LOG_TAG, t.getMessage());}
            }
        });
    }
}
