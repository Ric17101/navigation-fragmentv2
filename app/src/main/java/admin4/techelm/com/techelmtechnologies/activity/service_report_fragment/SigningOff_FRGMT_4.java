package admin4.techelm.com.techelmtechnologies.activity.service_report_fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONException;

import java.io.File;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.db.PartsDBUtil;
import admin4.techelm.com.techelmtechnologies.db.RecordingDBUtil;
import admin4.techelm.com.techelmtechnologies.db.ServiceJobDBUtil;
import admin4.techelm.com.techelmtechnologies.db.UploadsDBUtil;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;
import admin4.techelm.com.techelmtechnologies.utility.json.JSONHelper;
import admin4.techelm.com.techelmtechnologies.activity.menu.MainActivity;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobNewPartsWrapper;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobRecordingWrapper;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobUploadsWrapper;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;
import admin4.techelm.com.techelmtechnologies.activity.servicejob_main.PopulateServiceJobViewDetails;
import admin4.techelm.com.techelmtechnologies.utility.SignatureUtil;
import admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm.ServiceJobJSON_POST;
import admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm.ServiceJobUploadFile_VolleyPOST;

public class SigningOff_FRGMT_4 extends Fragment {

    private static final String TAG = SigningOff_FRGMT_4.class.getSimpleName();
    private static String SERVICE_JOB_UPLOAD_URL =
            "http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/index.php/Servicejob/";
    private Context mContext;
    private int mServiceID; // For DB Purpose to save the file on the ServiceID
    private int notificationId = 1;
    private static final int PROGRESS_MAX = 100;
    private static final int PROGRESS_CAPTURES = 45;
    private static final int PROGRESS_RECORDINGS = 30;
    private static final int PROGRESS_SIGNATURE = 15;
    private static final int PROGRESS_NEW_PARTS = 10;
    private static final int PROGRESS_ERROR = 0;
    private UploadDataWithNotificationTASK uploadTask;
    private Button button_next;

    // A. SERVICE ID INFO
    private static final String RECORD_JOB_SERVICE_KEY = "SERVICE_JOB";
    private static ServiceJobWrapper mServiceJobFromBundle; // From Calling Activity
    private ServiceJobDBUtil mSJDB;
    private List<ServiceJobWrapper> mSJResultList = null;

    // B. SIGNATURE PAD
    private RelativeLayout mRelativeLayout;

    private ImageButton imageButtonViewSignature;
    private SignaturePad mSignaturePad;
    private SignatureUtil mSignUtil;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    // SlidingPager Tab Set Up
    private static final String ARG_POSITION = "position";
    private int position;

    public static SigningOff_FRGMT_4 newInstance(int position, ServiceJobWrapper serviceJob) {
        SigningOff_FRGMT_4 frag = new SigningOff_FRGMT_4();
        Bundle args = new Bundle();

        args.putInt(ARG_POSITION, position);
        frag.setArguments(args);

        mServiceJobFromBundle = serviceJob;
        return (frag);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_signing_off_end_task, container, false);

        this.mContext = container.getContext();

        initSignaturePadPopUp(view);
        initButton(view);

        if (this.mServiceJobFromBundle != null) { // if Null don't show anything
            mServiceID = mServiceJobFromBundle.getID();

            // ServiceJob Details
            new PopulateServiceJobViewDetails()
                    .populateServiceJobDetails(
                            view, //view.findViewById(android.R.id.content),
                            mServiceJobFromBundle,
                            View.GONE,
                            TAG);
        }

