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
 *//*


package admin4.techelm.com.techelmtechnologies.model;

import android.os.Parcel;
import android.os.Parcelable;

// TODO: replace the public variables
public class CustomerWrapper implements Parcelable {

    public static final Creator<CustomerWrapper> CREATOR = new Creator<CustomerWrapper>() {
        @Override
        public CustomerWrapper createFromParcel(Parcel in) {
            return new CustomerWrapper(in);
        }

        @Override
        public CustomerWrapper[] newArray(int size) {
            return new CustomerWrapper[size];
        }
    };

    //private variables 14
    private int _id;
    private String _fullname;
    private String _person_in_charge;
    private String _job_site;
    private String _address;
    private String _email;
    private String _contact_no;
    private String _phone_no;
    private String _race;
    private String _status;
    private String _created_at;
    private String _created_by;
    private String _updated_at;
    private String _updated_by;

    // Empty constructor
    public CustomerWrapper() {
    }

    protected CustomerWrapper(Parcel in) {
        _id = in.readInt();
        _created_at = in.readString();
        _created_by = in.readString();
        _fullname = in.readString();
        _job_site = in.readString();
        _person_in_charge = in.readString();
        _address = in.readString();
        _status = in.readString();
        _race = in.readString();
        _contact_no = in.readString();
        _phone_no = in.readString();
    }

    // constructor
    public CustomerWrapper(int id, String day, String date, String service_num, String customer) {
        this._id = id;
        this._created_at = date;
        this._created_by = date;
        this._fullname = service_num;
        this._person_in_charge = customer;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(_created_at);
        dest.writeString(_created_by);
        dest.writeString(_fullname);
        dest.writeString(_job_site);
        dest.writeString(_person_in_charge);
        dest.writeString(_address);
        dest.writeString(_status);
        dest.writeString(_race);
        dest.writeString(_contact_no);
        dest.writeString(_phone_no);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "ID : " + this._id +
                "\nDay : " + this._updated_at +
                "\nStart Date : " + this._created_at +
                "\nEnd Date : " + this._created_by +
                "\nService Number : " + this._fullname +
                "\nService ID : " + this._job_site +
                "\nCustomer" + this._person_in_charge +
                "\nEngineer : " + this._address +
                "\nStatus : " + this._status +
                "\nType of Service : " + this._email +
                "\nTelephone : " + this._phone_no +
                "\nFax : " + this._email +
                "\nEquipment Type : " + this._race +
                "\nCompliants or Symptoms : " + this._contact_no +
                "\nActions or Remarks : " + this._phone_no;
    }

    */
/** GET AND SET METHOD *//*



    private int _id;
    private String _fullname;
    private String _person_in_charge;
    private String _job_site;
    private String _address;
    private String _email;
    private String _contact_no;
    private String _phone_no;
    private String _race;
    private String _status;
    private String _created_at;
    private String _created_by;
    private String _updated_at;
    private String _updated_by;

    public int getID() {
        return this._id;
    }
    public void setID(int id) {
        this._id = id;
    }

    public String getFullName() {
        return this._fullname;
    }
    public void setFullName(String structure) {
        this._fullname = structure;
    }

    public String getPersonInCharge() { return this._job_site; }
    public void setPersonInCharge(String val) {
        this._job_site = val;
    }

    public String getCustomerID() { return this._person_in_charge; }
    public void setCustomerID(String data) { this._person_in_charge = data; }

    public String getEngineer() { return this._address; }
    public void setEngineerID(String data) { this._address = data; }

    public String getPriceID() { return this._email; }
    public void setPriceID(String data) { this._email = data; }

    public String getComplaintsOrSymptoms() { return this._contact_no; }
    public void setComplaintsOrSymptoms(String data) { this._contact_no = data; }

    public String getRemarks() { return this._phone_no; }
    public void setRemarks(String data) { this._phone_no = data; }

    public String getEquipmentType() { return this._race; }
    public void setEquipmentType(String data) { this._race = data; }

//    public String getModelOrSerial() { return this._sta; }
//    public void setModelOrSerial(String data) { this._sta = data; }

    public String getStartDate() {
        return this._created_at;
    }
    public void setStartDate(String val) { this._created_at = val; }

    public String getEndDate() {
        return this._created_by;
    }
    public void setEndDate(String val) { this._created_by = val; }

    public String getStatus() { return this._status; }
    public void setStatus(String data) { this._status = data; }

    public String getContractServicing() {
        return this._updated_at;
    }
    public void setContractServicing(String num) {
        this._updated_at = num;
    }

    public String getWarrantyServicing() {
        return this._updated_by;
    }
    public void setWarrantyServicing(String val) {
        this._updated_by = val;
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

    */
/** MY COLUMNS *//*


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
}*/
