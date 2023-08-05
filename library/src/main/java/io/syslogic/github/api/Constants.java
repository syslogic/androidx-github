package io.syslogic.github.api;

import androidx.annotation.NonNull;

/**
 * Common Constants
 *
 * @author Martin Zeitler
 */
public final class Constants {
    @NonNull public static final String GITHUB_API_BASE_URL = "https://api.github.com/";
    @NonNull public static final String GITHUB_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /** Table Names */
    @NonNull public static final String TABLE_QUERY_STRINGS = "query_strings";
    @NonNull public static final String TABLE_REPOSITORIES  = "repositories";
    @NonNull public static final String TABLE_LICENSES      = "licenses";
    @NonNull public static final String TABLE_OWNERS        = "owners";
}
