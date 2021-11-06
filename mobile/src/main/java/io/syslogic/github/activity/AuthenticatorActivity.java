package io.syslogic.github.activity;

import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.os.Bundle;

import io.syslogic.github.R;

/**
 * Authenticator {@link BaseActivity}
 *
 * Copied from Android SDK (as the documentation suggested).
 */
public class AuthenticatorActivity extends BaseActivity {

    private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
    @SuppressWarnings("FieldCanBeLocal") private AccountManager mAccountManager;
    private Bundle mResultBundle = null;

    /**
     * Set the result that is to be sent as the result of the request that caused this Activity to be
     * launched. If result is null or this method is never called then the request will be canceled.
     * @param result this is returned as the result of the AbstractAccountAuthenticator request
     */
    public final void setAccountAuthenticatorResult(Bundle result) {
        mResultBundle = result;
    }

    /**
     * Retrieves the AccountAuthenticatorResponse from either the intent of the icicle,
     * if the icicle is non-zero.
     * @param icicle the save instance data of this Activity, may be null.
     */
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.mAccountManager = AccountManager.get(this);
        this.setContentView(R.layout.fragment_token);
        mAccountAuthenticatorResponse = getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }
    }

    /** Sends the result or a Constants.ERROR_CODE_CANCELED error if a result isn't present. */
    public void finish() {
        if (mAccountAuthenticatorResponse != null) {
            // send the result bundle back if set, otherwise send an error.
            if (mResultBundle != null) {
                mAccountAuthenticatorResponse.onResult(mResultBundle);
            } else {
                mAccountAuthenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED, "canceled");
            }
            mAccountAuthenticatorResponse = null;
        }
        super.finish();
    }
}
