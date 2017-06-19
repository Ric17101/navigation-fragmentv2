package admin4.techelm.com.techelmtechnologies.adapter;

/**
 * Created by admin 4 on 16/02/2017.
 *
 */

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_CFWrapper;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.ACTION_VIEW_TASK;

/**
 * Sublist of Problems with Action DropDown List
 */
public class SJ_Complaint_CFSubListAdapter extends RecyclerView.Adapter<SJ_Complaint_CFSubListAdapter.ViewHolder> {

    private static final String TAG = SJ_Complaint_CFSubListAdapter.class.getSimpleName();
    private final int CHECK_CODE = 0x1;
    private final int SHORT_DURATION = 1000;
    private List<ServiceJobComplaint_CFWrapper> mDataSet = new ArrayList<>();
    private String[] mCategoryList;
    private ServiceJobComplaint_CFWrapper serviceJobDataSet;
    private CallbackInterface mCallback;
    private int mLastAnimatedItemPosition = -1;
    private int mLasItemPosition = 0;
    private Context mContext;
    private boolean isBeforeFragment = true;

    private OnItemClickListener mItemsOnClickListener;
    private int counterOnBindViewHolder = 0;

    public SJ_Complaint_CFSubListAdapter(Context context) {
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

    public SJ_Complaint_CFSubListAdapter(List<ServiceJobComplaint_CFWrapper> serviceJobList) {
        this.mDataSet = serviceJobList;
        notifyDataSetChanged();
    }

    public SJ_Complaint_CFSubListAdapter swapData(List<ServiceJobComplaint_CFWrapper> mNewDataSet,
        String[] categoryList, boolean fragmentMode) {
        mCategoryList = categoryList;
        mDataSet = mNewDataSet;
        isBeforeFragment = fragmentMode;
        notifyDataSetChanged();
        return this;
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
         * @param clickedItemAction
         * @param mode
         */
        void onHandleActionsSelection(int position, ServiceJobComplaint_CFWrapper ServiceJobComplaint_CFWrapper, String clickedItemAction, int mode);
        void onHandleDeleteActionsFromListSelection(int id);
    }

    public interface OnItemClickListener {
        void onClick(ServiceJobComplaint_CFWrapper colorWrapper);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.i_complaint_list_layout, parent, false);
        mContext = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        serviceJobDataSet = mDataSet.get(holder.getAdapterPosition());

        // Set the Complaints Problem from the List
        holder.textViewProblems.setText(serviceJobDataSet.getComplaint());

        setUpViewHolderContents(holder, position);

        // Just to see what inside the Holder
        Log.d(TAG, "onBindViewHolder (" + ++counterOnBindViewHolder + ") = " +
                serviceJobDataSet.getComplaint() + " " + serviceJobDataSet.getSJComplaintFaultID());
        if (mLastAnimatedItemPosition < position) {
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = holder.getAdapterPosition(); // or mLastAnimatedItemPosition = position;
        }
    }

    @Override
    public int getItemCount() { return mDataSet.size(); }

    private void setUpViewHolderContents(final ViewHolder holder, final int position) {
        final ArrayList<String> options = setDDOptions(mCategoryList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item, options);
        holder.spinnerActions.setAdapter(adapter);

        //Set an Item Click Listener for ListView items
        holder.spinnerActions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos > 0) { // Index 0 -  Select --- CHECK
                    String clickedItemAction = options.get(pos);
                    String toastMessage = "Position : " + pos + " || Value : " + clickedItemAction;
                    Log.e(TAG, "onItemSelectedtoastMessage: " + toastMessage);

                    if (mCallback != null) {
                        mCallback.onHandleActionsSelection(pos, mDataSet.get(position), clickedItemAction, ACTION_VIEW_TASK);
                    }

                    holder.spinnerActions.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    // Setting DropDownList
    private ArrayList<String> setDDOptions(String[] mCategoryList) {
        final ArrayList<String> options = new ArrayList<>();
        // Collections.addAll(options, mCategoryList);

        int counter = 0;
        for (String item : mCategoryList) {
            if (counter == 0) {
                options.add("--- Select Action ---");
                options.add(item);
            }
            else
                options.add(item);
            counter++;
        }

        return options;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewProblems;
        Spinner spinnerActions;
        TextView textViewLabelColon;
        CardView spinnerActionsCardView;

        public ViewHolder(View view) {
            super(view);
            // Date Information
            textViewProblems = (TextView) view.findViewById(R.id.textViewProblems);
            spinnerActions = (Spinner) view.findViewById(R.id.spinnerActions);

            // Setting Up the Before and After ListViews
            textViewLabelColon = (TextView) view.findViewById(R.id.textViewLabelColon);
            spinnerActionsCardView = (CardView) view.findViewById(R.id.spinnerActionsCardView);
            if (isBeforeFragment) {
                textViewLabelColon.setVisibility(View.GONE);
                spinnerActionsCardView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == textViewProblems.getId()) {
                if (mCallback != null) {
                    // mCallback.onHandleRecordingsSelection(getAdapterPosition(), mDataSet.get(getAdapterPosition()), 2);
                    mCallback.onHandleDeleteActionsFromListSelection(mDataSet.get(getAdapterPosition()).getID());
                }
            } else if (v.getId() == textViewProblems.getId()) {
                if (mCallback != null) {
                    mCallback.onHandleDeleteActionsFromListSelection(mDataSet.get(getAdapterPosition()).getID());
                }

            }

        }
    }
}
