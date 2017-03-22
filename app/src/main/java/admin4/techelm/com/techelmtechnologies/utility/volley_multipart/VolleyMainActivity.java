package admin4.techelm.com.techelmtechnologies.utility.volley_multipart;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import admin4.techelm.com.techelmtechnologies.R;

public class VolleyMainActivity extends AppCompatActivity {

    private static String URL = "http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/index.php/Servicejob/servicejob_upload";
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

        // do anything before post data.. or triggered after button clicked
        new UploadFileVolleyAsync().execute();
    }

    private class UploadFileVolleyAsync extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            saveProfileAccount();
            return null;
        }

        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onPostExecute(String result) {
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void saveProfileAccount() {
        // loading or check internet connection or something...
        // ... then
        // String url = "http://www.angga-ari.com/api/something/awesome";
        VolleyMultipartRequest multipartRequest =
                new VolleyMultipartRequest(Method.POST,
                        URL,
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                String resultResponse = new String(response.data);
                                Log.e("VolleyMainActivity", resultResponse);
                                /*try {
                                    JSONObject aResponse = new JSONObject(resultResponse);
                                    String status = aResponse.getString("status");
                                    String message = aResponse.getString("message");

                                    if (status.equals("1")) {
                                        // tell everybody you have succed upload image and post strings
                                        Log.i("Messsage", message);
                                    } else {
                                        Log.i("Unexpected", message);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }*/
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
                        }
                        Log.i("Error", errorMessage);
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("api_token", "gh659gjhvdyudo973823tt9gvjf7i6ric75r76");
                        params.put("test", "test asdasdas?");
                        params.put("name", mNameInput.getText().toString());
                        params.put("location", mLocationInput.getText().toString());
                        params.put("about", mAboutInput.getText().toString());
                        params.put("contact", mContact.getText().toString());
                        return params;
                    }

                    @Override
                    protected Map<String, VolleyDataPart> getByteData() {
                        Map<String, VolleyDataPart> params = new HashMap<>();
                        // file name could found file base or direct access from real path
                        // for now just get bitmap data from ImageView
                        // params.put("cover", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), 0), "image/jpeg"));
                        params.put("uploaded_file", new VolleyDataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));

                        return params;
                    }
                };
        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
    }

}