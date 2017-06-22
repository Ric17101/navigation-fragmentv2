package admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm;

import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import admin4.techelm.com.techelmtechnologies.model.projectjob.b2.IPI_TaskCarWrapper;
import admin4.techelm.com.techelmtechnologies.model.projectjob.b2.IPI_TaskWrapper;
import admin4.techelm.com.techelmtechnologies.utility.DateUtil;
import admin4.techelm.com.techelmtechnologies.webservice.WebServiceRequest;
import admin4.techelm.com.techelmtechnologies.webservice.command.PostCommand;
import admin4.techelm.com.techelmtechnologies.webservice.interfaces.OnServiceListener;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebServiceInfo;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_SAVE_IPI_TASK_FORM_A_URL;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_SAVE_IPI_TASK_FORM_B_URL;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_SAVE_REVERT_STATUS_URL;

/**
 * Created by admin 4 on 17/05/2017.
 * STRING DATA Only, use ServiceJobJSON_POST Class for JSON DATA POST
 * THis callback can be implemented without implements on the class header,
 * POST command
 */

public class ProjectJobIPI_POST {

    private PostCommand postCommand;
    private static final String TAG = ProjectJobIPI_POST.class.getSimpleName();

    private OnEventListener mOnEventListener;

    public void setOnEventListener(OnEventListener listener) {
        mOnEventListener = listener;
    }

    public interface OnEventListener {
        void onEvent(); // May be send error or ok message
        void onError(String message);
        void onEventResult(WebResponse response);
    }

    // WEB API Setup
    public void cancel(View v) { postCommand.cancel(); }

    /**
     * B2 and B3 Form A - Confirmation Date Form (Modal)
     * @param ipiTaskWrapper - DAta will be sending to the server
     */
    public void postIPITaskFormA(IPI_TaskWrapper ipiTaskWrapper) {
        this.mOnEventListener.onEvent();
    /*web info*/
        WebServiceInfo webServiceInfo = new WebServiceInfo();
        webServiceInfo.setUrl(PROJECT_JOB_SAVE_IPI_TASK_FORM_A_URL);

    /*add parameter*/
        webServiceInfo.addParam("projectjob_task_id", ipiTaskWrapper.getID() + "");
        webServiceInfo.addParam("projectjob_id", ipiTaskWrapper.getProjectJob_ID() + "");
        webServiceInfo.addParam("comment", ipiTaskWrapper.getStatusComment());
        webServiceInfo.addParam("issue_CAR", ipiTaskWrapper.getToIssueCar());

        switch (ipiTaskWrapper.getStatusComment()) { // Sets the form for IPI Form A, then insert to IPI_TASKCar if yes
            case "YES" :
                ipiTaskWrapper.setNonConformance("");
                ipiTaskWrapper.setCorrectiveActions("");
                ipiTaskWrapper.setTargetCompletionDate("");
                break;
            default : break;
        }
        webServiceInfo.addParam("nonconformance", ipiTaskWrapper.getNonConformance());
        webServiceInfo.addParam("description", ipiTaskWrapper.getCorrectiveActions());
        webServiceInfo.addParam("completion_date", DateUtil.formatDate(ipiTaskWrapper.getTargetCompletionDate()));
        // webServiceInfo.addParam("target_remedy_date", DateUtil.formatDate(ipiTaskWrapper.getD()));

        webServiceInfo.addParam("form_type", ipiTaskWrapper.getFormType());

    /*postStartDate command*/
        postCommand = new PostCommand(webServiceInfo);

    /*request*/
        executeWebServiceRequest();
    }

    /**
     * B2 and B3 Form B - Corrective Action (Modal)
     * @param ipiTaskFinalWrapper - DAta will be sending to the server
     */
    public void postIPITaskFormB(IPI_TaskCarWrapper ipiTaskFinalWrapper) {
        this.mOnEventListener.onEvent();
    /*web info*/
        WebServiceInfo webServiceInfo = new WebServiceInfo();
        webServiceInfo.setUrl(PROJECT_JOB_SAVE_IPI_TASK_FORM_B_URL);

    /*add parameter*/
        webServiceInfo.addParam("projectjob_correctiveActions_id", ipiTaskFinalWrapper.getID() + "");
        webServiceInfo.addParam("description", ipiTaskFinalWrapper.getDescription());
        webServiceInfo.addParam("target_date", DateUtil.formatDate(ipiTaskFinalWrapper.getTargetRemedyDate()));
        webServiceInfo.addParam("completion_date", DateUtil.formatDate(ipiTaskFinalWrapper.getCompletionDate()));
        webServiceInfo.addParam("remarks", ipiTaskFinalWrapper.getRemarks());
        webServiceInfo.addParam("disposition", ipiTaskFinalWrapper.getDisposition());
        webServiceInfo.addParam("form_type", ipiTaskFinalWrapper.getFormType());

    /*postStartDate command*/
        postCommand = new PostCommand(webServiceInfo);

    /*request*/
        executeWebServiceRequest();
    }

    /**
     * On Event when:
     *  - onBackPress
     *  - onDestroy
     *  - on Button Back
     * @param id
     * @param status
     */
    public void postRevertStatus(int id, String status) {
    /*web info*/
        WebServiceInfo webServiceInfo = new WebServiceInfo();
        webServiceInfo.setUrl(SERVICE_JOB_SAVE_REVERT_STATUS_URL);

    /*add parameter*/
        webServiceInfo.addParam("id", id + "");
        webServiceInfo.addParam("status", status);

    /*postStartDate command*/
        postCommand = new PostCommand(webServiceInfo);

    /*request*/
        executeWebServiceRequest();
    }

    // TODO: parse reponse
    private void executeWebServiceRequest() {
        WebServiceRequest webServiceRequest = new WebServiceRequest(postCommand);
        webServiceRequest.execute();
        webServiceRequest.setOnServiceListener(new OnServiceListener() {
            @Override
            public void onServiceCallback(WebResponse response) {
                if (mOnEventListener != null) {
                    Log.e(TAG,  response.getStringResponse());
                    mOnEventListener.onEventResult(response);
                } else {
                    mOnEventListener.onError("Error. Try again later.");
                }
            }
        });
    }

    private String getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        System.out.println(TAG + ": Current time => "+c.getTime() + "\n" + formattedDate);
        return formattedDate;
    }
}
