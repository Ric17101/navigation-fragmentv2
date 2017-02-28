package admin4.techelm.com.techelmtechnologies.webservice.command;


import java.io.IOException;
import java.net.HttpURLConnection;

import admin4.techelm.com.techelmtechnologies.webservice.UtilNetConnection;
import admin4.techelm.com.techelmtechnologies.webservice.interfaces.WebCommand;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;
import admin4.techelm.com.techelmtechnologies.webservice.model.WebServiceInfo;

public class GetCommand implements WebCommand {
	private WebServiceInfo webInfo;
	private HttpURLConnection con;
	public GetCommand(WebServiceInfo webInfo) {
		this.webInfo=webInfo;
	}
	@Override
	public WebResponse execute() {
		WebResponse response = new WebResponse();
		try {
			con = UtilNetConnection.buildConnection(webInfo.getUrl());
			/*get data*/
			int responseCode = con.getResponseCode();
			String result = UtilNetConnection.inputStreamToString(con.getInputStream());
			/*set data*/
			response.setResponseCode(responseCode);
			response.setStringResponse(result); // response.setInputResponse(con.getInputStream()); // For Login only
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public void cancel() {
		if(con!=null)con.disconnect();
	}

	@Override
	public void setProgessListener() {
		// TODO Auto-generated method stub
		
	}


}
