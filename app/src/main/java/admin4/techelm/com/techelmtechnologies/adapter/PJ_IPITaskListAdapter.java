package admin4.techelm.com.techelmtechnologies.adapter;

/**
 * Created by admin 4 on 16/02/2017.
 * Services Rendered at the Recycler View after clicking Calendar Date
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
import admin4.techelm.com.techelmtechnologies.adapter.listener.IPITaskListener;
import admin4.techelm.com.techelmtechnologies.model.projectjob.ProjectJobWrapper;
import admin4.techelm.com.techelmtechnologies.model.projectjob.b2.IPI_TaskWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_VIEW_TASK;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_CONFIRMATION_DATE_FORM;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_START_TASK;

public class PJ_IPITaskListAdapter extends RecyclerView.Adapter<PJ_IPITaskListAdapter.ViewHolder> {

    private static final String LOG_TAG = PJ_IPITaskListAdapter.class.getSimpleName();

    private List<IPI_TaskWrapper> mDataSet = new ArrayList<>();
    private IPI_TaskWrapper dataSet;
    private IPITaskListener mCallback;
    private int mLastAnimatedItemPosition = -1;
    private int mLasItemPosition = 0;
    private Context mContext;

    private OnItemClickListener mItemsOnClickListener;
    private int counterOnBindViewHolder = 0;

    private FragmentSetListHelper_ProjectJob mSetHelper;

    public PJ_IPITaskListAdapter(Context context) {
        mContext = context;

        // .. Attach the interface
        try {
            mCallback = (IPITaskListener) context; // TODO: Troubleshooting the OnClickListener of the CardView Buttons inside the RecyclerView
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e("MyAdapter", "Must implement the ProjectJobListener in the Activity", ex);
        }
        System.gc();
    }

    public PJ_IPITaskListAdapter(List<IPI_TaskWrapper> serviceJobList) {
        this.mDataSet = serviceJobList;
        notifyDataSetChanged();
    }

    public void swapData(List<IPI_TaskWrapper> mNewDataSet) {
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

        dataSet = mDataSet.get(holder.getAdapterPosition());
        holder.textViewDay.setText(dataSet.getSerialNo()); // GREYED Below BIG Number
        // this.mSetHelper.setTextNumberSize(holder.textViewDateNumber, dataSet.getID()+"");
        holder.textViewDateNumber.setText(dataSet.getID() + ""); // BIG Number
        holder.textViewDate.setText(dataSet.getStatusComment()); // BLACK Date Below
        holder.textViewServiceNum.setText(dataSet.getNonConformance());
        holder.textViewCustomer.setText(dataSet.getDescription());
        holder.textViewEngineer.setText(dataSet.getTargetCompletionDate());
        // holder.textViewStatus.setText(this.mSetHelper.setStatusComment(dataSet.getStatusComment()+""));
        holder.textViewStatus.setText(this.mSetHelper.setStatus(dataSet.getStatusFlag()));
        holder.textViewStatus.setTextColor(this.mSetHelper.setColor(dataSet.getStatusComment()+""));
        holder.textViewTask.setText(Html.fromHtml(this.mSetHelper.setTaskText_PISSTask(dataSet.getStatusFlag())));
        holder.buttonTask.setImageResource(this.mSetHelper.setIconTask(dataSet.getStatusComment()+""));

        Log.d(LOG_TAG, "onBindViewHolder (" + ++counterOnBindViewHolder + ") = " + dataSet.getSerialNo());

        if (mLastAnimatedItemPosition < position) {
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = holder.getAdapterPosition(); // or mLastAnimatedItemPosition = position;
        }

        holder.textViewLabelDetail1.setText("Non-Conformance");
        holder.textViewLabelDetail2.setText("Descriptions");
        holder.textViewLabelDetail3.setText("Target Remedy Date");
        holder.textViewLabelDetail4.setText("Status");
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

        public final TextView textViewLabelDetail1;
        public final TextView textViewLabelDetail2;
        public final TextView textViewLabelDetail3;
        public final TextView textViewLabelDetail4;

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
            //buttonTask.setOnClickListener(this);
            buttonTaskFrameLayout = (FrameLayout) view.findViewById(R.id.buttonTaskFrameLayout);
            buttonTaskFrameLayout.setOnClickListener(this);

            // ImageButton Links
            textViewTask = (TextView) view.findViewById(R.id.textViewTask);

            frameLayoutButtonSJ = (FrameLayout) view.findViewById(R.id.frameLayoutButtonSJ);
            frameLayoutButtonSJ.setOnClickListener(this);

            // TEXT View Label Details
            textViewLabelDetail1 = (TextView) view.findViewById(R.id.textViewLabelDetail1);
            textViewLabelDetail2 = (TextView) view.findViewById(R.id.textViewLabelDetail2);
            textViewLabelDetail3 = (TextView) view.findViewById(R.id.textViewLabelDetail3);
            textViewLabelDetail4 = (TextView) view.findViewById(R.id.textViewLabelDetail4);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == frameLayoutButtonSJ.getId()) {
                if (mCallback != null) {
                    mCallback.onHandleSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), ACTION_VIEW_TASK);
                }
            } else if (v.getId() == buttonTaskFrameLayout.getId() /*|| v.getId() == textViewTask.getId()*/) {
                if (mCallback != null) {
                    mSetHelper.setActionOnClick(mCallback, getAdapterPosition(), mDataSet.get(getAdapterPosition()), PROJECT_JOB_CONFIRMATION_DATE_FORM);
                }
            }

        }
    }
}
