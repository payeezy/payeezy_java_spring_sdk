package com.firstdata.payeezy.client;

import com.firstdata.payeezy.models.exception.ApplicationRuntimeException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Use this call to pass apikey, token and secret required for making a transaction
 */
public class PayeezyRequestOptions {

    private String apiKey;
    private String token;
    private String secret;
    private String proxyHost;
    private String proxyPort;

    public PayeezyRequestOptions(String apiKey, String token, String securedSecret, String proxyHost, String proxyPort){
        this.apiKey = apiKey;
        this.token = token;
        this.secret = securedSecret;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        if(this.apiKey == null || this.token == null){
            throw new ApplicationRuntimeException("Invalid Request. Missing Credentials.");
        }
    }

    public PayeezyRequestOptions(String apiKey, String token, String securedSecret){
        this(apiKey, token, securedSecret,null, null);
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getToken() {
        return token;
    }

    public String getSecret() {
        return secret;
    }

    public Map<String,String> initialize(String paylaod) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException{
        return getSecurityKeys(paylaod);
    }

    protected Map<String,String> getSecurityKeys(String payLoad) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException{
        Map<String,String> returnMap=new HashMap<>();
            long nonce = Math.abs(SecureRandom.getInstance("SHA1PRNG").nextLong());
            returnMap.put(APIResourceConstants.SecurityConstants.NONCE, Long.toString(nonce));
            returnMap.put(APIResourceConstants.SecurityConstants.APIKEY, this.getApiKey());
            returnMap.put(APIResourceConstants.SecurityConstants.TIMESTAMP, Long.toString(System.currentTimeMillis()));
            returnMap.put(APIResourceConstants.SecurityConstants.TOKEN, this.getToken());
            returnMap.put(APIResourceConstants.SecurityConstants.APISECRET, this.getSecret());
            returnMap.put(APIResourceConstants.SecurityConstants.PAYLOAD, payLoad);
            returnMap.put(APIResourceConstants.SecurityConstants.AUTHORIZE, getMacValue(returnMap));
            return returnMap;
    }

    /**
     * Builds the authorization string that will sent as part of the Request Header
     * @param data
     * @return
     * @throws Exception
     */
    private String getMacValue(Map<String,String> data) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException{
        Mac mac=Mac.getInstance("HmacSHA256");
        String apiSecret= data.get(APIResourceConstants.SecurityConstants.APISECRET);
        SecretKeySpec secret_key = new SecretKeySpec(apiSecret.getBytes(), "HmacSHA256");
        mac.init(secret_key);
        String apikey = data.get(APIResourceConstants.SecurityConstants.APIKEY);
        String nonce = data.get(APIResourceConstants.SecurityConstants.NONCE);
        String timeStamp = data.get(APIResourceConstants.SecurityConstants.TIMESTAMP);
        String token = data.get(APIResourceConstants.SecurityConstants.TOKEN);
        String payload = data.get(APIResourceConstants.SecurityConstants.PAYLOAD);

        StringBuilder buff=new StringBuilder();
        buff.append(apikey)
                .append(nonce)
                .append(timeStamp);
        if(token!=null){
            buff.append(token);
        }
        if(payload != null){
            buff.append(payload);
        }
        String bufferData = buff.toString();
        //MessageLogger.logMessage(String.format(bufferData));
        byte[] macHash=mac.doFinal(bufferData.getBytes("UTF-8"));
        //MessageLogger.logMessage(Integer.toString(macHash.length));
        //MessageLogger.logMessage(String.format("MacHAsh:{}" , macHash));
        String authorizeString = new String(Base64.encodeBase64(toHex(macHash)));
        //   MessageLogger.logMessage(String.format("Authorize:{}" , authorizeString));
        return authorizeString;
    }

    /**
     * Converts the bytes to Hex bytes
     * @param arr
     * @return
     */
    private byte[] toHex(byte[] arr) {
        String hex= byteArrayToHex(arr);
        return hex.getBytes();
    }

    private String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("API KEY --> "+this.getApiKey()).append("Token --> "+this.getToken())
                .append("Secret --> "+this.getSecret());
        return stringBuffer.toString();
    }
}
