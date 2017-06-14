package admin4.techelm.com.techelmtechnologies.model.servicejob;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Action Service Repair
 */
public class ServiceJobComplaint_MobileWrapper implements Parcelable {
    private int mId;                    // id in database
    private int mServiceJobID;          // servicejob_id in database
    private int mSJCategoryID;          // servicejob_category_id in database
    private String mDateCreated;        // date_created
    private String mCategory;           // category in database

    public ServiceJobComplaint_MobileWrapper() { }

    public ServiceJobComplaint_MobileWrapper(Parcel in) {
        mId = in.readInt();
        mServiceJobID = in.readInt();
        mSJCategoryID = in.readInt();
        mDateCreated = in.readString();
        mCategory = in.readString();
    }

    public static final Creator<ServiceJobComplaint_MobileWrapper> CREATOR = new Creator<ServiceJobComplaint_MobileWrapper>() {
        public ServiceJobComplaint_MobileWrapper createFromParcel(Parcel in) {
            return new ServiceJobComplaint_MobileWrapper(in);
        }

        public ServiceJobComplaint_MobileWrapper[] newArray(int size) {
            return new ServiceJobComplaint_MobileWrapper[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mServiceJobID);
        dest.writeInt(mSJCategoryID);
        dest.writeString(mDateCreated);
        dest.writeString(mCategory);
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

    public int getServiceJobID() {
        return mServiceJobID;
    }
    public void setServiceJobID(int val) { mServiceJobID = val; }

    public int getSJCategoryId() {
        return mSJCategoryID;
    }
    public void setSJCategoryId(int val) { mSJCategoryID = val; }

    public String getDateCreated() { return mDateCreated; }
    public void setDateCreated(String val) { mDateCreated = val; }

    public String getSJCategory() {
        return mCategory;
    }
    public void setSJCategory(String val) { mCategory = val; }

    public String toString() {
        return "\nID : " + this.mId +
                "\nmServiceJobID: " + this.mServiceJobID +
                "\nmSJCategoryID: " + this.mSJCategoryID +
                "\nmDateCreated : " + this.mDateCreated +
                "\nmCategory: " + this.mCategory
            ;
    }
}