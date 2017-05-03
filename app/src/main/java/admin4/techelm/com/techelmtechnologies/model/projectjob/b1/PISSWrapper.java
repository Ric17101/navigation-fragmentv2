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

package admin4.techelm.com.techelmtechnologies.model.projectjob.b1;

import android.os.Parcel;
import android.os.Parcelable;


public class PISSWrapper implements Parcelable {

    public static final Creator<PISSWrapper> CREATOR = new Creator<PISSWrapper>() {
        @Override
        public PISSWrapper createFromParcel(Parcel in) {
            return new PISSWrapper(in);
        }

        @Override
        public PISSWrapper[] newArray(int size) {
            return new PISSWrapper[size];
        }
    };

    //private variables 13
    private int _id;
    private String _projectjob_id;
    private String _car_park_code;
    private String _property_officer;
    private String _tc_lew;
    private String _property_officer_telNo;
    private String _property_officer_mobileNo;
    private String _property_officer_branch;
    private String _tc_lew_telNo;
    private String _tc_lew_mobileNo;
    private String _tc_lew_email;
    private String _remarks;
    private String _date_site_walk;

    // Empty constructor
    public PISSWrapper() { }

    protected PISSWrapper(Parcel in) {
        _id = in.readInt();
        _projectjob_id = in.readString();
        _car_park_code = in.readString();
        _property_officer = in.readString();
        _tc_lew = in.readString();
        _property_officer_telNo = in.readString();
        _property_officer_mobileNo = in.readString();
        _property_officer_branch = in.readString();
        _tc_lew_telNo = in.readString();
        _tc_lew_mobileNo = in.readString();
        _tc_lew_email = in.readString();
        _remarks = in.readString();
        _date_site_walk = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(_projectjob_id);
        dest.writeString(_car_park_code);
        dest.writeString(_property_officer);
        dest.writeString(_tc_lew);
        dest.writeString(_property_officer_telNo);
        dest.writeString(_property_officer_mobileNo);
        dest.writeString(_property_officer_branch);
        dest.writeString(_tc_lew_telNo);
        dest.writeString(_tc_lew_mobileNo);
        dest.writeString(_tc_lew_email);
        dest.writeString(_remarks);
        dest.writeString(_date_site_walk);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "\nID : " + this._id +
                "\nProject Reference : " + this._projectjob_id +
                "\nCar Park Code : " + this._car_park_code +
                "\nProperty Officer : " + this._property_officer +
                "\nTC Lew : " + this._tc_lew +
                "\n_property_officer_telNo : " + this._property_officer_telNo +
                "\n_property_officer_mobileNo : " + this._property_officer_mobileNo +
                "\n_property_officer_branch : " + this._property_officer_branch +
                "\n_tc_lew_telNo : " + this._tc_lew_telNo +
                "\n_tc_lew_mobileNo : " + this._tc_lew_mobileNo +
                "\n_tc_lew_email : " + this._tc_lew_email +
                "\n_remarks : " + this._remarks +
                "\n_date_site_walk : " + this._date_site_walk
                ;
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

    public String getProjectJobID() {
        return this._projectjob_id;
    }
    public void setProjectJobID(String structure) {
        this._projectjob_id = structure;
    }

    public String getCarParkCode() {
        return this._car_park_code;
    }
    public void setCarParkCode(String val) {
        this._car_park_code = val;
    }

    public String getPropertyOfficer() {
        return this._property_officer;
    }
    public void setPropertyOfficer(String data) {
        this._property_officer = data;
    }

    public String getTCLew() {
        return this._tc_lew;
    }
    public void setTCLew(String num) {
        this._tc_lew = num;
    }

    public String getPropertyOfficerTelNo() {
        return this._property_officer_telNo;
    }
    public void setPropertyOfficerTelNo(String val) {
        this._property_officer_telNo = val;
    }

    public String getPropertyOfficerMobileNo() {
        return this._property_officer_mobileNo;
    }
    public void setPropertyOfficerMobileNo(String val) {
        this._property_officer_mobileNo = val;
    }

    public String getPropertyOfficerBranch() {
        return this._property_officer_branch;
    }
    public void setPropertyOfficerBranch(String data) {
        this._property_officer_branch = data;
    }

    public String getTCLewTelNo() {
        return this._tc_lew_telNo;
    }
    public void setTCLewTelNo(String val) {
        this._tc_lew_telNo = val;
    }

    public String getTCLewMobileNo() {
        return this._tc_lew_mobileNo;
    }
    public void setTCLewMobileNo(String val) {
        this._tc_lew_mobileNo = val;
    }

    public String getTCLewEmail() {
        return this._tc_lew_email;
    }
    public void setTCLewEmail(String val) {
        this._tc_lew_email = val;
    }

    public String getRemarks() {
        return this._remarks;
    }
    public void setRemarks(String data) {
        this._remarks = data;
    }

    public String getDateSiteWalk() {
        return this._date_site_walk;
    }
    public void setDateSiteWalk(String data) {
        this._date_site_walk = data;
    }
}