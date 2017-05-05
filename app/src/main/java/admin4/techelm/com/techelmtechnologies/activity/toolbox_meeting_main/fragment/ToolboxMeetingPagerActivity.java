package admin4.techelm.com.techelmtechnologies.activity.toolbox_meeting_main.fragment;

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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.astuetz.PagerSlidingTabStrip;

import java.util.HashMap;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.login.SessionManager;
import admin4.techelm.com.techelmtechnologies.activity.menu.MainActivity;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.helper.FragmentSetListHelper_ProjectJob;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;
import admin4.techelm.com.techelmtechnologies.utility.dialog.InterfaceDialogHolder;
import admin4.techelm.com.techelmtechnologies.utility.dialog.OpenDialog;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.LANDING_PAGE_ACTIVE_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.NAVIGATION_DRAWER_SELECTED_TOOLBOX;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B1;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B2;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B3;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_TYPE_KEY;

public class ToolboxMeetingPagerActivity extends FragmentActivity implements
        // TM_ListAdapter.ProjectJobListener,
        DatePickerDialog.OnDateSetListener,
        OpenDialog {

    private static final String TAG = ToolboxMeetingPagerActivity.class.getSimpleName();

    SessionManager mSession;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    // PAGER TAB SETUP
    private PagerSlidingTabStrip mTabPager;
    private ViewPager mViewPager;
    private ToolboxMeetingFragmentTab mPagerAdapter;

    // PAGER TAB Type of Form
    private int modeOfForm = 0;

    DatePickerDialog mDialog;
    private EditText editTextB2B3CompletionDate; // This is used for multiple pop up views
    private int completionDateClicked = 0;
    private EditText editTextB2B3TargetCompletionDate; // This is used for multiple pop up views
    private int completionTargetDateClicked = 0;
    private EditText editTextB2RectificationDate;
    private int rectificationDateClicked = 0;


    private int mFragmentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbox_meeting_list);

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
        this.modeOfForm = intent.getIntExtra(PROJECT_JOB_FORM_TYPE_KEY, 0); // This is not used yet

        setViewPagerTitleBar();
    }

    private void init_ViewPager() {
        /*
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mPagerAdapter = new ToolboxMeetingFragmentTab().newInstance();
        mFragmentTransaction.replace(R.id.containerView, mPagerAdapter).commit(); // tO RENDER THE  1st TAB on MAIN MENU

        this.mViewPager = mPagerAdapter.getViewPager();
    }

    private void setViewPagerTitleBar() {
        TextView textViewProjectJobTitleTab = (TextView) findViewById(R.id.textViewToolboxTitleTab);
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
     *      ToolboxMeetingPagerActivity
     */
    private void setBackGroundLayout() {
        LinearLayout backgroundLayout = (LinearLayout) findViewById(R.id.activity_toolbox);
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
        backToLandingPage(1);
    }

    /*@Override
    public void onHandleSelection(int position, ProjectJobWrapper serviceJob, int mode) {
        switch (mode) {
            case ACTION_START_DRAWING :
                if (this.modeOfForm == PROJECT_JOB_FORM_B1)
                    fromFragmentNavigate(1);
                else // This is for B2 and B3
                    showB2B3FormDialog();
                break;
            case ACTION_START_TASK :
                fromFragmentNavigate(1);
                Log.e(TAG, "This is ACTION_START_TASK");
                // onClickNextButton();
                break;
            case ACTION_CONTINUE_TASK :
                Log.e(TAG, "This is ACTION_CONTINUE_TASK");
                fromFragmentNavigate(1);
                break;
            case ACTION_VIEW_TASK :
                showMDialogSJDetails(serviceJob);
                Log.e(TAG, "This is ACTION_VIEW_TASK");
                break;
            case ACTION_START_CORRECTIVE_ACTION_FORM:
                showB2B3CorrectiveActionFormDialog();
                break;
            default :
                Log.e(TAG, "This is ACTION_VIEW_TASK");
                break;
        }
    }*/

    /**
     * TODO:  THIS should do the reversion of status when task has been cancelled or barcprecssesesseses
     *  Like on the ServiceJob
     * @param mode
     */
    public void backToLandingPage(final int mode) {
        Intent intent = new Intent(ToolboxMeetingPagerActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(LANDING_PAGE_ACTIVE_KEY, NAVIGATION_DRAWER_SELECTED_TOOLBOX);
        Bundle bundle = ActivityOptions.makeCustomAnimation(ToolboxMeetingPagerActivity.this,
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

    /**
     * Button Click on Next/Preview or Save
     */
    public void showProjectTaskForm() {
        switch (this.modeOfForm) {
            case PROJECT_JOB_FORM_B1 :
                showB1FormDialog();
                break;
            case PROJECT_JOB_FORM_B2 :
            case PROJECT_JOB_FORM_B3 :
                fromFragmentNavigate(1);
                break;
            default: break;
        }

    }

    private void showB1FormDialog() {
        MaterialDialog md = new MaterialDialog.Builder(ToolboxMeetingPagerActivity.this)
                .title("Project Job Form")
                .limitIconToDefaultSize()
                .positiveText("SAVE")
                .negativeText("CLOSE")
                .iconRes(R.mipmap.edit_icon)
                .autoDismiss(false)
                .customView(R.layout.m_b1_project_task_form, true)
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
                    }
                })
                .build();
        md.show();
    }

    /*************** END B1 - Pre Installation Site Survey ***************/


    /*************** B2 - In-process inspection (PW) ***************/
    /*************** B3 - In-process inspection (EPS) ***************/

    private void showB2B3FormDialog() {
        MaterialDialog md2 = new MaterialDialog.Builder(ToolboxMeetingPagerActivity.this)
                .title("Confirmation Date")
                .limitIconToDefaultSize()
                .positiveText("SAVE")
                .negativeText("CLOSE")
                .iconRes(R.mipmap.edit_icon)
                .autoDismiss(false)
                .customView(R.layout.m_b2_comformation_date_form, true)
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
                    }
                })
                .build();

        // Set Spinner Comment
        Spinner spinnerComment = new FragmentSetListHelper_ProjectJob().setSpinnerComment(ToolboxMeetingPagerActivity.this, md2.getCustomView());


        // Set up EditText for Date Picker Dialog date
        final View view = md2.getCustomView();
        editTextB2RectificationDate = (EditText) view.findViewById(R.id.editTextB2RectificationDate);
        editTextB2RectificationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rectificationDateClicked = 1;
                showDatePickerDialog();
            }
        });

        md2.show();
    }

    /**
     * TODO: This is implemented both on B2 and B3, should be only per class
     */
    private void showB2B3CorrectiveActionFormDialog() {
        MaterialDialog md2 = new MaterialDialog.Builder(ToolboxMeetingPagerActivity.this)
                .title("Corrective Actions")
                .limitIconToDefaultSize()
                .positiveText("SAVE")
                .negativeText("CLOSE")
                .iconRes(R.mipmap.edit_icon)
                .autoDismiss(false)
                .customView(R.layout.m_b2_corrective_action_form, true)
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
                    }
                })
                .build();

        // Setting up the EditText with Calendar Input on the form
        final View view  = md2.getCustomView();
        editTextB2B3CompletionDate = (EditText) view.findViewById(R.id.editTextB2B3CompletionDate);
        editTextB2B3CompletionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completionDateClicked = 1;
                showDatePickerDialog();
            }
        });
        editTextB2B3TargetCompletionDate = (EditText) view.findViewById(R.id.editTextB2B3TargetCompletionDate);
        editTextB2B3TargetCompletionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completionTargetDateClicked = 1;
                showDatePickerDialog();
            }
        });

        // Showing the Dialog in InterfaceHolder implemented in this Activity
        InterfaceDialogHolder.set(md2);
        showDialog(md2);
        // md2.show();
    }

    /**
     * TODO: SHOULD NOT USE THIS, Implement this when implemented as InterfaceDialogHolder in Activity
     * This called onClick of EditText
     * @param fragment - Current Fragment Calling
     * @param fragmentPosition - Position being paased by the Calling Fragment
     */
    public void showDateDialog(Fragment fragment, int fragmentPosition) {
        this.initCallingFragment(fragment, fragmentPosition);

        InterfaceDialogHolder.get().showDialog(this.getDateDilaog());
    }

    @TargetApi(Build.VERSION_CODES.N)
    private DatePickerDialog getDateDilaog() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog d = new DatePickerDialog(this, this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        // mDialog.show();
        return d;
    }

    public void showDatePickerDialog() {
        // this.getDateDilaog();
        // InterfaceDialogHolder.set(this);
        // showDialog(mDialog);
        // OR
        InterfaceDialogHolder.set(getDateDilaog());
        InterfaceDialogHolder.getDatePickerDialog().show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (completionTargetDateClicked == 1) {
            completionTargetDateClicked = 0;
            editTextB2B3TargetCompletionDate.setText(dayOfMonth + "/" + (month +1) + "/" + year);
        }
        if (completionDateClicked == 1) {
            completionDateClicked = 0;
            editTextB2B3CompletionDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        }
        if (rectificationDateClicked == 1) {
            rectificationDateClicked = 0;
            editTextB2RectificationDate.setText(dayOfMonth + "/" + (month +1) + "/" + year);
        }
    }

    @Override
    public void showDialog(DialogFragment dialog) {
        dialog.show(getSupportFragmentManager(), "tag");
    }

    @Override
    public void showDialog(DatePickerDialog dialog) {
        dialog.show();
    }

    @Override
    public void showDialog(MaterialDialog dialog) {
        dialog.show();
    }

    /**
     * This only to set what Fragment Calls the Calendar Event from the Fragment
     *  then set the EditText/Display only
     * @param fragment
     */
    private void initCallingFragment(Fragment fragment, int fragmentPosition) {
        this.mFragmentPosition = fragmentPosition;
        switch (fragmentPosition) {
//            case PROJECT_JOB_FRAGMENT_POSITION_2 : ncadft = (NonConformanceAndDateFragmentTest)fragment; break;
//            case PROJECT_JOB_FRAGMENT_POSITION_3 : cdft = (CompletionDateFragmentTest) fragment; break;
            default: break;
        }
    }

    public void showUpdateComment() {
        MaterialDialog md = new MaterialDialog.Builder(ToolboxMeetingPagerActivity.this)
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

        md.show();
    }

    /**
     * Called After Clicking Next at NonConformanceAndDateFragmentTest
     */
    public void showOKToSaveRectificationDate() {
        MaterialDialog md = new MaterialDialog.Builder(ToolboxMeetingPagerActivity.this)
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