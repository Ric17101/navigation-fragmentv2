package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b1;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.helper.FragmentSetListHelper_ProjectJob;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.ProjectJobViewPagerActivity;
import admin4.techelm.com.techelmtechnologies.model.projectjob.b1.PISSTaskWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_PISS_TASK_KEY;

public class DrawingFormFragment extends Fragment {

    private View rootView;
    private ImageButton imageButtonViewDrawing;

    // Instance variable
    private PISSTaskWrapper mProjectJobTask;

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
        this.mProjectJobTask = getArguments().getParcelable(PROJECT_JOB_PISS_TASK_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = container.getRootView();
        View view = inflater.inflate(R.layout.content_b1_drawing_and_remarks_form, null);

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
        Button button_next = (Button) view.findViewById(R.id.button_next);
        button_next.setText("SAVE");
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ((ProjectJobViewPagerActivity)getActivity()).fromFragmentNavigateToTaskList();
                ((ProjectJobViewPagerActivity) getActivity()).onBackPress();
            }
        });

        imageButtonViewDrawing = (ImageButton) view.findViewById(R.id.imageButtonViewDrawing);
        imageButtonViewDrawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 showDrawingCanvasFragment(mProjectJobTask);
            }
        });

        Spinner spinner = new FragmentSetListHelper_ProjectJob().setSpinnerComment(getActivity(), view);

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
}
