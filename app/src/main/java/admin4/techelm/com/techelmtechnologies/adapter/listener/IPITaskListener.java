package admin4.techelm.com.techelmtechnologies.adapter.listener;

import admin4.techelm.com.techelmtechnologies.model.projectjob.b2.IPI_TaskWrapper;

/**
 * Created by admin 4 on 05/05/2017.
 * In-process Inspection (PW and EPS)
 * TASK LIST
 */

public interface IPITaskListener {

    /**
     * Callback invoked when clicked
     *
     * @param position   - the position
     * @param ipiTaskWrapper - the object to pass back
     * @param mode - Action to be done
     */
    void onHandleSelection(int position, IPI_TaskWrapper ipiTaskWrapper, int mode);
}
