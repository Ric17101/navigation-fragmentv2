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
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import admin4.techelm.com.techelmtechnologies.adapter.ServiceJobPartsListAdapter;
import admin4.techelm.com.techelmtechnologies.db.PartsDBUtil;
import admin4.techelm.com.techelmtechnologies.db.ServiceJobDBUtil;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobNewPartsWrapper;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.activity.servicejob_main.PopulateServiceJobViewDetails;

public class PartReplacement_2 extends AppCompatActivity implements
        ServiceJobPartsListAdapter.CallbackInterface,
        ServiceJobDBUtil.OnDatabaseChangedListener,
        PartsDBUtil.OnDatabaseChangedListener {

    private static final String TAG = "PartReplacement_FRGMT_2";
    private int mServiceID; // For DB Purpose to save the file on the ServiceID

    // A. SERVICE ID INFO
    private static final String RECORD_JOB_SERVICE_KEY = "SERVICE_JOB";
    private ServiceJobDBUtil mSJDB;
    private List<ServiceJobWrapper> mSJResultList = null;
    private ServiceJobWrapper mServiceJobFromBundle; // From Calling Activity

    // B. CAMERA Controls
    private static final String IMAGE_DIRECTORY = "part_replacement";
    private MaterialDialog mCameraDialog;
    private Bitmap mBitmap;
    private Uri mPicUri;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 107;

    private ServiceJobPartsListAdapter mUploadListAdapter; // ListView Setup
    private RecyclerView mUploadResultsList;
    private List<ServiceJobNewPartsWrapper> mUploadResults = null;
    private PartsDBUtil mPartsDB;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_replacement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initButton();

        if (fromBundle() != null) { // if Null don't show anything
            mServiceID = mServiceJobFromBundle.getID();

            // ServiceJob Details
            new PopulateServiceJobViewDetails()
                    .populateServiceJobDetails(
                            this.findViewById(android.R.id.content),
                            mServiceJobFromBundle,
                            View.VISIBLE,
                            TAG);
            // mServiceID = savedInstanceState.getInt(RECORD_SERVICE_KEY);
            // mServiceID = 2;
            // populateServiceJobDetails(mServiceID);

            // Upload List
            setUpUploadsRecyclerView();
            setupUploadsResultsList();
            if (mUploadResults == null) {
                populatePartsCardList();
            }
        }
    }

    /**
     * PARSING data ServiceJob from Bundle passed by the
     *      ServiceReport_FRGMT_BEFORE => PartReplacement_FRGMT_2
     * @return - ServiceJobWrapper | NULL if no data has been submitted
     */
    private ServiceJobWrapper fromBundle() {
        Intent intent = getIntent();
        return mServiceJobFromBundle = (ServiceJobWrapper) intent.getParcelableExtra(RECORD_JOB_SERVICE_KEY);
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
                startActivity(new Intent(PartReplacement_2.this, ServiceReport_1.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, mServiceJobFromBundle));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        /** BUTTON NEXT */
        Button button_next = (Button) findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PartReplacement_2.this, AddReplacementPart_3.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(RECORD_JOB_SERVICE_KEY, mServiceJobFromBundle));
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        /** BUTTON VIEW DETAILS */
        ImageButton buttonViewDetails = (ImageButton) findViewById(R.id.buttonViewDetails);
        buttonViewDetails.setVisibility(View.GONE);
