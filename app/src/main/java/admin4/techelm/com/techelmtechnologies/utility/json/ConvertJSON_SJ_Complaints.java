package admin4.techelm.com.techelmtechnologies.utility.json;

/**
 * Created by admin 4 on 01/06/2017.
 * Used to convert JSON string in to WRAPPERS
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_ASRWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_CFWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_MobileWrapper;

/**
 *
 * GET : http://techelm2012.firstcomdemolinks.com/api/ci-rest-api-techelm/servicejob/get_all_servicejob_complaint_to_json_by_sjid/92
     {
         "complaints": {
             "complaint_list_count": 6,
             "complaint_list": [
                 {...},
                 {...},
                 {...},
                 {
                     "id": "4",
                     "servicejob_id": "94",
                     "servicejob_category_id": "4",
                     "date_created": "2017-06-01",
                     "active": "1",
                     "servicejob_categories_id": "4",
                     "category": "Boom",
                     "complaint_fault_list_count": 1,
                     "complaint_fault_list": [
                         {
                             "id": "17",
                             "servicejob_category_id": "4",
                             "complaint": "Damaged",
                             "date_created": "0000-00-00 00:00:00",
                             "active": "1",
                             "servicejob_complaint_fault_id": "17",
                             "servicejob_complaint_mobile_id": "4"
                         }
                     ],
                     "complaint_action_list_count": 1,
                     "complaint_action_list": [
                         {
                             "id": "5",
                             "servicejob_category_id": "4",
                             "action": "do something4",
                             "date_created": "2017-06-02",
                             "active": "1"
                         }
                     ]
                 },
                 {...},
                 {
                     "id": "6",
                     "servicejob_id": "94",
                     "servicejob_category_id": "6",
                     "date_created": "2017-06-01",
                     "active": "1",
                     "servicejob_categories_id": "6",
                     "category": "Car park booth",
                     "complaint_fault_list_count": 1,
                     "complaint_fault_list": [
                         {
                             "id": "22",
                             "servicejob_category_id": "6",
                             "complaint": "Car park booth complaint",
                             "date_created": "0000-00-00 00:00:00",
                             "active": "1",
                             "servicejob_complaint_fault_id": "22",
                             "servicejob_complaint_mobile_id": "6"
                         }
                     ],
                     "complaint_action_list_count": 0,
                     "complaint_action_list": []
                 }
             ]
         }
     }
 */

public class ConvertJSON_SJ_Complaints {

    private static final String TAG = ConvertJSON_SJ_Complaints.class.getSimpleName();
    private static final String SJ_JSON_KEY = "complaints";
    private static final String SJ_COMPLAINT_LIST_JSON_KEY = "complaint_list";
    private static final String SJ_COMPLAINT_FAULT_LIST_JSON_KEY = "complaint_fault_list";
    private static final String SJ_COMPLAINT_ACTION_LIST_JSON_KEY = "complaint_action_list";

    private boolean mResult = false;

    private ArrayList<ServiceJobComplaint_MobileWrapper> complaintMobileList;
    private ArrayList<ServiceJobComplaint_CFWrapper> complaintCFList;
    private ArrayList<ServiceJobComplaint_ASRWrapper> complaintASRList;

    public ConvertJSON_SJ_Complaints(String JSONResult) {
        /* ServiceJob Complaints Mobile */
        try {
            // Init List
            complaintCFList = new ArrayList<>();
            complaintASRList = new ArrayList<>();

            this.complaintMobileList = getResponseJSONfromServiceJobComplaintMobile(JSONResult);
        } catch (JSONException e) {
            this.complaintMobileList = null;
        }
    }

    public ArrayList<ServiceJobComplaint_MobileWrapper> getSJComplaintMobileList() {
        return this.complaintMobileList;
    }

    public ArrayList<ServiceJobComplaint_CFWrapper> getSJComplaintCFList() {
        return this.complaintCFList;
    }

    public ArrayList<ServiceJobComplaint_ASRWrapper> getSJComplaintASRList() {
        return this.complaintASRList;
    }

    /**
     * FALSE - BAD
     * TRUE - GOOD
     * @return
     */
    public boolean hasResult() {
        return mResult;
    }

