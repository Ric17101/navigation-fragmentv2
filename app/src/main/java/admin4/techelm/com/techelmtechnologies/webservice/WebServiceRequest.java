package admin4.techelm.com.techelmtechnologies.webservice;


import admin4.techelm.com.techelmtechnologies.webservice.interfaces.OnServiceListener;
import admin4.techelm.com.techelmtechnologies.webservice.interfaces.WebCommand;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;

public class WebServiceRequest extends NetworkTask<Void, Void, WebResponse> {
    private WebCommand command;

    public WebServiceRequest(WebCommand command) {
        this.command = command;
    }

    @Override
    public WebResponse doNetworkTask() {
        return command.execute();
    }

    @Override
    public void onPostSuccess(WebResponse webResponse) {
        if (onServiceListener != null) {
            onServiceListener.onServiceCallback(webResponse);
        }
    }

    @Override
    public void setOnServiceListener(OnServiceListener onServiceListener) {
        this.onServiceListener = onServiceListener;
    }
}
