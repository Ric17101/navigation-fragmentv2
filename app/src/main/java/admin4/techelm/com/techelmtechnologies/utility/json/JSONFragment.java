package admin4.techelm.com.techelmtechnologies.utility.json;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import admin4.techelm.com.techelmtechnologies.R;

public class JSONFragment extends Fragment {

    EditText etResponse;
    TextView tvIsConnected;
    TextView textBoxResponse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = /*(View)*/ inflater.inflate(
                R.layout.json_layout, container, false);
        // return inflater.inflate(R.layout.social_layout,null);

        // get reference to the views
        textBoxResponse = (TextView) view.findViewById(R.id.textBoxResponse);
        etResponse = (EditText) view.findViewById(R.id.etResponse);
        tvIsConnected = (TextView) view.findViewById(R.id.tvIsConnected);

        // initializedConnection();

        return view;
    }

    private void initializedConnection() {
        // check if you are connected or not
        if (new JSONHelper().isConnected(getActivity())) {
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are conncted");
        } else {
            tvIsConnected.setText("You are NOT connected");
        }


        // call AsynTask to perform network operation on separate thread
        // new HttpAsyncTask().execute("http://hmkcode.appspot.com/rest/controller/get.json");
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
        System.out.println("JSONFragment: I'm on the onCreate");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("JSONFragment: I'm on the onSaveInstanceState");
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        System.out.println("JSONFragment: I'm on the onActivityCreated");
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return JSONHelper.GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getContext(), "Received!", Toast.LENGTH_LONG).show();
            try {
                JSONObject json = new JSONObject(result);

                String str = "";

                JSONArray articles = json.getJSONArray("articleList");
                str += "articles length = " + json.getJSONArray("articleList").length();
                str += "\n--------\n";
                str += "names: " + articles.getJSONObject(0).names();
                str += "\n--------\n";
                str += "url: " + articles.getJSONObject(0).getString("url");

                textBoxResponse.setText(str);
                //etResponse.setText(json.toString(1));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}