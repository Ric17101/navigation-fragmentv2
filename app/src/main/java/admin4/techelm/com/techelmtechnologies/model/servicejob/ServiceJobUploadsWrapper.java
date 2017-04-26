package admin4.techelm.com.techelmtechnologies.model.servicejob;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceJobUploadsWrapper implements Parcelable {
    private int mId;                //id in database
    private int mServiceID;      // servicejob id in database
    private String mUploadName;  // file name
    private String mFilePath;       //file path
    private String mTaken;       //file taken BEFORE || AFTER

    public ServiceJobUploadsWrapper() { }

    public ServiceJobUploadsWrapper(Parcel in) {
        mId = in.readInt();
        mServiceID = in.readInt();
        mUploadName = in.readString();
        mFilePath = in.readString();
        mTaken = in.readString();
    }

    public static final Creator<ServiceJobUploadsWrapper> CREATOR = new Creator<ServiceJobUploadsWrapper>() {
        public ServiceJobUploadsWrapper createFromParcel(Parcel in) {
            return new ServiceJobUploadsWrapper(in);
        }

        public ServiceJobUploadsWrapper[] newArray(int size) {
            return new ServiceJobUploadsWrapper[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mServiceID);
        dest.writeString(mUploadName);
        dest.writeString(mFilePath);
        dest.writeString(mTaken);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getID() {
        return mId;
    }
    public void setId(int id) {
        mId = id;
    }

    public int getServiceId() {
        return mServiceID;
    }
    public void setServiceId(int serviceId) { mServiceID = serviceId; }

    public String getFilePath() {
        return mFilePath;
    }
    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public String getUploadName() { return mUploadName; }
    public void setUploadName(String name) {
        mUploadName = name;
    }

    public String getTaken() { return mTaken; }
    public void setTaken(String name) {
        mTaken = name;
    }

    public String toString() {
        return "ID : " + this.mId +
                "\nService ID : " + this.mServiceID +
                "\nFile Path : " + this.mFilePath +
                "\nTaken : " + this.mTaken +
                "\nFile Name : " + this.mUploadName;
    }
}