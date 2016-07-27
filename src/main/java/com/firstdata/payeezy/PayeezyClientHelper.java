package com.firstdata.payeezy;

import com.firstdata.payeezy.client.PayeezyClient;
import com.firstdata.payeezy.client.PayeezyRequestOptions;
import com.firstdata.payeezy.models.exception.ApplicationRuntimeException;
import com.firstdata.payeezy.models.transaction.PayeezyResponse;
import com.firstdata.payeezy.models.transaction.TransactionRequest;
import com.firstdata.payeezy.models.transaction.TransactionResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class that provides convenient methods to execute primary, secondary, getToken and dynamic pricing API's
 * the URL should always point to the host.
 * when making the GetToken call do not pass the protocol as the helper sets up the protocol
 */
@Component
@PropertySource("classpath:application.properties")
public class PayeezyClientHelper {

    private PayeezyClient payeezyClient;

    private JSONHelper jsonHelper = new JSONHelper();

    @Autowired
    private Environment env;

    @PostConstruct
    private void init(){
    	String vcapJson = env.getProperty("VCAP_SERVICES"); 
    	System.out.println("VCAP JSON is: " + vcapJson);
    	
        if(vcapJson != null) {
            String[] serviceNames = new String[]{"firstdata-service", "payeezy-service"};
        	Map<String, String> map = getMapFromVCAP(vcapJson, serviceNames, 0);
        	if (map != null && !map.isEmpty()) {
        		PayeezyRequestOptions requestOptions = new PayeezyRequestOptions(map.get("apikey"),map.get("token"), map.get("secret"));
				String url= map.get("url");
	            payeezyClient = new PayeezyClient(requestOptions, url);
        	} else {
        		throw new ApplicationRuntimeException("Can't find Payeezy service information in VCAP_SERVICES");
        	}
        } else {
            if(env == null){
                throw  new ApplicationRuntimeException("Application properties not found");
            }
        	String key = env.getProperty("apikey");
        	String secret = env.getProperty("pzsecret");
        	String token = env.getProperty("token");
        	String url = env.getProperty("url");
        	String proxyHost = env.getProperty("proxyHost");
        	String proxyPort = env.getProperty("proxyPort");
        	payeezyClient = new PayeezyClient(new PayeezyRequestOptions(key, token, secret, proxyHost, proxyPort), url);
        }
    }

    /**
     * Use this for primary transactions like Authorize, Purchase
     * @param transactionRequest
     * @return
     * @throws Exception
     */
    public PayeezyResponse doPrimaryTransaction(TransactionRequest transactionRequest) throws Exception {
        ResponseEntity<TransactionResponse> responseEntity = payeezyClient.post(transactionRequest);
        return new PayeezyResponse(responseEntity.getStatusCode().value(), jsonHelper.getJSONObject(responseEntity.getBody()));
    }

    /**
     *Use this for Secondary transactions like void, refund, capture etc
     */

    public PayeezyResponse doSecondaryTransaction(String id, TransactionRequest transactionRequest) throws Exception {
        ResponseEntity<TransactionResponse> responseEntity = payeezyClient.post(transactionRequest, id);
        return new PayeezyResponse(responseEntity.getStatusCode().value(), jsonHelper.getJSONObject(responseEntity.getBody()));
    }

    /**
     * retrieve app specific values from VCAP_SERVICES
     * @param vcapJson
     * @param serviceNames
     * @param index
     * @return
     */
    private HashMap<String, String> getMapFromVCAP(String vcapJson, String[] serviceNames, int index) {
        JsonElement contents = null;
        JsonElement credentials;
        String service = null;
        for(String serviceName : serviceNames) {
            contents = new JsonParser().parse(vcapJson)
                    .getAsJsonObject().get(serviceName);
            service= serviceName;
            if (contents != null) {
                break;
            }
        }

        if (contents == null) return null;

        if (contents.isJsonArray()) {
        	   JsonArray arr = contents.getAsJsonArray();
        	   if (index > (arr.size() -1)) {
        		   System.err.println("Out of bounds: " + index);
        		   return null;
        	   }
               credentials = arr.get(index).getAsJsonObject()
                              .get("credentials");
       } else {
               credentials = contents.getAsJsonObject().get("credentials");
       }
        if (credentials == null) {
        	System.err.println("Unable to retrieve credentials");
        	return null;
        }
        String uri = credentials.getAsJsonObject().get("uri").getAsString();
        String token = credentials.getAsJsonObject().get("token").getAsString();
        String secret = credentials.getAsJsonObject().get("secret").getAsString();
        String apikey = credentials.getAsJsonObject().get("apikey").getAsString();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("url", uri);
        map.put("token", token);
        map.put("secret", secret);
        map.put("apikey", apikey);

        System.out.println("Info from VCAP_SERVICES for service name: " + service + " is: " + uri + " " + map.toString());
        return map;
	}

}
