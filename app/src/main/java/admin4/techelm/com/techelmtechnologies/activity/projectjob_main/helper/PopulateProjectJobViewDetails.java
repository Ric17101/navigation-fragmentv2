package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.helper;

import android.view.View;
import android.widget.TextView;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.projectjob.ProjectJobWrapper;

/**
 * Reusable Content Populating for i_s_report_details.xml
 * Created by admin 4 on 14/03/2017.
 */

public class PopulateProjectJobViewDetails {

    private TextView textViewProjectRef;
    private TextView textViewCustomerName;
    private TextView textViewDate;
    private TextView textViewTargetCompletionDate;
    private TextView textViewFirstInspector;
    private TextView textViewSecondInspector;
    private TextView textViewThirdInspector;

    private void setViewElements(View view, ProjectJobWrapper serviceJob, int visibility) {
        // PROJECT JOB Controls
        textViewProjectRef = (TextView) view.findViewById(R.id.textViewProjectRef);
        textViewCustomerName = (TextView) view.findViewById(R.id.textViewCustomerName);
        textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        textViewTargetCompletionDate = (TextView) view.findViewById(R.id.textViewTargetCompletionDate);
        textViewFirstInspector = (TextView) view.findViewById(R.id.textViewFirstInspector);
        textViewSecondInspector = (TextView) view.findViewById(R.id.textViewSecondInspector);
        textViewThirdInspector = (TextView) view.findViewById(R.id.textViewThirdInspector);

        // Set TextView Contents
        // buttonViewDetails.setVisibility(visibility);

        textViewProjectRef.setText(serviceJob.getProjectRef());
        textViewCustomerName.setText(serviceJob.getCustomerName());
        textViewDate.setText(serviceJob.getStartDate());
        textViewTargetCompletionDate.setText(serviceJob.getTargetCompletionDate());
        textViewFirstInspector.setText(serviceJob.getFirstInspector());
        textViewSecondInspector.setText(serviceJob.getSecondInspector());
        textViewThirdInspector.setText(serviceJob.getThirdInspector());

    }

    /**
     * View Details on the Fragments onClick View
     * @param vDialog - view of the Material View
     * @param serviceJob - ServiceJob Wrapper from CalendarFragment
     */
    public void populateServiceJobDetails(View vDialog, ProjectJobWrapper serviceJob, int visibility, String TAG) {
        this.setViewElements(vDialog, serviceJob, visibility);
    }

}
