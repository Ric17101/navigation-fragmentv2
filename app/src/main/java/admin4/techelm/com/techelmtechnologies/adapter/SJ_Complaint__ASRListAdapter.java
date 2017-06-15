package admin4.techelm.com.techelmtechnologies.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.helper.FragmentSetListHelper_SJComplaint_CF;
import admin4.techelm.com.techelmtechnologies.adapter.listener.ServiceJobComplaintsCFListener;
import admin4.techelm.com.techelmtechnologies.adapter.listener.ServiceJobComplaintsCategoryListener;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaintWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_ASRWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_CFWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_MobileWrapper;
import admin4.techelm.com.techelmtechnologies.utility.view.ExpandableHeightListView;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_DELETE_DETAILS;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.LIST_DELIM;

public class SJ_Complaint__ASRListAdapter extends RecyclerView.Adapter<SJ_Complaint__ASRListAdapter.ViewHolder> {

    private static final String TAG = SJ_Complaint__ASRListAdapter.class.getSimpleName();

    private ArrayList<ServiceJobComplaintWrapper> mComplaintToShowOnList = new ArrayList<>();
    private ArrayList<ServiceJobComplaintWrapper> mComplaintDataSet = new ArrayList<>();
    private ArrayList<ServiceJobComplaint_MobileWrapper> mMobileDataSet = new ArrayList<>();
    private ArrayList<ServiceJobComplaint_CFWrapper> mComplaintCFDataSet = new ArrayList<>();
    private ArrayList<ServiceJobComplaint_ASRWrapper> mASRDataSet = new ArrayList<>();
    private ServiceJobComplaintWrapper dataSet;
    private ServiceJobComplaint_MobileWrapper sdataMobileSet;
    private ServiceJobComplaint_CFWrapper dataSubSet;
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

    public void swapData(ArrayList<ServiceJobComplaintWrapper> complaints,
                         ArrayList<ServiceJobComplaint_MobileWrapper> mNewDataSet,
                         ArrayList<ServiceJobComplaint_CFWrapper> mComplaintMobileList,
                         ArrayList<ServiceJobComplaint_ASRWrapper> mComplaintASRList,
                         boolean isBEFORE) {
        mComplaintDataSet = complaints;
        mMobileDataSet = mNewDataSet;
        mComplaintCFDataSet = mComplaintMobileList;
        mASRDataSet = mComplaintASRList;
        isBeforeFragment = isBEFORE;
        removeActionCategoryDuplicates(complaints);
        notifyDataSetChanged();
        Log.e(TAG, "complaintssize:" + complaints.size());
    }

    /**
        This method get only the cm_cf_id Per Category,
            all item already on the list will be ignored
     */
    private void removeActionCategoryDuplicates(ArrayList<ServiceJobComplaintWrapper> complaints) {
        mComplaintToShowOnList = new ArrayList<ServiceJobComplaintWrapper>(complaints);
        System.out.println("COUNTTTTT:" + mComplaintToShowOnList.size() + " BEFORE Duplicate Remove:" + mComplaintToShowOnList.toString());

        for (int i = 0; i < mComplaintToShowOnList.size(); i++) {
            for (int j = i+1; j < mComplaintToShowOnList.size(); j++) {
                if (mComplaintToShowOnList.get(i).getSJ_CM_CF_ID() == mComplaintToShowOnList.get(j).getSJ_CM_CF_ID() &&
                        mComplaintToShowOnList.get(i).getCategoryID() == mComplaintToShowOnList.get(j).getCategoryID()) {
                    mComplaintToShowOnList.remove(j);
                    j--;
                }
            }
        }
        System.out.println("COUNTTTTT:" + mComplaintToShowOnList.size() + "AFTER Duplicate Remove:" + mComplaintToShowOnList.toString());
    }

