package admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.helper;

import admin4.techelm.com.techelmtechnologies.adapter.SJ_Compaint_MobileListAdapter;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_MobileWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_VIEW_TASK;

/**
 * Created by admin 4 on 31/05/2017.
 *
 */

public class FragmentSetListHelper_SJComplaint_Mobile {

    public FragmentSetListHelper_SJComplaint_Mobile() {}

    // Called at PJ_PISSTaskListAdapter Only
    public void setActionOnClick(SJ_Compaint_MobileListAdapter.CallbackInterface mCallback, int adapterPosition, ServiceJobComplaint_MobileWrapper serviceJobComplaint_mobileWrapper, int mode) {
        switch (mode) {
            case ACTION_VIEW_TASK :
                mCallback.onHandleSelection(adapterPosition, serviceJobComplaint_mobileWrapper, ACTION_VIEW_TASK);
                break;
            default :
                break;
        }
    }
}
