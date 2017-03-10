/*
 * Copyright (c) 2016 Richard C.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package admin4.techelm.com.techelmtechnologies.model;

import android.os.Parcel;
import android.os.Parcelable;

// TODO: replace the public variables
public class ServiceJobWrapper implements Parcelable {

    public static final Creator<ServiceJobWrapper> CREATOR = new Creator<ServiceJobWrapper>() {
        @Override
        public ServiceJobWrapper createFromParcel(Parcel in) {
            return new ServiceJobWrapper(in);
        }

        @Override
        public ServiceJobWrapper[] newArray(int size) {
            return new ServiceJobWrapper[size];
        }
    };

    //private variables 24
    private int _id;
    private String _service_no;
    private String _customer_id;
    private String _service_id;
    private String _engineer_id;
    private String _price_id;
    private String _complaints_or_symptoms;
    private String _actions_or_remarks;
    private String _equipment_type;
    private String _model_or_serial_num;
    private String _start_date;
    private String _end_date;
    private String _status;
    // Type of service
    private String _contract_servicing;
    private String _warranty_servicing;
    private String _charges;
    private String _contract_repair;
    private String _warranty_repair;
    private String _others;
    private String _signature_name;

    /** MY COLUMNS */
    private String _telephone;
    private String _fax;
    private String _race;
    private String _type_of_service;
    private String _signature_file_path;

    // Empty constructor
    public ServiceJobWrapper() {
    }

    protected ServiceJobWrapper(Parcel in) {
        _id = in.readInt();
        _start_date = in.readString();
        _end_date = in.readString();
        _service_no = in.readString();
        _service_id = in.readString();
        _customer_id = in.readString();
        _engineer_id = in.readString();
        _status = in.readString();
        _equipment_type = in.readString();
        _model_or_serial_num = in.readString();
        _complaints_or_symptoms = in.readString();
        _actions_or_remarks = in.readString();
    }

    // constructor
    public ServiceJobWrapper(int id, String day, String date, String service_num, String customer) {
        this._id = id;
        this._start_date = date;
        this._end_date = date;
        this._service_no = service_num;
        this._customer_id = customer;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(_start_date);
        dest.writeString(_end_date);
        dest.writeString(_service_no);
        dest.writeString(_service_id);
        dest.writeString(_customer_id);
        dest.writeString(_engineer_id);
        dest.writeString(_status);
        dest.writeString(_equipment_type);
        dest.writeString(_model_or_serial_num);
        dest.writeString(_complaints_or_symptoms);
        dest.writeString(_actions_or_remarks);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "ID : " + this._id +
                "\nDay : " + this._contract_servicing +
                "\nStart Date : " + this._start_date +
                "\nEnd Date : " + this._end_date +
                "\nService Number : " + this._service_no +
                "\nService ID : " + this._service_id +
                "\nCustomer" + this._customer_id +
                "\nEngineer : " + this._engineer_id +
                "\nStatus : " + this._status +
                "\nType of Service : " + this._price_id +
                "\nTelephone : " + this._others +
                "\nFax : " + this._signature_name +
                "\nEquipment Type : " + this._equipment_type +
                "\nModel or Serial Number : " + this._model_or_serial_num +
                "\nCompliants or Symptoms : " + this._complaints_or_symptoms +
                "\nActions or Remarks : " + this._actions_or_remarks;
    }

    /** GET AND SET METHOD */

    public int getID() {
        return this._id;
    }
    public void setID(int id) {
        this._id = id;
    }

    public String getServiceNumber() {
        return this._service_no;
    }
    public void setServiceNumber(String structure) {
        this._service_no = structure;
    }

    public String getServiceID() { return this._service_id; }
    public void setServiceID(String val) {
        this._service_id = val;
    }

    public String getCustomerID() { return this._customer_id; }
    public void setCustomerID(String data) { this._customer_id = data; }

    public String getEngineer() { return this._engineer_id; }
    public void setEngineerID(String data) { this._engineer_id = data; }

    public String getPriceID() { return this._price_id; }
    public void setPriceID(String data) { this._price_id = data; }

    public String getComplaintsOrSymptoms() { return this._complaints_or_symptoms; }
    public void setComplaintsOrSymptoms(String data) { this._complaints_or_symptoms = data; }

    public String getActionsOrRemarks() { return this._actions_or_remarks; }
    public void setActionsOrRemarks(String data) { this._actions_or_remarks = data; }

    public String getEquipmentType() { return this._equipment_type; }
    public void setEquipmentType(String data) { this._equipment_type = data; }

    public String getModelOrSerial() { return this._model_or_serial_num; }
    public void setModelOrSerial(String data) { this._model_or_serial_num = data; }

    public String getStartDate() {
        return this._start_date;
    }
    public void setStartDate(String val) { this._start_date = val; }

    public String getEndDate() {
        return this._end_date;
    }
    public void setEndDate(String val) { this._end_date = val; }

    public String getStatus() { return this._status; }
    public void setStatus(String data) { this._status = data; }

    public String getContractServicing() {
        return this._contract_servicing;
    }
    public void setContractServicing(String num) {
        this._contract_servicing = num;
    }

    public String getWarrantyServicing() {
        return this._warranty_servicing;
    }
    public void setWarrantyServicing(String val) {
        this._warranty_servicing = val;
    }

    public String getCharges() {
        return this._charges;
    }
    public void setCharges(String val) {
        this._charges = val;
    }

    public String getContractRepair() {
        return this._contract_repair;
    }
    public void setContractRepair(String val) {
        this._contract_repair = val;
    }

    public String getWarrantyRepair() {
        return this._warranty_repair;
    }
    public void setWarrantyRepair(String val) {
        this._warranty_repair = val;
    }

    public String getOthers() {
        return this._others;
    }
    public void setOthers(String val) {
        this._others = val;
    }

    public String getSignatureName() {
        return this._signature_name;
    }
    public void setSignatureName(String val) {
        this._signature_name = val;
    }

    /** MY COLUMNS */

    public String getTelephone() {
        return this._telephone;
    }
    public void setTelephone(String val) {
        this._telephone = val;
    }

    public String getFax() {
        return this._fax;
    }
    public void setFax(String val) {
        this._fax = val;
    }

    public String getRace() { return this._race; }
    public void setRace(String val) {this._race = val; }

    public String getTypeOfService() {
        return this._type_of_service;
    }
    public void setTypeOfService(String val) {
        this._type_of_service = val;
    }

    public String getSignaturePath() {
        return this._signature_file_path;
    }
    public void setSignaturePath(String val) {
        this._signature_file_path = val;
    }
}