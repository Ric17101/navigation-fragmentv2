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

import com.afollestad.materialdialogs.MaterialDialog;
import com.marcohc.robotocalendar.RobotoCalendarView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.ProjectJobViewPagerActivity;
import admin4.techelm.com.techelmtechnologies.activity.servicejob_main.CalendarFragment;
import admin4.techelm.com.techelmtechnologies.adapter.PJ_IPIFinalTaskListAdapter;
import admin4.techelm.com.techelmtechnologies.model.projectjob.ProjectJobWrapper;
import admin4.techelm.com.techelmtechnologies.model.projectjob.b2.IPI_TaskFinalWrapper;
import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;
import admin4.techelm.com.techelmtechnologies.utility.json.ConvertJSON_PJ_B2_IPIFinalTasks;
import admin4.techelm.com.techelmtechnologies.utility.json.JSONHelper;
import admin4.techelm.com.techelmtechnologies.webservice.command.GetCommand;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm.ProjectJobIPI_POST;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.LIST_DELIM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B2;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B3;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_EPS;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_PW;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_TYPE_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_IPI_TASK_FINAL_LIST_URL;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_KEY;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * B2 and B3 Task List
 */
public class IPITaskListFinalFragment extends Fragment
{
    private static final String TAG = IPITaskListFinalFragment.class.getSimpleName();

    private TextView name;
    private RobotoCalendarView robotoCalendarView;
    private TextView textViewSJResult;
    private SlidingUpPanelLayout mLayout;

    private Context mContext;
    private PJ_IPIFinalTaskListAdapter mListAdapter;
    private RecyclerView mSearchResultsList;
    private SwipeRefreshLayout swipeRefreshServiceJobLayout;

    private List<IPI_TaskFinalWrapper> results = null;
    private PJFinalTask_RenderList mAuthTask = null;

    // Instance Variable
    private int mTypeOfForm;
    private ProjectJobWrapper mProjectJob;

