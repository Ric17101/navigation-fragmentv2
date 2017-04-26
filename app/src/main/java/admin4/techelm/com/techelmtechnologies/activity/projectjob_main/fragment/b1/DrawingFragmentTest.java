package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b1;

import android.annotation.TargetApi;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import admin4.techelm.com.techelmtechnologies.utility.drawing.CanvasView;
import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.ProjectJobViewPagerActivity;
import admin4.techelm.com.techelmtechnologies.utility.ImageUtility;

/**
 * Created by Ratan on 7/29/2015.
 */
public class DrawingFragmentTest extends Fragment implements View.OnClickListener {

    private Context mContext;
    CanvasView canvas;
    LinearLayout linearLayoutCanvas;
    Bitmap bitmap1,bitmap2;
    Resources resources;
    int BitmapSize = 30;
    int width, height;
    Button btnAddText,btnUndo,btnRedo,btnDraw,btnBlue,btnSave;
    EditText txtAddText;
    String text;
    ImageUtility image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.content_drawing_layout_test, container, false);

        this.mContext = container.getContext();

        resources = getResources();

        CreateBitmap();
        canvas = (CanvasView) view.findViewById(R.id.canvas);
        Drawable d = new BitmapDrawable(getResources(), bitmap1);
        canvas.setBackground(d);
        canvas.setPaintStrokeWidth(4F);

        btnAddText = (Button)view.findViewById(R.id.btnAddText);
        btnUndo = (Button)view.findViewById(R.id.btnUndo);
        btnRedo = (Button)view.findViewById(R.id.btnRedo);
        btnDraw = (Button)view.findViewById(R.id.btnDraw);
        btnBlue = (Button)view.findViewById(R.id.btnBlue);
        btnSave = (Button)view.findViewById(R.id.btnSave);
        txtAddText = (EditText)view.findViewById(R.id.txtAddText);

        btnAddText.setOnClickListener(this);
        btnUndo.setOnClickListener(this);
        btnRedo.setOnClickListener(this);
        btnDraw.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        btnSave.setOnClickListener(this);


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
        System.out.println("Drawing, currently under construction");
    }
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        System.out.println("Drawing, currently under construction");
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
                ((ProjectJobViewPagerActivity)getActivity()).fromFragmentNavigate(-1);

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
                ((ProjectJobViewPagerActivity)getActivity()).fromFragmentNavigate(1);
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
    public void GetBitmapWidthHeight(){
        bitmap1.setHeight(linearLayoutCanvas.getHeight());
        bitmap1.setWidth(linearLayoutCanvas.getWidth());
        /*width = bitmap1.getWidth() + BitmapSize * 2;
        height = bitmap1.getHeight() + BitmapSize * 2;*/
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
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
                bitmap2 = canvas.getBitmap();

                image = new ImageUtility(mContext);
                image.setExternal(true);
                image.setDirectoryName("DRAWING");
                image.save(bitmap2);

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
        }
    }
}
