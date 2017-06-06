package admin4.techelm.com.techelmtechnologies.activity.service_report_fragment;

import android.annotation.TargetApi;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.adapter.SJ_PartsListAdapter;
import admin4.techelm.com.techelmtechnologies.db.servicejob.PartsSJDBUtil;
import admin4.techelm.com.techelmtechnologies.db.servicejob.ServiceJobDBUtil;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobNewPartsWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.activity.servicejob_main.PopulateServiceJobViewDetails;
import admin4.techelm.com.techelmtechnologies.utility.CameraUtil;

public class PartReplacement_FRGMT_2_v1 extends Fragment {

    private static final String TAG = "PartReplacement_FRGMT_2";
    private int mServiceID; // For DB Purpose to save the file on the ServiceID
    private Context mContext;

    // A. SERVICE ID INFO
    private static final String RECORD_JOB_SERVICE_KEY = "SERVICE_JOB";
    private ServiceJobDBUtil mSJDB;
    private List<ServiceJobWrapper> mSJResultList = null;
    private static ServiceJobWrapper mServiceJobFromBundle; // From Calling Activity

    // B. CAMERA Controls
    private static final String IMAGE_DIRECTORY = "part_replacement";
    private MaterialDialog mCameraDialog;
    private Bitmap mBitmap;
    private Uri mPicUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private SJ_PartsListAdapter mUploadListAdapter; // ListView Setup
    private RecyclerView mUploadResultsList;
    private List<ServiceJobNewPartsWrapper> mUploadResults = null;
    private PartsSJDBUtil mPartsDB;
    private CardView cardViewNewUpload; // TODO: Test this if has content else donot show "New Replacement Part Added. with check"

    private ImageButton mButtonViewUploadFileNew;
    private ProgressBar mProgressBarUploadingNew;

    // SlidingPager Tab Set Up
    private static final String ARG_POSITION = "position";
    private int position;

    // Form New Replacement Part
    private LinearLayout mLinearLayoutNewPartDetails;
    private LinearLayout mLinearLayoutNewPartForm;

    public static PartReplacement_FRGMT_2_v1 newInstance(int position, ServiceJobWrapper serviceJob) {
        PartReplacement_FRGMT_2_v1 frag = new PartReplacement_FRGMT_2_v1();
        Bundle args = new Bundle();

        args.putInt(ARG_POSITION, position);
        frag.setArguments(args);

        mServiceJobFromBundle = serviceJob;
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_part_replacement, container, false);

        this.mContext = container.getContext();
        initLinearView(view);

        initSpinnerProgessBar(view);
        initButton(view);

        if (mServiceJobFromBundle != null) { // if Null don't show anything
            mServiceID = mServiceJobFromBundle.getID();

            // ServiceJob Details
            new PopulateServiceJobViewDetails()
                    .populateServiceJobDetails(
                            view,
                            mServiceJobFromBundle,
                            View.VISIBLE,
                            TAG);
            // mServiceID = savedInstanceState.getInt(RECORD_SERVICE_KEY);
            // mServiceID = 2;
            // populateServiceJobDetails(mServiceID);

            // Upload List
            setUpUploadsRecyclerView(view);
            setupUploadsResultsList();
            if (mUploadResults == null) {
                populateUploadsCardList();
            }
        }

