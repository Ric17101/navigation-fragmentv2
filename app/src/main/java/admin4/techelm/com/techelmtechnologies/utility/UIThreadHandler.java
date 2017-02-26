package admin4.techelm.com.techelmtechnologies.utility;

import android.content.Context;
import android.os.Handler;

/**
 * Created by admin 4 on 22/02/2017.
 * Class for handling the UI Thread which is asynchronous
 */

public class UIThreadHandler {
        private final Handler handler;

        public UIThreadHandler(Context context){
            handler = new Handler(context.getMainLooper());
        }

        public void someMethod() {
            // Do work
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Code to run on UI thread
                }
            });
        }

        public void runOnUiThread(Runnable r) {
            handler.post(r);
        }
}
