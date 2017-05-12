package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.astuetz.PagerSlidingTabStrip;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.login.SessionManager;
import admin4.techelm.com.techelmtechnologies.activity.menu.MainActivity;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b2.IPITaskListFinalFragment;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b2.ProjectJobLastFormFragment;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.helper.FragmentSetListHelper_ProjectJob;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.helper.PopulateProjectJobTaskViewDetails;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.helper.PopulateProjectJobViewDetails;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b1.DrawingCanvasFragment;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b1.DrawingFormFragment;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b2.CompletionDateFragmentTest;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b2.NonConformanceAndDateFragmentTest;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.helper.PopulateProjectJob_IPIFinalTaskViewDetails;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.helper.PopulateProjectJob_IPITaskViewDetails;
import admin4.techelm.com.techelmtechnologies.adapter.listener.IPITaskListener;
import admin4.techelm.com.techelmtechnologies.adapter.listener.IPIFinalTaskListener;
import admin4.techelm.com.techelmtechnologies.adapter.listener.PISSTaskListener;
import admin4.techelm.com.techelmtechnologies.adapter.listener.ProjectJobListener;
import admin4.techelm.com.techelmtechnologies.db.projectjob.PISS_TaskDBUtil;
import admin4.techelm.com.techelmtechnologies.model.projectjob.ProjectJobWrapper;
import admin4.techelm.com.techelmtechnologies.model.projectjob.b1.PISSTaskWrapper;
import admin4.techelm.com.techelmtechnologies.model.projectjob.b2.IPI_TaskFinalWrapper;
import admin4.techelm.com.techelmtechnologies.model.projectjob.b2.IPI_TaskWrapper;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;
import admin4.techelm.com.techelmtechnologies.utility.PermissionUtil;
import admin4.techelm.com.techelmtechnologies.utility.ProgressbarUtil;
import admin4.techelm.com.techelmtechnologies.utility.dialog.InterfaceDialogHolder;
import admin4.techelm.com.techelmtechnologies.utility.dialog.OpenDialog;
import admin4.techelm.com.techelmtechnologies.utility.http_auth.HttpBasicAuth;
import admin4.techelm.com.techelmtechnologies.utility.image_download.UILDownloader;
import admin4.techelm.com.techelmtechnologies.utility.image_download.UILListener;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_CONTINUE_TASK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_START_CORRECTIVE_ACTION_FORM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_START_DRAWING;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_START_IPI_CORRECTIVE_ACTION_TASK_FORM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_START_IPI_TASK_FORM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_START_TASK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_TASK_START_DRAWING;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_VIEW_TASK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.FRAGMENT_BACK_STACK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.LANDING_PAGE_ACTIVE_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.NAVIGATION_DRAWER_SELECTED_PROJECTJOB;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B1;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B2;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B3;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_TYPE_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FRAGMENT_POSITION_2;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FRAGMENT_POSITION_3;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_KEY;

