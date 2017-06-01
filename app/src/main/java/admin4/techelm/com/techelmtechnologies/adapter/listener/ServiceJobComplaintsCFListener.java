package admin4.techelm.com.techelmtechnologies.adapter.listener;

import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_CFWrapper;

/**
 * Created by admin 4 on 03/05/2017.
 *
 */

public interface ServiceJobComplaintsCFListener {

    /**
     * Callback invoked when clicked
     *
     * @param position - the position
     * @param serviceJobComplaint_cfWrapper - the text to pass back
     */
    void onHandleSelection(int position, ServiceJobComplaint_CFWrapper serviceJobComplaint_cfWrapper, int mode);
}
