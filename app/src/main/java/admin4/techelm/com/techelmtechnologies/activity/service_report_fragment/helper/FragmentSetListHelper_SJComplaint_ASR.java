package admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.helper;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_Complaint_ASRListAdapter;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_ASRWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_VIEW_TASK;

/**
 * Created by admin 4 on 31/05/2017.
 *
 */

public class FragmentSetListHelper_SJComplaint_ASR {

    public FragmentSetListHelper_SJComplaint_ASR() {}

    // Called at SJ_Complaint_ASRListAdapter Only
    public void setActionOnClick(SJ_Complaint_ASRListAdapter.CallbackInterface mCallback, int adapterPosition, ServiceJobComplaint_ASRWrapper serviceJobComplaint_cfWrapper, int mode) {
        switch (mode) {
            case ACTION_VIEW_TASK :
                mCallback.onHandleSelection(adapterPosition, serviceJobComplaint_cfWrapper, ACTION_VIEW_TASK);
                break;
            default :
                break;
        }
    }
}
