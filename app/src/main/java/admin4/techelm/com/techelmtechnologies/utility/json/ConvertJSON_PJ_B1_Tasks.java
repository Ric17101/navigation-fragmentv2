package admin4.techelm.com.techelmtechnologies.utility.json;

/**
 * Created by admin 4 on 03/05/2017.
 * Used to convert JSON string in to PISSTaskWrapper
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.model.projectjob.b1.PISSTaskWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.LIST_DELIM;

public class ConvertJSON_PJ_B1_Tasks {

    private static final String TAG = ConvertJSON_PJ_B1_Tasks.class.getSimpleName();

    private boolean mResult = false;

    public ConvertJSON_PJ_B1_Tasks() { }

    /**
     * FALSE - BAD
     * TRUE - GOOD
     * GET http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/index.php/projectjob/get_piss_tasks?projectjob_piss_id=1
         {
         "projectlist_piss_tasks": [
         {
         "id": "19",
         "projectjob_piss_id": "1",
         "serial_no": "F-90-S00090",
         "description": "TEST",
         "conformance": "N/A",
         "comments": "TEST COmments",
         "status": "For testing 04-05",
         "drawing_before": "http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/downloadables/drawing_test2.jpg",
         "drawing_after": ""
         },
         {...}
     * @return
     */
    public boolean hasResult() {
        return mResult;
    }

    public ArrayList<PISSTaskWrapper> parseListJSON(String JSONResult) throws JSONException {
        ArrayList<PISSTaskWrapper> translationList = new ArrayList<>();
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
            PISSTaskWrapper pw = new PISSTaskWrapper();
            pw.setID(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")));
            pw.setProjectPISSID(jsonArray.getJSONObject(i).getString("projectjob_piss_id"));
            pw.setSerialNo(jsonArray.getJSONObject(i).getString("serial_no"));
            pw.setDescription(jsonArray.getJSONObject(i).getString("description"));
            pw.setConformance(jsonArray.getJSONObject(i).getString("conformance"));
            pw.setComments(jsonArray.getJSONObject(i).getString("comments"));
            pw.setStatus(jsonArray.getJSONObject(i).getString("status"));
            pw.setDrawingBefore(jsonArray.getJSONObject(i).getString("drawing_before"));
            pw.setDrawingAfter(jsonArray.getJSONObject(i).getString("drawing_after"));

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
    public ArrayList<PISSTaskWrapper> projectJobTaskList(List<String> parsedServiceJob) {
        ArrayList<PISSTaskWrapper> translationList = new ArrayList<>();
       /* if (parsedServiceJob.size() == 0 || parsedServiceJob.isEmpty()) {
            this.aResponse = true; // No result
            return null;
        }*/

        this.mResult = parsedServiceJob.size() >= 0;

        for (String credential : parsedServiceJob) {
            PISSTaskWrapper sw = new PISSTaskWrapper();
            String[] pieces = credential.split(LIST_DELIM);

            sw.setID(Integer.parseInt(pieces[0]));
            sw.setProjectPISSID(pieces[1]);
            sw.setSerialNo(pieces[2]);
            sw.setDescription(pieces[3]);
            sw.setConformance(pieces[4]);
            sw.setComments(pieces[5]);
            sw.setStatus(pieces[6]);
            sw.setDrawingBefore(pieces[7]);
            // sw.setDrawingAfter(pieces[8]);
            translationList.add(sw);
        }
        return translationList;
    }


}