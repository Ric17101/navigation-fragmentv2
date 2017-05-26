package admin4.techelm.com.techelmtechnologies.utility.json;

/**
 * Created by admin 4 on 03/05/2017.
 * Used to convert JSON string in to IPI_TaskWrapper
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import admin4.techelm.com.techelmtechnologies.model.projectjob.b2.IPI_TaskCarWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.LIST_DELIM;

public class ConvertJSON_PJ_B2_IPIFinalTasks {

    private static final String TAG = ConvertJSON_PJ_B2_IPIFinalTasks.class.getSimpleName();

    private boolean mResult = false;

    public ConvertJSON_PJ_B2_IPIFinalTasks() { }

    /**
     * GET http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/index.php/projectjob/get_ipi_correctiveAction?projectjob_ipi_pw_id=1&form_type=PW
        ...get_ipi_correctiveAction?projectjob_ipi_pw_id=3&form_type=PW
         {
             "projectlist_ipi_correctiveactions":[
             {
                 "id":"1",
                 "projectjob_id":"1",
                 "serial_no":"1",
                 "car_no":"test",
                 "description":"test desc1.1",
                 "target_remedy_date":"2017-05-20",
                 "completion_date":"0000-00-00",
                 "remarks":"test",
                 "disposition":"test",
                 "status_flag":"0",
                 "date_updated":"0000-00-00 00:00:00",
                 "form_type":"PW"
             }]
         {...}
        ]}
     * @return
     */
    public boolean hasResult() {
        return mResult;
    }

    public ArrayList<IPI_TaskCarWrapper> parseListJSON(String JSONResult) throws JSONException {
        ArrayList<IPI_TaskCarWrapper> translationList = new ArrayList<>();
        JSONObject json = new JSONObject(JSONResult);
        String str = "";

        JSONArray jsonArray = json.getJSONArray("projectlist_ipi_correctiveactions");
        int jsonLen = jsonArray.length();
        this.mResult = jsonLen >= 0;

        /*if (jsonArray == null){
            return null;
        }*/
        int i = 0;
        do { // 10
            IPI_TaskCarWrapper pw = new IPI_TaskCarWrapper();
            pw.setID(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")));
            pw.setProjectJob_ID(jsonArray.getJSONObject(i).getInt("projectjob_id"));
            pw.setSerialNo(jsonArray.getJSONObject(i).getString("serial_no"));
            pw.setCarNo(jsonArray.getJSONObject(i).getString("car_no"));
            pw.setDescription(jsonArray.getJSONObject(i).getString("description"));
            pw.setTargetRemedyDate(jsonArray.getJSONObject(i).getString("target_remedy_date"));
            pw.setCompletionDate(jsonArray.getJSONObject(i).getString("completion_date"));
            pw.setRemarks(jsonArray.getJSONObject(i).getString("remarks"));
            pw.setDisposition(jsonArray.getJSONObject(i).getString("disposition"));
            pw.setStatusFlag(jsonArray.getJSONObject(i).getString("status_flag"));
            pw.setDateUpdated(jsonArray.getJSONObject(i).getString("date_updated"));
            pw.setFormType(jsonArray.getJSONObject(i).getString("form_type"));

            // Log.d(TAG, sw.toString());
            translationList.add(pw);
            i++;
        } while (jsonLen > i);
        return translationList;
    }

    /**
     * Parse JSON String from ':'
     * SJ IPI Details for Service JOb Begin Task Details
     * @param parsedIPITask
     * @return
     */
    public ArrayList<IPI_TaskCarWrapper> projectJobFinalTaskList(List<String> parsedIPITask) {
        ArrayList<IPI_TaskCarWrapper> translationList = new ArrayList<>();
       /* if (parsedServiceJob.size() == 0 || parsedServiceJob.isEmpty()) {
            this.aResponse = true; // No result
            return null;
        }*/

        this.mResult = parsedIPITask.size() >= 0;

        for (String credential : parsedIPITask) {
            IPI_TaskCarWrapper sw = new IPI_TaskCarWrapper();
            String[] pieces = credential.split(LIST_DELIM);

            sw.setID(Integer.parseInt(pieces[0]));
            sw.setProjectJob_ID(Integer.parseInt(pieces[1]));
            sw.setSerialNo(pieces[2]);
            sw.setCarNo(pieces[3]);
            sw.setDescription(pieces[4]);
            sw.setTargetRemedyDate(pieces[5]);
            sw.setCompletionDate(pieces[6]);
            sw.setRemarks(pieces[7]);
            sw.setDisposition(pieces[8]);
            sw.setStatusFlag(pieces[9]);
            sw.setDateUpdated(pieces[10]);
            sw.setFormType(pieces[11]);
            translationList.add(sw);
        }
        return translationList;
    }


}