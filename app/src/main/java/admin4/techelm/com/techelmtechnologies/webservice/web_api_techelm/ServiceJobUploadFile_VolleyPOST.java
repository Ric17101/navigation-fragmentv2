package admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import admin4.techelm.com.techelmtechnologies.utility.volley_multipart.VolleyDataPart;
import admin4.techelm.com.techelmtechnologies.utility.volley_multipart.VolleyMultipartRequest;
import admin4.techelm.com.techelmtechnologies.utility.volley_multipart.VolleySingleton;

import static org.apache.commons.io.FileUtils.readFileToByteArray;

/**
 * Created by admin 4 on 22/03/2017.
 * Used to Send JSON to server/web api then save the POST data
 * TODO: Should Implement at UploadFileCommand??
 * POST commmand
 */

public class ServiceJobUploadFile_VolleyPOST {

    private static final String TAG = "SJUpload_VolleyPOST";
    private static String SERVICE_JOB_UPLOAD_URL =
            "http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/index.php/Servicejob/servicejob_upload";
    private String mServiceJobLink;
    private Context mContext;
    private File mMapFile;
    private Map<String, String> mMap;
    private Map<String, VolleyDataPart> mFileParams;

    public ServiceJobUploadFile_VolleyPOST() { }

    public ServiceJobUploadFile_VolleyPOST build() { return this; }
    // Set Up Interfaces
    private OnEventListener mOnEventListener;
    public ServiceJobUploadFile_VolleyPOST setOnEventListener(OnEventListener listener) {
        this.mOnEventListener = listener;
        return this;
    }
    public interface OnEventListener {
        void onError(String msg, int error);
        void onSuccess(String msg, int success);
        // void onJSONPostResult(NetworkResponseData response);
    }

    public ServiceJobUploadFile_VolleyPOST setContext(Context context) {
        this.mContext = context;
        return this;
    }
    public ServiceJobUploadFile_VolleyPOST setLink(String link) {
        this.mServiceJobLink = link;
        return this;
    }

    /**
     * @param file - image file
     * @param fileName - e.g. "file_cover.jpg"
     * @param mimeType - e.g.
     *                 image/jpeg
     *                 audio/mpeg
     *                 video/3gpp
     * @return
     */
    public ServiceJobUploadFile_VolleyPOST addImageFile(File file, String fileName, String mimeType) {
        if (mFileParams == null) {
            mFileParams = new HashMap<>();
        }
        this.mFileParams.put("uploaded_file", new VolleyDataPart(fileName, getByteFromImageFile(file),mimeType));
        return this;
    }

    public ServiceJobUploadFile_VolleyPOST addMultipleFile(File file, String fileName, String mimeType, String count) {
        if (mFileParams == null) {
            mFileParams = new HashMap<>();
        }
        this.mFileParams.put("uploaded_file"+count, new VolleyDataPart(fileName, getByteFromImageFile(file),mimeType));
        return this;
    }
    public ServiceJobUploadFile_VolleyPOST addParam(String key, String value) {
        if (mMap == null) {
            mMap = new HashMap<>();
        }
        this.mMap.put(key, value);
        return this;
    }

    // TODO: do some check here is params has been set, before submit
    public void startUpload() {
        new UploadFileVolleyAsync().execute((Void) null);
        /*if (listIsNull()) {
            ;
        }else {
            new UploadFileVolleyAsync().execute((Void) null);
        }*/
    }
    private boolean listIsNull() {
        return this.mContext == null && this.mFileParams == null;
    }

    /**
     * Public Class for the Response from the web API or Link specified
     */
    public class NetworkResponseData {
        public String message;
        public int status;
        public String uploaded_file;
        public String toString() {
            return "\nMessage: " + message +
                    "\nStatus: " + status +
                    "\nUploaded Info: " + uploaded_file;
        }
    }
    /**
     * AsyncTask
     */
    private class UploadFileVolleyAsync extends AsyncTask<Void, Void, String> {
        protected String aResponse = "";
        private int aSuccess = 1;
        private NetworkResponseData aResponseData;

