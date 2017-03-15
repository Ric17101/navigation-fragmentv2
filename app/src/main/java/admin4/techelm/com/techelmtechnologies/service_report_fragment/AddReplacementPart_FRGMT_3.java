package admin4.techelm.com.techelmtechnologies.service_report_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.servicejob.PopulateServiceJobViewDetails;

public class AddReplacementPart_FRGMT_3 extends Fragment {

    private static final String TAG = "AddReplacementPart_FRGMT_3";
    private Context mContext;

    private Spinner spinnerReplacementParts;
    private Spinner spinnerQuantity;
    private Spinner spinnerUnitPrice;
    private Spinner spinnerTotalPrice;

    // A. SERVICE ID INFO (Not Displayed)
    private static final String RECORD_JOB_SERVICE_KEY = "SERVICE_JOB";
    private static ServiceJobWrapper mServiceJobFromBundle; // From Calling Activity

    // SlidingPager Tab Set Up
    private static final String ARG_POSITION = "position";
    private int position;

    public static AddReplacementPart_FRGMT_3 newInstance(int position, ServiceJobWrapper serviceJob) {
        AddReplacementPart_FRGMT_3 frag = new AddReplacementPart_FRGMT_3();
        Bundle args = new Bundle();

        args.putInt(ARG_POSITION, position);
        frag.setArguments(args);

        mServiceJobFromBundle = serviceJob;
        return (frag);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_add_replacement_part, container, false);

        this.mContext = container.getContext();

        initSpinner(view);
        initButton(view);

        return view;
    }

    private void initButton(View view) {
        /** BUTTON BACK */
        Button button_back = (Button) view.findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(AddReplacementPart_FRGMT_3.this, PartReplacement_FRGMT_2.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, mServiceJobFromBundle));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);*/
                ((ServiceJobViewPagerActivity)getActivity()).fromFragmentNavigate(-1);
            }
        });

        /** BUTTON NEXT */
        Button button_next = (Button) view.findViewById(R.id.button_next);
        button_next.setText("SAVE");
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(AddReplacementPart_FRGMT_3.this, SigningOff_FRGMT_4.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, mServiceJobFromBundle));
                overridePendingTransition(R.anim.enter, R.anim.exit);*/
                ((ServiceJobViewPagerActivity)getActivity()).fromFragmentNavigate(1);
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
     * @param view
     */
    private void initSpinner(View view) {
        ArrayList<String> options = new ArrayList<String>();
        options.add("Select ");
        options.add("option 1");
        options.add("option 2");
        options.add("option 3");

        // Spinner 1
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, options);
        spinnerReplacementParts = (Spinner) view.findViewById(R.id.spinnerReplacementParts);
        spinnerReplacementParts.setAdapter(adapter);

        // Spinner 2
        options.add("option 4");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, options);
        spinnerQuantity = (Spinner) view.findViewById(R.id.spinnerQuantity);
        spinnerQuantity.setAdapter(adapter2);

        // Spinner 3
        options.add("option 5");
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, options);
        spinnerUnitPrice = (Spinner) view.findViewById(R.id.spinnerUnitPrice);
        spinnerUnitPrice.setAdapter(adapter3);

        // Spinner 4
        options.add("option 6");
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, options);
        spinnerTotalPrice = (Spinner) view.findViewById(R.id.spinnerTotalPrice);
        spinnerTotalPrice.setAdapter(adapter4);
    }

}
