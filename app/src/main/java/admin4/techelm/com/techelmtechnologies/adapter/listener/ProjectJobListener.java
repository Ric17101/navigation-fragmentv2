package admin4.techelm.com.techelmtechnologies.adapter.listener;

import admin4.techelm.com.techelmtechnologies.model.projectjob.ProjectJobWrapper;

/**
 * Created by admin 4 on 03/05/2017.
 */

public interface ProjectJobListener {

    /**
     * Callback invoked when clicked
     *
     * @param position   - the position
     * @param projectWrapper - the text to pass back
     * @param action - Action to be done
     */
    void onHandleSelection(int position, ProjectJobWrapper projectWrapper, int action);
}
