package admin4.techelm.com.techelmtechnologies.activity.servicejob_main;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;

/**
 * Reusable Content Populating
 * Created by admin 4 on 14/03/2017.
 */

public class PopulateServiceJobViewDetails {
    /**
     * View on the CalendarFragment onClick View
     * @param vDialog - view of the Material View
     * @param serviceJob - ServiceJob Wrapper from CalendarFragment
     */
    public void populateServiceJobDetails(View vDialog, ServiceJobWrapper serviceJob, int visibility, String TAG) {

        // SERVICE JOB Controls
        ImageButton buttonViewDetails = (ImageButton) vDialog.findViewById(R.id.buttonViewDetails);
        TextView textViewLabelCustomerName = (TextView) vDialog.findViewById(R.id.textViewLabelCustomerName);
        TextView textViewLabelJobSite = (TextView) vDialog.findViewById(R.id.textViewLabelJobSite);
        TextView textViewLabelServiceNo = (TextView) vDialog.findViewById(R.id.textViewLabelServiceNo);
        TextView textViewLabelTypeOfService = (TextView) vDialog.findViewById(R.id.textViewLabelTypeOfService);
        TextView textViewLabelTelephone = (TextView) vDialog.findViewById(R.id.textViewLabelTelephone);
        TextView textViewLabelFax = (TextView) vDialog.findViewById(R.id.textViewLabelFax);
        TextView textViewLabelEquipmentType = (TextView) vDialog.findViewById(R.id.textViewLabelEquipmentType);
        TextView textViewLabelModel = (TextView) vDialog.findViewById(R.id.textViewLabelModel);
        TextView textViewComplaints = (TextView) vDialog.findViewById(R.id.textViewComplaints);
        TextView textViewRemarksActions = (TextView) vDialog.findViewById(R.id.textViewRemarksActions);


        // Log.e(TAG, "DATA: " + serviceJob.toString());
        buttonViewDetails.setVisibility(visibility);
        textViewLabelCustomerName.setText(serviceJob.getCustomerName());
        textViewLabelJobSite.setText(serviceJob.getJobSite());
        textViewLabelServiceNo.setText(serviceJob.getServiceNumber());
        textViewLabelTypeOfService.setText(serviceJob.getTypeOfService());
        textViewLabelTelephone.setText(serviceJob.getTelephone());
        textViewLabelFax.setText(serviceJob.getFax());
        textViewLabelEquipmentType.setText(serviceJob.getEquipmentType());
        textViewLabelModel.setText(serviceJob.getModelOrSerial());
        textViewComplaints.setText(serviceJob.getComplaintsOrSymptoms());
        textViewRemarksActions.setText(serviceJob.getActionsOrRemarks());
    }
}
