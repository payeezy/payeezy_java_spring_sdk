package com.firstdata.payeezy;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class PayeezyClientApplication {

  public static void main(String [] args){
        SpringApplication.run(PayeezyClientApplication.class, args);
    }

}