//        if (!buttonViewDetails.isShown())
//            buttonViewDetails.setVisibility(View.VISIBLE);
        buttonViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(PartReplacement_FRGMT_2.this, SigningOff_FRGMT_4.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.enter, R.anim.exit);*/
            }
        });

        /** BUTTON VIEW DETAILS */
        ImageButton buttonViewUploadFile = (ImageButton) findViewById(R.id.buttonViewUploadFileNew);
        buttonViewUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Do something with the file uploaded", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                mCameraDialog = showNewPartDialog();
            }
        });
    }


    /*********** A. SERVICE DETAILS ***********/
    @Override
    public void onNewSJEntryAdded(String serviceNum) {

    }

    @Override
    public void onSJEntryRenamed(String fileName) {

    }

    @Override
    public void onSJEntryDeleted() {

    }

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

        mSJDB = new ServiceJobDBUtil(PartReplacement_2.this);
        mSJDB.open();
        mSJResultList = mSJDB.getAllJSDetailsByID(serviceID);
        mSJDB.close();

        for (int i = 0; i < mSJResultList.size(); i++) {
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
        mUploadResultsList = (RecyclerView) findViewById(R.id.upload_file_results_service_job_list);
    }

    public void setupUploadsResultsList() {
        mUploadListAdapter = new ServiceJobPartsListAdapter(PartReplacement_2.this);
        mUploadResultsList.setAdapter(mUploadListAdapter);
        mUploadResultsList.setLayoutManager(new LinearLayoutManager(PartReplacement_2.this));
    }

    private void populatePartsCardList() {
        mPartsDB = new PartsDBUtil(PartReplacement_2.this);
        mPartsDB.open();
        mUploadResults = mPartsDB.getAllPartsBySJID(mServiceID);
        mPartsDB.close();

        for (int i = 0; i < mUploadResults.size(); i++) {
            Log.e(TAG, "DATA: " + mUploadResults.get(i).toString());
        }

        mUploadResultsList.setHasFixedSize(true);
        mUploadResultsList.setLayoutManager(new LinearLayoutManager(PartReplacement_2.this));
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
        Drawable d = Drawable.createFromPath(serviceJobRecordingWrapper.getQuantity() + "/" +
                serviceJobRecordingWrapper.getPartName());
        MyImageView.setImageDrawable(d);

        return md;
    }

    private void showUploadDialog(final ServiceJobNewPartsWrapper serviceJobRecordingWrapper) {
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
        Drawable draw = Drawable.createFromPath(serviceJobRecordingWrapper.getQuantity() + "/" +
                serviceJobRecordingWrapper.getPartName());
        image.setImageDrawable(draw);

        dialog.show();
    }


    public MaterialDialog showNewPartDialog() {
        boolean wrapInScrollView = false;
        MaterialDialog md = new MaterialDialog.Builder(this)
                .title("UPLOAD IMAGE.")
                .customView(R.layout.m_service_report_camera, wrapInScrollView)
                .negativeText("Save")
                .positiveText("Close")
                .iconRes(R.mipmap.ic_media_camera)
                .autoDismiss(false)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        NewPartSaveOperation ops = new NewPartSaveOperation();
                        ops.execute("Replacement Part"," Quantity", "Unit Price", "Total Price");
                        Snackbar.make(findViewById(android.R.id.content), "No image to save", Snackbar.LENGTH_LONG)
                                .setAction("OK", null).show();
                        dialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).show();
        return md;
    }

    private class NewPartSaveOperation extends AsyncTask<String, Void, ServiceJobNewPartsWrapper> {

        @Override
        protected ServiceJobNewPartsWrapper doInBackground(String... params) {
            ServiceJobNewPartsWrapper sjUp = new ServiceJobNewPartsWrapper();
            if (params.length != 0) {
                sjUp.setReplacementPartName(params[0]);
                sjUp.setQuantity(params[1]);
                sjUp.setUnitPrice(params[2]);
                sjUp.setTotalPrice(params[3]);
                sjUp.setServiceId(mServiceID);
                return sjUp;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ServiceJobNewPartsWrapper result) {
            if (result != null) {
                mPartsDB.open();
                mPartsDB.addNewPart(result, "FRAGMENT2");
                mPartsDB.close();

                populatePartsCardList();

                Snackbar.make(findViewById(android.R.id.content),
                        "Image saved into the Gallery: ",
                        Snackbar.LENGTH_LONG)
                        .setAction("OK", null).show();
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        "Unable to store the signature",
                        Snackbar.LENGTH_LONG)
                        .setAction("OK", null).show();
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
    public void onNewPartsEntryAdded(String fileName) {
        populatePartsCardList();
    }

    @Override
    public void onPartsEntryRenamed(String fileName) {

    }

    @Override
    public void onPartsEntryDeleted() {
        populatePartsCardList();
    }

    @Override
    public void onHandlePartsSelection(int position, ServiceJobNewPartsWrapper serviceJobNewPartsWrapper, int mode) {

    }

    @Override
    public void onHandleDeletePartsFromListSelection(final int id) {
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
                        mPartsDB = new PartsDBUtil(PartReplacement_2.this);
                        mPartsDB.open();
                        mPartsDB.removeItemWithId(id);
                        mPartsDB.close();
                        dialog.dismiss();
                    }
                })
                .build()
                .show();
    }

    @Override
    public void onHandleViewPartFromListSelection(ServiceJobNewPartsWrapper serviceJobPartWrapper) {
        showUploadDialog(serviceJobPartWrapper);
    }

    /*********** B. END CAMERA SETUP ***********/
}
