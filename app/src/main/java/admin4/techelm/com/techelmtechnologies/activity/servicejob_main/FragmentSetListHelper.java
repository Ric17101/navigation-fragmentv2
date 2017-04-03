package admin4.techelm.com.techelmtechnologies.activity.servicejob_main;

import android.graphics.Color;

import admin4.techelm.com.techelmtechnologies.R;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_COMPLETED;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_INCOMPLETE;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_NEW;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_PENDING;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_UNSIGNED;

/**
 * Created by admin 4 on 03/04/2017.
 * Use to Assign the Action for the Main Activties' Fragments List View Actions per TABS:
 *  - Calendar Fragment
 *  - ServiceJob Fragment
 *  - UnsignedServices Fragment
 */

public class FragmentSetListHelper {

    public FragmentSetListHelper() {}

    public String setStatus(String status) {
        switch (status) {
            case SERVICE_JOB_NEW: status = "New";
                break;
            case SERVICE_JOB_UNSIGNED: status = "Unsigned";
                break;
            case SERVICE_JOB_COMPLETED: status = "Completed";
                break;
            case SERVICE_JOB_PENDING:
            case SERVICE_JOB_INCOMPLETE: status = "Incomplete";
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
            case SERVICE_JOB_PENDING:
            case SERVICE_JOB_INCOMPLETE: return Color.RED;
            default: return Color.BLACK;
        }
    }

    // Called in ServiceJobAdapter
    public int setIconTask(String stringTask) {
        switch (stringTask) {
            case SERVICE_JOB_UNSIGNED:
            case SERVICE_JOB_PENDING:
            case SERVICE_JOB_INCOMPLETE: return R.mipmap.conti_icon;
            case SERVICE_JOB_COMPLETED : return R.mipmap.uploaded_icon;
            case SERVICE_JOB_NEW:
            default: return R.mipmap.begin_icon;
        }
    }
    // Called in ServiceJobAdapter
    public String setTaskText(String taskText) {
        switch (taskText) {
            case SERVICE_JOB_UNSIGNED:
            case SERVICE_JOB_PENDING:
            case SERVICE_JOB_INCOMPLETE: taskText = "<u>Continue >></u>";
                break;
            case SERVICE_JOB_NEW: taskText = "<b>Begin Task >></b>";
                break;
            case SERVICE_JOB_COMPLETED: taskText = "<u>Completed</u>";
                break;
            default : taskText = "";
                break;
        }
        return taskText;
    }


}
