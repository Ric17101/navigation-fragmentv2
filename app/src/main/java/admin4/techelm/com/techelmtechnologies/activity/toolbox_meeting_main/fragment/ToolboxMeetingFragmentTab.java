package admin4.techelm.com.techelmtechnologies.activity.toolbox_meeting_main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.ProjectJobFragmentTab;
import admin4.techelm.com.techelmtechnologies.activity.toolbox_meeting_main.ToolboxMeetingListFragment;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_TYPE_KEY;

public class ToolboxMeetingFragmentTab extends Fragment {

    private final static String TAG = ToolboxMeetingFragmentTab.class.getSimpleName();
    private TabLayout tabLayout;
    private HorizontalScrollView hScrollViewTab;
    private ViewPager viewPager;
    public static final int TAB_COUNT = 2;
    private MyAdapter mPagerAdapter;

    public static ToolboxMeetingFragmentTab newInstance() {
        ToolboxMeetingFragmentTab fragment = new ToolboxMeetingFragmentTab();
        /*Bundle args = new Bundle();

        args.putInt(PROJECT_JOB_FORM_TYPE_KEY, typeOfForm);
        fragment.setArguments(args);*/

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View view = inflater.inflate(R.layout.tab_layout, null); // View x = inflater.inflate(R.layout.tab_layout, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        hScrollViewTab = (HorizontalScrollView) view.findViewById(R.id.hScrollViewTab);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        // This will prevent from swiping the ViewPager
        /*viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                viewPager.setCurrentItem(viewPager.getCurrentItem());
                return false;
            }
        });*/
        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(2); // Three tabs all
        // viewPager.setHorizontalScrollBarEnabled(false);
        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return view;
    }

    // Just to Initialize the Button Next, Prev in ProjectJobViewPagerActivty
    public ViewPager getViewPager() {
        return this.viewPager;
    }

    public Fragment getActiveFragment(ViewPager container, int position) {
        return this.mPagerAdapter.getActiveFragment(container, position);
    }

    public void setCurrentTab(int nextOrPrevious) {
        this.viewPager.setCurrentItem(getItem(nextOrPrevious), true);
    }
    public void setCurrentToFirstTab() {
        this.viewPager.setCurrentItem(0);
    }

    private int getCurrentPosition() { return viewPager.getCurrentItem(); }
    private int getItem(int i) { return getCurrentPosition() + i; }

    class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AttendanceFragment();
                case 1:
                    return new MeetingDetailsFragment();
            }
            return null;
        }

        @Override
        public int getCount() { return TAB_COUNT; }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {
            Log.e(TAG, "getPageTitle(Postion):" + position);
            switch (position) {
                case 0:
                    return "Attendance";
                case 1:
                    return "Meeting details";
            }
            return null;
        }

        private Fragment getActiveFragment(ViewPager container, int position) {
            String name = makeFragmentName(container.getId(), position);
            return getChildFragmentManager().findFragmentByTag(name);
        }

        /**
         * @param containerViewId the ViewPager this adapter is being supplied to
         * @param id              pass in getItemId(position) as this is whats used internally in this class
         * @return the tag used for this pages fragment
         */
        public String makeFragmentName(int containerViewId, long id) {
            return "android:switcher:" + containerViewId + ":" + id;
        }
    }

}
