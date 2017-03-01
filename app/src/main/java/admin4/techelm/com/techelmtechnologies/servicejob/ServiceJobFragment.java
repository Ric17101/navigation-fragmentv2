package admin4.techelm.com.techelmtechnologies.servicejob;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.marcohc.robotocalendar.RobotoCalendarView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.ServiceJobListAdapter;
import admin4.techelm.com.techelmtechnologies.db.Calendar_ServiceJob_DBUtil;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ServiceJobFragment extends Fragment implements
        RobotoCalendarView.RobotoCalendarListener,
        ServiceJobListAdapter.OnItemClickListener
{
    private static final String TAG = CalendarFragment.class.getSimpleName();
    private static final int REQUEST_CODE = 1234;

    private RobotoCalendarView robotoCalendarView;
    private SlidingUpPanelLayout mLayout;

    private Context context;
    private ServiceJobListAdapter mListAdapter;
    private RecyclerView mSearchResultsList;

    private List<ServiceJobWrapper> results = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = /*(View)*/ inflater.inflate(
                R.layout.servicejob_activity, container, false);
        setContext(container.getContext());

        setupSlidingPanel(view);

        setUpCalendarView(view);

        setUpRecyclerView(view);
        setupResultsList(view);

        if (results == null) {

            populateCardList();
        }
        return view;
    }
/*    @Override
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
     * These Two Lines should be included on every Fragment to maintain the state and do not load again
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        System.out.println("ServiceJobFragment: I'm on the onCreate");
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //outState.putParcelable("results", results);
        System.out.println("ServiceJobFragment: I'm on the onSaveInstanceState");
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
        System.out.println("ServiceJobFragment: I'm on the onActivityCreated");
    }

    private void setContext(Context c) {
        this.context = c;
    }

    public void setUpRecyclerView(View upRecyclerView) {
        mSearchResultsList = (RecyclerView) upRecyclerView.findViewById(R.id.search_results_service_job_list);
    }

    public void setupResultsList(View view) {
        mListAdapter = new ServiceJobListAdapter(view.getContext());
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

    private void setupSlidingPanel(View view) {
        /** Listeners and Instanstiation */
        mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout_calendar);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        /** Set up Sliding Panel height to ANCHORED... */
        mLayout.setAnchorPoint(0.6f);
        // mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {

            case RESULT_OK:

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
        /*new UIThreadHandler(getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });*/
    }

    /**
     * UpdateJobServiceTask task = new UpdateJobServiceTask(view);
     task.execute("");
     */
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
