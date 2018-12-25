package io.syslogic.github.task;

import androidx.annotation.NonNull;

/**
 * Download Listener
 * @author Martin Zeitler
 * @version 1.0.0
**/
public interface DownloadListener {

    void OnFileSize(@NonNull String fileName, @NonNull Long fileSize);

    void OnFileExists(@NonNull String fileName, @NonNull Long fileSize);

    void OnProgress(@NonNull String fileName, @NonNull Integer progress, @NonNull Long fileSize);

    void OnComplete(@NonNull String fileName, @NonNull Long fileSize, @NonNull Boolean success);

    void OnException(@NonNull String fileName, @NonNull Exception e);
}
