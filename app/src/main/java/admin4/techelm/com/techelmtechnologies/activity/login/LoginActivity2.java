package admin4.techelm.com.techelmtechnologies.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.menu.MainActivity;
import admin4.techelm.com.techelmtechnologies.model.UserLoginWrapper;
import admin4.techelm.com.techelmtechnologies.task.LoginActivityAuthenticationTask;
import admin4.techelm.com.techelmtechnologies.task.TaskCanceller;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;
import admin4.techelm.com.techelmtechnologies.utility.ProgressbarUtil;
import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;
import admin4.techelm.com.techelmtechnologies.utility.json.JSONHelper;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity2 extends AppCompatActivity implements
        LoginAuthenticationTaskListener {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private LoginActivityAuthenticationTask mAuthTask = null;

    private SessionManager mSession;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    // Loading Indicator Setup
    private View mProgressView;
    private View mLoginFormView;
    private ProgressbarUtil mProgressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullScreenMode();

        setContentView(R.layout.activity_login2);

        setBackGroundLayout();

        mSession = new SessionManager(this);
        if (mSession.isLoggedIn()) {
            goToLandingPage();
        }

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

        initProgresBarIndicator();
    }

    private void initProgresBarIndicator() {
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.page_progress);
        this.mProgressIndicator = new ProgressbarUtil().newInstance(mProgressView, mLoginFormView, getResources());
    }

    public void setFullScreenMode() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * This method uses LargeHeap and Hardware Acceleration on the Androidmanifest file in order to
     * set the Background image of the App/Activities
     * @ called at
     *      MainActivity
     *      ServiceJobViewPagerActivity
     *      Login
     *      ServiceReport_TaskCompleted_5
     *      ProjectJobViewPagerActivity
     *      ToolboxMeetingPagerActivity
     */
    private void setBackGroundLayout() {
        LinearLayout backgroundLayout = (LinearLayout) findViewById(R.id.linearLayoutLogin);
        backgroundLayout.setBackground(new ImageUtility(this).ResizeImage(R.drawable.background));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (!new JSONHelper().isConnected(this)) {
            SnackBarNotificationUtil
                    .setSnackBar(findViewById(android.R.id.content), getResources().getString(R.string.noInternetConnection))
                    .setColor(getResources().getColor(R.color.colorPrimary1))
                    .show();
            return;
        }
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
            mProgressIndicator.showProgress(true);
            mAuthTask = new LoginActivityAuthenticationTask(email, password, this);
            new TaskCanceller(mAuthTask).setWait(LoginActivity2.this);
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
    public void onHandleShowProgessLogin(boolean task) { mProgressIndicator.showProgress(task); }
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
    @Override
    public void onHandleSuccessLogin(UserLoginWrapper user) {
        mSession = new SessionManager(this); // Set Sesssion Mngr for user login
        mSession.createLoginSession(mEmailView.getText().toString(), mPasswordView.getText().toString(), user.getID()+"");
        goToLandingPage();
    }
    @Override
    public void onHandleShowDetails(String details) {
        Toast.makeText(this, details, Toast.LENGTH_LONG).show();
    }

    private void goToLandingPage() {
        Intent i = new Intent(LoginActivity2.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.abc_popup_enter,
                R.anim.abc_popup_exit);
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
        protected void onPostExecute(String aResponse) {
            //textView.setText(aResponse);
        }
    }*/
}


