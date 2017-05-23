package admin4.techelm.com.techelmtechnologies.utility.image_download;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.HTTP_AUTHENTICATION_ACCESS;

/**
 * Created by admin 4 on 22/05/2017.
 * In order to handle the HTTP Authentication Header
 */

public class UILCustomDownloader extends BaseImageDownloader {

    public UILCustomDownloader(Context context) {
        super(context);
    }

    public UILCustomDownloader(Context context, int connectTimeout, int readTimeout) {
        super(context, connectTimeout, readTimeout);
    }

    /**
     * EXTENDED the BaseImageDownloader in order to Override what createConnection do
     * to retrieve the files with HTTPP Basic Authentication
     * @param url
     * @param extra
     * @return
     * @throws IOException
     */
    @Override
    protected HttpURLConnection createConnection(String url, Object extra) throws IOException {
        String encodedUrl = Uri.encode(url, ALLOWED_URI_CHARS);
        HttpURLConnection conn = (HttpURLConnection) new URL(encodedUrl).openConnection();
        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);

        // Added and Override Method
        String encoding = Base64.encodeToString(HTTP_AUTHENTICATION_ACCESS.getBytes(), Base64.DEFAULT);
        conn.setRequestProperty("Authorization", "Basic " + encoding);
        conn.connect();

        return conn;
    }

}
