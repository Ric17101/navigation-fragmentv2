package admin4.techelm.com.techelmtechnologies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceJobRecordingWrapper implements Parcelable {
    private int mId;            //id in database
    private String mServiceID;            // servicejob id in database
    private String mName;       // file name
    private String mFilePath;   //file path
    private int mLength;        // length of recording in seconds
    private long mTime;         // date/time of the recording

    public ServiceJobRecordingWrapper() { }

    public ServiceJobRecordingWrapper(Parcel in) {
        mId = in.readInt();
        mServiceID = in.readString();
        mName = in.readString();
        mFilePath = in.readString();
        mLength = in.readInt();
        mTime = in.readLong();
    }

    public static final Creator<ServiceJobRecordingWrapper> CREATOR = new Creator<ServiceJobRecordingWrapper>() {
        public ServiceJobRecordingWrapper createFromParcel(Parcel in) {
            return new ServiceJobRecordingWrapper(in);
        }

        public ServiceJobRecordingWrapper[] newArray(int size) {
            return new ServiceJobRecordingWrapper[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mServiceID);
        dest.writeInt(mLength);
        dest.writeLong(mTime);
        dest.writeString(mFilePath);
        dest.writeString(mName);
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

    public int getLength() {
        return mLength;
    }
    public void setLength(int length) {
        mLength = length;
    }

    public String getRecordingName() { return mName; }
    public void setName(String name) {
        mName = name;
    }

    public long getTime() {
        return mTime;
    }
    public void setTime(long time) {
        mTime = time;
    }

    public String toString() {
        return "ID : " + this.mId +
                "\nService ID : " + this.mServiceID +
                "\nFile Path : " + this.mFilePath +
                "\nlenght : " + this.mLength +
                "\nFile Name" + this.mName +
                "\nTime : " + this.mTime;
    }
}