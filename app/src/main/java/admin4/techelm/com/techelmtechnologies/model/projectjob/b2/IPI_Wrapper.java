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

package admin4.techelm.com.techelmtechnologies.model.projectjob.b2;

import android.os.Parcel;
import android.os.Parcelable;


public class IPI_Wrapper implements Parcelable {

    public static final Creator<IPI_Wrapper> CREATOR = new Creator<IPI_Wrapper>() {
        @Override
        public IPI_Wrapper createFromParcel(Parcel in) {
            return new IPI_Wrapper(in);
        }

        @Override
        public IPI_Wrapper[] newArray(int size) {
            return new IPI_Wrapper[size];
        }
    };

    // private variables 4
    private int _id;
    private int _projectjob_id;
    private String _sub_contractor;
    private String _date_inspected;

    // Empty constructor
    public IPI_Wrapper() { }

    protected IPI_Wrapper(Parcel in) {
        _id = in.readInt();
        _projectjob_id = in.readInt();
        _sub_contractor = in.readString();
        _date_inspected = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeInt(_projectjob_id);
        dest.writeString(_sub_contractor);
        dest.writeString(_date_inspected);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "\nID : " + this._id +
                "\n_projectjob_id : " + this._projectjob_id +
                "\n_sub_contractor : " + this._sub_contractor +
                "\n_date_inspected : " + this._date_inspected
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

    public int getProjectJobID() {
        return this._projectjob_id;
    }
    public void setProjectJobID(int structure) { this._projectjob_id = structure; }

    public String getSubContractor() {
        return this._sub_contractor;
    }
    public void setSubContractor(String val) {
        this._sub_contractor = val;
    }

    public String getDateInspected() {
        return this._date_inspected;
    }
    public void setDateInspected(String data) {
        this._date_inspected = data;
    }
}