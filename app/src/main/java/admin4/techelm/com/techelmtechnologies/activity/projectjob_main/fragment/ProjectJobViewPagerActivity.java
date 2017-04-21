package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.astuetz.PagerSlidingTabStrip;

import java.util.HashMap;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.login.SessionManager;
import admin4.techelm.com.techelmtechnologies.activity.menu.MainActivity;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b2b3.CompletionDateFragmentTest;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b2b3.NonConformanceAndDateFragmentTest;
import admin4.techelm.com.techelmtechnologies.activity.servicejob_main.PopulateServiceJobViewDetails;
import admin4.techelm.com.techelmtechnologies.adapter.PreInstallationSiteSurveyListAdapter;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_CONTINUE_TASK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_START_TASK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_VIEW_TASK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B1;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B2;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B3;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_TYPE_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FRAGMENT_POSITION_2;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FRAGMENT_POSITION_3;

public class ProjectJobViewPagerActivity extends FragmentActivity implements
        PreInstallationSiteSurveyListAdapter.CallbackInterface,
        DatePickerDialog.OnDateSetListener {

    private static final String TAG = ProjectJobViewPagerActivity.class.getSimpleName();

    SessionManager mSession;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    // PAGER TAB SETUP
    private PagerSlidingTabStrip mTabPager;
    private ViewPager mViewPager;
    private ProjectJobFragmentTab mPagerAdapter;

    // PAGER TAB Type of Form
    private int modeOfForm = 0;

    // B. Project Job Setup - B2,B3
    private NonConformanceAndDateFragmentTest ncadft;
    private CompletionDateFragmentTest cdft;
    private int _day;
    private int _month;
    private int _birthYear;
    DatePickerDialog mDialog;
    private int mFragmentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectjob);

        fromBundle();

        setBackGroundLayout();

        loginSessionTest();

        init_ViewPager();
    }

    /**
     * Can be used to title the TAB on the ViewPagerActivity
     * By using the MODE CONSTANT of modeOfForm
     */
    private void fromBundle() {
        Intent intent = getIntent();
        this.modeOfForm = intent.getIntExtra(PROJECT_JOB_FORM_TYPE_KEY, 0);

        setViewPagerTitleBar();
    }

    private void init_ViewPager() {
        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mPagerAdapter = new ProjectJobFragmentTab().newInstance(this.modeOfForm);
        mFragmentTransaction.replace(R.id.containerView, mPagerAdapter).commit(); // tO RENDER THE  1st TAB on MAIN MENU

        this.mViewPager = mPagerAdapter.getViewPager();
    }

    private void setViewPagerTitleBar() {
        TextView textViewProjectJobTitleTab = (TextView) findViewById(R.id.textViewProjectJobTitleTab);
        switch (this.modeOfForm) {
            case PROJECT_JOB_FORM_B1: textViewProjectJobTitleTab.setText(R.string.piss); break;
            case PROJECT_JOB_FORM_B2: textViewProjectJobTitleTab.setText(R.string.ipipw); break;
            case PROJECT_JOB_FORM_B3: textViewProjectJobTitleTab.setText(R.string.ipieps); break;
            default: break;
        }
    }

    /**
     * This method uses LargeHeap and Hardware Acceleration on the AndroidManifest file in order to
     * set the Background image of the App/Activities
     * @ called at
     *      MainActivity
     *      ServiceJobViewPagerActivity
     *      Login
     *      ServiceReport_TaskCompleted_5
     *      ProjectJobViewPagerActivity
     */
    private void setBackGroundLayout() {
        LinearLayout backgroundLayout = (LinearLayout) findViewById(R.id.activity_projectjob);
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
        super.onBackPressed();
        Intent i = new Intent(ProjectJobViewPagerActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_popup_exit);
    }

    @Override
    public void onHandleSelection(int position, ServiceJobWrapper serviceJob, int mode) {
        switch(mode) {
            case ACTION_START_TASK:
                Log.e(TAG, "This is ACTION_START_TASK");
                showUpdateComment();
                //fromFragmentNavigate(1);
                break;
            case ACTION_CONTINUE_TASK:
                Log.e(TAG, "This is ACTION_CONTINUE_TASK");
                fromFragmentNavigate(1);
                break;
            case ACTION_VIEW_TASK:
                showMDialogSJDetails(serviceJob);
                Log.e(TAG, "This is ACTION_VIEW_TASK");
                break;
            default:
                Log.e(TAG, "This is ACTION_VIEW_TASK");
                break;
        }
    }

    private void showMDialogSJDetails(ServiceJobWrapper serviceJob) {
        MaterialDialog md = new MaterialDialog.Builder(ProjectJobViewPagerActivity.this)
                .title("PROJECT JOB " + serviceJob.getServiceNumber())
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
     * TODO:  THIS should do the reversion of status when task has been cancelled or barcprecssesesseses
     * @param mode
     */
    public void backToLandingPage(final int mode) {
        Intent intent = new Intent(ProjectJobViewPagerActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = ActivityOptions.makeCustomAnimation(ProjectJobViewPagerActivity.this,
                R.anim.left_to_right, R.anim.right_to_left).toBundle();
        startActivity(intent, bundle);
        finish();
    }

    /*************** BUTTON FOR NEXT AND PREVIOUS ACTIONS ***************/
    /**
     * Called from Fragments then set the Page Navigation
     * @param nextOrPrevious -
     *      +1 - NEXT
     *      -1 - PREVIOUS
     */
    public void fromFragmentNavigate(int nextOrPrevious) {
        hideKeyboard();
        mPagerAdapter.setCurrentTab(nextOrPrevious);
    }
    public void fromFragmentNavigateToTaskList() {
        hideKeyboard();
        mPagerAdapter.setCurrentToFirstTab();
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(), 0);
    }
    /*************** END BUTTON FOR NEXT AND PREVIOUS ACTIONS ***************/


    /*
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
    */

    /*************** B1 - Pre Installation Site Survey ***************/


    /*************** END B1 - Pre Installation Site Survey ***************/


    /*************** B2 - In-process inspection (PW) ***************/
    /*************** B3 - In-process inspection (EPS) ***************/
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        _birthYear = year;
        _month = month + 1;
        _day = dayOfMonth;

        switch (this.mFragmentPosition) {
            case PROJECT_JOB_FRAGMENT_POSITION_2: ncadft.updateDisplay(_birthYear,_month,_day); break;
            case PROJECT_JOB_FRAGMENT_POSITION_3: cdft.updateDisplay(_birthYear,_month,_day); break;
            default: break;
        }
    }

    /**
     * This called onClick of EditText
     * @param fragment - Current Fragment Calling
     * @param fragmentPosition - Position being paased by the Calling Fragment
     */
    @TargetApi(Build.VERSION_CODES.N)
    public void showDateDialog(Fragment fragment, int fragmentPosition) {
        this.initCallingFragment(fragment, fragmentPosition);

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        mDialog = new DatePickerDialog(this, this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        mDialog.show();
    }

    /**
     * This only to set what Fragment Calls the Calendar Event from the Fragment
     *  then set the EditText/Display only
     * @param fragment
     */
    private void initCallingFragment(Fragment fragment, int fragmentPosition) {
        this.mFragmentPosition = fragmentPosition;
        switch (fragmentPosition) {
            case PROJECT_JOB_FRAGMENT_POSITION_2 : ncadft = (NonConformanceAndDateFragmentTest)fragment; break;
            case PROJECT_JOB_FRAGMENT_POSITION_3 : cdft = (CompletionDateFragmentTest) fragment; break;
            default: break;
        }
    }

    public void showUpdateComment() {
        MaterialDialog md = new MaterialDialog.Builder(ProjectJobViewPagerActivity.this)
                .title("Do you want to update comment?")
                // .customView(R.layout.i_edit_text_remarks, true)
                .limitIconToDefaultSize()
                .positiveText("YES")
                .negativeText("NO")
                .neutralText("N/A")
                .iconRes(R.mipmap.edit_icon)
                .autoDismiss(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        fromFragmentNavigate(1);
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build();

        /*TextView textViewTitleCommentRemarks = (TextView) md.getCustomView().findViewById(R.id.textViewTitleCommentRemarks);
        textViewTitleCommentRemarks.setText("");*/

        /*new PopulateServiceJobViewDetails()
                .populateServiceJobDetailsMaterialDialog(md.getCustomView(), serviceJob, View.GONE, TAG);*/
        md.show();
    }

    /**
     * Called After Clicking Next at NonConformanceAndDateFragmentTest
     */
    public void showOKToSaveRectificationDate() {
        MaterialDialog md = new MaterialDialog.Builder(ProjectJobViewPagerActivity.this)
                .title("DO YOU WANT TO SAVE RECTIFICATION DATE?")
                // .customView(R.layout.i_edit_text_remarks, true)
                .limitIconToDefaultSize()
                .positiveText("YES")
                .negativeText("NO")
                .iconRes(R.mipmap.ic_action_info)
                .autoDismiss(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        fromFragmentNavigate(1);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        fromFragmentNavigateToTaskList();
                        showUpdateComment();
                    }
                })
                .build();
        md.show();
    }

    /*************** END B2 - In-process inspection (PW) ***************/
    /*************** END B3 - In-process inspection (EPS) ***************/
}