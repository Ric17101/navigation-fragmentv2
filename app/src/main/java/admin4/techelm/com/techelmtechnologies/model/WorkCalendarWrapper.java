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

public class WorkCalendarWrapper implements Parcelable {

    public static final Creator<WorkCalendarWrapper> CREATOR = new Creator<WorkCalendarWrapper>() {
        @Override
        public WorkCalendarWrapper createFromParcel(Parcel in) {
            return new WorkCalendarWrapper(in);
        }

        @Override
        public WorkCalendarWrapper[] newArray(int size) {
            return new WorkCalendarWrapper[size];
        }
    };
    //private variables 9
    public int _id;
    public String _service_no;
    public int _customer_id;
    public int _service_id;
    public String _car_code;
    public int _engineer_id;
    public String _start_date;
    public String _end_date;
    public int _status;

    // Empty constructor
    public WorkCalendarWrapper() {
    }

    protected WorkCalendarWrapper(Parcel in) {
        _id = in.readInt();
        _service_no = in.readString();
        _customer_id = in.readInt();
        _service_id = in.readInt();
        _car_code = in.readString();
        _engineer_id = in.readInt();
        _start_date = in.readString();
        _end_date = in.readString();
        _status = in.readInt();
    }

    // constructor
    public WorkCalendarWrapper(int id,
                               String name,
                               String description,
                               String created_at,
                               int created_by,
                               String updated_at,
                               int updated_by) {
        this._id = id;
        this._service_no = name;
        this._car_code = description;
        this._start_date = created_at;
        this._engineer_id = created_by;
        this._end_date = updated_at;
        this._status = updated_by;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(_service_no);
        dest.writeInt(_customer_id);
        dest.writeInt(_service_id);
        dest.writeString(_car_code);
        dest.writeInt(_engineer_id);
        dest.writeString(_start_date);
        dest.writeString(_end_date);
        dest.writeInt(_status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "ID : " + this._id +
                "\nService No : " + this._service_no +
                "\nCar Code : " + this._car_code +
                "\nStart Date : " + this._start_date +
                "\nEngineer ID" + this._engineer_id +
                "\nENd Date : " + this._end_date +
                "\nStatus : " + this._status;
    }

    /**
     * GET AND SET METHOD
     */
    public int getID() {
        return this._id;
    }
    public void setID(int id) {
        this._id = id;
    }

    public String getServiceNo() { return this._service_no; }
    public void settServiceNo(String val) {
        this._service_no = val;
    }

    public int getCustomerID() {
        return this._customer_id;
    }
    public void setCustomerID(int val) {
        this._customer_id = val;
    }

    public int getServiceID() {
        return this._service_id;
    }
    public void setServiceID(int val) {
        this._service_id = val;
    }

    public String getCarCode() {
        return this._car_code;
    }
    public void setCarCode(String val) {
        this._car_code = val;
    }

    public int getEngineerID() {
        return this._engineer_id;
    }
    public void setEngineerID(int val) {
        this._engineer_id = val;
    }

    public String getStartDatet() {
        return this._start_date;
    }
    public void setStartDate(String val) {
        this._start_date = val;
    }

    public String getEndDatet() {
        return this._end_date;
    }
    public void setEndDate(String val) {
        this._end_date = val;
    }

    public int getStatus() { return this._status; }
    public void setStatus(int val) { this._status = val; }

}