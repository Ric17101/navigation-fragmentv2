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

package admin4.techelm.com.techelmtechnologies.model.servicejob;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceJobWrapper implements Parcelable {

    //private variables 27
    private int _id; // ServiceJob ID from the WEB API, should exist only once on local DB
    private String _service_no;
    private String _customer_id;
    private String _service_id;
    private String _type_of_service;
    private String _engineer_id;
    private String _locked_to_user;
    private String _complaints_or_symptoms;
    private String _remarks;
    private String _remarks_before;
    private String _remarks_after;
    private String _equipment_type;
    private String _serial_no;
    private String _start_date;
    private String _end_date;
    private String _status;
    private String _signature_name;
    private String _start_date_task;
    private String _end_date_task;
    private String _date_created;
    private String _customer_name; // full_name
    private String _job_site;
    private String _fax;
    private String _telephone;
    private String _phone_no;
    private String _engineer_name;

    /** MY COLUMNS */
    private String _signature_file_path;

    // Empty constructor
    public ServiceJobWrapper() { }

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

    protected ServiceJobWrapper(Parcel in) {
        _id = in.readInt();
        _service_no = in.readString();
        _customer_id = in.readString();
        _type_of_service = in.readString();
        _service_id = in.readString();
        _engineer_id = in.readString();
        _locked_to_user = in.readString();
        _complaints_or_symptoms = in.readString();
        _remarks = in.readString();
        _remarks_before = in.readString();
        _remarks_after = in.readString();
        _equipment_type = in.readString();
        _serial_no = in.readString();
        _start_date = in.readString();
        _end_date = in.readString();
        _status = in.readString();
        _signature_name = in.readString();
        _start_date_task = in.readString();
        _end_date_task = in.readString();
        _date_created = in.readString();
        _customer_name = in.readString();
        _job_site = in.readString();
        _fax = in.readString();
        _telephone = in.readString();
        _phone_no = in.readString();
        _engineer_name = in.readString();
        _signature_file_path = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(_service_no);
        dest.writeString(_customer_id);
        dest.writeString(_type_of_service);
        dest.writeString(_service_id);
        dest.writeString(_engineer_id);
        dest.writeString(_locked_to_user);
        dest.writeString(_complaints_or_symptoms);
        dest.writeString(_remarks);
        dest.writeString(_remarks_before);
        dest.writeString(_remarks_after);
        dest.writeString(_equipment_type);
        dest.writeString(_serial_no);
        dest.writeString(_start_date);
        dest.writeString(_end_date);
        dest.writeString(_status);
        dest.writeString(_signature_name);
        dest.writeString(_start_date_task);
        dest.writeString(_end_date_task);
        dest.writeString(_date_created);
        dest.writeString(_customer_name);
        dest.writeString(_job_site);
        dest.writeString(_fax);
        dest.writeString(_telephone);
        dest.writeString(_phone_no);
        dest.writeString(_engineer_name);
        dest.writeString(_signature_file_path);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "\nID : " + this._id +
                "\nService Number : " + this._service_no +
                "\nCustomer ID : " + this._customer_id +
                "\nService ID : " + this._service_id +
                "\nEngineer : " + this._engineer_id +
                "\nEngineer Name : " + this._engineer_name +
                "\nPrice ID : " + this._locked_to_user +
                "\nComplaints or Symptoms : " + this._complaints_or_symptoms +
                "\nActions or Remarks : " + this._remarks +
                "\nBefore Remarks : " + this._remarks_before +
                "\nAfter Remarks : " + this._remarks_after +
                "\nEquipment Type : " + this._equipment_type +
                "\nModel or Serial Number : " + this._serial_no +
                "\nStart Date : " + this._start_date +
                "\nEnd Date : " + this._end_date+
                "\nDate Created : " + this._date_created+
                "\nStatus : " + this._status +
                "\nType of service : " + this._type_of_service +
                "\nSignature File Path : " + this._signature_file_path +
                "\nSignature File Name : " + this._signature_name +
                "\nCustomer Name : " + this._customer_name +
                "\nJob Site : " + this._job_site +
                "\nFax : " + this._fax +
                "\nTelephone : " + this._telephone +
                "\nType of Service : " + this._type_of_service
        ;
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

    public String getCustomerID() { return this._customer_id; }
    public void setCustomerID(String data) { this._customer_id = data; }

    public String getServiceID() { return this._service_id; }
    public void setServiceID(String val) {
        this._service_id = val;
    }

    public String getTypeOfService() {
        return this._type_of_service;
    }
    public void setTypeOfService(String val) {
        this._type_of_service = val;
    }

    public String getEngineer() { return this._engineer_id; }
    public void setEngineerID(String data) { this._engineer_id = data; }

    public String getLockedToUser() { return this._locked_to_user; }
    public void setLockedToUser(String data) { this._locked_to_user = data; }

    public String getComplaintsOrSymptoms() { return this._complaints_or_symptoms; }
    public void setComplaintsOrSymptoms(String data) { this._complaints_or_symptoms = data; }

    public String getRemarks() { return this._remarks; }
    public void setRemarks(String data) { this._remarks = data; }

    public String getBeforeRemarks() { return this._remarks_before; }
    public void setBeforeRemarks(String data) { this._remarks_before = data; }

    public String getAfterRemarks() { return this._remarks_after; }
    public void setAfterRemarks(String data) { this._remarks_after = data; }

    public String getEquipmentType() { return this._equipment_type; }
    public void setEquipmentType(String data) { this._equipment_type = data; }

    public String getModelOrSerial() { return this._serial_no; }
    public void setModelOrSerial(String data) { this._serial_no = data; }

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

    public String getSignatureName() {
        return this._signature_name;
    }
    public void setSignatureName(String val) {
        this._signature_name = val;
    }

    public String getStartDateTask() {
        return this._start_date_task;
    }
    public void setStartDateTask(String val) {
        this._start_date_task = val;
    }

    public String getEndDateTask() {
        return this._end_date_task;
    }
    public void setEndDateTask(String val) {
        this._end_date_task = val;
    }

    public String getDateCreated() {
        return this._date_created;
    }
    public void setDateCreated(String val) {
        this._date_created = val;
    }

    /** MY COLUMNS */
    public String getCustomerName() { return this._customer_name; }
    public void setCustomerName(String val) { this._customer_name = val; }

    public String getJobSite() {
        return this._job_site;
    }
    public void setJobSite(String val) { this._job_site = val; }

    public String getFax() {
        return this._fax;
    }
    public void setFax(String val) { this._fax = val; }

    public String getTelephone() {
        return this._telephone;
    }
    public void setTelephone(String val) {
        this._telephone = val;
    }

    public String getPhone() {
        return this._phone_no;
    }
    public void setPhone(String val) {
        this._phone_no = val;
    }

    public String getEngineerName() {
        return this._engineer_name;
    }
    public void setEngineerName(String val) {
        this._engineer_name = val;
    }

    public String getSignaturePath() {
        return this._signature_file_path;
    }
    public void setSignaturePath(String val) {
        this._signature_file_path = val;
    }

}