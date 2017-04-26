package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment;

import android.graphics.Color;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.IPITaskListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.PISSTaskListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.ProjectJobB1ListAdapter;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_CHOOSE_FORM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_CONTINUE_TASK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_START_CORRECTIVE_ACTION_FORM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_START_DRAWING;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_CHOOSE_FORM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_CONTINUE_TASK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_START_DRAWING;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_START_TASK;
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

public class FragmentSetListHelper_ProjectJob {

    public FragmentSetListHelper_ProjectJob() {}

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
            case "" : return Color.BLUE;
            case PROJECT_JOB_CHOOSE_FORM: return Color.RED;
            default: return Color.BLACK;
        }
    }

    // Called in ServiceJobAdapter
    public int setIconTask(String stringTask) {
        switch (stringTask) {
            case "" : return R.mipmap.conti_icon;
            case PROJECT_JOB_CHOOSE_FORM: return R.mipmap.uploaded_icon;
            default: return R.mipmap.begin_icon;
        }
    }
    // Called in ProjectJobAdapter
    public String setTaskText(String taskText) {
        switch (taskText) {
            case PROJECT_JOB_CHOOSE_FORM: taskText = "<u><b>Choose Form</b></u>";
                break;
            case PROJECT_JOB_START_TASK: taskText = "<u><b>Start task >></b></u>";
                break;
            case PROJECT_JOB_CONTINUE_TASK: taskText = "<u><b>Continue task >></b></u>";
                break;
            default : taskText = "";
                break;
        }
        return taskText;
    }

    // Called at ProjecrJobListAdapter Only, Event to Select Type of Form
    public void setActionOnClick(ProjectJobB1ListAdapter.CallbackInterface mCallback, int adapterPosition, ServiceJobWrapper serviceJobWrapper, String status) {
        mCallback.onHandleSelection(adapterPosition, serviceJobWrapper, ACTION_CHOOSE_FORM);
    }

    // Called at PISSTaskListAdapter Only
    public void setActionOnClick(PISSTaskListAdapter.CallbackInterface mCallback, int adapterPosition, ServiceJobWrapper serviceJobWrapper, String status) {
        switch (status) {
            case PROJECT_JOB_START_DRAWING :
                mCallback.onHandleSelection(adapterPosition, serviceJobWrapper, ACTION_START_DRAWING);
                break;
            case PROJECT_JOB_CONTINUE_TASK :
                mCallback.onHandleSelection(adapterPosition, serviceJobWrapper, ACTION_CONTINUE_TASK);
                break;
            default :
                break;
        }
    }

    // Called at IPITaskListAdapter Only
    public void setActionOnClick(IPITaskListAdapter.CallbackInterface mCallback, int adapterPosition, ServiceJobWrapper serviceJobWrapper, String status) {
        mCallback.onHandleSelection(adapterPosition, serviceJobWrapper, ACTION_START_CORRECTIVE_ACTION_FORM);
    }


}
