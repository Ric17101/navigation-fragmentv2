package admin4.techelm.com.techelmtechnologies.activity.menu;/*
package com.techelmtechnologies.admin4.menu;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.techelmtechnologies.admin4.R;
import com.techelmtechnologies.admin4.adapter.SJ_ListAdapter;
import com.techelmtechnologies.admin4.servicejob.CalendarFragment;
import com.techelmtechnologies.admin4.servicejob.ServiceJobWrapper;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * TODO: REMOVE ASYSNTASK if necessary to see what is wrong with the runnables
 *//*

public class MainActivity_old extends AppCompatActivity implements SJ_ListAdapter.ProjectJobListener {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpNavigationMenu();
//        UpdateJobServiceTask task = new UpdateJobServiceTask();
//        task.execute("");
    }

    private void setUpNavigationMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_menu); // TODO: assert???
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
            //setupDrawerHeaderContent(navigationView);
        }

        // FOOTER NAV View (User Signout)
        NavigationView navigationUserView = (NavigationView) findViewById(R.id.nav_view_user);
        if (navigationUserView != null) {
            setupUserDrawerContent(navigationView);
        }


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupServiceJobViewPager(viewPager);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (AppCompatDelegate.getDefaultNightMode()) {
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                menu.findItem(R.id.menu_night_mode_system).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_AUTO:
                menu.findItem(R.id.menu_night_mode_auto).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                menu.findItem(R.id.menu_night_mode_night).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_NO:
                menu.findItem(R.id.menu_night_mode_day).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_night_mode_system:
                setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case R.id.menu_night_mode_day:
                setNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case R.id.menu_night_mode_night:
                setNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case R.id.menu_night_mode_auto:
                setNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNightMode(@AppCompatDelegate.NightMode int nightMode) {
        AppCompatDelegate.setDefaultNightMode(nightMode);

        if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        }
    }

    */
/**
     * Setting up the ViewPager for the Menus For SERVICE JOBS
     *
     * @param viewPager
     *//*

    private void setupServiceJobViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new CalendarFragment(), "Calendar");
        */
/*adapter.addFragment(new CheeseListFragment(), "Category 1");
        adapter.addFragment(new CheeseListFragment(), "Category 2");
        adapter.addFragment(new CheeseListFragment(), "Category 3");*//*

        viewPager.setAdapter(adapter);
    }

    */
/**
     * Specifically for the HeaderClose Button of the Navigation View on the NavBars
     *
     * @param navigationView
     *//*

    private void setupDrawerHeaderContent(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        RelativeLayout relativeLayoutNavigationHeader = (RelativeLayout) headerView.findViewById(R.id.nav_view_header);
        ImageView hearNavCloseButton = (ImageView) relativeLayoutNavigationHeader.findViewById(R.id.imageButtonCloseNav);
        hearNavCloseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = v.getID();
                        switch (id) {
                            case R.id.imageButtonCloseNav:
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                break;
                        }
                    }
                }
        );
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                    */
/*menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    return true;*//*

                        menuItem.setChecked(true);
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.nav_servicejobs:
                                //Do some thing here
                                // add navigation drawer item onclick method here
                                Log.d("setupDrawerContent","I'm in Service Jobs");
                                System.out.println("I'm in Service Jobs");
                                break;
                            case R.id.nav_checklist:
                                System.out.println("I'm in SIT WALK CHECKLIST");
                                break;
                            case R.id.nav_process_pe:
                                System.out.println("I'm in SITE IN PROCESS INSPECTION (PE)");
                                break;
                            case R.id.nav_process_eps:
                                System.out.println("I'm in SITE IN PROCESS INSPECTION (EPS WORKS)");
                                break;
                            case R.id.nav_toolbox:
                                System.out.println("I'm in TOOLBOX MEETING");
                                break;
                        }
                        return false;
                    }
                });
    }

    public void setupUserDrawerContent(NavigationView navigationUserView) {
        navigationUserView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        //menuItem.setChecked(true);
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.nav_signout:
                                System.out.println("I'm in Signout");
                                break;
                        }
                        return false;
                    }
                });
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragmentType, String title) {
            mFragments.add(fragmentType);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }


    */
/**
     * OnClick of Items Inside the Service Project List
     *
     * @param position   - the position
     * @param servicejob - the text to pass back
     * @param mode       - what button is what action
     *//*

    @Override
    public void onHandleRecordingsSelection(int position, ServiceJobWrapper servicejob, int mode) {
        System.out.println("I'm in onActivityResult" + position + " Data: " + servicejob + " Mode: " + mode);
    }

    private class UpdateJobServiceTask extends AsyncTask<String, Void, String> {
        public UpdateJobServiceTask() { }

        @Override
        protected String doInBackground(String... params) {
            setUpNavigationMenu();
            return null;
        }

        @Override
        protected void onPostExecute(String aResponse) {
            //textView.setText(aResponse);
            System.gc();
        }
    }
}
*/
