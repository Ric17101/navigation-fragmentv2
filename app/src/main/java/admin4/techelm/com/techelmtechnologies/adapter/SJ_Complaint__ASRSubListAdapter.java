package admin4.techelm.com.techelmtechnologies.adapter;

/**
 * Created by admin 4 on 16/02/2017.
 *
 */

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
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaintWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_ASRWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_CFWrapper;
import admin4.techelm.com.techelmtechnologies.utility.view.ExpandableHeightListView;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_DELETE_DETAILS;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_VIEW_TASK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.LIST_DELIM;

/**
 * Sublist of Problems with Action DropDown List
 */
public class SJ_Complaint__ASRSubListAdapter extends RecyclerView.Adapter<SJ_Complaint__ASRSubListAdapter.ViewHolder> {

    private static final String TAG = SJ_Complaint__ASRSubListAdapter.class.getSimpleName();
    private final int CHECK_CODE = 0x1;
    private final int SHORT_DURATION = 1000;
    private List<ServiceJobComplaint_CFWrapper> mDataSet = new ArrayList<>();
    private ArrayList<ServiceJobComplaintWrapper> mComplaintActionDataSet = new ArrayList<>();
    private ArrayList<ServiceJobComplaint_ASRWrapper> mActionListDataSet = new ArrayList<>();
    private ArrayList<ServiceJobComplaintWrapper> mComplaintToShowOnList = new ArrayList<>();
    private ArrayList<ServiceJobComplaintWrapper> mComplaintToShowOnListRemovedDuplicates = new ArrayList<>();

    private String[] mActionList;

    //private ServiceJobComplaint_CFWrapper serviceJobComplaintDataSet;
    private ServiceJobComplaintWrapper mComplaintDataSetToShow;
    private ServiceJobComplaintWrapper mComplaintDataSet;
    private CallbackInterface mCallback;
    private int mLastAnimatedItemPosition = -1;
    private int mLasItemPosition = 0;
    private Context mContext;

    private OnItemClickListener mItemsOnClickListener;
    private int counterOnBindViewHolder = 0;

    public SJ_Complaint__ASRSubListAdapter(Context context) {
        mContext = context;

        // .. Attach the interface
        try {
            mCallback = (CallbackInterface) context;
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e(TAG, "Must implement the ProjectJobListener in the Activity", ex);
        }
        // System.gc();
    }

    public SJ_Complaint__ASRSubListAdapter(List<ServiceJobComplaint_CFWrapper> serviceJobList) {
        this.mDataSet = serviceJobList;
        notifyDataSetChanged();
    }

    public SJ_Complaint__ASRSubListAdapter swapData(ArrayList<ServiceJobComplaint_CFWrapper> mNewDataSet,
            ArrayList<ServiceJobComplaintWrapper> complaintDataSet,
            ServiceJobComplaintWrapper dataSet,
            ArrayList<ServiceJobComplaint_ASRWrapper> actionList)
    {
        mDataSet = mNewDataSet;
        mComplaintDataSet = dataSet;
        mActionListDataSet = actionList;
        mComplaintActionDataSet = complaintDataSet;
        /*Log.e(TAG, "mDataSet:" + mDataSet.size());
        Log.e(TAG, "==========mDataSet==========:" + mDataSet.toString());
        Log.e(TAG, "++++++++++++mComplaintDataSet++++++++++++:" + mComplaintDataSet.toString());
        Log.e(TAG, "mActionListDataSet:" + mActionListDataSet.size());
        Log.e(TAG, "==========mActionListDataSet==========:" + mActionListDataSet.toString());
        Log.e(TAG, "mComplaintActionDataSet:" + mComplaintActionDataSet.size());
        Log.e(TAG, "==========mComplaintActionDataSet==========:" + mComplaintActionDataSet.toString());*/
        setComplaintLists(mComplaintDataSet, mComplaintActionDataSet);
        /*Log.e(TAG, "mComplaintToShowOnList:" + mComplaintToShowOnList.size());
        Log.e(TAG, "==========mComplaintToShowOnList==========:" + mComplaintToShowOnList.toString());*/
        // removeComplaintDuplicates(complaintDataSet, dataSet);
        removeComplaintCategoryDuplicates(mComplaintToShowOnList);
        notifyDataSetChanged();
        return this;
    }

    /**
     This method get only the cm_cf_id Per Category,
        all item already on the list will be ignored
     */
    private void removeComplaintCategoryDuplicates(ArrayList<ServiceJobComplaintWrapper> complaints) {
        mComplaintToShowOnListRemovedDuplicates = new ArrayList<ServiceJobComplaintWrapper>(complaints);
        Log.e(TAG, "COUNTTTTT:" + mComplaintToShowOnListRemovedDuplicates.size() + " BEFORE Duplicate Remove:" + mComplaintToShowOnListRemovedDuplicates.toString());

        for (int i = 0; i < mComplaintToShowOnListRemovedDuplicates.size(); i++) {
            for (int j = i+1; j < mComplaintToShowOnListRemovedDuplicates.size(); j++) {
                if (mComplaintToShowOnList.get(i).getSJ_CM_CF_ID() == mComplaintToShowOnList.get(j).getSJ_CM_CF_ID() &&
                    mComplaintToShowOnListRemovedDuplicates.get(i).getCategoryID() == mComplaintToShowOnListRemovedDuplicates.get(j).getCategoryID())
                {
                    mComplaintToShowOnListRemovedDuplicates.remove(j);
                    j--;
                }
            }
        }
        Log.e(TAG, "COUNTTTTT:" + mComplaintToShowOnListRemovedDuplicates.size() + "AFTER Duplicate Remove:" + mComplaintToShowOnListRemovedDuplicates.toString());
    }

