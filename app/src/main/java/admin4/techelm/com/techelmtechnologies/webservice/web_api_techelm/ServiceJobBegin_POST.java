package admin4.techelm.com.techelmtechnologies.webservice.web_api_techelm;

import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import admin4.techelm.com.techelmtechnologies.webservice.WebServiceRequest;
import admin4.techelm.com.techelmtechnologies.webservice.command.PostCommand;
import admin4.techelm.com.techelmtechnologies.webservice.interfaces.OnServiceListener;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebServiceInfo;

/**
 * Created by admin 4 on 17/03/2017.
 * This call run on Working Thread Alraedy
 * POST commmand
 *
 */

public class ServiceJobBegin_POST {

    private static final String SERVICE_JOB_URL =
            "http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/index.php/servicejob/";
    private PostCommand postCommand;
    public static final String TAG = "ServiceJobBegin_POST";

    private OnEventListener mOnEventListener;

    public void setOnEventListener(OnEventListener listener) {
        mOnEventListener = listener;
    }

    public interface OnEventListener {
        void onEvent();
        void onEventResult(WebResponse response);
    }

    // WEB API Setup
    public void cancel(View v) { postCommand.cancel(); }

    public void postStartDate(final int id) {
    /*web info*/
        WebServiceInfo webServiceInfo = new WebServiceInfo();
        String url = SERVICE_JOB_URL + "save_start_date";
        webServiceInfo.setUrl(url);

    /*add parameter*/
        webServiceInfo.addParam("id", id + "");
        webServiceInfo.addParam("start_date_task", getCurrentDateTime());

    /*postStartDate command*/
        postCommand = new PostCommand(webServiceInfo);

    /*request*/
        WebServiceRequest webServiceRequest = new WebServiceRequest(postCommand);
        webServiceRequest.execute();
        webServiceRequest.setOnServiceListener(new OnServiceListener() {
            @Override
            public void onServiceCallback(WebResponse response) {
                if (mOnEventListener != null) {
                    // mOnEventListener.onEvent();
                    mOnEventListener.onEventResult(response);
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
