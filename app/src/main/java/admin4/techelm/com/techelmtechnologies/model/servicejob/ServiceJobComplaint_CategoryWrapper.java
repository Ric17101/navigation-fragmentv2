package admin4.techelm.com.techelmtechnologies.model.servicejob;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceJobComplaint_CategoryWrapper implements Parcelable {
    private int mId;                //id in database
    private int mSJCategoryID;      // servicejob_category_id in database
    private String mCategory;       // Cateogry
    private String mDateCreated;    // date created

    public ServiceJobComplaint_CategoryWrapper() { }

    public ServiceJobComplaint_CategoryWrapper(Parcel in) {
        mId = in.readInt();
        mSJCategoryID = in.readInt();
        mCategory = in.readString();
        mDateCreated = in.readString();
    }

    public static final Creator<ServiceJobComplaint_CategoryWrapper> CREATOR = new Creator<ServiceJobComplaint_CategoryWrapper>() {
        public ServiceJobComplaint_CategoryWrapper createFromParcel(Parcel in) {
            return new ServiceJobComplaint_CategoryWrapper(in);
        }

        public ServiceJobComplaint_CategoryWrapper[] newArray(int size) {
            return new ServiceJobComplaint_CategoryWrapper[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mSJCategoryID);
        dest.writeString(mCategory);
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

    public String getCategory() { return mCategory; }
    public void setCategory(String val) {
        mCategory = val;
    }

    public String toString() {
        return "ID : " + this.mId +
                "\nmSJCategoryID: " + this.mSJCategoryID +
                "\nmCategory: " + this.mCategory +
                "\nmDateCreated : " + this.mDateCreated;
    }
}