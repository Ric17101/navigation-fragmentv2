package admin4.techelm.com.techelmtechnologies.activity.service_report_fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_ComplaintListExpandableAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_Complaint_CFListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_RecordingsListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_UploadsListAdapter;
import admin4.techelm.com.techelmtechnologies.db.servicejob.RecordingSJDBUtil;
import admin4.techelm.com.techelmtechnologies.db.servicejob.ServiceJobDBUtil;
import admin4.techelm.com.techelmtechnologies.db.servicejob.UploadsSJDBUtil;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_ASRWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_CFWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_MobileWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobRecordingWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobUploadsWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.activity.servicejob_main.PopulateServiceJobViewDetails;
import admin4.techelm.com.techelmtechnologies.utility.CameraUtil;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;
import admin4.techelm.com.techelmtechnologies.utility.PermissionUtil;
import admin4.techelm.com.techelmtechnologies.utility.PlaybackFragment;
import admin4.techelm.com.techelmtechnologies.utility.RecordingService;
import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_COMPLAINTS_ASR_LIST_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_COMPLAINTS_CF_LIST_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_COMPLAINTS_MOBILE_LIST_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_ID_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_TAKEN_KEY;

public class ServiceReport_FRGMT_BEFORE extends Fragment implements
        RecordingService.OnTimerChangedListener {

    private static final String TAG = "ServiceReport_BEFORE";
    private static final String UPLOAD_TAKEN = "BEFORE";
    private int mServiceID; // For DB Purpose to save the file on the ServiceID
    private Context mContext;

    // A. SERVICE ID INFO
    private ServiceJobDBUtil mSJDB;
    private List<ServiceJobWrapper> mSJResultList = null;
    private EditText mEditTextRemarks;
    private String remarks;

    // B. CAMERA Controls
    private static final String IMAGE_DIRECTORY = "upload_before";
    private MaterialDialog mCameraDialog;
    private Bitmap mBitmap;
    private Uri mPicUri;

    /*private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();*/

    /*private final static int ALL_PERMISSIONS_RESULT = 107;*/

    private SJ_UploadsListAdapter mUploadListAdapter; // ListView Setup
    private RecyclerView mUploadResultsList;
    private List<ServiceJobUploadsWrapper> mUploadResults = null;
    private UploadsSJDBUtil mUploadsDB;

    private ImageButton mButtonViewUploadImage;
    private ProgressBar mProgressBarUploading;

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

    private SJ_RecordingsListAdapter mRecordingListAdapter; // ListView Setup
    private RecyclerView mRecordResultsList;
    private List<ServiceJobRecordingWrapper> mResultsList = null;
    private RecordingSJDBUtil mRecodingDB;

    // D. From Activity Objects
    private ArrayList<ServiceJobComplaint_MobileWrapper> mComplaintMobileList = null;

    private SJ_Complaint_CFListAdapter mComplaintCFListAdapter; // ListView Setup
    private RecyclerView mComplaintCFResultsList;
    private ArrayList<ServiceJobComplaint_CFWrapper> mSJComplaintCFList = null;
    private TextView textViewComplaintResult;

    private SJ_Complaint_CFListAdapter mComplaintASRListAdapter; // ListView Setup
    private RecyclerView mComplaintASRResultsList;
    private ArrayList<ServiceJobComplaint_ASRWrapper> mComplaintASRList = null;

    // SJ_ComplaintListExpandableAdapter listAdapter; // Complaints List Adapter SetUp
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    // SlidingPager Tab Set Up, Instance Variables
    private static final String ARG_POSITION = "position";
    private int mPosition;
    private ServiceJobWrapper mServiceJobFromBundle; // From Calling Activity

    public static ServiceReport_FRGMT_BEFORE newInstance(int position, ServiceJobWrapper serviceJob,
             ArrayList<ServiceJobComplaint_CFWrapper> cfWrappers,
             ArrayList<ServiceJobComplaint_MobileWrapper> complaintMobileWrappers,
             ArrayList<ServiceJobComplaint_ASRWrapper> complaintAsrWrappers)
    {
        ServiceReport_FRGMT_BEFORE frag = new ServiceReport_FRGMT_BEFORE();
        Bundle args = new Bundle();

        args.putInt(ARG_POSITION, position);
        args.putParcelable(SERVICE_JOB_ID_KEY, serviceJob);
        args.putParcelableArrayList(SERVICE_JOB_COMPLAINTS_MOBILE_LIST_KEY, complaintMobileWrappers);
        args.putParcelableArrayList(SERVICE_JOB_COMPLAINTS_CF_LIST_KEY, cfWrappers);
        args.putParcelableArrayList(SERVICE_JOB_COMPLAINTS_ASR_LIST_KEY, complaintAsrWrappers);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);
        mServiceJobFromBundle = getArguments().getParcelable(SERVICE_JOB_ID_KEY);
        mComplaintMobileList = getArguments().getParcelableArrayList(SERVICE_JOB_COMPLAINTS_MOBILE_LIST_KEY);
        mSJComplaintCFList = getArguments().getParcelableArrayList(SERVICE_JOB_COMPLAINTS_CF_LIST_KEY);
        mComplaintASRList = getArguments().getParcelableArrayList(SERVICE_JOB_COMPLAINTS_ASR_LIST_KEY);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_service_report_before, container, false);

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
            populateUploadsCardList();
            if (mUploadResults == null) {
            }

            // D. Complaints List
            setUpComplaintsRecyclerView(view);
            setupComplaintsResultsList();
            populateComplaintsCardList();
            if (mResultsList == null) {
            }
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
    }

    // TODO : This should be from PermissionUtil
    private void initPermission() {
        PermissionUtil.initPermissions(getActivity());
        /*permissions.add(CAMERA);
        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }*/
    }

    private void initButton(View view) {
        /** BUTTON BACK */
        Button button_back = (Button) view.findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete the Service Job from SQLite DB on Back
                hideKeyboard();
                ((ServiceJobViewPagerActivity) getActivity()).deleteServiceJob();
                ((ServiceJobViewPagerActivity) getActivity()).backToLandingPage(1);
            }
        });

        /** BUTTON NEXT */
        Button button_next = (Button) view.findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                saveRemarksOnThread(mEditTextRemarks.getText().toString());
                ((ServiceJobViewPagerActivity) getActivity()).fromFragmentNavigate(1);
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

        mSJDB = new ServiceJobDBUtil(getActivity());
        mSJDB.open();
        remarks = mSJDB.getAllJSDetailsByServiceJobID(mServiceID).getBeforeRemarks();
        mSJDB.close();

        mEditTextRemarks.setText(remarks);
        /*mEditTextRemarks.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mEditTextRemarks.setRawInputType(InputType.TYPE_CLASS_TEXT);*/

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
        /*final ScrollView content_service_report_scroll_view = (ScrollView) view.findViewById(R.id.content_service_report_scroll_view);
        mEditTextRemarks.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // content_service_report_scroll_view.fullScroll(ScrollView.FOCUS_DOWN);
                // mEditTextRemarks.setFocusable(true);
                content_service_report_scroll_view.scrollTo(0, content_service_report_scroll_view.getBottom());
                return false;
            }
        });*/
    }

    private void saveRemarksOnThread(String remarks) {
        mSJDB = new ServiceJobDBUtil(getActivity());
        mSJDB.open();
        mSJDB.updateRequestIDRemarks_BEFORE(mServiceID, remarks);
        mSJDB.close();
        Log.e(TAG, "BEFOREsaveRemarksOnThread++");
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
                        mSJDB.updateRequestIDRemarks_BEFORE(mServiceID, input.getText().toString());
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
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    /*********** A.1 END EDIT VIEW POP UP REMARKS ***********/


    /*********** A. SERVICE DETAILS ***********/
    public void fromActivity_onNewSJEntryAdded(String serviceNum) { }
    public void fromActivity_onSJEntryRenamed(String remarks) {
        mEditTextRemarks.setText(remarks);
    }
    public void fromActivity_onSJEntryDeleted() { }

    /**
     * This is implemented aleady in the Helper Fragment List for this class
     * Load SJ from Database
     * @param serviceID
     */
    private void populateServiceJobDetails(int serviceID, View view) {

        // SERVICE JOB Controls
        TextView textViewLabelCustomerName = (TextView) view.findViewById(R.id.textViewLabelCustomerName);
        TextView textViewLabelJobSite = (TextView) view.findViewById(R.id.textViewLabelJobSite);
        TextView textViewLabelServiceNo = (TextView) view.findViewById(R.id.textViewLabelServiceNo);
        TextView textViewLabelTypeOfService = (TextView) view.findViewById(R.id.textViewLabelTypeOfService);
        TextView textViewLabelTelephone = (TextView) view.findViewById(R.id.textViewLabelTelephone);
        TextView textViewLabelFax = (TextView) view.findViewById(R.id.textViewLabelFax);
        TextView textViewLabelEquipmentType = (TextView) view.findViewById(R.id.textViewLabelEquipmentType);
        TextView textViewLabelModel = (TextView) view.findViewById(R.id.textViewLabelModel);
        TextView textViewComplaints = (TextView) view.findViewById(R.id.textViewComplaints);
        TextView textViewRemarksActions = (TextView) view.findViewById(R.id.textViewRemarksActions);

        mSJDB = new ServiceJobDBUtil(getActivity());
        mSJDB.open();
        mSJResultList = mSJDB.getAllJSDetailsByID(serviceID);
        mSJDB.close();

        for (int i = 0; i < mSJResultList.size(); i++) { // Only one is enough
            Log.e(TAG, "DATA: " + mSJResultList.get(i).toString());
            textViewLabelCustomerName.setText(mSJResultList.get(i).getCustomerName());
            textViewLabelJobSite.setText(mSJResultList.get(i).getRemarks());
            textViewLabelServiceNo.setText(mSJResultList.get(i).getServiceNumber());
            textViewLabelTypeOfService.setText(mSJResultList.get(i).getTypeOfService());
            textViewLabelTelephone.setText(mSJResultList.get(i).getTelephone());
            textViewLabelFax.setText(mSJResultList.get(i).getFax());
            textViewLabelEquipmentType.setText(mSJResultList.get(i).getEquipmentType());
            textViewLabelModel.setText(mSJResultList.get(i).getModelOrSerial());
            textViewComplaints.setText(mSJResultList.get(i).getComplaintsOrSymptoms());
            textViewRemarksActions.setText(mSJResultList.get(i).getRemarks());
        }
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

        Log.e(TAG, mUploadResults.toString());

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
                Log.e(TAG, "ServiceJobUploadsWrapper doInBackground=" + sjUp.toString());
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
                    ImageUtility util = new ImageUtility(this.mContext);
                    util.setExternal(true);
                    mBitmap = util.rotateImageIfRequired(mBitmap, mPicUri);
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

    /*private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<>();
        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }*/

    /*private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }*/

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this.mContext)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    /*private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }*/

    /*@TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                break;
        }
    }*/

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
        mRecordingListAdapter = new SJ_RecordingsListAdapter(this.mContext);
        mRecordResultsList.setAdapter(mRecordingListAdapter);
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
            mRecordingListAdapter.swapData(mResultsList);
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
    /*public void setUpComplaintsRecyclerView(View view) {
        mComplaintCFResultsList = (RecyclerView) view.findViewById(R.id.complaint_fault_service_job_list);
    }

    public void setupComplaintsResultsList() {
        mComplaintCFListAdapter = new SJ_Complaint_CFListAdapter(getActivity());
        mComplaintCFResultsList.setAdapter(mComplaintCFListAdapter);
        mComplaintCFResultsList.setLayoutManager(new LinearLayoutManager(this.mContext));
    }

    private void populateComplaintsCardList() {
        // mSJComplaintCFList = ((ServiceJobViewPagerActivity) getActivity()).mComplaintCFList;

        if (mSJComplaintCFList != null && mComplaintMobileList != null && mComplaintASRList != null) {
            //Log.e(TAG, "DATA: " + mResultsList.get(0).toString());
            mComplaintCFResultsList.setHasFixedSize(true);
            mComplaintCFResultsList.setLayoutManager(new LinearLayoutManager(this.mContext));
            mComplaintCFResultsList.setItemAnimator(new DefaultItemAnimator());
            mComplaintCFListAdapter.swapData(mComplaintMobileList, mSJComplaintCFList, mComplaintASRList, true);
        }
    }*/

    // COMPLAINTS List, with Add Action
    public void setUpComplaintsRecyclerView(View view) {
        mComplaintCFResultsList = (RecyclerView) view.findViewById(R.id.complaint_fault_service_job_list);
        /*int pxBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 70, getResources().getDisplayMetrics());
        mComplaintCFResultsList.setPadding(
                mComplaintCFResultsList.getPaddingLeft(),
                mComplaintCFResultsList.getPaddingTop(),
                mComplaintCFResultsList.getPaddingRight(),
                pxBottom);*/
        textViewComplaintResult = (TextView) view.findViewById(R.id.textViewComplaintResult);
    }

    public void setupComplaintsResultsList() {
        mComplaintCFListAdapter = new SJ_Complaint_CFListAdapter(getActivity());
        mComplaintCFResultsList.setAdapter(mComplaintCFListAdapter);
        mComplaintCFResultsList.setLayoutManager(new LinearLayoutManager(this.mContext));
    }

    private void populateComplaintsCardList() {
        // mSJComplaintCFList = ((ServiceJobViewPagerActivity) getActivity()).mComplaintCFList;

        if (mSJComplaintCFList != null && mComplaintMobileList != null && mComplaintASRList != null) {
            mComplaintCFResultsList.setHasFixedSize(true);
            mComplaintCFResultsList.setLayoutManager(new LinearLayoutManager(this.mContext));
            mComplaintCFResultsList.setItemAnimator(new DefaultItemAnimator());
            mComplaintCFListAdapter.swapData(mComplaintMobileList, mSJComplaintCFList, mComplaintASRList, false);
            mComplaintCFResultsList.setVisibility(View.VISIBLE);
            textViewComplaintResult.setVisibility(View.GONE);

        } else {
            Log.e(TAG, "else populateComplaintsCardList: " + mResultsList);
            Log.e(TAG, "else mSJComplaintCFList: " + mSJComplaintCFList);
            Log.e(TAG, "else mComplaintMobileList: " + mComplaintMobileList);
            Log.e(TAG, "else mComplaintASRList: " + mComplaintASRList);

            // No Result
            mComplaintCFResultsList.setVisibility(View.GONE);
            textViewComplaintResult.setVisibility(View.VISIBLE);
            textViewComplaintResult.setText("No complaints this time.");
        }
    }

    /*********** D. END SERVICEJOB COMPLAINTS ***********/
}
