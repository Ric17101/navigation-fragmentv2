package admin4.techelm.com.techelmtechnologies.activity.servicejob_main;

import android.inputmethodservice.Keyboard;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;

/**
 * Reusable Content Populating
 * Created by admin 4 on 14/03/2017.
 */

public class PopulateServiceJobViewDetails {

    TextView textViewLabelCustomerName;
    TextView textViewLabelJobSite;
    TextView textViewLabelServiceNo;
    TextView textViewLabelTypeOfService;
    TextView textViewLabelTelephone;
    TextView textViewLabelFax;
    TextView textViewLabelEquipmentType;
    TextView textViewLabelModel;
    TextView textViewComplaints;
    TextView textViewRemarksActions;
    TextView textViewHideShow;

    LinearLayout linearLayoutRemarks;
    TableRow rowTableTypeOfService;
    TableRow rowTableTelephone;
    TableRow rowTableFax;
    TableRow rowTableEquipmentType;
    TableRow rowTableSerialNo;

    /**
     * View on the CalendarFragment onClick View
     * @param vDialog - view of the Material View
     * @param serviceJob - ServiceJob Wrapper from CalendarFragment
     */
    public void populateServiceJobDetails(View vDialog, ServiceJobWrapper serviceJob, int visibility, String TAG) {

        // SERVICE JOB Controls
        ImageButton buttonViewDetails = (ImageButton) vDialog.findViewById(R.id.buttonViewDetails);
        textViewLabelCustomerName = (TextView) vDialog.findViewById(R.id.textViewLabelCustomerName);
        textViewLabelJobSite = (TextView) vDialog.findViewById(R.id.textViewLabelJobSite);
        textViewLabelServiceNo = (TextView) vDialog.findViewById(R.id.textViewLabelServiceNo);
        textViewLabelTypeOfService = (TextView) vDialog.findViewById(R.id.textViewLabelTypeOfService);
        textViewLabelTelephone = (TextView) vDialog.findViewById(R.id.textViewLabelTelephone);
        textViewLabelFax = (TextView) vDialog.findViewById(R.id.textViewLabelFax);
        textViewLabelEquipmentType = (TextView) vDialog.findViewById(R.id.textViewLabelEquipmentType);
        textViewLabelModel = (TextView) vDialog.findViewById(R.id.textViewLabelModel);
        textViewComplaints = (TextView) vDialog.findViewById(R.id.textViewComplaints);
        textViewRemarksActions = (TextView) vDialog.findViewById(R.id.textViewRemarksActions);

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

        // TextView More and Hide
        textViewHideShow = (TextView) vDialog.findViewById(R.id.textViewHideShow);
        textViewHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewHideShow.getText().toString().equals("MORE")) {
                    textViewHideShow.setText("BACK");
                    showOrHideTextView(View.GONE);
                } else {
                    textViewHideShow.setText("MORE");
                    showOrHideTextView(View.VISIBLE);
                }
            }
        });
        linearLayoutRemarks = (LinearLayout) vDialog.findViewById(R.id.linearLayoutRemarks);
        rowTableTypeOfService = (TableRow) vDialog.findViewById(R.id.rowTableTypeOfService);
        rowTableTelephone = (TableRow) vDialog.findViewById(R.id.rowTableTelephone);
        rowTableFax = (TableRow) vDialog.findViewById(R.id.rowTableFax);
        rowTableEquipmentType = (TableRow) vDialog.findViewById(R.id.rowTableEquipmentType);
        rowTableSerialNo = (TableRow) vDialog.findViewById(R.id.rowTableSerialNo);
    }

    private void showOrHideTextView(int visibility) {
        linearLayoutRemarks.setVisibility(visibility);
        rowTableTypeOfService.setVisibility(visibility);
        rowTableTelephone.setVisibility(visibility);
        rowTableFax.setVisibility(visibility);
        rowTableEquipmentType.setVisibility(visibility);
        rowTableSerialNo.setVisibility(visibility);
    }
}
