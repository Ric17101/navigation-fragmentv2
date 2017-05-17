package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b2;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.ProjectJobViewPagerActivity;
import admin4.techelm.com.techelmtechnologies.db.servicejob.UploadsSJDBUtil;
import admin4.techelm.com.techelmtechnologies.model.projectjob.ProjectJobWrapper;
import admin4.techelm.com.techelmtechnologies.model.projectjob.b1.PISSTaskWrapper;
import admin4.techelm.com.techelmtechnologies.model.projectjob.b2.IPI_Wrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobUploadsWrapper;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;
import admin4.techelm.com.techelmtechnologies.utility.SignatureImageButtonUtil;
import admin4.techelm.com.techelmtechnologies.utility.SignatureUtil;
import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;
import admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm.UploadFile_VolleyPOST;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_SAVE_IPI_TASK_FORM_C_URL;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_UPLOAD_CAPTURE_URL;

/**
 * Created by Admin4 on 05/05/2017.
 * AFTER BUTTON SAVE in B2 and B3
 */
public class ProjectJobLastFormFragment extends Fragment {

    private static final String TAG = ProjectJobLastFormFragment.class.getSimpleName();
    private Context mContext;
    private boolean hasSignatureSubContractor;
    private boolean hasSignatureDispositionendBy;

    private enum Signature {
        SUBCONTRACTOR,
        DISPOSITION
    }
    private SignaturePad mSignaturePad;
    private SignatureUtil mSignatureUtil;

    private ImageButton imageButtonViewSignatureSubContractor;
    private ImageButton imageButtonViewSignatureDisposition;

    // Instance Variables;
    ProjectJobWrapper mProjectJob;
    // IPI_TaskFinalWrapper mTask;

    public static ProjectJobLastFormFragment newInstance(ProjectJobWrapper project) {
        ProjectJobLastFormFragment fragment = new ProjectJobLastFormFragment();
        Bundle args = new Bundle();

        args.putParcelable(PROJECT_JOB_KEY, project);
        //args.putParcelable(PROJECT_JOB_IPI_FINAL_TASK_KEY, task);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fromBundle();
    }

    private void fromBundle() {
        this.mProjectJob = getArguments().getParcelable(PROJECT_JOB_KEY);
        //this.mTask = getArguments().getParcelable(PROJECT_JOB_IPI_FINAL_TASK_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.content_b2_last_form_signature, container, false);

        this.mContext = container.getContext();

        initButton(view);

        initFormView(view);

        return view;
    }

