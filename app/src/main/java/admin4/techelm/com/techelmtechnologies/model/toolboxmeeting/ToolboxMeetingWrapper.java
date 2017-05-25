package admin4.techelm.com.techelmtechnologies.model.toolboxmeeting;

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

import android.os.Parcel;
import android.os.Parcelable;


public class ToolboxMeetingWrapper implements Parcelable {

    public static final Creator<admin4.techelm.com.techelmtechnologies.model.toolboxmeeting.ToolboxMeetingWrapper> CREATOR = new Creator<admin4.techelm.com.techelmtechnologies.model.toolboxmeeting.ToolboxMeetingWrapper>() {
        @Override
        public admin4.techelm.com.techelmtechnologies.model.toolboxmeeting.ToolboxMeetingWrapper createFromParcel(Parcel in) {
            return new admin4.techelm.com.techelmtechnologies.model.toolboxmeeting.ToolboxMeetingWrapper(in);
        }

        @Override
        public admin4.techelm.com.techelmtechnologies.model.toolboxmeeting.ToolboxMeetingWrapper[] newArray(int size) {
            return new admin4.techelm.com.techelmtechnologies.model.toolboxmeeting.ToolboxMeetingWrapper[size];
        }
    };

    //private variables 12 + 2
    // TODO: Add CPCode Here....
    private int _projectjob_id; // ProjectJob ID from the WEB API, should exist only once on local DB
    private String _proj_ref;
    private String _project_site;
    private String _taget_completion_date;
    private String _first_inspector;
    private String _second_inspector;
    private String _third_inspector;
    private int _customer_id;
    private String _customer_name;
    private String _start_date;
    private String _end_date;
    private int _status_flag;

    // My Custom Attributes from the WEB
    private String _fax;
    private String _telephone;

    // Empty constructor
    public ToolboxMeetingWrapper() { }

    protected ToolboxMeetingWrapper(Parcel in) {
        _projectjob_id = in.readInt();
        _proj_ref = in.readString();
        _project_site = in.readString();
        _taget_completion_date = in.readString();
        _first_inspector = in.readString();
        _second_inspector = in.readString();
        _third_inspector = in.readString();
        _customer_id = in.readInt();
        _customer_name = in.readString();
        _start_date = in.readString();
        _end_date = in.readString();
        _status_flag = in.readInt();
        _fax = in.readString();
        _telephone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_projectjob_id);
        dest.writeString(_proj_ref);
        dest.writeString(_project_site);
        dest.writeString(_taget_completion_date);
        dest.writeString(_first_inspector);
        dest.writeString(_second_inspector);
        dest.writeString(_third_inspector);
        dest.writeInt(_customer_id);
        dest.writeString(_customer_name);
        dest.writeString(_start_date);
        dest.writeString(_end_date);
        dest.writeInt(_status_flag);
        dest.writeString(_fax);
        dest.writeString(_telephone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "\nID : " + this._projectjob_id +
                "\nProject Reference : " + this._proj_ref +
                "\nCustomer ID : " + this._customer_id +
                "\nCustomer Name : " + this._customer_name +
                "\nTarget Completion Date : " + this._taget_completion_date +
                "\nStart Date : " + this._start_date +
                "\nEnd Date : " + this._end_date +
                "\nStatus : " + this._status_flag +
                "\n1st Inspector : " + this._first_inspector +
                "\n2nd Inspector : " + this._second_inspector +
                "\n3rd Inspector : " + this._third_inspector +
                "\nProject Site : " + this._project_site +
                "\nFax : " + this._fax +
                "\nTelephone : " + this._telephone
                ;
    }

    /**
     * GET AND SET METHOD
     */

    public int getID() { return this._projectjob_id; }
    public void setID(int id) {
        this._projectjob_id = id;
    }

    public String getProjectRef() { return this._proj_ref; }
    public void setProjectRef(String structure) {
        this._proj_ref = structure;
    }

    public String getProjectSite() {
        return this._project_site;
    }
    public void setProjectSite(String val) {
        this._project_site = val;
    }

    public String getTargetCompletionDate() {
        return this._taget_completion_date;
    }
    public void setTargetCompletionDate(String data) {
        this._taget_completion_date = data;
    }

    public String getFirstInspector() {
        return this._first_inspector;
    }
    public void setFirstInspector(String num) {
        this._first_inspector = num;
    }

    public String getSecondInspector() {
        return this._second_inspector;
    }
    public void setSecondInspector(String val) {
        this._second_inspector = val;
    }

    public String getThirdInspector() {
        return this._third_inspector;
    }
    public void setThirdInspector(String val) {
        this._third_inspector = val;
    }

    public int getCustomerID() {
        return this._customer_id;
    }
    public void setCustomerID(int data) {
        this._customer_id = data;
    }

    public String getCustomerName() {
        return this._customer_name;
    }
    public void setCustomerName(String val) {
        this._customer_name = val;
    }

    public String getStartDate() {
        return this._start_date;
    }
    public void setStartDate(String val) {
        this._start_date = val;
    }

    public String getEndDate() {
        return this._end_date;
    }
    public void setEndDate(String val) {
        this._end_date = val;
    }

    public int getStatus() {
        return this._status_flag;
    }
    public void setStatus(int data) {
        this._status_flag = data;
    }

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
}
