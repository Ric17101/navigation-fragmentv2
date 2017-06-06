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

import static admin4.techelm.com.techelmtechnologies.utility.Constants.LIST_DELIM;

    /*
    POST :
        http://techelm2012.firstcomdemolinks.com/api/ci-rest-api-techelm/servicejob/get_date_services_by_month_and_by_employee_id
    PARAMETERS :
        employee_id: 6
        month: 6
        year: 2017
    RESPONSE :
        "servicelist": [
            {
                "id": "98",
                "service_no": "SEV2017061003",
                "customer_id": "1",
                "service_id": "6",
                "type_of_service": "",
                "complaint": "",
                "engineer_id": "6",
                "locked_to_user": "0",
                "remarks": "asdsad",
                "remarks_before": "",
                "remarks_after": "",
                "equipment_type": "EQUIP00001",
                "serial_no": "21asdsad",
                "start_date": "2017-06-01 05:04:50",
                "end_date": "0000-00-00 00:00:00",
                "status": "0",
                "signature_name": "",
                "start_date_task": "0000-00-00 00:00:00",
                "end_date_task": "0000-00-00 00:00:00",
                "date_created": "2017-06-01 05:04:50",
                "active": "1",
                "customer_name": "Customer1",
                "job_site": "Schenker Phils. Inc",
                "fax": "1234567",
                "phone_no": "23323",
                "engineer_name": "Administrator",
                "locked_to": null
            },
     */
public class ConvertJSON_SJ {

    private static final String TAG = ConvertJSON_SJ.class.getSimpleName();
    private boolean mResult = false;
    private boolean mHasResponse = false;

    public ConvertJSON_SJ() { }

    /**
     * FALSE - BAD
     * TRUE - GOOD
     * @return
     */
    public boolean hasResult() { return mResult; }
    public boolean hasResponse() { return mHasResponse; }

    public ArrayList<ServiceJobWrapper> parseServiceListJSON(String JSONResult) throws JSONException {
        ArrayList<ServiceJobWrapper> translationList = new ArrayList<ServiceJobWrapper>();

        JSONObject json = new JSONObject(JSONResult); // TODO: what error will be displayed on the list
        JSONArray jsonArray = json.getJSONArray("servicelist"); // TODO: no result or just readable.
        int jsonLen = jsonArray.length();
        this.mResult = jsonLen > 0;
        this.mHasResponse = true; // Passed thru the JSONObject Conversion
        int i = 0;
        do { // 24
            ServiceJobWrapper sw = new ServiceJobWrapper();
            sw.setID(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")));
            sw.setServiceNumber(jsonArray.getJSONObject(i).getString("service_no"));
            sw.setCustomerID(jsonArray.getJSONObject(i).getString("customer_id"));
            sw.setServiceID(jsonArray.getJSONObject(i).getString("service_id"));
            sw.setTypeOfService(jsonArray.getJSONObject(i).getString("type_of_service"));
            sw.setComplaintsOrSymptoms(jsonArray.getJSONObject(i).getString("complaint"));
            sw.setEngineerID(jsonArray.getJSONObject(i).getString("engineer_id"));
            sw.setLockedToUser(jsonArray.getJSONObject(i).getString("locked_to"));
            sw.setRemarks(jsonArray.getJSONObject(i).getString("remarks"));
            sw.setBeforeRemarks(jsonArray.getJSONObject(i).getString("remarks_before"));
            sw.setAfterRemarks(jsonArray.getJSONObject(i).getString("remarks_after"));
            sw.setEquipmentType(jsonArray.getJSONObject(i).getString("equipment_type"));
            sw.setModelOrSerial(jsonArray.getJSONObject(i).getString("serial_no"));
            sw.setStartDate(jsonArray.getJSONObject(i).getString("start_date").split(" ")[0]);
            sw.setEndDate(jsonArray.getJSONObject(i).getString("end_date").split(" ")[0]);
            sw.setStatus(jsonArray.getJSONObject(i).getString("status"));
            sw.setSignatureName(jsonArray.getJSONObject(i).getString("signature_name"));
            sw.setStartDateTask(jsonArray.getJSONObject(i).getString("start_date_task"));
            sw.setEndDateTask(jsonArray.getJSONObject(i).getString("end_date_task"));
            sw.setDateCreated(jsonArray.getJSONObject(i).getString("date_created"));
            sw.setCustomerName(jsonArray.getJSONObject(i).getString("customer_name"));
            sw.setJobSite(jsonArray.getJSONObject(i).getString("job_site"));
            sw.setFax(jsonArray.getJSONObject(i).getString("fax"));
            sw.setTelephone(jsonArray.getJSONObject(i).getString("telephone"));
            sw.setPhone(jsonArray.getJSONObject(i).getString("phone_no"));
            sw.setEngineerName(jsonArray.getJSONObject(i).getString("engineer_name"));
            // sw.setSignaturePath(jsonArray.getJSONObject(i).getString("signature_file_path"));

            Log.d(TAG, sw.toString());

            translationList.add(sw);

            i++;
        } while (jsonLen > i);
        return translationList;
    }

    /**
     * NOT USED 02/06/2017
     * 24 Columns + 2
     * Index 0 to 25
     * Parse JSON String from ':'
     * SJ Details for Service JOb Begin Task Details
     * @param parsedServiceJob
     * @return
     */
    public ArrayList<ServiceJobWrapper> serviceJobList(List<String> parsedServiceJob) {
        ArrayList<ServiceJobWrapper> translationList = new ArrayList<>();

        this.mResult = parsedServiceJob.size() >= 0;

        for (String credential : parsedServiceJob) {
            ServiceJobWrapper sw = new ServiceJobWrapper();
            String[] pieces = credential.split(LIST_DELIM);

            sw.setID(Integer.parseInt(pieces[0]));
            sw.setServiceNumber(pieces[1]);
            sw.setCustomerID(pieces[2]);
            sw.setServiceID(pieces[3]);
            sw.setEngineerID(pieces[4]);
            // sw.setPriceID(pieces[5]);
            sw.setComplaintsOrSymptoms(pieces[5]);
            sw.setRemarks(pieces[6]);
            sw.setBeforeRemarks(pieces[7]);
            sw.setAfterRemarks(pieces[8]);
            sw.setEquipmentType(pieces[9]);
            sw.setModelOrSerial(pieces[10]);
            sw.setStartDate(pieces[11]);
            sw.setEndDate(pieces[12]);
            sw.setStatus(pieces[13]);
            //sw.setContractServicing(pieces[14]);
            /*sw.setWarrantyServicing(pieces[14]);
            sw.setCharges(pieces[15]);
            sw.setContractRepair(pieces[16]);
            sw.setWarrantyRepair(pieces[17]);
            sw.setOthers(pieces[18]);*/
            sw.setTypeOfService(pieces[14]);
            sw.setSignatureName(pieces[15]);
//            sw.setStartDateTask(pieces[16]);
//            sw.setEndDateTask(pieces[17]);
            sw.setCustomerName(pieces[18]);
            sw.setJobSite(pieces[19]);
            sw.setFax(pieces[20]);
            sw.setTelephone(pieces[21]);
            sw.setEngineerName(pieces[22]);
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