public class ProjectJobViewPagerActivity extends FragmentActivity implements
        ProjectJobListener,
        PISSTaskListener,
        PISS_TaskDBUtil.OnDatabaseChangedListener,
        IPITaskListener,
        IPIFinalTaskListener,
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

    // instance Variable
    private int modeOfForm = 0;  // PAGER TAB Type of Form
    private ProjectJobWrapper mProjectJob;

    // B. Project Job Setup - B2,B3
    private NonConformanceAndDateFragmentTest ncadft;
    private CompletionDateFragmentTest cdft;
    private DrawingCanvasFragment dcf;
    private DrawingFormFragment dff;

    DatePickerDialog mDialog;
    private EditText editTextB2B3CompletionDate; // This is used for multiple pop up views
    private int completionDateClicked = 0;
    private EditText editTextB2B3TargetCompletionDate; // This is used for multiple pop up views
    private int completionTargetDateClicked = 0;
    private EditText editTextB2RectificationDate;
    private int rectificationDateClicked = 0;

    // Loading Indicator Setup
    private View mProgressView;
    private View mMainView;
    private ProgressbarUtil mProgressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullScreenMode();

        setContentView(R.layout.activity_projectjob_list);

        initProgresBarIndicator();

        fromBundle();

        setBackGroundLayout();

        loginSessionTest();

        init_ViewPager();

        initPermissions();

        mProgressIndicator.showProgress(false);

        HttpBasicAuth.testAuthetication();
    }

    private void initProgresBarIndicator() {
        mMainView = findViewById(R.id.containerView);
        mProgressView = findViewById(R.id.page_progress);
        this.mProgressIndicator = new ProgressbarUtil().newInstance(mProgressView, mMainView, getResources());
    }

    private void initPermissions() {
        PermissionUtil.verrifyReadStoragePermissions(this);
        PermissionUtil.verrifyWriteStoragePermissions(this);
        PermissionUtil.verrifyCameraPermissions(this);
    }

    // TODO:  separte this as util class
    public void setFullScreenMode() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * Can be used to title the TAB on the ViewPagerActivity
     * By using the MODE CONSTANT of modeOfForm
     */
    private void fromBundle() {
        mProgressIndicator.showProgress(true);

        Intent intent = getIntent();
        this.modeOfForm = intent.getIntExtra(PROJECT_JOB_FORM_TYPE_KEY, 0);
        this.mProjectJob = intent.getParcelableExtra(PROJECT_JOB_KEY);

        setViewPagerTitleBar();
    }

    private void init_ViewPager() {
        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mPagerAdapter = ProjectJobFragmentTab.newInstance(this.modeOfForm, this.mProjectJob);
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
     *  set the Background image of the App/Activities
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
    public void onHandleSelection(int position, ProjectJobWrapper serviceJob, int mode) {
        switch (mode) {
            case ACTION_START_DRAWING : // TODO: Change this to reasonable Variable Name, OK?
                if (this.modeOfForm == PROJECT_JOB_FORM_B1)
                    ;
                    //showDrawingFormFragment(task);//fromFragmentNavigate(1);
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
                showMDialogPJDetails(serviceJob);
                Log.e(TAG, "This is ACTION_VIEW_TASK");
                break;
            case ACTION_START_CORRECTIVE_ACTION_FORM: // TODO: REMOVE??
                showB2B3CorrectiveActionFormDialog();
                break;
            default :
                Log.e(TAG, "This is ACTION_VIEW_TASK");
                break;
        }
    }

    @Override
    public void onHandleSelection(int position, PISSTaskWrapper task, int mode) {
        switch (mode) {
            case ACTION_VIEW_TASK :
                Log.e(TAG, task.toString());
                showMDialogPJTaskDetalis(task);
                break;
            case ACTION_TASK_START_DRAWING :
                showDrawingFormFragment(task);
                break;
            default : break;
        }
    }

    @Override
    public void onHandleSelection(int position, IPI_TaskWrapper ipiTaskWrapper, int mode) {
        switch (mode) {
            case ACTION_VIEW_TASK :
                showMDialogPJ_IPITaskDetails(ipiTaskWrapper);
                break;
            case ACTION_START_CORRECTIVE_ACTION_FORM : // TODO: change this to proper action
                Log.e(TAG, "This is ACTION_START_CORRECTIVE_ACTION_FORM");
                showB2B3CorrectiveActionFormDialog();
                break;
            case ACTION_START_IPI_TASK_FORM :
                Log.e(TAG, "This is ACTION_START_IPI_TASK_FORM");
                showB2B3FormDialog();
                break;
        }
    }

    @Override
    public void onHandleSelection(int position, IPI_TaskFinalWrapper ipiTaskFinalWrapper, int mode) {
        switch (mode) {
            case ACTION_VIEW_TASK :
                showMDialogPJ_IPIFinalTaskDetails(ipiTaskFinalWrapper);
                break;
            case ACTION_START_IPI_CORRECTIVE_ACTION_TASK_FORM :
                showB2B3CorrectiveActionFormDialog();
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

    /*************** B1 - Pre Installation Site Survey ***************/
    private void showMDialogPJDetails(ProjectJobWrapper projectJob) {
        MaterialDialog md = new MaterialDialog.Builder(this)
                .title("PROJECT JOB " + projectJob.getProjectRef())
                .customView(R.layout.i_labels_project_job_details, true)
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

        new PopulateProjectJobViewDetails()
                .populateServiceJobDetails(md.getCustomView(), projectJob, View.GONE, TAG);
        md.show();
    }

    private void showMDialogPJTaskDetalis(PISSTaskWrapper task) {
        MaterialDialog md = new MaterialDialog.Builder(this)
                .title(task.getSerialNo() + ".) PRE-INSTALLATION TASK" )
                .customView(R.layout.i_labels_project_job_tasks_details, true)
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

        new PopulateProjectJobTaskViewDetails().populateServiceJobDetails(md.getCustomView(), task, TAG);
        md.show();
    }

    private void showMDialogPJ_IPITaskDetails(IPI_TaskWrapper ipiTaskWrapper) {
        MaterialDialog md = new MaterialDialog.Builder(this)
                .title(ipiTaskWrapper.getSerialNo() + ".) IN-PROCESS INSTALLATION DETAILS")
                .customView(R.layout.m_content_project_job_ipi_tasks_details, true)
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

        new PopulateProjectJob_IPITaskViewDetails()
                .populateServiceJobDetails(md.getCustomView(), ipiTaskWrapper, TAG);
        md.show();
    }

    private void showMDialogPJ_IPIFinalTaskDetails(IPI_TaskFinalWrapper ipiTaskFinalWrapper) {
        MaterialDialog md = new MaterialDialog.Builder(this)
                .title(ipiTaskFinalWrapper.getSerialNo() + ".) IN-PROCESS FINAL DETAILS")
                .customView(R.layout.m_content_project_job_ipi_final_tasks_details, true)
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
        new PopulateProjectJob_IPIFinalTaskViewDetails()
                .populateServiceJobDetails(md.getCustomView(), ipiTaskFinalWrapper, TAG);
        md.show();
    }

    /**
     * Button Click on Next/Preview or Save
     */
    public void onClickNextButton() {
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

    public void showDrawingCanvasFragment(PISSTaskWrapper taskWrapper) {
        System.out.println("showDrawingCanvasFragment");

        if (true) {
            //if (view.findViewById(R.id.containerView) != null) {
            DrawingCanvasFragment canvas = DrawingCanvasFragment.newInstamce(taskWrapper);
            FragmentTransaction trans = mFragmentManager.beginTransaction(); // OR getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.containerView, canvas).addToBackStack(FRAGMENT_BACK_STACK);
            trans.commit();
        }
    }

    public void showDrawingFormFragment(PISSTaskWrapper task) {
        System.out.println("showDrawingFormFragment");

        DrawingFormFragment form = DrawingFormFragment.newInstance(task);
        FragmentTransaction trans = mFragmentManager.beginTransaction();
        trans.replace(R.id.containerView, form).addToBackStack(FRAGMENT_BACK_STACK);
        trans.commit();
    }

    // TODO: Should implement also on th DrawingFormFragment.jaca and DrawingCanvasFragment
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
        if (this.dcf != null) {
            this.dcf.initCanvasView(imageLoaded);
            this.dcf = null;
        }
    }

    /*************** END B1 - Pre Installation Site Survey ***************/


    /*************** B2 - In-process inspection (PW) ***************/
    /*************** B3 - In-process inspection (EPS) ***************/
    public void showProjectJobLastFormFragment(/*IPI_TaskFinalWrapper task*/) {
        System.out.println("showDrawingFormFragment");
        ProjectJobLastFormFragment form = ProjectJobLastFormFragment.newInstance(this.mProjectJob);
        FragmentTransaction trans = mFragmentManager.beginTransaction();
        trans.replace(R.id.containerView, form).addToBackStack(FRAGMENT_BACK_STACK);
        trans.commit();
    }

    /*
        IPI PW/EPS Task List Form
     */
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
        Spinner spinner = new FragmentSetListHelper_ProjectJob().setSpinnerComment(ProjectJobViewPagerActivity.this, md2.getCustomView());

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
     * Final Tab or IPI PW/EPS 2nd TAB
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
        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
        if (completionTargetDateClicked == 1) {
            completionTargetDateClicked = 0;
            editTextB2B3TargetCompletionDate.setText(date);
        }
        if (completionDateClicked == 1) {
            completionDateClicked = 0;
            editTextB2B3CompletionDate.setText(date);
        }
        if (rectificationDateClicked == 1) {
            rectificationDateClicked = 0;
            editTextB2RectificationDate.setText(date);
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
     * TODO: fragment Position is needed?
     */
    private void initCallingFragment(Fragment fragment, int fragmentPosition) {
        // this.mFragmentPosition = fragmentPosition;
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
                .populateProjectJobDetailsMaterialDialog(md.getCustomView(), serviceJob, View.GONE, TAG);*/
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

    @Override
    public void onNewPISS_TaskEntryAdded(String fileName) {

    }

    @Override
    public void onPISS_TaskEntryRenamed(String fileName) {

    }

    @Override
    public void onPISS_TaskEntryDeleted() {

    }

    /*************** END B2 - In-process inspection (PW) ***************/
    /*************** END B3 - In-process inspection (EPS) ***************/
}