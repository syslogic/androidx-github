package io.syslogic.github.constants;

import androidx.annotation.NonNull;

public final class Constants {

    @NonNull
    public static final Integer PARAMETER_PUSHED_WITHIN_LAST_DAYS = 90;

    @NonNull
    public static final String GITHUB_API_BASE_URL = "https://api.github.com/";

    @NonNull
    public static final String ARGUMENT_ITEM_ID = "itemId";

    @NonNull
    public static final Integer REQUESTCODE_ADD_ACCESS_TOKEN = 500;

    @NonNull
    public static final Integer REQUESTCODE_DOWNLOAD_TARBALL = 501;

    @NonNull
    public static final Integer REQUESTCODE_DOWNLOAD_ZIPBALL = 502;

    @NonNull
    public static final String ARGUMENT_FILE_NAME    = "fileName";

    @NonNull
    public static final String ARGUMENT_FILE_SIZE    = "fileSize";

    @NonNull
    public static final String ARGUMENT_TASK_SUCCESS = "taskSuccess";

}
