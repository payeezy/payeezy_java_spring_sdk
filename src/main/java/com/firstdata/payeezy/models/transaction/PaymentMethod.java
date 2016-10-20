package com.firstdata.payeezy.models.transaction;


public enum PaymentMethod {
    CREDIT_CARD("credit_card"),TOKEN("token"),VALUELINK("valuelink"),THREEDS("3ds"),TELE_CHECK("tele_check"),PAYPAL("paypal"), CONNECT_PAY("connect_pay");

    private String value;

    PaymentMethod(String value){
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }
}
