package admin4.techelm.com.techelmtechnologies.activity.service_report_fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.ServiceJobPartsListAdapter;
import admin4.techelm.com.techelmtechnologies.db.servicejob.PartsSJDBUtil;
import admin4.techelm.com.techelmtechnologies.db.servicejob.ServiceJobDBUtil;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobNewPartsWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobNewReplacementPartsRatesWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.activity.servicejob_main.PopulateServiceJobViewDetails;
import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;

public class PartReplacement_FRGMT_2 extends Fragment {

    private static final String TAG = "PartReplacement_FRGMT_2";
    private static final String ADD_NEW_PART_REPLACEMENT = "CREATE";
    private static final String UPDATE_NEW_PART_REPLACEMENT = "UPDATE";
    private String actionNewReplacement = "";
    private int mServiceID; // For DB Purpose to save the file on the ServiceID
    private Context mContext;

    // A. SERVICE ID INFO
    private ServiceJobDBUtil mSJDB;
    private List<ServiceJobWrapper> mSJResultList = null;
    private static ServiceJobWrapper mServiceJobFromBundle; // From Calling Activity
    private static List<ServiceJobNewReplacementPartsRatesWrapper> mSJRatesList;

    // B. CAMERA Controls
    private static final String IMAGE_DIRECTORY = "part_replacement";

    private Uri mPicUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private ServiceJobPartsListAdapter mUploadListAdapter; // ListView Setup
    private RecyclerView mUploadResultsList;
    private List<ServiceJobNewPartsWrapper> mUploadResults = null;
    private PartsSJDBUtil mPartsDB;

    private ImageButton mButtonViewUploadFileNew;
    private ProgressBar mProgressBarUploadingNew;

    // SlidingPager Tab Set Up
    private static final String ARG_POSITION = "position";
    private int position;

    // B.1 Form New Replacement Part
    private MaterialDialog mNewPartDialog;
    private Spinner mSpinnerReplacementParts;
    private Spinner mSpinnerQuantity;
    // private Spinner mSpinnerUnitPrice;
    // private Spinner mSpinnerTotalPrice;
    private TextView mTextViewUnitPrice;
    private TextView mTextViewTotalPrice;
    private CardView mCardViewNewUpload;
    private ServiceJobNewPartsWrapper mSJPart; // Specifically, we use this global as per Update only of SJNew Parts

    public static PartReplacement_FRGMT_2 newInstance(int position, ServiceJobWrapper serviceJob, List<ServiceJobNewReplacementPartsRatesWrapper> rateList) {
        PartReplacement_FRGMT_2 frag = new PartReplacement_FRGMT_2();
        Bundle args = new Bundle();

        args.putInt(ARG_POSITION, position);
        frag.setArguments(args);

        mServiceJobFromBundle = serviceJob;
        mSJRatesList = rateList;
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_part_replacement, container, false);

        this.mContext = container.getContext();
        initNewPartsView(view);
        initNewPartsAddedView(view);
        initSpinnerProgessBar(view);
        initButton(view);

