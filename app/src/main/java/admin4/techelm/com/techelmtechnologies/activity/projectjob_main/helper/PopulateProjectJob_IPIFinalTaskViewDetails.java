package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.helper;

import android.view.View;
import android.widget.TextView;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.projectjob.b2.IPI_TaskFinalWrapper;

/**
 * Reusable Content Populating
 * Created by admin 4 on 08/05/2017.
 */

public class PopulateProjectJob_IPIFinalTaskViewDetails {

    private TextView textViewSerialNo;
    private TextView textViewCarNo;
    private TextView textViewDescritionCorrectiveAction;
    private TextView textViewTargetRemedyDate;
    private TextView textViewTargetCompletionDate;
    private TextView textViewRemarks;
    private TextView textViewDisposition;


    private void setViewElements(View view, IPI_TaskFinalWrapper ipiTaskFinalWrapper) {
        // PROJECT JOB Controls
        textViewSerialNo = (TextView) view.findViewById(R.id.textViewSerialNo);
        textViewCarNo = (TextView) view.findViewById(R.id.textViewCarNo);
        textViewDescritionCorrectiveAction = (TextView) view.findViewById(R.id.textViewDescriptionCorrectiveAction);
        textViewTargetRemedyDate = (TextView) view.findViewById(R.id.textViewTargetRemedyDate);
        textViewTargetCompletionDate = (TextView) view.findViewById(R.id.textViewTargetCompletionDate);
        textViewRemarks = (TextView) view.findViewById(R.id.textViewRemarks);
        textViewDisposition = (TextView) view.findViewById(R.id.textViewDisposition);

        // Set TextView Contents
        // buttonViewDetails.setVisibility(visibility);

        textViewSerialNo.setText(ipiTaskFinalWrapper.getSerialNo());
        textViewCarNo.setText(ipiTaskFinalWrapper.getCarNo());
        textViewDescritionCorrectiveAction.setText(ipiTaskFinalWrapper.getDescription());
        textViewTargetRemedyDate.setText(ipiTaskFinalWrapper.getTargetRemedyDate());
        textViewTargetCompletionDate.setText(ipiTaskFinalWrapper.getCompletionDate());
        textViewRemarks.setText(ipiTaskFinalWrapper.getRemarks());
        textViewDisposition.setText(ipiTaskFinalWrapper.getDisposition());

    }

    /**
     * View Details on the Fragments onClick View
     * @param vDialog - view of the Material View
     * @param projectTaskJob - IPI_TaskWrapper Wrapper
     */
    public void populateServiceJobDetails(View vDialog, IPI_TaskFinalWrapper projectTaskJob, String TAG) {
        this.setViewElements(vDialog, projectTaskJob);
    }

}
