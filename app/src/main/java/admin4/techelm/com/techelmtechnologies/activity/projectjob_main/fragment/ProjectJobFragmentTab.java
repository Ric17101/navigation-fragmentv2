package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment;

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
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b1.DrawingFragmentTest;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b1.PreInstallationSiteSurveyFragment;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b1.RemarksFragmentTest;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b2b3.CompletionDateFragmentTest;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b2b3.NonConformanceAndDateFragmentTest;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B1;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B2;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B3;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_TYPE_KEY;

// This is the same as FragmentPagerAdapters
public class ProjectJobFragmentTab extends Fragment {

    private final static String TAG = ProjectJobFragmentTab.class.getSimpleName();
    private TabLayout tabLayout;
    private HorizontalScrollView hScrollViewTab;
    private ViewPager viewPager;
    public static int TAB_COUUNT = 3;
    Fragment mFragment;
    MyAdapter mPagerAdapter;

    private int mTypeOfForm;

    public static ProjectJobFragmentTab newInstance(int typeOfForm) {
        ProjectJobFragmentTab fragment = new ProjectJobFragmentTab();
        Bundle args = new Bundle();

        args.putInt(PROJECT_JOB_FORM_TYPE_KEY, typeOfForm);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTypeOfForm = getArguments().getInt(PROJECT_JOB_FORM_TYPE_KEY);
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

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(2); // Three tabs all
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

    private int getCurrentPosition() { return viewPager.getCurrentItem(); }
    private int getItem(int i) { return getCurrentPosition() + i; }

    class MyAdapter extends FragmentPagerAdapter {
        FragmentManager cFragmentManager;
        public MyAdapter(FragmentManager fm) {
            super(fm);
            cFragmentManager = fm;
        }

        /**
         * Return fragment with respect to Position .
         */
        @Override
        public Fragment getItem(int position) {
            Log.e(TAG, "getItem(Postion):"+position);
            switch (mTypeOfForm) {
                case PROJECT_JOB_FORM_B1: return setFragmentB1(position);
                case PROJECT_JOB_FORM_B2: return setFragmentB2(position);
                case PROJECT_JOB_FORM_B3: return setFragmentB3(position);
                default: break;
            }
            return null;
        }

        private Fragment setFragmentB1(int position) {

            switch (position) {
                case 0:
                    return new PreInstallationSiteSurveyFragment();
                case 1:
                    return new DrawingFragmentTest(); // return UpdatesFragment.newInstance(position);
                case 2:
                    return new RemarksFragmentTest(); // return SentFragment_OLD.newInstance(position);
            }
            return null;
        }

        private Fragment setFragmentB2(int position) {
            switch (position) {
                case 0:
                    return new PreInstallationSiteSurveyFragment();
                case 1:
                    return new NonConformanceAndDateFragmentTest();
                case 2:
                    return new CompletionDateFragmentTest();
            }
            return null;
        }

        private Fragment setFragmentB3(int position) {
            switch (position) {
                case 0:
                    return new PreInstallationSiteSurveyFragment();
                case 1:
                    return new NonConformanceAndDateFragmentTest();
                case 2:
                    return new CompletionDateFragmentTest();
            }
            return null;
        }

        @Override
        public int getCount() { return TAB_COUUNT; }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {
            Log.e(TAG, "getPageTitle(Postion):"+position);
            switch (mTypeOfForm) {
                case PROJECT_JOB_FORM_B1: return setPageTitleB1(position);
                case PROJECT_JOB_FORM_B2: return setPageTitleB2B3(position);
                case PROJECT_JOB_FORM_B3: return setPageTitleB2B3(position);
                default: break;
            }
            return null;
        }

        private CharSequence setPageTitleB1(int position) {
            switch (position) {
                case 0:
                    return "Task list";
                case 1:
                    return "Drawing";
                case 2:
                    return "Remarks";
            }
            return "";
        }

        private CharSequence setPageTitleB2B3(int position) {
            switch (position) {
                case 0:
                    return "Task list";
                case 1:
                    return "In-process inspection";
                case 2:
                    return "Completion date";
            }
            return "";
        }

        public Fragment getActiveFragment(ViewPager container, int position) {
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
