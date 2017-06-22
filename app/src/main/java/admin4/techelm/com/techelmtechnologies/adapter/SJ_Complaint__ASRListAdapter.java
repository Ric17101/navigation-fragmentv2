package admin4.techelm.com.techelmtechnologies.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.helper.FragmentSetListHelper_SJComplaint_CF;
import admin4.techelm.com.techelmtechnologies.adapter.listener.ServiceJobComplaintsCFListener;
import admin4.techelm.com.techelmtechnologies.adapter.listener.ServiceJobComplaintsCategoryListener;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaintWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_ASRWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_CFWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_MobileWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.LIST_DELIM;

public class SJ_Complaint__ASRListAdapter extends RecyclerView.Adapter<SJ_Complaint__ASRListAdapter.ViewHolder> {

    private static final String TAG = SJ_Complaint__ASRListAdapter.class.getSimpleName();

    private ArrayList<ServiceJobComplaintWrapper> mComplaintToShowOnList = new ArrayList<>();
    private ArrayList<ServiceJobComplaintWrapper> mComplaintDataSet = new ArrayList<>();
    private ArrayList<ServiceJobComplaint_MobileWrapper> mMobileDataSet = new ArrayList<>();
    private ArrayList<ServiceJobComplaint_CFWrapper> mComplaintCFDataSet = new ArrayList<>();
    private ArrayList<ServiceJobComplaint_ASRWrapper> mASRDataSet = new ArrayList<>();
    private ArrayList<ServiceJobComplaint_CFWrapper> mActionList = new ArrayList<>();
    private ServiceJobComplaintWrapper dataSetCategoryToShow;
    //private String[] aSubItem;
    private ServiceJobComplaintsCategoryListener mCallback;
    private ServiceJobComplaintsCFListener mSubCallback;
    private int mLastAnimatedItemPosition = -1;
    private int mLasItemPosition = 0;
    private Context mContext;
    private boolean isBeforeFragment = true;

    private OnItemClickListener mItemsOnClickListener;
    private int counterOnBindViewHolder = 0;

    private FragmentSetListHelper_SJComplaint_CF mSetHelper;

    public SJ_Complaint__ASRListAdapter(Activity activity) {
        mContext = activity;

        // .. Attach the interface
        try {
            mCallback = (ServiceJobComplaintsCategoryListener) activity;
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e(TAG, "Must implement the Listener in the Activity", ex);
        }
        // System.gc();
    }

    public SJ_Complaint__ASRListAdapter(ArrayList<ServiceJobComplaint_MobileWrapper> serviceJobList) {
        this.mMobileDataSet = serviceJobList;
        notifyDataSetChanged();
    }

    public void swapData(ArrayList<ServiceJobComplaintWrapper> complaintsToShow,
                         ArrayList<ServiceJobComplaint_MobileWrapper> complaintMobileCategoryList,
                         ArrayList<ServiceJobComplaint_CFWrapper> complaintCFList,
                         ArrayList<ServiceJobComplaint_ASRWrapper> mComplaintASR_ActionByCatList,
                         boolean isBEFORE) {
        mComplaintDataSet = complaintsToShow;
        mMobileDataSet = complaintMobileCategoryList;
        mComplaintCFDataSet = complaintCFList;
        mASRDataSet = mComplaintASR_ActionByCatList;
        isBeforeFragment = isBEFORE;
        removeActionCategoryDuplicates(complaintsToShow);


        /*Log.e(TAG, "mDataSet:" + mComplaintDataSet.size());
        Log.e(TAG, "==========mDataSet==========:" + mComplaintDataSet.toString());
        Log.e(TAG, "mActionList:" + mActionList.size());
        Log.e(TAG, "==========mActionList==========:" + mActionList.toString());
        Log.e(TAG, "mMobileDataSet:" + mMobileDataSet.size());
        Log.e(TAG, "==========mMobileDataSet==========:" + mMobileDataSet.toString());
        Log.e(TAG, "mComplaintCFDataSet:" + mComplaintCFDataSet.size());
        Log.e(TAG, "==========mComplaintCFDataSet==========:" + mComplaintCFDataSet.toString());
        Log.e(TAG, "mASRDataSet:" + mASRDataSet.size());
        Log.e(TAG, "==========mASRDataSet==========:" + mASRDataSet.toString());
        Log.e(TAG, "mComplaintToShowOnList:" + mComplaintToShowOnList.size());
        Log.e(TAG, "==========mComplaintToShowOnList==========:" + mComplaintToShowOnList.toString());*/

        notifyDataSetChanged();
        Log.e(TAG, "complaintssize:" + complaintsToShow.size());
    }

