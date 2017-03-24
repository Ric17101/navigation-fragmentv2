package admin4.techelm.com.techelmtechnologies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceJobNewPartsWrapper implements Parcelable {
    private int mId;                //id in database
    private int mServiceJobID;         // servicejob id in database
    private String mReplacementPart;     // file name
    private String mQuantity;       //file path
    private String mUnitPrice;       //file path
    private String mTotalPrice;     // file name

    public ServiceJobNewPartsWrapper() { }

    public ServiceJobNewPartsWrapper(Parcel in) {
        mId = in.readInt();
        mServiceJobID = in.readInt();
        mReplacementPart = in.readString();
        mQuantity = in.readString();
        mUnitPrice = in.readString();
        mTotalPrice = in.readString();
    }

    public static final Creator<ServiceJobNewPartsWrapper> CREATOR = new Creator<ServiceJobNewPartsWrapper>() {
        public ServiceJobNewPartsWrapper createFromParcel(Parcel in) {
            return new ServiceJobNewPartsWrapper(in);
        }

        public ServiceJobNewPartsWrapper[] newArray(int size) {
            return new ServiceJobNewPartsWrapper[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mServiceJobID);
        dest.writeString(mQuantity);
        dest.writeString(mReplacementPart);
        dest.writeString(mUnitPrice);
        dest.writeString(mTotalPrice);
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

    public int getServiceJobId() {
        return mServiceJobID;
    }
    public void setServiceId(int val) { mServiceJobID = val; }

    public String getQuantity() {
        return mQuantity;
    }
    public void setQuantity(String val) {
        mQuantity = val;
    }

    public String getPartName() { return mReplacementPart; }
    public void setReplacementPartName(String val) {
        mReplacementPart = val;
    }

    public String getUnitPrice() {
        return mUnitPrice;
    }
    public void setUnitPrice(String val) {
        mUnitPrice = val;
    }

    public String getTotalPrice() { return mTotalPrice; }
    public void setTotalPrice(String val) {
        mTotalPrice = val;
    }

    public String toString() {
        return "ID : " + this.mId +
                "\nService ID : " + this.mServiceJobID +
                "\nReplacement Part: " + this.mReplacementPart +
                "\nQuantity : " + this.mQuantity +
                "\nUnit Price : " + this.mUnitPrice +
                "\nTotal Price : " + this.mTotalPrice;
    }
}