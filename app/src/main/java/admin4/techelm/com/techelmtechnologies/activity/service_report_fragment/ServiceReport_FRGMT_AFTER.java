package admin4.techelm.com.techelmtechnologies.activity.service_report_fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.servicejob_main.PopulateServiceJobViewDetails;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_Complaint_CFListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_Complaint__ASRListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_RecordingsListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_UploadsListAdapter;
import admin4.techelm.com.techelmtechnologies.db.servicejob.ComplaintActionDBUtil;
import admin4.techelm.com.techelmtechnologies.db.servicejob.RecordingSJDBUtil;
import admin4.techelm.com.techelmtechnologies.db.servicejob.ServiceJobDBUtil;
import admin4.techelm.com.techelmtechnologies.db.servicejob.UploadsSJDBUtil;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaintWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_ASRWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_CFWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_MobileWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobRecordingWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobUploadsWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.utility.CameraUtil;
import admin4.techelm.com.techelmtechnologies.utility.PermissionUtil;
import admin4.techelm.com.techelmtechnologies.utility.PlaybackFragment;
import admin4.techelm.com.techelmtechnologies.utility.RecordingService;
import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm.ServiceJobComplaints_POST;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.LIST_DELIM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_COMPLAINTS_ASR_LIST_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_COMPLAINTS_CF_LIST_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_COMPLAINTS_MOBILE_LIST_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_ID_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_TAKEN_KEY;

