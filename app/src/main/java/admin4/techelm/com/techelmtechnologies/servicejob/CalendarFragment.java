package admin4.techelm.com.techelmtechnologies.servicejob;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.marcohc.robotocalendar.RobotoCalendarView;
import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.CalendarListAdapter;
import admin4.techelm.com.techelmtechnologies.db.Calendar_ServiceJob_DBUtil;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CalendarFragment extends Fragment implements
        RobotoCalendarView.RobotoCalendarListener,
        CalendarListAdapter.OnItemClickListener
        //ServiceJobListAdapter.CallbackInterface
{

    private RobotoCalendarView robotoCalendarView;
    private Context context;

    private CalendarListAdapter mListAdapter;
    private RecyclerView mSearchResultsList;
    private static final int REQUEST_CODE = 1234;

    private List<ServiceJobWrapper> results = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = /*(View)*/ inflater.inflate(
                R.layout.calendar_activity, container, false);
        setContext(container.getContext());

        setUpCalendarView(view);
        setUpRecyclerView(view);

        setupResultsList(view);

        /*setUpRecyclerView(view);

        setupResultsList(view);*/
        if (results == null) {
//            UpdateJobServiceTask task = new UpdateJobServiceTask(view);
//            task.execute("");
            populateCardList();
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        System.out.println("CalendarFragment: I'm on the onCreate");
    }
    /*@Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }*/

    /**
     * These Two Lines should be included on every Fragment to maintain the state and donnot load again
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        setRetainInstance(true);
        System.out.println("CalendarFragment: I'm on the onSaveInstanceState");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (results == null) { // If Data is Null then fetch the Data List again
//            UpdateJobServiceTask task = new UpdateJobServiceTask(this.getView());
//            task.execute("");
            populateCardList();
        } else { // Restore the Data List again
            mListAdapter.swapData(results);
        }
        System.out.println("CalendarFragment: I'm on the onActivityCreated");
    }

    private void setContext(Context c) {
        this.context = c;
    }

    public void setUpRecyclerView(View upRecyclerView) {
        mSearchResultsList = (RecyclerView) upRecyclerView.findViewById(R.id.calendar_service_job_list);
    }

    public void setupResultsList(View view) {
        mListAdapter = new CalendarListAdapter(view.getContext());
        mSearchResultsList.setAdapter(mListAdapter);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    /**
     * Set up the Calendar View Listener on Click of the Date/Month
     *
     * @param view
     */
    private void setUpCalendarView(View view) {
        // Gets the calendar from the view
        robotoCalendarView = (RobotoCalendarView) view.findViewById(R.id.robotoCalendarPicker);
        Button markDayButton = (Button) view.findViewById(R.id.markDayButton);
        markDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                Random random = new Random(System.currentTimeMillis());
                int style = random.nextInt(2);
                int daySelected = random.nextInt(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.DAY_OF_MONTH, daySelected);

                switch (style) {
                    case 0:
                        robotoCalendarView.markCircleImage1(calendar);
                        break;
                    case 1:
                        robotoCalendarView.markCircleImage2(calendar);
                        break;
                    default:
                        break;
                }
            }
        });

        // Set listener, in this case, the same activity
        robotoCalendarView.setRobotoCalendarListener(this);

        robotoCalendarView.setShortWeekDays(false);

        robotoCalendarView.showDateTitle(true);

        robotoCalendarView.updateView();
    }

    @Override
    public void onDayClick(Calendar daySelectedCalendar) {
        System.out.println("onDayClick: " + daySelectedCalendar.getTime());
    }

    @Override
    public void onDayLongClick(Calendar daySelectedCalendar) {
        System.out.println("onDayLongClick: " + daySelectedCalendar.getTime());
    }

    @Override
    public void onRightButtonClick() {
        System.out.println("onRightButtonClick!");
    }

    @Override
    public void onLeftButtonClick() {
        System.out.println("onLeftButtonClick!");
        // Toast.makeText(this.context, "onLeftButtonClick!", Toast.LENGTH_SHORT).show();
    }

    private void activityResultIntent() {
        Intent check = new Intent();
        startActivityForResult(check, REQUEST_CODE);
    }

    /**
     * Setting up for the Result OnClick inside the CardView on the List Adapter
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {

            case RESULT_OK:

                /*if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    System.out.println("I'm in onActivityResult");
                    _speaker = new Speaker(getApplicationContext());
                    ImageButton buttonSpeakKoreanAlphabet = (ImageButton) findViewById(R.id.buttonSpeakKorean_alphabet);
                    buttonSpeakKoreanAlphabet.setClickable(true);
                } else {
                    Intent install = new Intent();
                    install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(install);
                }*/

                break;

            case RESULT_CANCELED:
                // ... Handle this situation
                break;
            default:
                break;
        }
    }

    private void populateCardList() {
        results = new Calendar_ServiceJob_DBUtil(context).getAllDetailsOfServiceJob();
        mSearchResultsList.setHasFixedSize(true);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(context));
        mSearchResultsList.setItemAnimator(new DefaultItemAnimator());
        mListAdapter.swapData(results);

    }

    /*private class UpdateJobServiceTask extends AsyncTask<String, Void, String> {

        private View mView;

        public UpdateJobServiceTask(View view) {
            mView = view;
        }

        @Override
        protected String doInBackground(String... params) {
            populateCardList();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //textView.setText(result);
        }
    }*/

    @Override
    public void onClick(ServiceJobWrapper colorWrapper) {

    }
}
