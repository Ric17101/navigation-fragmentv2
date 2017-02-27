package admin4.techelm.com.techelmtechnologies.service_report;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;

import admin4.techelm.com.techelmtechnologies.R;

public class AddReplacementPart_3 extends AppCompatActivity {

    private Spinner spinnerReplacementParts;
    private Spinner spinnerQuantity;
    private Spinner spinnerUnitPrice;
    private Spinner spinnerTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_replacement_part);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initSpinner();
        initButton();
    }

    private void initButton() {
        /** BUTTON BACK */
        Button button_back = (Button) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddReplacementPart_3.this, PartReplacement_2.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                // overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        /** BUTTON NEXT */
        Button button_next = (Button) findViewById(R.id.button_next);
        button_next.setText("SAVE");
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddReplacementPart_3.this, SigningOff_4.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                // overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        /** VIEW DETAILS */
        ImageButton buttonViewDetails = (ImageButton) findViewById(R.id.buttonViewDetails);
        buttonViewDetails.setVisibility(View.GONE);
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

        // Spinner 3
        options.add("option 5");
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        spinnerUnitPrice = (Spinner) findViewById(R.id.spinnerUnitPrice);
        spinnerUnitPrice.setAdapter(adapter3);

        // Spinner 4
        options.add("option 6");
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        spinnerTotalPrice = (Spinner) findViewById(R.id.spinnerTotalPrice);
        spinnerTotalPrice.setAdapter(adapter4);
    }

}
