package admin4.techelm.com.techelmtechnologies.webservice.interfaces;


import admin4.techelm.com.techelmtechnologies.webservice.model.WebResponse;

public interface WebCommand {
	 WebResponse execute();
	 void cancel();
	 void setProgessListener();
//	public void setProgessListener(OnServiceListener listener);
}
