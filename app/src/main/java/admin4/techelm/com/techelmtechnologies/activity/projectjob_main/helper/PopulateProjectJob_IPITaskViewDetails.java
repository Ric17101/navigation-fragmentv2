package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.helper;

import android.view.View;
import android.widget.TextView;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.projectjob.b2.IPI_TaskWrapper;

/**
 * Reusable Content Populating
 * Created by admin 4 on 08/05/2017.
 */

public class PopulateProjectJob_IPITaskViewDetails {

    private TextView textViewSerialNo;
    private TextView textViewDescription;

    private void setViewElements(View view, IPI_TaskWrapper serviceJob) {
        // PROJECT JOB Controls
        textViewSerialNo = (TextView) view.findViewById(R.id.textViewSerialNo);
        textViewDescription = (TextView) view.findViewById(R.id.textViewRemarks);

        // Set TextView Contents
        // buttonViewDetails.setVisibility(visibility);

        textViewSerialNo.setText(serviceJob.getSerialNo());
        textViewDescription.setText(serviceJob.getDescription());

    }

    /**
     * View Details on the Fragments onClick View
     * @param vDialog - view of the Material View
     * @param projectTaskJob - IPI_TaskWrapper Wrapper
     */
    public void populateServiceJobDetails(View vDialog, IPI_TaskWrapper projectTaskJob, String TAG) {
        this.setViewElements(vDialog, projectTaskJob);
    }

}
