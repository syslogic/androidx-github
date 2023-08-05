package io.syslogic.github.activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.ViewDataBinding;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.R;
import io.syslogic.github.databinding.FragmentAccessTokenBinding;
import io.syslogic.github.api.GithubClient;
import io.syslogic.github.api.model.User;
import io.syslogic.github.network.TokenHelper;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The Authenticator {@link BaseActivity}
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
    @SuppressWarnings({"deprecation", "RedundantSuppression"})
    protected void onCreate(@Nullable Bundle icicle) {
        super.onCreate(icicle);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            this.mResponse = getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, AccountAuthenticatorResponse.class);
        } else {
            this.mResponse = getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
        }

        if (this.mResponse != null) {
            this.mResponse.onRequestContinued();
        }

        this.setDataBinding(FragmentAccessTokenBinding.inflate(getLayoutInflater(), findViewById(android.R.id.content), true));
        this.getDataBinding().setToken("");

        this.getDataBinding().personalAccessToken.requestFocus();
        this.getDataBinding().addAccessToken.setOnClickListener(view -> {
            Editable editable = this.getDataBinding().personalAccessToken.getText();
            if (editable != null && !editable.toString().isEmpty()) {

                /* Obtain the username; this also validates the access token. */
                String token = editable.toString();
                Call<User> api = GithubClient.getUser(token);
                if (BuildConfig.DEBUG) {Log.w(LOG_TAG, api.request().url() + "");}
                api.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        mResult = new Bundle();
                        switch (response.code()) {

                            // OK
                            case 200 -> {
                                if (response.body() != null) {
                                    User item = response.body();
                                    Account account = TokenHelper.addAccount(AccountManager.get(AuthenticatorActivity.this), item.getLogin(), token);
                                    if (account != null) {
                                        mResult.putInt(AccountManager.KEY_ERROR_CODE, 0);
                                    } else {
                                        Toast.makeText(AuthenticatorActivity.this, "The access token has not been added.\nOnly one token is being supported", Toast.LENGTH_SHORT).show();
                                        mResult.putInt(AccountManager.KEY_ERROR_CODE, AccountManager.ERROR_CODE_UNSUPPORTED_OPERATION);
                                    }
                                }
                            }
                            case 401, 403, 404 -> {
                                /* "bad credentials" means that the provided access-token is invalid. */
                                mResult.putInt(AccountManager.KEY_ERROR_CODE, AccountManager.ERROR_CODE_UNSUPPORTED_OPERATION);
                                if (response.errorBody() != null) {
                                    logError(response.errorBody());
                                }
                            }
                        }
                        mResponse.onResult(mResult);
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        if (BuildConfig.DEBUG) {Log.e(LOG_TAG, "" + t.getMessage());}
                    }
                });
            }
        });
    }

    void logError(@NonNull ResponseBody responseBody) {
        try {
            String errors = responseBody.string();
            JsonObject jsonObject = JsonParser.parseString(errors).getAsJsonObject();
            Toast.makeText(AuthenticatorActivity.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
            if (BuildConfig.DEBUG) {Log.e(LOG_TAG, jsonObject.get("message").toString());}
        } catch (IOException e) {
            if (BuildConfig.DEBUG) {Log.e(LOG_TAG, "" + e.getMessage());}
        }
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
