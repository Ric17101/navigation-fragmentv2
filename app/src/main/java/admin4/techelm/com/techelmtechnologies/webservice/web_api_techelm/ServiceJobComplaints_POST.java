package admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm;

import android.util.Log;
import android.view.View;

import admin4.techelm.com.techelmtechnologies.webservice.WebServiceRequest;
import admin4.techelm.com.techelmtechnologies.webservice.command.PostCommand;
import admin4.techelm.com.techelmtechnologies.webservice.interfaces.OnServiceListener;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebServiceInfo;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_POST_ALL_COMPLAINTS_URL;

/**
 * Created by admin 4 on 17/03/2017.
 * This call run on Working Thread Alraedy
 * POST commmand
 *
 */

public class ServiceJobComplaints_POST {

    private PostCommand postCommand;
    public static final String TAG = ServiceJobComplaints_POST.class.getSimpleName();

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

    public void postComplaintTask(int sjid) {
    /*web info*/
        WebServiceInfo webServiceInfo = new WebServiceInfo();
        webServiceInfo.setUrl(SERVICE_JOB_POST_ALL_COMPLAINTS_URL);

    /*add parameter*/
        webServiceInfo.addParam("sjid", sjid + "");

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

}
