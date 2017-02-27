package admin4.techelm.com.techelmtechnologies.webservice;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jcf on 7/27/2016.
 */
public class UtilNetConnection {
    public static final String TAG = UtilNetConnection.class.getSimpleName();
    /*request method*/
    public static final String POST ="POST";
    /*use for post*/
    public static HttpURLConnection buildConnection(String url,String requestMethod) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
       /*set request*/
        con.setRequestMethod(requestMethod);
        con.setRequestProperty("Accept-Language", "UTF-8");
        con.setDoOutput(true);
        return con;
    }
    /*use for get*/
    public static HttpURLConnection buildConnection(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        /*set request*/
        con.setRequestProperty("Accept-Language", "UTF-8");
        return con;
    }
    /*HashMap to post parameter data*/
    public static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) first = false;
            else result.append("&");
            if(entry.getKey()==null)continue;
            Log.e(TAG,entry.getKey()+":"+entry.getValue());
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue()==null?"":entry.getValue(), "UTF-8"));
        }
        Log.e(TAG, "getPostDataString: " + result.toString());
        return result.toString();
    }
    /*InputStream To String*/
    public static String inputStreamToString(InputStream inputStream){
        StringBuilder builder = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
    public static void writeParam(HttpURLConnection urlConnection,String param){
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(param);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
