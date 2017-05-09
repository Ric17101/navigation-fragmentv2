package admin4.techelm.com.techelmtechnologies.adapter.listener;

import admin4.techelm.com.techelmtechnologies.model.projectjob.b2.IPI_TaskFinalWrapper;

/**
 * Created by admin 4 on 05/05/2017.
 *
 */

public interface IPIFinalTaskListener {

    /**
     * Callback invoked when clicked
     *
     * @param position            - the position
     * @param ipiTaskFinalWrapper - the object to pass back
     * @param mode                - Action to be done
     */
    void onHandleSelection(int position, IPI_TaskFinalWrapper ipiTaskFinalWrapper, int mode);
}
