package com.firstdata.payeezy.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class PayeezyRequestInterceptor implements ClientHttpRequestInterceptor {

    private final PayeezyRequestOptions requestOptions;
    private static Charset UTF_8 = Charset.forName("UTF-8");

    public PayeezyRequestInterceptor(PayeezyRequestOptions requestOptions) {
        this.requestOptions = requestOptions;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add(HttpHeaders.USER_AGENT, System.getProperty("java.version"));

        String payload = new String(body, UTF_8);
        try{
            Map<String, String> headerValues = this.requestOptions.initialize(payload);
            for (Map.Entry<String, String> entry : headerValues.entrySet()) {
                headers.add(entry.getKey(), entry.getValue());
            }

            request.getHeaders().putAll(headers);
        }catch (Exception ex){
            //
        }
        return execution.execute(request, body);
    }
}
