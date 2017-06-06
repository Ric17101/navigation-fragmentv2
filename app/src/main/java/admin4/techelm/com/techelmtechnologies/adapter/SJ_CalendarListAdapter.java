package admin4.techelm.com.techelmtechnologies.adapter;

/**
 * Created by admin 4 on 16/02/2017.
 *
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.activity.servicejob_main.FragmentSetListHelper_ServiceJob;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.*;

public class SJ_CalendarListAdapter extends RecyclerView.Adapter<SJ_CalendarListAdapter.ViewHolder> {

    private static final String TAG = SJ_CalendarListAdapter.class.getSimpleName();
    private final int CHECK_CODE = 0x1;
    private final int SHORT_DURATION = 1000;

    private List<ServiceJobWrapper> mDataSet = new ArrayList<>();
    private ServiceJobWrapper dataSet;
    private CallbackInterface mCallback;
    private int mLastAnimatedItemPosition = -1;
    private int mLasItemPosition = 0;
    private Context mContext;

    private OnItemClickListener mItemsOnClickListener;
    private int counterOnBindViewHolder = 0;

    private FragmentSetListHelper_ServiceJob mSetHelper;

    public SJ_CalendarListAdapter(Context context) {
        mContext = context;

        // .. Attach the interface
        try {
            mCallback = (CallbackInterface) context; // TODO: Troubleshooting the OnClickListener of the CardView Buttons inside the RecyclerView
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e("MyAdapter", "Must implement the ProjectJobListener in the Activity", ex);
        }
        System.gc();
    }

    public SJ_CalendarListAdapter(List<ServiceJobWrapper> serviceJobList) {
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.mSetHelper = new FragmentSetListHelper_ServiceJob();

        dataSet = mDataSet.get(holder.getAdapterPosition());
        holder.textViewDay.setText(dataSet.getServiceNumber());
        //holder.textViewDateNumber.setText(getCalendarDate(dataSet.getStartDate()));
        // this.mSetHelper.setTextNumberSize(holder.textViewDateNumber, dataSet.getID()+"");
        holder.textViewDateNumber.setText(dataSet.getID() + "");
        holder.textViewDate.setText(dataSet.getStartDate());
        holder.textViewServiceNum.setText(dataSet.getServiceNumber());
        holder.textViewCustomer.setText(dataSet.getCustomerName());
        holder.textViewEngineer.setText(dataSet.getEngineerName());
        holder.textViewStatus.setText(this.mSetHelper.setStatus(dataSet.getStatus()));
        holder.textViewStatus.setTextColor(this.mSetHelper.setColor(dataSet.getStatus()));
        holder.buttonTask.setImageResource(this.mSetHelper.setIconTask(dataSet.getStatus()));
        holder.textViewTask.setText(Html.fromHtml(this.mSetHelper.setTaskText(dataSet.getStatus())));

        Log.d(TAG, "onBindViewHolder (" + ++counterOnBindViewHolder + ") = " + dataSet.getServiceNumber());
        if (mLastAnimatedItemPosition < position) {
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = holder.getAdapterPosition(); // or mLastAnimatedItemPosition = position;
        }

        // Use your listener to pass back the data used FOR callback
        // Can be Implemented here or at the ViewHolder
        /*holder.buttonSpeakAlphabet.onClick(new View.onClick() {
            @Override
            public void onClick(View v) {
                if(mCallback != null){
                    mCallback.onHandleRecordingsSelection(position, mDataSet.get(position));
                    Toast.makeText(v.getContext(), "Item " + position, Toast.LENGTH_SHORT).show();
                }
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static int getScreenHeight() {
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

    public String getCalendarDate(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(strDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int day = cal.get(Calendar.DATE);
            return day + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate.split("-")[2];
    }

    public interface CallbackInterface {

        /**
         * Callback invoked when clicked
         *
         * @param position - the position
         * @param servicejob     - the text to pass back
         */
        void onHandleSelection(int position, ServiceJobWrapper servicejob, int mode);
    }

    public interface OnItemClickListener {
        void onClick(ServiceJobWrapper colorWrapper);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textViewDateNumber;
        private final TextView textViewDay;
        private final TextView textViewDate;

        private final TextView textViewServiceNum;
        private final TextView textViewCustomer;
        private final TextView textViewEngineer;
        private final TextView textViewStatus;
        private final TextView textViewTask;

        /*private final ImageButton buttonEditDetails;
        private final ImageButton buttonViewDetails;*/
        private final ImageButton buttonTask;
        private final TextView textViewEditDetails;
        private final TextView textViewViewDetails;

        private final FrameLayout frameLayoutButtonSJ;
        public final FrameLayout buttonTaskFrameLayout;

        private ViewHolder(View view) {
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

            // ImageButtons, REMOVE AND requested by Chris for all TABS in MAIN ACTIVITY
            /*buttonEditDetails = (ImageButton) view.findViewById(R.id.buttonEditDetails);
            buttonEditDetails.setOnClickListener(this);
            buttonViewDetails = (ImageButton) view.findViewById(R.id.buttonViewDetails);
            buttonViewDetails.setOnClickListener(this);*/
            textViewTask = (TextView) view.findViewById(R.id.textViewTask);

            // ImageButtons
            buttonTask = (ImageButton) view.findViewById(R.id.buttonTask);
            // buttonTask.setOnClickListener(this);
            buttonTaskFrameLayout = (FrameLayout) view.findViewById(R.id.buttonTaskFrameLayout);
            buttonTaskFrameLayout.setOnClickListener(this);

            // ImageButton Links
            textViewEditDetails = (TextView) view.findViewById(R.id.textViewEditDetails);
            // textViewEditDetails.setOnClickListener(this);
            textViewViewDetails = (TextView) view.findViewById(R.id.textViewViewDetails);
            // textViewViewDetails.setOnClickListener(this);

            frameLayoutButtonSJ = (FrameLayout) view.findViewById(R.id.frameLayoutButtonSJ);
            frameLayoutButtonSJ.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (/*v.getId() == buttonViewDetails.getId() || */v.getId() == frameLayoutButtonSJ.getId()) {
                if (mCallback != null) {
                    mCallback.onHandleSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), ACTION_VIEW_DETAILS);
                }
            } else
            if (v.getId() == buttonTaskFrameLayout.getId()) {
                if (mCallback != null) {
                    mSetHelper.setActionOnClick(mCallback, getAdapterPosition(), mDataSet.get(getAdapterPosition()), mDataSet.get(getAdapterPosition()).getStatus());
                }
            }

        }
    }
}
