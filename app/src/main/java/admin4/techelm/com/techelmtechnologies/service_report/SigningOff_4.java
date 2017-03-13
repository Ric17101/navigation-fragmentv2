package admin4.techelm.com.techelmtechnologies.service_report;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.gcacace.signaturepad.views.SignaturePad;

import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.db.ServiceJobDBUtil;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.utility.SignatureUtil;

public class SigningOff_4 extends AppCompatActivity implements
        ServiceJobDBUtil.OnDatabaseChangedListener {

    // A. SERVICE ID INFO
    ServiceJobDBUtil mSJDB;
    private List<ServiceJobWrapper> mSJResultList = null;

    // B. SIGNATURE PAD
    private RelativeLayout mRelativeLayout;

    private ImageButton imageButtonViewSignature;
    private SignaturePad mSignaturePad;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private Button mClearButton;
    private Button mSaveButton;
    private View mPopUpSignature;
    private PopupWindow mPopupWindow; // TODO : Cancel this onBackPress

    private static final String LOG_TAG = "TaskCompleted_5";
    private int mServiceID; // For DB Purpose to save the file on the ServiceID



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing_off_end_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initButton();

        initSignaturePadPopUp();

        // mServiceID = savedInstanceState.getInt(RECORD_SERVICE_KEY);
        mServiceID = 2;
        populateServiceJobDetails(mServiceID);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        // TODO: Notify user Here that he is already finished submitting the data
    }

    private Point getWindowSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        /**If you're not in an Activity you can get the default Display via WINDOW_SERVICE: */
        /*WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();*/
        return size;
    }
    private void initButton() {
        /** BUTTON BACK */
        Button button_back = (Button) findViewById(R.id.button_back);
        button_back.setVisibility(View.GONE);

        /** BUTTON NEXT */
        Button button_next = (Button) findViewById(R.id.button_next);
        button_next.setText("END TASK");
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigningOff_4.this, ServiceReport_TaskCompleted_5.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        /** BUTTON VIEW DETAILS */
        ImageButton buttonViewDetails = (ImageButton) findViewById(R.id.buttonViewDetails);
        buttonViewDetails.setVisibility(View.GONE);
    }

    public MaterialDialog showRecordDialog() {
        final SignatureUtil sign = new SignatureUtil(SigningOff_4.this, mSignaturePad);
        boolean wrapInScrollView = false;
        MaterialDialog md = new MaterialDialog.Builder(this)
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
                                Toast.makeText(SigningOff_4.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(SigningOff_4.this, "Signature saved into the Gallery:" + sign.getFilePath(),
                                    Toast.LENGTH_SHORT).show();
                            setDrawableImageSignature(sign.loadBitmap());
                        } else {
                            Toast.makeText(SigningOff_4.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                        }

                        /*if (sign.addSvgSignatureToGallery(mSignaturePad.getSignatureSvg())) {
                            Toast.makeText(SigningOff_4.this, "SVG Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SigningOff_4.this, "Unable to store the SVG signature", Toast.LENGTH_SHORT).show();
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

    private void initSignaturePadPopUp() {
        imageButtonViewSignature = (ImageButton) findViewById(R.id.imageButtonViewSignature);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        // Set a click listener for the text view
        imageButtonViewSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog mDialog = showRecordDialog();

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
                    Toast.makeText(SigningOff_4.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void setDrawableImageSignature(Bitmap drawableImageSignature) {
        BitmapDrawable ob = new BitmapDrawable(getResources(), drawableImageSignature);
        imageButtonViewSignature.setBackgroundDrawable(ob);
    }

    /*private void initSignaturePadPopUp2() {
        // Get the widgets reference from XML layout
        final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customView = inflater.inflate(R.layout.m_signing_off_signature, null);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        imageButtonViewSignature = (ImageButton) findViewById(R.id.imageButtonViewSignature);

        // Set a click listener for the text view
        imageButtonViewSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize a new instance of LayoutInflater service


                // Inflate the custom layout/view


                // Initialize a new instance of popup window
                mPopupWindow = new PopupWindow(
                        customView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                // Get a reference for the custom view close button
                ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);

                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });

                // Finally, show the popup window at the center location of root relative layout
                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);
            }
        });
    }*/
    /*********** A. SERVICE DETAILS ***********/
    @Override
    public void onNewSJEntryAdded(String serviceNum) {

    }

    @Override
    public void onSJEntryRenamed(String fileName) {

    }

    @Override
    public void onSJEntryDeleted() {

    }

    private void populateServiceJobDetails(int serviceID) {

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

        mSJDB = new ServiceJobDBUtil(SigningOff_4.this);
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
    }
    /*********** A. END SERVICE DETAILS ***********/

}
