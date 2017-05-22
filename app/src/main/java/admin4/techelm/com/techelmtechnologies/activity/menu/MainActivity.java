package admin4.techelm.com.techelmtechnologies.activity.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.helper.PopulateProjectJobViewDetails;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.ProjectJobChooseFormFragment;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.ProjectJobViewPagerActivity;
import admin4.techelm.com.techelmtechnologies.activity.toolbox_meeting_main.ToolboxMeetingListFragment;
import admin4.techelm.com.techelmtechnologies.activity.toolbox_meeting_main.fragment.ToolboxMeetingPagerActivity;
import admin4.techelm.com.techelmtechnologies.activity.toolbox_meeting_main.helper.PopulateToolboxMeetingViewDetails;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_CalendarListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.TM_ListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_UnsignedListAdapter;
import admin4.techelm.com.techelmtechnologies.activity.login.LoginActivity2;
import admin4.techelm.com.techelmtechnologies.activity.login.SessionManager;
import admin4.techelm.com.techelmtechnologies.activity.service_report.ServiceReport_1;
import admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.ServiceJobViewPagerActivity;
import admin4.techelm.com.techelmtechnologies.activity.servicejob_main.PopulateServiceJobViewDetails;
import admin4.techelm.com.techelmtechnologies.activity.servicejob_main.ServiceJobFragmentTab;
import admin4.techelm.com.techelmtechnologies.adapter.listener.ProjectJobListener;
import admin4.techelm.com.techelmtechnologies.adapter.listener.ServiceJobListener;
import admin4.techelm.com.techelmtechnologies.model.projectjob.ProjectJobWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobNewReplacementPartsRatesWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.model.toolboxmeeting.ToolboxMeetingWrapper;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;
import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;
import admin4.techelm.com.techelmtechnologies.utility.UIThreadHandler;
import admin4.techelm.com.techelmtechnologies.utility.json.ConvertJSON_SJ;
import admin4.techelm.com.techelmtechnologies.utility.json.JSONHelper;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm.ServiceJobBegin_POST;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.*;

