package admin4.techelm.com.techelmtechnologies.service_report;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.menu.MainActivity;

public class ServiceReport_1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initButton();
    }

    private void initButton() {
        /** BUTTON BACK */
        Button button_back = (Button) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ServiceReport_1.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                // overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        /** BUTTON NEXT */
        Button button_next = (Button) findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ServiceReport_1.this, PartReplacement_2.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                // overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        /** BUTTON UPLOAD CAPTURED IMAGE */
        ImageButton buttonViewUploadImage = (ImageButton) findViewById(R.id.buttonViewUploadImage);
        buttonViewUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Do something with the captured image.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /** BUTTON UPLOAD VOICE */
        ImageButton buttonViewUploadVoice = (ImageButton) findViewById(R.id.buttonViewUploadVoice);
        buttonViewUploadVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Do something with the voice recorded.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /** VIEW DETAILS */
        ImageButton buttonViewDetails = (ImageButton) findViewById(R.id.buttonViewDetails);
        buttonViewDetails.setVisibility(View.GONE);
    }

    private void onClickEdit(View view) {

    }

    private void onClickUploadImage(View view) {

    }

    private void onClickRecordVoice(View view) {

    }

}
