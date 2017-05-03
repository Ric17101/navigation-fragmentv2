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


public class IPI_PWTaskWrapper implements Parcelable {

    public static final Creator<IPI_PWTaskWrapper> CREATOR = new Creator<IPI_PWTaskWrapper>() {
        @Override
        public IPI_PWTaskWrapper createFromParcel(Parcel in) {
            return new IPI_PWTaskWrapper(in);
        }

        @Override
        public IPI_PWTaskWrapper[] newArray(int size) {
            return new IPI_PWTaskWrapper[size];
        }
    };

    //private variables 8
    private int _id;
    private String _projectjob_ipi_pw_id;
    private String _serial_no;
    private String _description;
    private String _status;
    private String _non_conformance;
    private String _corrective_actions;
    private String _target_completion_date;

    // Empty constructor
    public IPI_PWTaskWrapper() { }

    protected IPI_PWTaskWrapper(Parcel in) {
        _id = in.readInt();
        _projectjob_ipi_pw_id = in.readString();
        _serial_no = in.readString();
        _description = in.readString();
        _status = in.readString();
        _non_conformance = in.readString();
        _corrective_actions = in.readString();
        _target_completion_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(_projectjob_ipi_pw_id);
        dest.writeString(_serial_no);
        dest.writeString(_description);
        dest.writeString(_status);
        dest.writeString(_non_conformance);
        dest.writeString(_corrective_actions);
        dest.writeString(_target_completion_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "\nID : " + this._id +
                "\n_projectjob_ipi_pw_id : " + this._projectjob_ipi_pw_id +
                "\n_serial_no : " + this._serial_no +
                "\n_description : " + this._description +
                "\n_status : " + this._status +
                "\n_non_conformance : " + this._non_conformance +
                "\n_corrective_actions : " + this._corrective_actions +
                "\n_target_completion_date : " + this._target_completion_date
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

    public String getProjectJobIPI_PWID() {
        return this._projectjob_ipi_pw_id;
    }
    public void setProjectJobIPI_PWID(String structure) {
        this._projectjob_ipi_pw_id = structure;
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

    public String getStatus() {
        return this._status;
    }
    public void setStatus(String num) {
        this._status = num;
    }

    public String getNonConformance() {
        return this._non_conformance;
    }
    public void setNonConformance(String val) {
        this._non_conformance = val;
    }

    public String getCorrectiveActions() {
        return this._corrective_actions;
    }
    public void setCorrectiveActions(String val) {
        this._corrective_actions = val;
    }

    public String getTargetCompletionDate() {
        return this._target_completion_date;
    }
    public void setTargetCompletionDate(String data) {
        this._target_completion_date = data;
    }

}