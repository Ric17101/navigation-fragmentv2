package admin4.techelm.com.techelmtechnologies.utility.json;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.HTTP_AUTHENTICATION_ACCESS;

/**
 * Created by admin 4 on 21/02/2017.
 * REQUEST for DATA and
 */

public class JSONHelper {

    public static String GET(String urlLink){
        InputStream inputStream = null;
        String result = "";
        URL url = null;

        try {
            url = new URL(urlLink);
            // create HttpClient
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // make GET request to the given URL
            String encoding = Base64.encodeToString(HTTP_AUTHENTICATION_ACCESS.getBytes(), Base64.DEFAULT);
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Authorization", "Basic " + encoding);
            connection.connect();

            // receive response as inputStream
            inputStream = connection.getInputStream();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "null"; // aResponse = "Did not work!";

            Log.d("InputStream",result);
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
            result = "error";
        }

        return result;
    }

    public static String POST(String urlLink){
        InputStream inputStream = null;
        String result = "";
        URL url = null;

        try {
            url = new URL(urlLink);
            // create HttpClient
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // make GET request to the given URL
            String encoding = Base64.encodeToString(HTTP_AUTHENTICATION_ACCESS.getBytes(), Base64.DEFAULT);
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Authorization", "Basic " + encoding);
            connection.connect();

            // receive response as inputStream
            inputStream = connection.getInputStream();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "null"; // aResponse = "Did not work!";

            Log.d("InputStream",result);
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
            result = "error";
        }
        return result;
    }

    public static String GET_old(String url){
        InputStream inputStream = null;
        String result = "";

        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "null"; // aResponse = "Did not work!";

            Log.d("InputStream",result);
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
            result = "error";
        }

        return result;
    }

    public static String POST_old(String url){
        InputStream inputStream = null;
        String result = "";

        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpPost(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "null"; // aResponse = "Did not work!";

            Log.d("InputStream",result);
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
            result = "error";
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    /**
     * Return TRUE if Device is Connected to the Internet
     * @param activity - the Calling Activity / Context
     * @return
     */
    public boolean isConnected(Activity activity) {
        ConnectivityManager connMgr = null;
        try {
            connMgr = (ConnectivityManager) activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected())
                return true;
            else
                return false;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        return false;
    }

}
