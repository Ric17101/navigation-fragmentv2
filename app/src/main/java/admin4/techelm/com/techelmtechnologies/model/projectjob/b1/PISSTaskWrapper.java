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

    //private variables 9
    private int _id;
    private int _projectjob_id;
    private String _serial_no;
    private String _description;
    private String _conformance;
    private String _comments;
    private String _status;
    private String _drawing_before;
    private String _drawing_after;

    // Empty constructor
    public PISSTaskWrapper() { }

    protected PISSTaskWrapper(Parcel in) {
        _id = in.readInt();
        _projectjob_id = in.readInt();
        _serial_no = in.readString();
        _description = in.readString();
        _conformance = in.readString();
        _comments = in.readString();
        _status = in.readString();
        _drawing_before = in.readString();
        _drawing_after = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeInt(_projectjob_id);
        dest.writeString(_serial_no);
        dest.writeString(_description);
        dest.writeString(_conformance);
        dest.writeString(_comments);
        dest.writeString(_status);
        dest.writeString(_drawing_before);
        dest.writeString(_drawing_after);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "\nID : " + this._id +
                "\n_projectjob_id : " + this._projectjob_id +
                "\n_serial_no : " + this._serial_no +
                "\n_description : " + this._description +
                "\n_conformance : " + this._conformance +
                "\n_comments : " + this._comments +
                "\n_status : " + this._status +
                "\n_drawing_after" + _drawing_before +
                "\n_drawing_after" + _drawing_after
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

    public int getProjectID() { return this._projectjob_id; }
    public void setProjectID(int val) { this._projectjob_id = val; }

    public String getSerialNo() { return this._serial_no; }
    public void setSerialNo(String val) { this._serial_no = val; }

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
    public void setStatus(String val) { this._status = val; }

    public String getDrawingBefore() {
        return this._drawing_before;
    }
    public void setDrawingBefore(String val) {
        this._drawing_before = val;
    }

    public String getDrawingAfter() {
        return this._drawing_after;
    }
    public void setDrawingAfter(String val) { this._drawing_after = val; }

}