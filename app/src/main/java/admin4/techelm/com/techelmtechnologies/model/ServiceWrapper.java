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

public class ServiceWrapper implements Parcelable {

    public static final Creator<ServiceWrapper> CREATOR = new Creator<ServiceWrapper>() {
        @Override
        public ServiceWrapper createFromParcel(Parcel in) {
            return new ServiceWrapper(in);
        }

        @Override
        public ServiceWrapper[] newArray(int size) {
            return new ServiceWrapper[size];
        }
    };

    // private variables 10
    public int _id;
    public int _category_id;
    public String _service_name;
    public String _description;
    public String _default_unit_price;
    public int _status;
    public String _created_at;
    public int _created_by;
    public String _updated_at;
    public int _updated_by;

    // Empty constructor
    public ServiceWrapper() {
    }

    protected ServiceWrapper(Parcel in) {
        _id = in.readInt();
        _category_id = in.readInt();
        _service_name = in.readString();
        _description = in.readString();
        _default_unit_price = in.readString();
        _status = in.readInt();
        _created_at = in.readString();
        _created_by = in.readInt();
        _updated_at = in.readString();
        _updated_by = in.readInt();
    }

    // constructor
    public ServiceWrapper(int id, int day, String date, String service_num, String customer) {
        this._id = id;
        this._category_id = day;
        this._service_name = date;
        this._description = service_num;
        this._default_unit_price = customer;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeInt(_category_id);
        dest.writeString(_service_name);
        dest.writeString(_description);
        dest.writeString(_default_unit_price);
        dest.writeInt(_status);
        dest.writeString(_created_at);
        dest.writeInt(_created_by);
        dest.writeString(_updated_at);
        dest.writeInt(_updated_by);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "ID : " + this._id +
                "\nCategory ID : " + this._category_id +
                "\nService Name : " + this._service_name +
                "\nDescription : " + this._description +
                "\nDfault Unit Price" + this._default_unit_price +
                "\nStatus : " + this._status +
                "\nCreated At : " + this._created_at +
                "\nCreated By : " + this._created_by +
                "\nUpdated At : " + this._updated_at +
                "\nUpdated By : " + this._updated_by;
    }

    /** GET AND SET METHOD */

    public int getID() {
        return this._id;
    }
    public void setID(int val) {
        this._id = val;
    }

    public int getCategoryID() {
        return this._category_id;
    }
    public void setCategoryID(int val) {
        this._category_id = val;
    }

    public String getServiceNanem() {
        return this._service_name;
    }
    public void setServiceNanem(String val) {
        this._service_name = val;
    }

    public String getDescription() {
        return this._description;
    }
    public void setDescription(String val) {
        this._description = val;
    }

    public String getDefaultUnitPrice() { return this._default_unit_price; }
    public void setDefaultUnitPrice(String val) {
        this._default_unit_price = val;
    }

    public int getStatus() { return this._status; }
    public void setStatus(int val) { this._status = val; }

    public String getCreatedAt() { return this._created_at; }
    public void setCreatedAt(String val) { this._created_at = val; }

    public int getCreatedBy() { return this._created_by; }
    public void setCreatedBy(int val) { this._created_by = val; }

    public String getUpdatedAt() { return this._updated_at; }
    public void setUpdatedAt(String val) { this._updated_at = val; }

    public int getUpdatedBy() { return this._updated_by; }
    public void setUpdatedBy(int val) { this._updated_by = val; }

}