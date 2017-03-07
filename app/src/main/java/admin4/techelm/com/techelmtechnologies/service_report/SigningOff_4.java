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
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.gcacace.signaturepad.views.SignaturePad;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.utility.SignatureUtil;

public class SigningOff_4 extends AppCompatActivity {

    private RelativeLayout mRelativeLayout;

    private ImageButton imageButtonViewSignature;
    private SignaturePad mSignaturePad;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private Button mClearButton;
    private Button mSaveButton;
    private View mPopUpSignature;
    private PopupWindow mPopupWindow; // TODO : Cancel this onBackPress

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing_off_end_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initButton();

        initSignaturePadPopUp();
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
                .neutralText("Save")
                .negativeText("Clear")
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
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mSignaturePad = (SignaturePad) dialog.getCustomView().findViewById(R.id.signature_pad);
                        mSignaturePad.clear();
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


}