    public static IPITaskListFinalFragment newInstance(ProjectJobWrapper projectJobWrapper, int projectJobForm) {
        IPITaskListFinalFragment fragment = new IPITaskListFinalFragment();
        Bundle args = new Bundle();

        Log.e(TAG, "IPITaskListFinalFragment newInstance " + projectJobWrapper.toString());

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
    public void onCreate(Bundle savedInstanceState) {
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

        renderListFromCalendar(Calendar.getInstance());

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
                ((ProjectJobViewPagerActivity) getActivity()).onBackPress();
            }
        });

        /** BUTTON NEXT */
        Button button_next = (Button) view.findViewById(R.id.button_next);
        // button_next.setText("SAVE");
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // ((ProjectJobViewPagerActivity)getActivity()).fromFragmentNavigate(1);
             // ((ProjectJobViewPagerActivity) getActivity()).showProjectJobLastFormFragment();
                ((ProjectJobViewPagerActivity)getActivity()).showProjectJobLastFormFragment();
            }
        });
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
        mSearchResultsList = (RecyclerView) upRecyclerView.findViewById(R.id.search_results_b2b3_project_job_list);
        textViewSJResult = (TextView) upRecyclerView.findViewById(R.id.textViewSJResult);
        textViewSJResult.setVisibility(View.GONE);
    }
    public void setupResultsList(View view) {
        mListAdapter = new PJ_IPIFinalTaskListAdapter(view.getContext());
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
        String formattedDate = new CalendarFragment().convertLongDateToSimpleDate(daySelectedCalendar); // TODO: Should remove this from other Class on Section B
        mAuthTask = new PJFinalTask_RenderList(formattedDate, "", mContext);
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
    private class PJFinalTask_RenderList extends AsyncTask<Void, Void, List<IPI_TaskFinalWrapper>> {

        public final String TAG = CalendarFragment.class.getSimpleName();

        private String mDate;
        private String mID;
        private int resultStatus = 0;

        private GetCommand getCommand;
        private ArrayList<String> projectIPITaskList = new ArrayList<>();

        public PJFinalTask_RenderList(String date, String id, Context context) {
            mDate = date;
            mID = id;
            mContext = context;
            // System.gc();
        }

        private String getURL() {
            String url = "";
            switch (mTypeOfForm) {
                case PROJECT_JOB_FORM_B2:
                    url = String.format(PROJECT_JOB_IPI_TASK_FINAL_LIST_URL, mProjectJob.getID(), PROJECT_JOB_FORM_EPS);
                    break;
                case PROJECT_JOB_FORM_B3:
                    url = String.format(PROJECT_JOB_IPI_TASK_FINAL_LIST_URL, mProjectJob.getID(), PROJECT_JOB_FORM_PW);
                    break;
                default: return "";
            }
            Log.e(TAG, "getURL() " + url);
            return url;
        }

        /**
         *
         * @param JSONResult
         * @return
         *      null - no data
         *      '' - no internet connection/ server error
         *      String - successful aResponse
         */
        private String parseIPITaskFinalListJSON(String JSONResult) {
            if (JSONResult == null || JSONResult == "")
                return "";
            try {
                JSONObject json = new JSONObject(JSONResult);
                String str = "";

                    JSONArray jsonArray = json.getJSONArray("projectlist_ipi_correctiveactions");
                int jsonLen = json.getJSONArray("projectlist_ipi_correctiveactions").length();
                if (jsonLen == 0)
                    return "null";

                str += "names: " + jsonArray.getJSONObject(0).names();
                str += "\n--------\n";
                str += "jsonA length = " + jsonLen;

                Log.d(TAG, "parseJSON: " + str);
               /*
                GET : http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/index.php/projectjob/get_ipi_tasks?projectjob_ipi_pw_id=2&form_type=PW
                    {
                       "projectlist_ipi_correctiveactions":[
                        {
                            "id":"7",
                            "projectjob_ipi_pw_id":"3",
                            "serial_no":"3",
                            "car_no":"test3",
                            "description":"test desc 3.3",
                            "target_remedy_date":"2017-05-28",
                            "completion_date":"0000-00-00",
                            "remarks":"test",
                            "dispostion":"test",
                            "form_type":"PW"
                        },
                     {...}
                 */
                // jsonLen += 1;
                int i = 0;
                do { // 12
                    StringBuilder jsonRes = new StringBuilder();
                    jsonRes.append(jsonArray.getJSONObject(i).getString("id"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("projectjob_id"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("serial_no"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("car_no"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("description"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("target_remedy_date"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("completion_date"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("remarks"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("disposition"))
                            .append(LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("form_type"))
                            .append(LIST_DELIM)
                    ;
                    projectIPITaskList.add(jsonRes.toString());
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
        protected List<IPI_TaskFinalWrapper> doInBackground(Void... params) {
            String parsedServiceJob = "";
            try {
                parsedServiceJob = parseIPITaskFinalListJSON(JSONHelper.GET(getURL()));
                switch (parsedServiceJob) {
                    case "ok":
                        ConvertJSON_PJ_B2_IPIFinalTasks cJSON = new ConvertJSON_PJ_B2_IPIFinalTasks();
                        ArrayList<IPI_TaskFinalWrapper> resultList = cJSON.projectJobFinalTaskList(projectIPITaskList);
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
        protected void onPostExecute(List<IPI_TaskFinalWrapper> list) {
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
        protected void onCancelled() { }
    }

    /**
     * B.) B2 - IPI Corrective Action Form Submission
     *
     * @param ipiTaskFinalWrapper - data to submit on the server
     * @param dialog - dialog shown on the view
     */
    public void startPostB2ProjectJobFormB(IPI_TaskFinalWrapper ipiTaskFinalWrapper, final MaterialDialog dialog) {
        ProjectJobIPI_POST projectJob = new ProjectJobIPI_POST();
        projectJob.setOnEventListener(new ProjectJobIPI_POST.OnEventListener() {
            @Override
            public void onEvent() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, message);

                SnackBarNotificationUtil
                        .setSnackBar(getView(), "Error occurred, try again later.")
                        .setColor(getResources().getColor(R.color.colorPrimary1))
                        .show();
            }

            @Override
            public void onEventResult(WebResponse response) {
                Log.e(TAG, response.getStringResponse());
                dialog.dismiss();

                // prompt user
                SnackBarNotificationUtil
                        .setSnackBar(getView(),
                                "Save to server successfully.")
                        .setColor(getResources().getColor(R.color.colorPrimary1))
                        .show();

                // Goto Main Page
                ((ProjectJobViewPagerActivity)getActivity()).backToLandingPage(1);
            }
        });

        projectJob.postIPITaskFormB(ipiTaskFinalWrapper);
    }

}
