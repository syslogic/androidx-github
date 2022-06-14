package io.syslogic.github.activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.text.Editable;

import androidx.annotation.Nullable;

import io.syslogic.github.R;
import io.syslogic.github.databinding.FragmentAccessTokenBinding;
import io.syslogic.github.network.TokenHelper;

/**
 * The Authenticator {@link BaseActivity}.
 *
 * @author Martin Zeitler
 */
public class AuthenticatorActivity extends BaseActivity {

    @Nullable AccountAuthenticatorResponse mResponse = null;
    @Nullable FragmentAccessTokenBinding mDataBinding;
    @Nullable Bundle mResult = null;

    /** layout resId kept for reference */
    @SuppressWarnings("unused")
    private static final int resId = R.layout.fragment_access_token;

    /**
     * Retrieves the AccountAuthenticatorResponse from either the intent.
     * @param icicle the saved instance data of this Activity, may be null.
     */
    @Override
    protected void onCreate(@Nullable Bundle icicle) {

        super.onCreate(icicle);
        this.mResponse = getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
        if (this.mResponse != null) {
            this.mResponse.onRequestContinued();
        }

        this.mDataBinding = FragmentAccessTokenBinding.inflate(getLayoutInflater(), findViewById(android.R.id.content), true);
        this.mDataBinding.addAccessToken.setOnClickListener(view -> {
            Editable editable = this.mDataBinding.personalAccessToken.getText();
            if (editable != null && !editable.toString().isEmpty()) {

                /* TODO: add Account and return Bundle. */
                Account account = TokenHelper.addAccount(AccountManager.get(AuthenticatorActivity.this), editable.toString());
                if (account != null) {
                    this.mResult = new Bundle();
                    this.mResult.putInt(AccountManager.KEY_ERROR_CODE, 0);
                    this.mResponse.onResult(this.mResult);
                } else {
                    /* duplicate access token. */
                }
            }
        });
    }

    /** Sends the result or a Constants.ERROR_CODE_CANCELED error if a result isn't present. */
    @Override
    public void finish() {
        if (this.mResponse != null) {
            // send the result bundle back if set, otherwise send an error.
            this.mResponse.onError(AccountManager.ERROR_CODE_CANCELED, "canceled");
            this.mResponse = null;
        }
        super.finish();
    }
}
