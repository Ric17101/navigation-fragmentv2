package admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm;

import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import admin4.techelm.com.techelmtechnologies.webservice.WebServiceRequest;
import admin4.techelm.com.techelmtechnologies.webservice.command.PostCommand;
import admin4.techelm.com.techelmtechnologies.webservice.interfaces.OnServiceListener;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebServiceInfo;

import static android.R.id.message;

/**
 * Created by admin 4 on 22/03/2017.
 * Used to Send JSON to server/web api then save the POST data
 * TODO: Should Implement using Thread/AsyncTask
 * POST commmand
 */

public class ServiceJobJSON_POST {

    public static final String TAG = "ServiceJobJSON_POST";
    private static final String SERVICE_JOB_URL =
            "http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/index.php/servicejob/";

    private OnEventListener mOnEventListener;
    public void setOnEventListener(OnEventListener listener) {
        mOnEventListener = listener;
    }

    public interface OnEventListener {
        void onEvent();
        void onJSONPostResult(WebResponse response);
    }

    private JSONObject getJSONParams() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rate", "1");
        jsonObject.put("comment", "OK");
        jsonObject.put("category", "pro");
        jsonObject.put("day", "19");
        jsonObject.put("month", "8");
        jsonObject.put("year", "2015");
        jsonObject.put("hour", "16");
        jsonObject.put("minute", "41");
        jsonObject.put("day_of_week", "3");
        jsonObject.put("week", "34");
        jsonObject.put("rate_number", "1");
        return jsonObject;
    }

    private HttpURLConnection setHTTPConfig(String message) throws IOException {
        URL url = new URL(SERVICE_JOB_URL);
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

    public void postJSON() {
        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            String message = getJSONParams().toString();
            conn = setHTTPConfig(message);

            //open
            conn.connect();

            //setup send
            os = new BufferedOutputStream(conn.getOutputStream());
            os.write(message.getBytes());
            //clean up
            os.flush();

            //do somehting with response
            is = conn.getInputStream();
            //String contentAsString = readIt(is,len);
            String dataString = convertStreamToString(is);
            Log.e(TAG, dataString);
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
    }

    private void onResult(WebResponse response) {
        if (mOnEventListener != null) {
            // mOnEventListener.onEvent();
            response.getStringResponse();
            mOnEventListener.onJSONPostResult(response);
        }
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
