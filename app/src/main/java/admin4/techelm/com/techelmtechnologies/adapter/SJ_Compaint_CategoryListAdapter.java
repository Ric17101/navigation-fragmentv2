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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.helper.FragmentSetListHelper_SJComplaint_CF;
import admin4.techelm.com.techelmtechnologies.activity.service_report_fragment.helper.FragmentSetListHelper_SJComplaint_Category;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_CategoryWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_VIEW_TASK;

public class SJ_Compaint_CategoryListAdapter extends RecyclerView.Adapter<SJ_Compaint_CategoryListAdapter.ViewHolder> {

    private static final String TAG = SJ_Compaint_CategoryListAdapter.class.getSimpleName();

    private List<ServiceJobComplaint_CategoryWrapper> mDataSet = new ArrayList<>();
    private ServiceJobComplaint_CategoryWrapper dataSet;
    private CallbackInterface mCallback;
    private int mLastAnimatedItemPosition = -1;
    private int mLasItemPosition = 0;
    private Context mContext;

    private OnItemClickListener mItemsOnClickListener;
    private int counterOnBindViewHolder = 0;

    private FragmentSetListHelper_SJComplaint_Category mSetHelper;

    public SJ_Compaint_CategoryListAdapter(Activity context) {
        mContext = context;

        // .. Attach the interface
        try {
            mCallback = (CallbackInterface) context;
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e(TAG, "Must implement the Listener in the Activity", ex);
        }
        // System.gc();
    }

    public SJ_Compaint_CategoryListAdapter(List<ServiceJobComplaint_CategoryWrapper> serviceJobList) {
        this.mDataSet = serviceJobList;
        notifyDataSetChanged();
    }

    public void swapData(List<ServiceJobComplaint_CategoryWrapper> mNewDataSet) {
        mDataSet = mNewDataSet;
        notifyDataSetChanged();
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener) {
        this.mItemsOnClickListener = onClickListener;
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
        this.mSetHelper = new FragmentSetListHelper_SJComplaint_Category();

        dataSet = mDataSet.get(holder.getAdapterPosition());
        holder.textViewComplaints_CF.setText("\t"+ (position + 1) + ".)  " + dataSet.getCategory());

        Log.d(TAG, "onBindViewHolder (" + ++counterOnBindViewHolder + ") = " +
                dataSet.getCategory());
        if (mLastAnimatedItemPosition < position) {
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = holder.getAdapterPosition(); // or mLastAnimatedItemPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
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
         * Callback invoked when clicked
         *
         * @param position - the position
         * @param ServiceJobComplaint_CategoryWrapper - the text to pass back
         */
        void onHandleSelection(int position, ServiceJobComplaint_CategoryWrapper ServiceJobComplaint_CategoryWrapper, int mode);
        void onHandleDeleteFromListSelection(int id);
        void onHandleViewFromListSelection(ServiceJobComplaint_CategoryWrapper ServiceJobComplaint_CategoryWrapper);
    }

    public interface OnItemClickListener {
        void onClick(ServiceJobComplaint_CategoryWrapper colorWrapper);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView textViewComplaints_CF;

        public ViewHolder(View view) {
            super(view);

            // Date Information
            textViewComplaints_CF = (TextView) view.findViewById(R.id.textViewComplaints_CF);
            textViewComplaints_CF.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == textViewComplaints_CF.getId()) {
                if (mCallback != null) {
                    mSetHelper.setActionOnClick(
                            mCallback,
                            getAdapterPosition(),
                            mDataSet.get(getAdapterPosition()),
                            ACTION_VIEW_TASK);
                    // mCallback.onHandleSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), ACTION_VIEW_TASK);
                }
            }
        }

    }
}
