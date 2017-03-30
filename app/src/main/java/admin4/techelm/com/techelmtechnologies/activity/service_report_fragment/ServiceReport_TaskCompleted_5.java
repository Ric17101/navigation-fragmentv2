package admin4.techelm.com.techelmtechnologies.activity.service_report_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.thefinestartist.finestwebview.FinestWebView;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.menu.MainActivity;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;

public class ServiceReport_TaskCompleted_5 extends AppCompatActivity {

    private static final String LOG_TAG = "TaskCompleted_5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_report__task_completed);

        // setBackGroundLayout();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        initButton();
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
        new FinestWebView.Builder(this)
                .titleDefault(getString(R.string.app_name))
                .swipeRefreshColorRes(R.color.colorPrimary1)
                .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
                .show("http://enercon714.firstcomdemolinks.com/sampleREST/simple-codeigniter-rest-api-master/index.php/");
    }


}
