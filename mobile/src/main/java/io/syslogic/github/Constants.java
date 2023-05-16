package io.syslogic.github;

import androidx.annotation.NonNull;

/**
 * Common Constants
 *
 * @author Martin Zeitler
 */
public final class Constants {

    @NonNull public static final String ARGUMENT_ITEM_ID = "itemId";
    @NonNull public static final String ARGUMENT_REPOSITORY_TOPIC = "topic";

    @NonNull public static final Integer REQUESTCODE_ADD_ACCESS_TOKEN = 500;

    /** AccountManager account-type */
    @NonNull public static final String ACCOUNT_TYPE = "io.syslogic.github";

    /** Deprecated SDK Constant */
    @NonNull public static final String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    /** Retrofit Settings */
    @NonNull public static final String GITHUB_API_BASE_URL = "https://api.github.com/";
    @NonNull public static final String GITHUB_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /** RecyclerView Settings */
    @NonNull public static final Integer RECYCLERVIEW_SCROLLING_THRESHOLD = 12;
    @NonNull public static final Integer RECYCLERVIEW_DEFAULT_PAGE_SIZE   = 30;

    /** Preference Keys */
    @NonNull public static final String PREFERENCE_KEY_PERSONAL_ACCESS_TOKEN  = "personal_access_token";
    @NonNull public static final String PREFERENCE_KEY_WORKSPACE_DIRECTORY    = "workspace_directory";

    @NonNull public static final String PREFERENCE_KEY_SHOW_REPOSITORY_TOPICS = "show_repository_topics";
    @NonNull public static final String PREFERENCE_KEY_DEBUG_LOGGING          = "debug_logging";

    /** Table Names */
    @NonNull public static final String TABLE_QUERY_STRINGS = "query_strings";
    @NonNull public static final String TABLE_REPOSITORIES  = "repositories";
    @NonNull public static final String TABLE_LICENSES      = "licenses";
    @NonNull public static final String TABLE_OWNERS        = "owners";
    @NonNull public static final String TABLE_TOPICS        = "topics";
}
