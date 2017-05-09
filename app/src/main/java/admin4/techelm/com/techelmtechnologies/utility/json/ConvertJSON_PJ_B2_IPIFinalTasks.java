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


import admin4.techelm.com.techelmtechnologies.model.projectjob.b2.IPI_TaskFinalWrapper;

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
                 "id":"7",
                 "projectjob_ipi_pw_id":"3",
                 "serial_no":"3",
                 "car_no":"test3",
                 "description":"test desc 3.3",
                 "target_remedy_date":"2017-05-28",
                 "completion_date":"0000-00-00",
                 "remarks":"test",
                 "dispostion":"test",
                 "form_type":"PW"
             },
         {...}
        ]}
     * @return
     */
    public boolean hasResult() {
        return mResult;
    }

    public ArrayList<IPI_TaskFinalWrapper> parseListJSON(String JSONResult) throws JSONException {
        ArrayList<IPI_TaskFinalWrapper> translationList = new ArrayList<>();
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
            IPI_TaskFinalWrapper pw = new IPI_TaskFinalWrapper();
            pw.setID(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")));
            pw.setProjectJobIPI_PWID(jsonArray.getJSONObject(i).getString("projectjob_ipi_pw_id"));
            pw.setSerialNo(jsonArray.getJSONObject(i).getString("serial_no"));
            pw.setCarNo(jsonArray.getJSONObject(i).getString("car_no"));
            pw.setDescription(jsonArray.getJSONObject(i).getString("description"));
            pw.setTargetRemedyDate(jsonArray.getJSONObject(i).getString("target_remedy_date"));
            pw.setCompletionDate(jsonArray.getJSONObject(i).getString("completion_date"));
            pw.setRemarks(jsonArray.getJSONObject(i).getString("remarks"));
            pw.setDisposition(jsonArray.getJSONObject(i).getString("disposition"));
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
    public ArrayList<IPI_TaskFinalWrapper> projectJobFinalTaskList(List<String> parsedIPITask) {
        ArrayList<IPI_TaskFinalWrapper> translationList = new ArrayList<>();
       /* if (parsedServiceJob.size() == 0 || parsedServiceJob.isEmpty()) {
            this.aResponse = true; // No result
            return null;
        }*/

        this.mResult = parsedIPITask.size() >= 0;

        for (String credential : parsedIPITask) {
            IPI_TaskFinalWrapper sw = new IPI_TaskFinalWrapper();
            String[] pieces = credential.split(LIST_DELIM);

            sw.setID(Integer.parseInt(pieces[0]));
            sw.setProjectJobIPI_PWID(pieces[1]);
            sw.setSerialNo(pieces[2]);
            sw.setCarNo(pieces[3]);
            sw.setDescription(pieces[4]);
            sw.setTargetRemedyDate(pieces[5]);
            sw.setCompletionDate(pieces[6]);
            sw.setRemarks(pieces[7]);
            sw.setDisposition(pieces[8]);
            sw.setFormType(pieces[9]);
            translationList.add(sw);
        }
        return translationList;
    }


}