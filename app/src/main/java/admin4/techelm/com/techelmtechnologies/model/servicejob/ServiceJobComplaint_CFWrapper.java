package admin4.techelm.com.techelmtechnologies.model.servicejob;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceJobComplaint_CFWrapper implements Parcelable {
    private int mId;                        //id in database
    private int mSJCategoryID;              // servicejob_category_id in database
    private int mSJ_CMCFID;                 // servicejob_cm_cf_id in database
    private String mComplaint;              // Complaint
    private String mDateCreated;            // date created
    private int mSJComplaintFaultID;        // servicejob_complaint_fault_id in database
    private int mSJComplaintMobileID;       // servicejob_complaint_mobile_id in database

    public ServiceJobComplaint_CFWrapper() { }

    public ServiceJobComplaint_CFWrapper(Parcel in) {
        mId = in.readInt();
        mSJCategoryID = in.readInt();
        mSJ_CMCFID = in.readInt();
        mComplaint = in.readString();
        mDateCreated = in.readString();
        mSJComplaintFaultID = in.readInt();
        mSJComplaintMobileID = in.readInt();
    }

    public static final Creator<ServiceJobComplaint_CFWrapper> CREATOR = new Creator<ServiceJobComplaint_CFWrapper>() {
        public ServiceJobComplaint_CFWrapper createFromParcel(Parcel in) {
            return new ServiceJobComplaint_CFWrapper(in);
        }

        public ServiceJobComplaint_CFWrapper[] newArray(int size) {
            return new ServiceJobComplaint_CFWrapper[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mSJCategoryID);
        dest.writeInt(mSJ_CMCFID);
        dest.writeString(mComplaint);
        dest.writeString(mDateCreated);
        dest.writeInt(mSJComplaintFaultID);
        dest.writeInt(mSJComplaintMobileID);
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

    public int getSJCategoryID() {
        return mSJCategoryID;
    }
    public void setSJCategoryID(int val) { mSJCategoryID = val; }

    public int getSJ_CM_CF_ID() {
        return mSJ_CMCFID;
    }
    public void setSJ_CM_CF_ID(int val) { mSJ_CMCFID = val; }

    public String getDateCreated() {
        return mDateCreated;
    }
    public void setDateCreated(String val) { mDateCreated = val; }

    public String getComplaint() { return mComplaint; }
    public void setComplaint(String val) {
        mComplaint = val;
    }

    public int getSJComplaintFaultID() {
        return mSJComplaintFaultID;
    }
    public void setSJComplaintFaultID(int val) { mSJComplaintFaultID = val; }

    public int getSJComplaintMobileID() {
        return mSJComplaintMobileID;
    }
    public void setSJComplaintMobileID(int val) { mSJComplaintMobileID = val; }
    public String toString() {
        return "\nID : " + this.mId +
                "\nmSJCategoryID: " + this.mSJCategoryID +
                "\nmSJ_CMCFID: " + this.mSJ_CMCFID +
                "\nmComplaint: " + this.mComplaint +
                "\nmDateCreated : " + this.mDateCreated +
                "\nmSJComplaintFaultID : " + this.mSJComplaintFaultID +
                "\nmSJComplaintMobileID : " + this.mSJComplaintMobileID

            ;
    }
}