    /**
     * This is called at ServiceJobJSON_POST after after posting Posting NewParts to web in JSON form
     *   sj_complaints_list
     * @throws JSONException
     */
    private ArrayList<ServiceJobComplaint_MobileWrapper> getResponseJSONfromServiceJobComplaintMobile(String JSONResult) throws JSONException {

        ArrayList<ServiceJobComplaint_MobileWrapper> list = new ArrayList<>();
        JSONObject json = new JSONObject(JSONResult);

        JSONObject ob1 = json.getJSONObject(SJ_JSON_KEY);
        JSONArray jsonArray = ob1.getJSONArray(SJ_COMPLAINT_LIST_JSON_KEY);

        /* Test Length of JSONArray */
        int jsonLen = jsonArray.length();
        if (jsonLen == 0) {
            return null;
        } else {
            this.mResult = jsonLen >= 0;
        }

        // Log.d(TAG, "Status" + json.getInt("id")+"");
        Log.d(TAG, "data " + ob1.toString());

        if (jsonArray.length() != 0)
            Log.d(TAG, "servicejob_id " + jsonArray.getJSONObject(0).getString("servicejob_id"));

        /* Store Data from Decoded JSON Array */
        /*
            "id": "4",
             "servicejob_id": "94",
             "servicejob_category_id": "4",
             "date_created": "2017-06-01",
             "active": "1",
             "servicejob_categories_id": "4",
             "category": "Boom",

             "complaint_fault_list_count": 1,
             "complaint_fault_list": [...],
             "complaint_action_list_count": 1,
             "complaint_action_list": [...]
         */
        int i = 0;
        do { // 6
            ServiceJobComplaint_MobileWrapper item = new ServiceJobComplaint_MobileWrapper();
            item.setId(jsonArray.getJSONObject(i).getInt("id"));
            item.setServiceJobID(jsonArray.getJSONObject(i).getInt("servicejob_id"));
            item.setSJCategoryId(jsonArray.getJSONObject(i).getInt("servicejob_category_id"));
            item.setDateCreated(jsonArray.getJSONObject(i).getString("date_created"));
            item.setSJCategory(jsonArray.getJSONObject(i).getString("category"));

            // Getting List for Actions
            if (hasComplaintFaultList(jsonArray.getJSONObject(i))) {
                try {
                    getResponseJSONFromServiceJobComplaintCF(jsonArray.getJSONObject(i).getJSONArray(SJ_COMPLAINT_FAULT_LIST_JSON_KEY));
                } catch (JSONException e) {
                    continue;
                }
            }

            // Getting List for Actions Lisr
            if (hasComplaintActionList(jsonArray.getJSONObject(i))) {
                try {
                    getResponseJSONFromServiceJobComplaintASR(jsonArray.getJSONObject(i).getJSONArray(SJ_COMPLAINT_ACTION_LIST_JSON_KEY));
                } catch (JSONException e) {
                    continue;
                }
            }
            list.add(item);
            // Log.d(TAG, jsonArray.getJSONObject(i););
            i++;
        } while (jsonLen > i);

        return list;
    }

