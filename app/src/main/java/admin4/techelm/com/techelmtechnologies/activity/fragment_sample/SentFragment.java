package admin4.techelm.com.techelmtechnologies.activity.fragment_sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import admin4.techelm.com.techelmtechnologies.R;

/**
 * Created by Ratan on 7/29/2015.
 */
public class SentFragment extends Fragment {

    private static final String LOG_TAG = SentFragment.class.getSimpleName();
    private static final String ARG_POSITION = "position";

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private static FragmentManager mFragmentManager;

    public SentFragment newInstance(FragmentManager fm) {
        SentFragment frag = new SentFragment();
        mFragmentManager = fm;
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        pager = (ViewPager) container.findViewById(R.id.pager);
        pager.setAdapter(buildAdapter());
        tabs = (PagerSlidingTabStrip) container.findViewById(R.id.tabs);
        tabs.setViewPager(pager);
        return inflater.inflate(R.layout.sent_layout, null);


        // View result = inflater.inflate(R.layout.sent_layout, container, false);
        /*View aResponse = inflater.inflate(R.layout.pager, container, false);
        ViewPager pager = (ViewPager) aResponse.findViewById(R.id.pager);

        pager.setAdapter(buildAdapter());

        return (aResponse);*/
    }

    private PagerAdapter buildAdapter() {
        return (new MyAdapter(getActivity(), mFragmentManager));
    }


    /**
     * These Two Lines should be included on every Fragment to maintain the state and donnot load again
     */
    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //System.out.println("SentFragment: I'm on the onSaveInstanceState");
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        //System.out.println("SentFragment: I'm on the onActivityCreated");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        //System.out.println("SentFragment: I'm on the onCreate");
    }*/

    public class MyAdapter extends FragmentPagerAdapter {
        private String[] titles = {
                getString(R.string.title_activity_add_replacement_part),
                getString(R.string.title_activity_part_replacement),
                getString(R.string.title_activity_service_report)};
        private Context mContext = null;

        public MyAdapter(Context ctx, FragmentManager fm) {
            super(fm);
            this.mContext = ctx;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return SentFragment_OLD.newInstance(position);
                }
                case 1: {
                    return UpdatesFragment.newInstance(position);
                }
                case 2: {
                    return SentFragment_OLD.newInstance(position);
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
            // return(EditorFragment.getTitle(ctxt, position));
        }
    }
}
