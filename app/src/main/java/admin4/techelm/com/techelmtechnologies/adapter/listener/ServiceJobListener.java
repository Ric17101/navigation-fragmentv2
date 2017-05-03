package admin4.techelm.com.techelmtechnologies.adapter.listener;

import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobWrapper;

/**
 * Created by admin 4 on 03/05/2017.
 */

public interface ServiceJobListener {

    /**
     * Callback invoked when clicked
     *
     * @param position - the position
     * @param serviceJob - the text to pass back
     * @param mode - mode of action
     */
    void onHandleSelection(int position, ServiceJobWrapper serviceJob, int mode);
}
