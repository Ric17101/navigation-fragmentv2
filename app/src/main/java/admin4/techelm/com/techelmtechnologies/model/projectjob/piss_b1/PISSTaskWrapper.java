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

package admin4.techelm.com.techelmtechnologies.model.projectjob.piss_b1;

import android.os.Parcel;
import android.os.Parcelable;


public class PISSTaskWrapper implements Parcelable {

    public static final Creator<PISSTaskWrapper> CREATOR = new Creator<PISSTaskWrapper>() {
        @Override
        public PISSTaskWrapper createFromParcel(Parcel in) {
            return new PISSTaskWrapper(in);
        }

        @Override
        public PISSTaskWrapper[] newArray(int size) {
            return new PISSTaskWrapper[size];
        }
    };

    //private variables 7
    private int _id;
    private String _projectjob_piss_id;
    private String _serial_no;
    private String _description;
    private String _conformance;
    private String _comments;
    private String _status;

    // Empty constructor
    public PISSTaskWrapper() { }

    protected PISSTaskWrapper(Parcel in) {
        _id = in.readInt();
        _projectjob_piss_id = in.readString();
        _serial_no = in.readString();
        _description = in.readString();
        _conformance = in.readString();
        _comments = in.readString();
        _status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(_projectjob_piss_id);
        dest.writeString(_serial_no);
        dest.writeString(_description);
        dest.writeString(_conformance);
        dest.writeString(_comments);
        dest.writeString(_status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "\nID : " + this._id +
                "\n_projectjob_piss_id : " + this._projectjob_piss_id +
                "\n_serial_no : " + this._serial_no +
                "\n_description : " + this._description +
                "\n_conformance : " + this._conformance +
                "\n_comments : " + this._comments +
                "\n_status : " + this._status
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

    public String getProjectPISSID() {
        return this._projectjob_piss_id;
    }
    public void setProjectPISSID(String structure) {
        this._projectjob_piss_id = structure;
    }

    public String getSerialNo() {
        return this._serial_no;
    }
    public void setSerialNo(String val) {
        this._serial_no = val;
    }

    public String getDescription() {
        return this._description;
    }
    public void setDescription(String data) {
        this._description = data;
    }

    public String getConformance() {
        return this._conformance;
    }
    public void setConformance(String num) {
        this._conformance = num;
    }

    public String getComments() {
        return this._comments;
    }
    public void setComments(String val) {
        this._comments = val;
    }

    public String getStatus() {
        return this._status;
    }
    public void setStatus(String val) {
        this._status = val;
    }

}