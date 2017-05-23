package admin4.techelm.com.techelmtechnologies.activity.toolbox_meeting_main.helper;

import android.view.View;
import android.widget.TextView;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.projectjob.ProjectJobWrapper;
import admin4.techelm.com.techelmtechnologies.model.toolboxmeeting.ToolboxMeetingWrapper;

/**
 * Created by admin 3 on 22/05/2017.
 */

public class PopulateToolboxMeetingViewDetails {

    private TextView textViewProjectRef;
    private TextView textViewCustomerName;
    private TextView textViewDate;
    private TextView textViewTargetCompletionDate;
    private TextView textViewFirstInspector;
    private TextView textViewSecondInspector;
    private TextView textViewThirdInspector;

    private void setViewElements(View view, ToolboxMeetingWrapper serviceJob, int visibility) {
        // PROJECT JOB Controls
        textViewProjectRef = (TextView) view.findViewById(R.id.textViewProjectRef);
        textViewCustomerName = (TextView) view.findViewById(R.id.textViewCustomerName);
        textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        textViewTargetCompletionDate = (TextView) view.findViewById(R.id.textViewTargetRemedyDate);
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
    public void populateServiceJobDetails(View vDialog, ToolboxMeetingWrapper serviceJob, int visibility, String TAG) {
        this.setViewElements(vDialog, serviceJob, visibility);
    }

}