    private String getComplaintFromArrayList(int complaintIDToSearch) {
        for (ServiceJobComplaint_CFWrapper cf : this.mComplaintCFDataSet) {
            if (cf.getSJComplaintFaultIDID() == (complaintIDToSearch))
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

        dataSet = mComplaintToShowOnList.get(holder.getAdapterPosition());
        dataSet.setComplaint(getComplaintFromArrayList(mComplaintDataSet.get(position).getComplaintFaultID()));
        String complaintRow = "\t"+ (position + 1) + ".)  " + dataSet.getCategory().toUpperCase() + " - " + dataSet.getComplaint();
        holder.textViewComplaints_CF.setText(complaintRow);

        Log.d(TAG, "onBindViewHolder (" + ++counterOnBindViewHolder + ") = " +
                dataSet.getAction());
        if (mLastAnimatedItemPosition < position) {
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = holder.getAdapterPosition(); // or mLastAnimatedItemPosition = position;
        }


        ////////// Setting Up Sub List //////////
        StringBuilder sbActions = new StringBuilder();
        Log.e(TAG, "mComplaintDataSetsize:" + mComplaintDataSet.size());
        for (int i = 0; mComplaintDataSet.size() > i; i++) {
            Log.e(TAG, "getSJ_CM_CF_IDStringBuilder:" + mComplaintDataSet.get(i).getSJ_CM_CF_ID());
            if (dataSet.getSJ_CM_CF_ID() == mComplaintDataSet.get(i).getSJ_CM_CF_ID() &&
                    dataSet.getCategoryID() == mComplaintDataSet.get(i).getCategoryID()) {
                sbActions.append(mComplaintDataSet.get(i).getAction());
                sbActions.append(LIST_DELIM);
            }
        }

        // Preparing SubList Data
        final String[] aSubItem = sbActions.toString().split(LIST_DELIM);
        for (String item : aSubItem) {
            Log.e(TAG, "aSubItem:" +item);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (mContext, R.layout.i_complaint_action_list_item, aSubItem);

        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = li.inflate(R.layout.i_complaint_row_item, holder.linearLayoutLV, false);
        holder.linearLayoutLV.addView(vi);

        final ExpandableHeightListView lvComplaints = (ExpandableHeightListView) vi.findViewById(R.id.lvComplaints);

        //Data bind ListView with ArrayAdapter
        lvComplaints.setAdapter(adapter);
        lvComplaints.setExpanded(true);
        lvComplaints.setDividerHeight(0);

        //Set an Item Click Listener for ListView items
        lvComplaints.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                //Get ListView clicked item's corresponded Array element value
                String clickedItemValue = Arrays.asList(aSubItem).get(pos);

                //Generate a Toast message
                String toastMessage = "Position : "+ pos + " || Value : " + clickedItemValue;
                Log.e(TAG, toastMessage);

                dataSet.setAction(clickedItemValue);
                if (!isBeforeFragment) {
                    mSetHelper.setActionOnClickDelete(
                            mCallback,
                            position,
                            mMobileDataSet.get(position),
                            dataSet,
                            ACTION_DELETE_DETAILS);
                }
            }
        });
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView textViewComplaints_CF;
        //final ListView lvComplaints;
        final LinearLayout linearLayoutLV;
        final ExpandableRelativeLayout expandableLayout;

        public ViewHolder(View view) {
            super(view);

            // Date Information
            textViewComplaints_CF = (TextView) view.findViewById(R.id.textViewComplaints_CF);
            textViewComplaints_CF.setOnClickListener(this);

            linearLayoutLV = (LinearLayout) view.findViewById(R.id.linearLayoutLV);
            //lvComplaints = (ListView) view.findViewById(R.id.lvComplaints);

            // Panel Hiding/Showing
            expandableLayout = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout);
            // mSetHelper.hideView(expandableLayout);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == textViewComplaints_CF.getId()) {
                if (mCallback != null) {
                    mSetHelper.doHideOrShow(expandableLayout);
                }
            }
        }

    }
}
