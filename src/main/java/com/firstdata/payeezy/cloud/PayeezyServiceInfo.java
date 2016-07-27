package com.firstdata.payeezy.cloud;

import org.springframework.cloud.service.BaseServiceInfo;

import java.util.Map;

public class PayeezyServiceInfo extends BaseServiceInfo {

	public static final String PAYEEZY_SCHEME = "payeezy";
	
	private String apikey;
	private String secret;
	private String uri;
	private String token;
	private String jsSecurityKey;
	
	
	public PayeezyServiceInfo(String id) {
		super(id);
	}
	

	public PayeezyServiceInfo(String id, Map<String, Object> creds) {
		
		super(id);
		
		this.apikey = (String)creds.get("apikey");
		this.secret = (String)creds.get("secret");
		this.uri = (String)creds.get("uri");
		this.token = (String)creds.get("token");
		this.token = (String)creds.get("jsSecurityKey");
		
	}


	public String getApikey() {
		return apikey;
	}


	public void setApikey(String apikey) {
		this.apikey = apikey;
	}


	public String getSecret() {
		return secret;
	}


	public void setSecret(String secret) {
		this.secret = secret;
	}


	public String getUri() {
		return uri;
	}


	public void setUri(String uri) {
		this.uri = uri;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}
	
	
	
	
	

}
