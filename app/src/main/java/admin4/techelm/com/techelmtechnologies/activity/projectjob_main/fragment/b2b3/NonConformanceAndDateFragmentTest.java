package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b2b3;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.ProjectJobViewPagerActivity;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FRAGMENT_POSITION_2;

/**
 * Created by Ratan on 7/29/2015.
 */
public class NonConformanceAndDateFragmentTest extends Fragment  {

    EditText editTextB2RectificationDate;
    private Context _context;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("Main","yooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
        View view = inflater.inflate(R.layout.b2_foms_conform_date,container,false);

        editTextB2RectificationDate = (EditText) view.findViewById(R.id.editTextB2RectificationDate);
        editTextB2RectificationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ProjectJobViewPagerActivity)getActivity()).showDateDialog(NonConformanceAndDateFragmentTest.this, PROJECT_JOB_FRAGMENT_POSITION_2);
            }
        });
        initButton(view);
        return view;
    }
    /**
     * These Two Lines should be included on every Fragment to maintain the state and donnot load again
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("remarks, currently under construction");
    }
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        System.out.println("remarks, currently under construction");
    }

    public void updateDisplay(int _birthYear, int _month, int _day) {
        Log.i("HEY", _month +"/"+_day+"/"+_birthYear);

        editTextB2RectificationDate.setText(_month +"/"+_day+"/"+_birthYear);
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
                ((ProjectJobViewPagerActivity)getActivity()).fromFragmentNavigate(-1);
            }
        });

        /** BUTTON NEXT */
        Button button_next = (Button) view.findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(AddReplacementPart_FRGMT_3.this, SigningOff_FRGMT_4.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, mServiceJobFromBundle));
                overridePendingTransition(R.anim.enter, R.anim.exit);*/
                // ((ProjectJobViewPagerActivity)getActivity()).fromFragmentNavigate(1);
                ((ProjectJobViewPagerActivity)getActivity()).showOKToSaveRectificationDate();
            }
        });
    }
}
