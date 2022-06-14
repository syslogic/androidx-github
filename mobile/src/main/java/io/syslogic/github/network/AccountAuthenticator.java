package io.syslogic.github.network;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

import io.syslogic.github.activity.AuthenticatorActivity;

/**
 * Custom {@link AbstractAccountAuthenticator}.
 * TODO: implement abstract methods.
 * @author Martin Zeitler
 */
public class AccountAuthenticator extends AbstractAccountAuthenticator {

    @NonNull private final WeakReference<Context> mContext;

    public AccountAuthenticator(@NonNull Context context) {
        super(context);
        this.mContext = new WeakReference<>(context);
    }

    /**
     * Returns a Bundle that contains the Intent of the activity that can be used to edit the
     * properties. In order to indicate success the activity should call response.setResult()
     * with a non-null Bundle.
     *
     * @param response used to set the result for the request.
     *                 If the Constants.INTENT_KEY is set in the bundle then this response field
     *                 is to be used for sending future results if and when the Intent is started.
     *
     * @param accountType the AccountType whose properties are to be edited.
     *
     * @return a Bundle containing the result or the Intent to start to continue the request.
     *         If this is null then the request is considered to still be active
     *         and the result should sent later using response.
     */
    @Nullable
    @Override
    public Bundle editProperties(
            @NonNull AccountAuthenticatorResponse response,
            @NonNull String accountType
    ) {

        throw new UnsupportedOperationException();
    }

