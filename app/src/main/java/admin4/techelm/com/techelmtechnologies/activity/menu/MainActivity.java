package admin4.techelm.com.techelmtechnologies.activity.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.HashMap;

import admin4.techelm.com.techelmtechnologies.BuildConfig;
import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.CalendarListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.ServiceJobListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.UnsignedServiceJobListAdapter;
import admin4.techelm.com.techelmtechnologies.activity.fragment_sample.PrimaryFragment;
import admin4.techelm.com.techelmtechnologies.activity.fragment_sample.TabFragment;
import admin4.techelm.com.techelmtechnologies.activity.login.LoginActivity2;
import admin4.techelm.com.techelmtechnologies.activity.login.SessionManager;
import admin4.techelm.com.techelmtechnologies.activity.service_report.ServiceReport_1;
import admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.ServiceJobViewPagerActivity;
import admin4.techelm.com.techelmtechnologies.activity.servicejob_main.PopulateServiceJobViewDetails;
import admin4.techelm.com.techelmtechnologies.activity.servicejob_main.ServiceJobFragmentTab;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;
import admin4.techelm.com.techelmtechnologies.utility.UIThreadHandler;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm.ServiceJobBegin_POST;

public class MainActivity extends FragmentActivity implements
        ServiceJobListAdapter.CallbackInterface,
        CalendarListAdapter.CallbackInterface,
        UnsignedServiceJobListAdapter.CallbackInterface {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String VERSION = BuildConfig.VERSION_NAME;
    private static final String RECORD_SERVICE_KEY = "SERVICE_ID";
    private static final String RECORD_JOB_SERVICE_KEY = "SERVICE_JOB";

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    NavigationView mNavigationUserView;
    ActionBarDrawerToggle mDrawerToggle;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    SessionManager mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setBackGroundLayout();

        loginSessionTest();
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        new UIThreadHandler(this).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                init_DrawerNav();
            }
        });
    }

    /**
     * This method uses LargeHeap and Hardware Acceleration on the Androidmanifest file in order to
     * set the Background image of the App/Activities
     * @ called at
     *      MainActivity
     *      ServiceJobViewPagerActivity
     *      Login
     *      ServiceReport_TaskCompleted_5
     */
    private void setBackGroundLayout() {
        LinearLayout backgroundLayout = (LinearLayout) findViewById(R.id.activity_main);
        backgroundLayout.setBackground(new ImageUtility(this).ResizeImage(R.drawable.background));
    }

    private void loginSessionTest() {
        mSession = new SessionManager(this);
        mSession.checkLogin();
        // get user data from session
        HashMap<String, String> user = mSession.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);
        Log.e(TAG, "Name: "+ name + " Email: " + email);
    }

    /**
     * These Two Lines should be included on every Fragment to maintain the state and donnot load again
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("MainActivity: I'm on the onSaveInstanceState");
    }

    @Override
    public void onBackPressed() {
        logout();
    }

    /*@Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }*/

    private void logout() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to signout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mSession.clearPrefs();
                        Intent i = new Intent(MainActivity.this, LoginActivity2.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_popup_exit);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void init_DrawerNav() {
        /**
         *Setup the DrawerLayout and NavigationView
         */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationUserView = (NavigationView) findViewById(R.id.nav_view_user);

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new ServiceJobFragmentTab()).commit();

        /**
         * Setup click events on the Navigation View Items.
         */
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers(); // Close Nav Draw after onClick of Tabs
                menuItem.setChecked(true); // Set Active Tab

                switch (menuItem.getItemId()) {
                    case R.id.nav_servicejobs:
                        FragmentTransaction serviceJobFragmentTransaction = mFragmentManager.beginTransaction();
                        serviceJobFragmentTransaction.replace(R.id.containerView, new ServiceJobFragmentTab()).commit();
                        break;
                    case R.id.nav_checklist:
                        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.containerView, new PrimaryFragment()).commit();

                        /*startActivity(new Intent(MainActivity.this, ServiceJobViewPagerActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        overridePendingTransition(R.anim.enter, R.anim.exit);*/
                        break;
                    case R.id.nav_process_pe:
                        FragmentTransaction fragmentProcessTransaction = mFragmentManager.beginTransaction();

                        /*Bundle arguments = new Bundle();
                        arguments.putBundle(FRAGMENT_TRANSACTION_KEY, getFragmentManager());
                        fragmentProcessTransaction.setArguments(arguments);*/

                        fragmentProcessTransaction.replace(R.id.containerView, new PrimaryFragment()).commit();
                        break;
                    case R.id.nav_process_eps:
                        startActivity(new Intent(MainActivity.this, ServiceReport_1.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        break;
                    case R.id.nav_toolbox:
                        FragmentTransaction sampleFragmentTransaction = mFragmentManager.beginTransaction();
                        sampleFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
                        break;
                }
                return false;
            }

        });

        /**
         * Setup click events on the User Navigation View Items.
         */
        mNavigationUserView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.nav_signout:
                        logout();
                        break;
                }
                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar,
                R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
    }

    /**
     * Handles the Event onClick CallbackInterface for the CardViewList
     *
     * @param position   - the position
     * @param serviceJob - the text to pass back
     *                   3 - Show Details on ServiceReport_FRGMT_BEFORE
     *                   2 - Show Details on SJ MDialog
     * @param action
     */
    @Override
    public void onHandleSelection(int position, ServiceJobWrapper serviceJob, int action) {
        String strOut = "Position: " + position + " \nService Job:" + serviceJob.toString() + "\nMode: " + action;
        System.out.print(strOut);

        switch(action) {
            case 2 : // Show Details of SJ on MDialog
                showMDialogSJDetails(serviceJob);
                // Toast.makeText(getApplicationContext(), serviceJob.toString(), Toast.LENGTH_SHORT).show();
                break;
            case 3 : // Show Details on ServiceReport_FRGMT_BEFORE View
                startActivity(new Intent(MainActivity.this, ServiceJobViewPagerActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, serviceJob));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case 4 : // Confirm Begin Task
                confirmBeginTaskMDialog(serviceJob);
                break;
        }
    }

    /**
     * View on the CalendarFragment onClick View
     * @param serviceJob - ServiceJob Wrapper from CalendarFragment
     */
    private void confirmBeginTaskMDialog(final ServiceJobWrapper serviceJob) {
        MaterialDialog md = new MaterialDialog.Builder(this)
                .title("BEGIN TASK " + serviceJob.getServiceNumber() + "?")
                .customView(R.layout.i_labels_report_details_modal, true)
                .limitIconToDefaultSize()
                .negativeText("CLOSE")
                .positiveText("BEGIN")
                .iconRes(R.mipmap.view_icon)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        serviceJobStarTask(serviceJob);
                    }
                }).build();

        new PopulateServiceJobViewDetails()
                .populateServiceJobDetailsMaterialDialog(md.getCustomView(), serviceJob, View.GONE, TAG);
        md.show();
    }

    private void serviceJobStarTask(final ServiceJobWrapper serviceJob) {
        ServiceJobBegin_POST beginServiceJob = new ServiceJobBegin_POST();
        beginServiceJob.setOnEventListener(new ServiceJobBegin_POST.OnEventListener() {
            @Override
            public void onEvent() {
                // TODO: Close progress dialog here
                // TODO: Test Response if OK or not
            }

            @Override
            public void onEventResult(WebResponse response) {
                startActivity(new Intent(MainActivity.this, ServiceJobViewPagerActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, serviceJob));
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        beginServiceJob.postStartDate(serviceJob.getID());
    }

    /**
     * View on the CalendarFragment onClick View
     * @param serviceJob - ServiceJob Wrapper from CalendarFragment
     */
    private void showMDialogSJDetails(ServiceJobWrapper serviceJob) {
        MaterialDialog md = new MaterialDialog.Builder(this)
                .title("SERVICE JOB " + serviceJob.getServiceNumber())
                .customView(R.layout.i_labels_report_details_modal, true)
                .limitIconToDefaultSize()
                .positiveText("OK")
                .iconRes(R.mipmap.view_icon)
                .autoDismiss(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).build();

        new PopulateServiceJobViewDetails()
                .populateServiceJobDetailsMaterialDialog(md.getCustomView(), serviceJob, View.GONE, TAG);
        md.show();
    }

    /**
     * Represents an asynchronous Task
     * UITask mAuthTask = new UITask();
        mAuthTask.execute((Void) null);
     */
    /*public class UITask extends AsyncTask<Void, Void, Boolean> {
        UITask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TO DO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
                // init_DrawerNav();
            } catch (InterruptedException e) {
                return false;
            }


            // TO DO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean aSuccess) {
        }

        @Override
        protected void onCancelled() {
        }
    }*/
}