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

public class PartReplacement_2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_replacement);
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
                startActivity(new Intent(PartReplacement_2.this, ServiceReport_1.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                // overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        /** BUTTON NEXT */
        Button button_next = (Button) findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PartReplacement_2.this, AddReplacementPart_3.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                // overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        /** BUTTON VIEW DETAILS */
        ImageButton buttonViewDetails = (ImageButton) findViewById(R.id.buttonViewDetails);
        buttonViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PartReplacement_2.this, SigningOff_4.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                // overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        /** BUTTON VIEW DETAILS */
        ImageButton buttonViewUploadFile = (ImageButton) findViewById(R.id.buttonViewUploadFile);
        buttonViewUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Do something with the file uploaded", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