        return view;
    }

    // Request Details and Forms for adding new Replacement Part
    private void initLinearView(View view) {
        mLinearLayoutNewPartDetails = (LinearLayout) view.findViewById(R.id.linearLayoutNewPartDetails);
        mLinearLayoutNewPartDetails.setVisibility((View.VISIBLE));
//        mLinearLayoutNewPartForm = (LinearLayout) view.findViewById(R.id.linearLayoutNewPartForm);
//        mLinearLayoutNewPartForm.setVisibility((View.GONE));
    }

    private void swapPartViews() {
        if (mLinearLayoutNewPartDetails.getVisibility() == View.GONE) {
            mLinearLayoutNewPartDetails.setVisibility((View.VISIBLE));
            mLinearLayoutNewPartForm.setVisibility((View.GONE));
        } else {
            mLinearLayoutNewPartDetails.setVisibility((View.GONE));
            mLinearLayoutNewPartForm.setVisibility((View.VISIBLE));
        }
    }

    private void initSpinnerProgessBar(View view) {
        mProgressBarUploadingNew = (ProgressBar) view.findViewById(R.id.progressBarUploadingNew);
        mProgressBarUploadingNew.setVisibility(View.GONE);
    }

    private void initButton(View view) {

        /** BUTTON BACK */
        Button button_back = (Button) view.findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(PartReplacement_FRGMT_2.this, ServiceReport_FRGMT_BEFORE.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, mServiceJobFromBundle));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);*/
                ((ServiceJobViewPagerActivity)getActivity()).fromFragmentNavigate(-1);
            }
        });

        /** BUTTON NEXT */
        Button button_next = (Button) view.findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(PartReplacement_FRGMT_2.this, AddReplacementPart_FRGMT_3.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, mServiceJobFromBundle));
                overridePendingTransition(R.anim.enter, R.anim.exit);*/
                ((ServiceJobViewPagerActivity)getActivity()).fromFragmentNavigate(1);
            }
        });

        /** BUTTON VIEW DETAILS */
        ImageButton buttonViewDetails = (ImageButton) view.findViewById(R.id.buttonViewDetails);
        buttonViewDetails.setVisibility(View.GONE);
        /*if (!buttonViewDetails.isShown()) {
            buttonViewDetails.setVisibility(View.VISIBLE);
        }
        buttonViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(PartReplacement_FRGMT_2.this, SigningOff_FRGMT_4.class)
//                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });*/

        /** BUTTON UPLOAD NEW REPLACEMENT PART */
        mButtonViewUploadFileNew = (ImageButton) view.findViewById(R.id.buttonViewUploadFileNew);
        mButtonViewUploadFileNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Do something with the file uploaded", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                swapPartViews();
                mCameraDialog = showCameraDialog();
            }
        });
    }


    /*********** A. SERVICE DETAILS ***********/
    /*@Override
    public void onNewIPI_PWDEntryAdded(String serviceNum) {

    }

    @Override
    public void onIPI_PWDEntryUpdated(String fileName) {

    }

    @Override
    public void onIPI_PWDEntryDeleted() {

    }*/

    private void populateServiceJobDetails(int serviceID) {

        // SERVICE JOB Controls
        /*TextView textViewLabelCustomerName = (TextView) findViewById(R.id.textViewLabelCustomerName);
        TextView textViewLabelJobSite = (TextView) findViewById(R.id.textViewLabelJobSite);
        TextView textViewLabelServiceNo = (TextView) findViewById(R.id.textViewLabelServiceNo);
        TextView textViewLabelTypeOfService = (TextView) findViewById(R.id.textViewLabelTypeOfService);
        TextView textViewLabelTelephone = (TextView) findViewById(R.id.textViewLabelTelephone);
        TextView textViewLabelFax = (TextView) findViewById(R.id.textViewLabelFax);
        TextView textViewLabelEquipmentType = (TextView) findViewById(R.id.textViewLabelEquipmentType);
        TextView textViewLabelModel = (TextView) findViewById(R.id.textViewLabelModel);
        TextView textViewComplaints = (TextView) findViewById(R.id.textViewComplaints);
        TextView textViewRemarksActions = (TextView) findViewById(R.id.textViewRemarksActions);

        mSJDB = new ServiceJobDBUtil(PartReplacement_FRGMT_2.this);
        mSJDB.open();
        mSJResultList = mSJDB.getAllJSDetailsByID(serviceID);
        mSJDB.close();

        for (int i = 0; i < mSJResultList.size(); i++) {
            Log.e(TAG, "DATA: " + mSJResultList.get(i).toString());
            textViewLabelCustomerName.setText(mSJResultList.get(i).getCustomerID());
            textViewLabelJobSite.setText(mSJResultList.get(i).getRemarks());
            textViewLabelServiceNo.setText(mSJResultList.get(i).getServiceNumber());
            textViewLabelTypeOfService.setText(mSJResultList.get(i).getTypeOfService());
            textViewLabelTelephone.setText(mSJResultList.get(i).getTelephone());
            textViewLabelFax.setText(mSJResultList.get(i).getFax());
            textViewLabelEquipmentType.setText(mSJResultList.get(i).getEquipmentType());
            textViewLabelModel.setText(mSJResultList.get(i).getModelOrSerial());
            textViewComplaints.setText(mSJResultList.get(i).getComplaintsOrSymptoms());
            textViewRemarksActions.setText(mSJResultList.get(i).getRemarks());
        }*/
    }
    /*********** A. END SERVICE DETAILS ***********/


    /*********** B. CAMERA SETUP
     * @param view***********/
    public void setUpUploadsRecyclerView(View view) {
        mUploadResultsList = (RecyclerView) view.findViewById(R.id.upload_file_results_service_job_list);
    }

    public void setupUploadsResultsList() {
        mUploadListAdapter = new SJ_PartsListAdapter(getActivity());
        mUploadResultsList.setAdapter(mUploadListAdapter);
        mUploadResultsList.setLayoutManager(new LinearLayoutManager(this.mContext));
    }

    private void populateUploadsCardList() {
        mPartsDB = new PartsSJDBUtil(this.mContext);
        mPartsDB.open();
        mUploadResults = mPartsDB.getAllPartsBySJID(mServiceID);
        mPartsDB.close();

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

    public MaterialDialog showUploadDialog2(ServiceJobNewPartsWrapper serviceJobRecordingWrapper) {
        boolean wrapInScrollView = false;
        MaterialDialog md = new MaterialDialog.Builder(this.mContext)
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
        Drawable d = Drawable.createFromPath(serviceJobRecordingWrapper.getQuantity() + "/" +
                serviceJobRecordingWrapper.getPartName());
        MyImageView.setImageDrawable(d);

        return md;
    }

    private void showUploadDialog(final ServiceJobNewPartsWrapper serviceJobRecordingWrapper) {
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
        Drawable draw = Drawable.createFromPath(serviceJobRecordingWrapper.getQuantity() + "/" +
                serviceJobRecordingWrapper.getPartName());
        image.setImageDrawable(draw);

        dialog.show();
    }


    public MaterialDialog showCameraDialog() {
        final CameraUtil camU = new CameraUtil(getActivity(), "PART");
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
                            PartReplacement_FRGMT_2_v1.ImageSaveOperation ops = new PartReplacement_FRGMT_2_v1.ImageSaveOperation();
                            ops.execute(camU);

                            dialog.dismiss();
                        } else {
                            // Toast.makeText(PartReplacement_FRGMT_2.this, "No image to save", Toast.LENGTH_LONG).show();
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "No image to save", Snackbar.LENGTH_LONG)
                                    .setAction("OK", null).show();
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

    private class ImageSaveOperation extends AsyncTask<CameraUtil, Void, ServiceJobNewPartsWrapper> {
        private CameraUtil camU;

        @Override
        protected void onPreExecute() {
            mButtonViewUploadFileNew.setVisibility(View.GONE);
            mProgressBarUploadingNew.setVisibility(View.VISIBLE);
        }

        @Override
        protected ServiceJobNewPartsWrapper doInBackground(CameraUtil... params) {
            camU = params[0];
            if (camU.addJpgUploadToGallery(mBitmap, IMAGE_DIRECTORY)) {
                ServiceJobNewPartsWrapper sjUp = new ServiceJobNewPartsWrapper(); // Prepare record data
                sjUp.setReplacementPartName(camU.getFileName());
                sjUp.setQuantity(camU.getFilePath());
                sjUp.setServiceId(mServiceID);
                return sjUp;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ServiceJobNewPartsWrapper result) {
            if (result != null) {
                mPartsDB.open(); // Save data to DB
                mPartsDB.addNewPart(result, "FRAGMENT2");
                mPartsDB.close();

                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Image saved into the Gallery: " + camU.getFilePath(),
                        Snackbar.LENGTH_LONG)
                        .setAction("OK", null).show();

                populateUploadsCardList();
            } else {
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Unable to store the signature",
                        Snackbar.LENGTH_LONG)
                        .setAction("OK", null).show();
                // Toast.makeText(PartReplacement_FRGMT_2.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
            }

            if (mProgressBarUploadingNew.isShown()) {
                mButtonViewUploadFileNew.setVisibility(View.VISIBLE);
                mProgressBarUploadingNew.setVisibility(View.GONE);
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
            /*if (!hasPermission(perm)) {
                aResponse.add(perm);
            }*/
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

    public void fromActivity_onNewUploadsEntryAdded(String fileName) {
        populateUploadsCardList();
    }
    public void fromActivity_onUploadsEntryRenamed(String fileName) {

    }
    public void fromActivity_onUploadsEntryDeleted() {
        populateUploadsCardList();
    }


    public void fromActivity_onHandlePartsSelection(
            int position, ServiceJobNewPartsWrapper serviceJobNewPartsWrapper, int mode) {

    }
    public void fromActivity_onHandleDeletePartsFromListSelection(final int id) {
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
                        mPartsDB = new PartsSJDBUtil(getActivity());
                        mPartsDB.open();
                        mPartsDB.removeItemWithId(id);
                        mPartsDB.close();
                        dialog.dismiss();
                    }
                })
                .show();
    }
    public void fromActivity_onHandleViewPartFromListSelection(ServiceJobNewPartsWrapper serviceJobPartWrapper) {
        showUploadDialog(serviceJobPartWrapper);
    }


    /*********** B. END CAMERA SETUP ***********/
}
