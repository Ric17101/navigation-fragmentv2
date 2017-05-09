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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b1.DrawingFormFragment;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b1.PISSTaskListFragment;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b2.IPITaskListFinalFragment;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b2.IPITaskListFragment;
import admin4.techelm.com.techelmtechnologies.model.projectjob.ProjectJobWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B1;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B2;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_B3;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_FORM_TYPE_KEY;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_KEY;

// This is the same as FragmentPagerAdapters
public class ProjectJobFragmentTab extends Fragment {

    private final static String TAG = ProjectJobFragmentTab.class.getSimpleName();
    private TabLayout tabLayout;
    private View mView;
    private HorizontalScrollView hScrollViewTab;
    private ViewPager viewPager;
    public static int TAB_COUNT = 2;
    Fragment mFragment;
    MyAdapter mPagerAdapter;

    // Instance Varibles
    private int mTypeOfForm;
    private ProjectJobWrapper mProjectJob;

    public static ProjectJobFragmentTab newInstance(int typeOfForm, ProjectJobWrapper projectJobWrapper) {
        ProjectJobFragmentTab fragment = new ProjectJobFragmentTab();
        Bundle args = new Bundle();

        args.putInt(PROJECT_JOB_FORM_TYPE_KEY, typeOfForm);
        args.putParcelable(PROJECT_JOB_KEY, projectJobWrapper);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fromBundle();
    }