        return view;
    }

    private void setEndTaskButton() {
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSubmitSuccess()) {
                    /*Intent intent = new Intent(getActivity(), ServiceReport_TaskCompleted_5.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.enter, R.anim.exit).toBundle();
                    getActivity().startActivity(intent, bundle);
                    getActivity().finish();*/
                }
                button_next.setOnClickListener(null);
            }
        });
    }

    private void initButton(View view) {
        /** BUTTON BACK */
        Button button_back = (Button) view.findViewById(R.id.button_back);
        button_back.setVisibility(View.GONE);

        /** BUTTON NEXT */
        button_next = (Button) view.findViewById(R.id.button_next);
        button_next.setText("END TASK");
        setEndTaskButton();

        /** BUTTON VIEW DETAILS */
        ImageButton buttonViewDetails = (ImageButton) view.findViewById(R.id.buttonViewDetails);
        buttonViewDetails.setVisibility(View.GONE);
    }

    public MaterialDialog showSigningDialog(View view) {
        mSignUtil = new SignatureUtil(getActivity(), mSignaturePad);
        boolean wrapInScrollView = false;
        MaterialDialog md = new MaterialDialog.Builder(this.mContext)
                .title("SIGNATURE.")
                .customView(R.layout.m_signing_off_signature, wrapInScrollView)
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
                        mSignaturePad = (SignaturePad) dialog.getCustomView().findViewById(R.id.signature_pad);
                        mSignaturePad.clear();
                        mSignUtil = new SignatureUtil(getActivity(), mSignaturePad); // Clear the Signature pad?
                        setClearImageSignature();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mSignaturePad = (SignaturePad) dialog.getCustomView().findViewById(R.id.signature_pad);
                        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
                            @Override
                            public void onStartSigning() {
                                // Toast.makeText(SigningOff_FRGMT_4.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSigned() {
                                /*mSaveButton.setEnabled(true);
                                mClearButton.setEnabled(true);*/
                            }

                            @Override
                            public void onClear() {
                                /*mSaveButton.setEnabled(false);
                                mClearButton.setEnabled(false);*/
                            }
                        });
                        Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                        // TODO: Add AsyncTask Here...
                        if (mSignUtil.addJpgSignatureToGallery(signatureBitmap, "signature")) {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Signature saved into the Gallery:" + mSignUtil.getFilePath(), Snackbar.LENGTH_LONG)
                                    .setAction("OK", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        })
                                    .setActionTextColor(getResources().getColor(R.color.colorPrimary1))
                                    .show();

                            saveSignatureToDBOnAfterSaveToGallery(mSignUtil); // SAVING FILES to DB

                            setDrawableImageSignature(mSignUtil.loadBitmap());
                            dialog.dismiss();
                        } else {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Unable to store the signature", Snackbar.LENGTH_LONG)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(R.color.colorPrimary1))
                                    .show();
                        }

                        /*
                         IF YOU WANT TO SAVE FILES/IAMGE IN *.SVG
                        if (mSignUtil.addSvgSignatureToGallery(mSignaturePad.getSignatureSvg())) {
                            Toast.makeText(SigningOff_FRGMT_4.this, "SVG Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SigningOff_FRGMT_4.this, "Unable to store the SVG signature", Toast.LENGTH_SHORT).show();
                        }*/
                    }
                })
                .show();

        return md;
        /*new MaterialDialog.Builder(this)
                .title(getString(R.string.about_dialog_title, VERSION))
                .positiveText(R.string.dismiss)
                .content(Html.fromHtml(getString(R.string.about_body)))
                .iconRes(R.drawable.ic_translate)
                .show();*/
    }

    private void initSignaturePadPopUp(View view) {
        imageButtonViewSignature = (ImageButton) view.findViewById(R.id.imageButtonViewSignature);
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl);
        // Set a click listener for the text view
        imageButtonViewSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog mDialog = showSigningDialog(view);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the aResponse arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "Cannot write images to external storage",
                            Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary1))
                            .show();
                    // Toast.makeText(SigningOff_FRGMT_4.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void setClearImageSignature() {
        BitmapDrawable ob = new BitmapDrawable(getResources(), String.valueOf(getResources().getDrawable(R.drawable.composea)));
        imageButtonViewSignature.setBackground(ob);
        // imageButtonViewSignature.setBackgroundDrawable(ob);
    }

    private void setDrawableImageSignature(Bitmap drawableImageSignature) {
        BitmapDrawable ob = new BitmapDrawable(getResources(), drawableImageSignature);
        imageButtonViewSignature.setBackground(ob);
        // imageButtonViewSignature.setBackgroundDrawable(ob);

        // Set to wrap content signature
        ViewGroup.LayoutParams params = imageButtonViewSignature.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        imageButtonViewSignature.setLayoutParams(params);
    }

    // This is used and Call at CalendarFragment also
    private void noInternetSnackBar() {
        Snackbar.make(getActivity().getCurrentFocus(), "No internet connection.", Snackbar.LENGTH_LONG)
                .setAction("OK", null).show();
    }

    /*********** A. SERVICE DETAILS ***********/
    public void fromActivity_onNewSJEntryAdded(String serviceNum) {
        System.out.print("onNewSJEntryAdded: Called");
    }
    public void fromActivity_onSJEntryRenamed(String fileName) {
        System.out.print("onSJEntryRenamed: Called");
    }
    public void fromActivity_onSJEntryDeleted() {
        System.out.print("onSJEntryDeleted: Called");
    }

    /*********** A. END SERVICE DETAILS ***********/

    private void saveSignatureToDBOnAfterSaveToGallery(final SignatureUtil sign) {
        Log.e(TAG, mServiceID+" FName: "+sign.getFileName()+" FPath"+ sign.getFilePath());
        Runnable run = new Runnable() {
            @Override
            public void run() {
                mSJDB = new ServiceJobDBUtil(getActivity());
                mSJDB.open();
                mSJDB.updateRequestIDSignature(mServiceID, sign.getFileName(), sign.getFilePath()+"/");
                mSJDB.close();
            }
        };
        new Thread(run).start();
    }

    private boolean onSubmitSuccess() {
        if (new JSONHelper().isConnected(getActivity())) {
            new TestDBIfUserUploadedFiles().execute("");
        } else {
            noInternetSnackBar();
            return false;
        }
        return true;
    }

    // TODO: Should this separate from CLASS
    private class TestDBIfUserUploadedFiles extends AsyncTask<String, Void, ServiceJobWrapper>{
        boolean aHasRecordings = true;
        boolean aHasUploads = true;

        @Override
        protected ServiceJobWrapper doInBackground(String... params) {
            aHasRecordings = testIfRecordingsHasRecordings();
            aHasUploads = testIfUploadsHasCaptures();

            // Get Service Job Details
            ServiceJobWrapper sjw = getSJDetails();

            Log.e(TAG, "Service Job ID " + mServiceID);
            Log.e(TAG, sjw.toString());
            Log.e(TAG, mServiceJobFromBundle.toString());
            return sjw;
        }

        @Override
        protected void onPostExecute(ServiceJobWrapper sjw) {
            if (sjw != null && aHasUploads && aHasRecordings) {
                notifyUserOnSubmit(sjw);
            } else {
                setEndTaskButton();
                Snackbar.make(getActivity()
                        .findViewById(android.R.id.content),
                        "Please complete the report before end task.",
                        Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.white)) // colorPrimary1
                        .show();
            }
        }

        private boolean testIfUploadsHasCaptures() {
            // TEST Uploads
            UploadsDBUtil mUploadsDB = new UploadsDBUtil(getActivity());
            mUploadsDB.open();
            boolean result = mUploadsDB.hasInsertedRecordings(mServiceID);
            mUploadsDB.close();
            return result;
        }

        private boolean testIfRecordingsHasRecordings() {
            // TEST Recordings
            RecordingDBUtil mRecordingsDB = new RecordingDBUtil(getActivity());
            mRecordingsDB.open();
            boolean result = mRecordingsDB.hasInsertedRecordings(mServiceID);
            mRecordingsDB.close();
            return result;
        }

        private ServiceJobWrapper getSJDetails() {
            ServiceJobWrapper sjw = new ServiceJobWrapper();
            mSJDB = new ServiceJobDBUtil(getActivity());
            mSJDB.open();
            if (mSJDB.hasRemarks(mServiceID)) // && aHasUploads && aHasRecordings
                sjw = mSJDB.getAllJSDetailsByServiceJobID(mServiceID);
            mSJDB.close();
            return sjw;
        }

        private void notifyUserOnSubmit(ServiceJobWrapper sjw) {
            uploadTask = new UploadDataWithNotificationTASK();
            uploadTask.setIdRemarks(sjw.getID(), sjw.getActionsOrRemarks())
                    .setSignatureFile(sjw.getSignaturePath(), sjw.getSignatureName())
                    .execute((Void) null);
        }
    }


    /****************************** UPLOADING NOTIFICATION ******************************/
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotifyManager;
    private int mIncrProgress;
    // Save this to the Server, including the
    // Recording, Signature, Image uploaded, New replacement Parts
    private class UploadDataWithNotificationTASK extends AsyncTask<Void, Void, String> {

        private int aServiceID;
        private String aRemarks;
        private String aPath;
        private String aName;

        public UploadDataWithNotificationTASK setIdRemarks(int id, String remarks) {
            mIncrProgress = 0;
            this.aServiceID = id;
            this.aRemarks = remarks;
            return this;
        }

        public UploadDataWithNotificationTASK setSignatureFile(String path, String name) {
            this.aPath = path;
            this.aName = name;
            return this;
        }

        @Override
        protected void onPreExecute() {
            buildNotification();
        }

        @Override
        protected String doInBackground(Void... params) {
            doUpload();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "Im on onPostExecute @ UploadDataWithNotificationTASK");
            //if (mIncrProgress == PROGRESS_MAX) // This line is not called due to asynchronous call
            //   goHome();

            Snackbar.make(getActivity().findViewById(android.R.id.content),
                    "Upload started.", Snackbar.LENGTH_SHORT)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.colorPrimary1))
                    .show();
        }

        // TEST ONLY
        private void testNotification() {
            Notification repliedNotification =
                    new Notification.Builder(getActivity())
                            .setContentTitle("Titile Notifcation.")
                            .setContentText(getString(R.string.uploading))
                            //.setContentIntent(pIntent).setAutoCancel(true)
                            .setStyle(new Notification.BigTextStyle().bigText("Blah-blah-blah"))
                            .setSmallIcon(R.mipmap.ic_upload)
                            .build();

            // Issue the new notification.
            NotificationManager notificationManager =
                    (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationId, repliedNotification);
            /*NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getActivity())
                            .setSmallIcon(R.mipmap.ic_upload)
                            .setContentTitle("Simple notification")
                            .setContentText("This is test of simple notification.");
            // Gets an instance of the NotificationManager service
            NotificationManager notificationManager =(NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

            //to post your notification to the notification bar
            notificationManager.notify(0 , mBuilder.build());*/
        }

        // 1.a SET UP ACTIVITY PENDING EVENT
        private PendingIntent setUpActivityPendingEventSuccess() {

            Intent resultIntent = new Intent(getActivity(), ServiceReport_TaskCompleted_5.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
            // Adds the back stack
            stackBuilder.addParentStack(ServiceReport_TaskCompleted_5.class);
            // Adds the Intent to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            // Gets a PendingIntent containing the entire back stack
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            return resultPendingIntent;
        }

        // 1.b SET UP ACTIVITY PENDING EVENT to RETRY THE UPLOAD
        private PendingIntent setUpActivityPendingEventFailed() {

            Intent resultIntent = new Intent(getActivity(), ServiceJobViewPagerActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra(RECORD_JOB_SERVICE_KEY, mServiceJobFromBundle);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
            // Adds the back stack
            stackBuilder.addParentStack(ServiceJobViewPagerActivity.class);
            // Adds the Intent to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            // Gets a PendingIntent containing the entire back stack
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            return resultPendingIntent;
        }

        // 2. & 3. SET UP NOTIFICATION
        private void buildNotification() {

            // 2. SET UP  NOTIFICATION TO SHOW
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            mBuilder = new NotificationCompat.Builder(getActivity());
            mBuilder.setContentTitle(getString(R.string.app_name))
                    .setPriority(2)
                    .setContentText(getString(R.string.uploading))
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_upload)
                    .setLargeIcon(largeIcon)
                    .setContentText(getString(R.string.upload_ing))
                    .setOngoing(true)
                    .setProgress(PROGRESS_MAX, 100, false)
                    .build();

            // 3. Issue the new notification.
            mNotifyManager = (NotificationManager) getActivity()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyManager.notify(notificationId, mBuilder.build());
        }

        // 4.a DO UPLOAD
        private void doUpload() {
            // CAPTURES
            uploadCaptures(this.aServiceID);
            // RECORDINGS
            uploadRecordings(this.aServiceID);
            // SIGNATURES
            uploadSignature(this.aServiceID, aPath, aName);
            // NEW REPLACEMENT PARTS
            // uploadJSONNewParts(aServiceID, aRemarks);
        }

        // 4.b Incremenet Progress Notification
        int counter = 0;
        public void incrementProgressNotification(int progress) {
            counter++;
            sleep();
            // mBuilder.setProgress(PROGRESS_MAX, progress, false);
            // Displays the progress bar for the first time.
            mNotifyManager.notify(notificationId, mBuilder.build());
            mIncrProgress += progress;

            Log.e("COUNTER" + counter, "Progress:" +progress+ " Inc " + mIncrProgress);
            if (counter == 3) {
                uploadJSONNewParts(aServiceID, aRemarks); // NEW REPLACEMENT PARTS
            } else if (counter == 4 || progress == 0) {
                finishNotificationProgress();
            }
        }

        private void sleep() {
            try {
                // Sleep for 5 seconds
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.d(TAG, "sleep failure");
            }
        }

        // 5. Loop Finished
        private void finishNotificationProgress() {
            String notification = "";
            if (mIncrProgress != PROGRESS_MAX) { // did not finished the uploads
                notification = getString(R.string.upload_error);
                mBuilder.setSmallIcon(R.mipmap.ic_upload_error)
                    .setColor(Color.RED)
                    .setContentIntent(setUpActivityPendingEventFailed());
                setEndTaskButton();
            } else {
                notification = getString(R.string.upload_successfully);
                mBuilder.setSmallIcon(R.mipmap.ic_upload_success)
                        .setColor(Color.GREEN)
                        .setContentIntent(setUpActivityPendingEventSuccess());
                goHome();
            }
            publishNotification(notification);
        }

        // 6. Called last when finishes the process
        private void publishNotification(String notification) {
            // When the loop is finished, updates the notification
            mBuilder.setContentText(notification)
                    // Removes the progress bar
                    .setProgress(0, 0, false)
                    .setOngoing(false);
            mNotifyManager.notify(notificationId, mBuilder.build());
        }

    } /** End of AysyncTask **/

    private void goHome() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getActivity(), MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });
    }

    /**
     * SAVE NEW REPLACEMENT PARTS and REMARKS
     * This Upload called when all other upload finished
     *      When incmrement == 90
     *          counter == 3
     * @param serviceJobID
     * @param remarks
     */
    private void uploadJSONNewParts(int serviceJobID, String remarks) {
        PartsDBUtil db = new PartsDBUtil(getActivity());
        db.open();
        List<ServiceJobNewPartsWrapper> list = db.getAllPartsBySJID(serviceJobID);
        db.close();

        if (list != null) { // Data saved on the Local DB
            try {
                ServiceJobJSON_POST jsonPost = new ServiceJobJSON_POST()
                        .addJSONRemarks(remarks)
                        .addJSONNewReplacementPart(list)
                        .setOnEventListener(new ServiceJobJSON_POST.OnEventListener() {
                            @Override
                            public void onEvent() {
                                Log.e(TAG, "onEvent Error");
                                uploadTask.incrementProgressNotification(PROGRESS_ERROR);
                            }

                            @Override
                            public void onJSONPostResult(String response) {
                                Log.e(TAG, "Message = " + response);
                                //uploadTask.sleep();
                                uploadTask.incrementProgressNotification(PROGRESS_NEW_PARTS);
                            }
                        });

                Log.e("json", jsonPost.getJsonUpload().toString());
                jsonPost.startPostJSON();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            uploadTask.incrementProgressNotification(PROGRESS_ERROR);
        }
    }

    /**
     * UPLOAD CAPTURES
     * MULTIPLE FILES
     * @param serviceJobID - used to query the files/capture being uploaded to (From TABLE servicejob.id)
     */
    private void uploadCaptures(int serviceJobID) {
        // Prepare and get Data Parameter from SQLiteDB
        UploadsDBUtil mUploadsDB = new UploadsDBUtil(this.mContext);
        mUploadsDB.open();
        List<ServiceJobUploadsWrapper> mUploadResults = mUploadsDB.getAllUploadsBySJID(serviceJobID);
        mUploadsDB.close();

        boolean hasUploads = testIfUploadsIsNotBlank(mUploadResults);

        if (mUploadResults != null && hasUploads) {
            Log.e(TAG, mUploadResults.toString());

            ServiceJobUploadFile_VolleyPOST post = new ServiceJobUploadFile_VolleyPOST();
            int counter = 1;
            for (ServiceJobUploadsWrapper sjuw : mUploadResults) {
                Log.e(TAG, "2" + sjuw.toString());
                File signatureFile = new File(sjuw.getFilePath() + "/" + sjuw.getUploadName());
                // Reseize file before send to server
                signatureFile = new ImageUtility(getActivity()).rescaleImageFile(signatureFile);

                if (signatureFile.canRead()) { // File exists on the Directory
                    post.setContext(this.mContext)
                            .setLink(SERVICE_JOB_UPLOAD_URL + "servicejob_upload_capture")
                            .addMultipleFile(signatureFile, sjuw.getUploadName(), "image/jpeg", counter + "")
                            .addMultipleParam("taken", sjuw.getTaken(), counter + "")
                            .addParam("count", mUploadResults.size() + "")
                            .addParam("servicejob_id", sjuw.getServiceId() + "")
                            .setOnEventListener(new ServiceJobUploadFile_VolleyPOST.OnEventListener() {
                                @Override
                                public void onError(String msg, int success) {
                                    Log.e(TAG, "Message " + msg + " Error:" + success);
                                    uploadTask.incrementProgressNotification(PROGRESS_ERROR);
                                }

                                @Override
                                public void onSuccess(String msg, int success) {
                                    Log.e(TAG, "Message " + msg + " Success:" + success);
                                    //uploadTask.sleep();
                                    uploadTask.incrementProgressNotification(PROGRESS_CAPTURES);
                                }

                            });
                }
                counter++;
            }
            post.startUpload();
        } else {
            uploadTask.incrementProgressNotification(PROGRESS_ERROR);
        }
    }

    private boolean testIfUploadsIsNotBlank(List<ServiceJobUploadsWrapper> mUploadResults) {
        if (mUploadResults == null) {
            return false;
        }
        int resultCount = mUploadResults.size();
        int counter = 0;
        for (ServiceJobUploadsWrapper sjuw : mUploadResults) {
            if ((sjuw.getUploadName() != "" && sjuw.getUploadName() != null) &&
                (sjuw.getFilePath() != "" && sjuw.getFilePath() != null)) {
                counter++;
            }
        }
        return resultCount == counter;
    }

    /**
     *  UPLOAD RECORDINGS
     *  MULTIPLE FILES
     * @param serviceJobID - used to query the files/recordings being uploaded to (From TABLE servicejob.id)
     */
    private void uploadRecordings(int serviceJobID) {
        // Prepare and get Data Parameter from SQLiteDB
        RecordingDBUtil mRecordingsDB = new RecordingDBUtil(this.mContext);
        mRecordingsDB.open();
        List<ServiceJobRecordingWrapper> mRecordResults = mRecordingsDB.getAllRecordingsBySJID(serviceJobID);
        mRecordingsDB.close();

        boolean hasRecordings = testIfRecordingsIsNotBlank(mRecordResults);

        if (mRecordResults != null && hasRecordings) { // Data has been read
            Log.e(TAG, mRecordResults.toString());
            ServiceJobUploadFile_VolleyPOST post = new ServiceJobUploadFile_VolleyPOST();
            int counter = 1;
            for (ServiceJobRecordingWrapper sjrw : mRecordResults) {
                Log.e(TAG, "2" + sjrw.toString());
                File signatureFile = new File(sjrw.getFilePath());
                post.setContext(this.mContext)
                        .setLink(SERVICE_JOB_UPLOAD_URL + "servicejob_upload_recording")
                        .addMultipleFile(signatureFile, sjrw.getRecordingName(), "audio/mpeg", counter + "")
                        .addMultipleParam("taken", sjrw.getTaken(), counter + "")
                        .addParam("count", mRecordResults.size() + "")
                        .addParam("servicejob_id", sjrw.getServiceId() + "")
                        .setOnEventListener(new ServiceJobUploadFile_VolleyPOST.OnEventListener() {
                            @Override
                            public void onError(String msg, int success) {
                                Log.e(TAG, "Message " + msg + " Error:" + success);
                                uploadTask.incrementProgressNotification(PROGRESS_ERROR);
                            }

                            @Override
                            public void onSuccess(String msg, int success) {
                                Log.e(TAG, "Message " + msg + " Success:" + success);
                                //uploadTask.sleep();
                                uploadTask.incrementProgressNotification(PROGRESS_RECORDINGS);
                            }
                        });
                counter++;
            }
            post.startUpload();
        } else {
            uploadTask.incrementProgressNotification(PROGRESS_ERROR);
        }
    }

    private boolean testIfRecordingsIsNotBlank(List<ServiceJobRecordingWrapper> mRecordResults) {
        if (mRecordResults == null) {
            return false;
        }
        int resultCount = mRecordResults.size();
        int counter = 0;
        for (ServiceJobRecordingWrapper sjuw : mRecordResults) {
            if ((sjuw.getRecordingName() != "" && sjuw.getRecordingName() != null) &&
                    (sjuw.getFilePath() != "" && sjuw.getFilePath() != null)) {
                counter++;
            }
        }
        return resultCount == counter;
    }

    /**
     * UPLOAD SIGNATURE
     *      SINGLE FILE
     *      Before Remarks
     *      After Remarks
     * @param serviceJobId
     * @param path
     * @param name
     */
    private void uploadSignature(int serviceJobId, String path, String name) {

        // Prepare and get Data Parameter from SQLiteDB
        mSJDB = new ServiceJobDBUtil(getActivity());
        mSJDB.open();
        ServiceJobWrapper sjw = mSJDB.getAllJSDetailsByServiceJobID(mServiceID);
        mSJDB.close();

        File signatureFile = new File(path + name);
        // Reseize file before send to server
        signatureFile = new ImageUtility(getActivity()).rescaleImageFile(signatureFile);

        if (signatureFile.canRead()) { // File exist
            ServiceJobUploadFile_VolleyPOST post = new ServiceJobUploadFile_VolleyPOST()
                    .setContext(this.mContext)
                    .setLink(SERVICE_JOB_UPLOAD_URL + "servicejob_upload_signature")
                    .addImageFile(signatureFile, name, "image/jpeg")
                    .addParam("servicejob_id", serviceJobId+"")
                    .addParam("remarks_before", sjw.getBeforeRemarks())
                    .addParam("remarks_after", sjw.getAfterRemarks())
                    .setOnEventListener(new ServiceJobUploadFile_VolleyPOST.OnEventListener() {
                        @Override
                        public void onError(String msg, int success) {
                            Log.e(TAG, "Message " + msg + " Error:" + success);
                            uploadTask.incrementProgressNotification(PROGRESS_ERROR);
                        }

                        @Override
                        public void onSuccess(String msg, int success) {
                            Log.e(TAG, "Message " + msg + " Success:" + success);
                            //uploadTask.sleep();
                            uploadTask.incrementProgressNotification(PROGRESS_SIGNATURE);
                        }
                    });
            post.startUpload();
        } else {
            uploadTask.incrementProgressNotification(PROGRESS_ERROR);
        }
    }

    /****************************** END UPLOADING NOTIFICATION ******************************/
}
