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

import admin4.techelm.com.techelmtechnologies.model.projectjob.b2.IPI_TaskWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.LIST_DELIM;

public class ConvertJSON_PJ_B2_IPITasks {

    private static final String TAG = ConvertJSON_PJ_B2_IPITasks.class.getSimpleName();

    private boolean mResult = false;

    public ConvertJSON_PJ_B2_IPITasks() { }

    /**
     * GET http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/index.php/projectjob/get_ipi_tasks?projectjob_ipi_pw_id=1&form_type=PW
         {
         "projectlist_ipi_tasks": [
             {
             "id":"2",
             "projectjob_id":"1",
             "serial_no":"2",
             "description":"test desc 2",
             "status":"NO",
             "non_conformance":"test",
             "corrective_actions":"test",
             "target_completion_date":"2017-05-08",
             "status_flag":"4",
             "form_type":"PW"
             },
         {...}
        ]
        }
     * @return
     */
    public boolean hasResult() {
        return mResult;
    }

    public ArrayList<IPI_TaskWrapper> parseListJSON(String JSONResult) throws JSONException {
        ArrayList<IPI_TaskWrapper> translationList = new ArrayList<>();
        JSONObject json = new JSONObject(JSONResult);
        String str = "";

        JSONArray jsonArray = json.getJSONArray("projectlist_ipi_tasks");
        int jsonLen = jsonArray.length();
        this.mResult = jsonLen >= 0;

        /*if (jsonArray == null){
            return null;
        }*/
        int i = 0;
        do { // 10
            IPI_TaskWrapper pw = new IPI_TaskWrapper();
            pw.setID(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")));
            pw.setProjectJob_ID(jsonArray.getJSONObject(i).getInt("projectjob_id"));
            pw.setSerialNo(jsonArray.getJSONObject(i).getString("serial_no"));
            pw.setDescription(jsonArray.getJSONObject(i).getString("description"));
            pw.setStatusComment(jsonArray.getJSONObject(i).getString("status"));
            pw.setNonConformance(jsonArray.getJSONObject(i).getString("non_conformance"));
            pw.setCorrectiveActions(jsonArray.getJSONObject(i).getString("corrective_actions"));
            pw.setTargetCompletionDate(jsonArray.getJSONObject(i).getString("target_completion_date"));
            pw.setStatusFlag(jsonArray.getJSONObject(i).getString("status_flag"));
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
    public ArrayList<IPI_TaskWrapper> projectJobTaskList(List<String> parsedIPITask) {
        ArrayList<IPI_TaskWrapper> translationList = new ArrayList<>();
       /* if (parsedServiceJob.size() == 0 || parsedServiceJob.isEmpty()) {
            this.aResponse = true; // No result
            return null;
        }*/

        this.mResult = parsedIPITask.size() >= 0;

        for (String credential : parsedIPITask) {
            IPI_TaskWrapper sw = new IPI_TaskWrapper();
            String[] pieces = credential.split(LIST_DELIM);

            sw.setID(Integer.parseInt(pieces[0]));
            sw.setProjectJob_ID(Integer.parseInt(pieces[1]));
            sw.setSerialNo(pieces[2]);
            sw.setDescription(pieces[3]);
            sw.setStatusComment(pieces[4]);
            sw.setNonConformance(pieces[5]);
            sw.setCorrectiveActions(pieces[6]);
            sw.setTargetCompletionDate(pieces[7]);
            sw.setStatusFlag(pieces[8]);
            sw.setFormType(pieces[9]);
            translationList.add(sw);
        }
        return translationList;
    }


}