/*
package admin4.techelm.com.techelmtechnologies.utility.multipart_sampler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;
import net.gotev.uploadservice.okhttp.OkHttpStack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import admin4.techelm.com.techelmtechnologies.BuildConfig;
import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.utility.apphelper_multipart_request.AppHelper;
import admin4.techelm.com.techelmtechnologies.utility.apphelper_multipart_request.VolleyMultipartRequest;
import admin4.techelm.com.techelmtechnologies.utility.apphelper_multipart_request.VolleySingleton;
import okhttp3.OkHttpClient;

public class MultipartMainActivity extends AppCompatActivity {

    private static String LOG = "AndroidUploadService";
    private static String FILE_ARGS = "uploaded_file";
    private static String url = "http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/index.php/Servicejob/servicejob_upload";
    File imgFile = new File("/sdcard/DCIM/Screenshots/Screenshot_2017-03-20-23-21-04.png");

    private EditText mNameInput;
    private EditText mLocationInput;
    private EditText mAboutInput;
    private EditText mContact;

    private ImageView mAvatarImage;
    private ImageView mCoverImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_layout);

        mNameInput = (EditText) findViewById(R.id.input_name);
        mLocationInput = (EditText) findViewById(R.id.input_location);
        mAboutInput = (EditText) findViewById(R.id.input_about);
        mContact = (EditText) findViewById(R.id.input_contact);

        mAvatarImage = (ImageView) findViewById(R.id.avatar);
        mCoverImage = (ImageView) findViewById(R.id.cover);

        initMultipart();
        uploadMultipart(getApplicationContext());
    }

    private void initMultipart() {
        OkHttpClient client = new OkHttpClient(); // create your own OkHttp client
        UploadService.HTTP_STACK = new OkHttpStack(client); // make the library use your own OkHttp client
    }

    public void uploadMultipart(final Context context) {
        try {
            String uploadId =
                new MultipartUploadRequest(context, url)
                    // starting from 3.1+, you can also use content:// URI string instead of absolute file
                    .addFileToUpload(imgFile.getAbsolutePath(), FILE_ARGS)
                    // .setNotificationConfig(new UploadNotificationConfig())
                    .setMethod("POST")

                    .setUtf8Charset()
                    .setNotificationConfig(getNotificationConfig(R.string.multipart_upload))
                    .setMaxRetries(3)
                    .setCustomUserAgent(getUserAgent())
                    .setUsesFixedLengthStreamingMode(true)
                    .setDelegate(new UploadStatusDelegate() { // To see the Logs and responses from the server
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                            Log.d(LOG, "onProgress" + uploadInfo.getSuccessfullyUploadedFiles().toString());
                            Log.d(LOG, "onProgress" + uploadInfo.getElapsedTimeString());
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, Exception exception) {
                            Log.d(LOG, "onError" + uploadInfo.toString());
                            Log.d(LOG, "onError" + exception.getMessage());
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            Log.d(LOG, "onCompleted" + uploadInfo.getSuccessfullyUploadedFiles().toString());
                            Log.d(LOG, "onCompleted" + serverResponse.getBodyAsString());
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            Log.d(LOG, "onCancelled" + uploadInfo.getSuccessfullyUploadedFiles().toString());
                            Log.d(LOG, "onCancelled" + uploadInfo.getUploadRateString());
                        }
                    })
                    .startUpload();
            Log.e(LOG, uploadId);
        } catch (Exception exc) {
            Log.e(LOG, exc.getMessage(), exc);
        }
    }
    public String getUserAgent() {
        return "AndroidUploadService/" + BuildConfig.VERSION_NAME;
    }

    protected UploadNotificationConfig getNotificationConfig(@StringRes int title) {
        return new UploadNotificationConfig()
                .setIcon(R.mipmap.ic_upload)
                .setCompletedIcon(R.mipmap.ic_upload_success)
                .setErrorIcon(R.mipmap.ic_upload_error)
                .setCancelledIcon(R.mipmap.ic_cancelled)
                .setIconColor(Color.BLUE)
                .setCompletedIconColor(Color.GREEN)
                .setErrorIconColor(Color.RED)
                .setCancelledIconColor(Color.YELLOW)
                .setTitle(getString(title))
                .setInProgressMessage(getString(R.string.uploading))
                .setCompletedMessage(getString(R.string.upload_success))
                .setErrorMessage(getString(R.string.upload_error))
                .setCancelledMessage(getString(R.string.upload_cancelled))
                .setClickIntent(new Intent(this, MultipartMainActivity.class))
                .setClearOnAction(true)
                .setRingToneEnabled(true);
    }
}*/
