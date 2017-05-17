package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.helper;

import android.view.View;
import android.widget.TextView;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.projectjob.b1.PISSTaskWrapper;

/**
 * Reusable Content Populating
 * Created by admin 4 on 04/05/2017.
 */

public class PopulateProjectJobTaskViewDetails {

    private TextView textViewSerialNo;
    private TextView textViewDescription;
    private TextView textViewComments;
    private TextView textViewStatus;

    private void setViewElements(View view, PISSTaskWrapper serviceJob) {
        FragmentSetListHelper_ProjectJob mSetHelper = new FragmentSetListHelper_ProjectJob();
        // PROJECT JOB Controls
        textViewSerialNo = (TextView) view.findViewById(R.id.textViewSerialNo);
        textViewDescription = (TextView) view.findViewById(R.id.textViewRemarks);
        textViewComments = (TextView) view.findViewById(R.id.textViewComments);
        textViewStatus = (TextView) view.findViewById(R.id.textViewStatus);

        // Set TextView Contents
        // buttonViewDetails.setVisibility(visibility);

        textViewSerialNo.setText(serviceJob.getSerialNo());
        textViewDescription.setText(serviceJob.getDescription());
        textViewComments.setText(serviceJob.getComments());
        textViewStatus.setText(mSetHelper.setStatus(serviceJob.getStatus()));

    }

    /**
     * View Details on the Fragments onClick View
     * @param vDialog - view of the Material View
     * @param projectTaskJob - PISSTaskWrapper Wrapper
     */
    public void populateServiceJobDetails(View vDialog, PISSTaskWrapper projectTaskJob, String TAG) {
        this.setViewElements(vDialog, projectTaskJob);
    }

}
