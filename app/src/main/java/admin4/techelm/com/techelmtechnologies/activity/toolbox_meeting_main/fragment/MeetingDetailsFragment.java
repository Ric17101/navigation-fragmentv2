package admin4.techelm.com.techelmtechnologies.activity.toolbox_meeting_main.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.gcacace.signaturepad.views.SignaturePad;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b2.ProjectJobLastFormFragment;
import admin4.techelm.com.techelmtechnologies.utility.PermissionUtil;
import admin4.techelm.com.techelmtechnologies.utility.SignatureImageButtonUtil;
import admin4.techelm.com.techelmtechnologies.utility.SignatureUtil;
import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;

/**
 *
 */
public class MeetingDetailsFragment extends Fragment {

    private Context mContext;
    private boolean hasSignature;

    private SignaturePad mSignaturePad;
    private SignatureUtil mSignatureUtil;

    private Spinner dropdown;

    private ImageButton imageButtonViewSignatureMeeting;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.meeting_details, null);

        this.mContext = container.getContext();

        initButton(view);
        initFormView(view);

        return view;
    }
    /**
     * These Two Lines should be included on every Fragment to maintain the state and do not load again
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("remarks, currently under construction");
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        System.out.println("remarks, currently under construction");
    }

    private void initButton(View view) {
        /** BUTTON BACK */
        Button button_back = (Button) view.findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(AddReplacementPart_FRGMT_3.this, PartReplacement_FRGMT_2.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, mServiceJobFromBundle));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);*/
                ((ToolboxMeetingPagerActivity) getActivity()).fromFragmentNavigate(-1);
            }
        });

        /** BUTTON NEXT */
        Button button_next = (Button) view.findViewById(R.id.button_next);
        button_next.setText("SAVE");
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(AddReplacementPart_FRGMT_3.this, SigningOff_FRGMT_4.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, mServiceJobFromBundle));
                overridePendingTransition(R.anim.enter, R.anim.exit);*/
                ((ToolboxMeetingPagerActivity) getActivity()).fromFragmentNavigate(1);
            }
        });
    }

    private void initFormView(final View view) {
        // Sub Contractor Signature setup
        imageButtonViewSignatureMeeting = (ImageButton) view.findViewById(R.id.imageButtonViewSignature);
        imageButtonViewSignatureMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog md = showSigningDialog(view);
            }
        });

        dropdown = (Spinner)view.findViewById(R.id.spinnerMonth);
        String[] items = new String[]{"January", "February", "March","April", "May", "June","July", "August", "September","October", "November", "December"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.mContext, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    private void initPermissions() {
        PermissionUtil.verrifyReadStoragePermissions(getActivity());
        PermissionUtil.verrifyWriteStoragePermissions(getActivity());
    }

    /************ A. SIGNATURE **************/
    private MaterialDialog showSigningDialog(View view) {
        mSignatureUtil = new SignatureUtil(getActivity(), mSignaturePad);

        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .title("Meeting Signature")
                .customView(R.layout.m_signing_off_signature, false)
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
                        clearSignaturePad(dialog);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        saveSignatureToGallery(dialog);
                    }
                })
                .show();

        return materialDialog;
    }

    private void clearSignaturePad(MaterialDialog dialog) {
        mSignaturePad = (SignaturePad) dialog.getCustomView().findViewById(R.id.signature_pad);
        mSignaturePad.clear();
        mSignatureUtil = new SignatureUtil(getActivity(), mSignaturePad);
        SignatureImageButtonUtil.setClearImageSignature(getResources(), imageButtonViewSignatureMeeting);
    }

    private void saveSignatureToGallery(MaterialDialog dialog) {
        mSignaturePad = (SignaturePad) dialog.getCustomView().findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() { }

            @Override
            public void onSigned() { }

            @Override
            public void onClear() { }
        });

        Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();

        if (mSignatureUtil.addJpgSignatureToGallery(signatureBitmap, "signature")) {
            SnackBarNotificationUtil
                    .setSnackBar(getActivity().findViewById(android.R.id.content),
                            "Signature saved into the Gallery")
                    .setColor(getResources().getColor(R.color.colorPrimary1))
                    .show();
            SignatureImageButtonUtil.setDrawableImageSignature(getResources(),
                    imageButtonViewSignatureMeeting, mSignatureUtil.loadBitmap());
            dialog.dismiss();
            setHasSignature(true);
        } else {
            SnackBarNotificationUtil
                    .setSnackBar(getActivity().findViewById(android.R.id.content),
                            "Unable to save signature")
                    .setColor(getResources().getColor(R.color.colorPrimary1))
                    .show();
            setHasSignature(false);
        }
    }

    public void setHasSignature(boolean hasUpload) {
        this.hasSignature = hasUpload;
    }

    /************ A. END SIGNATURE **************/

}
