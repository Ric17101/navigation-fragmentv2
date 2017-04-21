package admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;

import admin4.techelm.com.techelmtechnologies.activity.fragment_sample.SentFragment_OLD;
import admin4.techelm.com.techelmtechnologies.activity.fragment_sample.UpdatesFragment;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.fragment.b1.PreInstallationSiteSurveyFragment;
import admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.PartReplacement_FRGMT_2;
import admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.ServiceReport_FRGMT_AFTER;
import admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.ServiceReport_FRGMT_BEFORE;
import admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.SigningOff_FRGMT_4;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobNewReplacementPartsRatesWrapper;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;

public class ProjectJobFragmentPagerAdapter_NOT_USED extends FragmentPagerAdapter {

    // private ServiceJobWrapper mServiceJobFromBundle;
    private FragmentManager mFragmentManager;

    private String[] titles = {
            "Task list",
            "Drawing",
            "Remarks",
    };

    public ProjectJobFragmentPagerAdapter_NOT_USED(FragmentManager fm, ServiceJobWrapper serviceJob, List<ServiceJobNewReplacementPartsRatesWrapper> rateList) {
        super(fm);
        this.mFragmentManager = fm;
        // this.mServiceJobFromBundle = serviceJob;
        // this.mServiceJobPartsRatesFromBundle = rateList;
    }

    public ProjectJobFragmentPagerAdapter_NOT_USED(FragmentManager fm) {
        super(fm);
        this.mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        System.out.print("ServiceJobFragmentPagerAdapter : This is from getITEM ");
        switch (position) {
            case 0:
                return new PreInstallationSiteSurveyFragment();
            case 1:
                return UpdatesFragment.newInstance(position);
            case 2:
                return SentFragment_OLD.newInstance(position);
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
    }

    public Fragment getActiveFragment(ViewPager container, int position) {
        String name = makeFragmentName(container.getId(), position);
        return mFragmentManager.findFragmentByTag(name);
    }

    /**
     * @param containerViewId the ViewPager this adapter is being supplied to
     * @param id              pass in getItemId(position) as this is whats used internally in this class
     * @return the tag used for this pages fragment
     */
    public static String makeFragmentName(int containerViewId, long id) {
        return "android:switcher:" + containerViewId + ":" + id;
    }
}
