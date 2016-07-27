package com.firstdata.payeezy.client;


import com.firstdata.payeezy.models.exception.ApplicationRuntimeException;
import com.firstdata.payeezy.models.transaction.TransactionRequest;
import com.firstdata.payeezy.models.transaction.TransactionResponse;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PayeezyClient {

	private static Logger logger = Logger.getLogger(PayeezyClient.class);

	private static Charset UTF_8 = Charset.forName("UTF-8");

	private final RestTemplate restTemplate = new RestTemplate();

	private String transactionsUrl;
	
	private String secondaryTransactionUrl;

	private PayeezyClient(){}

	public PayeezyClient(PayeezyRequestOptions requestOptions, String transactionsUrl) {
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new PayeezyRequestInterceptor(requestOptions));
		if(transactionsUrl == null || "".equals(transactionsUrl.trim())){
			throw new ApplicationRuntimeException("URL is not configured.");
		}
		this.restTemplate.setInterceptors(interceptors);
		this.transactionsUrl = transactionsUrl;
		String url;
		if (this.transactionsUrl.endsWith(APIResourceConstants.PRIMARY_TRANSACTIONS)) {
			url = transactionsUrl;
		} else {
			url = transactionsUrl + APIResourceConstants.PRIMARY_TRANSACTIONS;
		}
		
		logger.info("Transaction URL: " + url);
		//logger.info("Secondary transaction URL: " + this.secondaryTransactionUrl);
		this.secondaryTransactionUrl = url + "/{id}";
		this.transactionsUrl = url;

		// set up a proxy
		String proxyHost = requestOptions.getProxyHost();
		if(proxyHost != null && !"".equals(proxyHost.trim())){
			//default to 80 if none provided
			String proxyPort = requestOptions.getProxyPort() != null && !"".equals(requestOptions.getProxyPort().trim()) ? requestOptions.getProxyPort() : "80";
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			Proxy proxy= new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, Integer.valueOf(proxyPort)));
			requestFactory.setProxy(proxy);
			restTemplate.setRequestFactory(requestFactory);
		}
	}

	public ResponseEntity<TransactionResponse> post(TransactionRequest request) {
		return this.restTemplate.postForEntity(this.transactionsUrl, request, TransactionResponse.class);
	}

	public ResponseEntity<TransactionResponse> post(TransactionRequest request, String id){
		//logger.info("Secondary Transaction: {} {} ", this.secondaryTransactionUrl, jsonHelper.getJSONObject(request) );
		return this.restTemplate.postForEntity(this.secondaryTransactionUrl, request,TransactionResponse.class, id);
	}

	public ResponseEntity<String> get(String URL, Map<String, String> queryMap) {
		return this.restTemplate.getForEntity(URL, String.class, queryMap);
	}
	
	

	
	
	

}
