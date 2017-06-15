package admin4.techelm.com.techelmtechnologies.adapter.listener;

import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaintWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_MobileWrapper;

/**
 * Created by admin 4 on 03/05/2017.
 *
 */

public interface ServiceJobComplaintsCategoryListener {

    /**
     * Callback invoked when clicked
     * @param position - the position
     * @param serviceJobComplaint_mobileWrapper - the text to pass back
     * @param clickedItemValueAction
     * @param clickedItemValue
     * @param cmcf_id
     */
    void onHandleSelection(int position, ServiceJobComplaint_MobileWrapper serviceJobComplaint_mobileWrapper, String clickedItemValueAction, String clickedItemValue, String cmcf_id, int mode);

    void onHandleDeleteSelection(ServiceJobComplaint_MobileWrapper serviceJobComplaint_mobileWrapper, ServiceJobComplaintWrapper dataSet, int actionDeleteDetails);
}
