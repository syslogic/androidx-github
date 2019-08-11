package io.syslogic.github.task;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.constants.Constants;

import okhttp3.ResponseBody;

/**
 * Download Task
 * @author Martin Zeitler
 * @version 1.0.0
**/
public class DownloadTask extends AsyncTask<ResponseBody, Pair<Integer, Long>, Bundle> {

    /** {@link Log} Tag */
    private static final String LOG_TAG = DownloadTask.class.getSimpleName();

    /** Debug Output */
    private static final boolean mDebug = BuildConfig.DEBUG;

    /** Output Filename */
    private final String fileName;

    /** Callback Listener */
    private DownloadListener listener;

    /** Constructor */
    public DownloadTask(@NonNull String fileName, @NonNull DownloadListener listener) {
        this.fileName = fileName;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected final void onProgressUpdate(@NonNull Pair<Integer, Long>[] values) {
        if(values.length > 0 && values[0].first != null && values[0].second != null) {
            if (this.listener != null) {
                this.listener.OnProgress(this.fileName, values[0].first, values[0].second);
                this.logPercent(values[0].first, values[0].second);
            }
        }
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    protected Bundle doInBackground(@NonNull ResponseBody... urls) {

        Bundle result = new Bundle();
        boolean success = false;
        Long fileSize = -1L;

        try {

            /* determine the remote file-size */
            ResponseBody body = urls[0];
            fileSize = body.contentLength();

            File destinationFile = this.getDestination();
            if (! destinationFile.exists()) {

                if(this.listener != null) {
                    this.listener.OnFileSize(this.fileName, fileSize);
                }

                OutputStream outputStream = null;
                InputStream inputStream = null;

                try {

                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(destinationFile);
                    byte data[] = new byte[4096];
                    int progress = 0; int count;

                    while ((count = inputStream.read(data)) != -1) {

                        outputStream.write(data, 0, count);
                        progress += count;

                        //noinspection unchecked
                        publishProgress(new Pair<>(progress, fileSize));
                    }

                    if(fileSize == -1) {fileSize = destinationFile.length();}

                    outputStream.flush();
                    success = true;

                } catch (IOException e) {
                    if(this.listener != null) {
                        this.listener.OnException(this.fileName, e);
                    }
                } finally {
                    if(inputStream  != null) {inputStream.close();}
                    if(outputStream != null) {outputStream.close();}
                    body.close();
                }

            } else if(this.listener != null) {
                this.listener.OnFileExists(destinationFile.getName(), destinationFile.length());
            }

        } catch(Exception e) {
            if(this.listener != null) {
                this.listener.OnException(this.fileName, e);
            }
        }

        result.putBoolean(Constants.ARGUMENT_TASK_SUCCESS, success);
        result.putString(Constants.ARGUMENT_FILE_NAME, this.fileName);
        result.putLong(Constants.ARGUMENT_FILE_SIZE, fileSize);
        return result;
    }

    @Override
    protected void onPostExecute(@NonNull Bundle result) {

        boolean success = result.getBoolean(Constants.ARGUMENT_TASK_SUCCESS, false);
        String fileName = result.getString(Constants.ARGUMENT_FILE_NAME, this.fileName);
        Long fileSize   = result.getLong(Constants.ARGUMENT_FILE_SIZE,-1L);

        if (this.listener != null && success) {
            this.listener.OnComplete(fileName, fileSize, success);
        }
    }

    @SuppressWarnings("deprecation")
    private File getDestination() {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), this.fileName);
    }

    private void logPercent(Integer progress, Long fileSize) {
        if(mDebug && fileSize > 1) {
            String percent = String.format(Locale.getDefault(), "%.2f", (float) progress / fileSize * 100);
            Log.d(LOG_TAG, progress + " / " + fileSize + " = " + percent + "%");
        }
    }
}
