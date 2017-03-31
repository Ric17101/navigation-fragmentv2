package admin4.techelm.com.techelmtechnologies.task;

import android.os.AsyncTask;

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

public class TaskCanceller implements Runnable {
    private AsyncTask task;

    public TaskCanceller(AsyncTask task) {
        this.task = task;
    }

    @Override
    public void run() {
        if (task.getStatus() == AsyncTask.Status.RUNNING )
            task.cancel(true);
    }
}
