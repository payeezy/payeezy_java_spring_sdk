package com.firstdata.payeezy.samples;

import com.firstdata.payeezy.PayeezyAppConfig;
import com.firstdata.payeezy.PayeezyClientHelper;
import com.firstdata.payeezy.models.enrollment.EnrollmentApp;
import com.firstdata.payeezy.models.enrollment.EnrollmentRequest;
import com.firstdata.payeezy.models.transaction.PayeezyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Example to illustrate an authorization and reversal(void) using a credit card
 */

@SpringBootApplication
@Import(PayeezyAppConfig.class)
public class ACHPaySample implements CommandLineRunner{

    public static void main(String [] args){
        SpringApplication.run(ACHPaySample.class);
    }

    @Autowired
    private PayeezyClientHelper payeezyClientHelper;

    @Override
    public void run(String... strings) throws Exception {

        EnrollmentRequest enrollmentRequest = new EnrollmentRequest();
        enrollmentRequest.setFirstName("Squirtle");
        enrollmentRequest.setLastName("Pokemon");
        // set the address
        com.firstdata.payeezy.models.enrollment.Address address = new com.firstdata.payeezy.models.enrollment.Address();
        address.setAddressLine1("7979 Westheimer");
        address.setState("TX");
        address.setCity("Houston");
        address.setCountry("US");
        address.setEmail("jsmith@email.com");
        address.setZip("77063");
        enrollmentRequest.setAddress(address);
        //set the phone
        com.firstdata.payeezy.models.enrollment.Address.Phone phone = new com.firstdata.payeezy.models.enrollment.Address.Phone();
        phone.setType("MOBILE");
        phone.setNumber("9999955555");
        address.setPhone(phone);
        //set the Application
        EnrollmentApp enrollmentApp = new EnrollmentApp();
        enrollmentRequest.setEnrollmentApplication(enrollmentApp);
        enrollmentApp.setApplication("PayeezyACH");
        enrollmentApp.setDevice("DeviceXYZ123");
        enrollmentApp.setImei("IMEI76856745");
        enrollmentApp.setApplicationId("76ed6b08-224d-4f2e-9771-28cb5c9f26bd");
        enrollmentApp.setDeviceId("DeviceID65657");
        enrollmentApp.setIpAddress("192.168.1.1");
        enrollmentApp.setTrueIpAddress("192.168.1.1");
        enrollmentApp.setOrganizationId("FirtsDataInternalUAID9999");

        EnrollmentRequest.EnrollmentUser user = new EnrollmentRequest.EnrollmentUser();
        user.setRoutingNumber("311373125");
        user.setAccountNumber("728001010");
        enrollmentRequest.setEnrollmentUser(user);

        try{
            PayeezyResponse payeezyResponse = payeezyClientHelper.enrollInACH(enrollmentRequest);
            System.out.println("Status Code:"+payeezyResponse.getStatusCode());
            System.out.println("Response:"+payeezyResponse.getResponseBody());

            // Validate Micro Deposits
            // this will fail because the authentication answer will not match the account
         /* BAARequest baaRequest = new BAARequest();
            baaRequest.setEnrollmentId(enrollmentResponse.getEnrollmentId());
            baaRequest.setAuthenticationAnswer(11066);
            PayeezyResponse validationResponse = payeezyClientHelper.validateMicroDeposit(baaRequest);
            System.out.println("Status Code:"+validationResponse.getStatusCode());
            System.out.println("Response:"+validationResponse.getResponseBody());*/

            // update enrollment data data
            // the below example updates the address associated with the user
       /*     enrollmentRequest.setEnrollmentId(enrollmentResponse.getEnrollmentId());
            address.setAddressLine1("2000 Broadway Street");
            address.setState("CA");
            address.setCity("Redwood City");
            address.setZip("07789");

            PayeezyResponse updateResponse =  payeezyClientHelper.updateACHEnrollment(enrollmentRequest);
            System.out.println("Status Code:"+updateResponse.getStatusCode());
            System.out.println("Response:"+updateResponse.getResponseBody());

            // Close ACH Enrollment
            enrollmentRequest.setReason("Fraudulent Activity");
            PayeezyResponse closeResponse = payeezyClientHelper.closeACHEnrollment(enrollmentRequest);
            System.out.println("Status Code:"+closeResponse.getStatusCode());
            System.out.println("Response:"+closeResponse.getResponseBody());*/

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
