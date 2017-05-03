package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.listener.ProjectJobListener;
import admin4.techelm.com.techelmtechnologies.model.projectjob.ProjectJobWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_CHOOSE_FORM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_CONTINUE_TASK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_START_CORRECTIVE_ACTION_FORM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_START_DRAWING;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_CHOOSE_FORM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_COMPLETED;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_CONTINUE_TASK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_CORRECTIVE_ACTION_FORM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_NEW;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_ON_PROCESS;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_PENDING;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_START_DRAWING;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_START_TASK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_UNSIGNED;

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
            case PROJECT_JOB_NEW: status = "New";
                break;
            case PROJECT_JOB_UNSIGNED: status = "Unsigned";
                break;
            case PROJECT_JOB_COMPLETED: status = "Completed";
                break;
            case PROJECT_JOB_ON_PROCESS: status = "On Process";
                break;
            case PROJECT_JOB_PENDING: status = "Pending";
                break;
            default : status = "";
                break;
        }
        return status;
    }

    public int setColor(String status) {
        switch (status) {
            case "" : return Color.BLACK;
            case PROJECT_JOB_CHOOSE_FORM: return Color.RED;
            default: return Color.BLUE;
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

    public Spinner setSpinnerComment(Context context, View view) {
        ArrayList<String> options = new ArrayList<String>();
        options.add("YES");
        options.add("NO");
        options.add("NA");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, options);
        Spinner spinnerComment = (Spinner) view.findViewById(R.id.spinnerComment);
        spinnerComment.setAdapter(adapter);
        return spinnerComment;
    }

    // Called at PJ_PISSTaskListAdapter Only
    public void setActionOnClick(ProjectJobListener mCallback, int adapterPosition, ProjectJobWrapper ProjectJobWrapper, String status) {
        switch (status) {
            case PROJECT_JOB_CHOOSE_FORM :
                mCallback.onHandleSelection(adapterPosition, ProjectJobWrapper, ACTION_CHOOSE_FORM);
                break;
            case PROJECT_JOB_CORRECTIVE_ACTION_FORM :
                mCallback.onHandleSelection(adapterPosition, ProjectJobWrapper, ACTION_START_CORRECTIVE_ACTION_FORM);
                break;
            case PROJECT_JOB_START_DRAWING :
                mCallback.onHandleSelection(adapterPosition, ProjectJobWrapper, ACTION_START_DRAWING);
                break;
            case PROJECT_JOB_CONTINUE_TASK :
                mCallback.onHandleSelection(adapterPosition, ProjectJobWrapper, ACTION_CONTINUE_TASK);
                break;
            default :
                break;
        }
    }
}
