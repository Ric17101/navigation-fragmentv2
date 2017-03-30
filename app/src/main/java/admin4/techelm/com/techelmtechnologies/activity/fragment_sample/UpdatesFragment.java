package admin4.techelm.com.techelmtechnologies.activity.fragment_sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import admin4.techelm.com.techelmtechnologies.R;

/**
 * Created by Ratan on 7/29/2015.
 */
public class UpdatesFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String LOG_TAG = SentFragment_OLD.class.getSimpleName();

    private int position;

    public static UpdatesFragment newInstance(int position) {
        UpdatesFragment frag = new UpdatesFragment();
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
        //return inflater.inflate(R.layout.updates_layout,null);

        View result = inflater.inflate(R.layout.updates_layout, container, false);

        return (result);
    }
    /**
     * These Two Lines should be included on every Fragment to maintain the state and donnot load again
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("UpdatesFragment: I'm on the onSaveInstanceState");
    }
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        System.out.println("UpdatesFragment: I'm on the onActivityCreated");
    }
}