    private boolean hasComplaintFaultList(JSONObject jsonObject) {
        try {
            return (jsonObject.getJSONArray(SJ_COMPLAINT_FAULT_LIST_JSON_KEY).length() > 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
        "complaint_fault_list": [
             {
                 "id": "17",
                 "servicejob_category_id": "4",
                 "complaint": "Damaged",
                 "date_created": "0000-00-00 00:00:00",
                 "active": "1",
                 "servicejob_complaint_fault_id": "17",
                 "servicejob_complaint_mobile_id": "4"
             }
         ],
     */
    private void getResponseJSONFromServiceJobComplaintCF(JSONArray JSONArray_CF_List) throws JSONException {

        ArrayList<ServiceJobComplaint_CFWrapper> list = new ArrayList<>();
        /* Test Length of JSONArray */
        int jsonLen = JSONArray_CF_List.length();

        /* Store Data from Decoded JSON Array */
        int i = 0;
        do { // 4
            ServiceJobComplaint_CFWrapper item = new ServiceJobComplaint_CFWrapper();

            item.setID(JSONArray_CF_List.getJSONObject(i).getInt("id"));
            item.setSJCategoryID(JSONArray_CF_List.getJSONObject(i).getInt("servicejob_category_id"));
            item.setSJ_CM_CF_ID(JSONArray_CF_List.getJSONObject(i).getInt("servicejob_cm_cf_id"));
            item.setComplaint(JSONArray_CF_List.getJSONObject(i).getString("complaint"));
            item.setDateCreated(JSONArray_CF_List.getJSONObject(i).getString("date_created"));
            item.setSJComplaintFaultID(JSONArray_CF_List.getJSONObject(i).getInt("servicejob_complaint_fault_id"));
            item.setSJComplaintMobileID(JSONArray_CF_List.getJSONObject(i).getInt("servicejob_complaint_mobile_id"));
            this.complaintCFList.add(item);
            i++;
        } while (jsonLen > i);
    }


    private boolean hasComplaintActionList(JSONObject jsonObject) {
        try {
            return (jsonObject.getJSONArray(SJ_COMPLAINT_ACTION_LIST_JSON_KEY).length() > 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    /*
        "complaint_action_list": [
             {
                 "id": "5",
                 "servicejob_category_id": "4",
                 "action": "do something4",
                 "date_created": "2017-06-02",
                 "active": "1"
             }
         ]
     */
    private void getResponseJSONFromServiceJobComplaintASR(JSONArray jsonArray) throws JSONException {
        /* Test Length of JSONArray */
        int jsonLen = jsonArray.length();

        /* Store Data from Decoded JSON Array */
        int i = 0;
        do { // 4
            ServiceJobComplaint_ASRWrapper item = new ServiceJobComplaint_ASRWrapper();
            item.setId(jsonArray.getJSONObject(i).getInt("id"));
            item.setSJCategoryId(jsonArray.getJSONObject(i).getInt("servicejob_category_id"));
            item.setAction(jsonArray.getJSONObject(i).getString("action"));
            item.setDateCreated(jsonArray.getJSONObject(i).getString("date_created"));
            this.complaintASRList.add(item);
            i++;
        } while (jsonLen > i);
    }

    //////////// NOT USED

    private ArrayList<ServiceJobComplaint_CFWrapper> getResponseJSONfromServiceJobComplaintCF_OLD(String JSONResult) throws JSONException {

        ArrayList<ServiceJobComplaint_CFWrapper> list = new ArrayList<>();
        JSONObject json = new JSONObject(JSONResult);

        JSONObject ob1 = json.getJSONObject(SJ_JSON_KEY);
        JSONArray jsonArray = ob1.getJSONArray("complaint_fault_list");

        /* Test Length of JSONArray */
        int jsonLen = jsonArray.length();
        if (jsonLen == 0) {
            return null;
        } else {
            this.mResult = jsonLen >= 0;
        }

        /* Store Data from Decoded JSON Array */
        int i = 0;
        do { // 4
            ServiceJobComplaint_CFWrapper item = new ServiceJobComplaint_CFWrapper();

            item.setID(jsonArray.getJSONObject(i).getInt("id"));
            item.setSJCategoryID(jsonArray.getJSONObject(i).getInt("servicejob_category_id"));
            item.setComplaint(jsonArray.getJSONObject(i).getString("complaint"));
            item.setDateCreated(jsonArray.getJSONObject(i).getString("date_created"));
            list.add(item);
            i++;
        } while (jsonLen > i);

        return list;
    }

    private ArrayList<ServiceJobComplaint_ASRWrapper> getResponseJSONfromServiceJobComplaintASR_OLD(String JSONResult) throws JSONException {

        ArrayList<ServiceJobComplaint_ASRWrapper> list = new ArrayList<>();
        JSONObject json = new JSONObject(JSONResult);

        JSONObject ob1 = json.getJSONObject(SJ_JSON_KEY);
        JSONArray jsonArray = ob1.getJSONArray("complaint_fault_list");

        /* Test Length of JSONArray */
        int jsonLen = jsonArray.length();
        if (jsonLen == 0) {
            return null;
        } else {
            this.mResult = jsonLen >= 0;
        }

        /* Store Data from Decoded JSON Array */
        int i = 0;
        do { // 4
            ServiceJobComplaint_ASRWrapper item = new ServiceJobComplaint_ASRWrapper();

            item.setId(jsonArray.getJSONObject(i).getInt("id"));
            item.setSJCategoryId(jsonArray.getJSONObject(i).getInt("servicejob_category_id"));
            item.setAction(jsonArray.getJSONObject(i).getString("action"));
            item.setDateCreated(jsonArray.getJSONObject(i).getString("date_created"));
            list.add(item);
            i++;
        } while (jsonLen > i);

        return list;
    }

}