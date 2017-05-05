package admin4.techelm.com.techelmtechnologies.adapter.listener;

import admin4.techelm.com.techelmtechnologies.model.projectjob.b1.PISSTaskWrapper;

/**
 * Created by admin 4 on 03/05/2017.
 */

public interface PISSTaskListener {

    /**
     * Callback invoked when clicked
     *
     * @param position   - the position
     * @param projectTaskWrapper - the object to pass back
     * @param mode - Action to be done
     */
    void onHandleSelection(int position, PISSTaskWrapper projectTaskWrapper, int mode);
}
