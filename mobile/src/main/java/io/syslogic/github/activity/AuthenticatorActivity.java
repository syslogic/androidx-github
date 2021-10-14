package io.syslogic.github.activity;

import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.os.Bundle;

import io.syslogic.github.R;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    private AccountManager mAccountManager;

    private String mAuthTokenType;

    public AuthenticatorActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mAccountManager = AccountManager.get(this);
        this.setContentView(R.layout.fragment_token);
    }
}