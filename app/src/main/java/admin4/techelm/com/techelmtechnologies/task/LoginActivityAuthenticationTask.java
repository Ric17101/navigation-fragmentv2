package admin4.techelm.com.techelmtechnologies.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import admin4.techelm.com.techelmtechnologies.db.UserDBUtil;
import admin4.techelm.com.techelmtechnologies.json.JSONHelper;
import admin4.techelm.com.techelmtechnologies.model.UserLoginWrapper;
import admin4.techelm.com.techelmtechnologies.webservice.WebServiceRequest;
import admin4.techelm.com.techelmtechnologies.webservice.command.GetCommand;
import admin4.techelm.com.techelmtechnologies.webservice.interfaces.OnServiceListener;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebServiceInfo;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class LoginActivityAuthenticationTask extends AsyncTask<Void, Void, Boolean> {

    public static final String TAG = LoginActivityAuthenticationTask.class.getSimpleName();
    public static final String LOGIN_URL =
            "http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/index.php/auth/user";
    private String[] USER_CREDENTIALS = new String[]{""};
    private static final String[] DUMMY_CREDENTIALS = new String[] {
            "foo@example.com:hello", "bar@example.com:world", "@dev:password:"
    };

    private final String mEmail;
    private final String mPassword;
    private int passwordFlag = 0;
    private int usernameFlag = 0;
    private int connected = 1;
    private int loginStatus = 0;

    private CallbackInterface mCallback;
    private LoginActivityAuthenticationTask mAuthTask = null;
    private Context mContext; // Not really useful in here

    // POST Command for User Login Authentication
    private GetCommand postCommand;

    public LoginActivityAuthenticationTask(String email, String password, Context context) {
        mEmail = email;
        mPassword = password;

        // .. Attach the interface
        mContext = context;
        try {
            mCallback = (CallbackInterface) context;
        } catch (ClassCastException ex) {
            Log.e("TASK", "Must implement the LoginActivityAuthenticationTask in the Activity", ex);
        }
        System.gc();
    }

    public String getLink() {
        // Set up Login Credentials
        StringBuilder loginUser = new StringBuilder();
        loginUser.append(LOGIN_URL);
        loginUser.append("?user=" + mEmail);
        loginUser.append("&password=" + mPassword);
        return loginUser.toString();
    }

    public interface CallbackInterface {
        /**
         * Callback invoked when clicked and onProgress
         */
        void onHandleSelection(int position, UserLoginWrapper user, int mode);
        void onHandleShowProgessLogin(boolean taskStatus);
        void onHandleAuthTask(LoginActivityAuthenticationTask mAuthTask);
        void onHandleEmailError(String emailError);
        void onHandlePasswordError(String passwordError);
        void onHandleSuccessLogin(UserLoginWrapper user);
        void onHandleShowDetails(String details);
    }

    /**
     * Get User Credentials from DB
     *
     * @return List of Credentials
     */
    private List<String> getUserCredentials() {
        UserDBUtil db = new UserDBUtil(mContext);
        db.open();

        List<String> userCredential = db.getUserCredentials();

        db.close();
        return userCredential;
    }

    private String parseJSON(String JSONResult) {
        try {
            JSONObject json = new JSONObject(JSONResult);
            String str = "";

            JSONArray jsonArray = json.getJSONArray("UserCredentials");
            // {"UserCredentials":[{"status":204,"login":0,"message":"Unsuccessful login.","id":"","password":"password","username":"@de"}]}
            str += "jsonA length = " + json.getJSONArray("UserCredentials").length();
            str += "\n--------\n";
            str += "names: " + jsonArray.getJSONObject(0).names();
            str += "\n--------\n";
            str += "name: " + jsonArray.getJSONObject(0).getString("username");
            str += "\n--------\n";
            str += "Login: " + jsonArray.getJSONObject(0).getString("login");
            str += "\n--------\n";
            str += "status: " + jsonArray.getJSONObject(0).getString("status");
            str += "\n--------\n";
            str += "password: " + jsonArray.getJSONObject(0).getString("password");
            str += "\n--------\n";
            str += "id: " + jsonArray.getJSONObject(0).getString("id");
            str += "\n--------\n";
            str += "Message: " + jsonArray.getJSONObject(0).getString("message");

            Log.e(TAG, "parseJSON: " + str);

            StringBuilder jsonRes = new StringBuilder();
            jsonRes.append(jsonArray.getJSONObject(0).getString("username"))
                    .append(":")
                    .append(jsonArray.getJSONObject(0).getString("password"))
                    .append(":")
                    .append(jsonArray.getJSONObject(0).getString("message"))
                    .append(":")
                    .append(jsonArray.getJSONObject(0).getString("id"))
                    .append(":")
                    .append(jsonArray.getJSONObject(0).getString("login"))
                    .append(":")
                    .append(jsonArray.getJSONObject(0).getString("status"));

            return jsonRes.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            // mCallback.onHandleShowDetails(e.toString());
        }
        return "";
    }

    // TO DO: Network API activity
    public void postLogin(String email, String password) {
        /*web info*/
        WebServiceInfo webServiceInfo = new WebServiceInfo();
        // String url = "http://jsonplaceholder.typicode.com/posts";
         String url = "http://enercon714.firstcomdemolinks.com/sampleREST/simple-codeigniter-rest-api-master/index.php/auth/user?user=@dev&password=password";
        //String url = "http://enercon714.firstcomdemolinks.com/sampleREST/simple-codeigniter-rest-api-master/index.php/auth/user";
        webServiceInfo.setUrl(url);

        /*add parameter*/
        //webServiceInfo.addParam("user", email);
        //webServiceInfo.addParam("password", password);
        // webServiceInfo.addParam("userId", "2");

        /*postStartDate command*/
        postCommand = new GetCommand(webServiceInfo);

        //mCallback.onHandleShowDetails("2");
        /*request*/
        WebServiceRequest webServiceRequest = new WebServiceRequest(postCommand);
        webServiceRequest.execute();
        webServiceRequest.setOnServiceListener(new OnServiceListener() {
            @Override
            public void onServiceCallback(WebResponse response) {
                Log.e(TAG, "WebResponse: " + response.getStringResponse());
                // textView23.setText(response.getStringResponse());
                // USER_CREDENTIALS = response.getStringResponse();
                //mCallback.onHandleShowDetails("3");
                // parseJSON(response.getStringResponse());
            }
        });
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * 0 - no user existed
     * 1 - aSuccess login
     * 2 - invalid password but correct username
     * 3 - no response
     * 4 - no internet??? or blank reponse
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        String parsedUser = "";

        try {
            parsedUser = parseJSON(new JSONHelper().GET(getLink()));
            if (!parsedUser.equals("")) { /**GET USER DETAILS */
                USER_CREDENTIALS[0] = parsedUser;
            } else {
                Thread.sleep(2000); // Simulate network access.
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /** Check if user login input exists on the list of credentials USER_CREDENTIALS */
        if (USER_CREDENTIALS[0] != "") { // Test if no data from JSON on the WEB or no Connection
            for (String credential : USER_CREDENTIALS) {
                String[] pieces = credential.split(":");

                Log.e(TAG, "USER_CREDENTIALS: " + parsedUser);
                switch (pieces[4]) {
                    case "0" : loginStatus = 0; break;
                    case "1" : loginStatus = 1; break;
                    case "2" : loginStatus = 2; break;
                    case ""  : loginStatus = 3; break;
                    default : loginStatus = 4; break;
                }


                /** Succeses or NOT */
                switch (pieces[4]) {
                    case "1" :
                        return true;
                    case "2" :
                    case "0" :
                    case "" :
                    default :
                        return false;
                }


                /*if (pieces[0].equals(mEmail)) { // Account exists, return true if the password matches.
                    usernameFlag = 1;
                    if (pieces[1].equals(mPassword)) {
                        passwordFlag = 1;
                        return true;
                    } else {
                        passwordFlag = 2;
                        usernameFlag = 2;
                        return false;
                    }
                }*/
            }
            //usernameFlag = 1;
        }

        loginStatus = 4;
        return false;
        // TO DO: register the new account here. But since it was said that it is happening on the back end, ignore this
        // return true;
    }


    @Override
    protected void onPostExecute(final Boolean success) {
        mCallback.onHandleAuthTask(null); // mAuthTask = null;
        mCallback.onHandleShowProgessLogin(false); // showProgress(false);

        if (success) {
            // finish();
            UserLoginWrapper user = new UserLoginWrapper();
            if (USER_CREDENTIALS[0] != "") { // Test if no data from JSON on the WEB or no Connection
                for (String credential : USER_CREDENTIALS) {
                    String[] pieces = credential.split(":");
                    user.setUsername(pieces[0]);
                    user.setPassword(pieces[2]);
                    user.setID(Integer.parseInt(pieces[3]));
                }
            } else {
                user = null;
            }
            mCallback.onHandleSuccessLogin(user);
        } else {
            switch (loginStatus) {
                case 0 :
                    mCallback.onHandleEmailError("User Does not exist.");
                    break;
                case 2 :
                    mCallback.onHandlePasswordError("This password is incorrect");
                    break;
                case 3  :
                    mCallback.onHandleShowDetails("Error!\nServer Error.");
                    break;
                default :
                    mCallback.onHandleShowDetails("Error!\nCheck you internet connection.");
                    break;
            }

            /*if (connected == 1) { // Connected to teh internet
                if (usernameFlag == 1) {
                    mCallback.onHandleEmailError("This email address is invalid");
                } else if (usernameFlag == 2 && passwordFlag == 2) {
                    mCallback.onHandlePasswordError("This password is incorrect");
                }
                mCallback.onHandlePasswordError("This password is incorrect");
            } else if (loginStatus == 0) {
                mCallback.onHandleShowDetails("Error!\nUser Does not exist.");
            } else if (connected == 2) {
                mCallback.onHandleShowDetails("Error!\nThis password is incorrect.");
            }  else {
                mCallback.onHandleShowDetails("Error!\nCheck you internet connection.");
            }*/
        }

        if (mCallback != null) {
            mCallback.onHandleSelection(1, null, 2);
        }
    }

    @Override
    protected void onCancelled() {
        mCallback.onHandleAuthTask(null);
        mCallback.onHandleShowProgessLogin(false);
    }

}


