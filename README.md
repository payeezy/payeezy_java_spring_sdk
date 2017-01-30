# payeezy_java_spring
Payeezy Java Spring SDK

Payeezy Java SDK using Spring Boot is built to make developers life easy to integrate with the Payeezy API (https://developers.payeezy.com) for processing payements with various payment methods. Download the SDK, install it and start testing against the sandbox environment with developer credentials.

1) Add the following dependency for Maven in pom.xml

	```
		<dependency>
			<groupId>com.firstdata.payeezy</groupId>
			<artifactId>payeezy-client-spring</artifactId>
			<version>1.0</version>
		</dependency>
	```

2) Create your application.properties file in resources folder with the following information

    apikey=y6pWAJNyJyjGv66IsVuWnklkKUPFbb0a
    pzsecret=86fbae7030253af3cd15faef2a1f4b67353e41fb6799f576b5093ae52901e6f7
    token=fdoa-a480ce8951daa73262734cf102641994c1e55e7cdf4c02b6
    url=https://api-cert.payeezy.com/v1/transactions

    If you are behind a proxy, add the below properties in application propertes
      * proxyHost=Your proxy information goes here
      * proxyPort=Your proxy port information goes here
      * Proxy Port value will default to 80, if its not provided
	  
3) To Use PayeezyClientHelper class for processing transactions In your spring boot application, import the PayeezyAppConfig.class

    * @Import(PayeezyAppConfig.class)

    * Autowire PayeezyClientHelper.java. You are ready to use Payeezy API for transaction processing.

PayeezyClientHelper provides different methods to integrate with different transactions seemlessly. The SDK is supplied with Domain classes to build your transaction requests easily.

Please refer to PayeezyClientHelper java documentation to understand the various methods available.

Primary Transactions
-------------------------
1) Authorize
2) Purchase
3) Authorize_score
4) Score_only (Applicable to all payment methods)
5) Purchase_Score (Valid only for Value link)

Secondary Transactions
-------------------------
Secondary transactions like void, refund can be performed once a primary transaction is completed in case if you need to reverse/refund a transaction.

Pay With By Bank
-----------------
1) Enroll in ACH
2) Validate
3) UnEnroll
4) Update Enrollment


Please refer to documentaiton on https://developer.payeezy.com to understand the various types transactions and how to effectively use them for transaction processing.
## Contributing

1. Fork it 
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create a new Pull Request  


## Feedback
We appreciate the time you take to try out our sample code and welcome your feedback. Here are a few ways to get in touch:
* For generally applicable issues and feedback, create an issue in this repository.
* support@payeezy.com - for personal support at any phase of integration
* [1.855.799.0790](tel:+18557990790)  - for personal support in real time 

## Terms of Use
Terms and conditions for using Payeezy Direct API SDK: Please see [Payeezy Terms & conditions](https://developer.payeezy.com/terms-use)
 
### License
The Payeezy Java Spring SDK is open source and available under the MIT license. See the LICENSE file for more info.

