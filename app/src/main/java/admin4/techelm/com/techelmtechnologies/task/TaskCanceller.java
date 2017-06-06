package admin4.techelm.com.techelmtechnologies.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;

import admin4.techelm.com.techelmtechnologies.utility.json.JSONHelper;

/**
 * Created by admin 4 on 31/03/2017.
 * To cancel the task if the loading is running on a certain amount of time
 * And when you call your AsyncTask, you need to run the cancle task after a certain amount of time (=the timeout, in this case 20 sec)

         private Handler handler = new Handler();
         private TaskCanceler taskCanceler;
         ...
         LoadData task = new LoadData();
         taskCanceler = new TaskCanceler(task);
         handler.postDelayed(taskCanceler, 20*1000);
         task.execute(...)
     It's a good idea if you clean this up on cancel or finish with

         if(taskCanceler != null && handler != null) {
            handler.removeCallbacks(taskCanceler);
         }
    You can of course wrap this in an custom implementation of AsyncTask.
    I've used this pattern many times and it works like a charm.
    One thing to note, in rare cases the handler would not start,
    I suspect if you create it in the wrong context it will not survive in certain instances,
    so I forced the handler to be an the UI Thread with
            handler= new Handler(Looper.getMainLooper());
 */

/** @VERSION2
 * You can use this as follow

     yourTask = new PJTask_RenderList(formattedDate, "", mContext);
     new TaskCanceller(yourTask).setWait(getActivity());
     yourTask.execute((Void) null)

 * In version 1 you need to call this.mHandler.removeCallbacks(this.mTaskCanceller);,
 *  then in Version 2 don't need to do anuthing since this is Automatically Handled by the run() method
 * And then you call the Override onCalled in you Asynctask Class, like so; for your own action

     @Override
     protected void onCancelled() {
         Log.i(TAG, "onCancelled hideSwipeRefreshing() new PJTask_RenderList()");
     }

 */
public class TaskCanceller implements Runnable {

    private final static long LONG_DELAY = 20000;
    private long taskDelay = 2*1000; // 2 Seconds if not connected to internet

    private AsyncTask task;
    private Handler mHandler = null;
    private TaskCanceller mTaskCanceller;

    public TaskCanceller(AsyncTask task) {
        this.mHandler = new Handler();
        this.task = task;
        this.mTaskCanceller = this;
    }

    public TaskCanceller setWait(Activity activity) {
        if (new JSONHelper().isConnected(activity)) {
            taskDelay = LONG_DELAY;
        }
        this.mHandler.postDelayed(mTaskCanceller, taskDelay); // 20*1000 = 20 Seconds,
        return this;
    }

    // Use this to Customize the Leng of Delay in Milliseconds
    public void setLongDelay(long delay) {
        this.taskDelay = delay;
    }

    @Override
    public void run() {
        if (this.task.getStatus() == AsyncTask.Status.RUNNING )
            this.task.cancel(true);
        else { // Task is running already and 20 Secs is still counting
            if (this.mTaskCanceller != null && this.mHandler != null) {
                this.mHandler.removeCallbacks(this.mTaskCanceller);
            }
        }
    }
}