    private void setComplaintLists(ServiceJobComplaintWrapper dataset,
                                   ArrayList<ServiceJobComplaintWrapper> complaints) {
        mComplaintToShowOnList = new ArrayList<>();
        for (ServiceJobComplaintWrapper item : complaints) {
            if (item.getCategoryID() == dataset.getCategoryID())
            {
                mComplaintToShowOnList.add(item);
            }
        }
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener) {
        this.mItemsOnClickListener = onClickListener;
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

    public interface CallbackInterface {
        /**
         *  Callback invoked when clicked
         * @param position - the position
         * @param ServiceJobComplaint_CFWrapper - the text to pass back
         * @param complaint
         * @param clickedItemAction
         * @param mode
         */
        void onHandleASRDeleteSelection(int position, ServiceJobComplaint_CFWrapper ServiceJobComplaint_CFWrapper, ServiceJobComplaintWrapper complaint, String clickedItemAction, int mode);
        void onHandleDeleteASRFromListSelection(int id);
    }

    public interface OnItemClickListener {
        void onClick(ServiceJobComplaint_CFWrapper colorWrapper);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.i_action_list_layout, parent, false);
        mContext = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mComplaintDataSetToShow = mComplaintToShowOnListRemovedDuplicates.get(holder.getAdapterPosition());

        // Set the Complaints Problem from the List
        holder.textViewProblems.setText(mComplaintDataSetToShow.getComplaint());

        setUpViewHolderContents(mComplaintDataSetToShow, holder, position);

        if (mLastAnimatedItemPosition < position) {
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = holder.getAdapterPosition(); // or mLastAnimatedItemPosition = position;
        }
    }

    private String[] getActionListByComplaint(ServiceJobComplaintWrapper complaint)
    {
        StringBuilder sbActions = new StringBuilder();
        for (ServiceJobComplaintWrapper item : mComplaintActionDataSet) {

            if (item.getCategoryID() == complaint.getCategoryID() &&
                item.getSJ_CM_CF_ID() == complaint.getSJ_CM_CF_ID())
            {
                sbActions.append(item.getAction());
                sbActions.append(LIST_DELIM);
            }
        }
        return sbActions.toString().split(LIST_DELIM);
    }

    @Override
    public int getItemCount() { return mComplaintToShowOnListRemovedDuplicates.size(); }


    private void setUpViewHolderContents(final ServiceJobComplaintWrapper complaint, final ViewHolder holder, final int position) {
        mActionList = getActionListByComplaint(complaint);
        final ArrayList<String> options = setDDOptions(mActionList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                R.layout.i_complaint_list_item, options);

        holder.lvActions.setAdapter(adapter);
        holder.lvActions.setExpanded(true);
        //Set an Item Click Listener for ListView items
        holder.lvActions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                //if (pos > 0) { // Index 0 -  Select --- CHECK
                //}
                String clickedItemAction = options.get(pos);
                String toastMessage = "Position : " + pos + " || Value : " + clickedItemAction;
                Log.e(TAG, "onItemSelectedtoastMessage: " + toastMessage);

                if (mCallback != null) {
                    mCallback.onHandleASRDeleteSelection(pos, mDataSet.get(position), complaint, clickedItemAction, ACTION_DELETE_DETAILS);
                }
            }
        });
    }

    // Setting DropDownList
    private ArrayList<String> setDDOptions(String[] actionList) {
        final ArrayList<String> options = new ArrayList<>();
        Collections.addAll(options, actionList);
        return options;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewProblems;
        final ExpandableHeightListView lvActions;
        final ExpandableRelativeLayout expandableLayout;
        //final LinearLayout linearLayoutActionLV;

        public ViewHolder(View view) {
            super(view);
            // Date Information
            textViewProblems = (TextView) view.findViewById(R.id.textViewProblems);
            //spinnerActions = (Spinner) view.findViewById(R.id.spinnerActions);
            //linearLayoutActionLV = (LinearLayout) view.findViewById(R.id.linearLayoutActionLV);
            expandableLayout = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout);

            lvActions = (ExpandableHeightListView) view.findViewById(R.id.lvActions);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == textViewProblems.getId()) {
                if (mCallback != null) {
                    // mCallback.onHandleRecordingsSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), 2);
                    mCallback.onHandleDeleteASRFromListSelection(mDataSet.get(getAdapterPosition()).getID());
                }
            } else if (v.getId() == textViewProblems.getId()) {
                if (mCallback != null) {
                    mCallback.onHandleDeleteASRFromListSelection(mDataSet.get(getAdapterPosition()).getID());
                }
            }
        }

    }
}
