package admin4.techelm.com.techelmtechnologies.activity.servicejob_main;

import android.graphics.Color;
import android.util.TypedValue;
import android.widget.TextView;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_CalendarListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_UnsignedListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.listener.ServiceJobListener;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_ALREADY_COMPLETED;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_ALREADY_ON_PROCESS;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_BEGIN;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_EDIT;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_COMPLETED;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_NEW;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_ON_PROCESS;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_PENDING;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_UNSIGNED;

/**
 * Created by admin 4 on 03/04/2017.
 * Use to Assign the Action for the Main Activties' Fragments List View Actions per TABS:
 *  - Calendar Fragment
 *  - ServiceJob Fragment
 *  - UnsignedServices Fragment
 */

public class FragmentSetListHelper_ServiceJob {

    public FragmentSetListHelper_ServiceJob() {}

    public String setStatus(String status) {
        switch (status) {
            case SERVICE_JOB_NEW: status = "New";
                break;
            case SERVICE_JOB_UNSIGNED: status = "Unsigned";
                break;
            case SERVICE_JOB_COMPLETED: status = "Completed";
                break;
            case SERVICE_JOB_ON_PROCESS: status = "On Process";
                break;
            case SERVICE_JOB_PENDING: status = "Pending";
                break;
            default : status = "";
                break;
        }
        return status;
    }

    public int setColor(String status) {
        switch (status) {
            case SERVICE_JOB_COMPLETED: return Color.BLUE;
            case SERVICE_JOB_NEW:
            case SERVICE_JOB_UNSIGNED:
            case SERVICE_JOB_ON_PROCESS:
            case SERVICE_JOB_PENDING: return Color.RED;
            default: return Color.BLACK;
        }
    }

    // Called in ServiceJobAdapter
    public int setIconTask(String stringTask) {
        switch (stringTask) {
            case SERVICE_JOB_UNSIGNED:
            case SERVICE_JOB_ON_PROCESS:
            case SERVICE_JOB_PENDING: return R.mipmap.conti_icon;
            case SERVICE_JOB_COMPLETED : return R.mipmap.uploaded_icon;
            case SERVICE_JOB_NEW:
            default: return R.mipmap.begin_icon;
        }
    }
    // Called in ServiceJobAdapter
    public String setTaskText(String taskText) {
        switch (taskText) {
            case SERVICE_JOB_ON_PROCESS: //  taskText = "<u><b>Pending</b></u>"; // Should this be Continue to whom it has been opened??
            case SERVICE_JOB_UNSIGNED:
            case SERVICE_JOB_PENDING: taskText = "<u><b>Continue >></b></u>";
                break;
            case SERVICE_JOB_NEW: taskText = "<u><b>Begin Task >></b></u>";
                break;
            case SERVICE_JOB_COMPLETED: taskText = "<b>Completed</b>";
                break;
            default : taskText = "";
                break;
        }
        return taskText;
    }

    public void setTextNumberSize(TextView textViewDateNumber, String text) {
        switch (text.length()) {
            case 3 :
                textViewDateNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                textViewDateNumber.setPadding(textViewDateNumber.getPaddingLeft(), 14, textViewDateNumber.getPaddingRight(), 14);
                break;
            case 4 :
                textViewDateNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                textViewDateNumber.setPadding(textViewDateNumber.getPaddingLeft(), 12, textViewDateNumber.getPaddingRight(), 12);
                break;
        }
    }

    // Called at SJ_CalendarListAdapter Only
    public void setActionOnClick(SJ_CalendarListAdapter.CallbackInterface mCallback, int adapterPosition, ServiceJobWrapper serviceJobWrapper, String status) {
        switch(status) {
            case SERVICE_JOB_COMPLETED :
                mCallback.onHandleSelection(adapterPosition, serviceJobWrapper, ACTION_ALREADY_COMPLETED);
                break;
            case SERVICE_JOB_NEW :
                mCallback.onHandleSelection(adapterPosition, serviceJobWrapper, ACTION_BEGIN);
                break;
            case SERVICE_JOB_PENDING :
            case SERVICE_JOB_UNSIGNED :
                mCallback.onHandleSelection(adapterPosition, serviceJobWrapper, ACTION_EDIT);
                break;
            case SERVICE_JOB_ON_PROCESS :
                mCallback.onHandleSelection(adapterPosition, serviceJobWrapper, ACTION_ALREADY_ON_PROCESS);
                break;
            default :
                break;
        }
    }

    // Called at SJ_ListAdapter Only
    // Action EDIT only
    public void setActionOnClick(SJ_UnsignedListAdapter.CallbackInterface mCallback, int adapterPosition, ServiceJobWrapper serviceJobWrapper, String status) {
        switch(status) {
            case SERVICE_JOB_PENDING :
            case SERVICE_JOB_UNSIGNED :
                mCallback.onHandleSelection(adapterPosition, serviceJobWrapper, ACTION_EDIT);
                break;
            case SERVICE_JOB_ON_PROCESS :
                mCallback.onHandleSelection(adapterPosition, serviceJobWrapper, ACTION_ALREADY_ON_PROCESS);
                break;
            default :
                break;
        }
    }

    // Called at SJ_ListAdapter Only
    public void setActionOnClick(ServiceJobListener mCallback, int adapterPosition, ServiceJobWrapper serviceJobWrapper, String status) {
        switch(status) {
            case SERVICE_JOB_COMPLETED :
                mCallback.onHandleSelection(adapterPosition, serviceJobWrapper, ACTION_ALREADY_COMPLETED);
                break;
            case SERVICE_JOB_NEW :
                mCallback.onHandleSelection(adapterPosition, serviceJobWrapper, ACTION_BEGIN);
                break;
            case SERVICE_JOB_PENDING :
            case SERVICE_JOB_UNSIGNED :
                mCallback.onHandleSelection(adapterPosition, serviceJobWrapper, ACTION_EDIT);
                break;
            case SERVICE_JOB_ON_PROCESS :
                mCallback.onHandleSelection(adapterPosition, serviceJobWrapper, ACTION_ALREADY_ON_PROCESS);
                break;
            default :
                break;
        }
    }


}
