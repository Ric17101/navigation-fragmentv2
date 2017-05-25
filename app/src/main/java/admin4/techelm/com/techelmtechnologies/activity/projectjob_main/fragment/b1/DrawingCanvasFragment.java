package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b1;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alexvasilkov.gestures.Settings;
import com.alexvasilkov.gestures.views.interfaces.GestureView;

import java.io.File;

import admin4.techelm.com.techelmtechnologies.db.projectjob.PISS_TaskDBUtil;
import admin4.techelm.com.techelmtechnologies.model.projectjob.b1.PISSTaskWrapper;
import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;
import admin4.techelm.com.techelmtechnologies.utility.drawing.CanvasView;
import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.ProjectJobViewPagerActivity;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.NEW_DOMAIN_URL;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_PISS_TASK_KEY;

/**
 * Created 16/05/2017.
 * Drawing Canvas with pencil/eraser and text
 */
public class DrawingCanvasFragment extends Fragment implements
        View.OnClickListener
    {

    private static final String TAG = DrawingCanvasFragment.class.getSimpleName();
    // Sampler
    private static final String IMAGE_URL = "http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/downloadables/drawing_test2.jpg";

    private Context mContext;
    CanvasView canvas;
    LinearLayout linearLayoutCanvas;
    Bitmap bitmap1,bitmap2;
    Resources resources;
    int BitmapSize = 30;
    int width, height;
    Button btnAddText,btnUndo,btnRedo,btnDraw,btnBlue,btnSave, btnZoom;
    EditText txtAddText;
    String text;
    ImageUtility image;

    ImageView mockImageView;
    GestureView mGestureView;

    private PISSTaskWrapper mPissTask;
    private boolean hasEdited = false;

    public static DrawingCanvasFragment newInstamce(PISSTaskWrapper pissTaskWrapper) {
        DrawingCanvasFragment fragment = new DrawingCanvasFragment();
        Bundle args = new Bundle();

        args.putParcelable(PROJECT_JOB_PISS_TASK_KEY, pissTaskWrapper);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fromBundle();
    }

    private void fromBundle() {
        this.mPissTask = getArguments().getParcelable(PROJECT_JOB_PISS_TASK_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.content_b1_drawing_layout_test, container, false);

        this.mContext = container.getContext();

        this.resources = getResources();

        new LoadTASKProjectIFExistsTask().newInstance(this.mPissTask, view).execute((Void) null);

        return view;
    }

    /**
     * These Two Lines should be included on every Fragment to maintain the state and donnot load again
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("Drawing, currently under construction");
    }
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        System.out.println("Drawing, currently under construction");
    }

    private void initCanvasView() {
        canvas = (CanvasView) getView().findViewById(R.id.canvas);
        Drawable d = new BitmapDrawable(getResources(), bitmap1);
        canvas.setBackground(d);
        canvas.setPaintStrokeWidth(4F);
        initGestureView(getView());
        initButton(getView());
    }

    private void downloadImage(View view, boolean edited) {
        /*
        UILDownloader downloader = new UILDownloader(getActivity());
        downloader.setImageFrom(IMAGE_URL);
        downloader.setImageView(mockImageView);
        downloader.start();
        */
        mGestureView = (GestureView) view.findViewById(R.id.gestureView);
        mockImageView = (ImageView) view.findViewById(R.id.mockImageView);

        String URI = NEW_DOMAIN_URL + this.mPissTask.getDrawingBefore();
        ((ProjectJobViewPagerActivity) getActivity())
                .downloadImageFromURL(
                        DrawingCanvasFragment.this,
                        URI,
                        mockImageView,
                        ProjectJobViewPagerActivity.fragmentType.CANVAS);
        this.hasEdited = edited;
    }

    /**
     * Returns the Uri which can be used to delete/work with images in the photo gallery.
     * @param filePath Path to IMAGE on SD card
     * @return Uri in the format of... content://media/external/images/media/[NUMBER]
     */
    private Uri getImageContentUri(String filePath) {
        long photoId;
        Uri photoUri = MediaStore.Images.Media.getContentUri("external");

        String[] projection = {MediaStore.Images.ImageColumns._ID};
        // TODO This will break if we have no matching item in the MediaStore.
        Cursor cursor = getActivity().getContentResolver().query(photoUri, projection, MediaStore.Images.ImageColumns.DATA + " LIKE ?", new String[] { filePath }, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(projection[0]);
        photoId = cursor.getLong(columnIndex);

        cursor.close();
        return Uri.parse(photoUri.toString() + "/" + photoId);
    }

    /***** GESTURE SETTINGS *****/
    private void initGestureView(View view) {
        mGestureView.getController().getSettings()
            .setMaxZoom(2f)
            .setDoubleTapZoom(-1f) // Falls back to max zoom level
            .setPanEnabled(true)
            .setZoomEnabled(true)
            .setDoubleTapEnabled(true)
            .setRotationEnabled(false)
            .setRestrictRotation(false)
            .setOverscrollDistance(0f, 0f)
            .setOverzoomFactor(2f)
            .setFillViewport(false)
            .setFitMethod(Settings.Fit.INSIDE)
            .setGravity(Gravity.CENTER);
    }

    private void enableGestureView() {
        canvas.setEditMode(CanvasView.Mode.DISABLE);

        if (!mGestureView.getController().getSettings().isGesturesEnabled()) {
            mGestureView.getController().getSettings().enableGestures();
        }
    }

    private void disableGestureView() {
        canvas.setEditMode(CanvasView.Mode.ENABLE);
        if (mGestureView.getController().getSettings().isGesturesEnabled()) {
            mGestureView.getController().getSettings().disableGestures();
        }
    }

    public void initCanvasView(Bitmap imageLoaded) {
        this.bitmap1 = imageLoaded;
        if (hasEdited && this.mPissTask.getDrawingAfter() != null) { // Load after Drawing
            File editedDrawing = new File(this.mPissTask.getDrawingAfter());
            this.bitmap1 = ImageUtility.loadBitmapFromile(editedDrawing);
            Log.e(TAG, "has Edited " + editedDrawing.getName());
        }
        initCanvasView();
        // TODO: save the originalImage then compare or whatever
    }

    private void initButton(View view) {
        btnAddText = (Button) view.findViewById(R.id.btnAddText);
        btnUndo = (Button) view.findViewById(R.id.btnUndo);
        btnRedo = (Button) view.findViewById(R.id.btnRedo);
        btnDraw = (Button) view.findViewById(R.id.btnDraw);
        btnZoom = (Button) view.findViewById(R.id.btnZoom);
        btnBlue = (Button) view.findViewById(R.id.btnBlue);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        txtAddText = (EditText) view.findViewById(R.id.txtAddText);

        btnAddText.setOnClickListener(this);
        btnUndo.setOnClickListener(this);
        btnRedo.setOnClickListener(this);
        btnDraw.setOnClickListener(this);
        btnZoom.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        /** BUTTON BACK */
        Button button_back = (Button) view.findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProjectJobViewPagerActivity) getActivity()).onBackPress();
            }
        });

        /** BUTTON SAVE DRAWING */
        Button button_next = (Button) view.findViewById(R.id.button_next);
        button_next.setText("SAVE DRAWING");
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*saveImage();
                ((ProjectJobViewPagerActivity) getActivity()).fromFragmentNavigate(1);*/
                setButtonEnabled(false);
                saveImage();
            }
        });
    }

    public CanvasView getCanvas() {
        return this.canvas;
    }

    public void CreateBitmap(){
        bitmap1 = BitmapFactory.decodeResource(
                resources,
                R.drawable.background
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void GetBitmapWidthHeight() {
        bitmap1.setHeight(linearLayoutCanvas.getHeight());
        bitmap1.setWidth(linearLayoutCanvas.getWidth());
        /*
        width = bitmap1.getWidth() + BitmapSize * 2;
        height = bitmap1.getHeight() + BitmapSize * 2;
        */
    }

    @Override
    public void onClick(View v) {
        // disableGestureView();
        switch(v.getId()) {
            case R.id.btnAddText:
                text = txtAddText.getText().toString();
                Log.i("MyActivity", text);

                // Change Mode
                canvas.setMode(CanvasView.Mode.TEXT);

                // Setter
                canvas.setText(text);
                canvas.setFontSize(50F);
                break;
            case R.id.btnUndo:
                canvas.undo();   // Undo
                break;
            case R.id.btnRedo:
                canvas.redo();   // Redo
                break;
            case R.id.btnSave:
                setButtonEnabled(false);
                saveImage();

                Log.i(TAG, "try erase");
                canvas.setMode(CanvasView.Mode.ERASER);  // for using Eraser
                break;
            case R.id.btnDraw:
                canvas.setMode(CanvasView.Mode.DRAW);    // for drawing
                canvas.setPaintStrokeColor(Color.BLACK);
                break;
            case R.id.btnBlue:
                canvas.setPaintStrokeColor(Color.BLUE);
                break;
            case R.id.btnZoom :
                // enableGestureView();
                break;
        }
    }

    private void setButtonEnabled(boolean mode) {
        btnSave.setEnabled(mode);
    }

    private void saveImage() {
        bitmap2 = canvas.getBitmap();

        image = new ImageUtility(getActivity());
        image.setExternal(false);
        image.setDirectoryName("DRAWING");
        image.setFileName(String.format("DRAWING_PISS_TASK_%d.jpg", System.currentTimeMillis()));

        // Notify message to user
        String message = "";
        if (image.save(bitmap2)) { // save image to storage
            message = "Image saved.";
            // TODO: save to DB from here
            mPissTask.setDrawingAfter(image.loadImageFile().getAbsolutePath());
            Log.e(TAG, "setDrawing After " + image.loadImageFile().getAbsolutePath());
            new SaveTASKProjectTask().newInstance(mPissTask).execute((Void) null);
        } else {
            message = "Can't save image.";
        }

        // Finally, prompt user
        SnackBarNotificationUtil
                .setSnackBar(getActivity().findViewById(android.R.id.content), message)
                .setColor(getResources().getColor(R.color.colorPrimary1))
                .show();
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
            /*if (!taskDBUtil.hasInsertedDrawings(task.getID())) { // Check if already inserted then will not add anymore
            }*/
            insertedID = taskDBUtil.addPISSTask(task);
            taskDBUtil.close();
            return insertedID + "";
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e(TAG, "Im on the onPostExecute");
            Log.e(TAG, "Inserted id " + s);
            super.onPostExecute(s);
            setButtonEnabled(true);
            ((ProjectJobViewPagerActivity) getActivity()).onBackPress();
            // setButtonEnabled(true);
        }
    }

    /** THIS Class loads the Task Project Job whether it is existed already on the DB, else will load from the web(The image) **/
    private class LoadTASKProjectIFExistsTask extends AsyncTask<Void, Void, String> {
        private PISSTaskWrapper task;
        private View cView;
        private boolean hasEdited = false;

        public LoadTASKProjectIFExistsTask newInstance(PISSTaskWrapper pissTaskWrapper, View view) {
            Log.e(TAG, "Im on the newInstance00");
            this.task = pissTaskWrapper;
            this.cView = view;
            return this;
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.e(TAG, "Im on the doInBackground");
            PISS_TaskDBUtil taskDBUtil = new PISS_TaskDBUtil(getActivity());
            taskDBUtil.open();
            if (taskDBUtil.hasInsertedDrawings(task.getID())) { // Check if already inserted then load task from DB
                task = taskDBUtil.getDetailsByPISSTaskID(task.getID());
                mPissTask = task; // CHANGE THE PissTask from DB
                hasEdited = true;
            }
            taskDBUtil.close();
            return hasEdited + "";
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e(TAG, "Im on the onPostExecute " + s);
            Log.e(TAG, "Im on the onPostExecute " + task.toString());
            super.onPostExecute(s);
            downloadImage(cView, hasEdited); // CreateBitmap();
        }
    }


}
