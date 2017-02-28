package admin4.techelm.com.techelmtechnologies.login;

import android.util.Log;
import android.view.View;

import admin4.techelm.com.techelmtechnologies.webservice.WebServiceRequest;
import admin4.techelm.com.techelmtechnologies.webservice.command.GetCommand;
import admin4.techelm.com.techelmtechnologies.webservice.command.PostCommand;
import admin4.techelm.com.techelmtechnologies.webservice.interfaces.OnServiceListener;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebServiceInfo;

public class LoginWebservice {
    /*sample api credit to https://github.com/typicode/jsonplaceholder#how-to*/

    private PostCommand postCommand;
    private GetCommand getCommand;
    public static final String TAG = "LoginWebservice";

    public void cancel(View v) {
        postCommand.cancel();
    }

    public void postLogin(String email, String password) {
        /*web info*/
        WebServiceInfo webServiceInfo = new WebServiceInfo();
        String url = "http://jsonplaceholder.typicode.com/posts";
        webServiceInfo.setUrl(url);

        /*add parameter*/
        webServiceInfo.addParam("email", email);
        webServiceInfo.addParam("password", password);
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
                // textView23.setText(response.getStringResponse());
            }
        });
    }

    public void post(View v, String email, String password) {
        /*web info*/
        WebServiceInfo webServiceInfo = new WebServiceInfo();
        String url = "http://jsonplaceholder.typicode.com/posts";
        webServiceInfo.setUrl(url);

        /*add parameter*/
        webServiceInfo.addParam("email", email);
        webServiceInfo.addParam("password", password);
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
                //textView23.setText(response.getStringResponse());
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
                //textView23.setText(response.getStringResponse());
            }
        });
    }
}
