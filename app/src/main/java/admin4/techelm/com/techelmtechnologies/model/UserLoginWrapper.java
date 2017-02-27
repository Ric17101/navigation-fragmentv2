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
public class UserLoginWrapper implements Parcelable {

    public static final Creator<UserLoginWrapper> CREATOR = new Creator<UserLoginWrapper>() {
        @Override
        public UserLoginWrapper createFromParcel(Parcel in) {
            return new UserLoginWrapper(in);
        }

        @Override
        public UserLoginWrapper[] newArray(int size) {
            return new UserLoginWrapper[size];
        }
    };
    //private variables 12
    public int _id;
    public String _user_group_id;
    public String _role;
    public String _fullname;
    /** USER and PASSWORD DETAILS */
    public String _username;
    public String _password;
    public String _auth_key;
    public String _password_hash;
    public String _password_reset_token;
    public String _photo;
    public int _status;
    public int _deleted;

    // Empty constructor
    public UserLoginWrapper() {
    }

    protected UserLoginWrapper(Parcel in) {
        _id = in.readInt();
        _user_group_id = in.readString();
        _role = in.readString();
        _fullname = in.readString();
        _username = in.readString();
        _password = in.readString();
        _auth_key = in.readString();
        _password_hash = in.readString();
        _password_reset_token = in.readString();
        _photo = in.readString();
        _status = in.readInt();
        _deleted = in.readInt();
    }

    // constructor
    public UserLoginWrapper(int id, String day, String date, String service_num, String customer,
                            String password,
                            String auth_key,
                            String password_hash,
                            String password_reset_token,
                            String photo,
                            int status,
                            int deleted) {
        this._id = id;
        this._user_group_id = day;
        this._role = date;
        this._fullname = service_num;
        this._username = customer;
        this._password = password;
        this._auth_key = auth_key;
        /** MOTE DETAILS */	        /** MOTE DETAILS */
        this._password_hash = password_hash;
        this._password_reset_token = password_reset_token;
        this._photo = photo;
        this._status = status;
        this._deleted = deleted;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(_user_group_id);
        dest.writeString(_role);
        dest.writeString(_fullname);
        dest.writeString(_username);
        dest.writeString(_password);
        dest.writeString(_auth_key);
        /** MOTE DETAILS */
        dest.writeString(_password_hash);
        dest.writeString(_password_reset_token);
        dest.writeString(_photo);
        dest.writeInt(_status);
        dest.writeInt(_deleted);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "ID : " + this._id +
                "\nUser Group ID : " + this._user_group_id +
                "\nRole : " + this._role +
                "\nFullname : " + this._fullname +
                "\nUsername" + this._username +
                "\nPassword : " + this._password +
                "\nAuth Key : " + this._auth_key +
                "\nPassword Hash : " + this._password_hash +
                "\nPassword Reset token : " + this._password_reset_token +
                "\nPhoto : " + this._photo +
                "\nStatus : " + this._status +
                "\nDeleted : " + this._deleted;
    }

    /** GET AND SET METHOD */

    public int getID() {
        return this._id;
    }
    public void setID(int id) {
        this._id = id;
    }

    public String getUserGroupId() { return this._user_group_id; }
    public void setUserGroupId(String num) {
        this._user_group_id = num;
    }

    public String getRole() {
        return this._role;
    }
    public void setRole(String arabic) {
        this._role = arabic;
    }

    public String getFullname() {
        return this._fullname;
    }
    public void setFullname(String structure) {
        this._fullname = structure;
    }

    /** USER and PASSWORD DETAILS */
    public String getUsername() { return this._username; }
    public void setUsername(String data) {
        this._username = data;
    }

    public String getPassword() { return this._password; }
    public void setPassword(String data) { this._password = data; }

    public String getAuthKey() { return this._auth_key; }
    public void setAuthKey(String data) { this._auth_key = data; }

    public String getPasswordHash() { return this._password_hash; }
    public void setPasswordHash(String data) { this._password_hash = data; }

    public String getPasswordResetToken() { return this._password_reset_token; }
    public void setPasswordResetToken(String data) { this._password_reset_token = data; }

    public String getPhoto() { return this._photo; }
    public void setPhoto(String data) { this._photo = data; }

    public int getStatus() { return this._status; }
    public void setStatus(int data) { this._status = data; }

    public int getDeleted() { return this._deleted; }
    public void setDeleted(int data ) { this._deleted = data; }

}