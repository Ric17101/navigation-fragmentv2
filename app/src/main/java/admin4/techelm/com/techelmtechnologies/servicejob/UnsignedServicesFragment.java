package admin4.techelm.com.techelmtechnologies.servicejob;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.UnsignedServiceJobListAdapter;
import admin4.techelm.com.techelmtechnologies.utility.UIThreadHandler;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class UnsignedServicesFragment extends Fragment implements
        UnsignedServiceJobListAdapter.OnItemClickListener {

    private Context context;

    private UnsignedServiceJobListAdapter mListAdapter;
    private RecyclerView mSearchResultsList;
    private static final int REQUEST_CODE = 1234;

    private List<ServiceJobWrapper> results = null;

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
//            UpdateJobServiceTask task = new UpdateJobServiceTask(view);
//            task.execute("");
            populateCardList();
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
            populateCardList();
        } else { // Restore the Data List again
            mListAdapter.swapData(results);
        }
        System.out.println("UnsignedServicesFragment: I'm on the onActivityCreated");
    }

    private void setContext(Context c) {
        this.context = c;
    }

    public void setUpRecyclerView(View upRecyclerView) {
        mSearchResultsList = (RecyclerView) upRecyclerView.findViewById(R.id.unsigned_service_job_list);
    }

    public void setupResultsList(View view) {
        mListAdapter = new UnsignedServiceJobListAdapter(view.getContext());
        mSearchResultsList.setAdapter(mListAdapter);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(view.getContext()));
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
        new UIThreadHandler(getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                results = getAllDetailsOfLetters();
                mSearchResultsList.setHasFixedSize(true);
                mSearchResultsList.setLayoutManager(new LinearLayoutManager(context));
                mSearchResultsList.setItemAnimator(new DefaultItemAnimator());
                mListAdapter.swapData(results);
            }
        });
    }

    private class UpdateJobServiceTask extends AsyncTask<String, Void, String> {

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
    }

    /***********************************
     * TEST
     ***********************************/

    // Getting Details Data of all ALPHABET
    public List<ServiceJobWrapper> getAllDetailsOfLetters() {
        ArrayList<ServiceJobWrapper> translationList = new ArrayList<>();
        int x = 20;
        do {
            ServiceJobWrapper alpha = new ServiceJobWrapper();
            alpha.setID(Integer.parseInt(x + ""));
            alpha.setDay(x + "");
            alpha.setDate("TestDate" + x);
            alpha.setServiceNumber("00" + x);
            alpha.setCustomer("Customer" + x);
            alpha.setEngineer("Engineer" + x);
            alpha.setStatus((x % 2 == 1) ? "Pending" : "Completed");
            translationList.add(alpha);
            x--;
        } while (x >= 10);

        return translationList;
    }


    @Override
    public void onClick(ServiceJobWrapper colorWrapper) {

    }
}
