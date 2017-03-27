package admin4.techelm.com.techelmtechnologies.service_report_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.ServiceJobPartsListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.ServiceJobRecordingsListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.ServiceJobUploadsListAdapter;
import admin4.techelm.com.techelmtechnologies.db.PartsDBUtil;
import admin4.techelm.com.techelmtechnologies.db.RecordingDBUtil;
import admin4.techelm.com.techelmtechnologies.db.ServiceJobDBUtil;
import admin4.techelm.com.techelmtechnologies.db.UploadsDBUtil;
import admin4.techelm.com.techelmtechnologies.fragment_sample.LicensesFragment;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobNewPartsWrapper;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobRecordingWrapper;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobUploadsWrapper;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;


public class ServiceJobViewPagerActivity extends AppCompatActivity implements
        ServiceJobRecordingsListAdapter.CallbackInterface, // A. ServiceReport_FRGMT_1
        ServiceJobUploadsListAdapter.CallbackInterface,
        RecordingDBUtil.OnDatabaseChangedListener,
        ServiceJobDBUtil.OnDatabaseChangedListener, // [A & B & D]
        UploadsDBUtil.OnDatabaseChangedListener,
        ServiceJobPartsListAdapter.CallbackInterface, // B. PartReplacement_FRGMT_2
        PartsDBUtil.OnDatabaseChangedListener
{

    private static final String LOG_TAG = ServiceJobViewPagerActivity.class.getSimpleName();
    private static final String RECORD_JOB_SERVICE_KEY = "SERVICE_JOB";
    private static final int FRAGMENT_POSISTION_SERVICE_REPORT = 0;
    private static final int FRAGMENT_POSISTION_SERVICE_REPORT_B = 1;
    private static final int FRAGMENT_POSISTION_PART_REPLACEMENT = 2;
    private static final int FRAGMENT_POSISTION_SIGNING_OFF = 3;
    private ServiceJobWrapper mServiceJobFromBundle; // From Calling Activity

    private ServiceJobDBUtil mSJDB; // For saving and delete of the Service Job

    // PAGER TAB SETUP
    private PagerSlidingTabStrip mTabPager;
    private ViewPager mViewPager;
    private ServiceJobFragmentPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_sliding_tab);

        if (fromBundle() != null) { // if Null don't show anything
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mPagerAdapter = new ServiceJobFragmentPagerAdapter(getSupportFragmentManager(),
                    this.mServiceJobFromBundle);
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.setOffscreenPageLimit(3); // Set to Four Pages
            mTabPager = (PagerSlidingTabStrip) findViewById(R.id.tabsStrip);
            mTabPager.setViewPager(mViewPager);

            // Save Service Job from Bundle
            createdServiceJob(this.mServiceJobFromBundle);
        } else {
            Snackbar.make(this.findViewById(android.R.id.content),
                    "No data selected from calendar.", Snackbar.LENGTH_LONG)
                    .setAction("OK", null).show();
        }

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }*/


    }

    private void createdServiceJob(final ServiceJobWrapper sign) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Log.e("ServiceJobPageActivity", mServiceJobFromBundle.toString());
                mSJDB = new ServiceJobDBUtil(ServiceJobViewPagerActivity.this);
                mSJDB.open();
                mSJDB.addServiceJob(sign);
                mSJDB.close();
            }
        };
        new Thread(run).start();
    }

    public void deleteServiceJob() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                mSJDB = new ServiceJobDBUtil(ServiceJobViewPagerActivity.this);
                mSJDB.open();
                mSJDB.removeServiceJob(String.valueOf(mServiceJobFromBundle.getID()));
                mSJDB.close();
            }
        };
        new Thread(run).start();
    }



    /**
     * PARSING data ServiceJob from Bundle passed by the
     *      MainActivity => CalendarFragment
     * @return - ServiceJobWrapper | NULL if no data has been submitted
     */
    private ServiceJobWrapper fromBundle() {
        Intent intent = getIntent();
        return this.mServiceJobFromBundle = (ServiceJobWrapper) intent.getParcelableExtra(RECORD_JOB_SERVICE_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Handle presses on the action bar items
        /*switch (item.getItemId()) {
            case R.id.action_licenses:
                openLicenses();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }*/
        return super.onOptionsItemSelected(item);
    }

    public void openLicenses() {
        LicensesFragment licensesFragment = new LicensesFragment();
        licensesFragment.show(getSupportFragmentManager().beginTransaction(), "dialog_licenses");
    }

    /**
     * Called from Fragments then set the Page Navigation
     * @param nextOrPrevious -
     *      +1 - NEXT
     *      -1 - PREVIOUS
     */
    public void fromFragmentNavigate(int nextOrPrevious) {
        mViewPager.setCurrentItem(getItem(nextOrPrevious), true);
    }
    private int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }

    /*********** A. CALLBACKS from ServiceReport_FRGMT_1 ***********/
    private ServiceReport_FRGMT_1 getFragmentServiceReport() {
        return (ServiceReport_FRGMT_1) mPagerAdapter.getActiveFragment(mViewPager, FRAGMENT_POSISTION_SERVICE_REPORT);
    }

    @Override
    public void onHandleRecordingsSelection(
            int position, ServiceJobRecordingWrapper serviceJobRecordingWrapper, int mode) {
        getFragmentServiceReport()
                .fromActivity_onHandleRecordingsSelection(position, serviceJobRecordingWrapper, mode);
    }
    @Override
    public void onHandleDeleteRecordingsFromListSelection(final int id) {
        getFragmentServiceReport().fromActivity_onHandleDeleteRecordingsFromListSelection(id);
    }
    @Override
    public void onHandlePlayFromListSelection(ServiceJobRecordingWrapper serviceJobRecordingWrapper) {
        getFragmentServiceReport().fromActivity_onHandlePlayFromListSelection(serviceJobRecordingWrapper);
    }

    @Override
    public void onHandleUploadsSelection(
            int position, ServiceJobUploadsWrapper serviceJobRecordingWrapper, int mode) {
        getFragmentServiceReport()
                .fromActivity_onHandleUploadsSelection(position, serviceJobRecordingWrapper, mode);
    }
    @Override
    public void onHandleDeleteUploadsFromListSelection(final int id) {
        getFragmentServiceReport().fromActivity_onHandleDeleteUploadsFromListSelection(id);
    }
    @Override
    public void onHandleViewUploadFromListSelection(ServiceJobUploadsWrapper serviceJobRecordingWrapper) {
        getFragmentServiceReport().fromActivity_onHandleViewUploadFromListSelection(serviceJobRecordingWrapper);
    }

    @Override
    public void onNewRecordingsEntryAdded(String fileName) {
        getFragmentServiceReport().fromActivity_onNewRecordingsEntryAdded(fileName);
    }
    @Override
    public void onRecordingsEntryRenamed(String fileName) {
        getFragmentServiceReport().fromActivity_onRecordingsEntryRenamed(fileName);
    }
    @Override
    public void onRecordingsEntryDeleted() {
        getFragmentServiceReport().fromActivity_onRecordingsEntryDeleted();
    }

    @Override
    public void onNewSJEntryAdded(String serviceNum) {
        getFragmentServiceReport().fromActivity_onNewSJEntryAdded(serviceNum);
        getFragmentSigningOff().fromActivity_onNewSJEntryAdded(serviceNum);
    }
    @Override
    public void onSJEntryRenamed(String fileName) {
        getFragmentServiceReport().fromActivity_onSJEntryRenamed(fileName);
        getFragmentSigningOff().fromActivity_onSJEntryRenamed(fileName);
    }
    @Override
    public void onSJEntryDeleted() {
        getFragmentServiceReport().fromActivity_onSJEntryDeleted();
        getFragmentSigningOff().fromActivity_onSJEntryDeleted();
    }

    /*********** A. END CALLBACKS from ServiceReport_FRGMT_1 ***********/


    /*********** B. CALLBACKS from PartReplacement_FRGMT_2 ***********/
    @Override
    public void onNewPartsEntryAdded(String fileName) {
        getFragmentPartReplacement().fromActivity_onNewPartsEntryAdded(fileName);
    }
    @Override
    public void onPartsEntryRenamed(String fileName) {
        getFragmentPartReplacement().fromActivity_onPartsEntryRenamed(fileName);
    }
    @Override
    public void onPartsEntryDeleted() {
        getFragmentPartReplacement().fromActivity_onPartsEntryDeleted();
    }

    private PartReplacement_FRGMT_2 getFragmentPartReplacement() {
        return (PartReplacement_FRGMT_2) mPagerAdapter.getActiveFragment(mViewPager, FRAGMENT_POSISTION_PART_REPLACEMENT);
    }

    @Override
    public void onHandlePartsSelection(int position, ServiceJobNewPartsWrapper serviceJobNewPartsWrapper, int mode) {
        getFragmentPartReplacement().fromActivity_onHandlePartsSelection(
                position, serviceJobNewPartsWrapper, mode);
    }
    @Override
    public void onHandleDeletePartsFromListSelection(final int id) {
        getFragmentPartReplacement().fromActivity_onHandleDeletePartsFromListSelection(id);
    }
    @Override
    public void onHandleViewPartFromListSelection(ServiceJobNewPartsWrapper serviceJobPartWrapper) {
        getFragmentPartReplacement()
                .fromActivity_onHandleViewPartFromListSelection(serviceJobPartWrapper);
    }

    @Override
    public void onNewUploadsDBEntryAdded(String fileName) {
        getFragmentServiceReport().fromActivity_onNewUploadsDBEntryAdded(fileName);
    }

    @Override
    public void onUploadsDBEntryRenamed(String fileName) {
        getFragmentServiceReport().fromActivity_onUploadsDBEntryRenamed(fileName);
    }
    @Override
    public void onUploadsDBEntryDeleted() {
        getFragmentServiceReport().fromActivity_onUploadsDBEntryDeleted();
    }
    /*********** B. CALLBACKS END from PartReplacement_FRGMT_2 ***********/


    /*********** D. CALLBACKS from SigningOff_FRGMT_4 ***********/
    private SigningOff_FRGMT_4 getFragmentSigningOff() {
        return (SigningOff_FRGMT_4) mPagerAdapter.getActiveFragment(mViewPager, FRAGMENT_POSISTION_SIGNING_OFF);
    }

    /*********** D. CALLBACKS END from SigningOff_FRGMT_4 ***********/







}
