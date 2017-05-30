package admin4.techelm.com.techelmtechnologies.adapter;

/**
 * Created by admin 4 on 16/02/2017.
 * Projects Rendered at the Recycler View
 */

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.projectjob_main.helper.FragmentSetListHelper_ProjectJob;
import admin4.techelm.com.techelmtechnologies.adapter.listener.ProjectJobListener;
import admin4.techelm.com.techelmtechnologies.model.projectjob.ProjectJobWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_CHOOSE_FORM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_VIEW_DETAILS;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_CHOOSE_FORM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_COMPLETED;

public class PJ_ListAdapter extends RecyclerView.Adapter<PJ_ListAdapter.ViewHolder> {

    private static final String LOG_TAG = PJ_ListAdapter.class.getSimpleName();
    private final int CHECK_CODE = 0x1;
    private final int SHORT_DURATION = 1000;

    private List<ProjectJobWrapper> mDataSet = new ArrayList<>();
    private ProjectJobWrapper serviceJobDataSet;
    private ProjectJobListener mCallback;
    private int mLastAnimatedItemPosition = -1;
    private int mLasItemPosition = 0;
    private Context mContext;

    private OnItemClickListener mItemsOnClickListener;
    private int counterOnBindViewHolder = 0;

    private FragmentSetListHelper_ProjectJob mSetHelper;

    public PJ_ListAdapter(Context context) {
        mContext = context;

        // .. Attach the interface
        try {
            mCallback = (ProjectJobListener) context; // TODO: Troubleshooting the OnClickListener of the CardView Buttons inside the RecyclerView
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e("MyAdapter", "Must implement the ProjectJobListener in the Activity", ex);
        }
        System.gc();
    }

    public PJ_ListAdapter(List<ProjectJobWrapper> serviceJobList) {
        this.mDataSet = serviceJobList;
        notifyDataSetChanged();
    }

    public void swapData(List<ProjectJobWrapper> mNewDataSet) {
        mDataSet = mNewDataSet;
        notifyDataSetChanged();
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener) {
        this.mItemsOnClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_results_servicejob_list_item, parent, false);
        mContext = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        this.mSetHelper = new FragmentSetListHelper_ProjectJob();

        serviceJobDataSet = mDataSet.get(holder.getAdapterPosition());
        holder.textViewDay.setText(serviceJobDataSet.getProjectRef());
        holder.textViewDateNumber.setText(serviceJobDataSet.getID() + "");
        holder.textViewDate.setText(serviceJobDataSet.getStartDate());
        holder.textViewServiceNum.setText(serviceJobDataSet.getStatus() + "");
        holder.textViewCustomer.setText(serviceJobDataSet.getCustomerName());
        holder.textViewEngineer.setText(serviceJobDataSet.getEngineerName());
        holder.textViewStatus.setText(this.mSetHelper.setStatus(serviceJobDataSet.getStatus()+""));
        holder.textViewStatus.setTextColor(this.mSetHelper.setColor(serviceJobDataSet.getStatus()+""));
        holder.textViewTask.setText(Html.fromHtml(this.mSetHelper.setTaskText(PROJECT_JOB_CHOOSE_FORM)));
        holder.buttonTask.setImageResource(this.mSetHelper.setIconTask(serviceJobDataSet.getStatus()+""));

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

    // Test whether it is completed, then do nothing
    private boolean isCompleted(String status) {
        return status.equals(SERVICE_JOB_COMPLETED);
    }


    public interface OnItemClickListener {
        void onClick(ProjectJobWrapper colorWrapper);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView textViewDateNumber;
        public final TextView textViewDay;
        public final TextView textViewDate;

        public final TextView textViewServiceNum;
        public final TextView textViewCustomer;
        public final TextView textViewEngineer;
        public final TextView textViewStatus;

        public final ImageButton buttonTask;
        public final TextView textViewTask;

        private final FrameLayout frameLayoutButtonSJ;
        public final FrameLayout buttonTaskFrameLayout;

        public ViewHolder(View view) {
            super(view);

            // Date Information
            textViewDateNumber = (TextView) view.findViewById(R.id.textViewDateNumber);
            textViewDay = (TextView) view.findViewById(R.id.textViewDay);
            textViewDate = (TextView) view.findViewById(R.id.textViewDate);

            // Data from DB
            textViewServiceNum = (TextView) view.findViewById(R.id.textViewServiceNum);
            textViewCustomer = (TextView) view.findViewById(R.id.textViewCustomer);
            textViewEngineer = (TextView) view.findViewById(R.id.textViewEngineer);
            textViewStatus = (TextView) view.findViewById(R.id.textViewStatus);

            // ImageButtons
            buttonTask = (ImageButton) view.findViewById(R.id.buttonTask);
            // buttonTask.setOnClickListener(this);
            buttonTaskFrameLayout = (FrameLayout) view.findViewById(R.id.buttonTaskFrameLayout);
            buttonTaskFrameLayout.setOnClickListener(this);

            // ImageButton Links
            textViewTask = (TextView) view.findViewById(R.id.textViewTask);

            frameLayoutButtonSJ = (FrameLayout) view.findViewById(R.id.frameLayoutButtonSJ);
            frameLayoutButtonSJ.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == frameLayoutButtonSJ.getId()) {
                if (mCallback != null) {
                    mCallback.onHandleSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), ACTION_VIEW_DETAILS);

                }
            } else if (v.getId() == buttonTaskFrameLayout.getId()) {
                if (mCallback != null) {
                    mCallback.onHandleSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), ACTION_CHOOSE_FORM);
                }
            }
        }
    }
}
