package admin4.techelm.com.techelmtechnologies.activity.servicejob_main;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;

/**
 * Reusable Content Populating for i_labels_report_details.xml
 * Created by admin 4 on 14/03/2017.
 */

public class PopulateServiceJobViewDetails {

    private static final String TAG = PopulateServiceJobViewDetails.class.getSimpleName();
    private int PANEL_STATUS = 0;
    private TextView textViewLabelCustomerName;
    private TextView textViewLabelJobSite;
    private TextView textViewLabelServiceNo;
    private TextView textViewLabelTypeOfService;
    private TextView textViewLabelTelephone;
    private TextView textViewLabelFax;
    private TextView textViewLabelEquipmentType;
    private TextView textViewLabelModel;
    private TextView textViewComplaints;
    private TextView textViewRemarksActions;
    private TextView textViewHideShow;

    private TableLayout TableLayoutLabels;
    private LinearLayout linearLayoutRemarks;

    private ExpandableRelativeLayout mExpandLayout;

    private void setViewElements(View view, ServiceJobWrapper serviceJob, int visibility) {
        Log.e(TAG, "setViewElements " +serviceJob.toString() );
        // SERVICE JOB Controls
        ImageButton buttonViewDetails = (ImageButton) view.findViewById(R.id.buttonViewDetails);
        buttonViewDetails.setVisibility(View.GONE);
        textViewLabelCustomerName = (TextView) view.findViewById(R.id.textViewLabelCustomerName);
        textViewLabelJobSite = (TextView) view.findViewById(R.id.textViewLabelJobSite);
        textViewLabelServiceNo = (TextView) view.findViewById(R.id.textViewLabelServiceNo);
        textViewLabelTypeOfService = (TextView) view.findViewById(R.id.textViewLabelTypeOfService);
        textViewLabelTelephone = (TextView) view.findViewById(R.id.textViewLabelTelephone);
        textViewLabelFax = (TextView) view.findViewById(R.id.textViewLabelFax);
        textViewLabelEquipmentType = (TextView) view.findViewById(R.id.textViewLabelEquipmentType);
        textViewLabelModel = (TextView) view.findViewById(R.id.textViewLabelModel);
        textViewComplaints = (TextView) view.findViewById(R.id.textViewComplaints);
        textViewRemarksActions = (TextView) view.findViewById(R.id.textViewRemarksActions);

        // Set TextView Contents
        // buttonViewDetails.setVisibility(visibility);

        textViewLabelCustomerName.setText(serviceJob.getCustomerName());
        textViewLabelJobSite.setText(serviceJob.getJobSite());
        textViewLabelServiceNo.setText(serviceJob.getServiceNumber());
        textViewLabelTypeOfService.setText(serviceJob.getTypeOfService());
        textViewLabelTelephone.setText(serviceJob.getTelephone());
        textViewLabelFax.setText(serviceJob.getFax());
        textViewLabelEquipmentType.setText(serviceJob.getEquipmentType());
        textViewLabelModel.setText(serviceJob.getModelOrSerial());
        textViewComplaints.setText(serviceJob.getComplaintsOrSymptoms());
        textViewRemarksActions.setText(serviceJob.getRemarks());
    }

    /**
     * Use only for the Main Activity for showing the Details on the Material Dialog
     * @param mDialog - Material Dialog, container
     * @param serviceJob - contents
     * @param visibility - View.[GONE, Visible]
     * @param TAG - TAG?
     */
    public void populateServiceJobDetailsMaterialDialog(View mDialog, ServiceJobWrapper serviceJob, int visibility, String TAG) {
        this.setViewElements(mDialog, serviceJob, visibility);

        textViewHideShow = (TextView) mDialog.findViewById(R.id.textViewHideShow);
        textViewHideShow.setVisibility(visibility);
    }

    /**
     * View Details on the Fragments onClick View
     * @param vDialog - view of the Material View
     * @param serviceJob - ServiceJob Wrapper from CalendarFragment
     */
    public void populateServiceJobDetails(View vDialog, ServiceJobWrapper serviceJob, int visibility, String TAG) {
        this.setViewElements(vDialog, serviceJob, visibility);

        // TextView More and Hide
        textViewHideShow = (TextView) vDialog.findViewById(R.id.textViewHideShow);
        textViewHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doHideOrShow();
            }
        });

        // Table Layout Top
        TableLayoutLabels = (TableLayout) vDialog.findViewById(R.id.TableLayoutLabels);
        TableLayoutLabels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doHideOrShow();
            }
        });

        // LinearLayout Below tablelayout
        linearLayoutRemarks = (LinearLayout) vDialog.findViewById(R.id.linearLayoutShown);
        linearLayoutRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doHideOrShow();
            }
        });

        // Panel Hiding/Showing
        mExpandLayout = (ExpandableRelativeLayout) vDialog.findViewById(R.id.expandableLayout);
        hideView();
    }

    private void doHideOrShow() {
        if (this.PANEL_STATUS == 0) {
            showView();
        } else {
            hideView();
        }
    }

    private void hideView() {
        this.PANEL_STATUS = 0;
        textViewHideShow.setText("MORE");
        mExpandLayout.move(0);
    }

    private void showView() {
        this.PANEL_STATUS = 1;
        textViewHideShow.setText("BACK");
        mExpandLayout.toggle();
    }
}
