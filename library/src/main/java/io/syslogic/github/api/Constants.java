package io.syslogic.github.api;

import androidx.annotation.NonNull;

/**
 * Common Constants
 *
 * @author Martin Zeitler
 */
public final class Constants {

    /** GitHub API Base URL */
    @NonNull public static final String GITHUB_API_BASE_URL = "https://api.github.com/";

    /** GitHub API date-format. */
    @NonNull public static final String GITHUB_API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /** Table name for QueryString. */
    @NonNull public static final String TABLE_QUERY_STRINGS = "query_strings";

    /** Table name for Repository. */
    @NonNull public static final String TABLE_REPOSITORIES  = "repositories";

    /** Table name for Workflow. */
    @NonNull public static final String TABLE_WORKFLOWS     = "workflows";

    /** Table name for WorkflowRun. */
    @NonNull public static final String TABLE_WORKFLOW_RUNS = "workflow_runs";

    /** Table name for License. */
    @NonNull public static final String TABLE_LICENSES      = "licenses";

    /** Table name for Owner. */
    @NonNull public static final String TABLE_OWNERS        = "owners";
}
