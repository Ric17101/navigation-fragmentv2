package admin4.techelm.com.techelmtechnologies.adapter.listener;

import admin4.techelm.com.techelmtechnologies.model.projectjob.b2.IPI_CorrectiveActionFinalWrapper;

/**
 * Created by admin 4 on 05/05/2017.
 *
 */

public interface IPI_CorrectiveActionFinal_TaskListener {

    /**
     * Callback invoked when clicked
     *
     * @param position                        - the position
     * @param ipiCorrectiveActionFinalWrapper - the object to pass back
     * @param mode                            - Action to be done
     */
    void onHandleSelection(int position, IPI_CorrectiveActionFinalWrapper ipiCorrectiveActionFinalWrapper, int mode);
}
