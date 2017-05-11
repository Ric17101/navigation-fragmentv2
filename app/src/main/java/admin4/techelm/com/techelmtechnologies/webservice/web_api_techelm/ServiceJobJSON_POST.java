package admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.utility.json.ConvertJSON_SJ;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobNewPartsWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_NEW_PARTS_UPLOAD_URL;

/**
 * Created by admin 4 on 22/03/2017.
 * Purely implemented using JSON data
 * Used to Send JSON to server/web api then save the POST data
 * POST commmand
 */

public class ServiceJobJSON_POST {

    public static final String TAG = ServiceJobJSON_POST.class.getSimpleName();
    private JSONObject mJsonUpload;

    private OnEventListener mOnEventListener;
    public ServiceJobJSON_POST setOnEventListener(OnEventListener listener) {
        mOnEventListener = listener;
        return this;
    }

    public interface OnEventListener {
        void onEvent();
        void onJSONPostResult(String response);
    }

    private void onResult(String response) {
        if (mOnEventListener != null) {
            mOnEventListener.onJSONPostResult(response);
        }
    }

    public ServiceJobJSON_POST addJSONRemarks(String remarks)  throws  JSONException {
        if (this.mJsonUpload == null) {
            this.mJsonUpload = new JSONObject();
        }
        this.mJsonUpload.put("remarks", remarks);
        return this;
    }

    public ServiceJobJSON_POST addJSONNewReplacementPart(List<ServiceJobNewPartsWrapper> newPartsList) throws JSONException {
        JSONArray jsonList = new JSONArray();
        for (ServiceJobNewPartsWrapper service : newPartsList) {
            JSONObject jsonParts = new JSONObject();
            jsonParts.put("servicejob_id", service.getServiceJobId());
            jsonParts.put("parts_name", service.getPartName());
            jsonParts.put("quantity", service.getQuantity());
            jsonParts.put("unit_price", service.getUnitPrice());
            jsonParts.put("total_price", service.getTotalPrice());
            jsonList.put(jsonParts);
        }

        if (this.mJsonUpload == null) {
            this.mJsonUpload = new JSONObject();
        }
        this.mJsonUpload.put("new_replacement_parts", jsonList);
        return this;
    }

    public void startPostJSON() {
        new UploadJSONTask().execute((Void) null);
    }

    // For testing only to see what is being set on the JSON object
    public JSONObject getJsonUpload() {
        return this.mJsonUpload;
    }

    private class UploadJSONTask extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            String response = postJSON();
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private JSONObject getJSONParams() throws JSONException {
        this.mJsonUpload.put("api_token", "gh659gjhvdyudo973823tt9gvjf7i6ric75r76");
        return this.mJsonUpload;
    }

    private URLConnection setHTTPConfig2(String message) throws IOException {
        URL url = new URL(SERVICE_JOB_NEW_PARTS_UPLOAD_URL);
        URLConnection connection = url.openConnection();
        connection.setReadTimeout(10000 /*milliseconds*/);
        connection.setConnectTimeout(15000 /* milliseconds */);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        //make some HTTP header nicety
        connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        return connection;
    }

    private HttpURLConnection setHTTPConfig(String message) throws IOException {
        URL url = new URL(SERVICE_JOB_NEW_PARTS_UPLOAD_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /*milliseconds*/);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setFixedLengthStreamingMode(message.getBytes().length);

        //make some HTTP header nicety
        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        return conn;
    }

    private String postJSON() {
        String dataString = "";
        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            // Set JSON Param
            String message = getJSONParams().toString();
            conn = setHTTPConfig(message);
            conn.connect();

            //setup send
            os = new BufferedOutputStream(conn.getOutputStream());
            os.write(message.getBytes());
            //clean up
            os.flush();

            //f (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //do somehting with response
                is = conn.getInputStream();
                dataString = getInputStringToString(is);
                Log.e(TAG, dataString);
                Log.e(TAG, "Message "+conn.getResponseMessage());
                String response = new ConvertJSON_SJ().getResponseJSONfromServiceJob(dataString);
                mOnEventListener.onJSONPostResult(response);
            //}
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            //clean up
            try {
                if (os != null) { // Check if not null for safety closure
                    os.close();
                }
                if (is != null) { // Check if not null for safety closure
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (conn != null) { // Check if not null for safety closure
                conn.disconnect();
            }
        }
        return dataString;
    }

    private String getInputStringToString(InputStream is) throws IOException, JSONException {
        //Reader reader = new InputStreamReader(connection.getInputStream());

        BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);
        return responseStrBuilder.toString();
    }


    /**
     * Utility Method for reading the responses from the web
     * @param is - InputStream
     * @return string response from the web (Can be JSON or Just an Echo from the API
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
