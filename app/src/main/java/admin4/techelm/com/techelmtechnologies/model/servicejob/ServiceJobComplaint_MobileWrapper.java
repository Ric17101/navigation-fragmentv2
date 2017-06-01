package admin4.techelm.com.techelmtechnologies.model.servicejob;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Action Service Repair
 */
public class ServiceJobComplaint_MobileWrapper implements Parcelable {
    private int mId;                    //id in database
    private int mServiceJobID;          // servicejob_category_id in database
    private int mSJCategoryID;          // servicejob_category_id in database
    private int mSJASRID;               // Action Service Repair ID in database
    private int mSJComplaintFaultID;    // servicejob_category_id in database
    // private String mAction;             // Complaint
    private String mDateCreated;        // Date created

    public ServiceJobComplaint_MobileWrapper() { }

    public ServiceJobComplaint_MobileWrapper(Parcel in) {
        mId = in.readInt();
        mServiceJobID = in.readInt();
        mSJCategoryID = in.readInt();
        mSJASRID = in.readInt();
        mSJComplaintFaultID = in.readInt();
        //mAction = in.readString();
        mDateCreated = in.readString();
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
        dest.writeInt(mSJASRID);
        dest.writeInt(mSJComplaintFaultID);
        //dest.writeString(mAction);
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

    public int getServiceJobID() {
        return mServiceJobID;
    }
    public void setServiceJobID(int val) { mServiceJobID = val; }

    public int getSJCategoryId() {
        return mSJCategoryID;
    }
    public void setSJCategoryId(int val) { mSJCategoryID = val; }

    public int getSJASRID() {
        return mSJASRID;
    }
    public void setSJASRID(int val) { mSJASRID = val; }

    public int getSJComplaintFaultID() {
        return mSJComplaintFaultID;
    }
    public void setSJComplaintFaultID(int val) { mSJComplaintFaultID = val; }

    public String getDateCreated() {
        return mDateCreated;
    }
    public void setDateCreated(String val) {
        mDateCreated = val;
    }

    /*public String getAction() { return mAction; }
    public void setAction(String val) {
        mAction = val;
    }*/

    public String toString() {
        return "ID : " + this.mId +
                "\nmServiceJobID: " + this.mServiceJobID +
                "\nmSJCategoryID: " + this.mSJCategoryID +
                "\nmSJASRID: " + this.mSJASRID +
                "\nmSJComplaintFaultID: " + this.mSJComplaintFaultID +
                "\nmDateCreated : " + this.mDateCreated
                //"\nmAction: " + this.mAction
                ;
    }
}