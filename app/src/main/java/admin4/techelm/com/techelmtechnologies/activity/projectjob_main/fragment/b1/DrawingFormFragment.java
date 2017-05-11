package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.io.File;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.helper.FragmentSetListHelper_ProjectJob;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.ProjectJobViewPagerActivity;
import admin4.techelm.com.techelmtechnologies.db.projectjob.PISS_TaskDBUtil;
import admin4.techelm.com.techelmtechnologies.model.projectjob.b1.PISSTaskWrapper;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;
import admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm.UploadFile_VolleyPOST;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_PISS_TASK_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_PISS_TASK_UPLOAD_DRAWING_URL;

public class DrawingFormFragment extends Fragment {

    private static final String TAG = DrawingFormFragment.class.getSimpleName();

    private View rootView;
    private ImageButton imageButtonViewDrawing;
    private Context mContext;

    private Button button_next;

    // Instance variable
    private PISSTaskWrapper mPissTask;

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
        fromBundle();
    }

    private void fromBundle() {
        this.mPissTask = getArguments().getParcelable(PROJECT_JOB_PISS_TASK_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = container.getRootView();

        View view = inflater.inflate(R.layout.content_b1_drawing_and_remarks_form, null);

        this.mContext = container.getContext();

        initButton(view);

        return view;
    }

    /**
     * These Two Lines should be included on every Fragment to maintain the state and donnot load again
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
                new UploadDrawingTASK().newInstance(mPissTask).execute();
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

        Spinner spinner = new FragmentSetListHelper_ProjectJob().setSpinnerComment(getActivity(), view);
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

    public void showDrawingCanvasFragment(PISSTaskWrapper taskWrapper) {
        System.out.println("showDrawingCanvasFragment");
        ((ProjectJobViewPagerActivity)getActivity()).showDrawingCanvasFragment(taskWrapper);
    }

    /********** UPLOAD TASK *************/
    private boolean hasDrawing = false;
    private class UploadDrawingTASK extends AsyncTask<PISSTaskWrapper, Void, String> {

        private PISSTaskWrapper task;

        public UploadDrawingTASK newInstance(PISSTaskWrapper pissTaskWrapper) {
            Log.e(TAG, "Im on the newInstance00");
            this.task = pissTaskWrapper;
            return this;
        }

        @Override
        protected String doInBackground(PISSTaskWrapper... params) {
            Log.e(TAG, "Im on the doInBackground");
            uploadDrawing(task);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e(TAG, "Im on the onPostExecute");
            super.onPostExecute(s);
            setButtonEnabled(true);
        }

        private void doUpload() { }
    }

    private void setHasDrawing(boolean hasUpload) {
        this.hasDrawing = hasUpload;
    }

    /**
     * UPLOAD DRAWING
     *      SINGLE FILE
     * @param pissTaskWrapper
     */
    private void uploadDrawing(PISSTaskWrapper pissTaskWrapper) {
        UploadFile_VolleyPOST post = new UploadFile_VolleyPOST();

        // Prepare and get Data Parameter from SQLiteDB
        PISS_TaskDBUtil taskDBUtil = new PISS_TaskDBUtil(getActivity());
        taskDBUtil.open();
        PISSTaskWrapper taskDBUtilDetailsByProjectJobID = taskDBUtil.getDetailsByProjectJobID(pissTaskWrapper.getProjectID());
        taskDBUtil.close();

        Log.e(TAG, "uploadDrawing " + pissTaskWrapper.toString());

        // Retrieve File
        File drawingFile = new File(taskDBUtilDetailsByProjectJobID.getDrawingAfter());

        if (drawingFile.canRead()) {
            setHasDrawing(false);
            post = setDataDrawingVolley(post, pissTaskWrapper);
            post.addParam("hasDrawing", "false");
            Log.e(TAG, "hasDrawing=false 1st IF");
        } else {
            setHasDrawing(true); // TODO: Fix this as this will be useful when retrieving from SQLDB

            File drawingImage = new ImageUtility(getActivity()).rescaleImageFile(drawingFile); // Reseize file before send to server

            if (drawingImage.canRead()) { // File exist

                post = setDataDrawingVolley(post, pissTaskWrapper);

                if (this.hasDrawing) { // If user signed and clicked save on the Sign PAD
                    post.addImageFile(drawingImage, drawingFile.getName(), "image/jpeg");
                    post.addParam("hasDrawing", "true");
                    Log.e(TAG, "hasDrawing=true 3rd IF");
                } else {
                    post.addParam("hasDrawing", "false");
                    Log.e(TAG, "hasDrawing=false 3rd IF");
                }
            } else {
                post.addParam("hasDrawing", "true");
                Log.e(TAG, "hasDrawing=ERROR 2nd ELSE" );
            }
        }
        post.startUpload();
    }

    private UploadFile_VolleyPOST setDataDrawingVolley(UploadFile_VolleyPOST post, PISSTaskWrapper pissTaskWrapper) {
        post.setContext(this.mContext)
            .setLink(PROJECT_JOB_PISS_TASK_UPLOAD_DRAWING_URL)
            .addParam("projectjob_task_id", pissTaskWrapper.getProjectID() + "")
            .addParam("description", pissTaskWrapper.getDescription())
            .addParam("comments", pissTaskWrapper.getComments())
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
        return post;
    }

}
