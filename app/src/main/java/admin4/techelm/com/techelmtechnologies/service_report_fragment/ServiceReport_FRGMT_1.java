package admin4.techelm.com.techelmtechnologies.service_report_fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
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
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.ServiceJobRecordingsListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.ServiceJobUploadsListAdapter;
import admin4.techelm.com.techelmtechnologies.db.RecordingDBUtil;
import admin4.techelm.com.techelmtechnologies.db.ServiceJobDBUtil;
import admin4.techelm.com.techelmtechnologies.db.UploadsDBUtil;
import admin4.techelm.com.techelmtechnologies.menu.MainActivity;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobRecordingWrapper;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobUploadsWrapper;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.servicejob.PopulateServiceJobViewDetails;
import admin4.techelm.com.techelmtechnologies.utility.CameraUtil;
import admin4.techelm.com.techelmtechnologies.utility.PlaybackFragment;
import admin4.techelm.com.techelmtechnologies.utility.RecordingService;

import static android.Manifest.permission.CAMERA;

public class ServiceReport_FRGMT_1 extends Fragment implements
        RecordingService.OnTimerChangedListener {

    private static final String TAG = "ServiceReport_FRGMT_1";
    private int mServiceID; // For DB Purpose to save the file on the ServiceID
    private Context mContext;

    // A. SERVICE ID INFO
    private static final String RECORD_JOB_SERVICE_KEY = "SERVICE_JOB";
    private static final String RECORD_SERVICE_KEY = "SERVICE_ID";
    private ServiceJobDBUtil mSJDB;
    private List<ServiceJobWrapper> mSJResultList = null;
    private static ServiceJobWrapper mServiceJobFromBundle; // From Calling Activity
    private EditText mEditTextRemarks;

    // B. CAMERA Controls
    private static final String IMAGE_DIRECTORY = "upload";
    private MaterialDialog mCameraDialog;
    private Bitmap mBitmap;
    private Uri mPicUri;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 107;

    private ServiceJobUploadsListAdapter mUploadListAdapter; // ListView Setup
    private RecyclerView mUploadResultsList;
    private List<ServiceJobUploadsWrapper> mUploadResults = null;
    private UploadsDBUtil mUploadsDB;

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

    private ServiceJobRecordingsListAdapter mListAdapter; // ListView Setup
    private RecyclerView mRecordResultsList;
    private List<ServiceJobRecordingWrapper> mResultsList = null;
    private RecordingDBUtil mRecodingDB;

    // SlidingPager Tab Set Up
    private static final String ARG_POSITION = "position";
    private int mPosition;

    public static ServiceReport_FRGMT_1 newInstance(int position, ServiceJobWrapper serviceJob) {
        ServiceReport_FRGMT_1 frag = new ServiceReport_FRGMT_1();
        Bundle args = new Bundle();

        args.putInt(ARG_POSITION, position);
        frag.setArguments(args);

        mServiceJobFromBundle = serviceJob;
        return (frag);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_service_report, container, false);

        this.mContext = container.getContext();

        initSpinnerProgessBar(view);
        initButton(view);
        initPermission();

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
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content), "No data selected from calendar.", Snackbar.LENGTH_LONG)
                    .setAction("OK", null).show();
        }

        return view;
    }

    private void initSpinnerProgessBar(View view) {
        mProgressBarUploading = (ProgressBar) view.findViewById(R.id.progressBarUploading);
        mProgressBarUploading.setVisibility(View.GONE);
    }

    private void initPermission() {
        permissions.add(CAMERA);
        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    private void initButton(View view) {
        /** BUTTON BACK */
        Button button_back = (Button) view.findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(getActivity(), MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);*/
                if (mPosition > 0) { // For the purpose of the BEFORE and AFTER TAB page same Fragment and Views
                    ((ServiceJobViewPagerActivity)getActivity()).fromFragmentNavigate(-1);
                } else {
                    Intent intent = new Intent(getActivity(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.left_to_right, R.anim.right_to_left).toBundle();
                    getActivity().startActivity(intent, bundle);
                    getActivity().finish();
                }
            }
        });

        /** BUTTON NEXT */
        Button button_next = (Button) view.findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(ServiceReport_FRGMT_1.this, PartReplacement_FRGMT_2.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.enter, R.anim.exit);*/

                /*startActivity(new Intent(ServiceReport_FRGMT_1.this, PartReplacement_FRGMT_2.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, mServiceJobFromBundle));
                overridePendingTransition(R.anim.enter, R.anim.exit);*/
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
        final String remarks = null == mServiceJobFromBundle.getActionsOrRemarks() ?
                "" : mServiceJobFromBundle.getActionsOrRemarks();
        mEditTextRemarks.setText(remarks);

        mEditTextRemarks.setOnFocusChangeListener((new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        showEditViewRemarksDialog(remarks);
                    }
                })
        );
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
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mEditTextRemarks.setText(input.getText());
                        mSJDB = new ServiceJobDBUtil(getActivity());
                        mSJDB.open();
                        mSJDB.updateRequestIDRemarks(mServiceID, input.getText().toString());
                        mSJDB.close();
                    }
                })
                .show();

        return md;
    }

    /*********** A.1 END EDIT VIEW POP UP REMARKS ***********/


    /*********** A. SERVICE DETAILS ***********/
    public void fromActivity_onNewSJEntryAdded(String serviceNum) {
    }
    public void fromActivity_onSJEntryRenamed(String fileName) {
    }
    public void fromActivity_onSJEntryDeleted() {
    }

    /**
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


    /*********** B. CAMERA SETUP ***********/
    public void setUpUploadsRecyclerView(View view) {
        mUploadResultsList = (RecyclerView) view.findViewById(R.id.upload_results_service_job_list);
    }

    public void setupUploadsResultsList() {
        mUploadListAdapter = new ServiceJobUploadsListAdapter(getActivity());
        mUploadResultsList.setAdapter(mUploadListAdapter);
        mUploadResultsList.setLayoutManager(new LinearLayoutManager(this.mContext));
    }

    private void populateUploadsCardList() {
        mUploadsDB = new UploadsDBUtil(this.mContext);
        mUploadsDB.open();
        mUploadResults = mUploadsDB.getAllUploadsBySJID(mServiceID);
        mUploadsDB.close();

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
        Drawable draw = Drawable.createFromPath(serviceJobRecordingWrapper.getFilePath() + "/" +
                serviceJobRecordingWrapper.getUploadName());
        image.setImageDrawable(draw);

        dialog.show();
    }


    public MaterialDialog showCameraDialog() {
        final CameraUtil camU = new CameraUtil(getActivity(), "CAPTURE");
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
                            Snackbar.make(getActivity().findViewById(android.R.id.content),
                                    "No image to save",
                                    Snackbar.LENGTH_LONG)
                                    .setAction("OK", null).show();
                            /*Toast.makeText(ServiceReport_FRGMT_1.this,
                                    "No image to save", Toast.LENGTH_LONG).show();*/
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

                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Image saved into the Gallery: " + camU.getFilePath(),
                        Snackbar.LENGTH_LONG)
                        .setAction("OK", null).show();
            } else {
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Unable to store the signature",
                        Snackbar.LENGTH_LONG)
                        .setAction("OK", null).show();
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
                    // mBitmap = rotateImageIfRequired(mBitmap, mPicUri);
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
     * @param data the returned data of the activity result
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

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();
        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    // TODO: Study on how to implement separate listerner
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
                        mUploadsDB = new UploadsDBUtil(getActivity());
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
        mListAdapter = new ServiceJobRecordingsListAdapter(this.mContext);
        mRecordResultsList.setAdapter(mListAdapter);
        mRecordResultsList.setLayoutManager(new LinearLayoutManager(this.mContext));
    }

    private void populateRecordingsCardList() {
        mRecodingDB = new RecordingDBUtil(this.mContext);
        mRecodingDB.open();
        mResultsList = mRecodingDB.getAllRecordingsBySJID(mServiceID);
        mRecodingDB.close();

        for (int i = 0; i < mResultsList.size(); i++) {
            Log.e(TAG, "DATA: " + mResultsList.get(i).toString());
        }

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
            Snackbar.make(getActivity().findViewById(android.R.id.content),
                    R.string.toast_recording_start,
                    Snackbar.LENGTH_LONG)
                    .setAction("OK", null).show();
            // Toast.makeText(ServiceReport_FRGMT_1.this ,R.string.toast_recording_start, Toast.LENGTH_SHORT).show();
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

            intent.putExtra(RECORD_SERVICE_KEY, mServiceID); // Param to Service on RecodingService
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
                        mRecodingDB = new RecordingDBUtil(getActivity());
                        mRecodingDB.open();
                        mRecodingDB.removeItemWithId(id);
                        mRecodingDB.close();
                    }
                })
                .show();
    }

    public void fromActivity_onHandlePlayFromListSelection(
            ServiceJobRecordingWrapper serviceJobRecordingWrapper) {
        try {
            PlaybackFragment playbackFragment =
                    new PlaybackFragment().newInstance(serviceJobRecordingWrapper);

            // FragmentTransaction transaction = ((FragmentActivity)ServiceReport_FRGMT_1.this)
            FragmentTransaction transaction = (getActivity())
                    .getSupportFragmentManager()
                    .beginTransaction();

            playbackFragment.show(transaction, "dialog_playback");

        } catch (Exception e) {
            Log.e(TAG, "exception", e);
        }
    }

    public void fromActivity_onNewRecordingsEntryAdded(String fileName) {
        if (mRecordingDialog.isShowing())
            mRecordingDialog.dismiss();
        Snackbar.make(getActivity().findViewById(android.R.id.content),
                "Recording " + fileName + " has been added.",
                Snackbar.LENGTH_LONG)
                .setAction("OK", null).show();
        /*Toast.makeText(ServiceReport_FRGMT_1.this, "Recording " + fileName + " has been added.",
                Toast.LENGTH_SHORT).show();*/
        populateRecordingsCardList();
    }

    public void fromActivity_onRecordingsEntryRenamed(String fileName) {
        Snackbar.make(getActivity().findViewById(android.R.id.content),
                "Recording " + fileName + " has been renamed.",
                Snackbar.LENGTH_LONG)
                .setAction("OK", null).show();
    }

    public void fromActivity_onRecordingsEntryDeleted() {
        /*Toast.makeText(ServiceReport_FRGMT_1.this, "Delete successful.",
                Toast.LENGTH_SHORT).show();*/
        if (mRecordingDialog.isShowing())
            mRecordingDialog.dismiss();
        Snackbar.make(getActivity().findViewById(android.R.id.content),
                "Delete successful.",
                Snackbar.LENGTH_LONG)
                .setAction("OK", null).show();
        populateRecordingsCardList();
    }

    @Override
    public void onTimerChanged(int seconds) { // TODO: If you can implement from Service Class to Activity
    }

    /*********** C. END SOUND RECORDING ***********/

}
