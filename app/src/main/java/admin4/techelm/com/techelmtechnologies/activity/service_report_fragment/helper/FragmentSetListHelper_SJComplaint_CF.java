package admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.helper;

import admin4.techelm.com.techelmtechnologies.adapter.SJ_Complaint_CFListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.listener.ServiceJobComplaintsCFListener;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_CFWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_VIEW_TASK;

/**
 * Created by admin 4 on 31/05/2017.
 *
 */

public class FragmentSetListHelper_SJComplaint_CF {

    public FragmentSetListHelper_SJComplaint_CF() {}


    // Called at PJ_PISSTaskListAdapter Only
    public void setActionOnClick(ServiceJobComplaintsCFListener mCallback, int adapterPosition, ServiceJobComplaint_CFWrapper serviceJobComplaint_cfWrapper, int mode) {
        switch (mode) {
            case ACTION_VIEW_TASK :
                mCallback.onHandleSelection(adapterPosition, serviceJobComplaint_cfWrapper, ACTION_VIEW_TASK);
                break;
            default :
                break;
        }
    }
}
