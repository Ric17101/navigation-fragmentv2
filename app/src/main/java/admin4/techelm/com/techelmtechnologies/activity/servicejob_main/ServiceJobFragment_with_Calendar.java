package admin4.techelm.com.techelmtechnologies.activity.servicejob_main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.ServiceJobListAdapter;
import admin4.techelm.com.techelmtechnologies.db.Calendar_ServiceJob_DBUtil;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.utility.json.ConvertJSON;
import admin4.techelm.com.techelmtechnologies.utility.json.JSONHelper;
import admin4.techelm.com.techelmtechnologies.webservice.WebServiceRequest;
import admin4.techelm.com.techelmtechnologies.webservice.command.GetCommand;
import admin4.techelm.com.techelmtechnologies.webservice.command.PostCommand;
import admin4.techelm.com.techelmtechnologies.webservice.interfaces.OnServiceListener;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebServiceInfo;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_UPLOAD_URL;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ServiceJobFragment_with_Calendar extends Fragment implements
        RobotoCalendarView.RobotoCalendarListener,
        ServiceJobListAdapter.OnItemClickListener
{
    private static final String TAG = ServiceJobFragment_with_Calendar.class.getSimpleName();
    private static final int REQUEST_CODE = 1234;

    private TextView name;
    private RobotoCalendarView robotoCalendarView;
    private TextView textViewSJResult;
    private SlidingUpPanelLayout mLayout;

    private Context mContext;
    private ServiceJobListAdapter mListAdapter;
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

        setupSlidingPanel(view);

        setUpRecyclerView(view);

        setupResultsList(view);

        setupSwipeRefreshServiceJobLayout(view);

        if (results == null) {
            // populateCardList();
        }
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
                displayDotsPerMonth("setupSwipeRefreshServiceJobLayout");
                if (robotoCalendarView != null) {
                    renderListFromCalendar(robotoCalendarView.getCurrentCalendar());
                } else {
                    renderListFromCalendar(Calendar.getInstance());
                }
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
        mListAdapter = new ServiceJobListAdapter(view.getContext());
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

        displayDotsPerMonth("setUpCalendarView");
        name = (TextView) view.findViewById(R.id.name);
        renderListFromCalendar(Calendar.getInstance());

        // Set listener, in this case, the same activity
        robotoCalendarView.setRobotoCalendarListener(this);

        robotoCalendarView.setShortWeekDays(false);

        robotoCalendarView.showDateTitle(true);

        robotoCalendarView.updateView();
    }

    /********* SLIDING PANEL *********/
    private void setupSlidingPanel(View view) {
        /** Listeners and Instanstiation */
        name = (TextView) view.findViewById(R.id.name);
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
                collapseCalendarPanel(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        /** Set up Sliding Panel height to ANCHORED... */
        mLayout.setAnchorPoint(0.7f);
        collapseCalendarPanel(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    private void collapseCalendarPanel(SlidingUpPanelLayout.PanelState state) {
        mLayout.setPanelState(state);
        // displayDotsPerMonth("setUpCalendarView"); //new CalendarServiceJobDatesDots_POST().postStartDate(Calendar.MONTH + 1, Calendar.getInstance().get(Calendar.YEAR));
    }

    private void renderListFromCalendar(Calendar daySelectedCalendar) {
        String formattedDate = new CalendarFragment().convertLongDateToSimpleDate(daySelectedCalendar);
        mAuthTask = new CalendarSJTask_RenderList(formattedDate, "", mContext);
        mAuthTask.execute((Void) null);
        name.setText(formattedDate);
    }

    @Override
    public void onDayClick(Calendar daySelectedCalendar) {
        System.out.println("onDayClick: " + daySelectedCalendar.getTime());
        renderListFromCalendar(daySelectedCalendar);
        collapseCalendarPanel(SlidingUpPanelLayout.PanelState.COLLAPSED); // Collapse the panel
    }

    @Override
    public void onDayLongClick(Calendar daySelectedCalendar) {
        System.out.println("onDayLongClick: " + daySelectedCalendar.getTime());
        renderListFromCalendar(daySelectedCalendar);
    }

    @Override
    public void onRightButtonClick() {
        displayDotsPerMonth("onRightButtonClick");
    }

    @Override
    public void onLeftButtonClick() {
        displayDotsPerMonth("onLeftButtonClick");
    }

    /**
     * Display Dots on the Calendar on Click
     * @param msgStr
     */
    private void displayDotsPerMonth(String msgStr) {
        Date date;
        if (robotoCalendarView != null) { // If roboto is not Set,,, this is not realy gonna be false, just to make sure
            date = robotoCalendarView.getCurrentCalendar().getTime();
        } else {
            date = Calendar.getInstance().getTime();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        new CalendarServiceJobDatesDots_POST().post(month +1, year);
        System.out.println(msgStr + "! MONTH " + month + "YEAR " + year);
    }

    /**
     * To be implemented at Fragments later
     * or Use a another class uusing CallbackInterface for more modularity
     */
    private class CalendarServiceJobDatesDots_POST {
        private PostCommand postCommand;
        public static final String TAG = "CALENDAR_POST";

        public void cancel(View v) {
            postCommand.cancel();
        }

        public void post(final int month, final int year) {
        /*web info*/
            WebServiceInfo webServiceInfo = new WebServiceInfo();
            String url = SERVICE_JOB_UPLOAD_URL + "get_date_services_by_month";
            webServiceInfo.setUrl(url);

        /*add parameter*/
            webServiceInfo.addParam("month", month+"");
            webServiceInfo.addParam("year", year+"");

        /*postStartDate command*/
            postCommand = new PostCommand(webServiceInfo);

        /*request*/
            WebServiceRequest webServiceRequest = new WebServiceRequest(postCommand);
            webServiceRequest.execute();
            webServiceRequest.setOnServiceListener(new OnServiceListener() {
                @Override
                public void onServiceCallback(WebResponse response) {
                    Log.e(TAG, "WebResponse: " + response.getStringResponse());
                    // textView23.setText(response.getStringResponse());
                    // TODO: Add this inside the Asynctask
                    //getListSJ(response.getStringResponse());
                    new ServiceJobFragment_with_Calendar.ParseJasonToDateDotsTask().execute(response.getStringResponse());
                }
            });
        }
    }

    /**
     * Called on Change of Date CalendarView Month DOTS
     */
    private class ParseJasonToDateDotsTask extends AsyncTask<String, Void, List<ServiceJobWrapper>> {

        private boolean hasResutFlag = true; // Set to 1 if no aResponse
        /**
         * Converstion of JSON string to ServiceJob Wrapper
         * TODO: Need to store at sqlite on edit/start
         * DOING in Background...
         * @param JSONResult
         * @return
         */
        private ArrayList<ServiceJobWrapper> getListSJ(String JSONResult) {
            if (JSONResult == null || JSONResult.equals("")) { // No Connection or server is off
                hasResutFlag = false;
                return null;
            }
            try {
                ConvertJSON cJSON = new ConvertJSON();
                ArrayList<ServiceJobWrapper> resultList = cJSON.parseServiceListJSON(JSONResult);
                hasResutFlag = cJSON.hasResult();
                // return (hasResutFlag ? resultList : null);
                return resultList;
            } catch (JSONException e) {
                e.printStackTrace();
                // mCallback.onHandleShowDetails(e.toString());
            }
            return null;
        }

        protected List<ServiceJobWrapper> doInBackground(String... response) {
            if (response[0] == "")
                return null;
            return getListSJ(response[0]);
        }

        protected void onPostExecute(List<ServiceJobWrapper> list) {
            if (list != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
                Calendar calendar = Calendar.getInstance();

                for (ServiceJobWrapper sjw : list) {
                    try {
                        Date date = formatter.parse(sjw.getStartDate()); // Proper conversion of Date
                        calendar.setTime(date);
                        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DATE));
                        robotoCalendarView.markCircleImage2(calendar);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (!hasResutFlag) // Has no aResponse, else do nothing
                    noInternetSnackBar();
                else
                    noResultSnackBar();
            }
            hideSwipeRefreshing();
        }
    }

    /********* SLIDING PANEL END *********/

    private void noInternetSnackBar() {
        mSearchResultsList.setVisibility(View.GONE);
        textViewSJResult.setText(R.string.noInternetPrompt);
        textViewSJResult.setVisibility(View.VISIBLE);
        Snackbar.make(getView(), "No internet connection.", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setActionTextColor(getResources().getColor(R.color.white))
                .show();
        // removeRobotoCalendarDots();
    }

    private void noResultSnackBar() {
        mSearchResultsList.setVisibility(View.GONE);
        textViewSJResult.setText("No service job this time.");
        textViewSJResult.setVisibility(View.VISIBLE);
        // removeRobotoCalendarDots();
    }

    // NOT Used
    private void removeRobotoCalendarDots() {
        if (robotoCalendarView != null)
            robotoCalendarView.removeMarkCircleImage2(robotoCalendarView.getCurrentCalendar());
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
        results = new Calendar_ServiceJob_DBUtil(mContext).getAllDetailsOfServiceJob();
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
    private class CalendarSJTask_RenderList extends AsyncTask<Void, Void, List<ServiceJobWrapper>> {

        public final String TAG = CalendarFragment.class.getSimpleName();
        private final String SJ_LIST_DELIM = ":-:";

        private String mDate;
        private String mID;
        private int resultStatus = 0;

        private GetCommand getCommand;
        private ArrayList<String> serviceList = new ArrayList<String>();

        public CalendarSJTask_RenderList(String date, String id, Context context) {
            mDate = date;
            mID = id;
            mContext = context;
            // System.gc();
        }
        private String getDetailsLink() {
            StringBuilder sb = new StringBuilder();
            sb.append(SERVICE_JOB_UPLOAD_URL);
            sb.append("get_date_services/" + mDate);
            return sb.toString();
        }

        public String getServiceJobLink() {
            StringBuilder sb = new StringBuilder();
            sb.append(SERVICE_JOB_UPLOAD_URL);
            sb.append("detail/" + mID);
            return sb.toString();
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

                JSONArray jsonArray = json.getJSONArray("servicelist");
                int jsonLen = json.getJSONArray("servicelist").length();
                if (jsonLen == 0)
                    return "null";

                str += "names: " + jsonArray.getJSONObject(0).names();
                str += "\n--------\n";
                str += "jsonA length = " + jsonLen;
                str += "\n--------\n";
                str += "ID: " + jsonArray.getJSONObject(0).getString("id");
                str += "\n--------\n";
                str += "Service No: " + jsonArray.getJSONObject(0).getString("service_no");
                str += "\n--------\n";
                str += "Customer ID: " + jsonArray.getJSONObject(0).getString("customer_id");
                str += "\n--------\n";
                str += "Service ID: " + jsonArray.getJSONObject(0).getString("service_id");
                str += "\n--------\n";
                str += "Engineer id: " + jsonArray.getJSONObject(0).getString("engineer_id");
                str += "\n--------\n";
                str += "Price ID: " + jsonArray.getJSONObject(0).getString("price_id");
                str += "\n--------\n";
                str += "Complaint: " + jsonArray.getJSONObject(0).getString("complaint");
                str += "\n--------\n";
                str += "Remarks: " + jsonArray.getJSONObject(0).getString("remarks");
                str += "\n--------\n";
                str += "Equipment Type: " + jsonArray.getJSONObject(0).getString("equipment_type");
                str += "\n--------\n";
                str += "Serial No: " + jsonArray.getJSONObject(0).getString("serial_no");
                str += "\n--------\n";
                str += "Start Date: " + jsonArray.getJSONObject(0).getString("start_date");
                str += "\n--------\n";
                str += "End Date: " + jsonArray.getJSONObject(0).getString("end_date");
                str += "\n--------\n";
                str += "Status: " + jsonArray.getJSONObject(0).getString("status");

                Log.d(TAG, "parseJSON: " + str);

                // jsonLen += 1;
                int i = 0;
                do { // 24 + 2
                    StringBuilder jsonRes = new StringBuilder();
                    jsonRes.append(jsonArray.getJSONObject(i).getString("id"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("service_no"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("customer_id"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("service_id"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("engineer_id"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("price_id"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("complaint"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("remarks"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("remarks_before"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("remarks_after"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("equipment_type"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("serial_no"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("start_date").split(" ")[0])
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("end_date").split(" ")[0])
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("status"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("contract_servicing"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("warranty_servicing"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("charges"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("contract_repair"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("warranty_repair"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("others"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("signature_name"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("start_date_task"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("end_date_task"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("fullname"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("job_site"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("fax"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("phone_no"))
                            .append(SJ_LIST_DELIM)
                            .append(jsonArray.getJSONObject(i).getString("engineer_name"))
                            .append(SJ_LIST_DELIM)
                    ;
                    serviceList.add(jsonRes.toString());
                    i++;
                } while (jsonLen > i);

                return "ok";
            } catch (JSONException e) {
                e.printStackTrace();
                // mCallback.onHandleShowDetails(e.toString());
            }
            return "";
        }

        // TO DO: Network API activity
        public void postLogin(String email, String password) {
            /*web info*/
            WebServiceInfo webServiceInfo = new WebServiceInfo();
            // String url = "http://jsonplaceholder.typicode.com/posts";
            String url = "http://enercon714.firstcomdemolinks.com/sampleREST/simple-codeigniter-rest-api-master/index.php/auth/user?user=@dev&password=password";
            //String url = "http://enercon714.firstcomdemolinks.com/sampleREST/simple-codeigniter-rest-api-master/index.php/auth/user";
            webServiceInfo.setUrl(url);

            /*add parameter*/
            //webServiceInfo.addParam("user", email);
            //webServiceInfo.addParam("password", password);
            // webServiceInfo.addParam("userId", "2");

            /*postStartDate command*/
            getCommand = new GetCommand(webServiceInfo);

            //mCallback.onHandleShowDetails("2");
            /*request*/
            WebServiceRequest webServiceRequest = new WebServiceRequest(getCommand);
            webServiceRequest.execute();
            webServiceRequest.setOnServiceListener(new OnServiceListener() {
                @Override
                public void onServiceCallback(WebResponse response) {
                    Log.e(TAG, "WebResponse: " + response.getStringResponse());
                    // textView23.setText(response.getStringResponse());
                    // SERVICE_JOB = response.getStringResponse();
                    //mCallback.onHandleShowDetails("3");
                    // parseJSON(response.getStringResponse());
                }
            });
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
        protected List<ServiceJobWrapper> doInBackground(Void... params) {
            String parsedServiceJob = "";
            try {
                parsedServiceJob = parseServiceListJSON(JSONHelper.GET(getDetailsLink()));
                if (parsedServiceJob.equals("ok")) {
                    ConvertJSON cJSON = new ConvertJSON();
                    ArrayList<ServiceJobWrapper> resultList =  cJSON.serviceJobList(serviceList);
                    resultStatus = (cJSON.hasResult() ? 1 : 3);
                    return (resultStatus == 1 ? resultList : null);
                } else if (parsedServiceJob.equals("null")) {
                    resultStatus = 2;
                    return null;
                } else if (parsedServiceJob.equals("")) {
                    // NO CONNECTION
                    resultStatus = 3;
                    return null;
                } else {
                    Thread.sleep(2000); // Simulate network access.
                    return null; // Data Return is null or either no internet
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ServiceJobWrapper> list) {
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

    @Override
    public void onClick(ServiceJobWrapper colorWrapper) {

    }
}
