package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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

import admin4.techelm.com.techelmtechnologies.utility.SnackBarNotificationUtil;
import admin4.techelm.com.techelmtechnologies.utility.drawing.CanvasView;
import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.ProjectJobViewPagerActivity;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;

/**
 * Created by Ratan on 7/29/2015.
 */
public class DrawingCanvasFragment extends Fragment implements
        View.OnClickListener
    {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.content_b1_drawing_layout_test, container, false);

        this.mContext = container.getContext();

        this.resources = getResources();

        downloadImage(view); // CreateBitmap();

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

    private void downloadImage(View view) {
        /*UILDownloader downloader = new UILDownloader(getActivity());
        downloader.setImageFrom(IMAGE_URL);
        downloader.setImageView(mockImageView);
        downloader.start();*/
        mGestureView = (GestureView) view.findViewById(R.id.gestureView);
        mockImageView = (ImageView) view.findViewById(R.id.mockImageView);
        ((ProjectJobViewPagerActivity) getActivity()).downloadImageFromURL(DrawingCanvasFragment.this, IMAGE_URL, mockImageView);
    }

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
        initCanvasView();
        // TODO: save the originalImage then compare or whatever
    }

    private void initButton(View view) {
        btnAddText = (Button)view.findViewById(R.id.btnAddText);
        btnUndo = (Button)view.findViewById(R.id.btnUndo);
        btnRedo = (Button)view.findViewById(R.id.btnRedo);
        btnDraw = (Button)view.findViewById(R.id.btnDraw);
        btnZoom = (Button)view.findViewById(R.id.btnZoom);
        btnBlue = (Button)view.findViewById(R.id.btnBlue);
        btnSave = (Button)view.findViewById(R.id.btnSave);
        txtAddText = (EditText)view.findViewById(R.id.txtAddText);

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
                saveImage();
                ((ProjectJobViewPagerActivity) getActivity()).fromFragmentNavigate(1);
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
        /*width = bitmap1.getWidth() + BitmapSize * 2;
        height = bitmap1.getHeight() + BitmapSize * 2;*/
    }

    @Override
    public void onClick(View v) {
        disableGestureView();
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
                saveImage();

                Log.i("MyActivity", "try erase");
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
                enableGestureView();
                break;
        }
    }

    private void saveImage() {
        bitmap2 = canvas.getBitmap();

        // image = new ImageUtility(mContext);
        image = new ImageUtility(getActivity());
        image.setExternal(true);
        image.setDirectoryName("DRAWING");

        String message = "";
        if (image.save(bitmap2)) {
            message = "Image saved.";
        } else {
            message = "Can't save image.";
        }

        SnackBarNotificationUtil
                .setSnackBar(getActivity().findViewById(android.R.id.content),
                        message)
                .setColor(getResources().getColor(R.color.colorPrimary1))
                .show();
    }
}
