package admin4.techelm.com.techelmtechnologies.activity.service_report;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;

public class AddReplacementPart_3 extends AppCompatActivity {

    private static final String TAG = "AddReplacementPart_FRGMT_3";

    private Spinner spinnerReplacementParts;
    private Spinner spinnerQuantity;
    private Spinner spinnerUnitPrice;
    private Spinner spinnerTotalPrice;

    // A. SERVICE ID INFO
    private static final String RECORD_JOB_SERVICE_KEY = "SERVICE_JOB";
    private ServiceJobWrapper mServiceJobFromBundle; // From Calling Activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_add_replacement_part);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initSpinner();
        initButton();

        fromBundle();
    }

    /**
     * PARSING data ServiceJob from Bundle passed by the
     *      PartReplacement_FRGMT_2 => AddReplacementPart_FRGMT_3
     * @return - ServiceJobWrapper | NULL if no data has been submitted
     */
    private ServiceJobWrapper fromBundle() {
        Intent intent = getIntent();
        return mServiceJobFromBundle = (ServiceJobWrapper) intent.getParcelableExtra(RECORD_JOB_SERVICE_KEY);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    private void initButton() {
        /** BUTTON BACK */
        Button button_back = (Button) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddReplacementPart_3.this, PartReplacement_2.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, mServiceJobFromBundle));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        /** BUTTON NEXT */
        Button button_next = (Button) findViewById(R.id.button_next);
        button_next.setText("SAVE");
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddReplacementPart_3.this, SigningOff_4.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, mServiceJobFromBundle));
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    /**
     * Return all value from the Spinner
     *
     * @return
     */
    private String[] getSpinnerValues() {
        String TextReplacementParts = spinnerReplacementParts.getSelectedItem().toString();
        String TextQuantity = spinnerQuantity.getSelectedItem().toString();
        String TextUnitPrice = spinnerUnitPrice.getSelectedItem().toString();
        String TextTotalPrice = spinnerTotalPrice.getSelectedItem().toString();

        String[] val = {TextReplacementParts, TextQuantity, TextUnitPrice, TextTotalPrice};
        return val;
    }

    /**
     * mSpinner.setSelection(options.indexOf("option 2"));
     */
    private void initSpinner() {
        ArrayList<String> options = new ArrayList<String>();
        options.add("Select ");
        options.add("option 1");
        options.add("option 2");
        options.add("option 3");

        // Spinner 1
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        spinnerReplacementParts = (Spinner) findViewById(R.id.spinnerReplacementParts);
        spinnerReplacementParts.setAdapter(adapter);

        // Spinner 2
        options.add("option 4");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        spinnerQuantity = (Spinner) findViewById(R.id.spinnerQuantity);
        spinnerQuantity.setAdapter(adapter2);

    }

}
