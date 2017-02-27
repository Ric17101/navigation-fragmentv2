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

    //private variables
    public int _id;
    public String _day;
    public String _date;
    public String _service_num;
    public String _customer;
    public String _engineer;
    public String _status;
    /** MOTE DETAILS */
    public String _type_of_service;
    public String _telephone;
    public String _fax;
    public String _equipment_type;
    public String _model_or_serial_num;
    public String _complaints_or_symptoms;
    public String _actions_or_remarks;

    // Empty constructor
    public ServiceJobWrapper() {
    }

    protected ServiceJobWrapper(Parcel in) {
        _id = in.readInt();
        _day = in.readString();
        _date = in.readString();
        _service_num = in.readString();
        _customer = in.readString();
        _engineer = in.readString();
        _status = in.readString();
        _type_of_service = in.readString();
        _telephone = in.readString();
        _fax = in.readString();
        _equipment_type = in.readString();
        _model_or_serial_num = in.readString();
        _complaints_or_symptoms = in.readString();
        _actions_or_remarks = in.readString();
    }

    // constructor
    public ServiceJobWrapper(int id, String day, String date, String service_num, String customer) {
        this._id = id;
        this._day = day;
        this._date = date;
        this._service_num = service_num;
        this._customer = customer;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(_day);
        dest.writeString(_date);
        dest.writeString(_service_num);
        dest.writeString(_customer);
        dest.writeString(_engineer);
        dest.writeString(_status);
        /** MOTE DETAILS */
        dest.writeString(_type_of_service);
        dest.writeString(_telephone);
        dest.writeString(_fax);
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
                "\nDay : " + this._day +
                "\nDate : " + this._date +
                "\nService Number : " + this._service_num +
                "\nCustomer" + this._customer +
                "\nEngineer : " + this._engineer +
                "\nStatus : " + this._status +
                "\nType of Service : " + this._type_of_service +
                "\nTelephone : " + this._telephone +
                "\nFax : " + this._fax +
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

    public String getDay() {
        return this._day;
    }
    public void setDay(String num) {
        this._day = num;
    }

    public String getDate() {
        return this._date;
    }
    public void setDate(String arabic) {
        this._date = arabic;
    }

    public String getServiceNumber() {
        return this._service_num;
    }
    public void setServiceNumber(String structure) {
        this._service_num = structure;
    }

    public String getCustomer() { return this._customer; }
    public void setCustomer(String data) {
        this._customer = data;
    }

    public String getEngineer() { return this._engineer; }
    public void setEngineer(String data) { this._engineer = data; }

    public String getStatus() { return this._status; }
    public void setStatus(String data) { this._status = data; }

    public String getTypeOfService() { return this._type_of_service; }
    public void setTypeOfService(String data) { this._type_of_service = data; }
    /** MOTE DETAILS */

    public String getTelephone() { return this._telephone; }
    public void setTelephone(String data) { this._telephone = data; }

    public String getFax() { return this._fax; }
    public void setFax(String data) { this._fax = data; }

    public String getEquipmentType() { return this._equipment_type; }
    public void setEquipmentType(String data) { this._equipment_type = data; }

    public String getModelOrSerial() { return this._model_or_serial_num; }
    public void setModelOrSerial(String data) { this._model_or_serial_num = data; }


    public String getComplaintsOrSymptoms() { return this._complaints_or_symptoms; }
    public void setComplaintsOrSymptoms(String data) { this._complaints_or_symptoms = data; }

    public String getActionsOrRemarks() { return this._actions_or_remarks; }
    public void setActionsOrRemarks(String data) { this._actions_or_remarks = data; }

}