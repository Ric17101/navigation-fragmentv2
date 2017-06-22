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
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_ASRWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_CFWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_MobileWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.LIST_DELIM;

public class SJ_Complaint_CFListAdapter extends RecyclerView.Adapter<SJ_Complaint_CFListAdapter.ViewHolder> {

    private static final String TAG = SJ_Complaint_CFListAdapter.class.getSimpleName();

    private ArrayList<ServiceJobComplaint_MobileWrapper> mMobileDataSet = new ArrayList<>();
    private ArrayList<ServiceJobComplaint_CFWrapper> mComplaintDataSet = new ArrayList<>();
    private ArrayList<ServiceJobComplaint_ASRWrapper> mASRDataSetList = new ArrayList<>();
    private ArrayList<ServiceJobComplaint_CFWrapper> mActionList = new ArrayList<>();
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

    public SJ_Complaint_CFListAdapter(Activity activity) {
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

    public SJ_Complaint_CFListAdapter(ArrayList<ServiceJobComplaint_MobileWrapper> serviceJobList) {
        this.mMobileDataSet = serviceJobList;
        notifyDataSetChanged();
    }

    public void swapData(ArrayList<ServiceJobComplaint_MobileWrapper> mNewDataSet,
                         ArrayList<ServiceJobComplaint_CFWrapper> mComplaintMobileList,
                         ArrayList<ServiceJobComplaint_ASRWrapper> mComplaintASRList, boolean isBEFORE)
    {
        mMobileDataSet = mNewDataSet;
        mComplaintDataSet = mComplaintMobileList;
        mASRDataSetList = mComplaintASRList;
        isBeforeFragment = isBEFORE;
        notifyDataSetChanged();
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener) {
        this.mItemsOnClickListener = onClickListener;
    }

    private String[] getActionFromArrayListByCategoryID(int categoryIDToSearch) {
        StringBuilder sbActions = new StringBuilder();
        Log.e(TAG, "getActionIDFromArrayList" + mASRDataSetList.toString());

        for (ServiceJobComplaint_ASRWrapper asr : this.mASRDataSetList) {
            if (asr.getSJCategoryId() == (categoryIDToSearch)) {
                sbActions.append(asr.getAction());
                sbActions.append(LIST_DELIM);
            }
        }

        // Preparing SubList Data
        String[] aSubItem = sbActions.toString().split(LIST_DELIM);

        return aSubItem;
    }

    private ArrayList<ServiceJobComplaint_CFWrapper> getSJComplaint_CFArrayList(ServiceJobComplaint_MobileWrapper category) {
        Log.e(TAG, "getActionIDFromArrayList" + mComplaintDataSet.toString());
        mActionList = new ArrayList<>();

        for (ServiceJobComplaint_CFWrapper asr : this.mComplaintDataSet) {
            if (asr.getSJCategoryID() == category.getSJCategoryId()) {
                mActionList.add(asr);
            }
        }

        return mActionList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_report_complaints_fault_list_item, parent, false);
        mContext = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        this.mSetHelper = new FragmentSetListHelper_SJComplaint_CF();

        sdataMobileSet = mMobileDataSet.get(holder.getAdapterPosition());
        holder.textViewComplaints_CF.setText("\t"+ (position + 1) + ".)  " + sdataMobileSet.getSJCategory().toUpperCase());

        Log.d(TAG, "onBindViewHolder (" + ++counterOnBindViewHolder + ") = " +
                sdataMobileSet.getSJCategory());
        if (mLastAnimatedItemPosition < position) {
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = holder.getAdapterPosition(); // or mLastAnimatedItemPosition = position;
        }

        ////////// Set up the List of Problems === Setting Dropdown Sublist Actions //////////
        String[] categoryList = getActionFromArrayListByCategoryID(sdataMobileSet.getSJCategoryId());
        SJ_Complaint_CFSubListAdapter subList = new SJ_Complaint_CFSubListAdapter(this.mContext).swapData(getSJComplaint_CFArrayList(sdataMobileSet), categoryList, isBeforeFragment);

        holder.action_complaint_list.setAdapter(subList);
        holder.action_complaint_list.setLayoutManager(new LinearLayoutManager(this.mContext));

        ////////// Setting Up Sub List //////////
        // This is working but not used
        /*StringBuilder sbComplaints = new StringBuilder();
        Log.e(TAG, mComplaintDataSet.toString());
        for (int i = 0; mComplaintDataSet.size() > i; i++) {
            if (sdataMobileSet.getSJCategoryId() == mComplaintDataSet.get(i).getSJCategoryID()) {
                sbComplaints.append(mComplaintDataSet.get(i).getComplaint());
                sbComplaints.append(LIST_DELIM);
            }
        }

        // Preparing SubList Data
        final String[] aSubItem = sbComplaints.toString().split(LIST_DELIM);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (mContext, R.layout.i_complaint_list_item, aSubItem);

        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = li.inflate(R.layout.i_complaint_row_item, holder.linearLayoutActionLV, false);
        holder.linearLayoutActionLV.addView(vi);

        final ExpandableHeightListView lvActions = (ExpandableHeightListView) vi.findViewById(R.id.lvActions);

        //Data bind ListView with ArrayAdapter
        lvActions.setAdapter(adapter);
        lvActions.setExpanded(true);
        lvActions.setDividerHeight(0);

        //Set an Item Click Listener for ListView items
        lvActions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                //Get ListView clicked item's corresponded Array element value
                String clickedItemValue = Arrays.asList(aSubItem).get(pos);

                //Generate a Toast message
                String toastMessage = "Position : "+ pos + " || Value : " + clickedItemValue;

                //Apply the ListView background color as user selected item value
                // lvActions.setBackgroundColor(Color.parseColor(clickedItemValue));

                //Display user response as a Toast message
                Toast.makeText(mContext, toastMessage, Toast.LENGTH_SHORT).show();
                Log.e(TAG, toastMessage);

                if (!isBeforeFragment) {
                    mSetHelper.setActionOnClickView(
                            mCallback,
                            position,
                            mMobileDataSet.get(position),
                            clickedItemValue,
                            mMobileDataSet.get(position).getSJCategory(),
                            mMobileDataSet.get(position).getSJCategory(),
                            ACTION_VIEW_TASK);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mMobileDataSet.size();
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
        //final ListView lvActions;
        final LinearLayout linearLayoutLV;
        final ExpandableRelativeLayout expandableLayout;
        final RecyclerView action_complaint_list;

        public ViewHolder(View view) {
            super(view);

            // Date Information
            textViewComplaints_CF = (TextView) view.findViewById(R.id.textViewComplaints_CF);
            textViewComplaints_CF.setOnClickListener(this);

            linearLayoutLV = (LinearLayout) view.findViewById(R.id.linearLayoutLV);
            action_complaint_list = (RecyclerView) view.findViewById(R.id.action_complaint_list);
            //lvActions = (ListView) view.findViewById(R.id.lvActions);

            // Panel Hiding/Showing
            expandableLayout = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout);
            expandableLayout.setEnabled(false);
            expandableLayout.setExpanded(true);
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
