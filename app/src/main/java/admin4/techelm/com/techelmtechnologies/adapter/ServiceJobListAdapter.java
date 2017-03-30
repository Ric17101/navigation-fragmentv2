package admin4.techelm.com.techelmtechnologies.adapter;

/**
 * Created by admin 4 on 16/02/2017.
 * Services Rendered at the Recycler View after clicking Calendar Date
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.ServiceJobWrapper;

public class ServiceJobListAdapter extends RecyclerView.Adapter<ServiceJobListAdapter.ViewHolder> {

    private static final String LOG_TAG = "RecyclerViewAdapter";
    private final int CHECK_CODE = 0x1;
    private final int SHORT_DURATION = 1000;
    private final static String STATUS_NEW = "0";
    private final static String STATUS_UNSIGNED = "1";
    private final static String STATUS_PENDING = "2";
    private final static String STATUS_COMPLETED = "3";
    private final static String STATUS_INCOMPLETE = "4";
    private final static String STATUS_ON_PROCESS = "5";

    private List<ServiceJobWrapper> mDataSet = new ArrayList<>();
    private ServiceJobWrapper serviceJobDataSet;
    private CallbackInterface mCallback;
    private int mLastAnimatedItemPosition = -1;
    private int mLasItemPosition = 0;
    private Context mContext;

    private OnItemClickListener mItemsOnClickListener;
    private int counterOnBindViewHolder = 0;

    public ServiceJobListAdapter(Context context) {
        mContext = context;

        // .. Attach the interface
        try {
            mCallback = (CallbackInterface) context; // TODO: Troubleshooting the OnClickListener of the CardView Buttons inside the RecyclerView
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e("MyAdapter", "Must implement the CallbackInterface in the Activity", ex);
        }
        System.gc();
    }

    public ServiceJobListAdapter(List<ServiceJobWrapper> serviceJobList) {
        this.mDataSet = serviceJobList;
        notifyDataSetChanged();
    }

    public void swapData(List<ServiceJobWrapper> mNewDataSet) {
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
        serviceJobDataSet = mDataSet.get(holder.getAdapterPosition());
        holder.textViewDay.setText(serviceJobDataSet.getServiceNumber());
        holder.textViewDateNumber.setText(serviceJobDataSet.getID() + "");
        holder.textViewDate.setText(serviceJobDataSet.getStartDate());
        holder.textViewServiceNum.setText(serviceJobDataSet.getServiceNumber());
        holder.textViewCustomer.setText(serviceJobDataSet.getCustomerName());
        holder.textViewEngineer.setText(serviceJobDataSet.getEngineer());
        holder.textViewStatus.setText(setStatus(serviceJobDataSet.getStatus()));
        holder.textViewStatus.setTextColor(setColor(serviceJobDataSet.getStatus()));
        holder.textViewTask.setText(Html.fromHtml(setTaskText(serviceJobDataSet.getStatus())));
//        if (serviceJobDataSet.getStatus() == "3")
//            holder.buttonTask.setVisibility(View.GONE);
        holder.buttonTask.setImageResource(setIconTask(serviceJobDataSet.getStatus()));

        Log.d(LOG_TAG, "onBindViewHolder (" + ++counterOnBindViewHolder + ") = " + serviceJobDataSet.getServiceNumber());

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
    private String setStatus(String status) {
        switch (status) {
            case STATUS_NEW : status = "New";
                break;
            case STATUS_UNSIGNED : status = "Unsigned";
                break;
            case STATUS_PENDING : status = "Pending";
                break;
            case STATUS_COMPLETED : status = "Completed";
                break;
            case STATUS_INCOMPLETE : status = "Incomplete";
                break;
            case STATUS_ON_PROCESS : status = "On Process";
                break;
            default : status = "";
                break;
        }
        return status;
    }

    public int setColor(String status) {
        switch (status) {
            /*
                0 - New - Begin Task
                1 - unsigned
                2 - Pending
                3 - Completed
                4 - Incomplete - Continue
                5 - On Process
            * */
            case STATUS_COMPLETED : return Color.BLUE;
            case STATUS_NEW :
            case STATUS_UNSIGNED :
            case STATUS_PENDING :
            case STATUS_ON_PROCESS :
            case STATUS_INCOMPLETE : return Color.RED;
        }
        return Color.BLACK;
    }

    private int setIconTask(String stringTask) {
        switch (stringTask) {
            case STATUS_UNSIGNED :
            case STATUS_PENDING :
            case STATUS_INCOMPLETE : return R.mipmap.conti_icon;
            case STATUS_COMPLETED :
            case STATUS_ON_PROCESS : return R.mipmap.uploaded_icon;
            case STATUS_NEW :
            default: return R.mipmap.begin_icon;
        }
    }

    private String setTaskText(String taskText) {
        switch (taskText) {
            case STATUS_UNSIGNED :
            case STATUS_PENDING :
            case STATUS_INCOMPLETE : taskText = "<u>Continue >></u>";
                break;
            case STATUS_NEW : taskText = "<b>Begin Task >></b>";
                break;
            case STATUS_COMPLETED : taskText = "<u>Completed</u>";
                break;
            case STATUS_ON_PROCESS : taskText = "<b>On Process</b>";
                break;
            default : taskText = "";
                break;
        }
        return taskText;
    }

    // Test whether it is completed, then do nothing
    private boolean isCompleted(String status) {
        return status.equals("1");
    }

    public interface CallbackInterface {

        /**
         * Callback invoked when clicked
         *
         * @param position - the position
         * @param serviceJob - the text to pass back
         */
        void onHandleSelection(int position, ServiceJobWrapper serviceJob, int mode);
    }

    public interface OnItemClickListener {
        void onClick(ServiceJobWrapper colorWrapper);
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
            buttonTask.setOnClickListener(this);

            // ImageButton Links
            textViewTask = (TextView) view.findViewById(R.id.textViewTask);

            frameLayoutButtonSJ = (FrameLayout) view.findViewById(R.id.frameLayoutButtonSJ);
            frameLayoutButtonSJ.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == frameLayoutButtonSJ.getId()) {
                if (mCallback != null) {
                    mCallback.onHandleSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), 2);
                }
            } else if (v.getId() == buttonTask.getId() || v.getId() == textViewTask.getId()) {
                if (mCallback != null) {
                    if (!isCompleted(mDataSet.get(getAdapterPosition()).getStatus()))
                        mCallback.onHandleSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), 4);
                }
            }

        }
    }
}
