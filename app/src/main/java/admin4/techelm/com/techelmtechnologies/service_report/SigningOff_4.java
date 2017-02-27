package admin4.techelm.com.techelmtechnologies.service_report;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import admin4.techelm.com.techelmtechnologies.R;

public class SigningOff_4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing_off_end_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initButton();
    }

    private void initButton() {
        /** BUTTON BACK */
        Button button_back = (Button) findViewById(R.id.button_back);
        button_back.setVisibility(View.GONE);

        /** BUTTON NEXT */
        Button button_next = (Button) findViewById(R.id.button_next);
        button_next.setText("END TASK");
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigningOff_4.this, ServiceReport_TaskCompleted_5.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                // overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

}
