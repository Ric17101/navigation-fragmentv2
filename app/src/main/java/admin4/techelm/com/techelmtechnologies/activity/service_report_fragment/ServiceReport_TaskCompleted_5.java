package admin4.techelm.com.techelmtechnologies.activity.service_report_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.thefinestartist.finestwebview.FinestWebView;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.menu.MainActivity;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;
import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_SERVICE_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_VIEW_DETAIL_URL;

public class ServiceReport_TaskCompleted_5 extends AppCompatActivity {

    private static final String TAG = "TaskCompleted_5";
    private ServiceJobWrapper mServiceJobFromBundle; // From Calling Activity


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_report__task_completed);

        // setBackGroundLayout();

        if (fromBundle() != null) { // if Null don't show anything
            Log.e(TAG, "ServiceReport_TaskCompleted_5 : \n"+ this.mServiceJobFromBundle.toString());
        } else {
            SnackBarNotificationUtil
                    .setSnackBar(findViewById(android.R.id.content),
                            "No data selected from calendar.")
                    .setColor(getResources().getColor(R.color.colorPrimary1))
                    .show();
        }

        initButton();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ServiceReport_TaskCompleted_5.this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    /**
     * PARSING data ServiceJob from Bundle passed by the
     *      MainActivity => CalendarFragment
     *      Used by ServiceTaskCompleted
     * @return - ServiceJobWrapper | NULL if no data has been submitted
     */
    private ServiceJobWrapper fromBundle() {
        Intent intent = getIntent();
        return this.mServiceJobFromBundle = (ServiceJobWrapper) intent.getParcelableExtra(SERVICE_JOB_SERVICE_KEY);
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
        CoordinatorLayout backgroundLayout = (CoordinatorLayout) findViewById(R.id.activity_service_report_task_completed_layout);
        backgroundLayout.setBackground(new ImageUtility(this).ResizeImage(R.drawable.background));
    }

    private void initButton() {

        /** BUTTON TO MAIN PAGE */
        Button buttonBackToTaskMenu = (Button) findViewById(R.id.buttonBackToTaskMenu);
        buttonBackToTaskMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ServiceReport_TaskCompleted_5.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        /** BUTTON VIEW DETAILS ON THE WEB */
        Button buttonShowDetailsOnTheWeb = (Button) findViewById(R.id.buttonShowDetailsOnTheWeb);
        buttonShowDetailsOnTheWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentWebView();
            }
        });
    }

    /**
     * Show the Details on the WEB using WEBVIEW
     */
    private void setContentWebView() {
        new FinestWebView.Builder(ServiceReport_TaskCompleted_5.this)
                .titleDefault("Service Job Details " + this.mServiceJobFromBundle.getServiceNumber())
                .toolbarScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS)
                .webViewBuiltInZoomControls(true)
                .titleDefault(getString(R.string.app_name))
                .swipeRefreshColorRes(R.color.colorPrimary1)
                .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
                .show(SERVICE_JOB_VIEW_DETAIL_URL + this.mServiceJobFromBundle.getID());
    }


}
