package admin4.techelm.com.techelmtechnologies.model.servicejob;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Action Service Repair
 */
public class ServiceJobComplaintWrapper implements Parcelable {

    private int mId;                    // id in database
    private int mServiceJobID;          // servicejob_id in database
    private int mSJ_CMCFID;             // cm_cf_id in database
    private String mSJ_Complaint;       // compllaint in database
    private int mSJComplaintFaultID;    // complaint_fault_id in database
    private int mSJComplaintMobileID;   // complaint_mobile_id in database
    private int mSJCategoryID;          // category_id in database
    private String mCategory;           // category in database
    private int mActionID;              // action_id in database
    private String mAction;             // action

    public ServiceJobComplaintWrapper() { }

    public ServiceJobComplaintWrapper(Parcel in) {
        mId = in.readInt();
        mServiceJobID = in.readInt();
        mSJ_CMCFID = in.readInt();
        mSJ_Complaint = in.readString();
        mSJComplaintFaultID = in.readInt();
        mSJComplaintMobileID = in.readInt();
        mSJCategoryID = in.readInt();
        mCategory = in.readString();
        mActionID = in.readInt();
        mAction = in.readString();
    }

    public static final Creator<ServiceJobComplaintWrapper> CREATOR = new Creator<ServiceJobComplaintWrapper>() {
        public ServiceJobComplaintWrapper createFromParcel(Parcel in) {
            return new ServiceJobComplaintWrapper(in);
        }

        public ServiceJobComplaintWrapper[] newArray(int size) {
            return new ServiceJobComplaintWrapper[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mServiceJobID);
        dest.writeInt(mSJ_CMCFID);
        dest.writeString(mSJ_Complaint);
        dest.writeInt(mSJComplaintFaultID);
        dest.writeInt(mSJComplaintMobileID);
        dest.writeInt(mSJCategoryID);
        dest.writeString(mCategory);
        dest.writeInt(mActionID);
        dest.writeString(mAction);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getID() {
        return mId;
    }
    public void setID(int id) {
        mId = id;
    }

    public int getServiceJobID() {
        return mServiceJobID;
    }
    public void setServiceJobID(int val) { mServiceJobID = val; }

    public int getSJ_CM_CF_ID() {
        return mSJ_CMCFID;
    }
    public void setSJ_CM_CF_ID(int val) { mSJ_CMCFID = val; }

    public String getComplaint() {
        return mSJ_Complaint;
    }
    public void setComplaint(String val) { mSJ_Complaint = val; }

    public int getComplaintFaultID() {
        return mSJComplaintFaultID;
    }
    public void setComplaintFaultID(int val) { mSJComplaintFaultID = val; }

    public int getComplaintMobileID() {
        return mSJComplaintMobileID;
    }
    public void setComplaintMobileID(int val) { mSJComplaintMobileID = val; }

    public int getCategoryID() {
        return mSJCategoryID;
    }
    public void setCategoryID(int val) { mSJCategoryID = val; }

    public String getCategory() { return mCategory; }
    public void setCategory(String val) { mCategory = val; }

    public int getActionID() { return mActionID; }
    public void setActionID(int val) { mActionID = val; }

    public String getAction() {
        return mAction;
    }
    public void setAction(String val) { mAction = val; }

    public String toString() {
        return "\nID : " + this.mId +
                "\nmServiceJobID: " + this.mServiceJobID +
                "\nmSJ_CMCFID: " + this.mSJ_CMCFID +
                "\nmSJ_Complaint: " + this.mSJ_Complaint +
                "\nmSJComplaintFaultID: " + this.mSJComplaintFaultID +
                "\nmSJComplaintMobileID : " + this.mSJComplaintMobileID +
                "\nmSJCategoryID: " + this.mSJCategoryID +
                "\nmCategory : " + this.mCategory +
                "\nmActionID : " + this.mActionID +
                "\nmAction: " + this.mAction
            ;
    }
}