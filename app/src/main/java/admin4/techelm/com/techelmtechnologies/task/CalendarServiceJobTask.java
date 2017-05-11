package admin4.techelm.com.techelmtechnologies.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.db.UserDBUtil;
import admin4.techelm.com.techelmtechnologies.utility.json.JSONHelper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.webservice.WebServiceRequest;
import admin4.techelm.com.techelmtechnologies.webservice.command.GetCommand;
import admin4.techelm.com.techelmtechnologies.webservice.interfaces.OnServiceListener;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebServiceInfo;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_URL;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class CalendarServiceJobTask extends AsyncTask<Void, Void, List<ServiceJobWrapper>> {

    public static final String TAG = CalendarServiceJobTask.class.getSimpleName();
    private String[] SERVICE_JOB = new String[]{""};
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world", "@dev:password:"
    };

    private final String mDate;
    private final String mID;
    private int resultStatus = 0;

    private CallbackInterface mCallback;
    private CalendarServiceJobTask mAuthTask = null;
    private Context mContext; // For DB Caching???

    // POST Command for User Login Authentication
    private GetCommand getCommand;

    public CalendarServiceJobTask(String date, String id, Context context) {
        mDate = date;
        mID = id;

        // .. Attach the interface
        mContext = context;
        try {
            mCallback = (CallbackInterface) context;
        } catch (ClassCastException ex) {
            Log.e("TASK", "Must implement the CalendarServiceJobTask in the Activity", ex);
        }
        System.gc();
    }

    private String getDetailsLink() {
        StringBuilder sb = new StringBuilder();
        sb.append(SERVICE_JOB_URL);
        sb.append("get_date_services/" + mDate);
        return sb.toString();
    }

    public String getServiceJobLink() {
        StringBuilder sb = new StringBuilder();
        sb.append(SERVICE_JOB_URL);
        sb.append("detail/" + mID);
        return sb.toString();
    }

    public interface CallbackInterface {
        /**
         * Callback invoked when clicked and onProgress
         */
        void onHandleShowProgressLogin(boolean taskStatus);
        void onHandleAuthTask(CalendarServiceJobTask mAuthTask);
        void onHandleShowDetails(String details);
        void onHandleShowServiceJobList(List<ServiceJobWrapper> list);
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

    /**
     *
     * @param JSONResult
     * @return
     *      null - no data
     *      '' - no internet connection/ server error
     *      String - successful result
     */
    private String parseJSON(String JSONResult) {
        try {
            JSONObject json = new JSONObject(JSONResult);
            String str = "";

            JSONArray jsonArray = json.getJSONArray("");
            int jsonLen = json.getJSONArray("").length();
            if (jsonLen == 0)
                return "null";

            str += "names: " + jsonArray.getJSONObject(0).names();
            str += "\n--------\n";
            str += "jsonA length = " + jsonLen;
            str += "\n--------\n";
            str += "ID: " + jsonArray.getJSONObject(0).getString("id");
            str += "\n--------\n";
            str += "Service No: " + jsonArray.getJSONObject(0).getString("service_no");
            str += "\n--------\n";
            str += "Customer ID: " + jsonArray.getJSONObject(0).getString("customer_id");
            str += "\n--------\n";
            str += "Service ID: " + jsonArray.getJSONObject(0).getString("service_id");
            str += "\n--------\n";
            str += "Engineer id: " + jsonArray.getJSONObject(0).getString("engineer_id");
            str += "\n--------\n";
            str += "Price ID: " + jsonArray.getJSONObject(0).getString("price_id");
            str += "\n--------\n";
            str += "Complaint: " + jsonArray.getJSONObject(0).getString("complaint");
            str += "\n--------\n";
            str += "Remarks: " + jsonArray.getJSONObject(0).getString("remarks");
            str += "\n--------\n";
            str += "Equipment Type: " + jsonArray.getJSONObject(0).getString("equipment_type");
            str += "\n--------\n";
            str += "Serial No: " + jsonArray.getJSONObject(0).getString("serial_no");
            str += "\n--------\n";
            str += "Start Date: " + jsonArray.getJSONObject(0).getString("start_date");
            str += "\n--------\n";
            str += "End Date: " + jsonArray.getJSONObject(0).getString("end_date");
            str += "\n--------\n";
            str += "Status: " + jsonArray.getJSONObject(0).getString("status");

            Log.e(TAG, "parseJSON: " + str);


            jsonLen+=1;
            for (int i = 0; jsonLen > i; i++) {
                StringBuilder jsonRes = new StringBuilder();
                jsonRes.append(jsonArray.getJSONObject(0).getString("id"))
                        .append(":")
                        .append(jsonArray.getJSONObject(0).getString("service_no"))
                        .append(":")
                        .append(jsonArray.getJSONObject(0).getString("customer_id"))
                        .append(":")
                        .append(jsonArray.getJSONObject(0).getString("service_id"))
                        .append(":")
                        .append(jsonArray.getJSONObject(0).getString("engineer_id"))
                        .append(":")
                        .append(jsonArray.getJSONObject(0).getString("price_id"))
                        .append(":")
                        .append(jsonArray.getJSONObject(0).getString("complaint"))
                        .append(":")
                        .append(jsonArray.getJSONObject(0).getString("remarks"))
                        .append(":")
                        .append(jsonArray.getJSONObject(0).getString("equipment_type"))
                        .append(":")
                        .append(jsonArray.getJSONObject(0).getString("serial_no"))
                        .append(":")
                        .append(jsonArray.getJSONObject(0).getString("start_date"))
                        .append(":")
                        .append(jsonArray.getJSONObject(0).getString("end_date"));
                SERVICE_JOB[i] = jsonRes.toString();
            }
            return "ok";
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
        getCommand = new GetCommand(webServiceInfo);

        //mCallback.onHandleShowDetails("2");
        /*request*/
        WebServiceRequest webServiceRequest = new WebServiceRequest(getCommand);
        webServiceRequest.execute();
        webServiceRequest.setOnServiceListener(new OnServiceListener() {
            @Override
            public void onServiceCallback(WebResponse response) {
                Log.e(TAG, "WebResponse: " + response.getStringResponse());
                // textView23.setText(response.getStringResponse());
                // SERVICE_JOB = response.getStringResponse();
                //mCallback.onHandleShowDetails("3");
                // parseJSON(response.getStringResponse());
            }
        });
    }

    private List<ServiceJobWrapper> serviceJobList(String parsedServiceJob) {
        ArrayList<ServiceJobWrapper> translationList = new ArrayList<>();
        for (String credential : SERVICE_JOB) {
            ServiceJobWrapper sw = new ServiceJobWrapper();
            String[] pieces = credential.split(":");

            sw.setID(Integer.parseInt(pieces[0]));
            sw.setServiceNumber(pieces[1]);
            sw.setCustomerID(pieces[2]);
            sw.setServiceNumber(pieces[3]);
            sw.setEngineerID(pieces[4]);
            sw.setTypeOfService(pieces[5]);
            sw.setComplaintsOrSymptoms(pieces[6]);
            sw.setActionsOrRemarks(pieces[7]);
            sw.setEquipmentType(pieces[8]);
            sw.setModelOrSerial(pieces[9]);
            sw.setStartDate(pieces[10]);
            sw.setEndDate(pieces[11]);
            Log.d("SERVICE_JOBS", sw.toString());
        }
        return translationList;
    }

    @Override
    protected void onPreExecute() { super.onPreExecute(); }

    /**
     * resultStatus
     * 0 - Default no Internet
     * 1 - ok, with data
     * 2 - no response or no Data
     * 3 - no internet??? or blank reponse
     */
    @Override
    protected List<ServiceJobWrapper> doInBackground(Void... params) {
        String parsedServiceJob = "";
        try {
            parsedServiceJob = parseJSON(JSONHelper.GET(getServiceJobLink()));
            switch (parsedServiceJob) {
                case "ok":
                    resultStatus = 1;
                    return serviceJobList(parsedServiceJob);
                case "null":
                    resultStatus = 2;
                    return null;
                case "":
                    // NO CONNECTION
                    resultStatus = 3;
                    return null;
                default:
                    Thread.sleep(2000); // Simulate network access.

                    return null; // Data Return is null or either no internet

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<ServiceJobWrapper> list) {
        switch (resultStatus) {
            case 1 :
                mCallback.onHandleShowServiceJobList(list); // mAuthTask = null;
                break;
            case 2 :
                mCallback.onHandleShowDetails("There's no data on the Date " + mDate);
                break;
            case 3 :
            default :
                mCallback.onHandleShowDetails("Error Check your Internet Connection " + mDate + " ID:" + mID);
                break;
        }
    }

    @Override
    protected void onCancelled() {
        mCallback.onHandleAuthTask(null);
        mCallback.onHandleShowProgressLogin(false);
    }

}


