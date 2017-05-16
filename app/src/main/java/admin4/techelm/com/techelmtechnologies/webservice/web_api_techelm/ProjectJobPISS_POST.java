package admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm;

import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import admin4.techelm.com.techelmtechnologies.model.projectjob.b1.PISSWrapper;
import admin4.techelm.com.techelmtechnologies.webservice.WebServiceRequest;
import admin4.techelm.com.techelmtechnologies.webservice.command.PostCommand;
import admin4.techelm.com.techelmtechnologies.webservice.interfaces.OnServiceListener;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebServiceInfo;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.PROJECT_JOB_SAVE_PISS_TASK_FORM_URL;
import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_SAVE_REVERT_STATUS_URL;

/**
 * Created by admin 4 on 16/05/2017.
 * STRING DATA Only, use ServiceJobJSON_POST Class for JSON DATA POST
 * THis callback can be impelemented without implements on the class header,
 * POST command
 */

public class ProjectJobPISS_POST {

    private PostCommand postCommand;
    private static final String TAG = ProjectJobPISS_POST.class.getSimpleName();

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

    public void postPISSTaskForm(PISSWrapper pissWrapper) {
        this.mOnEventListener.onEvent();
    /*web info*/
        WebServiceInfo webServiceInfo = new WebServiceInfo();
        webServiceInfo.setUrl(PROJECT_JOB_SAVE_PISS_TASK_FORM_URL);

    /*add parameter*/
        webServiceInfo.addParam("projectjob_id", pissWrapper.getProjectJobID() + "");
        webServiceInfo.addParam("property_officer", pissWrapper.getPropertyOfficer());
        webServiceInfo.addParam("tc_lew", pissWrapper.getTCLew());
        webServiceInfo.addParam("property_officer_telNo", pissWrapper.getPropertyOfficerTelNo());
        webServiceInfo.addParam("tc_lew_telNo", pissWrapper.getTCLewTelNo());
        webServiceInfo.addParam("property_officer_mobileNo", pissWrapper.getPropertyOfficerMobileNo());
        webServiceInfo.addParam("tc_lew_mobileNo", pissWrapper.getTCLewMobileNo());
        webServiceInfo.addParam("property_officer_branch", pissWrapper.getPropertyOfficerBranch());
        webServiceInfo.addParam("tc_lew_email", pissWrapper.getTCLewEmail());
        webServiceInfo.addParam("remarks", pissWrapper.getRemarks()); // Can be nothing... this is not used in server side

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
