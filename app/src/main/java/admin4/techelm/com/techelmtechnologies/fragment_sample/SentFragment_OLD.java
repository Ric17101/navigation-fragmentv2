package admin4.techelm.com.techelmtechnologies.fragment_sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import admin4.techelm.com.techelmtechnologies.R;

public class SentFragment_OLD extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String LOG_TAG = SentFragment_OLD.class.getSimpleName();

    private int position;

    public static SentFragment_OLD newInstance(int position) {
        SentFragment_OLD frag = new SentFragment_OLD();
        Bundle args = new Bundle();

        args.putInt(ARG_POSITION, position);
        frag.setArguments(args);

        return (frag);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // return inflater.inflate(R.layout.sent_layout,null);

        View result = inflater.inflate(R.layout.primary_layout, container, false);
        /*EditText editor = (EditText) aResult.findViewById(R.id.editor);
        int position = getArguments().getInt(KEY_POSITION, -1);*/

        // editor.setHint(getTitle(getActivity(), position));

        return (result);
    }

    /**
     * These Two Lines should be included on every Fragment to maintain the state and donnot load again
     * @param outState
     */
    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("SentFragment: I'm on the onSaveInstanceState");
    }
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        System.out.println("SentFragment: I'm on the onActivityCreated");
    }*/

}
