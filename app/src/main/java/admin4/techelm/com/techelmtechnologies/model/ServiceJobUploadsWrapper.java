package admin4.techelm.com.techelmtechnologies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceJobUploadsWrapper implements Parcelable {
    private int mId;                //id in database
    private String mServiceID;      // servicejob id in database
    private String mSignatureName;  // file name
    private String mFilePath;       //file path

    public ServiceJobUploadsWrapper() { }

    public ServiceJobUploadsWrapper(Parcel in) {
        mId = in.readInt();
        mServiceID = in.readString();
        mSignatureName = in.readString();
        mFilePath = in.readString();
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
        dest.writeString(mServiceID);
        dest.writeString(mFilePath);
        dest.writeString(mSignatureName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return mId;
    }
    public void setId(int id) {
        mId = id;
    }

    public String getServiceId() {
        return mServiceID;
    }
    public void setServiceId(String serviceId) {
        mServiceID = serviceId;
    }

    public String getFilePath() {
        return mFilePath;
    }
    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public String getSignatureName() { return mSignatureName; }
    public void setSignatureName(String name) {
        mSignatureName = name;
    }

    public String toString() {
        return "ID : " + this.mId +
                "\nService ID : " + this.mServiceID +
                "\nFile Path : " + this.mFilePath +
                "\nFile Name" + this.mSignatureName;
    }
}