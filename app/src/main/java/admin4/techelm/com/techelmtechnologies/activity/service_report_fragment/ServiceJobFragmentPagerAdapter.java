package admin4.techelm.com.techelmtechnologies.activity.service_report_fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;

import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobNewReplacementPartsRatesWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;

public class ServiceJobFragmentPagerAdapter extends FragmentPagerAdapter {

    private ServiceJobWrapper mServiceJobFromBundle;
    private FragmentManager mFragmentManager;
    private List<ServiceJobNewReplacementPartsRatesWrapper> mServiceJobPartsRatesFromBundle;

    private String[] titles = {
            "1. BEFORE",
            "2. AFTER",
            "3. PART REPLACEMENT",
            "4. SIGNING OFF"
    };

    public ServiceJobFragmentPagerAdapter(FragmentManager fm, ServiceJobWrapper serviceJob, List<ServiceJobNewReplacementPartsRatesWrapper> rateList) {
        super(fm);
        this.mFragmentManager = fm;
        this.mServiceJobFromBundle = serviceJob;
        this.mServiceJobPartsRatesFromBundle = rateList;
    }

    @Override
    public Fragment getItem(int position) {
        System.out.print("ServiceJobFragmentPagerAdapter : This is from getITEM ");
        switch (position) {
            case 0 :
                return ServiceReport_FRGMT_BEFORE.newInstance(position, this.mServiceJobFromBundle);
            case 1 :
                return ServiceReport_FRGMT_AFTER.newInstance(position, this.mServiceJobFromBundle);
            case 2 :
                return PartReplacement_FRGMT_2.newInstance(position, this.mServiceJobFromBundle, this.mServiceJobPartsRatesFromBundle);
            case 3 :
                return SigningOff_FRGMT_4.newInstance(position, this.mServiceJobFromBundle);

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