    /**
     * Adds an account of the specified accountType.
     *
     * @param response to send the result back to the AccountManager, will never be null
     * @param accountType the type of account to add, will never be null
     * @param authTokenType the type of auth token to retrieve after adding the account, may be null
     * @param requiredFeatures a String array of authenticator-specific features that the added account must support, may be null
     * @param options a Bundle of authenticator-specific options.
     *                It always contains
     *                {@link AccountManager#KEY_CALLER_PID} and {@link AccountManager#KEY_CALLER_UID}
     *                fields which will let authenticator know the identity of the caller.
     *
     * @return a Bundle result or null if the result is to be returned via the response.
     *
     * The result will contain either:
     * <ul>
     * <li> {@link AccountManager#KEY_INTENT}, or
     * <li> {@link AccountManager#KEY_ACCOUNT_NAME} and {@link AccountManager#KEY_ACCOUNT_TYPE} of the account that was added, or
     * <li> {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} to indicate an error
     * </ul>
     */
    @Nullable
    @Override
    public Bundle addAccount(
            @NonNull AccountAuthenticatorResponse response,
            @NonNull String accountType,
            @NonNull String authTokenType,
            @NonNull String[] requiredFeatures,
            @NonNull Bundle options
    ) {
        Intent intent = new Intent(mContext.get(), AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    /**
     * Checks that the user knows the credentials of an account.
     *
     * @param response to send the result back to the AccountManager, will never be null
     * @param account the account whose credentials are to be checked, will never be null
     * @param options a Bundle of authenticator-specific options, may be null
     * @return a Bundle result or null if the result is to be returned via the response. The result
     * will contain either:
     * <ul>
     * <li> {@link AccountManager#KEY_INTENT}, or
     * <li> {@link AccountManager#KEY_BOOLEAN_RESULT}, true if the check succeeded, false otherwise
     * <li> {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} to
     * indicate an error
     * </ul>
     */
    @Nullable
    @Override
    public Bundle confirmCredentials(
            @NonNull AccountAuthenticatorResponse response,
            @NonNull Account account,
            @NonNull Bundle options
    ) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets an authtoken for an account.
     *
     * If not {@code null}, the resultant {@link Bundle} will contain different sets of keys
     * depending on whether a token was successfully issued and, if not, whether one
     * could be issued via some {@link android.app.Activity}.
     * <p>
     * If a token cannot be provided without some additional activity, the Bundle should contain
     * {@link AccountManager#KEY_INTENT} with an associated {@link Intent}. On the other hand, if
     * there is no such activity, then a Bundle containing
     * {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} should be
     * returned.
     * <p>
     * If a token can be successfully issued, the implementation should return the
     * {@link AccountManager#KEY_ACCOUNT_NAME} and {@link AccountManager#KEY_ACCOUNT_TYPE} of the
     * account associated with the token as well as the {@link AccountManager#KEY_AUTHTOKEN}. In
     * addition {@link AbstractAccountAuthenticator} implementations that declare themselves
     * {@code android:customTokens=true} may also provide a non-negative {@link
     * #KEY_CUSTOM_TOKEN_EXPIRY} long value containing the expiration timestamp of the expiration
     * time (in millis since the unix epoch), tokens will be cached in memory based on
     * application's packageName/signature for however long that was specified.
     * <p>
     * Implementers should assume that tokens will be cached on the basis of account and
     * authTokenType. The system may ignore the contents of the supplied options Bundle when
     * determining to re-use a cached token. Furthermore, implementers should assume a supplied
     * expiration time will be treated as non-binding advice.
     * <p>
     * Finally, note that for {@code android:customTokens=false} authenticators, tokens are cached
     * indefinitely until some client calls {@link
     * AccountManager#invalidateAuthToken(String,String)}.
     *
     * @param response to send the result back to the AccountManager, will never be null
     * @param account the account whose credentials are to be retrieved, will never be null
     * @param authTokenType the type of auth token to retrieve, will never be null
     * @param options a Bundle of authenticator-specific options. It always contains
     * {@link AccountManager#KEY_CALLER_PID} and {@link AccountManager#KEY_CALLER_UID}
     * fields which will let authenticator know the identity of the caller.
     * @return a Bundle result or null if the result is to be returned via the response.
     */
    @Nullable
    @Override
    public Bundle getAuthToken(
            @NonNull AccountAuthenticatorResponse response,
            @NonNull Account account,
            @NonNull String authTokenType,
            @NonNull Bundle options
    ) {
        throw new UnsupportedOperationException();
    }

    /**
     * Ask the authenticator for a localized label for the given authTokenType.
     * @param authTokenType the authTokenType whose label is to be returned, will never be null
     * @return the localized label of the auth token type, may be null if the type isn't known
     */
    @Nullable
    @Override
    public String getAuthTokenLabel(@NonNull String authTokenType) {
        throw new UnsupportedOperationException();
    }

    /**
     * Update the locally stored credentials for an account.
     * @param response to send the result back to the AccountManager, will never be null
     * @param account the account whose credentials are to be updated, will never be null
     * @param authTokenType the type of auth token to retrieve after updating the credentials,
     * may be null
     * @param options a Bundle of authenticator-specific options, may be null
     * @return a Bundle result or null if the result is to be returned via the response. The result
     * will contain either:
     * <ul>
     * <li> {@link AccountManager#KEY_INTENT}, or
     * <li> {@link AccountManager#KEY_ACCOUNT_NAME} and {@link AccountManager#KEY_ACCOUNT_TYPE} of
     * the account whose credentials were updated, or
     * <li> {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} to
     * indicate an error
     * </ul>
     */
    @Nullable
    @Override
    public Bundle updateCredentials(
            @NonNull AccountAuthenticatorResponse response,
            @NonNull Account account,
            @NonNull String authTokenType,
            @NonNull Bundle options
    ) {
        throw new UnsupportedOperationException();
    }

    /**
     * Checks if the account supports all the specified authenticator specific features.
     * @param response to send the result back to the AccountManager, will never be null
     * @param account the account to check, will never be null
     * @param features an array of features to check, will never be null
     * @return a Bundle result or null if the result is to be returned via the response. The result
     * will contain either:
     * <ul>
     * <li> {@link AccountManager#KEY_INTENT}, or
     * <li> {@link AccountManager#KEY_BOOLEAN_RESULT}, true if the account has all the features,
     * false otherwise
     * <li> {@link AccountManager#KEY_ERROR_CODE} and {@link AccountManager#KEY_ERROR_MESSAGE} to
     * indicate an error
     * </ul>
     */
    @Nullable
    @Override
    public Bundle hasFeatures(
            @NonNull AccountAuthenticatorResponse response,
            @NonNull Account account,
            @NonNull String[] features
    ) {
        throw new UnsupportedOperationException();
    }
}
