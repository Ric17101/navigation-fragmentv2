package admin4.techelm.com.techelmtechnologies.utility.json;

/**
 * Created by admin 4 on 22/02/2017.
 * Used to convert JSON string in to WRAPPERS
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobNewReplacementPartsRatesWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;

public class ConvertJSON_SJ {

    private static final String TAG = ConvertJSON_SJ.class.getSimpleName();
    private static final String SJ_LIST_DELIM = ":-:";
    private boolean mResult = false;

    public ConvertJSON_SJ() { }

    /**
     * FALSE - BAD
     * TRUE - GOOD
     * @return
     */
    public boolean hasResult() {
        return mResult;
    }

    public ArrayList<ServiceJobWrapper> parseServiceListJSON(String JSONResult) throws JSONException {
        ArrayList<ServiceJobWrapper> translationList = new ArrayList<>();
        JSONObject json = new JSONObject(JSONResult);
        String str = "";

        JSONArray jsonArray = json.getJSONArray("servicelist");
        int jsonLen = jsonArray.length();
        this.mResult = jsonLen >= 0;

        /*if (jsonArray == null){
            return null;
        }*/
        int i = 0;
        do { // 24
            ServiceJobWrapper sw = new ServiceJobWrapper();
            sw.setID(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")));
            sw.setServiceNumber(jsonArray.getJSONObject(i).getString("service_no"));
            sw.setCustomerID(jsonArray.getJSONObject(i).getString("customer_id"));
            sw.setServiceID(jsonArray.getJSONObject(i).getString("service_id"));
            sw.setEngineerID(jsonArray.getJSONObject(i).getString("engineer_id"));
            sw.setPriceID(jsonArray.getJSONObject(i).getString("price_id"));
            sw.setComplaintsOrSymptoms(jsonArray.getJSONObject(i).getString("complaint"));
            sw.setActionsOrRemarks(jsonArray.getJSONObject(i).getString("remarks"));
            sw.setBeforeRemarks(jsonArray.getJSONObject(i).getString("remarks_before"));
            sw.setAfterRemarks(jsonArray.getJSONObject(i).getString("remarks_after"));
            sw.setEquipmentType(jsonArray.getJSONObject(i).getString("equipment_type"));
            sw.setModelOrSerial(jsonArray.getJSONObject(i).getString("serial_no"));
            sw.setStartDate(jsonArray.getJSONObject(i).getString("start_date").split(" ")[0]);
            sw.setEndDate(jsonArray.getJSONObject(i).getString("end_date").split(" ")[0]);
            sw.setStatus(jsonArray.getJSONObject(i).getString("status"));
            sw.setContractServicing(jsonArray.getJSONObject(i).getString("contract_servicing"));
            sw.setWarrantyServicing(jsonArray.getJSONObject(i).getString("warranty_servicing"));
            sw.setCharges(jsonArray.getJSONObject(i).getString("charges"));
            sw.setContractRepair(jsonArray.getJSONObject(i).getString("contract_repair"));
            sw.setWarrantyRepair(jsonArray.getJSONObject(i).getString("warranty_repair"));
            sw.setOthers(jsonArray.getJSONObject(i).getString("others"));
            sw.setTypeOfService(jsonArray.getJSONObject(i).getString("type_of_service"));
            sw.setSignatureName(jsonArray.getJSONObject(i).getString("signature_name"));
            sw.setCustomerName(jsonArray.getJSONObject(i).getString("fullname"));
            sw.setJobSite(jsonArray.getJSONObject(i).getString("job_site"));
            sw.setFax(jsonArray.getJSONObject(i).getString("fax"));
            sw.setTelephone(jsonArray.getJSONObject(i).getString("phone_no"));
            sw.setEngineerName(jsonArray.getJSONObject(i).getString("engineer_name"));
            // Log.d(TAG, sw.toString());
            translationList.add(sw);
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
    public ArrayList<ServiceJobWrapper> serviceJobList(List<String> parsedServiceJob) {
        ArrayList<ServiceJobWrapper> translationList = new ArrayList<>();
       /* if (parsedServiceJob.size() == 0 || parsedServiceJob.isEmpty()) {
            this.aResponse = true; // No result
            return null;
        }*/

        this.mResult = parsedServiceJob.size() >= 0;

        for (String credential : parsedServiceJob) {
            ServiceJobWrapper sw = new ServiceJobWrapper();
            String[] pieces = credential.split(SJ_LIST_DELIM);

            sw.setID(Integer.parseInt(pieces[0]));
            sw.setServiceNumber(pieces[1]);
            sw.setCustomerID(pieces[2]);
            sw.setServiceID(pieces[3]);
            sw.setEngineerID(pieces[4]);
            sw.setPriceID(pieces[5]);
            sw.setComplaintsOrSymptoms(pieces[6]);
            sw.setActionsOrRemarks(pieces[7]);
            sw.setBeforeRemarks(pieces[8]);
            sw.setAfterRemarks(pieces[9]);
            sw.setEquipmentType(pieces[10]);
            sw.setModelOrSerial(pieces[11]);
            sw.setStartDate(pieces[12]);
            sw.setEndDate(pieces[13]);
            sw.setStatus(pieces[14]);
            sw.setContractServicing(pieces[15]);
            sw.setWarrantyServicing(pieces[16]);
            sw.setCharges(pieces[17]);
            sw.setContractRepair(pieces[18]);
            sw.setWarrantyRepair(pieces[19]);
            sw.setOthers(pieces[20]);
            sw.setTypeOfService(pieces[21]);
            sw.setSignatureName(pieces[22]);
//            sw.setStartDateTask(pieces[23]);
//            sw.setEndDateTask(pieces[24]);
            sw.setCustomerName(pieces[25]);
            sw.setJobSite(pieces[26]);
            sw.setFax(pieces[27]);
            sw.setTelephone(pieces[28]);
            sw.setEngineerName(pieces[29]);
            // Log.d(TAG, sw.toString());
            translationList.add(sw);
        }

        return translationList;
    }


    /**
     * TODO: do something with the responses
     * This is called at ServiceJobJSON_POST after after posting Posting NewParts to web in JSON form
     *  site : servicejob_new_parts_json
     * @param JSONResult
     * @throws JSONException
     */
    public String getResponseJSONfromServiceJob(String JSONResult) throws JSONException {
        JSONObject json = new JSONObject(JSONResult);
        String str = "";

        //JSONArray jsonArray = json.getJSONArray("uploaded_file");
        JSONObject ob1 = json.getJSONObject("uploaded_file");
        JSONObject ob2 = ob1.getJSONObject("data");
        JSONArray jsonArray = ob2.getJSONArray("new_replacement_parts");
        //int jsonLen = jsonArray.length();
        //this.mResult = jsonLen >= 0;

        Log.d(TAG, "Status" + json.getInt("status")+"");
        Log.d(TAG, "data " + ob1.toString());
        if (jsonArray.length() != 0)
            Log.d(TAG, "parts_name " + jsonArray.getJSONObject(0).getString("parts_name"));
        /*
        int i = 0;
        do { // 24
            jsonArray.getJSONObject(i);
            // jsonArray.getJSONObject(i).getString("id"))
            // Log.d(TAG, jsonArray.getJSONObject(i););
            i++;
        } while (jsonLen > i);
        */
        return "UPDATE " + jsonArray.length() + "New Replacement Parts.";
    }

    /**
     * Used to List the Parts Name and Unit Price on the Form before submission
     * @param JSONResult
     * @return
     * @throws JSONException
     */
    public ArrayList<ServiceJobNewReplacementPartsRatesWrapper> getResponseJSONPartReplacementRate(String JSONResult) throws JSONException {
        ArrayList<ServiceJobNewReplacementPartsRatesWrapper> partsRateList = new ArrayList<>();

        // this.mResult = parsedServiceJob.size() >= 0;

        JSONObject json = new JSONObject(JSONResult);
        String str = "";

        //JSONArray jsonArray = json.getJSONArray("uploaded_file");
        // JSONObject ob1 = json.getJSONObject("parts_rates");
        JSONArray jsonArray = json.getJSONArray("parts_rates");

        int jsonLen = jsonArray.length();
        this.mResult = jsonLen >= 0;

        //Log.d(TAG, "Status" + json.getInt("status") + "");
        Log.d(TAG, "data " + json.toString());
        if (jsonLen == 0) {
            return null;
        } else {
            Log.d(TAG, "parts_name " + jsonArray.getJSONObject(0).getString("parts_name"));
        }
        int i = 0;
        do { // 24
            ServiceJobNewReplacementPartsRatesWrapper rate = new ServiceJobNewReplacementPartsRatesWrapper();

            JSONObject row = jsonArray.getJSONObject(i);
            rate.setId(Integer.parseInt(row.getString("id")));
            rate.setReplacementPartName(row.getString("parts_name"));
            rate.setUnitPrice(row.getString("unit_price"));
            rate.setDescription(row.getString("description"));
            // Log.d(TAG, jsonArray.getJSONObject(i););
            partsRateList.add(rate);
            i++;
        } while (jsonLen > i);

        // return "UPDATE " + jsonArray.length() + "New Replacement Parts.";

        return partsRateList;
    }
}