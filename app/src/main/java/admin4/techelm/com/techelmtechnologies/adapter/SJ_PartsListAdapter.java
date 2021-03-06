package admin4.techelm.com.techelmtechnologies.adapter;

/**
 * Created by admin 4 on 16/02/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobNewPartsWrapper;

public class SJ_PartsListAdapter extends RecyclerView.Adapter<SJ_PartsListAdapter.ViewHolder> {

    private static final String LOG_TAG = "PartsListAdapter";
    private final int CHECK_CODE = 0x1;
    private final int SHORT_DURATION = 1000;
    private List<ServiceJobNewPartsWrapper> mDataSet = new ArrayList<>();
    private ServiceJobNewPartsWrapper servicePartDataSet;
    private CallbackInterface mCallback;
    private int mLastAnimatedItemPosition = -1;
    private int mLasItemPosition = 0;
    private Context mContext;

    private OnItemClickListener mItemsOnClickListener;
    private int counterOnBindViewHolder = 0;

    public SJ_PartsListAdapter(Activity context) {
        mContext = context;

        // .. Attach the interface
        try {
            mCallback = (CallbackInterface) context;
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e(LOG_TAG, "Must implement the ProjectJobListener in the Activity", ex);
        }
        // System.gc();
    }

    public SJ_PartsListAdapter(List<ServiceJobNewPartsWrapper> serviceJobList) {
        this.mDataSet = serviceJobList;
        notifyDataSetChanged();
    }

    public void swapData(List<ServiceJobNewPartsWrapper> mNewDataSet) {
        mDataSet = mNewDataSet;
        notifyDataSetChanged();
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener) {
        this.mItemsOnClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_report_replacement_part_list_item, parent, false);
        mContext = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        servicePartDataSet = mDataSet.get(holder.getAdapterPosition());
        holder.textViewPartDetail.setText((position + 1) +".) Replacement " + servicePartDataSet.getPartName() + " Added.");
        holder.textViewQuantity.setText(Html.fromHtml("<b>Qty.</b>: " + servicePartDataSet.getQuantity()));
        holder.textViewUnitPrice.setText(Html.fromHtml("<b>Unit Price</b>: " + servicePartDataSet.getUnitPrice()));

        Log.d(LOG_TAG, "onBindViewHolder (" + ++counterOnBindViewHolder + ") = " +
                servicePartDataSet.getQuantity() + servicePartDataSet.getPartName());
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
         * @param serviceJobNewPartsWrapper - the text to pass back
         */
        void onHandlePartsSelection(int position, ServiceJobNewPartsWrapper serviceJobNewPartsWrapper, int mode);
        void onHandleDeletePartsFromListSelection(int id);
        void onHandleViewPartFromListSelection(ServiceJobNewPartsWrapper serviceJobPartWrapper);
    }

    public interface OnItemClickListener {
        void onClick(ServiceJobNewPartsWrapper partWrapper);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewPartDetail;
        TextView textViewQuantity;
        TextView textViewUnitPrice;
        TextView textViewEdit;
        TextView textViewDelete;

        ImageButton imageButtonDeletes;
        ImageButton imageButtonEdit;

        public ViewHolder(View view) {
            super(view);

            // Part Information
            textViewQuantity = (TextView) view.findViewById(R.id.textViewQuantity);
            textViewUnitPrice = (TextView) view.findViewById(R.id.textViewUnitPrice);
            textViewPartDetail = (TextView) view.findViewById(R.id.textViewPartDetail);
            textViewPartDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onHandleViewPartFromListSelection(mDataSet.get(getAdapterPosition()));
                    }
                }

            });

            textViewEdit = (TextView) view.findViewById(R.id.textViewEdit);
            textViewEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onHandleViewPartFromListSelection(mDataSet.get(getAdapterPosition()));
                    }
                }

            });
            textViewDelete = (TextView) view.findViewById(R.id.textViewDelete);
            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onHandleDeletePartsFromListSelection(mDataSet.get(getAdapterPosition()).getID());
                    }
                }
            });

            // ImageButtons
            imageButtonDeletes = (ImageButton) view.findViewById(R.id.imageButtonDeletes);
            imageButtonDeletes.setOnClickListener(this);

            imageButtonEdit = (ImageButton) view.findViewById(R.id.imageButtonEdit);
            imageButtonEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == imageButtonDeletes.getId()) {
                if (mCallback != null) {
                    // mCallback.onHandleRecordingsSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), 2);
                    mCallback.onHandleDeletePartsFromListSelection(mDataSet.get(getAdapterPosition()).getID());
                }
            } else if (v.getId() == imageButtonEdit.getId()) {
                if (mCallback != null) {
                    mCallback.onHandleViewPartFromListSelection(mDataSet.get(getAdapterPosition()));
                }

            }

        }
    }
}