public class MainActivity extends FragmentActivity implements
        ServiceJobListener,
        SJ_CalendarListAdapter.CallbackInterface,
        SJ_UnsignedListAdapter.CallbackInterface,
        ProjectJobListener,
        TM_ListAdapter.CallbackInterface
        // OnTaskKill.onStopCallbackInterface
    {

    private static final String TAG = MainActivity.class.getSimpleName();

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    NavigationView mNavigationUserView;
    private int navigation_active = 0;

    ActionBarDrawerToggle mDrawerToggle;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    SessionManager mSession;

    // B. Project Job Setup - B2,B3
    private MaterialDialog mProjectJobFormSelectorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullScreenMode();

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

        this.mProjectJobFormSelectorDialog = initNewPartDialog();
    }

    public void setFullScreenMode() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * This method uses LargeHeap and Hardware Acceleration on the Androidmanifest file in order to
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

    // onPause() or onDestroy()
    @Override
    public void onPause() {
        super.onPause();
        dismissDialog();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

        /**
     * Can be used to title the TAB on the ViewPagerActivity
     * By using the MODE CONSTANT of modeOfForm
     */
    private int fromBundleToActiveNavigation() {
        Intent intent = getIntent();
        return intent.getIntExtra(LANDING_PAGE_ACTIVE_KEY, 0);
    }

    private void dismissDialog() {
        if (this.mProjectJobFormSelectorDialog != null)
            this.mProjectJobFormSelectorDialog.dismiss();
    }
    /*
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    */

    private void logout() {
        if (new JSONHelper().isConnected(this)) {
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
        } else {
            SnackBarNotificationUtil
                    .setSnackBar(findViewById(android.R.id.content),
                            "Can't logout now. " + getResources().getString(R.string.noInternetConnection))
                    .setColor(getResources().getColor(R.color.colorPrimary1))
                    .show();
        }
    }

        private Fragment initActiveFragment() {
            Fragment activeFragment;
            switch (fromBundleToActiveNavigation()) {
                default:
                case NAVIGATION_DRAWER_SELECTED_SERVICEJOB :
                    activeFragment = new ServiceJobFragmentTab();
                    break;
                case NAVIGATION_DRAWER_SELECTED_PROJECTJOB :
                    activeFragment = new ProjectJobChooseFormFragment();
                    break;
                case NAVIGATION_DRAWER_SELECTED_TOOLBOX :
                    activeFragment = new ToolboxMeetingListFragment();
                    break;
            }
            return activeFragment;
        }

    private void init_DrawerNav() {
        /**
         *Setup the DrawerLayout and NavigationView
         */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationUserView = (NavigationView) findViewById(R.id.nav_view_user);

        /**
         * Lets inflate the very first fragmentType
         * Here , we are inflating the TabFragment as the first Fragment
         */
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, initActiveFragment()).commit(); // tO RENDER THE  1st TAB on MAIN MENU

        /**
         * Setup click events on the Navigation View Items.
         */
        mNavigationView.getMenu().getItem(fromBundleToActiveNavigation()).setChecked(true);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers(); // Close Nav Draw after onClick of Tabs
                menuItem.setChecked(true); // Set Active Tab

                switch (menuItem.getItemId()) {
                    case R.id.nav_servicejobs :
                        FragmentTransaction serviceJobFragmentTransaction = mFragmentManager.beginTransaction();
                        serviceJobFragmentTransaction.replace(R.id.containerView, new ServiceJobFragmentTab()).commit();
                        break;
                    case R.id.nav_projectjobs :
                        FragmentTransaction serviceProjectFragmentTransaction = mFragmentManager.beginTransaction();
                        serviceProjectFragmentTransaction.replace(R.id.containerView, new ProjectJobChooseFormFragment()).commit();
                        break;
                    case R.id.nav_checklist :
                        /*FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                        ncadft = new NonConformanceAndDateFragmentTest();
                        fragmentTransaction.replace(R.id.containerView, ncadft).commit();*/

                        /*startActivity(new Intent(MainActivity.this, ServiceJobViewPagerActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        overridePendingTransition(R.anim.enter, R.anim.exit);*/
                        break;
                    case R.id.nav_process_pe :
                        FragmentTransaction fragmentProcessTransaction = mFragmentManager.beginTransaction();

                        /*Bundle arguments = new Bundle();
                        arguments.putBundle(FRAGMENT_TRANSACTION_KEY, getFragmentManager());
                        fragmentProcessTransaction.setArguments(arguments);*/
                        /*cdft = new CompletionDateFragmentTest();
                        fragmentProcessTransaction.replace(R.id.containerView, cdft).commit();*/
                        break;
                    case R.id.nav_process_eps :
                        startActivity(new Intent(MainActivity.this, ServiceReport_1.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        break;
                    case R.id.nav_toolbox :
                        FragmentTransaction sampleFragmentTransaction = mFragmentManager.beginTransaction();
                        sampleFragmentTransaction.replace(R.id.containerView, new ToolboxMeetingListFragment()).commit();
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
     * Handles the Event onClick ProjectJobListener for the CardViewList
     *  From  AdpterList
     *      - SJ_CalendarListAdapter
     *      - SJ_UnsignedListAdapter
     *      - SJ_ListAdapter
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

        switch (action) {
            /*************************** SERVICE JOB *****************************/
            case ACTION_VIEW_DETAILS : // Show Details of SJ on MDialog
                showMDialogSJDetails(serviceJob);
                break;
            case ACTION_ALREADY_ON_PROCESS :
                /*
                SnackBarNotificationUtil
                        .setSnackBar(findViewById(android.R.id.content), "Currently on process.")
                        .setColor(getResources().getColor(R.color.colorPrimary1))
                        .show();
                break;
                // TODO: This should Indicate who is Currently Editing the Service Job is client wants to... also on the status of the SJ
                */
            case ACTION_EDIT: // Show Details on ServiceReport_FRGMT_BEFORE View
                confirmContinueTaskMDialog(serviceJob);
                break;
            case ACTION_BEGIN: // Confirm Begin Task
                confirmBeginTaskMDialog(serviceJob);
                break;
            case ACTION_ALREADY_COMPLETED :
                SnackBarNotificationUtil
                        .setSnackBar(findViewById(android.R.id.content), "Already completed.")
                        .setColor(getResources().getColor(R.color.colorPrimary1))
                        .show();
                break;
            /*************************** END SERVICE JOB *****************************/
        }
    }

    @Override
    public void onHandleSelection(int position, ProjectJobWrapper projectWrapper, int action) {
        switch (action) {
            case ACTION_VIEW_DETAILS : // Show Details of SJ on MDialog
                showMDialogPJDetails(projectWrapper);
                break;
            /*************************** PROJECT JOB *****************************/
            case ACTION_CHOOSE_FORM :
                this.mProjectJobFormSelectorDialog.show();
                initFormSelectorButton(this.mProjectJobFormSelectorDialog.getCustomView(), projectWrapper);
                break;
            /*************************** END PROJECT JOB *****************************/

            case ACTION_TOOLBOX_MEETING :
                startToolBoxMeetingViewPager();
                break;
        }
    }
    @Override
    public void onHandleSelection(int position, ToolboxMeetingWrapper projectWrapper, int action) {
            switch (action) {
                case ACTION_VIEW_DETAILS : // Show Details of SJ on MDialog
                    showMDialogTMDetails(projectWrapper);
                    break;
                /*************************** PROJECT JOB *****************************/
                /*case ACTION_CHOOSE_FORM :
                    this.mProjectJobFormSelectorDialog.show();
                    //initFormSelectorButton(this.mProjectJobFormSelectorDialog.getCustomView(), projectWrapper);
                    break;*/
                /*************************** END PROJECT JOB *****************************/

                case ACTION_TOOLBOX_MEETING :
                    Log.wtf("Select:","xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                    startToolBoxMeetingViewPager();
                    break;
            }
        }

    /*************************** A. SERVICE JOB *****************************/

    /**
     * To Begin Task Filling up the form, newly open Service Job, Begin Task
     * Save Start time to DB
     * @param serviceJob - ServiceJob Wrapper
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
                        serviceJobUpdateStatus(serviceJob, ACTION_BEGIN);
                    }
                }).build();

        new PopulateServiceJobViewDetails()
                .populateServiceJobDetailsMaterialDialog(md.getCustomView(), serviceJob, View.GONE, TAG);
        md.show();
    }

    /**
     * To Continue  Filling up the form, or to sign the Service Job
     * Save Start end to DB, then Add Time_count on servicejob_time TABLE
     * @param serviceJob - ServiceJob Wrapper
     */
    private void confirmContinueTaskMDialog(final ServiceJobWrapper serviceJob) {
        MaterialDialog md = new MaterialDialog.Builder(this)
                .title("CONTINUE TASK " + serviceJob.getServiceNumber() + "?")
                .customView(R.layout.i_labels_report_details_modal, true)
                .limitIconToDefaultSize()
                .negativeText("CLOSE")
                .positiveText("CONTINUE")
                .iconRes(R.mipmap.view_icon)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        switch (serviceJob.getStatus()) {
                            case SERVICE_JOB_PENDING :
                            case SERVICE_JOB_UNSIGNED : serviceJobUpdateStatus(serviceJob, ACTION_EDIT);
                                break;
                            case SERVICE_JOB_ON_PROCESS : serviceJobUpdateStatus(serviceJob, ACTION_ALREADY_ON_PROCESS);
                                break;
                        }
                    }
                }).build();

        new PopulateServiceJobViewDetails()
                .populateServiceJobDetailsMaterialDialog(md.getCustomView(), serviceJob, View.GONE, TAG);
        md.show();
    }

    /**
     * This redirect to the SeriveJobViewPagerActivity considering the mode
     * This Update the ServiceJob Status to OnProcess while being updated by the current user
     * @param serviceJob - data to process
     * @param mode - mode being done
     */
    private void serviceJobUpdateStatus(final ServiceJobWrapper serviceJob, final int mode) {
        final ServiceJobBegin_POST beginServiceJob = new ServiceJobBegin_POST();
        beginServiceJob.setOnEventListener(new ServiceJobBegin_POST.OnEventListener() {
            @Override
            public void onEvent() {
                // TODO: Close progress dialog here
                // TODO: Test Response if OK or not
            }

            @Override
            public void onError(String message) {
                // TODO: Close progress dialog then try again if connected to internet
            }

            @Override
            public void onEventResult(WebResponse response) {
                servicesJobStarTask(serviceJob, serviceJob.getStatus());
            }
        });

        switch (mode) {
            case ACTION_BEGIN: beginServiceJob.postStartDate(serviceJob.getID()); break;
            case ACTION_EDIT: beginServiceJob.postContinueDate(serviceJob.getID()); break;
            /*
                WE DON'T Save DATE_TIME here (ACTION_ALREADY_ON_PROCESS), cases are
                   - User re-open the app,
                   - or closed the app intentionally
            */
            case ACTION_ALREADY_ON_PROCESS : servicesJobStarTask(serviceJob, serviceJob.getStatus()); break;
        }
    }

    /**
     * This is Called After serviceJobUpdateStatus()
     *  postGetListOfReplacementPartsDate
     *  Then will populate the List of New Replacement Parts Rate For the Form,
     *  if error, then will not proceed to Form/Update/Begin Task
     * @param serviceJob
     * @param status
     */
    private void servicesJobStarTask(final ServiceJobWrapper serviceJob, final String status) {
        final ServiceJobBegin_POST beginServiceJob = new ServiceJobBegin_POST();
        beginServiceJob.setOnEventListener(new ServiceJobBegin_POST.OnEventListener() {
            @Override
            public void onEvent() {
                // TODO: Close progress dialog here
                // TODO: Test Response if OK or not
            }

            @Override
            public void onError(String message) {
                // TODO: Close progress dialog then try again if connected to internet
            }

            @Override
            public void onEventResult(WebResponse response) {
                proceedViewPagerActivity(serviceJob, status, response.getStringResponse());
            }
        });

        // To Start the Activity
        beginServiceJob.postGetListOfReplacementPartsDate();
    }

    /**
     * Proceed to EDIT, BEGIN, SIGN or UNSIGNED the ServiceJob
     * @param serviceJob - Data to process
     * @param mode - mode of process or action
     * @param JSONListPartsRate
     */
    private void proceedViewPagerActivity(ServiceJobWrapper serviceJob, String mode, String JSONListPartsRate) {
        try {
            ArrayList<ServiceJobNewReplacementPartsRatesWrapper> rateList = new ConvertJSON_SJ().getResponseJSONPartReplacementRate(JSONListPartsRate);
            Log.e(TAG, rateList.toString());

            startActivity(new Intent(MainActivity.this, ServiceJobViewPagerActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra(SERVICE_JOB_SERVICE_KEY, serviceJob)
                    .putExtra(SERVICE_JOB_PREVIOUS_STATUS_KEY, mode)
                    .putExtra(SERVICE_JOB_PARTS_REPLACEMENT_LIST, rateList));
            overridePendingTransition(R.anim.enter, R.anim.exit);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Will Prpomt User that Prices for the form isnot ok
        SnackBarNotificationUtil
                .setSnackBar(findViewById(android.R.id.content), "Error on Parsing Parts Replacement Rates")
                .setColor(getResources().getColor(R.color.colorPrimary1))
                .show();
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

    /*************************** A. END SERVICE JOB *****************************/
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
    /*************************** B. PROJECT JOB *****************************/
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

        /*************************** B. PROJECT JOB *****************************/
        private void showMDialogTMDetails(ToolboxMeetingWrapper projectJob) {
            MaterialDialog md = new MaterialDialog.Builder(this)
                    .title("COMPLETED PROJECT JOB " + projectJob.getProjectRef())
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

            new PopulateToolboxMeetingViewDetails()
                    .populateServiceJobDetails(md.getCustomView(), projectJob, View.GONE, TAG);
            md.show();
        }
    private MaterialDialog initNewPartDialog() {
        boolean wrapInScrollView = false;
        MaterialDialog md = new MaterialDialog.Builder(MainActivity.this)
                .title("SELECT TYPE OF FORM")
                .customView(R.layout.i_choose_type_of_form, wrapInScrollView)
                .positiveText("Close")
                // .iconRes(R.mipmap.replacepart_icon) // android:background="@mipmap/replacepart_icon"
                .autoDismiss(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).build();
        return md;
    }

    // Set Up Listener to CardView for Selecting Type of forms
    // The components are initialized late in order to change the ID
    private void initFormSelectorButton(View view, final ProjectJobWrapper projectJob) {
        CardView cvPISS = (CardView) view.findViewById(R.id.cvPISS);
        CardView cvPW = (CardView) view.findViewById(R.id.cvPW);
        CardView cvEPS = (CardView) view.findViewById(R.id.cvEPS);

        cvPISS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProjectJobsList(PROJECT_JOB_FORM_B1, projectJob);
            }
        });

        cvPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProjectJobsList(PROJECT_JOB_FORM_B2, projectJob);
            }
        });

        cvEPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProjectJobsList(PROJECT_JOB_FORM_B3, projectJob);
            }
        });
    }

    /**
     * @param typeOfForm - This will Redirect to ServiceJobViewPagerActivity with extra intent of Type of Form
     */
    private void startProjectJobsList(int typeOfForm, ProjectJobWrapper projectJob) {
        Intent i = new Intent(MainActivity.this, ProjectJobViewPagerActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .putExtra(PROJECT_JOB_FORM_TYPE_KEY, typeOfForm)
                .putExtra(PROJECT_JOB_KEY, projectJob);
        startActivity(i);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    /*************************** B. END PROJECT JOB *****************************/

    private void startToolBoxMeetingViewPager() {
        Intent i = new Intent(MainActivity.this, ToolboxMeetingPagerActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                /*.putExtra(PROJECT_JOB_FORM_TYPE_KEY, typeOfForm)*/;
        startActivity(i);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }
}