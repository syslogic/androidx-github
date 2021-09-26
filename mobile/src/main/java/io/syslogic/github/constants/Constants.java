package io.syslogic.github.constants;

import androidx.annotation.NonNull;

/**
 * Constants
 * @author Martin Zeitler
 */
public final class Constants {

    @NonNull
    public static final Integer PARAMETER_PUSHED_WITHIN_LAST_DAYS = 90;

    @NonNull
    public static final String GITHUB_API_BASE_URL = "https://api.github.com/";

    @NonNull
    public static final String GITHUB_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @NonNull
    public static final String ARGUMENT_ITEM_ID = "itemId";

    @NonNull
    public static final Integer REQUESTCODE_ADD_ACCESS_TOKEN = 500;

    @NonNull
    public static final Integer REQUESTCODE_DOWNLOAD_ZIPBALL = 501;

    @NonNull
    public static final Integer REQUESTCODE_DOWNLOAD_TARBALL = 502;

    @NonNull
    public static final String ARGUMENT_FILE_NAME    = "fileName";

    @NonNull
    public static final String ARGUMENT_FILE_SIZE    = "fileSize";

    @NonNull
    public static final String ARGUMENT_TASK_SUCCESS = "taskSuccess";

    @NonNull
    public static final String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    @NonNull
    public static final String TABLE_REPOSITORIES = "repositories";

    @NonNull
    public static final String TABLE_LICENSES = "licenses";

    @NonNull
    public static final String TABLE_OWNERS = "owners";

    @NonNull
    public static final String TABLE_TOPICS = "topics";

}
