package admin4.techelm.com.techelmtechnologies.model.servicejob;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Action Service Repair
 */
public class ServiceJobComplaint_ASRWrapper implements Parcelable {
    private int mId;                //id in database
    private int mSJCategoryID;      // servicejob_category_id in database
    private String mAction;         // Complaint
    private String mDateCreated;    // date created

    public ServiceJobComplaint_ASRWrapper() { }

    public ServiceJobComplaint_ASRWrapper(Parcel in) {
        mId = in.readInt();
        mSJCategoryID = in.readInt();
        mAction = in.readString();
        mDateCreated = in.readString();
    }

    public static final Creator<ServiceJobComplaint_ASRWrapper> CREATOR = new Creator<ServiceJobComplaint_ASRWrapper>() {
        public ServiceJobComplaint_ASRWrapper createFromParcel(Parcel in) {
            return new ServiceJobComplaint_ASRWrapper(in);
        }

        public ServiceJobComplaint_ASRWrapper[] newArray(int size) {
            return new ServiceJobComplaint_ASRWrapper[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mSJCategoryID);
        dest.writeString(mAction);
        dest.writeString(mDateCreated);
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

    public int getSJCategoryId() {
        return mSJCategoryID;
    }
    public void setSJCategoryId(int val) { mSJCategoryID = val; }

    public String getDateCreated() {
        return mDateCreated;
    }
    public void setDateCreated(String val) {
        mDateCreated = val;
    }

    public String getAction() { return mAction; }
    public void setAction(String val) {
        mAction = val;
    }

    public String toString() {
        return "\nID : " + this.mId +
                "\nmSJCategoryID: " + this.mSJCategoryID +
                "\nmDateCreated : " + this.mDateCreated +
                "\nmAction: " + this.mAction;
    }
}