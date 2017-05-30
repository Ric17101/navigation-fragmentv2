package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.io.File;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.helper.FragmentSetListHelper_ProjectJob;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.ProjectJobViewPagerActivity;
import admin4.techelm.com.techelmtechnologies.db.projectjob.PISS_TaskDBUtil;
import admin4.techelm.com.techelmtechnologies.model.projectjob.b1.PISSTaskWrapper;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;
import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;
import admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm.UploadFile_VolleyPOST;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.FIRSTCM_DOMAIN_URL;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.NEW_DOMAIN_URL;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_PISS_TASK_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_PISS_TASK_UPLOAD_DRAWING_URL;

public class DrawingFormFragment extends Fragment {

    private static final String TAG = DrawingFormFragment.class.getSimpleName();

    private ImageButton imageButtonViewDrawing;
    private Context mContext;

    private Button button_next;

    // Instance variable
    private final static String SPINNER_TAG = "mSpinnerComment";
    private PISSTaskWrapper mPissTask;
    private Spinner mSpinnerComment;
    private String mCurrentSpinner = "";
    private EditText editTextB1Remarks;

    public static DrawingFormFragment newInstance(PISSTaskWrapper pissTaskWrapper) {
        DrawingFormFragment fragment = new DrawingFormFragment();
        Bundle args = new Bundle();

        args.putParcelable(PROJECT_JOB_PISS_TASK_KEY, pissTaskWrapper);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        Log.e(TAG, "Im on the onCreate");
        if (saveInstanceState != null) {
            Log.e(TAG, "Im on the onCreate spinner="+saveInstanceState.getInt(SPINNER_TAG, 0));
            //mSpinnerComment.setSelection(saveInstanceState.getInt(SPINNER_TAG, 0));
        }

        fromBundle();
    }

    private void fromBundle() {
        this.mPissTask = getArguments().getParcelable(PROJECT_JOB_PISS_TASK_KEY);
        Log.e(TAG, "Im on the fromBundle");
        new SaveTASKProjectTask().newInstance(this.mPissTask).execute((Void)null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.content_b1_drawing_and_remarks_form, null);

        this.mContext = container.getContext();

        initButton(view);

        downloadImage(view);

        return view;
    }

    /**
     * Populate the Spinner and Remarks from wrapper
     * @param task - PISS task details
     */
    private void initTaskInputs(PISSTaskWrapper task) {
        Log.e(TAG, "initTaskInputs " + task.toString());
        this.mSpinnerComment = new FragmentSetListHelper_ProjectJob().setSpinnerComment(
                getActivity(), getView(), task.getConformance());

        this.editTextB1Remarks = (EditText) getView().findViewById(R.id.editTextB1Remarks);
        this.editTextB1Remarks.setText(task.getComments());
    }

