package io.syslogic.github;

import androidx.annotation.NonNull;

/**
 * Common Constants
 * @author Martin Zeitler
 */
public final class Constants {
    @NonNull public static final String GITHUB_API_BASE_URL = "https://api.github.com/";
    @NonNull public static final String GITHUB_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    @NonNull public static final String CLIENT_ACCOUNT_TYPE = "io.syslogic.github";
    @NonNull public static final String ARGUMENT_ITEM_ID = "itemId";
    @NonNull public static final Integer RECYCLERVIEW_SCROLLING_THRESHOLD = 12;
    @NonNull public static final Integer REQUESTCODE_ADD_ACCESS_TOKEN = 500;
    @NonNull public static final Integer REQUESTCODE_DOWNLOAD_ZIPBALL = 501;
    @NonNull public static final Integer REQUESTCODE_DOWNLOAD_TARBALL = 502;
    @NonNull public static final String PREFERENCE_KEY_DEBUG_LOGGING = "debug_logging";
    @NonNull public static final String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    @NonNull public static final String TABLE_REPOSITORIES = "repositories";
    @NonNull public static final String TABLE_LICENSES = "licenses";
    @NonNull public static final String TABLE_OWNERS = "owners";
    @NonNull public static final String TABLE_QUERIES = "queries"; // TODO: should replace TABLE_TOPICS.
    @NonNull public static final String TABLE_TOPICS = "topics";
}
