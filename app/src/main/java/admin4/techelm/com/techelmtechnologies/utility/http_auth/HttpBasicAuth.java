package admin4.techelm.com.techelmtechnologies.utility.http_auth;

/**
 * Created by admin 4 on 12/05/2017.
 */

import android.util.Base64;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//import java.util.Base64;


public class HttpBasicAuth {

    public static void main(String[] args) {

        try {
            URL url = new URL ("http://techelm2012.firstcomdemolinks.com/api/ci-rest-api-techelm/auth/user?password=password&user=admin");
            // String encoding = Base64Encoder.encode ("test1:test1");
            //String encoding = Base64.getEncoder().encodeToString("firstcom:opendemolink88".getBytes("utf-8"));
            String encoding = Base64.encodeToString("firstcom:opendemolink88".getBytes(), 1);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            InputStream content = (InputStream) connection.getInputStream();
            BufferedReader in   =
                    new BufferedReader (new InputStreamReader (content));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}
