package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.HashMap;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.login.SessionManager;
import admin4.techelm.com.techelmtechnologies.activity.menu.MainActivity;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b1.DrawingCanvasFragment;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b2.CompletionDateFragmentTest;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b2.NonConformanceAndDateFragmentTest;
import admin4.techelm.com.techelmtechnologies.activity.servicejob_main.PopulateServiceJobViewDetails;
import admin4.techelm.com.techelmtechnologies.adapter.IPITaskListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.PISSTaskListAdapter;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;
import admin4.techelm.com.techelmtechnologies.utility.dialog.InterfaceDialogHolder;
import admin4.techelm.com.techelmtechnologies.utility.dialog.OpenDialog;
import admin4.techelm.com.techelmtechnologies.utility.image_download.UILDownloader;
import admin4.techelm.com.techelmtechnologies.utility.image_download.UILListener;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_CONTINUE_TASK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_START_CORRECTIVE_ACTION_FORM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_START_DRAWING;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_START_TASK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_VIEW_TASK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.LANDING_PAGE_ACTIVE_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.NAVIGATION_DRAWER_SELECTED_PROJECTJOB;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B1;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B2;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B3;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_TYPE_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FRAGMENT_POSITION_2;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FRAGMENT_POSITION_3;

public class ProjectJobViewPagerActivity extends FragmentActivity implements
        PISSTaskListAdapter.CallbackInterface,
        IPITaskListAdapter.CallbackInterface,
        DatePickerDialog.OnDateSetListener,
        OpenDialog,
        UILListener {

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
    private DrawingCanvasFragment dcf;

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
        setContentView(R.layout.activity_projectjob_list);

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

    /*@Override
    public void onResume() {

        super.onResume();

        getCurrentFocus().setFocusableInTouchMode(true);
        getCurrentFocus().requestFocus();
        getCurrentFocus().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    return true;
                }

                return false;
            }
        });
    }*/

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
        onBackPress();
    }

    public void onBackPress() {
        if (mFragmentManager.getBackStackEntryCount() > 0) { // This is usually for DrawingCanvasFragment on backPress
            mFragmentManager.popBackStack();
        } else {
            backToLandingPage(1);
            // super.onBackPressed();
        }
    }

    @Override
    public void onHandleSelection(int position, ServiceJobWrapper serviceJob, int mode) {
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
                // showProjectTaskForm();
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
    }

    /**
     * TODO:  THIS should do the reversion of status when task has been cancelled or barcprecssesesseses
     *  Like on the ServiceJob
     * @param mode
     */
    public void backToLandingPage(final int mode) {
        Intent intent = new Intent(ProjectJobViewPagerActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(LANDING_PAGE_ACTIVE_KEY, NAVIGATION_DRAWER_SELECTED_PROJECTJOB);
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
        MaterialDialog md = new MaterialDialog.Builder(ProjectJobViewPagerActivity.this)
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

    public void showDrawingCanvasFragment() {
        System.out.println("showDrawingCanvasFragment");
        if (true) {
            //if (view.findViewById(R.id.containerView) != null) {
            DrawingCanvasFragment canvas = new DrawingCanvasFragment();
            FragmentTransaction trans = mFragmentManager.beginTransaction(); // OR getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.containerView, canvas).addToBackStack("DRAWING_CANVAS");
            trans.commit();
        }
    }

    public void downloadImageFromURL(Fragment drawingFragment, String url, ImageView mockImageView) {
        Log.e(TAG, "downloadImageFromURL "  + url);
        this.dcf = (DrawingCanvasFragment) drawingFragment;
        UILDownloader downloader = new UILDownloader(ProjectJobViewPagerActivity.this);
        downloader.setImageFrom(url);
        downloader.setImageView(mockImageView);
        downloader.start();
    }

    @Override
    public void OnHandleError(String message) {
        Log.e(TAG, "OnHandleError " + message);
    }

    @Override
    public void OnHandleStartDownload(String message) {
        Log.e(TAG, "OnHandleStartDownload " + message);
    }

    @Override
    public void OnHandleLoadingCompleted(String imageURI, Bitmap imageLoaded) {
        Log.e(TAG, "OnHandleLoadingCompleted "  + imageURI);
        this.dcf.initCanvasView(imageLoaded);
    }

    /*************** END B1 - Pre Installation Site Survey ***************/


    /*************** B2 - In-process inspection (PW) ***************/
    /*************** B3 - In-process inspection (EPS) ***************/
    /*@Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        _birthYear = year;
        _month = month + 1;
        _day = dayOfMonth;

        switch (this.mFragmentPosition) {
            case PROJECT_JOB_FRAGMENT_POSITION_2: ncadft.updateDisplay(_birthYear,_month,_day); break;
            case PROJECT_JOB_FRAGMENT_POSITION_3: cdft.updateDisplay(_birthYear,_month,_day); break;
            default: break;
        }
    }*/

    private void showB2B3FormDialog() {
        MaterialDialog md2 = new MaterialDialog.Builder(ProjectJobViewPagerActivity.this)
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
        ArrayList<String> options = new ArrayList<String>();
        options.add("YES");
        options.add("NO");
        options.add("NA");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProjectJobViewPagerActivity.this,
                android.R.layout.simple_spinner_item, options);
        Spinner spinnerComment = (Spinner) md2.getCustomView().findViewById(R.id.spinnerComment);
        spinnerComment.setAdapter(adapter);

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
        MaterialDialog md2 = new MaterialDialog.Builder(ProjectJobViewPagerActivity.this)
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

    // TODO: Should use this inside a Fragment Class?
    /*public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            editTextB2B3CompletionDate.setText(_month +"/"+_day+"/"+_birthYear);
        }
    }*/

    /*public void updateDateEditText(int _birthYear, int _month, int _day) {
        Log.i("HEY", _month +"/"+_day+"/"+_birthYear);

        editTextB2B3CompletionDate.setText(_month +"/"+_day+"/"+_birthYear);
    }*/

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