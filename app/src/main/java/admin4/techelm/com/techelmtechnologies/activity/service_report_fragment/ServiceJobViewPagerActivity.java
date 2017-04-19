package admin4.techelm.com.techelmtechnologies.activity.service_report_fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;

import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.menu.MainActivity;
import admin4.techelm.com.techelmtechnologies.adapter.ServiceJobPartsListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.ServiceJobRecordingsListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.ServiceJobUploadsListAdapter;
import admin4.techelm.com.techelmtechnologies.db.PartsDBUtil;
import admin4.techelm.com.techelmtechnologies.db.RecordingDBUtil;
import admin4.techelm.com.techelmtechnologies.db.ServiceJobDBUtil;
import admin4.techelm.com.techelmtechnologies.db.UploadsDBUtil;
import admin4.techelm.com.techelmtechnologies.activity.fragment_sample.LicensesFragment;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobNewPartsWrapper;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobNewReplacementPartsRatesWrapper;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobRecordingWrapper;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobUploadsWrapper;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm.ServiceJobBegin_POST;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_PARTS_REPLACEMENT_LIST;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_PREVIOUS_STATUS_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_SERVICE_KEY;


public class ServiceJobViewPagerActivity extends AppCompatActivity implements
        ServiceJobRecordingsListAdapter.CallbackInterface, // A. ServiceReport_FRGMT_BEFORE
        ServiceJobUploadsListAdapter.CallbackInterface,
        RecordingDBUtil.OnDatabaseChangedListener,
        ServiceJobDBUtil.OnDatabaseChangedListener, // [A & B & D]
        UploadsDBUtil.OnDatabaseChangedListener,
        ServiceJobPartsListAdapter.CallbackInterface, // B. PartReplacement_FRGMT_2
        PartsDBUtil.OnDatabaseChangedListener
        // OnTaskKill.onStopCallbackInterface // TODO: if user close the app permanently
{

    private static final String TAG = ServiceJobViewPagerActivity.class.getSimpleName();
    private static final int FRAGMENT_POSISTION_SERVICE_REPORT_BEFORE = 0;
    private static final int FRAGMENT_POSISTION_SERVICE_REPORT_AFTER = 1;
    private static final int FRAGMENT_POSISTION_PART_REPLACEMENT = 2;
    private static final int FRAGMENT_POSISTION_SIGNING_OFF = 3;
    private ServiceJobWrapper mServiceJobFromBundle; // From Calling Activity
    private String mPreviousStatusFromBundle; // From Calling Activity
    private List<ServiceJobNewReplacementPartsRatesWrapper> rateList;

    private ServiceJobDBUtil mSJDB; // For saving and delete of the Service Job

    // PAGER TAB SETUP
    private PagerSlidingTabStrip mTabPager;
    private ViewPager mViewPager;
    private ServiceJobFragmentPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_sliding_tab);

        setBackGroundLayout();

        if (fromBundle() != null) { // if Null don't show anything
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mPagerAdapter = new ServiceJobFragmentPagerAdapter(getSupportFragmentManager(),
                    this.mServiceJobFromBundle,
                    this.rateList);
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
        LinearLayout backgroundLayout = (LinearLayout) findViewById(R.id.activity_pager_sliding_tab_layout);
        backgroundLayout.setBackground(new ImageUtility(this).ResizeImage(R.drawable.background));
    }

    @Override
    public void onBackPressed() {
        backToLandingPage(1);
    }

    private void createdServiceJob(final ServiceJobWrapper sign) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Log.e("ServiceJobPageActivity", mServiceJobFromBundle.toString());
                Log.e("ServiceJobPageActivity2", sign.toString());
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

    // ToDO some check if not connected to Internet

    /**
     * This will update/revert the status from the server
     * Mode 1 - Back to Main Page
     * Mode 0 - onDestroy
     * @param mode
     */
    public void backToLandingPage(final int mode) {
        ServiceJobBegin_POST beginServiceJob = new ServiceJobBegin_POST();
        beginServiceJob.setOnEventListener(new ServiceJobBegin_POST.OnEventListener() {
            @Override
            public void onEvent() {
                // TODO: Close progress dialog here
                // TODO: Test Response if OK or not
            }

            @Override
            public void onError(String message) {
            }

            @Override
            public void onEventResult(WebResponse response) {
                if (mode == 1) {
                    Intent intent = new Intent(ServiceJobViewPagerActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle = ActivityOptions.makeCustomAnimation(ServiceJobViewPagerActivity.this,
                            R.anim.left_to_right, R.anim.right_to_left).toBundle();
                    startActivity(intent, bundle);
                    finish();
                }
            }
        });
        beginServiceJob.postRevertStatus(this.mServiceJobFromBundle.getID(), this.mPreviousStatusFromBundle);
    }

    /**
     * PARSING data ServiceJob from Bundle passed by the
     *      MainActivity => CalendarFragment
     *      Used by ServiceTaskCompleted
     * @return - ServiceJobWrapper | NULL if no data has been submitted
     */
    private ServiceJobWrapper fromBundle() {
        Intent intent = getIntent();
        this.mPreviousStatusFromBundle = intent.getStringExtra(SERVICE_JOB_PREVIOUS_STATUS_KEY);
        this.rateList = intent.getParcelableArrayListExtra(SERVICE_JOB_PARTS_REPLACEMENT_LIST);
        return this.mServiceJobFromBundle = (ServiceJobWrapper) intent.getParcelableExtra(SERVICE_JOB_SERVICE_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
    private int getCurrentPosition() { return mViewPager.getCurrentItem(); }
    private int getItem(int i) {
        return getCurrentPosition() + i;
    }

    /******* A. CALLBACKS from ServiceReport_FRGMT_BEFORE & ServiceReport_FRGMT_AFTER ********/
    private ServiceReport_FRGMT_BEFORE getFragmentServiceReport_BEFORE() {
        return (ServiceReport_FRGMT_BEFORE) mPagerAdapter.getActiveFragment(mViewPager, FRAGMENT_POSISTION_SERVICE_REPORT_BEFORE);
    }

    private ServiceReport_FRGMT_AFTER getFragmentServiceReport_AFTER() {
        return (ServiceReport_FRGMT_AFTER) mPagerAdapter.getActiveFragment(mViewPager, FRAGMENT_POSISTION_SERVICE_REPORT_AFTER);
    }

    @Override
    public void onHandleRecordingsSelection(
            int position, ServiceJobRecordingWrapper serviceJobRecordingWrapper, int mode) {
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_BEFORE)
            getFragmentServiceReport_BEFORE().fromActivity_onHandleRecordingsSelection(position, serviceJobRecordingWrapper, mode);
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_AFTER)
            getFragmentServiceReport_AFTER().fromActivity_onHandleRecordingsSelection(position, serviceJobRecordingWrapper, mode);
    }
    @Override
    public void onHandleDeleteRecordingsFromListSelection(final int id) {
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_BEFORE)
            getFragmentServiceReport_BEFORE().fromActivity_onHandleDeleteRecordingsFromListSelection(id);
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_AFTER)
            getFragmentServiceReport_AFTER().fromActivity_onHandleDeleteRecordingsFromListSelection(id);
    }
    @Override
    public void onHandlePlayFromListSelection(ServiceJobRecordingWrapper serviceJobRecordingWrapper) {
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_BEFORE)
            getFragmentServiceReport_BEFORE().fromActivity_onHandlePlayFromListSelection(serviceJobRecordingWrapper);
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_AFTER)
            getFragmentServiceReport_AFTER().fromActivity_onHandlePlayFromListSelection(serviceJobRecordingWrapper);
    }

    @Override
    public void onHandleUploadsSelection(
            int position, ServiceJobUploadsWrapper serviceJobRecordingWrapper, int mode) {
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_BEFORE)
            getFragmentServiceReport_BEFORE().fromActivity_onHandleUploadsSelection(position, serviceJobRecordingWrapper, mode);
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_AFTER)
            getFragmentServiceReport_AFTER().fromActivity_onHandleUploadsSelection(position, serviceJobRecordingWrapper, mode);
    }
    @Override
    public void onHandleDeleteUploadsFromListSelection(final int id) {
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_BEFORE)
            getFragmentServiceReport_BEFORE().fromActivity_onHandleDeleteUploadsFromListSelection(id);
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_AFTER)
            getFragmentServiceReport_AFTER().fromActivity_onHandleDeleteUploadsFromListSelection(id);
    }
    @Override
    public void onHandleViewUploadFromListSelection(ServiceJobUploadsWrapper serviceJobRecordingWrapper) {
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_BEFORE)
            getFragmentServiceReport_BEFORE().fromActivity_onHandleViewUploadFromListSelection(serviceJobRecordingWrapper);
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_AFTER)
            getFragmentServiceReport_AFTER().fromActivity_onHandleViewUploadFromListSelection(serviceJobRecordingWrapper);
    }

    @Override
    public void onNewRecordingsEntryAdded(String fileName) {
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_BEFORE)
            getFragmentServiceReport_BEFORE().fromActivity_onNewRecordingsEntryAdded(fileName);
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_AFTER)
            getFragmentServiceReport_AFTER().fromActivity_onNewRecordingsEntryAdded(fileName);
    }
    @Override
    public void onRecordingsEntryRenamed(String fileName) {
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_BEFORE)
            getFragmentServiceReport_BEFORE().fromActivity_onRecordingsEntryRenamed(fileName);
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_AFTER)
            getFragmentServiceReport_AFTER().fromActivity_onRecordingsEntryRenamed(fileName);
    }
    @Override
    public void onRecordingsEntryDeleted() {
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_BEFORE)
            getFragmentServiceReport_BEFORE().fromActivity_onRecordingsEntryDeleted();
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_AFTER)
            getFragmentServiceReport_AFTER().fromActivity_onRecordingsEntryDeleted();
    }

    @Override
    public void onNewSJEntryAdded(String serviceNum) {
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_BEFORE)
            getFragmentServiceReport_BEFORE().fromActivity_onNewSJEntryAdded(serviceNum);
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_AFTER)
            getFragmentServiceReport_AFTER().fromActivity_onNewSJEntryAdded(serviceNum);
        getFragmentSigningOff().fromActivity_onNewSJEntryAdded(serviceNum);
    }
    @Override
    public void onSJEntryUpdated(String remarks) {
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_BEFORE)
            getFragmentServiceReport_BEFORE().fromActivity_onSJEntryRenamed(remarks);
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_AFTER)
            getFragmentServiceReport_AFTER().fromActivity_onSJEntryRenamed(remarks);
        getFragmentSigningOff().fromActivity_onSJEntryRenamed(remarks);
    }
    @Override
    public void onSJEntryDeleted() {
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_BEFORE)
            getFragmentServiceReport_BEFORE().fromActivity_onSJEntryDeleted();
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_AFTER)
            getFragmentServiceReport_AFTER().fromActivity_onSJEntryDeleted();
        getFragmentSigningOff().fromActivity_onSJEntryDeleted();
    }

    /******* A. END CALLBACKS from ServiceReport_FRGMT_BEFORE & ServiceReport_FRGMT_AFTER ********/

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
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_BEFORE)
            getFragmentServiceReport_BEFORE().fromActivity_onNewUploadsDBEntryAdded(fileName);
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_AFTER)
            getFragmentServiceReport_AFTER().fromActivity_onNewUploadsDBEntryAdded(fileName);
    }

    @Override
    public void onUploadsDBEntryRenamed(String fileName) {
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_BEFORE)
            getFragmentServiceReport_BEFORE().fromActivity_onUploadsDBEntryRenamed(fileName);
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_AFTER)
            getFragmentServiceReport_AFTER().fromActivity_onUploadsDBEntryRenamed(fileName);
    }
    @Override
    public void onUploadsDBEntryDeleted() {
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_BEFORE)
            getFragmentServiceReport_BEFORE().fromActivity_onUploadsDBEntryDeleted();
        if (getCurrentPosition() == FRAGMENT_POSISTION_SERVICE_REPORT_AFTER)
            getFragmentServiceReport_AFTER().fromActivity_onUploadsDBEntryDeleted();
    }
    /*********** B. CALLBACKS END from PartReplacement_FRGMT_2 ***********/


    /*********** D. CALLBACKS from SigningOff_FRGMT_4 ***********/
    private SigningOff_FRGMT_4 getFragmentSigningOff() {
        return (SigningOff_FRGMT_4) mPagerAdapter.getActiveFragment(mViewPager, FRAGMENT_POSISTION_SIGNING_OFF);
    }

    /*********** D. CALLBACKS END from SigningOff_FRGMT_4 ***********/

}
