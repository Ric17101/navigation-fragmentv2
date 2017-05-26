package admin4.techelm.com.techelmtechnologies.activity.projectjob_main;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.login.SessionManager;
import admin4.techelm.com.techelmtechnologies.activity.servicejob_main.CalendarFragment;
import admin4.techelm.com.techelmtechnologies.adapter.PJ_ListAdapter;
import admin4.techelm.com.techelmtechnologies.db.projectjob.ProjectJobDBUtil;
import admin4.techelm.com.techelmtechnologies.model.projectjob.ProjectJobWrapper;
import admin4.techelm.com.techelmtechnologies.task.TaskCanceller;
import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;
import admin4.techelm.com.techelmtechnologies.utility.json.ConvertJSON_PJ;
import admin4.techelm.com.techelmtechnologies.utility.json.JSONHelper;
import admin4.techelm.com.techelmtechnologies.webservice.command.GetCommand;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.LIST_DELIM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_LIST_URL;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_LIST_URL;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ProjectJobChooseFormFragment extends Fragment
{
    private static final String TAG = ProjectJobChooseFormFragment.class.getSimpleName();

    private TextView name;
    private RobotoCalendarView robotoCalendarView;
    private TextView textViewSJResult;
    private SlidingUpPanelLayout mLayout;

    private Context mContext;
    private PJ_ListAdapter mListAdapter;
    private RecyclerView mSearchResultsList;
    private SwipeRefreshLayout swipeRefreshServiceJobLayout;

    private List<ProjectJobWrapper> results = null;
    private PJTask_RenderList mAuthTask = null;

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
        System.out.println("ProjectJobChooseFormFragment: I'm on the onCreate");
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //outState.putParcelable("results", results);
        System.out.println("ProjectJobChooseFormFragment: I'm on the onSaveInstanceState");
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
        System.out.println("ProjectJobChooseFormFragment: I'm on the onActivityCreated");
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
        mListAdapter = new PJ_ListAdapter(view.getContext());
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
        mAuthTask = new PJTask_RenderList(formattedDate, "", mContext);
        new TaskCanceller(mAuthTask).setWait(getActivity());
        mAuthTask.execute((Void) null);
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
        results = new ProjectJobDBUtil(mContext).getAllProjects();
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

    /**
     * Called on click of Date CAlendar the render a list of Services at CardView
     * Show a list of SJ retrieved from API
     */
    private class PJTask_RenderList extends AsyncTask<Void, Void, List<ProjectJobWrapper>> {

        public final String TAG = CalendarFragment.class.getSimpleName();

        private String mDate;
        private String mID;
        private int resultStatus = 0;

        private GetCommand getCommand;
        private ArrayList<String> projectList = new ArrayList<>();

        public PJTask_RenderList(String date, String id, Context context) {
            mDate = date;
            mID = id;
            mContext = context;
            // System.gc();
        }

        private String getLink() {
            SessionManager mSession = new SessionManager(getActivity());
            int employee_id = Integer.parseInt(mSession.getUserDetails().get(SessionManager.KEY_USER_ID));
            return String.format(PROJECT_JOB_LIST_URL, employee_id);
        }

        /**
         *
         * @param JSONResult
         * @return
         *      null - no data
         *      '' - no internet connection/ server error
         *      String - successful aResponse
         */
        private String parseServiceListJSON(String JSONResult) {
            if (JSONResult == null || JSONResult == "")
                return "";
            try {
                JSONObject json = new JSONObject(JSONResult);
                String str = "";

                JSONArray jsonArray = json.getJSONArray("projectlist");
                int jsonLen = json.getJSONArray("projectlist").length();
                if (jsonLen == 0)
                    return "null";

                str += "names: " + jsonArray.getJSONObject(0).names();
                str += "\n--------\n";
                str += "jsonA length = " + jsonLen;

                Log.d(TAG, "parseJSON: " + str);

                // jsonLen += 1;
                int i = 0;
                do { // 12
                    StringBuilder jsonRes = new StringBuilder();
                    jsonRes.append(jsonArray.getJSONObject(i).getString("id"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("project_ref"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("job_site"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("target_completion_date"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("first_inspector"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("second_inspector"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("third_inspector"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("customer_id"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("fullname"))
                            .append(LIST_DELIM)
                            /*.append(jsonArray.getJSONObject(i).getString("start_date").split(" ")[0])
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("end_date").split(" ")[0])*/
                            .append(jsonArray.getJSONObject(i).getString("start_date"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("end_date"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("status_flag"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("locked_to_user"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("locked_to_user_name"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("engineer_name"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("fax"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("phone_no"))
                            .append(LIST_DELIM)

                    ;
                    projectList.add(jsonRes.toString());
                    i++;
                } while (jsonLen > i);

                return "ok";
            } catch (JSONException e) {
                e.printStackTrace();
                // mCallback.onHandleShowDetails(e.toString());
            }
            return "";
        }

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        /**
         * resultStatus
         * 0 - Default no Internet
         * 1 - ok, with data
         * 2 - no response or no Data
         * 3 - no internet??? or blank reponse
         */
        @Override
        protected List<ProjectJobWrapper> doInBackground(Void... params) {
            String parsedServiceJob = "";
            try {
                parsedServiceJob = parseServiceListJSON(JSONHelper.GET(getLink()));
                switch (parsedServiceJob) {
                    case "ok":
                        ConvertJSON_PJ cJSON = new ConvertJSON_PJ();
                        ArrayList<ProjectJobWrapper> resultList = cJSON.projectJobList(projectList);
                        resultStatus = (cJSON.hasResult() ? 1 : 3);
                        return (resultStatus == 1 ? resultList : null);
                    case "null":
                        resultStatus = 2;
                        return null;
                    case "":
                        // NO CONNECTION
                        resultStatus = 3;
                        return null;
                    default:
                        Thread.sleep(2000); // Simulate network access.

                        return null; // Data Return is null or either no internet

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ProjectJobWrapper> list) {
            switch (resultStatus) {
                case 1 :
                    results = list;
                    mSearchResultsList.setVisibility(View.VISIBLE);
                    mSearchResultsList.setItemAnimator(new DefaultItemAnimator());
                    mListAdapter.swapData(list);
                    textViewSJResult.setVisibility(View.GONE);
                    // populateCardList();
                    break;
                case 2 :
                    mSearchResultsList.setVisibility(View.GONE);
                    textViewSJResult.setText("There's no service job on the Date \n" + mDate + ".");
                    textViewSJResult.setVisibility(View.VISIBLE);
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
        }
    }

}
