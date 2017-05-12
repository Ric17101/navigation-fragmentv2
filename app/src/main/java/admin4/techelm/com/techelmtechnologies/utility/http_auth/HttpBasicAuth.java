package admin4.techelm.com.techelmtechnologies.utility.http_auth;

/**
 * Created by admin 4 on 12/05/2017.
 */

import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.util.Base64;

//import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.HTTP_AUTHENTICATION_ACCESS;

//import java.util.Base64;


public class HttpBasicAuth {

    public static void main(String[] args) {

        testAuthetication();

    }

    public void testAuth() {
        new testAuthTASK().execute((Void) null);
    }

    private class testAuthTASK extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            executeAUth();
            return "";
        }
    }

    private void executeAUth() {
        URL url = null;
        try {
            url = new URL("http://techelm2012.firstcomdemolinks.com/api/ci-rest-api-techelm/auth/user?password=password&user=admin");

            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();

            //httpUrlConnection.setUseCaches(false);
//            httpUrlConnection.setRequestProperty("User-Agent", "MyAgent");
//            httpUrlConnection.setConnectTimeout(30000);
//            httpUrlConnection.setReadTimeout(30000);

            String encoding = Base64.encodeToString(HTTP_AUTHENTICATION_ACCESS.getBytes(), Base64.DEFAULT);
            httpUrlConnection.addRequestProperty("Authorization", "Basic " + encoding);
            //httpUrlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            httpUrlConnection.connect();

            // Display the Response
            InputStream content = (InputStream) httpUrlConnection.getInputStream();
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Not working in Android
    public static void testAuthetication() {
        try {
            URL url = new URL("http://techelm2012.firstcomdemolinks.com/api/ci-rest-api-techelm/auth/user?password=password&user=admin");
            // String encoding = Base64Encoder.encode ("test1:test1");
            // byte[] encoding = Base64.encodeBase64("firstcom:opendemolink88".getBytes("UTF-8"));
            //String encoding = Base64.encodeToString("firstcom:opendemolink88".getBytes("utf-8"));
            // byte[] encoding = Base64.encode("firstcom:opendemolink88".getBytes("utf-8"), Base64.DEFAULT);
            String encoding = Base64.encodeToString("firstcom:opendemolink88".getBytes(), Base64.DEFAULT);
            System.out.println(encoding + "");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            InputStream content = (InputStream) connection.getInputStream();
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (NetworkOnMainThreadException | IOException e) {
            e.printStackTrace();
        }
    }

    private class testtestAutheticationTASK extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            testAuthetication();
            return "";
        }
    }
}
