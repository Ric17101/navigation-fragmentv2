package admin4.techelm.com.techelmtechnologies.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import admin4.techelm.com.techelmtechnologies.json.JSONHelper;
import admin4.techelm.com.techelmtechnologies.menu.MainActivity;
import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.UserLoginWrapper;
import admin4.techelm.com.techelmtechnologies.task.LoginActivityAuthenticationTask;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity2 extends AppCompatActivity implements
        LoginActivityAuthenticationTask.CallbackInterface {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private LoginActivityAuthenticationTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void initializedConnection() {
        // check if you are connected or not
        if (new JSONHelper().isConnected(this)) {
            //tvIsConnected.setBackgroundColor(0xFF00CC00);
            //tvIsConnected.setText("You are conncted");
        } else {
            //tvIsConnected.setText("You are NOT connected");
        }


        // call AsynTask to perform network operation on separate thread
        // new HttpAsyncTask().execute("http://hmkcode.appspot.com/rest/controller/get.json");
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new LoginActivityAuthenticationTask(email, password, this);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
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

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity2.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void onHandleSelection(int position, UserLoginWrapper user, int mode) {
    }
    @Override
    public void onHandleShowProgessLogin(boolean task) {
        showProgress(task);
    }
    @Override
    public void onHandleAuthTask(LoginActivityAuthenticationTask loginAuthTask) {
        mAuthTask = loginAuthTask;
    }
    @Override
    public void onHandleEmailError(String emailError) {
        mEmailView.setError(emailError);
        mEmailView.requestFocus();
    }
    @Override
    public void onHandlePasswordError(String passwordError) {
        mPasswordView.setError(passwordError);
        mPasswordView.requestFocus();
    }
    @Override public void onHandleSuccessLogin(UserLoginWrapper user) {
        Intent i = new Intent(LoginActivity2.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.abc_popup_enter,
                R.anim.abc_popup_exit);
    }
    @Override
    public void onHandleShowDetails(String details) {
        Toast.makeText(this, details, Toast.LENGTH_LONG).show();
    }
    /**
     * UpdateJobServiceTask task = new UpdateJobServiceTask(view);
     * task.execute("");
     */
    /*private class UpdateJobServiceTask extends AsyncTask<String, Void, String> {

        public UpdateJobServiceTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            populateAutoComplete();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //textView.setText(result);
        }
    }*/
}


