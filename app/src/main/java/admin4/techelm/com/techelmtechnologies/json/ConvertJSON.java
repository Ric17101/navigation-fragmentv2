package admin4.techelm.com.techelmtechnologies.json;

/**
 * Created by admin 4 on 22/02/2017.
 * Used to convert JSON string in to WRAPPERS
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;

public class ConvertJSON {

    private static final String TAG = ConvertJSON.class.getSimpleName();
    private static final String SJ_LIST_DELIM = ":-:";
    private boolean mResult = false;

    public ConvertJSON() {

    }

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
            sw.setTypeOfService(jsonArray.getJSONObject(i).getString("price_id"));
            sw.setComplaintsOrSymptoms(jsonArray.getJSONObject(i).getString("complaint"));
            sw.setActionsOrRemarks(jsonArray.getJSONObject(i).getString("remarks"));
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
            sw.setSignatureName(jsonArray.getJSONObject(i).getString("signature_name"));
            sw.setCustomerName(jsonArray.getJSONObject(i).getString("fullname"));
            sw.setJobSite(jsonArray.getJSONObject(i).getString("job_site"));
            sw.setFax(jsonArray.getJSONObject(i).getString("fax"));
            sw.setTelephone(jsonArray.getJSONObject(i).getString("phone_no"));
            // Log.d(TAG, sw.toString());
            translationList.add(sw);
            i++;
        } while (jsonLen > i);
        return translationList;
    }

    /**
     * 24 Columns
     * Parse JSON String from ':'
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
            sw.setEquipmentType(pieces[8]);
            sw.setModelOrSerial(pieces[9]);
            sw.setStartDate(pieces[10]);
            sw.setEndDate(pieces[11]);
            sw.setStatus(pieces[12]);
            sw.setContractServicing(pieces[13]);
            sw.setWarrantyServicing(pieces[14]);
            sw.setCharges(pieces[15]);
            sw.setContractRepair(pieces[16]);
            sw.setWarrantyRepair(pieces[17]);
            sw.setOthers(pieces[18]);
            sw.setSignatureName(pieces[19]);
            // sw.setContractServicing(pieces[20]);
            sw.setCustomerName(pieces[20]);
            sw.setJobSite(pieces[21]);
            sw.setFax(pieces[22]);
            sw.setTelephone(pieces[23]);
            // Log.d(TAG, sw.toString());
            translationList.add(sw);
        }
        return translationList;
    }

}