package admin4.techelm.com.techelmtechnologies.adapter.listener;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
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
import admin4.techelm.com.techelmtechnologies.adapter.SJ_UploadsListAdapter;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobUploadsWrapper;
import admin4.techelm.com.techelmtechnologies.model.toolboxmeeting.ToolboxMeetingUploadsWrapper;

/**
 * Created by admin 3 on 23/05/2017.
 */

public class TM_UploadListAdapter extends RecyclerView.Adapter<TM_UploadListAdapter.ViewHolder> {

    private static final String LOG_TAG = "UplaodsListAdapter";
    private final int CHECK_CODE = 0x1;
    private final int SHORT_DURATION = 1000;
    private List<ToolboxMeetingUploadsWrapper> mDataSet = new ArrayList<>();
    private ToolboxMeetingUploadsWrapper serviceUploadDataSet;
    private TM_UploadListAdapter.CallbackInterface mCallback;
    private int mLastAnimatedItemPosition = -1;
    private int mLasItemPosition = 0;
    private Context mContext;

    private TM_UploadListAdapter.OnItemClickListener mItemsOnClickListener;
    private int counterOnBindViewHolder = 0;

    public TM_UploadListAdapter(Activity context) {
        mContext = context;

        // .. Attach the interface
        try {
            mCallback = (TM_UploadListAdapter.CallbackInterface) context;
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e(LOG_TAG, "Must implement the ProjectJobListener in the Activity", ex);
        }
        // System.gc();
    }

    public TM_UploadListAdapter(List<ToolboxMeetingUploadsWrapper> serviceJobList) {
        this.mDataSet = serviceJobList;
        notifyDataSetChanged();
    }

    public void swapData(List<ToolboxMeetingUploadsWrapper> mNewDataSet) {
        this.mDataSet = mNewDataSet;
        notifyDataSetChanged();
    }

    public void setItemsOnClickListener(TM_UploadListAdapter.OnItemClickListener onClickListener) {
        this.mItemsOnClickListener = onClickListener;
    }

    @Override
    public TM_UploadListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_report_voice_record_list_item, parent, false);
        mContext = view.getContext();
        return new TM_UploadListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TM_UploadListAdapter.ViewHolder holder, final int position) {
        serviceUploadDataSet = mDataSet.get(holder.getAdapterPosition());
        holder.textViewNameRecord.setText(serviceUploadDataSet.getUploadName() + " uploaded.");

        Log.d(LOG_TAG, "onBindViewHolder (" + ++counterOnBindViewHolder + ") = " +
                serviceUploadDataSet.getFilePath() + serviceUploadDataSet.getUploadName());
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
         * @param serviceJobRecordingWrapper - the text to pass back
         */
        void onHandleUploadsSelection(int position, ToolboxMeetingUploadsWrapper serviceJobRecordingWrapper, int mode);
        void onHandleDeleteUploadsFromListSelection(int id);
        void onHandleViewUploadFromListSelection(ToolboxMeetingUploadsWrapper serviceJobRecordingWrapper);
    }

    public interface OnItemClickListener {
        void onClick(ServiceJobUploadsWrapper colorWrapper);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView textViewNameRecord;
        TextView textViewDeleteRecord;

        ImageButton imageButtonDelete;
        ImageButton imageButtonUploaded;

        public ViewHolder(View view) {
            super(view);

            // Date Information
            textViewNameRecord = (TextView) view.findViewById(R.id.textViewNameRecord);
            textViewNameRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onHandleViewUploadFromListSelection(mDataSet.get(getAdapterPosition()));
                    }
                }

            });
            textViewDeleteRecord = (TextView) view.findViewById(R.id.textViewDeleteRecord);
            textViewDeleteRecord.setVisibility(View.GONE);
            textViewDeleteRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onHandleDeleteUploadsFromListSelection(mDataSet.get(getAdapterPosition()).getID());
                    }
                }
            });

            // ImageButtons
            imageButtonDelete = (ImageButton) view.findViewById(R.id.imageButtonDelete);
            imageButtonDelete.setVisibility(View.GONE);
            imageButtonDelete.setOnClickListener(this);

            imageButtonUploaded = (ImageButton) view.findViewById(R.id.imageButtonUploaded);
            imageButtonUploaded.setVisibility(View.GONE);
            imageButtonUploaded.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == imageButtonDelete.getId()) {
                if (mCallback != null) {
                    // mCallback.onHandleRecordingsSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), 2);
                    mCallback.onHandleDeleteUploadsFromListSelection(mDataSet.get(getAdapterPosition()).getID());
                }
            } else if (v.getId() == imageButtonUploaded.getId()) {
                if (mCallback != null) {
                    mCallback.onHandleViewUploadFromListSelection(mDataSet.get(getAdapterPosition()));
                }
            }

        }
    }
}
