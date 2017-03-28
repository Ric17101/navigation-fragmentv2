package admin4.techelm.com.techelmtechnologies.service_report_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.thefinestartist.finestwebview.FinestWebView;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.menu.MainActivity;

public class ServiceReport_TaskCompleted_5 extends AppCompatActivity {

    private static final String LOG_TAG = "TaskCompleted_5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_report__task_completed);
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
        setContentWebView();
        initButton();
    }

    private void setContentWebView() {
        new FinestWebView.Builder(this).titleDefault("The Finest Artist")
                .swipeRefreshColorRes(R.color.colorPrimary1)
                .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
                .show("http://enercon714.firstcomdemolinks.com/sampleREST/simple-codeigniter-rest-api-master/index.php/");
    }

    private void initButton() {

        /** BUTTON VIEW DETAILS */
        Button buttonBackToTaskMenu = (Button) findViewById(R.id.buttonBackToTaskMenu);
        buttonBackToTaskMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ServiceReport_TaskCompleted_5.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });
    }



}
