package admin4.techelm.com.techelmtechnologies.activity.toolbox_meeting_main.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.gcacace.signaturepad.views.SignaturePad;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.toolboxmeeting.ToolboxMeetingWrapper;
import admin4.techelm.com.techelmtechnologies.utility.PermissionUtil;
import admin4.techelm.com.techelmtechnologies.utility.SignatureImageButtonUtil;
import admin4.techelm.com.techelmtechnologies.utility.SignatureUtil;
import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;
import admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm.UploadFile_VolleyPOST;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.TOOLBOXMEETING_MEETING_DETAILS_UPLOAD_URL;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.TOOLBOX_MEETING_KEY;

/**
 *
 */
public class MeetingDetailsFragment extends Fragment {

    private Context mContext;
    private boolean hasSignature;

    private SignaturePad mSignaturePad;
    private SignatureUtil mSignatureUtil;

    private ImageButton imageButtonViewSignatureMeeting;

    private EditText meeting_details;
    private String md_Content;
    private ToolboxMeetingWrapper toolboxMeetingWrapper;

    public static MeetingDetailsFragment newInstance(ToolboxMeetingWrapper projectJobWrapper) {
        MeetingDetailsFragment fragment = new MeetingDetailsFragment();
        Bundle args = new Bundle();

        args.putParcelable(TOOLBOX_MEETING_KEY, projectJobWrapper);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        fromBundle();
    }

    private void fromBundle() {
        this.toolboxMeetingWrapper = getArguments().getParcelable(TOOLBOX_MEETING_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.meeting_details, null);

        this.mContext = container.getContext();

        initButton(view);
        initFormView(view);

        initPermissions();

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
                uploadSignature();
                ((ToolboxMeetingPagerActivity) getActivity()).backToToolboxLandingPage(5);
            }
        });
    }

    private void initFormView(final View view) {
        // Sub Contractor Signature setup
        meeting_details = (EditText) view.findViewById(R.id.editTextMeetingDetails);

        imageButtonViewSignatureMeeting = (ImageButton) view.findViewById(R.id.imageButtonViewSignature);
        imageButtonViewSignatureMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog md = showSigningDialog(view);
            }
        });


    }

    private void initPermissions() {
        PermissionUtil.initPermissions(getActivity());
        //PermissionUtil.verrifyWriteStoragePermissions(getActivity());
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
        hasSignature = true;

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

    private void uploadSignature() {
        UploadFile_VolleyPOST post = new UploadFile_VolleyPOST();
        md_Content = meeting_details.getText().toString();
        post.setContext(this.mContext)
                .setLink(TOOLBOXMEETING_MEETING_DETAILS_UPLOAD_URL)
                .addParam("projectjob_id", toolboxMeetingWrapper.getID()+"")
                .addParam("meeting_details", md_Content);

        if(hasSignature){
            Log.wtf("SIGNATURE: ", hasSignature + "");
            post.addParam("signature", "true")
                    .addImageFile(this.mSignatureUtil.getFile(), this.mSignatureUtil.getFile().getName(), "image/jpeg");
        }
        else{
            Log.wtf("SIGNATURE: ","FALSE");
            post.addParam("signature", "false");
        }

        post.setOnEventListener(new UploadFile_VolleyPOST.OnEventListener() {
            @Override
            public void onError(String msg, int success) {
                Log.e("MEETING DETAILS", "Message " + msg + " Error:" + success);
            }

            @Override
            public void onSuccess(String msg, int success) {
                Log.e("MEETING DETAILS", "Message " + msg + " Success:" + success);
                //uploadTask.sleep();
            }
        });

        post.startUpload();
    }

}
