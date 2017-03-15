package admin4.techelm.com.techelmtechnologies.menu;

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
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import admin4.techelm.com.techelmtechnologies.BuildConfig;
import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.CalendarListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.ServiceJobListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.UnsignedServiceJobListAdapter;
import admin4.techelm.com.techelmtechnologies.fragment_sample.PrimaryFragment;
import admin4.techelm.com.techelmtechnologies.fragment_sample.SentFragment;
import admin4.techelm.com.techelmtechnologies.fragment_sample.TabFragment;
import admin4.techelm.com.techelmtechnologies.login.LoginActivity2;
import admin4.techelm.com.techelmtechnologies.service_report.ServiceReport_1;
import admin4.techelm.com.techelmtechnologies.service_report.ViewPagerActivity;
import admin4.techelm.com.techelmtechnologies.service_report_fragment.ServiceJobViewPagerActivity;
import admin4.techelm.com.techelmtechnologies.servicejob.PopulateServiceJobViewDetails;
import admin4.techelm.com.techelmtechnologies.servicejob.ServiceJobFragmentTab;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.utility.UIThreadHandler;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    @Override
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
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to signout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
     *                   3 - Show Details on ServiceReport_FRGMT_1
     *                   2 - Show Details on SJ MDialog
     * @param mode
     */
    @Override
    public void onHandleSelection(int position, ServiceJobWrapper serviceJob, int mode) {
        String strOut = "Position: " + position + " \nService Job:" + serviceJob.toString() + "\nMode: " + mode;
        System.out.print(strOut);

        switch(mode) {
            case 3 : // Show Details on ServiceReport_FRGMT_1 View
                startActivity(new Intent(MainActivity.this, ServiceJobViewPagerActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, serviceJob));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case 2 : // Show Details of SJ on MDialog
                showMDialogSJDetails(serviceJob);
                // Toast.makeText(getApplicationContext(), serviceJob.toString(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * View on the CalendarFragment onClick View
     * @param serviceJob - ServiceJob Wrapper from CalendarFragment
     */
    private void showMDialogSJDetails(ServiceJobWrapper serviceJob) {
        MaterialDialog md = new MaterialDialog.Builder(this)
                .title("SERVICE JOB " + serviceJob.getServiceNumber())
                .customView(R.layout.i_labels_report_details, true)
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
                .populateServiceJobDetails(md.getCustomView(), serviceJob, View.GONE, TAG);
        md.show();
    }

    /**
     * View on the CalendarFragment onClick View
     * @param vDialog - view of the Material View
     * @param serviceJob - ServiceJob Wrapper from CalendarFragment
     */
    private void populateServiceJobDetails(View vDialog, ServiceJobWrapper serviceJob) {

        // SERVICE JOB Controls
        ImageButton buttonViewDetails = (ImageButton) vDialog.findViewById(R.id.buttonViewDetails);
        TextView textViewLabelCustomerName = (TextView) vDialog.findViewById(R.id.textViewLabelCustomerName);
        TextView textViewLabelJobSite = (TextView) vDialog.findViewById(R.id.textViewLabelJobSite);
        TextView textViewLabelServiceNo = (TextView) vDialog.findViewById(R.id.textViewLabelServiceNo);
        TextView textViewLabelTypeOfService = (TextView) vDialog.findViewById(R.id.textViewLabelTypeOfService);
        TextView textViewLabelTelephone = (TextView) vDialog.findViewById(R.id.textViewLabelTelephone);
        TextView textViewLabelFax = (TextView) vDialog.findViewById(R.id.textViewLabelFax);
        TextView textViewLabelEquipmentType = (TextView) vDialog.findViewById(R.id.textViewLabelEquipmentType);
        TextView textViewLabelModel = (TextView) vDialog.findViewById(R.id.textViewLabelModel);
        TextView textViewComplaints = (TextView) vDialog.findViewById(R.id.textViewComplaints);
        TextView textViewRemarksActions = (TextView) vDialog.findViewById(R.id.textViewRemarksActions);


        Log.e(TAG, "DATA: " + serviceJob.toString());
        buttonViewDetails.setVisibility(View.GONE);
        textViewLabelCustomerName.setText(serviceJob.getCustomerID());
        textViewLabelJobSite.setText(serviceJob.getActionsOrRemarks());
        textViewLabelServiceNo.setText(serviceJob.getServiceNumber());
        textViewLabelTypeOfService.setText(serviceJob.getTypeOfService());
        textViewLabelTelephone.setText(serviceJob.getTelephone());
        textViewLabelFax.setText(serviceJob.getFax());
        textViewLabelEquipmentType.setText(serviceJob.getEquipmentType());
        textViewLabelModel.setText(serviceJob.getModelOrSerial());
        textViewComplaints.setText(serviceJob.getComplaintsOrSymptoms());
        textViewRemarksActions.setText(serviceJob.getActionsOrRemarks());
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
        protected void onPostExecute(final Boolean success) {
        }

        @Override
        protected void onCancelled() {
        }
    }*/
}