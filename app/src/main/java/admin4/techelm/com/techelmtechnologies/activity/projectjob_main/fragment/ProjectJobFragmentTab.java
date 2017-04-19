package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.ProjectJobFragment;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b1.PreInstallationSiteSurveyFragment;
import admin4.techelm.com.techelmtechnologies.activity.servicejob_main.ServiceJobFragment;
import admin4.techelm.com.techelmtechnologies.activity.servicejob_main.UnsignedServicesFragment;

public class ProjectJobFragmentTab extends Fragment {

    private TabLayout tabLayout;
    private HorizontalScrollView hScrollViewTab;
    private ViewPager viewPager;
    public static int TAB_COUUNT = 3;

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
                    return new PreInstallationSiteSurveyFragment();
                case 1:
                    return new UnsignedServicesFragment();
                case 2:
                    return new ServiceJobFragment();
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

            switch (position) {
                case 0:
                    return "Task list";
                case 1:
                    return "Drawing";
                case 2:
                    return "Remarks";
            }
            return null;
        }
    }

}
