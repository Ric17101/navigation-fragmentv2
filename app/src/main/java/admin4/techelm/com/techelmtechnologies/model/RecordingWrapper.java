package admin4.techelm.com.techelmtechnologies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RecordingWrapper implements Parcelable {
    private String mName;       // file name
    private String mFilePath;   //file path
    private int mId;            //id in database
    private int mLength;        // length of recording in seconds
    private long mTime;         // date/time of the recording

    public RecordingWrapper() { }

    public RecordingWrapper(Parcel in) {
        mName = in.readString();
        mFilePath = in.readString();
        mId = in.readInt();
        mLength = in.readInt();
        mTime = in.readLong();
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

    public int getId() {
        return mId;
    }
    public void setId(int id) {
        mId = id;
    }

    public String getName() { return mName; }
    public void setName(String name) {
        mName = name;
    }

    public long getTime() {
        return mTime;
    }
    public void setTime(long time) {
        mTime = time;
    }

    public static final Creator<RecordingWrapper> CREATOR = new Creator<RecordingWrapper>() {
        public RecordingWrapper createFromParcel(Parcel in) {
            return new RecordingWrapper(in);
        }

        public RecordingWrapper[] newArray(int size) {
            return new RecordingWrapper[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mLength);
        dest.writeLong(mTime);
        dest.writeString(mFilePath);
        dest.writeString(mName);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}