    /**
     * These Two Lines should be included on every Fragment to maintain the state and donnot load again
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // outState.putInt(SPINNER_TAG, mSpinnerComment.getSelectedItemPosition());
        Log.wtf(TAG, "onSaveInstanceState");
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Log.wtf(TAG, "onActivityCreated: currently under construction");
    }

    @Override
    public void onViewStateRestored(@NonNull Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.wtf(TAG, "onViewStateRestored");
//        this.mSpinnerComment = new FragmentSetListHelper_ProjectJob().setSpinnerComment(
//                getActivity(), getView(), this.mPissTask.getConformance());

        if (!this.mCurrentSpinner.equals(""))
            this.mSpinnerComment = new FragmentSetListHelper_ProjectJob().setSpinnerComment(
                getActivity(), getView(), this.mCurrentSpinner);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.wtf(TAG, "onStop");
        this.mCurrentSpinner = this.mSpinnerComment.getSelectedItem().toString();
    }

    private void initButton(final View view) {
        /** BUTTON BACK */
        Button button_back = (Button) view.findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ((ProjectJobViewPagerActivity) getActivity()).fromFragmentNavigate(-1);
                ((ProjectJobViewPagerActivity) getActivity()).onBackPress();
            }
        });

        /** BUTTON NEXT */
        button_next = (Button) view.findViewById(R.id.button_next);
        button_next.setText("SAVE");
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ((ProjectJobViewPagerActivity)getActivity()).fromFragmentNavigateToTaskList();
                // ((ProjectJobViewPagerActivity) getActivity()).onBackPress();
                // new UpdateTASKProjectTask().newInstance(mPissTask).execute((Void)null);

                new UploadDrawingTASK().newInstance(mPissTask).execute("");
                setButtonEnabled(false);
            }
        });

        imageButtonViewDrawing = (ImageButton) view.findViewById(R.id.imageButtonViewDrawing);
        imageButtonViewDrawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 showDrawingCanvasFragment(mPissTask);
            }
        });
    }

    private void setButtonEnabled(boolean mode) {
        button_next.setEnabled(mode);
    }

    private void setDrawableImageSignature(Bitmap drawableImageSignature) {
        BitmapDrawable ob = new BitmapDrawable(getResources(), drawableImageSignature);
        imageButtonViewDrawing.setBackground(ob);
        // imageButtonViewSignature.setBackgroundDrawable(ob);

        // Set to wrap content signature
        ViewGroup.LayoutParams params = imageButtonViewDrawing.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        imageButtonViewDrawing.setLayoutParams(params);
    }

    private void downloadImage(View view) {
        //((ProjectJobViewPagerActivity) getActivity()).downloadImageFromURL(DrawingCanvasFragment.this, IMAGE_URL, mockImageView);

        String URI = FIRSTCM_DOMAIN_URL + this.mPissTask.getDrawingBefore();
        ((ProjectJobViewPagerActivity) getActivity())
            .downloadImageFromURL(
                DrawingFormFragment.this,
                URI,
                imageButtonViewDrawing,
                ProjectJobViewPagerActivity.fragmentType.FORM);
    }

    public void showDrawingCanvasFragment(PISSTaskWrapper taskWrapper) {
        System.out.println("showDrawingCanvasFragment");
        ((ProjectJobViewPagerActivity) getActivity()).showDrawingCanvasFragment(taskWrapper);
    }

    /********** UPDATE PROJECT TASK *************/
    private class UpdateTASKProjectTask extends AsyncTask<Void, Void, String> {
        private PISSTaskWrapper task;
        private String conformance;
        private String comments;

        public UpdateTASKProjectTask newInstance(PISSTaskWrapper pissTaskWrapper) {
            Log.e(TAG, "Im on the newInstance00");
            this.task = pissTaskWrapper;
            return this;
        }

        @Override
        protected void onPreExecute() {
            this.conformance = mSpinnerComment.getSelectedItem().toString();
            this.comments = editTextB1Remarks.getText().toString();
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.e(TAG, "Im on the doInBackground");
            PISS_TaskDBUtil taskDBUtil = new PISS_TaskDBUtil(getActivity());
            taskDBUtil.open();

            int insertedID = taskDBUtil.addPISSTask(task);
            mPissTask = taskDBUtil.getDetailsByPISSTaskID(task.getID());
//            mPissTask.setComments(this.comments);
//            mPissTask.setConformance(this.conformance);
            task = mPissTask;
            Log.e(TAG, "Im on the doInBackground " + taskDBUtil.getAllTask().toString());

            taskDBUtil.close();
            return insertedID + "";
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e(TAG, "Im on the onPostExecute");
            super.onPostExecute(s);
            // initTaskInputs(task);
        }
    }

    /********** SAVE PROJECT TASK *************/
    private class SaveTASKProjectTask extends AsyncTask<Void, Void, String> {
        private PISSTaskWrapper task;

        public SaveTASKProjectTask newInstance(PISSTaskWrapper pissTaskWrapper) {
            Log.e(TAG, "Im on the newInstance00");
            this.task = pissTaskWrapper;
            return this;
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.e(TAG, "Im on the doInBackground");
            PISS_TaskDBUtil taskDBUtil = new PISS_TaskDBUtil(getActivity());
            taskDBUtil.open();

            int insertedID = 0;
            if (!taskDBUtil.hasInsertedDrawings(task.getID())) { // Check if already inserted then will not add anymore
                insertedID = taskDBUtil.addPISSTask(task);
            }
            mPissTask = taskDBUtil.getDetailsByPISSTaskID(task.getID());
            task = mPissTask;
            Log.e(TAG, "Im on the doInBackground" + taskDBUtil.getAllTask().toString());

            taskDBUtil.close();
            return insertedID + "";
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e(TAG, "Im on the onPostExecute");
            super.onPostExecute(s);
            setButtonEnabled(true);
            initTaskInputs(task);
        }
    }


    /********** UPLOAD TASK *************/
    private boolean hasDrawing = true;
    private class UploadDrawingTASK extends AsyncTask<String, Void, String> {

        private PISSTaskWrapper task;

        public UploadDrawingTASK newInstance(PISSTaskWrapper pissTaskWrapper) {
            Log.e(TAG, "Im on the newInstance00");
            this.task = pissTaskWrapper;
            return this;
        }

        @Override
        protected void onPreExecute() {
            this.task.setConformance(mSpinnerComment.getSelectedItem().toString());
            this.task.setComments(editTextB1Remarks.getText().toString());
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e(TAG, "Im on the doInBackground");

            updateSQLDB();

            uploadDrawing(task);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e(TAG, "Im on the onPostExecute");
            super.onPostExecute(s);
            setButtonEnabled(true);
        }

        private void updateSQLDB() {
            PISS_TaskDBUtil taskDBUtil = new PISS_TaskDBUtil(getActivity());
            taskDBUtil.open();
            mPissTask = taskDBUtil.getDetailsByPISSTaskID(this.task.getID());
            int insertedID = taskDBUtil.addPISSTask(mPissTask);
            Log.e(TAG, "Im on the doInBackground " + taskDBUtil.getAllTask().toString());
            taskDBUtil.close();
        }
    }

    private void setHasDrawing(boolean hasUpload) {
        this.hasDrawing = hasUpload;
    }

    /**
     * UPLOAD DRAWING
     *      SINGLE FILE
     * @param pissTaskWrapper -
     */
    private void uploadDrawing(PISSTaskWrapper pissTaskWrapper) {
        boolean canUploadFile = false;
        UploadFile_VolleyPOST post = new UploadFile_VolleyPOST();

        // Prepare and get Data Parameter from SQLiteDB
        PISS_TaskDBUtil taskDBUtil = new PISS_TaskDBUtil(getActivity());
        taskDBUtil.open();
        PISSTaskWrapper task = taskDBUtil.getDetailsByPISSTaskID(pissTaskWrapper.getID());
        taskDBUtil.close();

        Log.e(TAG, "task:uploadDrawing " + task.toString());
        Log.e(TAG, "task:Drawing File " + task.getDrawingAfter());

        // Retrieve File
        File drawingFile = new File("");
        File drawingImage = new File("");

        if (task.getDrawingAfter() != null /*|| task.getDrawingAfter().equals("")*/)
            drawingFile = new File(task.getDrawingAfter());

        if (!drawingFile.canRead()) {
            canUploadFile = false;
            Log.e(TAG, "hasDrawing=false 1st IF");
        } else {
            //setHasDrawing(true); // TODO: Fix this as this will be useful when retrieving from SQLDB
            drawingImage = new ImageUtility(getActivity()).rescaleImageFile(drawingFile); // Reseize file before send to server
            if (drawingImage.canRead()) { // File exist
                if (this.hasDrawing) { // If user signed and clicked save on the Sign PAD
                    canUploadFile = true;
                    Log.e(TAG, "hasDrawing=true 3rd IF");
                } else {
                    canUploadFile = false;
                    Log.e(TAG, "hasDrawing=false 3rd IF");
                }
            } else {
                canUploadFile = false;
                Log.e(TAG, "hasDrawing=ERROR 2nd ELSE");
            }
        }

        // Finally Decide whether to save/upload file
        if (canUploadFile) {
            post.addImageFile(drawingImage, drawingFile.getName(), "image/jpeg");
            post.addParam("hasDrawing", "true");
        } else {
            post.addParam("hasDrawing", "false");
        }

        post = setDataDrawingVolley(post, pissTaskWrapper);
        post.startUpload();
    }

    private UploadFile_VolleyPOST setDataDrawingVolley(UploadFile_VolleyPOST post, PISSTaskWrapper pissTaskWrapper) {
        post.setContext(this.mContext)
            .setLink(PROJECT_JOB_PISS_TASK_UPLOAD_DRAWING_URL)
            .addParam("projectjob_task_id", pissTaskWrapper.getID() + "")
            .addParam("description", pissTaskWrapper.getDescription())
            .addParam("comments", pissTaskWrapper.getComments())
            .setOnEventListener(new UploadFile_VolleyPOST.OnEventListener() {
                @Override
                public void onError(String msg, int success) {
                    Log.e(TAG, "Message " + msg + " Error:" + success);SnackBarNotificationUtil
                            .setSnackBar(getActivity().findViewById(android.R.id.content),
                                    "An Error occurred, try again later.")
                            .setColor(getResources().getColor(R.color.colorPrimary1))
                            .show();
                }
                @Override
                public void onSuccess(String msg, int success) {
                    Log.e(TAG, "Message " + msg + " Success:" + success);
                    // TODO: Should prompt user when file has been upload via Volley or via Asyncrhonous Class?
                    //uploadTask.sleep();
                    // Finally, prompt user
                    SnackBarNotificationUtil
                            .setSnackBar(getActivity().findViewById(android.R.id.content),
                                    "Uploaded to server.")
                            .setColor(getResources().getColor(R.color.colorPrimary1))
                            .show();

                    // Return to Home on success
                    ((ProjectJobViewPagerActivity) getActivity()).onBackPress();
                }
            });
        return post;
    }

}
