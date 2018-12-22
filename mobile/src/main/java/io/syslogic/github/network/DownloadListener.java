package io.syslogic.github.task;

public interface IDownloadTask {

    void OnFileSize(String fileName, Long fileSize);

    void OnFileExists(String fileName, Long fileSize);

    void OnProgress(String fileName, Integer progress, Long fileSize);

    void OnComplete(String fileName, Long fileSize, Boolean success);

    void OnException(String fileName, Exception e);
}
