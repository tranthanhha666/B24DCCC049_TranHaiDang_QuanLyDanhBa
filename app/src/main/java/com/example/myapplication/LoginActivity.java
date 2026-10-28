package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public final class LoginActivity extends BaseActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private TextView loginError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showScreen(R.layout.activity_login);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginError = findViewById(R.id.loginError);
        findViewById(R.id.loginButton).setOnClickListener(view -> attemptLogin());
        passwordInput.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                attemptLogin();
                return true;
            }
            return false;
        });
    }

    private void attemptLogin() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        loginError.setVisibility(View.GONE);
        usernameInput.setError(null);
        passwordInput.setError(null);
        boolean valid = true;
        if (username.isEmpty()) {
            usernameInput.setError(getString(R.string.required_username));
            valid = false;
        }
        if (password.isEmpty()) {
            passwordInput.setError(getString(R.string.required_password));
            valid = false;
        }
        if (!valid) return;
        if (!LoginValidator.matches(username, password)) {
            loginError.setVisibility(View.VISIBLE);
            return;
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
