package admin4.techelm.com.techelmtechnologies.activity.toolbox_meeting_main.fragment;

import android.annotation.TargetApi;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_UploadsListAdapter;
import admin4.techelm.com.techelmtechnologies.db.servicejob.UploadsSJDBUtil;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobUploadsWrapper;
import admin4.techelm.com.techelmtechnologies.utility.CameraUtil;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;
import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;
import me.sudar.zxingorient.Barcode;
import me.sudar.zxingorient.ZxingOrient;
import me.sudar.zxingorient.ZxingOrientResult;

import static android.Manifest.permission.CAMERA;

/**
 *
 */
public class AttendanceFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    Button btnScanCode;
    private static final String TAG = AttendanceFragment.class.getSimpleName();
    private static final int REQUEST_CAMERA = 0x00000011;
    ArrayList<String> list;
    ListView listAttendees;

    private static final String UPLOAD_TAKEN = "MEETING_IMAGE";
    private int mServiceID; // For DB Purpose to save the file on the ServiceID
    private static final String IMAGE_DIRECTORY = "meeting_image";
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

    private ImageButton mButtonViewUploadImage;
    private ProgressBar mProgressBarUploading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.attendance_layout, null);
        initButton(view);
        initPermission();
        initSpinnerProgessBar(view);
        //setUpViews(view);

        this.mContext = container.getContext();

        btnScanCode = (Button) view.findViewById(R.id.btnScanCode);
        btnScanCode.setOnClickListener(this);

        list = new ArrayList<>();

        return view;
    }

    /**
     * These Two Lines should be included on every Fragment to maintain the state and donnot load again
     *
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
                ((ToolboxMeetingPagerActivity) getActivity()).backToToolboxLandingPage(5);
            }
        });

        /** BUTTON NEXT */
        Button button_next = (Button) view.findViewById(R.id.button_next);
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

        /** BUTTON UPLOAD CAPTURED IMAGE */
        mButtonViewUploadImage = (ImageButton) view.findViewById(R.id.buttonViewUploadImage1);
        mButtonViewUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraDialog = showCameraDialog();
            }
        });

        listAttendees = (ListView) view.findViewById(R.id.listAttendees);
        listAttendees.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
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

    private void initSpinnerProgessBar(View view) {
        mProgressBarUploading = (ProgressBar) view.findViewById(R.id.progressBarUploading);
        mProgressBarUploading.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScanCode:

                Log.i("MyActivity", "heyxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

                new ZxingOrient(AttendanceFragment.this)
                        .setInfo("QR code Scanner")
                        //.setToolbarColor("#c099cc00")
                        //.setInfoBoxColor("#c099cc00")
                        .setBeep(false)
                        .setVibration(true)
                        .initiateScan(Barcode.QR_CODE);


                break;
        }
    }

    private void setUpViews(View view) {
        // Upload List
        setUpUploadsRecyclerView(view);
        setupUploadsResultsList();
        populateUploadsCardList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ZxingOrientResult scanResult = ZxingOrient.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && scanResult.getContents() != null) {

            list.add(scanResult.getContents());

            ArrayAdapter<String> arrayAdapter =
                    new ArrayAdapter<String>(this.mContext,android.R.layout.simple_list_item_1, list);
            // Set The Adapter
            listAttendees.setAdapter(arrayAdapter);

            Toast.makeText(getActivity(),
                    scanResult.getContents() + " has been scanned.", Toast.LENGTH_LONG).show();
            for (String scanned : list){
                Log.i("Scanned Items: ", scanned);
            }
        }
        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK && scanResult == null) {
            ImageView imageView = (ImageView) mCameraDialog.getCustomView().findViewById(R.id.imageViewCamera);
            if (getPickImageResultUri(intent) != null) {
                mPicUri = getPickImageResultUri(intent);
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mPicUri);
                    mBitmap = ImageUtility.rotateImageIfRequired(mBitmap, mPicUri);
                    // mBitmap = getResizedBitmap(mBitmap, 500);

                    /*CircleImageView croppedImageView = (CircleImageView) mCameraDialog.getCustomView().findViewById(R.id.img_profile);
                    croppedImageView.setImageBitmap(mBitmap);*/
                    imageView.setImageBitmap(mBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = (Bitmap) intent.getExtras().get("data");

                mBitmap = bitmap;
                /*CircleImageView croppedImageView = (CircleImageView) mCameraDialog.getCustomView().findViewById(R.id.img_profile);
                if (croppedImageView != null) {
                    croppedImageView.setImageBitmap(mBitmap);
                }*/
                imageView.setImageBitmap(mBitmap);
            }
        }
    }

    /*********** B. CAMERA SETUP ***********/
    public void setUpUploadsRecyclerView(View view) {
        mUploadResultsList = (RecyclerView) view.findViewById(R.id.upload_results_service_job_list);
    }

    public void setupUploadsResultsList() {
        /*mUploadListAdapter = new SJ_UploadsListAdapter(getActivity());
        mUploadResultsList.setAdapter(mUploadListAdapter);
        mUploadResultsList.setLayoutManager(new LinearLayoutManager(this.mContext));*/
    }

    private void populateUploadsCardList() {
        /*mUploadsDB = new UploadsSJDBUtil(this.mContext);
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
            *//*new UIThreadHandler(getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });*//*
        }*/
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
                /*ServiceJobUploadsWrapper sjUp = new ServiceJobUploadsWrapper();
                sjUp.setUploadName(camU.getFileName());
                sjUp.setFilePath(camU.getFilePath());
                sjUp.setTaken(UPLOAD_TAKEN);
                sjUp.setServiceId(mServiceID);
                return sjUp;*/
                Log.i("IMAGE SAVE", "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
                SnackBarNotificationUtil
                        .setSnackBar(getActivity().findViewById(android.R.id.content),
                                "Image saved into the Gallery.")
                        .setColor(getResources().getColor(R.color.colorPrimary1))
                        .show();

                return null;
            } else {

                Log.i("IMAGE SAVE", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                return null;
            }

        }

        @Override
        protected void onPostExecute(ServiceJobUploadsWrapper sjUP) {
            /*if (sjUP != null) {

                mUploadsDB.open(); // Save Upload to DB as record and runon UIThread
                mUploadsDB.addUpload(sjUP);
                mUploadsDB.close();

                SnackBarNotificationUtil
                        .setSnackBar(getActivity().findViewById(android.R.id.content),
                                "Image saved into the Gallery." + camU.getFilePath())
                        .setColor(getResources().getColor(R.color.colorPrimary1))
                        .show();
            } else {
                SnackBarNotificationUtil
                        .setSnackBar(getActivity().findViewById(android.R.id.content),
                                "Unable to save the signature")
                        .setColor(getResources().getColor(R.color.colorPrimary1))
                        .show();
            }*/

            if (mProgressBarUploading.isShown()) {
                mButtonViewUploadImage.setVisibility(View.VISIBLE);
                mButtonViewUploadImage.setImageBitmap(mBitmap);
                mButtonViewUploadImage.setScaleType(ImageView.ScaleType.FIT_XY);
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
}
