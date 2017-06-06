package admin4.techelm.com.techelmtechnologies.activity.servicejob_main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marcohc.robotocalendar.RobotoCalendarView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONException;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.login.SessionManager;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_ListAdapter;
import admin4.techelm.com.techelmtechnologies.db.servicejob.CalendarSJDBUtil;
import admin4.techelm.com.techelmtechnologies.task.TaskCanceller;
import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;
import admin4.techelm.com.techelmtechnologies.utility.json.ConvertJSON_SJ;
import admin4.techelm.com.techelmtechnologies.utility.json.JSONHelper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_LIST_URL;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ServiceJobFragment extends Fragment implements
        SJ_ListAdapter.OnItemClickListener
{
    private static final String TAG = ServiceJobFragment.class.getSimpleName();
    private static final int REQUEST_CODE = 1234;

    private TextView name;
    private RobotoCalendarView robotoCalendarView;
    private TextView textViewSJResult;
    private SlidingUpPanelLayout mLayout;

    private Context mContext;
    private SJ_ListAdapter mListAdapter;
    private RecyclerView mSearchResultsList;
    private SwipeRefreshLayout swipeRefreshServiceJobLayout;

    private List<ServiceJobWrapper> results = null;
    private CalendarSJTask_RenderList mAuthTask = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.servicejob_activity, container, false);
        setContext(container.getContext());

        setUpCalendarView(view);

        setUpRecyclerView(view);

        setupResultsList(view);

        setupSwipeRefreshServiceJobLayout(view);

        if (results == null) {
            // populateCardList();
        }
        renderListFromCalendar(Calendar.getInstance());
        return view;
    }

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
            // populateCardList();
        } else { // Restore the Data List again
            mListAdapter.swapData(results);
        }
        System.out.println("ServiceJobFragment: I'm on the onActivityCreated");
    }

    private void setContext(Context c) {
        this.mContext = c;
    }

    /***** SWIPE REFRESH LAYOUT *****/
    private void setupSwipeRefreshServiceJobLayout(View view) {
        swipeRefreshServiceJobLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshServiceJobLayout);
        swipeRefreshServiceJobLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                renderListFromCalendar(Calendar.getInstance());
            }
        });
        swipeRefreshServiceJobLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
    }
    private void hideSwipeRefreshing() {
        if (swipeRefreshServiceJobLayout != null)
            swipeRefreshServiceJobLayout.setRefreshing(false);
    }

    public void setUpRecyclerView(View upRecyclerView) {
        mSearchResultsList = (RecyclerView) upRecyclerView.findViewById(R.id.search_results_service_job_list);
        textViewSJResult = (TextView) upRecyclerView.findViewById(R.id.textViewSJResult);
        textViewSJResult.setVisibility(View.GONE);
    }
    public void setupResultsList(View view) {
        mListAdapter = new SJ_ListAdapter(view.getContext());
        mSearchResultsList.setAdapter(mListAdapter);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    /**
     * CALENDAR VIEW
     * Set up the Calendar View Listener on Click of the Date/Month
     *
     * @param view
     */
    private void setUpCalendarView(View view) {
        // Gets the calendar from the view
        robotoCalendarView = (RobotoCalendarView) view.findViewById(R.id.robotoCalendarServiceJobPicker);
        robotoCalendarView.setVisibility(View.GONE);
    }

    private void renderListFromCalendar(Calendar daySelectedCalendar) {
        String formattedDate = new CalendarFragment().convertLongDateToSimpleDate(daySelectedCalendar);
        mAuthTask = new CalendarSJTask_RenderList(formattedDate, "", mContext);
        new TaskCanceller(mAuthTask).setWait(getActivity());
        mAuthTask.execute((Void) null);
        //name.setText(formattedDate);
    }

    private void noInternetSnackBar() {
        mSearchResultsList.setVisibility(View.GONE);
        textViewSJResult.setText(R.string.noInternetPrompt);
        textViewSJResult.setVisibility(View.VISIBLE);
        SnackBarNotificationUtil
                .setSnackBar(getActivity().findViewById(android.R.id.content),
                        getResources().getString(R.string.noInternetConnection))
                .setColor(getResources().getColor(R.color.colorPrimary1))
                .show();
        // removeRobotoCalendarDots();
    }

    private void noResultTryAgain() {
        mSearchResultsList.setVisibility(View.GONE);
        textViewSJResult.setText("Try again later.");
        textViewSJResult.setVisibility(View.VISIBLE);
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
        results = new CalendarSJDBUtil(mContext).getAllDetailsOfServiceJob();
        mSearchResultsList.setHasFixedSize(true);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(mContext));
        mSearchResultsList.setItemAnimator(new DefaultItemAnimator());
        mListAdapter.swapData(results);
        /*new UIThreadHandler(getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });*/
    }

    @Override
    public void onClick(ServiceJobWrapper colorWrapper) {

    }

    /**
     * Called on click of Date CAlendar the render a list of Services at CardView
     * Show a list of SJ retrieved from API
     */
    private class CalendarSJTask_RenderList extends AsyncTask<Void, Void, List<ServiceJobWrapper>> {

        public final String TAG = CalendarFragment.class.getSimpleName();

        private String mDate;
        private String mID;
        private int resultStatus = 0;

        private ArrayList<String> serviceList = new ArrayList<>();

        public CalendarSJTask_RenderList(String date, String id, Context context) {
            mDate = date;
            mID = id;
            mContext = context;
            // System.gc();
        }

        private String getLink() {
            SessionManager mSession = new SessionManager(getActivity());
            int employee_id = Integer.parseInt(mSession.getUserDetails().get(SessionManager.KEY_USER_ID));
            return String.format(SERVICE_JOB_LIST_URL, employee_id, mDate);
        }

        private void simulateNetworkAccess() {
            // Simulate network access. For 2 Seconds.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void noResponse() {
            mSearchResultsList.setVisibility(View.GONE);
            textViewSJResult.setText("There's no service job on the Date \n" + mDate + ".");
            textViewSJResult.setVisibility(View.VISIBLE);
        }

        private boolean hasInternet() {
            return new JSONHelper().isConnected(getActivity());
        }

        @Override
        protected void onPreExecute() { }

        /**
         * resultStatus
         * 0 - Default no Internet
         * 1 - ok, with data
         * 2 - no response or no Data
         * 3 - no internet??? or blank response
         *   - NO CONNECTION
         *
         *  OLD IMPLEMENTATION
         *  null - no data
         *      '' - no internet connection/ server error
         *      String - successful aResponse
         */
        @Override
        protected List<ServiceJobWrapper> doInBackground(Void... params) {
            if (!hasInternet()) {
                resultStatus = 3;
                return null;
            }

            ConvertJSON_SJ cJSON = new ConvertJSON_SJ();
            try {
                ArrayList<ServiceJobWrapper> resultList = cJSON.parseServiceListJSON(JSONHelper.GET(getLink()));
                resultStatus = (cJSON.hasResult()) ? 1 : 2;
                return resultList;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            simulateNetworkAccess();
            resultStatus = (cJSON.hasResult() ? 3 : 2);
            return null;
        }

        @Override
        protected void onPostExecute(List<ServiceJobWrapper> list) {
            switch (resultStatus) {
                case 1 :
                    if (list != null) {
                        results = list;
                        mSearchResultsList.setVisibility(View.VISIBLE);
                        mSearchResultsList.setItemAnimator(new DefaultItemAnimator());
                        mListAdapter.swapData(list);
                        textViewSJResult.setVisibility(View.GONE);
                        // populateCardList();
                    } else {
                        noResponse();
                    }
                    break;
                case 2 :
                    noResponse();
                    break;
                case 3 :
                default :
                    noInternetSnackBar();
                    break;
            }
            hideSwipeRefreshing();
        }

        @Override
        protected void onCancelled() {
            noResultTryAgain();
            hideSwipeRefreshing();
            Log.i(TAG, "onCancelled hideSwipeRefreshing() new PJ_IPITaskList_RenderList()");
        }
    }

}
