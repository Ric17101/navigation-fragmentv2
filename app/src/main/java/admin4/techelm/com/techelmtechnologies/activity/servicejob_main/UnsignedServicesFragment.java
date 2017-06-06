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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.login.SessionManager;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_UnsignedListAdapter;
import admin4.techelm.com.techelmtechnologies.db.servicejob.CalendarSJDBUtil;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.task.TaskCanceller;
import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;
import admin4.techelm.com.techelmtechnologies.utility.UIThreadHandler;
import admin4.techelm.com.techelmtechnologies.utility.json.ConvertJSON_SJ;
import admin4.techelm.com.techelmtechnologies.utility.json.JSONHelper;

import java.util.ArrayList;
import java.util.List;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_UNSIGNED_LIST_URL;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class UnsignedServicesFragment extends Fragment implements
        SJ_UnsignedListAdapter.OnItemClickListener {

    public final String TAG = UnsignedServicesFragment.class.getSimpleName();

    private SJ_UnsignedListAdapter mListAdapter;
    private RecyclerView mSearchResultsList;
    private static final int REQUEST_CODE = 1234;

    private List<ServiceJobWrapper> results = null;
    private SwipeRefreshLayout swipeRefreshCalendarLayout;
    private Context mContext;
    private TextView textViewUnsignedSJResult;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UnsignedFormSJTask_RenderList mAuthTask = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = /*(View)*/ inflater.inflate(
                R.layout.unsigned_services_activity, container, false);
        setContext(container.getContext());

        setUpRecyclerView(view);

        setupResultsList(view);

        if (results == null) {
            // UpdateJobServiceTask task = new UpdateJobServiceTask(view);
            // task.execute("");
            //populateCardList();
        }

        setupSwipeRefreshCalendarLayout(view);

        return view;
    }

    /**
     * These Two Lines should be included on every Fragment to maintain the state and do not load again
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        System.out.println("UnsignedServicesFragment: I'm on the onCreate");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setRetainInstance(true);
        System.out.println("UnsignedServicesFragment: I'm on the onSaveInstanceState");
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
        System.out.println("UnsignedServicesFragment: I'm on the onActivityCreated");
    }

    private void setContext(Context c) {
        this.mContext = c;
    }

    /***** SWIPE REFRESH LAYOUT *****/
    private void setupSwipeRefreshCalendarLayout(View view) {
        swipeRefreshCalendarLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshCalendarLayout);
        swipeRefreshCalendarLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                renderListFromAPI();
            }
        });
        swipeRefreshCalendarLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
    }

    private void hideSwipeRefreshing() {
        if (swipeRefreshCalendarLayout != null)
            swipeRefreshCalendarLayout.setRefreshing(false);
    }

    public void setUpRecyclerView(View upRecyclerView) {
        mSearchResultsList = (RecyclerView) upRecyclerView.findViewById(R.id.unsigned_service_job_list);
    }

    public void setupResultsList(View view) {
        mListAdapter = new SJ_UnsignedListAdapter(getActivity()); // this should be view.getContext() else Just getActivity from FragmentActivity
        mSearchResultsList.setAdapter(mListAdapter);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Set up Unsigned Result List
        textViewUnsignedSJResult = (TextView) view.findViewById(R.id.textViewUnsignedSJResult);
        textViewUnsignedSJResult.setVisibility(View.VISIBLE);
        textViewUnsignedSJResult.setText("Swipe to refresh list");
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

    private void renderListFromAPI() {
        mAuthTask = new UnsignedFormSJTask_RenderList(this.mContext);
        new TaskCanceller(mAuthTask).setWait(getActivity());
        mAuthTask.execute((Void) null);
    }

    // Not used, for testing Only
    private void populateCardList() {
        new UIThreadHandler(getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //results = getAllDetailsOfLetters();
                results = new CalendarSJDBUtil(mContext).getAllDetailsOfServiceJob();
                mSearchResultsList.setHasFixedSize(true);
                mSearchResultsList.setLayoutManager(new LinearLayoutManager(mContext));
                mSearchResultsList.setItemAnimator(new DefaultItemAnimator());
                mListAdapter.swapData(results);
            }
        });
    }

    private void noInternetSnackBar() {
        mSearchResultsList.setVisibility(View.GONE);
        textViewUnsignedSJResult.setText(R.string.noInternetPrompt);
        textViewUnsignedSJResult.setVisibility(View.VISIBLE);
        SnackBarNotificationUtil
                .setSnackBar(getActivity().findViewById(android.R.id.content),
                        getResources().getString(R.string.noInternetConnection))
                .setColor(getResources().getColor(R.color.colorPrimary1))
                .show();
    }

    private void noResultSnackBar() {
        mSearchResultsList.setVisibility(View.GONE);
        textViewUnsignedSJResult.setText("No service job this time.\nSwipe to refresh");
        textViewUnsignedSJResult.setVisibility(View.VISIBLE);
    }

    private void noResultTryAgain() {
        mSearchResultsList.setVisibility(View.GONE);
        textViewUnsignedSJResult.setText("Try again later.");
        textViewUnsignedSJResult.setVisibility(View.VISIBLE);
    }

    public void messageFromTask(String message) {
        System.out.println("CalendarFragment: I'm on the messageFromTask " + message);
    }

    @Override
    public void onClick(ServiceJobWrapper colorWrapper) {

    }

    private class UnsignedFormSJTask_RenderList extends AsyncTask<Void, Void, List<ServiceJobWrapper>> {

        private int resultStatus = 0;

        private ArrayList<String> serviceList = new ArrayList<>();

        public UnsignedFormSJTask_RenderList(Context context) {
            mContext = context;
            // System.gc();
        }

        private String getEmployeeID_ForLink() {
            SessionManager mSession = new SessionManager(getActivity());
            return mSession.getUserDetails().get(SessionManager.KEY_USER_ID);
        }

        private String getLink() {
            SessionManager mSession = new SessionManager(getActivity());
            int employee_id = Integer.parseInt(mSession.getUserDetails().get(SessionManager.KEY_USER_ID));
            return String.format(SERVICE_JOB_UNSIGNED_LIST_URL, employee_id);
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
            messageFromTask("There's no data on the Date ");
            mSearchResultsList.setVisibility(View.GONE);
            textViewUnsignedSJResult.setText("No service job this time.\nSwipe to refresh");
            textViewUnsignedSJResult.setVisibility(View.VISIBLE);
            noResultSnackBar();
        }

        private boolean hasInternet() {
            return new JSONHelper().isConnected(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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
                case 1:
                    if (list != null) {
                        results = list;
                        mSearchResultsList.setItemAnimator(new DefaultItemAnimator());
                        mListAdapter.swapData(list);
                        mSearchResultsList.setVisibility(View.VISIBLE);
                        textViewUnsignedSJResult.setVisibility(View.GONE);
                    } else {
                        noResponse();
                    }
                    break;
                case 2:
                    noResponse();
                    break;
                case 3:
                default:
                    messageFromTask("Error Check your Internet Connection");
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
