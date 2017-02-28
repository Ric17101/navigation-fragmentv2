package admin4.techelm.com.techelmtechnologies.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.CalendarListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.ServiceJobListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.UnsignedServiceJobListAdapter;
import admin4.techelm.com.techelmtechnologies.fragment_sample.PrimaryFragment;
import admin4.techelm.com.techelmtechnologies.fragment_sample.SentFragment;
import admin4.techelm.com.techelmtechnologies.fragment_sample.TabFragment;
import admin4.techelm.com.techelmtechnologies.login.LoginActivity2;
import admin4.techelm.com.techelmtechnologies.service_report.ServiceReport_1;
import admin4.techelm.com.techelmtechnologies.servicejob.ServiceJobFragmentTab;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.utility.UIThreadHandler;

public class MainActivity extends AppCompatActivity implements
        ServiceJobListAdapter.CallbackInterface,
        CalendarListAdapter.CallbackInterface,
        UnsignedServiceJobListAdapter.CallbackInterface {

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
                        fragmentTransaction.replace(R.id.containerView, new SentFragment()).commit();
                        break;
                    case R.id.nav_process_pe:
                        FragmentTransaction fragmentProcessTransaction = mFragmentManager.beginTransaction();
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
     * @param mode
     */
    @Override
    public void onHandleSelection(int position, ServiceJobWrapper serviceJob, int mode) {
        String strOut = "Position: " + position + " \nService Job:" + serviceJob.toString() + "\nMode: " + mode;
        System.out.print(strOut);
    }


    /**
     * Represents an asynchronous Task
     * UITask mAuthTask = new UITask();
        mAuthTask.execute((Void) null);
     */
    public class UITask extends AsyncTask<Void, Void, Boolean> {
        UITask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
                // init_DrawerNav();
            } catch (InterruptedException e) {
                return false;
            }


            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
        }

        @Override
        protected void onCancelled() {
        }
    }
}