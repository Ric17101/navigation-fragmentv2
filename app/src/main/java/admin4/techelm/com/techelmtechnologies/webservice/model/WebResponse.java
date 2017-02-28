package admin4.techelm.com.techelmtechnologies.webservice.model;


import java.io.InputStream;

public class WebResponse {
    private String stringResponse;
    private InputStream inputResponse;
    private int responseCode;

    public int getResponseCode() {
        return responseCode;
    }
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getStringResponse() {
        return stringResponse;
    }
    public void setStringResponse(String stringResponse) { this.stringResponse = stringResponse; }

    /*public InputStream getInputResponse() {
        return inputResponse;
    }
    public void setInputResponse(InputStream stringResponse) { this.inputResponse = stringResponse; }*/

}
