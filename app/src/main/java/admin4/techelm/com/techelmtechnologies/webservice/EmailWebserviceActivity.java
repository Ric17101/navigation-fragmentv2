package admin4.techelm.com.techelmtechnologies.webservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.webservice.command.GetCommand;
import admin4.techelm.com.techelmtechnologies.webservice.command.PostCommand;
import admin4.techelm.com.techelmtechnologies.webservice.interfaces.OnServiceListener;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebServiceInfo;

import static admin4.techelm.com.techelmtechnologies.utility.Constants.SERVICE_JOB_UPLOAD_URL;

public class EmailWebserviceActivity extends AppCompatActivity {
    /*sample api credit to https://github.com/typicode/jsonplaceholder#how-to*/

    private PostCommand postCommand;
    private GetCommand getCommand;
    public static final String TAG = EmailWebserviceActivity.class.getSimpleName();

    TextView textView23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        textView23 = (TextView) findViewById(R.id.textView23);
    }

    public void cancel(View v) {
        postCommand.cancel();
    }

    public void post(View v) {
        /*web info*/
        WebServiceInfo webServiceInfo = new WebServiceInfo();
        String url = SERVICE_JOB_UPLOAD_URL + "send_email";
        webServiceInfo.setUrl(url);
        /*add parameter*/
        webServiceInfo.addParam("title", "sample title");
        webServiceInfo.addParam("body", "sample body");
        webServiceInfo.addParam("userId", "2");
        /*post command*/
        postCommand = new PostCommand(webServiceInfo);
        /*request*/
        WebServiceRequest webServiceRequest = new WebServiceRequest(postCommand);
        webServiceRequest.execute();
        webServiceRequest.setOnServiceListener(new OnServiceListener() {
            @Override
            public void onServiceCallback(WebResponse response) {
                Log.e(TAG, "WebResponse: " + response.getStringResponse());
                textView23.setText(response.getStringResponse());
            }
        });
    }

    public void get(View v) {
         /*web info*/
        String url = "http://jsonplaceholder.typicode.com/posts/1";
        WebServiceInfo webServiceInfo = new WebServiceInfo();
        webServiceInfo.setUrl(url);
        /*get command*/
        getCommand = new GetCommand(webServiceInfo);
        /*request*/
        WebServiceRequest webServiceRequest = new WebServiceRequest(getCommand);
        webServiceRequest.execute();
        webServiceRequest.setOnServiceListener(new OnServiceListener() {
            @Override
            public void onServiceCallback(WebResponse response) {
                Log.e(TAG, "WebResponse: " + response.getStringResponse());
                textView23.setText(response.getStringResponse());
            }
        });
    }
}
