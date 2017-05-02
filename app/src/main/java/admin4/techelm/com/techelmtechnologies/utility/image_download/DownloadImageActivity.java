package admin4.techelm.com.techelmtechnologies.utility.image_download;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import admin4.techelm.com.techelmtechnologies.R;

/**
 * Created by admin 4 on 27/04/2017.
 */

public class DownloadImageActivity extends AppCompatActivity implements UILListener {

    private final static String TAG = DownloadImageActivity.class.getSimpleName();
    private static final String IMAGE_URL = "http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/downloadables/drawing_test2.jpg";
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_layout);

        imageView = (ImageView) findViewById(R.id.imageViewDownload);
        downloadImage();
    }

    private void downloadImage() {
        UILDownloader downloader = new UILDownloader(DownloadImageActivity.this);
        downloader.setImageFrom(IMAGE_URL);
        downloader.setImageView(imageView);
        downloader.start();
    }

    @Override
    public void OnHandleError(String message) {
        Log.e(TAG, "OnHandleError " + message);
    }

    @Override
    public void OnHandleStartDownload(String message) {
        Log.e(TAG, "OnHandleStartDownload " + message);
    }

    @Override
    public void OnHandleLoadingCompleted(String imageURI, Bitmap imageLoaded) {
        // imageView.setImageBitmap(imageLoaded);
        Log.e(TAG, "OnHandleLoadingCompleted " + imageURI);
    }
}
