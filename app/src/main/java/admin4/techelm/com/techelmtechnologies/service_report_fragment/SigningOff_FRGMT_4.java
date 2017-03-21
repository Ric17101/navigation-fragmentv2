package admin4.techelm.com.techelmtechnologies.service_report_fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.gcacace.signaturepad.views.SignaturePad;

import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.db.ServiceJobDBUtil;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.servicejob.PopulateServiceJobViewDetails;
import admin4.techelm.com.techelmtechnologies.utility.SignatureUtil;

public class SigningOff_FRGMT_4 extends Fragment
         {

    private static final String TAG = SigningOff_FRGMT_4.class.getSimpleName();
    private Context mContext;

    // A. SERVICE ID INFO
    private static final String RECORD_JOB_SERVICE_KEY = "SERVICE_JOB";
    private static ServiceJobWrapper mServiceJobFromBundle; // From Calling Activity
    private ServiceJobDBUtil mSJDB;
    private List<ServiceJobWrapper> mSJResultList = null;

    // B. SIGNATURE PAD
    private RelativeLayout mRelativeLayout;

    private ImageButton imageButtonViewSignature;
    private SignaturePad mSignaturePad;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final String LOG_TAG = "TaskCompleted_5";
    private int mServiceID; // For DB Purpose to save the file on the ServiceID


    // SlidingPager Tab Set Up
    private static final String ARG_POSITION = "position";
    private int position;

    public static SigningOff_FRGMT_4 newInstance(int position, ServiceJobWrapper serviceJob) {
        SigningOff_FRGMT_4 frag = new SigningOff_FRGMT_4();
        Bundle args = new Bundle();

        args.putInt(ARG_POSITION, position);
        frag.setArguments(args);

        mServiceJobFromBundle = serviceJob;
        return (frag);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_signing_off_end_task, container, false);

        this.mContext = container.getContext();

        initSignaturePadPopUp(view);
        initButton(view);

        if (this.mServiceJobFromBundle != null) { // if Null don't show anything
            mServiceID = mServiceJobFromBundle.getID();

            // ServiceJob Details
            new PopulateServiceJobViewDetails()
                    .populateServiceJobDetails(
                            view, //view.findViewById(android.R.id.content),
                            mServiceJobFromBundle,
                            View.GONE,
                            TAG);
        }

        return view;
    }

    private void initButton(View view) {
        /** BUTTON BACK */
        Button button_back = (Button) view.findViewById(R.id.button_back);
        button_back.setVisibility(View.GONE);

        /** BUTTON NEXT */
        Button button_next = (Button) view.findViewById(R.id.button_next);
        button_next.setText("END TASK");
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSubmitSuccess()) {
                    Intent intent = new Intent(getActivity(), ServiceReport_TaskCompleted_5.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.enter, R.anim.exit).toBundle();
                    getActivity().startActivity(intent, bundle);
                    getActivity().finish();
                }
            }
        });

        /** BUTTON VIEW DETAILS */
        ImageButton buttonViewDetails = (ImageButton) view.findViewById(R.id.buttonViewDetails);
        buttonViewDetails.setVisibility(View.GONE);
    }

    public MaterialDialog showSigningDialog(View view) {
        final SignatureUtil sign = new SignatureUtil(getActivity(), mSignaturePad);
        boolean wrapInScrollView = false;
        MaterialDialog md = new MaterialDialog.Builder(this.mContext)
                .title("SIGNATURE.")
                .customView(R.layout.m_signing_off_signature, wrapInScrollView)
                .positiveText("Close")
                .neutralText("Clear")
                .negativeText("Save")
                .iconRes(R.drawable.composea)
                .autoDismiss(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mSignaturePad = null;
                        dialog.dismiss();
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mSignaturePad = (SignaturePad) dialog.getCustomView().findViewById(R.id.signature_pad);
                        mSignaturePad.clear();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mSignaturePad = (SignaturePad) dialog.getCustomView().findViewById(R.id.signature_pad);
                        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
                            @Override
                            public void onStartSigning() {
                                // Toast.makeText(SigningOff_FRGMT_4.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSigned() {
                                /*mSaveButton.setEnabled(true);
                                mClearButton.setEnabled(true);*/
                            }

                            @Override
                            public void onClear() {
                                /*mSaveButton.setEnabled(false);
                                mClearButton.setEnabled(false);*/
                            }
                        });
                        Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                        // TODO: Add AsyncTask Here...
                        if (sign.addJpgSignatureToGallery(signatureBitmap, "signature")) {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Signature saved into the Gallery:" + sign.getFilePath(), Snackbar.LENGTH_LONG)
                                    .setAction("OK", null).show();

                            saveSignatureToDBOnAfterSaveToGallery(sign); // SAVING FILES to DB

                            setDrawableImageSignature(sign.loadBitmap());
                            dialog.dismiss();
                        } else {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Unable to store the signature", Snackbar.LENGTH_LONG)
                                    .setAction("OK", null).show();
                        }

                        /*if (sign.addSvgSignatureToGallery(mSignaturePad.getSignatureSvg())) {
                            Toast.makeText(SigningOff_FRGMT_4.this, "SVG Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SigningOff_FRGMT_4.this, "Unable to store the SVG signature", Toast.LENGTH_SHORT).show();
                        }*/
                    }
                })
                .show();

        return md;
        /*new MaterialDialog.Builder(this)
                .title(getString(R.string.about_dialog_title, VERSION))
                .positiveText(R.string.dismiss)
                .content(Html.fromHtml(getString(R.string.about_body)))
                .iconRes(R.drawable.ic_translate)
                .show();*/
    }

    private void initSignaturePadPopUp(View view) {
        imageButtonViewSignature = (ImageButton) view.findViewById(R.id.imageButtonViewSignature);
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl);
        // Set a click listener for the text view
        imageButtonViewSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog mDialog = showSigningDialog(view);

                /*ImageButton btnHybrid = (ImageButton) vDialog.findViewById(R.id.ib_close);
                btnHybrid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("hybrid", "called");
                        mPopupWindow.dismiss();
                    }
                });*/

                // initSignatureFunction(mDialog);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "Cannot write images to external storage",
                            Snackbar.LENGTH_LONG)
                            .setAction("OK", null).show();
                    // Toast.makeText(SigningOff_FRGMT_4.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void setDrawableImageSignature(Bitmap drawableImageSignature) {
        BitmapDrawable ob = new BitmapDrawable(getResources(), drawableImageSignature);
        imageButtonViewSignature.setBackgroundDrawable(ob);
    }

    /*********** A. SERVICE DETAILS ***********/
    public void fromActivity_onNewSJEntryAdded(String serviceNum) {
        System.out.print("onNewSJEntryAdded: Called");
    }
    public void fromActivity_onSJEntryRenamed(String fileName) {
        System.out.print("onSJEntryRenamed: Called");
    }
    public void fromActivity_onSJEntryDeleted() {
        System.out.print("onSJEntryDeleted: Called");
    }

    /*private void populateServiceJobDetails(int serviceID) {

        // SERVICE JOB Controls
        TextView textViewLabelCustomerName = (TextView) findViewById(R.id.textViewLabelCustomerName);
        TextView textViewLabelJobSite = (TextView) findViewById(R.id.textViewLabelJobSite);
        TextView textViewLabelServiceNo = (TextView) findViewById(R.id.textViewLabelServiceNo);
        TextView textViewLabelTypeOfService = (TextView) findViewById(R.id.textViewLabelTypeOfService);
        TextView textViewLabelTelephone = (TextView) findViewById(R.id.textViewLabelTelephone);
        TextView textViewLabelFax = (TextView) findViewById(R.id.textViewLabelFax);
        TextView textViewLabelEquipmentType = (TextView) findViewById(R.id.textViewLabelEquipmentType);
        TextView textViewLabelModel = (TextView) findViewById(R.id.textViewLabelModel);
        TextView textViewComplaints = (TextView) findViewById(R.id.textViewComplaints);
        TextView textViewRemarksActions = (TextView) findViewById(R.id.textViewRemarksActions);

        mSJDB = new ServiceJobDBUtil(SigningOff_FRGMT_4.this);
        mSJDB.open();
        mSJResultList = mSJDB.getAllJSDetailsByID(serviceID);
        mSJDB.close();

        for (int i = 0; i < mSJResultList.size(); i++) {
            Log.e(LOG_TAG, "DATA: " + mSJResultList.get(i).toString());
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
        }
    }*/
    /*********** A. END SERVICE DETAILS ***********/

    private void saveSignatureToDBOnAfterSaveToGallery(SignatureUtil sign) {
        mSJDB = new ServiceJobDBUtil(getActivity());
        mSJDB.open();
        mSJDB.updateRequestIDSignature(mServiceID, sign.getFilePath(), sign.getFilePath());
        mSJDB.close();
    }

    private boolean onSubmitSuccess() {
        // TODO: Save this to the Server, including the
        // Recording, Signature, Image uploaded, New replacement Parts

        // mServiceJobFromBundle
        // mServiceID

        return true;
    }

}
