package com.example.android.data;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import static com.example.android.data.Constants.MY_GLOBAL_PREFS;
import static com.example.android.data.Constants.USER_NAME_KEY;

public class SigninActivity extends AppCompatActivity {

    // UI references.
    private EditText mUserNameView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Set up the sign-in form.
        mUserNameView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        SharedPreferences prefs = getSharedPreferences(MY_GLOBAL_PREFS, MODE_PRIVATE);
        String userName = prefs.getString(USER_NAME_KEY, "");

        if (!TextUtils.isEmpty(userName)) {
            mUserNameView.setText(userName);
            mUserNameView.setSelection(userName.length());
        }

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignin();
            }
        });
    }

    private void attemptSignin() {

        // clear errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the sign-in attempt.
        String userName = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Intent intent = new Intent();
            intent.putExtra(Constants.USER_NAME_KEY, userName);
            intent.putExtra(Constants.PASSWORD_KEY, password);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}

