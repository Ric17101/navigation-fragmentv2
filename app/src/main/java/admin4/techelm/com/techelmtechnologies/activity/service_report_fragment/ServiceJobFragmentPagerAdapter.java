package admin4.techelm.com.techelmtechnologies.activity.service_report_fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;

import admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.fragment.PartReplacement_FRGMT_2;
import admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.fragment.ServiceReport_FRGMT_AFTER;
import admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.fragment.ServiceReport_FRGMT_BEFORE;
import admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.fragment.SigningOff_FRGMT_4;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_ASRWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_CFWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_MobileWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobNewReplacementPartsRatesWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;

public class ServiceJobFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = ServiceJobFragmentPagerAdapter.class.getSimpleName();
    private ServiceJobWrapper mServiceJobFromBundle;
    private FragmentManager mFragmentManager;
    private ArrayList<ServiceJobNewReplacementPartsRatesWrapper> mSJPartsRatesFromBundle;

    private ArrayList<ServiceJobComplaint_MobileWrapper> mSJComplaintMobileList;
    private ArrayList<ServiceJobComplaint_CFWrapper> mSJComplaintCFList;
    private ArrayList<ServiceJobComplaint_ASRWrapper> mSJComplaintASRList;

    private String[] titles = {
            "1. BEFORE",
            "2. AFTER",
            "3. PART REPLACEMENT",
            "4. SIGNING OFF"
    };

    public ServiceJobFragmentPagerAdapter(FragmentManager fm, ServiceJobWrapper serviceJob,
                                          ArrayList<ServiceJobNewReplacementPartsRatesWrapper> rateList,
                                          ArrayList<ServiceJobComplaint_MobileWrapper> mComplaintMobileList,
                                          ArrayList<ServiceJobComplaint_CFWrapper> mComplaintCFList,
                                          ArrayList<ServiceJobComplaint_ASRWrapper> mComplaintASRList) {
        super(fm);
        this.mFragmentManager = fm;
        this.mServiceJobFromBundle = serviceJob;
        this.mSJPartsRatesFromBundle = rateList;

        this.mSJComplaintMobileList = mComplaintMobileList;
        this.mSJComplaintCFList = mComplaintCFList;
        this.mSJComplaintASRList = mComplaintASRList;
    }

    @Override
    public Fragment getItem(int position) {
        Log.e(TAG, "This is from getITEM:"+ position);
        switch (position) {
            case 0 :
                return ServiceReport_FRGMT_BEFORE.newInstance(position, this.mServiceJobFromBundle,
                        this.mSJComplaintCFList, this.mSJComplaintMobileList, this.mSJComplaintASRList);
            case 1 :
                return ServiceReport_FRGMT_AFTER.newInstance(position, this.mServiceJobFromBundle,
                        this.mSJComplaintCFList, this.mSJComplaintMobileList, this.mSJComplaintASRList);
            case 2 :
                return PartReplacement_FRGMT_2.newInstance(position, this.mServiceJobFromBundle, this.mSJPartsRatesFromBundle);
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
     * @return the tag used for this pages fragmentType
     */
    public static String makeFragmentName(int containerViewId, long id) {
        return "android:switcher:" + containerViewId + ":" + id;
    }
}
