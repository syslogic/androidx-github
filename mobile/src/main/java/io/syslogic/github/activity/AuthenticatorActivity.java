package io.syslogic.github.activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.ViewDataBinding;

import io.syslogic.github.R;
import io.syslogic.github.databinding.FragmentAccessTokenBinding;
import io.syslogic.github.network.TokenHelper;

/**
 * The Authenticator {@link BaseActivity}.
 *
 * @author Martin Zeitler
 */
public class AuthenticatorActivity extends BaseActivity {

    /** Log Tag */
    @SuppressWarnings("unused")
    private static final String LOG_TAG = AuthenticatorActivity.class.getSimpleName();

    /** layout resId kept for reference */
    @SuppressWarnings("unused")
    private static final int resId = R.layout.fragment_access_token;

    @Nullable FragmentAccessTokenBinding mDataBinding;
    @Nullable AccountAuthenticatorResponse mResponse = null;
    @Nullable Bundle mResult = null;

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

        this.setDataBinding(FragmentAccessTokenBinding.inflate(getLayoutInflater(), findViewById(android.R.id.content), true));
        this.getDataBinding().setToken("");

        this.getDataBinding().personalAccessToken.requestFocus();
        this.getDataBinding().addAccessToken.setOnClickListener(view -> {
            Editable editable = this.getDataBinding().personalAccessToken.getText();
            if (editable != null && !editable.toString().isEmpty()) {

                /* TODO: add Account and return Bundle? */
                Account account = TokenHelper.addAccount(AccountManager.get(AuthenticatorActivity.this), editable.toString());
                this.mResult = new Bundle();
                if (account != null) {
                    this.mResult.putInt(AccountManager.KEY_ERROR_CODE, 0);
                } else {
                    Toast.makeText(AuthenticatorActivity.this, "The access token has not been added.\nOnly one token is being supported", Toast.LENGTH_SHORT).show();
                    this.mResult.putInt(AccountManager.KEY_ERROR_CODE, AccountManager.ERROR_CODE_UNSUPPORTED_OPERATION);
                }
                this.mResponse.onResult(this.mResult);
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

    @NonNull
    @VisibleForTesting
    private FragmentAccessTokenBinding getDataBinding() {
        assert this.mDataBinding != null;
        return this.mDataBinding;
    }

    private void setDataBinding(@NonNull ViewDataBinding binding) {
        this.mDataBinding = (FragmentAccessTokenBinding) binding;
    }
}