    /**
        This method get only the cm_cf_id Per Category,
            all item already on the list will be ignored
     */
    private void removeActionCategoryDuplicates(ArrayList<ServiceJobComplaintWrapper> complaints) {
        mComplaintToShowOnList = new ArrayList<ServiceJobComplaintWrapper>(complaints);
        Log.e(TAG, "COUNTTTTT:" + mComplaintToShowOnList.size() + " BEFORE Duplicate Remove:" + mComplaintToShowOnList.toString());

        for (int i = 0; i < mComplaintToShowOnList.size(); i++) {
            for (int j = i+1; j < mComplaintToShowOnList.size(); j++) {
                if (//mComplaintToShowOnList.get(i).getSJ_CM_CF_ID() == mComplaintToShowOnList.get(j).getSJ_CM_CF_ID() &&
                        mComplaintToShowOnList.get(i).getCategoryID() == mComplaintToShowOnList.get(j).getCategoryID()) {
                    mComplaintToShowOnList.remove(j);
                    j--;
                }
            }
        }
        Log.e(TAG, "COUNTTTTT:" + mComplaintToShowOnList.size() + "AFTER Duplicate Remove:" + mComplaintToShowOnList.toString());
    }

    private String getComplaintFromArrayList(int complaintIDToSearch) {
        for (ServiceJobComplaint_CFWrapper cf : this.mComplaintCFDataSet) {
            if (cf.getSJComplaintFaultID() == complaintIDToSearch)
                return cf.getComplaint();
        }
        return "";
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener) {
        this.mItemsOnClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_report_complaints_action_list_item, parent, false);
        mContext = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        this.mSetHelper = new FragmentSetListHelper_SJComplaint_CF();

        dataSetCategoryToShow = mComplaintToShowOnList.get(holder.getAdapterPosition());
        dataSetCategoryToShow.setComplaint(getComplaintFromArrayList(mComplaintDataSet.get(position).getComplaintFaultID()));
        String complaintRow = "\t"+ (position + 1) + ".)  " + dataSetCategoryToShow.getCategory().toUpperCase();
                // + " - " + dataSetCategoryToShow.getComplaint();
        holder.textViewComplaints_CF.setText(complaintRow);

        ////////// Set up the List of Problems === Setting Dropdown Sublist Actions //////////
        String[] actionList = getActionFromArrayListByCategoryID(dataSetCategoryToShow.getCategoryID());
        SJ_Complaint__ASRSubListAdapter subList = new SJ_Complaint__ASRSubListAdapter(this.mContext)
                .swapData(getSJComplaint_CFArrayList(dataSetCategoryToShow), mComplaintDataSet, dataSetCategoryToShow, mASRDataSet);

        holder.action_service_report_complaint_list.setAdapter(subList);
        holder.action_service_report_complaint_list.setLayoutManager(new LinearLayoutManager(this.mContext));

        Log.d(TAG, "onBindViewHolder (" + ++counterOnBindViewHolder + ") = " +
                dataSetCategoryToShow.getAction());
        if (mLastAnimatedItemPosition < position) {
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = holder.getAdapterPosition(); // or mLastAnimatedItemPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mComplaintToShowOnList.size();
    }

    private static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void animateItem(View view) {
        view.setTranslationY(getScreenHeight());
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }

    public interface OnItemClickListener {
        void onClick(ServiceJobComplaint_CFWrapper colorWrapper);
    }

    private String[] getActionFromArrayListByCategoryID(int categoryIDToSearch) {
        StringBuilder sbActions = new StringBuilder();
        Log.e(TAG, "getActionFromArrayListByCategoryID" + mComplaintDataSet.toString());

        for (ServiceJobComplaintWrapper asr : this.mComplaintDataSet) {
            if (asr.getCategoryID() == categoryIDToSearch) {
                sbActions.append(asr.getAction());
                sbActions.append(LIST_DELIM);
            }
        }

        // Preparing SubList Data
        String[] aSubItem = sbActions.toString().split(LIST_DELIM);

        return aSubItem;
    }

    private ArrayList<ServiceJobComplaint_CFWrapper> getSJComplaint_CFArrayList(ServiceJobComplaintWrapper category) {
        Log.e(TAG, "getActionIDFromArrayList" + mComplaintDataSet.toString());
        mActionList = new ArrayList<>();

        for (ServiceJobComplaint_CFWrapper asr : this.mComplaintCFDataSet) {
            if (asr.getSJCategoryID() == category.getCategoryID()) {
                mActionList.add(asr);
            }
        }

        return mActionList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView textViewComplaints_CF;
        //final ListView lvActions;
        final LinearLayout linearLayoutLV;
        final ExpandableRelativeLayout expandableLayout;
        final RecyclerView action_service_report_complaint_list;

        public ViewHolder(View view) {
            super(view);

            // Date Information
            textViewComplaints_CF = (TextView) view.findViewById(R.id.textViewComplaints_CF);
            textViewComplaints_CF.setOnClickListener(this);

            linearLayoutLV = (LinearLayout) view.findViewById(R.id.linearLayoutLV);

            // Panel Hiding/Showing
            expandableLayout = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout);
            action_service_report_complaint_list = (RecyclerView)
                    view.findViewById(R.id.action_service_report_complaint_list);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == textViewComplaints_CF.getId()) {
                if (mCallback != null) {
                    // mSetHelper.doHideOrShow(expandableLayout);
                }
            }
        }

    }
}
