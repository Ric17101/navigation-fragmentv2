package admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.helper;

import android.util.TypedValue;
import android.widget.TextView;

import admin4.techelm.com.techelmtechnologies.adapter.SJ_Compaint_CategoryListAdapter;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_CategoryWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_VIEW_TASK;

/**
 * Created by admin 4 on 31/05/2017.
 *
 */

public class FragmentSetListHelper_SJComplaint_Category {

    public FragmentSetListHelper_SJComplaint_Category() {}

    // Called at PJ_PISSTaskListAdapter Only
    public void setActionOnClick(SJ_Compaint_CategoryListAdapter.CallbackInterface mCallback, int adapterPosition, ServiceJobComplaint_CategoryWrapper serviceJobComplaint_cfWrapper, int mode) {
        switch (mode) {
            case ACTION_VIEW_TASK :
                mCallback.onHandleSelection(adapterPosition, serviceJobComplaint_cfWrapper, ACTION_VIEW_TASK);
                break;
            default :
                break;
        }
    }
}