public class ServiceReport_FRGMT_AFTER extends Fragment implements
        RecordingService.OnTimerChangedListener {

    private static final String TAG = "ServiceReport_AFTER";
    private static final String UPLOAD_TAKEN = "AFTER";
    private int mServiceID; // For DB Purpose to save the file on the ServiceID
    private Context mContext;

    // A. SERVICE ID INFO
    private ServiceJobDBUtil mSJDB;
    private List<ServiceJobWrapper> mSJResultList = null;
    private EditText mEditTextRemarks;
    private String remarks;

    // B. CAMERA Controls
    private static final String IMAGE_DIRECTORY = "upload_after";
    private MaterialDialog mCameraDialog;
    private Bitmap mBitmap;
    private Uri mPicUri;

    private SJ_UploadsListAdapter mUploadListAdapter; // ListView Setup
    private RecyclerView mUploadResultsList;
    private List<ServiceJobUploadsWrapper> mUploadResults = null;
    private UploadsSJDBUtil mUploadsDB;

    private ImageButton mButtonViewUploadImage;
    private ProgressBar mProgressBarUploading;
    private ProgressBar mProgressBarLoadingAction;

    // C. Recording controls
    private MaterialDialog mRecordingDialog;
    private FloatingActionButton mRecordButton = null;
    private Button mPauseButton = null;

    private TextView mRecordingPrompt;
    private int mRecordPromptCount = 0;

    private boolean mStartRecording = true;
    private boolean mPauseRecording = true;

    private Chronometer mChronometer = null;
    long timeWhenPaused = 0; //stores time when user clicks pause button

    private SJ_RecordingsListAdapter mListAdapter; // ListView Setup
    private RecyclerView mRecordResultsList;
    private List<ServiceJobRecordingWrapper> mResultsList = null;
    private RecordingSJDBUtil mRecodingDB;

    // D. From Activity Objects
    private ArrayList<ServiceJobComplaint_MobileWrapper> mComplaintMobileCategoryList = null;

    private SJ_Complaint_CFListAdapter mComplaintCFListAdapter; // ListView Setup
    private RecyclerView mComplaintCFResultsList;
    private ArrayList<ServiceJobComplaint_CFWrapper> mSJComplaintCFList = null;
    private TextView textViewComplaintResult;

    private SJ_Complaint__ASRListAdapter mComplaintASRListAdapter; // ListView Setup
    private RecyclerView mComplaintASRResultsList;
    private ArrayList<ServiceJobComplaint_ASRWrapper> mComplaintASR_ActionByCatList = null;
    private TextView textViewActionResult;
    private Spinner mSpinnerAction;

    // SlidingPager Tab Set Up, Instance Variable
    private static final String ARG_POSITION = "position";
    private int mPosition;
    private ServiceJobWrapper mServiceJobFromBundle; // From Calling Activity

    public static ServiceReport_FRGMT_AFTER newInstance(int position, ServiceJobWrapper serviceJob,
            ArrayList<ServiceJobComplaint_CFWrapper> mSJComplaintCFList,
            ArrayList<ServiceJobComplaint_MobileWrapper> mSJComplaintMobileList,
            ArrayList<ServiceJobComplaint_ASRWrapper> mSJComplaintASRList)
    {
        ServiceReport_FRGMT_AFTER frag = new ServiceReport_FRGMT_AFTER();
        Bundle args = new Bundle();

        args.putInt(ARG_POSITION, position);
        args.putParcelable(SERVICE_JOB_ID_KEY, serviceJob);
        args.putParcelableArrayList(SERVICE_JOB_COMPLAINTS_CF_LIST_KEY, mSJComplaintCFList);
        args.putParcelableArrayList(SERVICE_JOB_COMPLAINTS_MOBILE_LIST_KEY, mSJComplaintMobileList);
        args.putParcelableArrayList(SERVICE_JOB_COMPLAINTS_ASR_LIST_KEY, mSJComplaintASRList);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);
        mServiceJobFromBundle = getArguments().getParcelable(SERVICE_JOB_ID_KEY);
        mComplaintMobileCategoryList = getArguments().getParcelableArrayList(SERVICE_JOB_COMPLAINTS_MOBILE_LIST_KEY);
        mSJComplaintCFList = getArguments().getParcelableArrayList(SERVICE_JOB_COMPLAINTS_CF_LIST_KEY);
        mComplaintASR_ActionByCatList = getArguments().getParcelableArrayList(SERVICE_JOB_COMPLAINTS_ASR_LIST_KEY);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_service_report_after, container, false);

        this.mContext = container.getContext();

        initSpinnerProgessBar(view);

        initButton(view);

        initPermission();

        setUpViews(view);

        return view;
    }

    private void setUpViews(View view) {
        if (mServiceJobFromBundle != null) { // if Null don't show anything
            mServiceID = mServiceJobFromBundle.getID();

            // Edit Text Remarks
            setUpEditRemarks(view);

            // ServiceJob Details
            new PopulateServiceJobViewDetails()
                    .populateServiceJobDetails(
                            view, //this.findViewById(android.R.id.content),
                            mServiceJobFromBundle,
                            View.GONE,
                            TAG);

            // Recording List
            setUpRecordingsRecyclerView(view);
            setupRecordingsResultsList();
            if (mResultsList == null) {
                populateRecordingsCardList();
            }

            // Upload List
            setUpUploadsRecyclerView(view);
            setupUploadsResultsList();
            if (mUploadResults == null) {
                populateUploadsCardList();
            }

            // D. Complaints List
            setUpComplaintsRecyclerView(view);
            setupComplaintsResultsList();
            populateComplaintsCardList();
            if (mResultsList == null) { }

            // E. Complaint Actions
            setUpComplaintActionsRecyclerView(view);
            setupActionsResultList();
            new PopulateActionOperation().execute(mServiceJobFromBundle.getID()+"");

        } else {
            SnackBarNotificationUtil
                    .setSnackBar(getActivity().findViewById(android.R.id.content),
                            "No data selected from calendar.")
                    .setColor(getResources().getColor(R.color.colorPrimary1))
                    .show();
        }

    }

    private void initSpinnerProgessBar(View view) {
        mProgressBarUploading = (ProgressBar) view.findViewById(R.id.progressBarUploading);
        mProgressBarUploading.setVisibility(View.GONE);
        mProgressBarLoadingAction = (ProgressBar) view.findViewById(R.id.progressBarLoadingAction);
        mProgressBarLoadingAction.setVisibility(View.GONE);
    }

    private void initPermission() {
        PermissionUtil.initPermissions(getActivity());
    }

    private void initButton(View view) {
        /** BUTTON BACK */
        Button button_back = (Button) view.findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                ((ServiceJobViewPagerActivity)getActivity()).fromFragmentNavigate(-1);

            }
        });

        /** BUTTON NEXT */
        Button button_next = (Button) view.findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                saveRemarksOnThread(mEditTextRemarks.getText().toString());
                ((ServiceJobViewPagerActivity)getActivity()).fromFragmentNavigate(1);
            }
        });

        /** BUTTON UPLOAD CAPTURED IMAGE */
        mButtonViewUploadImage = (ImageButton) view.findViewById(R.id.buttonViewUploadImage);
        mButtonViewUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mCameraDialog = showCameraDialog();
            }
        });

        /** BUTTON UPLOAD VOICE */
        ImageButton buttonViewUploadVoice = (ImageButton) view.findViewById(R.id.buttonViewUploadVoice);
        buttonViewUploadVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecordingDialog = showRecordingDialog();
                initRecordingView(mRecordingDialog.getView());
            }
        });

        /** BUTTON VIEW DETAILS */
        ImageButton buttonViewDetails = (ImageButton) view.findViewById(R.id.buttonViewDetails);
        buttonViewDetails.setVisibility(View.GONE);
    }

    /*********** A.1 EDIT VIEW POP UP REMARKS ***********/
    private void setUpEditRemarks(View view) {
        mEditTextRemarks = (EditText) view.findViewById(R.id.editTextRemarks);
        /*mEditTextRemarks.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);*/
        remarks = "";
        /*if (mPosition == 0) {
            remarks = (null == mServiceJobFromBundle.getRemarks() ?
                    "" : mServiceJobFromBundle.getRemarks());
        } else {*/
            mSJDB = new ServiceJobDBUtil(getActivity());
            mSJDB.open();
            remarks = mSJDB.getAllJSDetailsByServiceJobID(mServiceID).getAfterRemarks();
            mSJDB.close();
        //}
        mEditTextRemarks.setText(remarks);
        /*mEditTextRemarks.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        mEditTextRemarks.setText(((EditText)v).getText().toString());
                        saveRemarksOnThread(((EditText)v).getText().toString());

                        Log.e(TAG, ((EditText)v).getText().toString());

                        // Hide SoftKeyboard on Inactive Editext Listener
                        InputMethodManager imm = (InputMethodManager)
                                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mEditTextRemarks.getWindowToken(), 0);
                        break;
                }
                return false;
            }
        });*/
        /*mEditTextRemarks.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mEditTextRemarks.setRawInputType(InputType.TYPE_CLASS_TEXT);*/
        /*mEditTextRemarks.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    mEditTextRemarks.setText(v.getText().toString());
                    saveRemarksOnThread(v.getText().toString());

                    Log.e(TAG, v.getText().toString());

                    InputMethodManager imm = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEditTextRemarks.getWindowToken(), 0);
                    return true;
                }
//                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
//                {
//                    mEditTextRemarks.setSelection(0);
//                    InputMethodManager imm = (InputMethodManager)
//                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(mEditTextRemarks.getWindowToken(), 0);
//                    return true;
//                }
                return false;
            }
        });*/

        /*mEditTextRemarks.setOnFocusChangeListener((new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        showEditViewRemarksDialog(remarks);
                    }
                })
        );*/
    }

    private void saveRemarksOnThread(String remarks) {
        mSJDB = new ServiceJobDBUtil(getActivity());
        mSJDB.open();
        mSJDB.updateRequestIDRemarks_AFTER(mServiceID, remarks);
        mSJDB.close();
        Log.e(TAG, "saveRemarksOnThread++");
        SnackBarNotificationUtil
                .setSnackBar(getActivity().findViewById(android.R.id.content), "Remarks saved.")
                .setColor(getResources().getColor(R.color.colorPrimary1))
                .show();
    }

    public MaterialDialog showEditViewRemarksDialog(String remarks) {
        boolean wrapInScrollView = false;

        final EditText input = new EditText(this.mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setText(remarks);

        MaterialDialog md = new MaterialDialog.Builder(this.mContext)
                .title("REMARKS")
                .customView(input, wrapInScrollView)
                .positiveText("Close")
                .negativeText("Save")
                .iconRes(R.mipmap.edit_icon)
                .limitIconToDefaultSize()
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mEditTextRemarks.setText(input.getText());
                        mSJDB = new ServiceJobDBUtil(getActivity());
                        mSJDB.open();
                        mSJDB.updateRequestIDRemarks_AFTER(mServiceID, input.getText().toString());
                        mSJDB.close();
                        hideKeyboard();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        hideKeyboard();
                    }
                })
                .show();

        return md;
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager =(InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    /*********** A.1 END EDIT VIEW POP UP REMARKS ***********/


    /*********** A. SERVICE DETAILS ***********/
    public void fromActivity_onNewSJEntryAdded(String serviceNum) {
    }
    public void fromActivity_onSJEntryRenamed(String remarks) {
        mEditTextRemarks.setText(remarks);
    }
    public void fromActivity_onSJEntryDeleted() { }

    public void fromActivity_onSJEntryAddAction(ServiceJobComplaint_MobileWrapper mobileWrapper,
                                                String clickedItemValue, String clickedItemValueAction) {
        Log.e(TAG, "fromActivity_onSJEntryAddAction " + mobileWrapper.toString());

        ServiceJobComplaintWrapper complaintWrapper = setComplaint(mobileWrapper, clickedItemValue);
        showAddActionDialog(mobileWrapper, clickedItemValue, complaintWrapper);
    }

    public void fromActivity_onSJEntryDeleteAction(ServiceJobComplaint_MobileWrapper mobileWrapper,
           String clickedItemValue, String cmcf_id, String clickedItemValueActionOrCategory) {
        Log.e(TAG, "fromActivity_onSJEntryDeleteAction " + mobileWrapper.toString());

        ServiceJobComplaintWrapper complaintWrapper = setComplaintForDelete(mobileWrapper, clickedItemValue, clickedItemValueActionOrCategory, cmcf_id);
        showDeleteActionDialog(mobileWrapper, complaintWrapper);
    }
    // Should NOT USED
    public void fromActivity_onSJEntryDeleteAction(
            ServiceJobComplaint_MobileWrapper serviceJobComplaint_mobileWrapper,
            ServiceJobComplaintWrapper dataSet)
    {
        Log.e(TAG, "serviceJobComplaint_mobileWrapper " + serviceJobComplaint_mobileWrapper.toString());
        Log.e(TAG, "ServiceJobComplaintWrapper " + dataSet.toString());
        showDeleteActionDialog(serviceJobComplaint_mobileWrapper, dataSet);
    }

    public void fromActivity_onHandleASRDeleteSelection(
            ServiceJobComplaint_CFWrapper serviceJobComplaint_mobileWrapper,
            ServiceJobComplaintWrapper complaint, String dataSet)
    {
        Log.e(TAG, "serviceJobComplaint_mobileWrapper " + serviceJobComplaint_mobileWrapper.toString());
        Log.e(TAG, "ServiceJobComplaintWrapper " + dataSet.toString());
        Log.e(TAG, "complaint " + complaint.toString());
        servicesJobStartDeleteComplaintsTask(complaint, dataSet);
    }

    public void fromActivity_onHandleActionsSelection(ServiceJobComplaint_CFWrapper mobileWrapper,
            String clickedItemAction)
    {
        Log.e(TAG, "fromActivity_onSJEntryAddAction " + mobileWrapper.toString());

        ServiceJobComplaintWrapper complaintWrapper = setComplaintForAdd(mobileWrapper, clickedItemAction);

        Log.e(TAG, "complaintWrapper " + complaintWrapper.toString());
        new ActionSaveOperation().execute(complaintWrapper);
    }

    private ServiceJobComplaintWrapper setComplaintForDelete(ServiceJobComplaint_MobileWrapper mobileWrapper,
            String clickedItemValue, String clickedItemValueActionOrCategory, String cmcf_id)
    {
        ServiceJobComplaintWrapper complaintWrapper = new ServiceJobComplaintWrapper();
        complaintWrapper.setServiceJobID(mobileWrapper.getServiceJobID());
        complaintWrapper.setSJ_CM_CF_ID(Integer.parseInt(cmcf_id));
        complaintWrapper.setComplaint(clickedItemValueActionOrCategory);
        complaintWrapper.setComplaintFaultID(getComplaintFaultIDFromArrayList(clickedItemValue));
        complaintWrapper.setComplaintMobileID(mobileWrapper.getID());
        complaintWrapper.setCategoryID(mobileWrapper.getSJCategoryId());
        complaintWrapper.setCategory(mobileWrapper.getSJCategory());
        complaintWrapper.setActionID(getActionIDFromArrayList(clickedItemValue));
        return complaintWrapper;
    }

    private ServiceJobComplaintWrapper setComplaintForAdd(ServiceJobComplaint_CFWrapper mobileWrapper,
            String clickedItemAction)
    {
        ServiceJobComplaintWrapper complaintWrapper = new ServiceJobComplaintWrapper();
        complaintWrapper.setServiceJobID(mServiceJobFromBundle.getID());
        complaintWrapper.setSJ_CM_CF_ID(mobileWrapper.getSJ_CM_CF_ID());
        complaintWrapper.setComplaint(mobileWrapper.getComplaint());
        complaintWrapper.setComplaintFaultID(mobileWrapper.getSJComplaintFaultID());
        complaintWrapper.setComplaintMobileID(mobileWrapper.getID());
        complaintWrapper.setCategoryID(mobileWrapper.getSJCategoryID());
        complaintWrapper.setCategory(getCategoryFromArrayList(mobileWrapper.getSJCategoryID()));
        complaintWrapper.setAction(clickedItemAction);
        complaintWrapper.setActionID(getActionIDFromArrayList(clickedItemAction));

        return complaintWrapper;
    }

    private ServiceJobComplaintWrapper setComplaint(ServiceJobComplaint_MobileWrapper mobileWrapper,
                                                    String clickedItemValueComplaint) {
        ServiceJobComplaintWrapper complaintWrapper = new ServiceJobComplaintWrapper();
        complaintWrapper.setServiceJobID(mobileWrapper.getServiceJobID());
        complaintWrapper.setSJ_CM_CF_ID(getCMCFIDFromArrayList(clickedItemValueComplaint));
        complaintWrapper.setComplaint(clickedItemValueComplaint);
        // complaintWrapper.setComplaint(getComplaintFromArrayList(clickedItemValue));
        complaintWrapper.setComplaintFaultID(getComplaintFaultIDFromArrayList(clickedItemValueComplaint));
        complaintWrapper.setComplaintMobileID(mobileWrapper.getID());
        complaintWrapper.setCategoryID(mobileWrapper.getSJCategoryId());
        complaintWrapper.setCategory(mobileWrapper.getSJCategory());
        return complaintWrapper;
    }

    /**
     * Search and Get Value Complaint Name Clicked ID
     * @param complaintToSearch - clicked sub category complaint name
     * @return ID else 0 if none
     */
    private int getComplaintFaultIDFromArrayList(String complaintToSearch) {
        for (ServiceJobComplaint_CFWrapper cf : this.mSJComplaintCFList) {
            if (cf.getComplaint() != null && cf.getComplaint().contains(complaintToSearch))
                return cf.getSJComplaintFaultID();
        }
        return 0;
    }

    private int getCMCFIDFromArrayList(String complaintToSearch) {
        for (ServiceJobComplaint_CFWrapper cf : this.mSJComplaintCFList) {
            if (cf.getComplaint() != null && cf.getComplaint().contains(complaintToSearch))
                return cf.getSJ_CM_CF_ID();
        }
        return 0;
    }

    private String getComplaintFromArrayList(String complaintToSearch) {
        for (ServiceJobComplaint_CFWrapper cf : this.mSJComplaintCFList) {
            if (cf.getComplaint() != null && cf.getComplaint().contains(complaintToSearch))
                return cf.getComplaint();
        }
        return "";
    }

    private int getActionIDFromArrayList(String actionToSearch) {
        for (ServiceJobComplaint_ASRWrapper asr : this.mComplaintASR_ActionByCatList) {
            if (asr.getAction() != null && asr.getAction().contains(actionToSearch))
                return asr.getID();
        }
        return 0;
    }

    private String getCategoryFromArrayList(int categoryID) {
        for (ServiceJobComplaint_MobileWrapper cfWrapper : this.mComplaintMobileCategoryList) {
            if (cfWrapper.getSJCategoryId() == categoryID)
                return cfWrapper.getSJCategory();
        }
        return "";
    }

    // NO USED
    public MaterialDialog showAddActionDialog(ServiceJobComplaint_MobileWrapper mobileWrapper,
              String clickedItemValue, final ServiceJobComplaintWrapper complaintWrapper) {
        boolean wrapInScrollView = false;
        MaterialDialog md = new MaterialDialog.Builder(this.mContext)
                .title("NEW ACTION")
                .customView(R.layout.m_service_report_add_action, wrapInScrollView)
                .negativeText("Save")
                .positiveText("Close")
                .iconRes(R.mipmap.replacepart_icon)
                .autoDismiss(false)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.e(TAG, "mSpinnerAction = " + mSpinnerAction.getSelectedItem().toString());

                        if (mSpinnerAction != null && mSpinnerAction.getSelectedItemPosition() > 0) {
                            String action = mSpinnerAction.getSelectedItem().toString();
                            int actionID = getActionIDFromArrayList(action);
                            complaintWrapper.setAction(action);
                            complaintWrapper.setActionID(actionID);

                            Log.e(TAG, "showAddActionDialog YES " + complaintWrapper.toString());
                            new ActionSaveOperation().execute(complaintWrapper);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(mContext, "You must Select Action before save.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).show();

        // Getting Category for Dropdown
        StringBuilder sbFaults = new StringBuilder();
        Log.e(TAG, "showAddActionDialog " + mobileWrapper.toString());
        for (int i = 0; mComplaintASR_ActionByCatList.size() > i; i++) {
            if (mobileWrapper.getSJCategoryId() == mComplaintASR_ActionByCatList.get(i).getSJCategoryId()) {
                sbFaults.append(mComplaintASR_ActionByCatList.get(i).getAction());
                sbFaults.append(LIST_DELIM);
            }
        }

        // Preparing Action Drop Down
        final String[] aSubItem = sbFaults.toString().split(LIST_DELIM);

        initActionSpinner(md.getCustomView(), clickedItemValue, aSubItem);
        return md;
    }

    public MaterialDialog showDeleteActionDialog(ServiceJobComplaint_MobileWrapper mobileWrapper,
                                                 final ServiceJobComplaintWrapper complaintWrapper) {
        boolean wrapInScrollView = false;
        MaterialDialog md = new MaterialDialog.Builder(this.mContext)
                .title("DELETE ACTION.")
                .customView(R.layout.m_service_report_delete_action, wrapInScrollView)
                .negativeText("Delete")
                .positiveText("Close")
                .iconRes(R.mipmap.del_icon)
                .autoDismiss(false)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Toast.makeText(mContext, "You delete " + complaintWrapper.getAction(), Toast.LENGTH_SHORT).show();
                        servicesJobStartDeleteComplaintsTask(dialog, complaintWrapper, complaintWrapper.getAction());
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).show();

        // Setting Details for Action
        View view = md.getCustomView();
        TextView tv = (TextView) view.findViewById(R.id.textViewActionDetails);
        String details = /*"Category:\t" + complaintWrapper.getCategory().toUpperCase()
                + "\nComplaint:\t" + complaintWrapper.getComplaint()
                + "\nActoin:\t" +*/ complaintWrapper.getAction();
        tv.setText(details);
        // Getting Category for Dropdown
        Log.e(TAG, "showAddActionDialog " + mobileWrapper.toString());
        return md;
    }

    private void initActionSpinner(View customView, String clickedItemValue, String[] aSubItem) {
        ArrayList<String> options = new ArrayList<>();
        options.add("Select Action");
        for (String action : aSubItem) {
            options.add(action);
        }

        // Spinner 1
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, options);
        mSpinnerAction = (Spinner) customView.findViewById(R.id.spinnerAction);
        mSpinnerAction.setAdapter(adapter);

        // TextView
        TextView textViewActionTitle = (TextView) customView.findViewById(R.id.textViewActionTitle);
        textViewActionTitle.setText("Actions for \""+ clickedItemValue +"\".");
    }

    private class ActionSaveOperation extends AsyncTask<ServiceJobComplaintWrapper, Void, Boolean> {

        private ComplaintActionDBUtil cActionDB;
        private ServiceJobComplaintWrapper cComplaint;

        @Override
        protected void onPreExecute() {
            cActionDB = new ComplaintActionDBUtil(mContext);
        }

        @Override
        protected Boolean doInBackground(ServiceJobComplaintWrapper... params) {
            if (params[0] != null) {
                cActionDB.open();
                cActionDB.addNewAction(params[0]);
                cActionDB.close();
                cComplaint = params[0];
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean onSuccess) {
            String msg = "Unable to save action.";
            if (onSuccess) {
                msg = "Action saved.";

                cActionDB.open();
                Log.e(TAG, "onPostExecute " + cActionDB.getAllActionsBySJID(cComplaint.getServiceJobID()).toString());
                cActionDB.close();
                servicesJobStartAddComplaintsTask(cComplaint);
            }
            SnackBarNotificationUtil
                    .setSnackBar(getActivity().findViewById(android.R.id.content), msg)
                    .setColor(getResources().getColor(R.color.colorPrimary1))
                    .show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private void servicesJobStartAddComplaintsTask(final ServiceJobComplaintWrapper complaintWrapper) {
        final ServiceJobComplaints_POST beginServiceJob = new ServiceJobComplaints_POST();
        beginServiceJob.setOnEventListener(new ServiceJobComplaints_POST.OnEventListener() {
            @Override
            public void onEvent() {
                // TODO: Close progress dialog here
                // TODO: Test Response if OK or not
            }

            @Override
            public void onError(String message) {
                // TODO: Close progress dialog then try again if connected to internet
            }

            @Override
            public void onEventResult(WebResponse response) {
                // proceedViewPagerActivity(serviceJob, status, response.getStringResponse());
                Log.e(TAG, "onEventResult " + response.getStringResponse());
                Log.e(TAG, "complaintWrapper " + complaintWrapper.toString());
                // new MainActivity.ServiceJobTask(serviceJob, status, startTaskResponse, response.getStringResponse()).execute((Void) null);
                new PopulateActionOperation().execute(mServiceJobFromBundle.getID()+"");
            }
        });

        // To Start the Activity
        beginServiceJob.postAddComplaint(complaintWrapper);
    }

    // NOT USED
    private void servicesJobStartDeleteComplaintsTask(final MaterialDialog dialog,
            final ServiceJobComplaintWrapper complaintWrapper, String clickedItemValue) {
        final ServiceJobComplaints_POST beginServiceJob = new ServiceJobComplaints_POST();
        beginServiceJob.setOnEventListener(new ServiceJobComplaints_POST.OnEventListener() {
            @Override
            public void onEvent() {
                // TODO: Close progress dialog here
                // TODO: Test Response if OK or not
            }

            @Override
            public void onError(String message) {
                // TODO: Close progress dialog then try again if connected to internet
            }

            @Override
            public void onEventResult(WebResponse response) {
                // proceedViewPagerActivity(serviceJob, status, response.getStringResponse());
                Log.e(TAG, "onEventResult " + response.getStringResponse());
                Log.e(TAG, "complaintWrapper " + complaintWrapper.toString());
                // new MainActivity.ServiceJobTask(serviceJob, status, startTaskResponse, response.getStringResponse()).execute((Void) null);
                new PopulateDeleteActionOperation().setComplaint(complaintWrapper).execute(mServiceJobFromBundle.getID()+"");
                dialog.dismiss();
            }
        });

        // To Start the Activity
        int actionID = getActionIDFromArrayList(clickedItemValue);
        complaintWrapper.setActionID(actionID);
        beginServiceJob.postDeleteComplaint(complaintWrapper);
    }

    private void servicesJobStartDeleteComplaintsTask(
            final ServiceJobComplaintWrapper complaintWrapper, String clickedItemValue)
    {
        final ServiceJobComplaints_POST beginServiceJob = new ServiceJobComplaints_POST();
        beginServiceJob.setOnEventListener(new ServiceJobComplaints_POST.OnEventListener() {
            @Override
            public void onEvent() {
                // TODO: Close progress dialog here
                // TODO: Test Response if OK or not
            }

            @Override
            public void onError(String message) {
                // TODO: Close progress dialog then try again if connected to internet
            }

            @Override
            public void onEventResult(WebResponse response) {
                // proceedViewPagerActivity(serviceJob, status, response.getStringResponse());
                Log.e(TAG, "onEventResult " + response.getStringResponse());
                Log.e(TAG, "complaintWrapper " + complaintWrapper.toString());
                // new MainActivity.ServiceJobTask(serviceJob, status, startTaskResponse, response.getStringResponse()).execute((Void) null);
                new PopulateDeleteActionOperation().setComplaint(complaintWrapper).execute(mServiceJobFromBundle.getID()+"");
            }
        });

        // To Start the Activity
        int actionID = getActionIDFromArrayList(clickedItemValue);
        complaintWrapper.setActionID(actionID);
        beginServiceJob.postDeleteComplaint(complaintWrapper);
    }

    /*********** A. END SERVICE DETAILS ***********/


    /*********** B. CAMERA SETUP ***********/
    public void setUpUploadsRecyclerView(View view) {
        mUploadResultsList = (RecyclerView) view.findViewById(R.id.upload_results_service_job_list);
    }

    public void setupUploadsResultsList() {
        mUploadListAdapter = new SJ_UploadsListAdapter(getActivity());
        mUploadResultsList.setAdapter(mUploadListAdapter);
        mUploadResultsList.setLayoutManager(new LinearLayoutManager(this.mContext));
    }

    private void populateUploadsCardList() {
        mUploadsDB = new UploadsSJDBUtil(this.mContext);
        mUploadsDB.open();
        mUploadResults = mUploadsDB.getAllUploadsBySJID_ByTaken(mServiceID, UPLOAD_TAKEN);
        mUploadsDB.close();

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

    private void showUploadDialog(final ServiceJobUploadsWrapper serviceJobRecordingWrapper) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.m_service_report_image_view, null);
        dialog.setView(dialogLayout);
        dialog.setTitle("IMAGE CAPTURED");
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ImageView image = (ImageView) dialogLayout.findViewById(R.id.imageViewUpload);
        Drawable draw = Drawable.createFromPath(serviceJobRecordingWrapper.getFilePath() /*+ "/" +
                serviceJobRecordingWrapper.getUploadName()*/);

        image.setImageDrawable(draw);

        dialog.show();
    }

    public MaterialDialog showCameraDialog() {
        final CameraUtil camU = new CameraUtil(getActivity(), "CAPTURE_" + UPLOAD_TAKEN);
        boolean wrapInScrollView = false;
        MaterialDialog md = new MaterialDialog.Builder(this.mContext)
                .title("UPLOAD IMAGE.")
                .customView(R.layout.m_service_report_camera, wrapInScrollView)
                .neutralText("Capture")
                .negativeText("Save")
                .positiveText("Close")
                .iconRes(R.mipmap.ic_media_camera)
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
                        if (mBitmap != null && mPicUri != null) {
                            ImageSaveOperation ops = new ImageSaveOperation();
                            ops.execute(camU);

                            dialog.dismiss();
                        } else {
                            SnackBarNotificationUtil
                                    .setSnackBar(getActivity().findViewById(android.R.id.content),
                                            "No image to save")
                                    .setColor(getResources().getColor(R.color.colorPrimary1))
                                    .show();
                        }
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        // mSignaturePad.clear();
                        mBitmap = null;
                        mPicUri = null;
                        dialog.dismiss();
                    }
                }).show();
        return md;
    }

    private class ImageSaveOperation extends AsyncTask<CameraUtil, Void, ServiceJobUploadsWrapper> {
        private CameraUtil camU;

        @Override
        protected void onPreExecute() {
            mButtonViewUploadImage.setVisibility(View.GONE);
            mProgressBarUploading.setVisibility(View.VISIBLE);
        }

        @Override
        protected ServiceJobUploadsWrapper doInBackground(CameraUtil... params) {
            camU = params[0];
            if (camU.addJpgUploadToGallery(mBitmap, IMAGE_DIRECTORY)) {
                // Prepare Data for DB
                ServiceJobUploadsWrapper sjUp = new ServiceJobUploadsWrapper();
                sjUp.setUploadName(camU.getFileName());
                sjUp.setFilePath(camU.getFilePath());
                sjUp.setTaken(UPLOAD_TAKEN);
                sjUp.setServiceId(mServiceID);
                return sjUp;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ServiceJobUploadsWrapper sjUP) {
            if (sjUP != null) {

                mUploadsDB.open(); // Save Upload to DB as record and runon UIThread
                mUploadsDB.addUpload(sjUP);
                mUploadsDB.close();

                SnackBarNotificationUtil
                        .setSnackBar(getActivity().findViewById(android.R.id.content),
                                "Image saved into the Gallery."/* + camU.getFilePath()*/)
                        .setColor(getResources().getColor(R.color.colorPrimary1))
                        .show();
            } else {
                SnackBarNotificationUtil
                        .setSnackBar(getActivity().findViewById(android.R.id.content),
                                "Unable to save the signature")
                        .setColor(getResources().getColor(R.color.colorPrimary1))
                        .show();
            }

            if (mProgressBarUploading.isShown()) {
                mButtonViewUploadImage.setVisibility(View.VISIBLE);
                mProgressBarUploading.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
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

    /** Show image on the dialog*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {
            ImageView imageView = (ImageView) mCameraDialog.getCustomView().findViewById(R.id.imageViewCamera);
            if (getPickImageResultUri(data) != null) {
                mPicUri = getPickImageResultUri(data);
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mPicUri);
                    mBitmap = rotateImageIfRequired(mBitmap, mPicUri);
                    // mBitmap = getResizedBitmap(mBitmap, 500);

                    /*CircleImageView croppedImageView = (CircleImageView) mCameraDialog.getCustomView().findViewById(R.id.img_profile);
                    croppedImageView.setImageBitmap(mBitmap);*/
                    imageView.setImageBitmap(mBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = (Bitmap) data.getExtras().get("data");

                mBitmap = bitmap;
                /*CircleImageView croppedImageView = (CircleImageView) mCameraDialog.getCustomView().findViewById(R.id.img_profile);
                if (croppedImageView != null) {
                    croppedImageView.setImageBitmap(mBitmap);
                }*/
                imageView.setImageBitmap(mBitmap);
            }
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

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("pic_uri", mPicUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        mPicUri = savedInstanceState.getParcelable("pic_uri");
    }*/

    // TODO: Study on how to implement separate listerner
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this.mContext)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void fromActivity_onNewUploadsDBEntryAdded(String fileName) {
        if (mCameraDialog != null)
            if (mCameraDialog.isShowing())
                mCameraDialog.dismiss();
        populateUploadsCardList();
    }
    public void fromActivity_onUploadsDBEntryRenamed(String fileName) { }
    public void fromActivity_onUploadsDBEntryDeleted() {
        populateUploadsCardList();
    }

    public void fromActivity_onHandleUploadsSelection(
            int position, ServiceJobUploadsWrapper serviceJobRecordingWrapper, int mode) { }
    public void fromActivity_onHandleViewUploadFromListSelection(
            ServiceJobUploadsWrapper serviceJobRecordingWrapper) {
        showUploadDialog(serviceJobRecordingWrapper);
    }

    public void fromActivity_onHandleDeleteUploadsFromListSelection(final int id) {
        new MaterialDialog.Builder(this.mContext)
                .title("COMFIRM DELETE IMAGE.")
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
                        mUploadsDB = new UploadsSJDBUtil(getActivity());
                        mUploadsDB.open();
                        mUploadsDB.removeItemWithId(id);
                        mUploadsDB.close();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /*********** B. END CAMERA SETUP ***********/


    /*********** C. SOUND RECORDING ***********/
    public void setUpRecordingsRecyclerView(View view) {
        mRecordResultsList = (RecyclerView) view.findViewById(R.id.recording_results_service_job_list);
    }

    public void setupRecordingsResultsList() {
        mListAdapter = new SJ_RecordingsListAdapter(this.mContext);
        mRecordResultsList.setAdapter(mListAdapter);
        mRecordResultsList.setLayoutManager(new LinearLayoutManager(this.mContext));
    }

    private void populateRecordingsCardList() {
        mRecodingDB = new RecordingSJDBUtil(this.mContext);
        mRecodingDB.open();
        mResultsList = mRecodingDB.getAllRecordingsBySJID_ByTaken(mServiceID, UPLOAD_TAKEN);
        mRecodingDB.close();
        if (mResultsList != null) {
            //Log.e(TAG, "DATA: " + mResultsList.get(0).toString());
            /*for (int i = 0; i < mResultsList.size(); i++) {
                Log.e(TAG, "DATA: " + mResultsList.get(i).toString());
            }*/
            mRecordResultsList.setHasFixedSize(true);
            mRecordResultsList.setLayoutManager(new LinearLayoutManager(this.mContext));
            mRecordResultsList.setItemAnimator(new DefaultItemAnimator());
            mListAdapter.swapData(mResultsList);
            /*new UIThreadHandler(getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });*/
        }
    }

    public MaterialDialog showRecordingDialog() {
        boolean wrapInScrollView = false;
        MaterialDialog md = new MaterialDialog.Builder(this.mContext)
                .title("RECORD.")
                .customView(R.layout.m_signing_off_recording, wrapInScrollView)
                .positiveText("Close")
                .iconRes(R.mipmap.ic_media_mic_blk)
                .autoDismiss(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();

        return md;
    }

    private void initRecordingView(View recordView) {
        mChronometer = (Chronometer) recordView.findViewById(R.id.chronometer);
        mChronometer.setTextColor(getResources().getColor(R.color.black));
        //update recording prompt text
        mRecordingPrompt = (TextView) recordView.findViewById(R.id.recording_status_text);

        mRecordButton = (FloatingActionButton) recordView.findViewById(R.id.btnRecord);
        // mRecordButton.setBackgroundColor(getResources().getColor(R.color.black));
        mRecordButton.setColorPressed(getResources().getColor(R.color.colorPrimary1));
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);
                mStartRecording = !mStartRecording;
            }
        });

        mPauseButton = (Button) recordView.findViewById(R.id.btnPause);
        mPauseButton.setVisibility(View.GONE); //hide pause button before recording starts
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPauseRecord(mPauseRecording);
                mPauseRecording = !mPauseRecording;
            }
        });
    }


    // Recording Start/Stop
    //TODO: recording pause
    private void onRecord(boolean start){

        Intent intent = new Intent(getActivity(), RecordingService.class);

        if (start) {
            // start recording
            mRecordButton.setImageResource(R.mipmap.ic_media_stop);
            //mPauseButton.setVisibility(View.VISIBLE);
            SnackBarNotificationUtil
                    .setSnackBar(getActivity().findViewById(android.R.id.content),
                            getResources().getString(R.string.toast_recording_start))
                    .setColor(getResources().getColor(R.color.colorPrimary1))
                    .show();
            File folder = new File(Environment.getExternalStorageDirectory() + "/SoundRecorder");
            if (!folder.exists()) {
                //folder /SoundRecorder doesn't exist, then create the folder
                folder.mkdir();
            }

            //start Chronometer
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    if (mRecordPromptCount == 0) {
                        mRecordingPrompt.setText(getString(R.string.record_in_progress) + ".");
                    } else if (mRecordPromptCount == 1) {
                        mRecordingPrompt.setText(getString(R.string.record_in_progress) + "..");
                    } else if (mRecordPromptCount == 2) {
                        mRecordingPrompt.setText(getString(R.string.record_in_progress) + "...");
                        mRecordPromptCount = -1;
                    }

                    mRecordPromptCount++;
                }
            });

            intent.putExtra(SERVICE_JOB_ID_KEY, mServiceID); // Param to Service on RecodingService
            intent.putExtra(SERVICE_JOB_TAKEN_KEY, UPLOAD_TAKEN); // Param to Service on RecodingService
            //start RecordingService
            getActivity().startService(intent);
            //keep screen on while recording
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            mRecordingPrompt.setText(getString(R.string.record_in_progress) + ".");
            mRecordPromptCount++;
        } else {
            //stop recording
            mRecordButton.setImageResource(R.mipmap.ic_mic_white_36dp);
            //mPauseButton.setVisibility(View.GONE);
            mChronometer.stop();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            timeWhenPaused = 0;
            mRecordingPrompt.setText(getString(R.string.record_prompt));

            getActivity().stopService(intent);
            //allow the screen to turn off again once recording is finished
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    //TODO: implement pause recording
    private void onPauseRecord(boolean pause) {
        if (pause) {
            //pause recording
            mPauseButton.setCompoundDrawablesWithIntrinsicBounds
                    (R.mipmap.ic_media_play ,0 ,0 ,0);
            mRecordingPrompt.setText(getString(R.string.resume_recording_button).toUpperCase());
            timeWhenPaused = mChronometer.getBase() - SystemClock.elapsedRealtime();
            mChronometer.stop();
        } else {
            //resume recording
            mPauseButton.setCompoundDrawablesWithIntrinsicBounds
                    (R.mipmap.ic_media_pause ,0 ,0 ,0);
            mRecordingPrompt.setText(getString(R.string.pause_recording_button).toUpperCase());
            mChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenPaused);
            mChronometer.start();
        }
    }

    public void fromActivity_onHandleRecordingsSelection(
            int position, ServiceJobRecordingWrapper serviceJobRecordingWrapper, int mode) {
    }
    public void fromActivity_onHandleDeleteRecordingsFromListSelection(final int id) {
        new MaterialDialog.Builder(this.mContext)
                .title("COMFIRM DELETE RECORDING.")
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
                        mRecodingDB = new RecordingSJDBUtil(getActivity());
                        mRecodingDB.open();
                        mRecodingDB.removeItemWithId(id);
                        mRecodingDB.close();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void fromActivity_onHandlePlayFromListSelection(
            ServiceJobRecordingWrapper serviceJobRecordingWrapper) {
        try {
            PlaybackFragment playbackFragment =
                    new PlaybackFragment().newInstance(serviceJobRecordingWrapper);

            // FragmentTransaction transaction = ((FragmentActivity)ServiceReport_FRGMT_BEFORE.this)
            FragmentTransaction transaction = (getActivity())
                    .getSupportFragmentManager()
                    .beginTransaction();

            playbackFragment.show(transaction, "dialog_playback");

        } catch (Exception e) {
            Log.e(TAG, "exception", e);
        }
    }

    public void fromActivity_onNewRecordingsEntryAdded(String fileName) {

        if (mRecordingDialog!= null)
            if (mRecordingDialog.isShowing())
                mRecordingDialog.dismiss();
        SnackBarNotificationUtil
                .setSnackBar(getActivity().findViewById(android.R.id.content),
                        "Recording" /* + fileName*/ + " has been saved.")
                .setColor(getResources().getColor(R.color.colorPrimary1))
                .show();
        populateRecordingsCardList();
    }

    public void fromActivity_onRecordingsEntryRenamed(String fileName) {
        SnackBarNotificationUtil
                .setSnackBar(getActivity().findViewById(android.R.id.content),
                        "Recording" /*+ fileName*/ + " has been renamed.")
                .setColor(getResources().getColor(R.color.colorPrimary1))
                .show();
    }

    public void fromActivity_onRecordingsEntryDeleted() {
        /*Toast.makeText(ServiceReport_FRGMT_BEFORE.this, "Delete successful.",
                Toast.LENGTH_SHORT).show();*/
        if (mRecordingDialog != null)
            if (mRecordingDialog.isShowing())
                mRecordingDialog.dismiss();
        SnackBarNotificationUtil
                .setSnackBar(getActivity().findViewById(android.R.id.content),
                        "Delete successful.")
                .setColor(getResources().getColor(R.color.colorPrimary1))
                .show();
        populateRecordingsCardList();
    }

    @Override
    public void onTimerChanged(int seconds) { // TODO: If you can implement from Service Class to Activity
    }

    /*********** C. END SOUND RECORDING ***********/

    /*********** D. SERVICE JOB COMPLAINTS ***********/

    // D.1 COMPLAINTS List, with Add Action
    public void setUpComplaintsRecyclerView(View view) {
        mComplaintCFResultsList = (RecyclerView) view.findViewById(R.id.complaint_fault_service_job_list);
        textViewComplaintResult = (TextView) view.findViewById(R.id.textViewComplaintResult);
    }

    public void setupComplaintsResultsList() {
        mComplaintCFListAdapter = new SJ_Complaint_CFListAdapter(getActivity());
        mComplaintCFResultsList.setAdapter(mComplaintCFListAdapter);
        mComplaintCFResultsList.setLayoutManager(new LinearLayoutManager(this.mContext));
    }

    private void populateComplaintsCardList() {
        // mSJComplaintCFList = ((ServiceJobViewPagerActivity) getActivity()).mComplaintCFList;

        if (mSJComplaintCFList != null && mComplaintMobileCategoryList != null && mComplaintASR_ActionByCatList != null) {
            mComplaintCFResultsList.setHasFixedSize(true);
            mComplaintCFResultsList.setLayoutManager(new LinearLayoutManager(this.mContext));
            mComplaintCFResultsList.setItemAnimator(new DefaultItemAnimator());
            mComplaintCFListAdapter.swapData(mComplaintMobileCategoryList, mSJComplaintCFList, mComplaintASR_ActionByCatList, false);
            mComplaintCFResultsList.setVisibility(View.VISIBLE);
            textViewComplaintResult.setVisibility(View.GONE);

        } else {
            Log.e(TAG, "else populateComplaintsCardList: " + mResultsList);
            Log.e(TAG, "else mSJComplaintCFList: " + mSJComplaintCFList);
            Log.e(TAG, "else mComplaintMobileCategoryList: " + mComplaintMobileCategoryList);
            Log.e(TAG, "else mComplaintASR_ActionByCatList: " + mComplaintASR_ActionByCatList);

            // No Result
            mComplaintCFResultsList.setVisibility(View.GONE);
            textViewComplaintResult.setVisibility(View.VISIBLE);
            textViewComplaintResult.setText("No complaints this time.");
        }
    }


    // D.2 ACTIONS List, with View and Delete  Action
    public void setUpComplaintActionsRecyclerView(View view) {
        mComplaintASRResultsList = (RecyclerView) view.findViewById(R.id.action_service_job_list);
        textViewActionResult = (TextView) view.findViewById(R.id.textViewActionResult);
    }
    public void setupActionsResultList() {
        mComplaintASRListAdapter = new SJ_Complaint__ASRListAdapter(getActivity());
        mComplaintASRResultsList.setAdapter(mComplaintASRListAdapter);
        mComplaintASRResultsList.setLayoutManager(new LinearLayoutManager(this.mContext));
    }

    private void populateActionsCardList(ArrayList<ServiceJobComplaintWrapper> complaintsToShow) {
        // mSJComplaintCFList = ((ServiceJobViewPagerActivity) getActivity()).mComplaintCFList;

        if (complaintsToShow != null && mSJComplaintCFList != null && mComplaintMobileCategoryList != null && mComplaintASR_ActionByCatList != null) {
            mComplaintASRResultsList.setHasFixedSize(true);
            mComplaintASRResultsList.setLayoutManager(new LinearLayoutManager(this.mContext));
            mComplaintASRResultsList.setItemAnimator(new DefaultItemAnimator());
            mComplaintASRListAdapter.swapData(complaintsToShow, mComplaintMobileCategoryList, mSJComplaintCFList, mComplaintASR_ActionByCatList, false);

            textViewActionResult.setVisibility(View.GONE);
        } else {
            Log.e(TAG, "else ArrayList ServiceJobComplaintWrapper: " + complaintsToShow);
            Log.e(TAG, "else populateComplaintsCardList: " + mResultsList);
            Log.e(TAG, "else mSJComplaintCFList: " + mSJComplaintCFList);
            Log.e(TAG, "else mComplaintMobileCategoryList: " + mComplaintMobileCategoryList);
            Log.e(TAG, "else mComplaintASR_ActionByCatList: " + mComplaintASR_ActionByCatList);

            // No Result
            textViewActionResult.setVisibility(View.VISIBLE);
            textViewActionResult.setText("No actions this time.");
        }
    }

    private class PopulateActionOperation extends AsyncTask<String, Void, ArrayList<ServiceJobComplaintWrapper>> {

        private ComplaintActionDBUtil cActionDB;
        private ArrayList<ServiceJobComplaintWrapper> cComplaint;

        @Override
        protected void onPreExecute() {
            cActionDB = new ComplaintActionDBUtil(mContext);
        }

        @Override
        protected ArrayList<ServiceJobComplaintWrapper> doInBackground(String... params) {
            if (!params[0].equals("")) {
                cActionDB.open();
                cComplaint = cActionDB.getAllActionsBySJID(Integer.parseInt(params[0]));
                cActionDB.close();
                return cComplaint;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ServiceJobComplaintWrapper> complaints) {
            String msg = "No actions from DB.";
            if (complaints != null) {
                msg = "Actions retrieved.";
                Log.e(TAG, "onPostExecute " + complaints.toString());
                setUpComplaintActionsRecyclerView(getView());
                setupActionsResultList();
                populateActionsCardList(complaints);
                // new PopulateActionOperation().execute(mServiceJobFromBundle.getID()+"");
            }

            /*SnackBarNotificationUtil
                    .setSnackBar(getActivity().findViewById(android.R.id.content), msg)
                    .setColor(getResources().getColor(R.color.colorPrimary1))
                    .show();*/
        }
    }

    private class PopulateDeleteActionOperation extends AsyncTask<String, Void, ArrayList<ServiceJobComplaintWrapper>> {

        private ComplaintActionDBUtil cActionDB;
        private ArrayList<ServiceJobComplaintWrapper> cComplaint;
        private ServiceJobComplaintWrapper Complaint;

        public PopulateDeleteActionOperation setComplaint(ServiceJobComplaintWrapper id) {
            Complaint = id;
            return this;
        }

        @Override
        protected void onPreExecute() {
            cActionDB = new ComplaintActionDBUtil(mContext);
        }

        @Override
        protected ArrayList<ServiceJobComplaintWrapper> doInBackground(String... params) {
            if (!params[0].equals("")) {
                cActionDB.open();
                cActionDB.removeItemWithId(Complaint);
                cComplaint = cActionDB.getAllActionsBySJID(Integer.parseInt(params[0]));
                cActionDB.close();
                return cComplaint;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ServiceJobComplaintWrapper> complaints) {
            String msg = "No actions from DB.";
            if (complaints != null) {
                msg = "Actions retrieved.";
                Log.e(TAG, "onPostExecute " + complaints.toString());

                setUpComplaintActionsRecyclerView(getView());
                setupActionsResultList();
                populateActionsCardList(complaints);
                //new PopulateActionOperation().execute(mServiceJobFromBundle.getID()+"");
            }

            /*SnackBarNotificationUtil
                    .setSnackBar(getActivity().findViewById(android.R.id.content), msg)
                    .setColor(getResources().getColor(R.color.colorPrimary1))
                    .show();*/
        }
    }


    /*********** D. END SERVICEJOB COMPLAINTS ***********/
}
