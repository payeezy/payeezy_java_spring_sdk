package com.firstdata.payeezy.samples;

import com.firstdata.payeezy.JSONHelper;
import com.firstdata.payeezy.PayeezyAppConfig;
import com.firstdata.payeezy.PayeezyClientHelper;
import com.firstdata.payeezy.models.transaction.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Example to illustrate an authorization and reversal(void) using ach
 */

@SpringBootApplication
@Import(PayeezyAppConfig.class)
public class ACHTransactionSample implements CommandLineRunner{

    public static void main(String [] args){
        SpringApplication.run(ACHTransactionSample.class);
    }

    @Autowired
    private PayeezyClientHelper payeezyClientHelper;

    @Autowired
    JSONHelper helper;

    @Override
    public void run(String... strings) throws Exception {

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount("100"); // always set the amouunt in cents
        transactionRequest.setTransactionType(TransactionType.AUTHORIZE.name().toLowerCase());
        transactionRequest.setPaymentMethod(PaymentMethod.ACH.getValue());
        transactionRequest.setCurrency("USD");
        // set the credit card info
        Ach ach = new Ach();
        ach.setToken("1639635000001693");
        transactionRequest.setAch(ach);
        try{
            System.out.println(helper.getJSONObject(transactionRequest));
            PayeezyResponse payeezyResponse = payeezyClientHelper.doPrimaryTransaction(transactionRequest);
            System.out.println("Status Code:"+payeezyResponse.getStatusCode());
            System.out.println("Response:"+payeezyResponse.getResponseBody());

            JSONHelper jsonHelper = new JSONHelper();
            TransactionResponse transactionResponse = jsonHelper.fromJson(payeezyResponse.getResponseBody(), TransactionResponse.class);
            // the responses for connect pay will not have any transaction_tag
            // ACH Capture requires only transaction id
            TransactionRequest captureRequest = new TransactionRequest();
            captureRequest.setTransactionType(TransactionType.CAPTURE.name().toLowerCase());
            captureRequest.setPaymentMethod(PaymentMethod.ACH.getValue());
            captureRequest.setAmount("100"); // should always match the request amount
            captureRequest.setCurrency("USD");
            captureRequest.setAch(ach);

            PayeezyResponse voidPayeezyResponse = payeezyClientHelper.doSecondaryTransaction(transactionResponse.getTransactionId(), captureRequest);
            System.out.println(voidPayeezyResponse.getResponseBody());
        }catch (Exception e){
            e.printStackTrace();
        }
        System.exit(0);
    }
 }