        if (mServiceJobFromBundle != null) { // if Null don't show anything
            mServiceID = mServiceJobFromBundle.getID();

            // ServiceJob Details
            new PopulateServiceJobViewDetails()
                    .populateServiceJobDetails(
                            view,
                            mServiceJobFromBundle,
                            View.VISIBLE,
                            TAG);
            // mServiceID = savedInstanceState.getInt(RECORD_SERVICE_KEY);
            // mServiceID = 2;
            // populateServiceJobDetails(mServiceID);

            // Upload List
            setUpUploadsRecyclerView(view);
            setupUploadsResultsList();
            if (mUploadResults == null) {
                populateUploadsCardList();
            }
        }
        return view;
    }

    private void initSpinnerProgessBar(View view) {
        mProgressBarUploadingNew = (ProgressBar) view.findViewById(R.id.progressBarUploadingNew);
        mProgressBarUploadingNew.setVisibility(View.GONE);
    }

    private void initButton(View view) {

        /** BUTTON BACK */
        Button button_back = (Button) view.findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(PartReplacement_FRGMT_2.this, ServiceReport_FRGMT_BEFORE.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, mServiceJobFromBundle));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);*/
                ((ServiceJobViewPagerActivity) getActivity()).fromFragmentNavigate(-1);
            }
        });

        /** BUTTON NEXT */
        Button button_next = (Button) view.findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(PartReplacement_FRGMT_2.this, AddReplacementPart_FRGMT_3.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, mServiceJobFromBundle));
                overridePendingTransition(R.anim.enter, R.anim.exit);*/
                ((ServiceJobViewPagerActivity) getActivity()).fromFragmentNavigate(1);
            }
        });

        /** BUTTON VIEW DETAILS */
        ImageButton buttonViewDetails = (ImageButton) view.findViewById(R.id.buttonViewDetails);
        buttonViewDetails.setVisibility(View.GONE);
        /*buttonViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(PartReplacement_FRGMT_2.this, SigningOff_FRGMT_4.class)
                //        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                //overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });*/

        /** BUTTON UPLOAD NEW REPLACEMENT PART */
        mButtonViewUploadFileNew = (ImageButton) view.findViewById(R.id.buttonViewUploadFileNew);
        mButtonViewUploadFileNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionNewReplacement = ADD_NEW_PART_REPLACEMENT;
                setSpinnerValue("Part 2", "3", "some value", "some value"); // Populate Spinners before showing
                mNewPartDialog.show(); // Initialize at onCreate
            }
        });
    }


    /*********** A. SERVICE DETAILS ***********/
    // THIS IS DONE FROM PopulateServiceJobViewDetails()
    /*@Override
    public void onNewIPI_PWDEntryAdded(String serviceNum) {

    }

    @Override
    public void onIPI_PWDEntryUpdated(String fileName) {

    }

    @Override
    public void onIPI_PWDEntryDeleted() {

    }*/
    private void populateServiceJobDetails(int serviceID) {

        // SERVICE JOB Controls
        /*TextView textViewLabelCustomerName = (TextView) findViewById(R.id.textViewLabelCustomerName);
        TextView textViewLabelJobSite = (TextView) findViewById(R.id.textViewLabelJobSite);
        TextView textViewLabelServiceNo = (TextView) findViewById(R.id.textViewLabelServiceNo);
        TextView textViewLabelTypeOfService = (TextView) findViewById(R.id.textViewLabelTypeOfService);
        TextView textViewLabelTelephone = (TextView) findViewById(R.id.textViewLabelTelephone);
        TextView textViewLabelFax = (TextView) findViewById(R.id.textViewLabelFax);
        TextView textViewLabelEquipmentType = (TextView) findViewById(R.id.textViewLabelEquipmentType);
        TextView textViewLabelModel = (TextView) findViewById(R.id.textViewLabelModel);
        TextView textViewComplaints = (TextView) findViewById(R.id.textViewComplaints);
        TextView textViewRemarksActions = (TextView) findViewById(R.id.textViewRemarksActions);

        mSJDB = new ServiceJobDBUtil(PartReplacement_FRGMT_2.this);
        mSJDB.open();
        mSJResultList = mSJDB.getAllJSDetailsByID(serviceID);
        mSJDB.close();

        for (int i = 0; i < mSJResultList.size(); i++) {
            Log.e(TAG, "DATA: " + mSJResultList.get(i).toString());
            textViewLabelCustomerName.setText(mSJResultList.get(i).getCustomerID());
            textViewLabelJobSite.setText(mSJResultList.get(i).getActionsOrRemarks());
            textViewLabelServiceNo.setText(mSJResultList.get(i).getServiceNumber());
            textViewLabelTypeOfService.setText(mSJResultList.get(i).getTypeOfService());
            textViewLabelTelephone.setText(mSJResultList.get(i).getTelephone());
            textViewLabelFax.setText(mSJResultList.get(i).getFax());
            textViewLabelEquipmentType.setText(mSJResultList.get(i).getEquipmentType());
            textViewLabelModel.setText(mSJResultList.get(i).getModelOrSerial());
            textViewComplaints.setText(mSJResultList.get(i).getComplaintsOrSymptoms());
            textViewRemarksActions.setText(mSJResultList.get(i).getActionsOrRemarks());
        }*/
    }
    /*********** A. END SERVICE DETAILS ***********/


    /*********** B.1 NEW REPLACEMENT PART DAILOG SETUP ***********/
    private void initNewPartsAddedView(View view) {
        mCardViewNewUpload = (CardView) view.findViewById(R.id.cardViewNewUpload);
        mCardViewNewUpload.setVisibility(View.GONE);
    }

    private void initNewPartsView(View view) {
        mNewPartDialog = initNewPartDialog();
        View dialogView = mNewPartDialog.getCustomView();
        mSpinnerReplacementParts = (Spinner) dialogView.findViewById(R.id.spinnerReplacementParts);
        mSpinnerQuantity = (Spinner) dialogView.findViewById(R.id.spinnerQuantity);
        // mSpinnerUnitPrice = (Spinner) dialogView.findViewById(R.id.spinnerUnitPrice);
        // mSpinnerTotalPrice = (Spinner) dialogView.findViewById(R.id.spinnerTotalPrice);
        mTextViewUnitPrice = (TextView) dialogView.findViewById(R.id.textViewUnitPrice);
        mTextViewTotalPrice = (TextView) dialogView.findViewById(R.id.textViewTotalPrice);
    }

    private void actionNewPartReplacement(MaterialDialog dialog) {
        dialog.dismiss();

        SnackBarNotificationUtil
                .setSnackBar(getActivity().findViewById(android.R.id.content),
                        "Save new part")
                .setColor(getResources().getColor(R.color.colorPrimary1))
                .show();
        new NewPartsCreateOperation().execute(
                mSpinnerReplacementParts.getSelectedItem().toString(),
                mSpinnerQuantity.getSelectedItem().toString(),
                mTextViewUnitPrice.getText().toString(),
                mTextViewTotalPrice.getText().toString()
        );
    }

    private void actionUpdatePartReplacement(MaterialDialog dialog) {
        dialog.dismiss();
        SnackBarNotificationUtil
                .setSnackBar(getActivity().findViewById(android.R.id.content),
                        "Update new part")
                .setColor(getResources().getColor(R.color.colorPrimary1))
                .show();
        mSJPart.setReplacementPartName(mSpinnerReplacementParts.getSelectedItem().toString());
        mSJPart.setQuantity(mSpinnerQuantity.getSelectedItem().toString());
        // mSJPart.setUnitPrice(mSpinnerUnitPrice.getSelectedItem().toString());
        // mSJPart.setTotalPrice(mSpinnerTotalPrice.getSelectedItem().toString());
        mSJPart.setUnitPrice(this.mTextViewUnitPrice.getText().toString());
        mSJPart.setUnitPrice(this.mTextViewTotalPrice.getText().toString());
        new NewPartsUpdateOperation().execute(mSJPart);
    }

    private MaterialDialog initNewPartDialog() {
        boolean wrapInScrollView = false;
        MaterialDialog md = new MaterialDialog.Builder(this.mContext)
                .title("NEW REPLACEMENT PART")
                .customView(R.layout.i_new_replacement_part, wrapInScrollView)
                .negativeText("Save")
                .positiveText("Close")
                .iconRes(R.mipmap.replacepart_icon) // android:background="@mipmap/replacepart_icon"
                .autoDismiss(false)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivityForResult(getPickImageChooserIntent(), 200);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (actionNewReplacement.equals(ADD_NEW_PART_REPLACEMENT)) {
                            actionNewPartReplacement(dialog);
                            showCheckNewReplacementPartAdded();
                        } else if (actionNewReplacement.equals(UPDATE_NEW_PART_REPLACEMENT)) {
                            actionUpdatePartReplacement(dialog);
                        }
                    }

                    private void showCheckNewReplacementPartAdded() {
                        new UIShowAddSuccessTask().execute((Void) null);
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).build();
        return md;
    }

    private void setSpinnerValue(String... spinnerArgs) {
        populateSpinnerPartsValue(spinnerArgs[0]);
        populateSpinnerQuantityValue(spinnerArgs[1]);
        populateSpinnerUnitPriceValue(spinnerArgs[2]);
        populateSpinnerTotalPriceValue(spinnerArgs[3]);
    }

    /**
     * @param compareValue - The actual Value get from SQLite, means local device only
     */
    private void populateSpinnerPartsValue(String compareValue) {
        // 1. Replacement Parts
        ArrayList<String> newPartsList = new ArrayList<>();
        for (ServiceJobNewReplacementPartsRatesWrapper rate : mSJRatesList) {
            newPartsList.add(rate.getPartName());
        }
        //newPartsList.add("Part 2");
        //newPartsList.add("Part 3");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, newPartsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerReplacementParts.setAdapter(adapter);
        if (!compareValue.equals(null)) {
            int spinnerPosition = adapter.getPosition(compareValue);
            mSpinnerReplacementParts.setSelection(spinnerPosition);
        }

        // Listener to Change the Rate of New Parts based on the Selected on the List item
        mSpinnerReplacementParts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                computeUnitPrice(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void computeUnitPrice(int position) {
        // A. Set Unit Price Base
        mTextViewUnitPrice.setText(mSJRatesList.get(position).getUnitPrice());
        // computeTotalPrice(position);

        // B
        double computeTotalPriceByQuantity = 0.00;
        String strQuantity = mSpinnerQuantity.getSelectedItem().toString();
        double quantity = ((strQuantity == "0" || strQuantity == "" || strQuantity == null) ? 0.00 : Double.parseDouble(strQuantity));
        computeTotalPriceByQuantity = (Double.parseDouble(this.mTextViewUnitPrice.getText().toString())) *
                (quantity);
        mTextViewTotalPrice.setText(computeTotalPriceByQuantity+"");

        Log.e(TAG, "Quantity = " + strQuantity + "\nUnit Price=" + mSJRatesList.get(position).getUnitPrice()  +  "\nTotalPrice=" + computeTotalPriceByQuantity);
    }

    private void populateSpinnerQuantityValue(String compareValue) {
        // 2. Quantity
        ArrayList<String> newPartsList = new ArrayList<>();
        newPartsList.add("1");
        newPartsList.add("2");
        newPartsList.add("3");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, newPartsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerQuantity.setAdapter(adapter);
        if (!compareValue.equals(null)) {
            int spinnerPosition = adapter.getPosition(compareValue);
            mSpinnerQuantity.setSelection(spinnerPosition);
        }

        mSpinnerQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                computeTotalPrice(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void computeTotalPrice(int position) {
        // B. Set Total Price based on the poistion of the Spinner and its value from mSJRatesList
        double computeTotalPriceByQuantity = 0;
        String strQuantity = mSpinnerQuantity.getSelectedItem().toString();
        double quantity = ((strQuantity == "0" || strQuantity == "" || strQuantity == null) ? 0.00 : Double.parseDouble(strQuantity));
        String strUnitPrice = this.mTextViewUnitPrice.getText().toString();
        double unitPrice = ((strUnitPrice == "0" || strUnitPrice == "" || strUnitPrice == null) ? 1.00 : Double.parseDouble(strUnitPrice));

        computeTotalPriceByQuantity = (unitPrice) * (quantity);

        mTextViewTotalPrice.setText(computeTotalPriceByQuantity+"");
    }

    private String addTrailingZeroTo2DecimalPoint(String price) {
        if (price.indexOf(".") != -1) {
            String[] decimal = price.split(".");
            if (decimal[1].length() == 2) {

            } else {
                return price + "0";
            }
        }
        return price;
    }


    private void populateSpinnerUnitPriceValue(String compareValue) {
        // 3. Unit Price
        mTextViewUnitPrice.setText(compareValue);
    }

    private void populateSpinnerTotalPriceValue(String compareValue) {
        // 4. Total Price
        this.mTextViewTotalPrice.setText(compareValue);
    }

    /*******************************/
    /*private void setSpinnerValue(String... spinnerArgs) {
        populateSpinnerPartsValue(spinnerArgs[0]);
        populateSpinnerQuantityValue(spinnerArgs[1]);
        populateSpinnerUnitPriceValue(spinnerArgs[2]);
        populateSpinnerTotalPriceValue(spinnerArgs[3]);
    }

    private void populateSpinnerPartsValue(String compareValue) {
        // 1. Replacement Parts
        ArrayList<String> newPartsList = new ArrayList<>();
        newPartsList.add("Part 1");
        newPartsList.add("Part 2");
        newPartsList.add("Part 3");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, newPartsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerReplacementParts.setAdapter(adapter);
        if (!compareValue.equals(null)) {
            int spinnerPosition = adapter.getPosition(compareValue);
            mSpinnerReplacementParts.setSelection(spinnerPosition);
        }
    }

    private void populateSpinnerQuantityValue(String compareValue) {
        // 2. Quantity
        ArrayList<String> newPartsList = new ArrayList<>();
        newPartsList.add("1");
        newPartsList.add("2");
        newPartsList.add("3");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, newPartsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerQuantity.setAdapter(adapter);
        if (!compareValue.equals(null)) {
            int spinnerPosition = adapter.getPosition(compareValue);
            mSpinnerQuantity.setSelection(spinnerPosition);
        }
    }

    private void populateSpinnerUnitPriceValue(String compareValue) {
        // 3. Unit Price
        ArrayList<String> newPartsList = new ArrayList<>();
        newPartsList.add("5000");
        newPartsList.add("10000");
        newPartsList.add("15000");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, newPartsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerUnitPrice.setAdapter(adapter);
        if (!compareValue.equals(null)) {
            int spinnerPosition = adapter.getPosition(compareValue);
            mSpinnerUnitPrice.setSelection(spinnerPosition);
        }
    }

    private void populateSpinnerTotalPriceValue(String compareValue) {
        // 4. Total Price
        ArrayList<String> newPartsList = new ArrayList<>();
        newPartsList.add("10000");
        newPartsList.add("20000");
        newPartsList.add("30000");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, newPartsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTotalPrice.setAdapter(adapter);
        if (!compareValue.equals(null)) {
            int spinnerPosition = adapter.getPosition(compareValue);
            mSpinnerTotalPrice.setSelection(spinnerPosition);
        }
    }*/
    /*******************************/

    private class NewPartsUpdateOperation extends AsyncTask<ServiceJobNewPartsWrapper, Void, ServiceJobNewPartsWrapper> {
        @Override
        protected void onPreExecute() {
            mButtonViewUploadFileNew.setVisibility(View.GONE);
            mProgressBarUploadingNew.setVisibility(View.VISIBLE);
        }

        @Override
        protected ServiceJobNewPartsWrapper doInBackground(ServiceJobNewPartsWrapper... newPartsArgs) {
            if (newPartsArgs.length != 0) {
                ServiceJobNewPartsWrapper sjUp = newPartsArgs[0]; // Prepare record data
                Log.d(TAG, sjUp.toString());
                return sjUp;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ServiceJobNewPartsWrapper result) {
            if (result != null) {
                mPartsDB.open(); // Save data to DB
                mPartsDB.editPart(result, "FRAGMENT2");
                mPartsDB.close();

                SnackBarNotificationUtil
                        .setSnackBar(getActivity().findViewById(android.R.id.content),
                                "Replacement Updated")
                        .setColor(getResources().getColor(R.color.colorPrimary1))
                        .show();

                populateUploadsCardList();
            } else {
                SnackBarNotificationUtil
                        .setSnackBar(getActivity().findViewById(android.R.id.content),
                                "Unable to save the signature")
                        .setColor(getResources().getColor(R.color.colorPrimary1))
                        .show();
            }

            if (mProgressBarUploadingNew.isShown()) {
                mButtonViewUploadFileNew.setVisibility(View.VISIBLE);
                mProgressBarUploadingNew.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class NewPartsCreateOperation extends AsyncTask<String, Void, ServiceJobNewPartsWrapper> {
        @Override
        protected void onPreExecute() {
            mButtonViewUploadFileNew.setVisibility(View.GONE);
            mProgressBarUploadingNew.setVisibility(View.VISIBLE);
        }

        @Override
        protected ServiceJobNewPartsWrapper doInBackground(String... newPartsArgs) {
            if (newPartsArgs.length != 0) {
                ServiceJobNewPartsWrapper sjUp = new ServiceJobNewPartsWrapper(); // Prepare record data
                sjUp.setServiceId(mServiceID);
                sjUp.setReplacementPartName(newPartsArgs[0]);
                sjUp.setQuantity(newPartsArgs[1]);
                sjUp.setUnitPrice(newPartsArgs[2]);
                sjUp.setTotalPrice(newPartsArgs[3]);
                return sjUp;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ServiceJobNewPartsWrapper result) {
            if (result != null) {
                mPartsDB.open(); // Save data to DB
                mPartsDB.addNewPart(result, "FRAGMENT2");
                mPartsDB.close();

                SnackBarNotificationUtil
                        .setSnackBar(getActivity().findViewById(android.R.id.content),
                                "New Replacement Added.")
                        .setColor(getResources().getColor(R.color.colorPrimary1))
                        .show();
                populateUploadsCardList();
            } else {
                SnackBarNotificationUtil
                        .setSnackBar(getActivity().findViewById(android.R.id.content),
                                "Unable to save the signature")
                        .setColor(getResources().getColor(R.color.colorPrimary1))
                        .show();
            }

            if (mProgressBarUploadingNew.isShown()) {
                mButtonViewUploadFileNew.setVisibility(View.VISIBLE);
                mProgressBarUploadingNew.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    private void showUploadDialog(ServiceJobNewPartsWrapper sjParts) {
        this.mSJPart = sjParts;
        setSpinnerValue(sjParts.getPartName(), sjParts.getQuantity(), sjParts.getUnitPrice(), sjParts.getTotalPrice());
        actionNewReplacement = UPDATE_NEW_PART_REPLACEMENT; // Set action whether to Update or Add
        mNewPartDialog.show();
    }

    public void fromActivity_onNewPartsEntryAdded(String fileName) {
        populateUploadsCardList();
    }

    public void fromActivity_onPartsEntryRenamed(String fileName) {

    }

    public void fromActivity_onPartsEntryDeleted() {
        populateUploadsCardList();
    }


    public void fromActivity_onHandlePartsSelection(
            int position, ServiceJobNewPartsWrapper serviceJobNewPartsWrapper, int mode) {

    }

    public void fromActivity_onHandleDeletePartsFromListSelection(final int id) {
        new MaterialDialog.Builder(this.mContext)
                .title("COMFIRM DELETE REPLACEMENT PART.")
                .positiveText("Delete")
                .negativeText("Close")
                .iconRes(R.mipmap.del_icon)
                .autoDismiss(false)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mPartsDB = new PartsSJDBUtil(getActivity());
                        mPartsDB.open();
                        mPartsDB.removeItemWithId(id);
                        mPartsDB.close();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void fromActivity_onHandleViewPartFromListSelection(ServiceJobNewPartsWrapper serviceJobPartWrapper) {
        showUploadDialog(serviceJobPartWrapper);
    }


    // Show the SuccessPrompt within 5 seconds
    public class UIShowAddSuccessTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            mCardViewNewUpload.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mCardViewNewUpload.setVisibility(View.GONE);
        }

        @Override
        protected void onCancelled() {
        }
    }
    /*********** B.1 END NEW REPLACEMENT PART SETUP ***********/


    /*********** B. CAMERA SETUP ***********/
    public void setUpUploadsRecyclerView(View view) {
        mUploadResultsList = (RecyclerView) view.findViewById(R.id.upload_file_results_service_job_list);
    }

    public void setupUploadsResultsList() {
        mUploadListAdapter = new ServiceJobPartsListAdapter(getActivity());
        mUploadResultsList.setAdapter(mUploadListAdapter);
        mUploadResultsList.setLayoutManager(new LinearLayoutManager(this.mContext));
    }

    private void populateUploadsCardList() {
        mPartsDB = new PartsSJDBUtil(this.mContext);
        mPartsDB.open();
        mUploadResults = mPartsDB.getAllPartsBySJID(mServiceID);
        mPartsDB.close();

        if (mUploadResults != null) {
            for (int i = 0; i < mUploadResults.size(); i++) {
                Log.e(TAG, "DATA: " + mUploadResults.get(i).toString());
            }

            mUploadResultsList.setHasFixedSize(true);
            mUploadResultsList.setLayoutManager(new LinearLayoutManager(this.mContext));
            mUploadResultsList.setItemAnimator(new DefaultItemAnimator());
            mUploadListAdapter.swapData(mUploadResults);
            /*new UIThreadHandler(getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });*/
        }
    }

    /**
     * Create a chooser intent to select the source to get image from.<br/>
     * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the intent chooser.
     */
    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getActivity().getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);
        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getActivity().getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        Log.d("getImage", getImage.getAbsolutePath());
        Log.d("outputFileUri", outputFileUri.getPath());
        return outputFileUri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

        }
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    /**
     * Get the URI of the selected image from {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera and gallery image.
     *
     * @param data the returned data of the activity aResponse
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this.mContext)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (hasPermission(perms)) {
                    } else {
                        permissionsRejected.add(perms);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                                //Log.d("API123", "permisionrejected " + permissionsRejected.size());

                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }
    }


    /*********** B. END CAMERA SETUP ***********/
}
