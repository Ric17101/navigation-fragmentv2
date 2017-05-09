package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b2;

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
import android.widget.Button;
import android.widget.TextView;

import com.marcohc.robotocalendar.RobotoCalendarView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.ProjectJobViewPagerActivity;
import admin4.techelm.com.techelmtechnologies.adapter.PJ_IPITaskListAdapter;
import admin4.techelm.com.techelmtechnologies.model.projectjob.ProjectJobWrapper;
import admin4.techelm.com.techelmtechnologies.model.projectjob.b2.IPI_TaskWrapper;
import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;
import admin4.techelm.com.techelmtechnologies.utility.json.ConvertJSON_PJ_B2_IPITasks;
import admin4.techelm.com.techelmtechnologies.utility.json.JSONHelper;
import admin4.techelm.com.techelmtechnologies.webservice.command.GetCommand;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.LIST_DELIM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B2;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B3;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_EPS;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_PW;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_TYPE_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_IPI_TASK_LIST_URL;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_KEY;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * In-process Inspection (PW and EPS)
 * TASK LIST
 */
public class IPITaskListFragment extends Fragment
{
    private static final String TAG = IPITaskListFragment.class.getSimpleName();
    private static final int REQUEST_CODE = 1234;

    private TextView name;
    private RobotoCalendarView robotoCalendarView;
    private TextView textViewSJResult;
    private SlidingUpPanelLayout mLayout;

    private Context mContext;
    private PJ_IPITaskListAdapter mListAdapter;
    private RecyclerView mSearchResultsList;
    private SwipeRefreshLayout swipeRefreshServiceJobLayout;

    private List<IPI_TaskWrapper> results = null;
    private PJ_IPITaskList_RenderList mAuthTask = null;

    // Instance Variable
    private ProjectJobWrapper mProjectJob;
    private int mTypeOfForm;

    public static IPITaskListFragment newInstance(ProjectJobWrapper projectJobWrapper, int projectJobForm) {
        IPITaskListFragment fragment = new IPITaskListFragment();
        Bundle args = new Bundle();

        Log.e(TAG, "PISSTaskListFragment newInstance " + projectJobWrapper.toString());

        args.putInt(PROJECT_JOB_FORM_TYPE_KEY, projectJobForm);
        args.putParcelable(PROJECT_JOB_KEY, projectJobWrapper);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * These Two Lines should be included on every Fragment to maintain the state and do not load again
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fromBundle();

        // setRetainInstance(true);
        System.out.println("ServiceJobFragment: I'm on the onCreate");
    }

    private void fromBundle() {
        this.mTypeOfForm = getArguments().getInt(PROJECT_JOB_FORM_TYPE_KEY);
        this.mProjectJob = getArguments().getParcelable(PROJECT_JOB_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.content_b2_projectjob_tasks, container, false);

        setContext(container.getContext());

        setUpCalendarView(view);

        setUpRecyclerView(view);

        setupResultsList(view);

        setupSwipeRefreshServiceJobLayout(view);

        renderTaskList();

        initButton(view);

        return view;
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
        } else { // Restore the Data List again
            mListAdapter.swapData(results);
        }
        System.out.println("ServiceJobFragment: I'm on the onActivityCreated");
    }

    private void setContext(Context c) {
        this.mContext = c;
    }

    private void initButton(View view) {
        /** BUTTON BACK */
        Button button_back = (Button) view.findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete the Service Job from SQLite DB on Back
                ((ProjectJobViewPagerActivity)getActivity()).backToLandingPage(-1);
            }
        });

        /** BUTTON NEXT */
        Button button_next = (Button) view.findViewById(R.id.button_next);
        // button_next.setText("SAVE");
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ((ProjectJobViewPagerActivity)getActivity()).fromFragmentNavigate(1);
                ((ProjectJobViewPagerActivity)getActivity()).onClickNextButton();
            }
        });
    }

    /***** SWIPE REFRESH LAYOUT *****/
    private void setupSwipeRefreshServiceJobLayout(View view) {
        swipeRefreshServiceJobLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshServiceJobLayout);
        swipeRefreshServiceJobLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                renderTaskList();
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
        mSearchResultsList = (RecyclerView) upRecyclerView.findViewById(R.id.search_results_b2b3_project_job_list);
        textViewSJResult = (TextView) upRecyclerView.findViewById(R.id.textViewSJResult);
        textViewSJResult.setVisibility(View.GONE);
    }
    public void setupResultsList(View view) {
        mListAdapter = new PJ_IPITaskListAdapter(view.getContext());
        mSearchResultsList.setAdapter(mListAdapter);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    /**
     * NOT USED
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

    private void renderTaskList() {
        mAuthTask = new PJ_IPITaskList_RenderList(mProjectJob.getID()+"", mContext);
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

    /**
     * Called on click of Date CAlendar the render a list of Services at CardView
     * Show a list of SJ retrieved from API
     */
    private class PJ_IPITaskList_RenderList extends AsyncTask<Void, Void, List<IPI_TaskWrapper>> {

        public final String TAG = PJ_IPITaskList_RenderList.class.getSimpleName();

        private String projectjob_piss_id;
        private int resultStatus = 0;

        private GetCommand getCommand;
        private ArrayList<String> projectList = new ArrayList<String>();

        public PJ_IPITaskList_RenderList(String id, Context context) {
            projectjob_piss_id = id;
            mContext = context;
            // System.gc();
        }

        private String getURL() {
            String url = "";
            switch (mTypeOfForm) {
                case PROJECT_JOB_FORM_B2:
                    url = String.format(PROJECT_JOB_IPI_TASK_LIST_URL, mProjectJob.getID(), PROJECT_JOB_FORM_EPS);
                    break;
                case PROJECT_JOB_FORM_B3:
                    url = String.format(PROJECT_JOB_IPI_TASK_LIST_URL, mProjectJob.getID(), PROJECT_JOB_FORM_PW);
                    break;
                default: return "";
            }
            Log.e(TAG, "getURL() " + url);
            return url;
        }

        /**
         * @param JSONResult - JSON Result from the web
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

                JSONArray jsonArray = json.getJSONArray("projectlist_ipi_tasks");
                int jsonLen = json.getJSONArray("projectlist_ipi_tasks").length();
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
                            .append(jsonArray.getJSONObject(i).getString("projectjob_ipi_pw_id"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("serial_no"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("description"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("status"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("non_conformance"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("corrective_actions"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("target_completion_date"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("status_flag"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("form_type"))
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
        protected List<IPI_TaskWrapper> doInBackground(Void... params) {
            String parsedServiceJob = "";
            try {
                parsedServiceJob = parseServiceListJSON(JSONHelper.GET(getURL()));
                // JSONHelper.GET(PROJECT_JOB_PISS_TASK_LIST_URL + projectjob_piss_id));
                switch (parsedServiceJob) {
                    case "ok":
                        ConvertJSON_PJ_B2_IPITasks cJSON = new ConvertJSON_PJ_B2_IPITasks();
                        ArrayList<IPI_TaskWrapper> resultList = cJSON.projectJobTaskList(projectList);
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
        protected void onPostExecute(List<IPI_TaskWrapper> list) {
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
                    textViewSJResult.setText("There's no tasks");
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
        protected void onCancelled() { }
    }

}