        @Override
        protected String doInBackground(Void... params) {
            saveServiceReportForJSONResponse();
            return aResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            /*if (aResponseData == null) {
                mOnEventListener.onError("Result is null.", 0);
                aSuccess = 0;
            }*/

            switch (aSuccess) {
                case 0 :
                    mOnEventListener.onError(result, aSuccess);
                    break;
                case 1 :
                    mOnEventListener.onSuccess(result, aSuccess);
                    break;
                default :
                    mOnEventListener.onError("On Default Case", aSuccess);
                    break;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        // This is also implemented at ConvertJSON
        private NetworkResponseData convertNetworkJSONResponse(String resultResponse) {
            try {
                JSONObject aResult = new JSONObject(resultResponse);
                NetworkResponseData response = new NetworkResponseData();
                response.status = aResult.getInt("status");
                response.message = aResult.getString("message");
                response.uploaded_file = "";
                if (response.status == 200) {
                    response.uploaded_file = aResult.getString("uploaded_file");
                }
                return response;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            aSuccess = 0;
            return null;
        }

        private void saveServiceReportForJSONResponse() {
            // loading or check internet connection or something...
            // ... then
            // String url = "http://www.angga-ari.com/api/something/awesome";
            VolleyMultipartRequest multipartRequest =
                    new VolleyMultipartRequest(Request.Method.POST,
                            mServiceJobLink,
                            new Response.Listener<NetworkResponse>() {
                                @Override
                                public void onResponse(NetworkResponse response) {
                                    String resultResponse = new String(response.data);
                                    Log.e(TAG, resultResponse);
                                    // TODO: get the responses from the web
//                                    aResponseData = convertNetworkJSONResponse(resultResponse);
                                    aResponse = resultResponse;
                                    aResponseData = convertNetworkJSONResponse(resultResponse);
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse networkResponse = error.networkResponse;
                            String errorMessage = "Unknown error";
                            if (networkResponse == null) {
                                if (error.getClass().equals(TimeoutError.class)) {
                                    errorMessage = "Request timeout";
                                } else if (error.getClass().equals(NoConnectionError.class)) {
                                    errorMessage = "Failed to connect server";
                                }
                            } else {
                                String result = new String(networkResponse.data);
                                try {
                                    JSONObject response = new JSONObject(result);
                                    String status = response.getString("status");
                                    String message = response.getString("message");

                                    Log.e("Error Status", status);
                                    Log.e("Error Message", message);

                                    if (networkResponse.statusCode == 404) {
                                        errorMessage = "Resource not found";
                                    } else if (networkResponse.statusCode == 401) {
                                        errorMessage = message + " Please login again";
                                    } else if (networkResponse.statusCode == 400) {
                                        errorMessage = message + " Check your inputs";
                                    } else if (networkResponse.statusCode == 500) {
                                        errorMessage = message + " Something is getting wrong";
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                aResponse = result;
                            }
                            aSuccess = 0;

                            Log.i("Error", errorMessage);
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            if (mMap == null) {
                                mMap = new HashMap<>();
                            }
                            mMap.put("api_token", "gh659gjhvdyudo973823tt9gvjf7i6ric75r76");
                            return mMap;
                        }

                        @Override
                        protected Map<String, VolleyDataPart> getByteData() {
                            Map<String, VolleyDataPart> params = mFileParams;
                            // file name could found file base or direct access from real path
                            // for now just get bitmap data from ImageView
                            // params.put("uploaded_file", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(mContext, mCoverImage.getDrawable()), "image/jpeg"));
                            // params.put("uploaded_file", new VolleyDataPart("file_cover.jpg", getByteFromImageFile(), "image/jpeg"));
                            return params;
                        }
                    };
            VolleySingleton.getInstance(mContext).addToRequestQueue(multipartRequest);
        }
    }



    /**
     * to convert a file object into a drawable
     * @return null if not convertable into (mime data like "image/jpeg")
     */
    private byte[] getByteFromImageFile(File file) {
        //if (isImage(file))
        try {
            return readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isImage(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);
        return options.outWidth != -1 && options.outHeight != -1;
    }
}
