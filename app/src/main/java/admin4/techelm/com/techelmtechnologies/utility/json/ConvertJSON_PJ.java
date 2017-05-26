package admin4.techelm.com.techelmtechnologies.utility.json;

/**
 * Created by admin 4 on 03/05/2017.
 * Used to convert JSON string in to WRAPPERS
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.model.projectjob.ProjectJobWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.LIST_DELIM;

public class ConvertJSON_PJ {

    private static final String TAG = ConvertJSON_PJ.class.getSimpleName();

    private boolean mResult = false;

    public ConvertJSON_PJ() { }

    /**
     * FALSE - BAD
     * TRUE - GOOD
     * "id":"1",
         "project_ref":"PJT00001",
         "customer_id":"2",
         "start_date":"2017-03-03",
         "end_date":"2017-03-09",
         "target_completion_date":"0000-00-00",
         "first_inspector":"1",
         "second_inspector":"0",
         "third_inspector":"0",
         "status_flag":"1",
         "fullname":"Customer2",
         "job_site":"HG10",
         "fax":"23389898",
         "phone_no":"87878"
     * @return
     */
    public boolean hasResult() {
        return mResult;
    }

    public ArrayList<ProjectJobWrapper> parseServiceListJSON(String JSONResult) throws JSONException {
        ArrayList<ProjectJobWrapper> translationList = new ArrayList<>();
        JSONObject json = new JSONObject(JSONResult);
        String str = "";

        JSONArray jsonArray = json.getJSONArray("projectlist");
        int jsonLen = jsonArray.length();
        this.mResult = jsonLen >= 0;

        /*if (jsonArray == null){
            return null;
        }*/
        int i = 0;
        do { // 24
            ProjectJobWrapper pw = new ProjectJobWrapper();
            pw.setID(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")));
            pw.setProjectRef(jsonArray.getJSONObject(i).getString("project_ref"));
            pw.setProjectSite(jsonArray.getJSONObject(i).getString("job_site"));
            pw.setTargetCompletionDate(jsonArray.getJSONObject(i).getString("target_completion_date"));
            pw.setFirstInspector(jsonArray.getJSONObject(i).getString("first_inspector"));
            pw.setSecondInspector(jsonArray.getJSONObject(i).getString("second_inspector"));
            pw.setThirdInspector(jsonArray.getJSONObject(i).getString("third_inspector"));
            pw.setCustomerID(Integer.parseInt(jsonArray.getJSONObject(i).getString("customer_id")));
            pw.setCustomerName(jsonArray.getJSONObject(i).getString("fullname"));
            pw.setStartDate(jsonArray.getJSONObject(i).getString("start_date")); // sw.setStartDate(jsonArray.getJSONObject(i).getString("fax").split(" ")[0]);
            pw.setEndDate(jsonArray.getJSONObject(i).getString("end_date")); // sw.setEndDate(jsonArray.getJSONObject(i).getString("phone_no").split(" ")[0]);
            pw.setStatus(Integer.parseInt(jsonArray.getJSONObject(i).getString("status_flag")));
            pw.setLockedToUser(Integer.parseInt(jsonArray.getJSONObject(i).getString("locked_to_user")));
            pw.setLockedToUserName(jsonArray.getJSONObject(i).getString("locked_to_user_name"));
            pw.setEngineerName(jsonArray.getJSONObject(i).getString("engineer_name"));
            pw.setFax(jsonArray.getJSONObject(i).getString("fax"));
            pw.setTelephone(jsonArray.getJSONObject(i).getString("telephone"));
            // Log.d(TAG, sw.toString());
            translationList.add(pw);
            i++;
        } while (jsonLen > i);
        return translationList;
    }

    /**
     * 24 Columns + 2
     * Index 0 to 25
     * Parse JSON String from ':'
     * SJ Details for Service JOb Begin Task Details
     * @param parsedServiceJob
     * @return
     */
    public ArrayList<ProjectJobWrapper> projectJobList(List<String> parsedServiceJob) {
        ArrayList<ProjectJobWrapper> translationList = new ArrayList<>();
       /* if (parsedServiceJob.size() == 0 || parsedServiceJob.isEmpty()) {
            this.aResponse = true; // No result
            return null;
        }*/

        this.mResult = parsedServiceJob.size() >= 0;

        for (String credential : parsedServiceJob) {
            ProjectJobWrapper sw = new ProjectJobWrapper();
            String[] pieces = credential.split(LIST_DELIM);

            sw.setID(Integer.parseInt(pieces[0]));
            sw.setProjectRef(pieces[1]);
            sw.setProjectSite(pieces[2]);
            sw.setTargetCompletionDate(pieces[3]);
            sw.setFirstInspector(pieces[4]);
            sw.setSecondInspector(pieces[5]);
            sw.setThirdInspector(pieces[6]);
            sw.setCustomerID(Integer.parseInt(pieces[7]));
            sw.setCustomerName(pieces[8]);
            sw.setStartDate(pieces[9]);
            sw.setEndDate(pieces[10]);
            sw.setStatus(Integer.parseInt(pieces[11]));
            sw.setLockedToUser(Integer.parseInt(pieces[12]));
            sw.setLockedToUserName(pieces[13]);
            sw.setEngineerName(pieces[14]);
            sw.setFax(pieces[15]);
            sw.setTelephone(pieces[16]);

            translationList.add(sw);
        }
        return translationList;
    }


}