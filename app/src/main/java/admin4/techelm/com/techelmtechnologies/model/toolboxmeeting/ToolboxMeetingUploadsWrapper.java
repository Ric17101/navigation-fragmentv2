package admin4.techelm.com.techelmtechnologies.model.toolboxmeeting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin 3 on 22/05/2017.
 */

public class ToolboxMeetingUploadsWrapper implements Parcelable {
        private int mId;                //id in database
        private int mProjectjobID;      // servicejob id in database
        private String mUploadName;  // file name
        private String mFilePath;       //file path

        public ToolboxMeetingUploadsWrapper() { }

        public ToolboxMeetingUploadsWrapper(Parcel in) {
            mId = in.readInt();
            mProjectjobID = in.readInt();
            mUploadName = in.readString();
            mFilePath = in.readString();
        }

        public static final Creator<ToolboxMeetingUploadsWrapper> CREATOR = new Creator<ToolboxMeetingUploadsWrapper>() {
            public ToolboxMeetingUploadsWrapper createFromParcel(Parcel in) {
                return new ToolboxMeetingUploadsWrapper(in);
            }

            @Override
            public ToolboxMeetingUploadsWrapper[] newArray(int size) {
                return new ToolboxMeetingUploadsWrapper[size];
            }
        };

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(mId);
            dest.writeInt(mProjectjobID);
            dest.writeString(mUploadName);
            dest.writeString(mFilePath);
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

        public int getProjectjobID() {
            return mProjectjobID;
        }
        public void setProjectjobID(int serviceId) { mProjectjobID = serviceId; }

        public String getFilePath() {
            return mFilePath;
        }
        public void setFilePath(String filePath) {
            mFilePath = filePath;
        }

        public String getUploadName() { return mUploadName; }
        public void setUploadName(String name) {
            mUploadName = name;
        }

        public String toString() {
            return "ID : " + this.mId +
                    "\nProject ID : " + this.mProjectjobID +
                    "\nFile Path : " + this.mFilePath +
                    "\nFile Name : " + this.mUploadName;
        }
}

