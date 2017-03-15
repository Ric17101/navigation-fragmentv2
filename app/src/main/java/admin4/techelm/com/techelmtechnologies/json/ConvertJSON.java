package admin4.techelm.com.techelmtechnologies.json;

/**
 * Created by admin 4 on 22/02/2017.
 * Used to convert JSON string in to WRAPPERS
 */

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;

public class ConvertJSON {

    private static final String TAG = ConvertJSON.class.getSimpleName();
    private boolean mResult = true;

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
        int jsonLen = json.getJSONArray("servicelist").length();
        if (jsonLen == 0) {
            this.mResult = false; // No result
            return null;
        }
        int i = 0;
        do {
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
            Log.d(TAG, sw.toString());
            translationList.add(sw);
            i++;
        } while (jsonLen > i);
        return translationList;
    }

}