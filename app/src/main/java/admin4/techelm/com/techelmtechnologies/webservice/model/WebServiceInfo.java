package admin4.techelm.com.techelmtechnologies.webservice.model;


import java.util.HashMap;

public class WebServiceInfo {
    private String url;

    public HashMap<String, String> getParam() {
        return param;
    }

    private HashMap<String, String> param;
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public WebServiceInfo addParam(String name, String value) {
        if(param == null) param = new HashMap<>();
        param.put(name,value);
        return this;
    }
//	private String mUrl;
//	private File mUploadFile;
//	private List<NameValuePair> mParam;
//	private String mMultipartName = "file";
//	private String stringEntity;
//
//	public WebServiceInfo(String url) {
//		mUrl 	= url;
//	}
//	
//	public void setUrl(String url) {
//		mUrl = url;
//	}
//	
//	public String getUrl() {
//		return mUrl;
//	}
//	
//	public WebServiceInfo addParam(String name, String value) {
//		
//		if(mParam == null) mParam = new ArrayList<NameValuePair>();
//		mParam.add(new BasicNameValuePair( name, value));
//		
//		return this;
//	}
//	
//	public WebServiceInfo addParam(String value) {
//		
//		if(mParam == null) mParam = new ArrayList<NameValuePair>();
//		mParam.add(new BasicNameValuePair("", value));
//		
//		return this;
//	}
//	
//	public List<NameValuePair> getParam() {
//		return mParam;
//	}
//	
//	public void setUploadFile(File file) {
//		mUploadFile = file;
//	}
//	
//	public File getUploadFile() {
//		return mUploadFile;
//	}
//
//	public void setMultipartName(String fileName) {
//		mMultipartName = fileName;
//	}
//	
//	public String getMultipartName() {
//		return mMultipartName ;
//	}
//
//	/**
//	 * @return the stringEntity
//	 */
//	public String getStringEntity() {
//		return stringEntity;
//	}
//
//	/**
//	 * @param stringEntity the stringEntity to set
//	 */
//	public void setStringEntity(String stringEntity) {
//		this.stringEntity = stringEntity;
//	}
//
//	
//	public HttpEntity getEntity() throws UnsupportedEncodingException{
//		if(stringEntity != null && stringEntity.length() > 0){
//			return new StringEntity(stringEntity, HTTP.UTF_8);
//		}else{
//			return new UrlEncodedFormEntity(mParam);
//		}
//			
//	}
}
