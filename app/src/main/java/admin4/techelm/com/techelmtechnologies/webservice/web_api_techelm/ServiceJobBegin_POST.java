package admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm;

import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import admin4.techelm.com.techelmtechnologies.webservice.WebServiceRequest;
import admin4.techelm.com.techelmtechnologies.webservice.command.PostCommand;
import admin4.techelm.com.techelmtechnologies.webservice.interfaces.OnServiceListener;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebServiceInfo;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_UPLOAD_URL;

/**
 * Created by admin 4 on 17/03/2017.
 * This call run on Working Thread Alraedy
 * POST commmand
 *
 */

public class ServiceJobBegin_POST {

    private PostCommand postCommand;
    public static final String TAG = "ServiceJobBegin_POST";

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

    public void postStartDate(int id) {
    /*web info*/
        WebServiceInfo webServiceInfo = new WebServiceInfo();
        String url = SERVICE_JOB_UPLOAD_URL + "save_start_date";
        webServiceInfo.setUrl(url);

    /*add parameter*/
        webServiceInfo.addParam("id", id + "");
        webServiceInfo.addParam("start_date_task", getCurrentDateTime()); // Can be nothing... this is not used in server side

    /*postStartDate command*/
        postCommand = new PostCommand(webServiceInfo);

    /*request*/
        executeWebServiceRequest();
    }

    public void postContinueDate(int id) {
    /*web info*/
        WebServiceInfo webServiceInfo = new WebServiceInfo();
        String url = SERVICE_JOB_UPLOAD_URL + "save_continue_start_date";
        webServiceInfo.setUrl(url);

    /*add parameter*/
        webServiceInfo.addParam("id", id + "");
        //webServiceInfo.addParam("start_task_time", getCurrentDateTime());

    /*postStartDate command*/
        postCommand = new PostCommand(webServiceInfo);

    /*request*/
        executeWebServiceRequest();
    }

    public void postGetListOfReplacementPartsDate() {
    /*web info*/
        WebServiceInfo webServiceInfo = new WebServiceInfo();
        String url = SERVICE_JOB_UPLOAD_URL + "get_part_replacement_rates";
        webServiceInfo.setUrl(url);

    /*add parameter*/
        webServiceInfo.addParam("id", "test");
        //webServiceInfo.addParam("start_task_time", getCurrentDateTime());

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
        String url = SERVICE_JOB_UPLOAD_URL + "save_revert_status";
        webServiceInfo.setUrl(url);

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
                    // mOnEventListener.onEvent();
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
