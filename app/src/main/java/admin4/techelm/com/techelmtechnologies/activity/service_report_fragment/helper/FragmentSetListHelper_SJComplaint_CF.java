package admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.helper;

import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import admin4.techelm.com.techelmtechnologies.adapter.SJ_Complaint_CFListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.listener.ServiceJobComplaintsCFListener;
import admin4.techelm.com.techelmtechnologies.adapter.listener.ServiceJobComplaintsCategoryListener;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_CFWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_MobileWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_VIEW_TASK;

/**
 * Created by admin 4 on 31/05/2017.
 *
 */

public class FragmentSetListHelper_SJComplaint_CF {

    private int PANEL_STATUS = 0;

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


    public void setActionOnClick(ServiceJobComplaintsCategoryListener mCallback,
             int adapterPosition,
             ServiceJobComplaint_MobileWrapper complaintMobileWrapper,
             String clickedItemValue, int mode)
    {
        switch (mode) {
            case ACTION_VIEW_TASK :
                mCallback.onHandleSelection(adapterPosition, complaintMobileWrapper, clickedItemValue, ACTION_VIEW_TASK);
                break;
            default :
                break;
        }
    }

    public void doHideOrShow(ExpandableRelativeLayout layout) {
        if (this.PANEL_STATUS == 0) {
            showView(layout);
        } else {
            hideView(layout);
        }
    }

    public void hideView(ExpandableRelativeLayout mExpandLayout) {
        this.PANEL_STATUS = 0;
        mExpandLayout.move(0);
    }

    private void showView(ExpandableRelativeLayout mExpandLayout) {
        this.PANEL_STATUS = 1;
        mExpandLayout.toggle();
    }
}