    private void fromBundle() {
        this.mTypeOfForm = getArguments().getInt(PROJECT_JOB_FORM_TYPE_KEY);
        this.mProjectJob = getArguments().getParcelable(PROJECT_JOB_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View view = inflater.inflate(R.layout.tab_layout, null); // View x = inflater.inflate(R.layout.tab_layout, container, false);

        setHeaderVisibilityByFragmentPosition(view);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        hScrollViewTab = (HorizontalScrollView) view.findViewById(R.id.hScrollViewTab);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        // This will prevent from swiping the ViewPager
        viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                viewPager.setCurrentItem(viewPager.getCurrentItem());
                return false;
            }
        });
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

        /**
         * Init Header Based on the Form being passed
         */
        initHeader(view);

        return view;
    }

    private void initHeader(View view) {
        switch (this.mTypeOfForm) {
            case PROJECT_JOB_FORM_B1:
                inti_B1HeaderDetails(view);
                break;
            case PROJECT_JOB_FORM_B2:
            case PROJECT_JOB_FORM_B3:
                inti_B2HeaderDetails(view);
                Log.e(TAG, "initHeader");
                break;
            default: break;
        }
    }

    // Header for B1
    private void inti_B1HeaderDetails(View view) {
        TextView textViewLabelProjectRef = (TextView) view.findViewById(R.id.textViewLabelProjectRef);
        TextView textViewLabelCPCode = (TextView) view.findViewById(R.id.textViewLabelCPCode);
        TextView textViewLabelAttendedBy = (TextView) view.findViewById(R.id.textViewLabelAttendedBy);
        TextView textViewLabelDateOfSiteWalk = (TextView) view.findViewById(R.id.textViewLabelDateOfSiteWalk);

        textViewLabelProjectRef.setText(this.mProjectJob.getProjectRef());
        textViewLabelCPCode.setText(this.mProjectJob.getID()+"");
        textViewLabelAttendedBy.setText(this.mProjectJob.getFirstInspector());
        textViewLabelDateOfSiteWalk.setText(this.mProjectJob.getStartDate());
    }

    // Header for B2 and B3
    private void inti_B2HeaderDetails(View view) {
        TextView textViewLabelProjRef = (TextView) view.findViewById(R.id.textViewLabelProjRef);
        TextView textViewLabelDateOfSiteWalk2 = (TextView) view.findViewById(R.id.textViewLabelDateOfSiteWalk2); // This is not used...
        TextView textViewLabelProjectSite = (TextView) view.findViewById(R.id.textViewLabelProjectSite);
        TextView textViewLabelSubContractor = (TextView) view.findViewById(R.id.textViewLabelSubContractor);
        TextView textViewLabelInspectionDate = (TextView) view.findViewById(R.id.textViewLabelInspectionDate);
        TextView textViewLabelWorkCompletionDate = (TextView) view.findViewById(R.id.textViewLabelWorkCompletionDate);
        TextView textViewLabelSignature = (TextView) view.findViewById(R.id.textViewLabelSignature);

        textViewLabelProjRef.setText(this.mProjectJob.getProjectRef());
        textViewLabelProjectSite.setText(this.mProjectJob.getProjectSite());
        textViewLabelSubContractor.setText(this.mProjectJob.getSecondInspector());
        textViewLabelInspectionDate.setText(this.mProjectJob.getStartDate());
        textViewLabelWorkCompletionDate.setText(this.mProjectJob.getTargetCompletionDate());
        textViewLabelSignature.setText(this.mProjectJob.getStatus() + "");
    }

    // Just to show the Header Layout ONLY FOR THE ProjectJob - SECTION B
    private void setHeaderVisibilityByFragmentPosition(View view) {
        switch (this.mTypeOfForm) {
            case PROJECT_JOB_FORM_B1:
                LinearLayout projectJobLayoutHeader = (LinearLayout) view.findViewById(R.id.projectJobLayoutHeader);
                projectJobLayoutHeader.setVisibility(View.VISIBLE);
                break;
            case PROJECT_JOB_FORM_B2:
            case PROJECT_JOB_FORM_B3:
                LinearLayout projectJobLayoutB2B3Header = (LinearLayout) view.findViewById(R.id.projectJobLayoutB2B3Header);
                projectJobLayoutB2B3Header.setVisibility(View.VISIBLE);
                break;
            default: break;
        }
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
                case PROJECT_JOB_FORM_B1: return PISSTaskListFragment.newInstance(mProjectJob);
                case PROJECT_JOB_FORM_B2: return setFragmentB2(position);
                case PROJECT_JOB_FORM_B3: return setFragmentB3(position);
                default: break;
            }
            return null;
        }

        private Fragment setFragmentB2(int position) {
            switch (position) {
                case 0:
                    return IPITaskListFragment.newInstance(mProjectJob, PROJECT_JOB_FORM_B2);
                case 1:
                    return IPITaskListFinalFragment.newInstance(mProjectJob, PROJECT_JOB_FORM_B2);
            }
            return null;
        }

        private Fragment setFragmentB3(int position) {
            switch (position) {
                case 0:
                    return IPITaskListFragment.newInstance(mProjectJob, PROJECT_JOB_FORM_B3);
                case 1:
                    return IPITaskListFinalFragment.newInstance(mProjectJob, PROJECT_JOB_FORM_B3);
            }
            return null;
        }

        @Override
        public int getCount() {
            if (mTypeOfForm == PROJECT_JOB_FORM_B1) {
                return 1;
            } else {
                return TAB_COUNT;
            }
        }

        /**
         * This method returns the title of the tab according to the position.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            Log.e(TAG, "getPageTitle(Postion):" + position);

            switch (mTypeOfForm) {
                case PROJECT_JOB_FORM_B1: return "TASK LIST";
                case PROJECT_JOB_FORM_B2: return setPageTitleB2B3(position);
                case PROJECT_JOB_FORM_B3: return setPageTitleB2B3(position);
                default: break;
            }
            return null;
        }

        // NOT USED
        private CharSequence setPageTitleB1(int position) {
            switch (position) {
                case 0:
                    return "TASK LIST";
                case 1:
                    return "DRAWING FORM";
            }
            return "";
        }

        private CharSequence setPageTitleB2B3(int position) {
            switch (position) {
                case 0:
                    return "TASK LIST";
                case 1:
                    return "FINAL";
            }
            return "";
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
