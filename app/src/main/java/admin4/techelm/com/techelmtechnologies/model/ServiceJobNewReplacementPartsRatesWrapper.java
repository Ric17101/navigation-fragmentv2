package admin4.techelm.com.techelmtechnologies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin 4 on 18/04/2017.
 * Used for New Parts Replacement Form, before submit
 * Parcelable in order to pass this Model as Arguments in another Activity in android
 */

public class ServiceJobNewReplacementPartsRatesWrapper implements Parcelable {

    private int mId;                    // id in database
    private String mReplacementPart;    // file name
    private String mUnitPrice;          //file path
    private String mDescription;        // if needed

    public ServiceJobNewReplacementPartsRatesWrapper() {}

    public ServiceJobNewReplacementPartsRatesWrapper(Parcel in) {
        mId = in.readInt();
        mReplacementPart = in.readString();
        mUnitPrice = in.readString();
        mDescription = in.readString();
    }

    public static final Creator<ServiceJobNewReplacementPartsRatesWrapper> CREATOR = new Creator<ServiceJobNewReplacementPartsRatesWrapper>() {
        public ServiceJobNewReplacementPartsRatesWrapper createFromParcel(Parcel in) {
            return new ServiceJobNewReplacementPartsRatesWrapper(in);
        }

        public ServiceJobNewReplacementPartsRatesWrapper[] newArray(int size) {
            return new ServiceJobNewReplacementPartsRatesWrapper[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mReplacementPart);
        dest.writeString(mUnitPrice);
        dest.writeString(mDescription);
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

    public String getDescription() {
        return mDescription;
    }
    public void setDescription(String val) { mDescription = val; }

    public String toString() {
        return "ID : " + this.mId +
                "\nReplacement Part: " + this.mReplacementPart +
                "\nUnit Price : " + this.mUnitPrice +
                "\nDescription : " + this.mDescription;
    }
}
