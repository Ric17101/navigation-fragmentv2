package admin4.techelm.com.techelmtechnologies.activity.service_report;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
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
import com.melnykov.fab.FloatingActionButton;

import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_RecordingsListAdapter;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_UploadsListAdapter;
import admin4.techelm.com.techelmtechnologies.db.servicejob.RecordingSJDBUtil;
import admin4.techelm.com.techelmtechnologies.db.servicejob.ServiceJobDBUtil;
import admin4.techelm.com.techelmtechnologies.db.servicejob.UploadsSJDBUtil;
import admin4.techelm.com.techelmtechnologies.activity.menu.MainActivity;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobRecordingWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobUploadsWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.activity.servicejob_main.PopulateServiceJobViewDetails;
import admin4.techelm.com.techelmtechnologies.utility.CameraUtil;
import admin4.techelm.com.techelmtechnologies.utility.PlaybackFragment;
import admin4.techelm.com.techelmtechnologies.utility.RecordingService;

import static android.Manifest.permission.CAMERA;

public class ServiceReport_1 extends AppCompatActivity implements
        SJ_RecordingsListAdapter.CallbackInterface,
        SJ_UploadsListAdapter.CallbackInterface,
        RecordingService.OnTimerChangedListener,
        RecordingSJDBUtil.OnDatabaseChangedListener,
        ServiceJobDBUtil.OnDatabaseChangedListener,
        UploadsSJDBUtil.OnDatabaseChangedListener {

    private static final String TAG = "ServiceReport_1";
    private int mServiceID; // For DB Purpose to save the file on the ServiceID

    // A. SERVICE ID INFO
    private static final String RECORD_JOB_SERVICE_KEY = "SERVICE_JOB";
    private static final String RECORD_SERVICE_KEY = "SERVICE_ID";
    private ServiceJobDBUtil mSJDB;
    private List<ServiceJobWrapper> mSJResultList = null;
    private ServiceJobWrapper mServiceJobFromBundle; // From Calling Activity

    // B. CAMERA Controls
    private static final String IMAGE_DIRECTORY = "upload";
    private MaterialDialog mCameraDialog;
    private Bitmap mBitmap;
    private Uri mPicUri;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 107;

    private SJ_UploadsListAdapter mUploadListAdapter; // ListView Setup
    private RecyclerView mUploadResultsList;
    private List<ServiceJobUploadsWrapper> mUploadResults = null;
    private UploadsSJDBUtil mUploadsDB;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_report_after);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initButton();
        initPermission();

        if (fromBundle() != null) { // if Null don't show anything
            mServiceID = mServiceJobFromBundle.getID();

            // ServiceJob Details
            new PopulateServiceJobViewDetails()
                    .populateServiceJobDetails(
                            this.findViewById(android.R.id.content),
                            mServiceJobFromBundle,
                            View.GONE,
                            TAG);

            // Recording List
            setUpRecordingsRecyclerView();
            setupRecordingsResultsList();
            if (mResultsList == null) {
                populateRecordingsCardList();
            }

            // Upload List
            setUpUploadsRecyclerView();
            setupUploadsResultsList();
            if (mUploadResults == null) {
                populateUploadsCardList();
            }
        } else {
            Snackbar.make(this.findViewById(android.R.id.content), "No data selected from calendar.", Snackbar.LENGTH_LONG)
                    .setAction("OK", null).show();
        }
    }

    /**
     * PARSING data ServiceJob from Bundle passed by the
     *      MainActivity => CalendarFragment
     * @return - ServiceJobWrapper | NULL if no data has been submitted
     */
    private ServiceJobWrapper fromBundle() {
        Intent intent = getIntent();
        return mServiceJobFromBundle = (ServiceJobWrapper) intent.getParcelableExtra(RECORD_JOB_SERVICE_KEY);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    private void initButton() {
        /** BUTTON BACK */
        Button button_back = (Button) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ServiceReport_1.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        /** BUTTON NEXT */
        Button button_next = (Button) findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(ServiceReport_FRGMT_BEFORE.this, PartReplacement_FRGMT_2.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.enter, R.anim.exit);*/

                startActivity(new Intent(ServiceReport_1.this, PartReplacement_2.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, mServiceJobFromBundle));
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        /** BUTTON UPLOAD CAPTURED IMAGE */
        ImageButton buttonViewUploadImage = (ImageButton) findViewById(R.id.buttonViewUploadImage);
        buttonViewUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Do something with the captured image.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            mCameraDialog = showCameraDialog();
            }
        });

        /** BUTTON UPLOAD VOICE */
        ImageButton buttonViewUploadVoice = (ImageButton) findViewById(R.id.buttonViewUploadVoice);
        buttonViewUploadVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Do something with the voice recorded.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                mRecordingDialog = showRecordingDialog();
                initRecordingView(mRecordingDialog.getView());
            }
        });

        /** BUTTON VIEW DETAILS */
        ImageButton buttonViewDetails = (ImageButton) findViewById(R.id.buttonViewDetails);
        buttonViewDetails.setVisibility(View.GONE);
    }

    /*********** A. SERVICE DETAILS ***********/
    @Override
    public void onNewSJEntryAdded(String serviceNum) {

    }

    @Override
    public void onSJEntryUpdated(String fileName) {

    }

    @Override
    public void onSJEntryDeleted() {

    }

    /**
     * Load SJ from Database
     * @param serviceID
     */
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

        mSJDB = new ServiceJobDBUtil(ServiceReport_1.this);
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
    public void setUpUploadsRecyclerView() {
        mUploadResultsList = (RecyclerView) findViewById(R.id.upload_results_service_job_list);
    }

    public void setupUploadsResultsList() {
        mUploadListAdapter = new SJ_UploadsListAdapter(ServiceReport_1.this);
        mUploadResultsList.setAdapter(mUploadListAdapter);
        mUploadResultsList.setLayoutManager(new LinearLayoutManager(ServiceReport_1.this));
    }

    private void populateUploadsCardList() {
        mUploadsDB = new UploadsSJDBUtil(ServiceReport_1.this);
        mUploadsDB.open();
        mUploadResults = mUploadsDB.getAllUploadsBySJID_ByTaken(mServiceID, "");
        mUploadsDB.close();

        for (int i = 0; i < mUploadResults.size(); i++) {
            Log.e(TAG, "DATA: " + mUploadResults.get(i).toString());
        }

        mUploadResultsList.setHasFixedSize(true);
        mUploadResultsList.setLayoutManager(new LinearLayoutManager(ServiceReport_1.this));
        mUploadResultsList.setItemAnimator(new DefaultItemAnimator());
        mUploadListAdapter.swapData(mUploadResults);
        /*new UIThreadHandler(getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });*/
    }

    public MaterialDialog showUploadDialog2(ServiceJobUploadsWrapper serviceJobRecordingWrapper) {
        boolean wrapInScrollView = false;
        MaterialDialog md = new MaterialDialog.Builder(this)
                .title("CAPTURED IMAGE.")
                .customView(R.layout.m_service_report_image_view, wrapInScrollView)
                .positiveText("Close")
                .iconRes(R.mipmap.ic_media_camera)
                .autoDismiss(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();

        // Setting image View based on the FilePath
        ImageView MyImageView = (ImageView) md.findViewById(R.id.imageViewUpload);
        Drawable d = Drawable.createFromPath(serviceJobRecordingWrapper.getFilePath() + "/" +
                serviceJobRecordingWrapper.getUploadName());
        MyImageView.setImageDrawable(d);

        return md;
    }

    private void showUploadDialog(final ServiceJobUploadsWrapper serviceJobRecordingWrapper) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
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
        final CameraUtil camU = new CameraUtil(ServiceReport_1.this, "CAPTURE");
        boolean wrapInScrollView = false;
        MaterialDialog md = new MaterialDialog.Builder(this)
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
                            Snackbar.make(findViewById(android.R.id.content),
                                    "No image to save",
                                    Snackbar.LENGTH_LONG)
                                    .setAction("OK", null).show();
                            /*Toast.makeText(ServiceReport_FRGMT_BEFORE.this,
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
        protected ServiceJobUploadsWrapper doInBackground(CameraUtil... params) {
            camU = params[0];
            if (camU.addJpgUploadToGallery(mBitmap, IMAGE_DIRECTORY)) {
                // Save tp DB
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
        protected void onPostExecute(ServiceJobUploadsWrapper result) {
            if (result != null) {
                mUploadsDB.open();
                mUploadsDB.addUpload(result);
                mUploadsDB.close();
                Snackbar.make(findViewById(android.R.id.content),
                        "Image saved into the Gallery: " + camU.getFilePath(),
                        Snackbar.LENGTH_LONG)
                        .setAction("OK", null).show();
                // Toast.makeText(ServiceReport_FRGMT_BEFORE.this, "Image saved into the Gallery: " + camU.getQuantity(), Toast.LENGTH_SHORT).show();
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        "Unable to store the signature",
                        Snackbar.LENGTH_LONG)
                        .setAction("OK", null).show();
                // Toast.makeText(ServiceReport_FRGMT_BEFORE.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {}

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
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        Log.d("getImage", getImage.getAbsolutePath());
        Log.d("outputFileUri", outputFileUri.getPath());
        return outputFileUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {
            ImageView imageView = (ImageView) mCameraDialog.getCustomView().findViewById(R.id.imageViewCamera);
            if (getPickImageResultUri(data) != null) {
                mPicUri = getPickImageResultUri(data);
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mPicUri);
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

    @Override
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
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<>();
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
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
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

    @Override
    public void onNewUploadsDBEntryAdded(String fileName) {
        mCameraDialog.dismiss();
        populateUploadsCardList();
    }

    @Override
    public void onUploadsDBEntryRenamed(String fileName) {

    }

    @Override
    public void onUploadsDBEntryDeleted() {
        populateUploadsCardList();
    }

    @Override
    public void onHandleUploadsSelection(int position, ServiceJobUploadsWrapper serviceJobRecordingWrapper, int mode) {

    }

    @Override
    public void onHandleDeleteUploadsFromListSelection(final int id) {
        new MaterialDialog.Builder(this)
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
                        mUploadsDB = new UploadsSJDBUtil(ServiceReport_1.this);
                        mUploadsDB.open();
                        mUploadsDB.removeItemWithId(id);
                        mUploadsDB.close();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onHandleViewUploadFromListSelection(ServiceJobUploadsWrapper serviceJobRecordingWrapper) {
        showUploadDialog(serviceJobRecordingWrapper);
    }

    /*********** B. END CAMERA SETUP ***********/


    /*********** C. SOUND RECORDING ***********/
    public void setUpRecordingsRecyclerView() {
        mRecordResultsList = (RecyclerView) findViewById(R.id.recording_results_service_job_list);
    }

    public void setupRecordingsResultsList() {
        mListAdapter = new SJ_RecordingsListAdapter(ServiceReport_1.this);
        mRecordResultsList.setAdapter(mListAdapter);
        mRecordResultsList.setLayoutManager(new LinearLayoutManager(ServiceReport_1.this));
    }

    private void populateRecordingsCardList() {
        mRecodingDB = new RecordingSJDBUtil(ServiceReport_1.this);
        mRecodingDB.open();
        mResultsList = mRecodingDB.getAllRecordingsBySJID_ByTaken(mServiceID, "");
        mRecodingDB.close();

        for (int i = 0; i < mResultsList.size(); i++) {
            Log.e(TAG, "DATA: " + mResultsList.get(i).toString());
        }

        mRecordResultsList.setHasFixedSize(true);
        mRecordResultsList.setLayoutManager(new LinearLayoutManager(ServiceReport_1.this));
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
        MaterialDialog md = new MaterialDialog.Builder(this)
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

        Intent intent = new Intent(ServiceReport_1.this, RecordingService.class);

        if (start) {
            // start recording
            mRecordButton.setImageResource(R.mipmap.ic_media_stop);
            //mPauseButton.setVisibility(View.VISIBLE);
            Snackbar.make(findViewById(android.R.id.content),
                    R.string.toast_recording_start,
                    Snackbar.LENGTH_LONG)
                    .setAction("OK", null).show();
            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TELCHEM/recording");

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
            this.startService(intent);
            //keep screen on while recording
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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

            this.stopService(intent);
            //allow the screen to turn off again once recording is finished
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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

    @Override
    public void onHandleRecordingsSelection(int position, ServiceJobRecordingWrapper serviceJobRecordingWrapper, int mode) {

    }

    @Override
    public void onHandleDeleteRecordingsFromListSelection(final int id) {
        new MaterialDialog.Builder(this)
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
                    mRecodingDB = new RecordingSJDBUtil(ServiceReport_1.this);
                    mRecodingDB.open();
                    mRecodingDB.removeItemWithId(id);
                    mRecodingDB.close();
                }
            })
            .show();
    }

    @Override
    public void onHandlePlayFromListSelection(ServiceJobRecordingWrapper serviceJobRecordingWrapper) {
        try {
            PlaybackFragment playbackFragment =
                    new PlaybackFragment().newInstance(serviceJobRecordingWrapper);

            // FragmentTransaction transaction = ((FragmentActivity)ServiceReport_FRGMT_BEFORE.this)
            FragmentTransaction transaction = (ServiceReport_1.this)
                    .getSupportFragmentManager()
                    .beginTransaction();

            playbackFragment.show(transaction, "dialog_playback");

        } catch (Exception e) {
            Log.e(TAG, "exception", e);
        }
    }

    @Override
    public void onNewRecordingsEntryAdded(String fileName) {
        mRecordingDialog.dismiss();
        Snackbar.make(findViewById(android.R.id.content),
                "Recording " + fileName + " has been added.",
                Snackbar.LENGTH_LONG)
                .setAction("OK", null).show();
        /*Toast.makeText(ServiceReport_FRGMT_BEFORE.this, "Recording " + fileName + " has been added.",
                Toast.LENGTH_SHORT).show();*/
        populateRecordingsCardList();
    }

    @Override
    public void onRecordingsEntryRenamed(String fileName) {
        Snackbar.make(findViewById(android.R.id.content),
                "Recording " + fileName + " has been renamed.",
                Snackbar.LENGTH_LONG)
                .setAction("OK", null).show();
    }

    @Override
    public void onRecordingsEntryDeleted() {
        /*Toast.makeText(ServiceReport_FRGMT_BEFORE.this, "Delete successful.",
                Toast.LENGTH_SHORT).show();*/
        Snackbar.make(findViewById(android.R.id.content),
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
