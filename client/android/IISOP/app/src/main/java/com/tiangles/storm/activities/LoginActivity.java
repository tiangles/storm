package com.tiangles.storm.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;

import com.tiangles.storm.R;
import com.tiangles.storm.StormApp;
import com.tiangles.storm.preference.PreferenceEngine;
import com.tiangles.storm.user.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity{
    // UI references.
    @BindView(R.id.login_progress) View mProgressView;
    @BindView(R.id.login_form) View mLoginFormView;

    @BindView(R.id.login_user_name) EditText mUserNameEditor;
    @BindView(R.id.login_password) EditText mPasswordEditor;
    @BindView(R.id.login_remember_password) CheckBox mRememberPasswordBox;
    @BindView(R.id.login_auto_login) CheckBox mAutoLoginBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        User user = StormApp.getCurrentUser();
        PreferenceEngine.getInstance().loadUserInfo(user);

        mUserNameEditor.setText(user.mUserName);
        mPasswordEditor.setText(user.mPassword);
        mRememberPasswordBox.setChecked(user.mRememberPassword);
        mAutoLoginBox.setChecked(user.mAutoLogin);
        if(user.mAutoLogin){
            attemptLogin();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @OnClick(R.id.login_sign_in_button)
    public void onSignIn(){
        attemptLogin();
    }


    String serverAddr;
    int serverPort;
    @OnClick(R.id.login_setting_button)
    public void onSetting(){
        serverAddr =  PreferenceEngine.getInstance().getServerAddress();
        serverPort = PreferenceEngine.getInstance().getServerPort();
        Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String addr =  PreferenceEngine.getInstance().getServerAddress();
        int port = PreferenceEngine.getInstance().getServerPort();
        if(!addr.equals(serverAddr) || port != serverPort) {
            StormApp.closeNetwork();
        }
    }


    @OnEditorAction(R.id.login_password)
    public boolean onEditorAction(int id){
        if (id == R.id.login_password_action || id == EditorInfo.IME_NULL) {
            attemptLogin();
            return true;
        }
        return false;
    }

    private void attemptLogin() {
        // Reset errors.
        mUserNameEditor.setError(null);
        mPasswordEditor.setError(null);

        // Store values at the time of the login attempt.
        String signInUserName = mUserNameEditor.getText().toString();
        String password = mPasswordEditor.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordEditor.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordEditor;
            cancel = true;
        }

        // Check for a valid user name.
        if (TextUtils.isEmpty(signInUserName)) {
            mUserNameEditor.setError(getString(R.string.error_field_required));
            focusView = mUserNameEditor;
            cancel = true;
        } else if (!isUserNameValid(signInUserName)) {
            mUserNameEditor.setError(getString(R.string.error_invalid_user_name));
            focusView = mUserNameEditor;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            final User user = new User();
            user.set(-1, signInUserName,
                    password,
                    mRememberPasswordBox.isChecked(),
                    mAutoLoginBox.isChecked()
            );
            user.login(new User.LoginListener() {
                @Override
                public void onLoginDone(int error, String msg) {
                    showProgress(false);
                    if(user.mAuthSucceeded){
                        PreferenceEngine.getInstance().saveUserInfo(user);
                        switchToMain();
                    } else {
                        mPasswordEditor.setError(msg);
                        mPasswordEditor.requestFocus();
                    }
                }
            });
        }
    }

    private boolean isUserNameValid(String userName) {
        return !userName.isEmpty();
    }

    private boolean isPasswordValid(String password) {
        return !password.isEmpty();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void switchToMain(){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