    /**
     * These Two Lines should be included on every Fragment to maintain the state and donnot load again
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("Last Form, currently under construction");
    }
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        System.out.println("Last Form, currently under construction");
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
                ((ProjectJobViewPagerActivity) getActivity()).onBackPress();

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
                ((ProjectJobViewPagerActivity)getActivity()).fromFragmentNavigate(1);
            }
        });
    }

    private void initFormView(final View view) {
        // Sub Contractor Signature setup
        LinearLayout linearLayoutSubContractor = (LinearLayout) view.findViewById(R.id.linearLayoutSubContractor);
        imageButtonViewSignatureSubContractor = (ImageButton) linearLayoutSubContractor.findViewById(R.id.imageButtonViewSignature);
        imageButtonViewSignatureSubContractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog md = showSigningDialog(view, Signature.SUBCONTRACTOR);
            }
        });
        TextView textViewTitleCommentRemarksSubContractor = (TextView) linearLayoutSubContractor.findViewById(R.id.textViewTitleCommentRemarks);
        textViewTitleCommentRemarksSubContractor.setText("Sub Contractor Signature");

        // Disposition Signature setup
        LinearLayout linearLayoutDispositionedBy = (LinearLayout) view.findViewById(R.id.linearLayoutDispositionedBy);
        imageButtonViewSignatureDisposition = (ImageButton) linearLayoutDispositionedBy.findViewById(R.id.imageButtonViewSignature);
        imageButtonViewSignatureDisposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog md = showSigningDialog(view, Signature.DISPOSITION);
            }
        });
        TextView textViewTitleCommentRemarksDisposition = (TextView) linearLayoutDispositionedBy.findViewById(R.id.textViewTitleCommentRemarks);
        textViewTitleCommentRemarksDisposition.setText("Disposition Signature");
    }

    /************ A. SIGNATURE **************/
    private MaterialDialog showSigningDialog(View view, final Signature mode) {
        mSignatureUtil = new SignatureUtil(getActivity(), mSignaturePad);

        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .title(setSignatureModalTitle(mode))
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
                        clearSignaturePad(dialog, mode);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        saveSignatureToGallery(dialog, mode, mSignatureUtil);
                    }
                })
                .show();

        return materialDialog;
    }

    private String setSignatureModalTitle(Signature mode) {
        String title = "";
        switch (mode) {
            case DISPOSITION :
                title = "Disposition Signature";
                break;
            case SUBCONTRACTOR :
                title = "Sub contractor Signature";
                break;
        }
        return title;
    }

    private void clearSignaturePad(MaterialDialog dialog, Signature mode) {
        mSignaturePad = (SignaturePad) dialog.getCustomView().findViewById(R.id.signature_pad);
        mSignaturePad.clear();
        mSignatureUtil = new SignatureUtil(getActivity(), mSignaturePad);

        switch (mode) {
            case DISPOSITION :
                SignatureImageButtonUtil.setClearImageSignature(getResources(), imageButtonViewSignatureDisposition);
                break;
            case SUBCONTRACTOR :
                SignatureImageButtonUtil.setClearImageSignature(getResources(), imageButtonViewSignatureSubContractor);
                break;
        }
    }

    private void saveSignatureToGallery(MaterialDialog dialog, Signature mode, SignatureUtil mSignatureUtil) {
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

        if (this.mSignatureUtil.addJpgSignatureToGallery(signatureBitmap, "signature")) {
            SnackBarNotificationUtil
                .setSnackBar(getActivity().findViewById(android.R.id.content),
                        "Signature saved into the Gallery")
                .setColor(getResources().getColor(R.color.colorPrimary1))
                .show();

            switch (mode) {
                case DISPOSITION :
                    SignatureImageButtonUtil.setDrawableImageSignature(getResources(),
                            imageButtonViewSignatureDisposition, this.mSignatureUtil.loadBitmap());
                    break;
                case SUBCONTRACTOR:
                    SignatureImageButtonUtil.setDrawableImageSignature(getResources(),
                            imageButtonViewSignatureSubContractor, this.mSignatureUtil.loadBitmap());
                    break;
            }
            dialog.dismiss();

            // Set if has signed
            switch (mode) {
                case DISPOSITION : setHasSignatureDispositionendBy(true); break;
                case SUBCONTRACTOR : setHasSignatureSubContractor(true); break;
            }

        } else {
            SnackBarNotificationUtil
                .setSnackBar(getActivity().findViewById(android.R.id.content),
                        "Unable to save signature")
                .setColor(getResources().getColor(R.color.colorPrimary1))
                .show();

            // Set if has signed
            switch (mode) {
                case DISPOSITION : setHasSignatureDispositionendBy(false); break;
                case SUBCONTRACTOR : setHasSignatureSubContractor(false); break;
            }
        }
    }

    public void setHasSignatureSubContractor(boolean hasUpload) {
        this.hasSignatureSubContractor = hasUpload;
    }
    public void setHasSignatureDispositionendBy(boolean hasUpload) {
        this.hasSignatureDispositionendBy = hasUpload;
    }

    /************ A. END SIGNATURE **************/

    private class SaveSignatureTask extends AsyncTask<Void, Void, String> {
        private IPI_Wrapper task;
        private View cView;
        private boolean hasEdited = false;
        private Signature signMode;

        public SaveSignatureTask newInstance(IPI_Wrapper ipiWrapper, View view, Signature mode) {
            Log.e(TAG, "Im on the newInstance00");
            this.task = ipiWrapper;
            this.cView = view;
            return this;
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.e(TAG, "Im on the doInBackground");

            return hasEdited + "";
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e(TAG, "Im on the onPostExecute " + s);
            Log.e(TAG, "Im on the onPostExecute " + task.toString());
            super.onPostExecute(s);
            
        }
    }

    private void uploadSignatures(IPI_Wrapper ipiWrapper, Signature mode) {
        Log.e(TAG, ipiWrapper.toString());

        UploadFile_VolleyPOST post = new UploadFile_VolleyPOST();
        post.setContext(this.mContext)
                .setLink(PROJECT_JOB_SAVE_IPI_TASK_FORM_C_URL)
                .addParam("projectjob_id", ipiWrapper.getProjectJobID()+"")
                .addParam("disposition_by", ipiWrapper.getDispositionedBy())
                .addParam("sub_contractor", ipiWrapper.getSubContractor())
                .setOnEventListener(new UploadFile_VolleyPOST.OnEventListener() {
                    @Override
                    public void onError(String msg, int success) {
                        Log.e(TAG, "Message " + msg + " Error:" + success);
                    }

                    @Override
                    public void onSuccess(String msg, int success) {
                        Log.e(TAG, "Message " + msg + " Success:" + success);
                        //uploadTask.sleep();
                    }
                });

        // TODO: Form Type based on th mode of Signature...
        // post.addParam("form_type", ipiWrapper.)

        int counter = 0;
        if (hasSignatureDispositionendBy) {
            counter++;
            post.addMultipleFile(this.mSignatureUtil.getFile(), this.mSignatureUtil.getFile().getName(), "image/jpeg", "1")
                .addParam("hasContractorSign", hasSignatureDispositionendBy + "");
        }

        if (hasSignatureSubContractor) {
            post.addMultipleFile(this.mSignatureUtil.getFile(), this.mSignatureUtil.getFile().getName(), "image/jpeg", "2")
                    .addParam("hasSignatureSubContractor", hasSignatureDispositionendBy + "");
        }
        post.startUpload();
    }


}
