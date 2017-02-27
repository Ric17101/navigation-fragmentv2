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
public class UserGroupWrapper implements Parcelable {

    public static final Creator<UserGroupWrapper> CREATOR = new Creator<UserGroupWrapper>() {
        @Override
        public UserGroupWrapper createFromParcel(Parcel in) {
            return new UserGroupWrapper(in);
        }

        @Override
        public UserGroupWrapper[] newArray(int size) {
            return new UserGroupWrapper[size];
        }
    };
    //private variables 7
    public int _id;
    public String _name;
    public String _description;
    public String _created_at;
    public int _created_by;
    public String _updated_at;
    public int _updated_by;

    // Empty constructor
    public UserGroupWrapper() {
    }

    protected UserGroupWrapper(Parcel in) {
        _id = in.readInt();
        _name = in.readString();
        _description = in.readString();
        _created_at = in.readString();
        _created_by = in.readInt();
        _updated_at = in.readString();
        _updated_by = in.readInt();
    }

    // constructor
    public UserGroupWrapper(int id,
                            String name,
                            String description,
                            String created_at,
                            int created_by,
                            String updated_at,
                            int updated_by) {
        this._id = id;
        this._name = name;
        this._description = description;
        this._created_at = created_at;
        this._created_by = created_by;
        this._updated_at = updated_at;
        this._updated_by = updated_by;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(_name);
        dest.writeString(_description);
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
                "\nName : " + this._name +
                "\nDescription : " + this._description +
                "\nCreated At : " + this._created_at +
                "\nCreated By" + this._created_by +
                "\nUpdated at : " + this._updated_at +
                "\nUpdated By : " + this._updated_by;
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

    public String getName() { return this._name; }
    public void setName(String num) {
        this._name = num;
    }

    public String getDescription() {
        return this._description;
    }
    public void setDescription(String arabic) {
        this._description = arabic;
    }

    public String getCreatedAt() {
        return this._created_at;
    }
    public void setCreatedAt(String structure) {
        this._created_at = structure;
    }

    public int getCreatedBy() { return this._created_by; }
    public void setCreatedBy(int data) {
        this._created_by = data;
    }

    public String getUpdatedAt() { return this._updated_at; }
    public void setUpdatedAt(String data) { this._updated_at = data; }

    public int getUpdatedBy() { return this._updated_by; }
    public void setUpdatedBy(int data) { this._updated_by = data; }

}