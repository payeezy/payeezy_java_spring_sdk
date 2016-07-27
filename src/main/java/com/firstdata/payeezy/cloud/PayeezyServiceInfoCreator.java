package com.firstdata.payeezy.cloud;

import java.util.Map;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

public class PayeezyServiceInfoCreator extends CloudFoundryServiceInfoCreator<PayeezyServiceInfo> {

	public PayeezyServiceInfoCreator() {
		// the literal in the tag is CloudFoundry-specific
		super(new Tags("payeezy"), PayeezyServiceInfo.PAYEEZY_SCHEME);
	}
	
	
	public PayeezyServiceInfoCreator(Tags tags, String[] uriSchemes) {
		super(tags, uriSchemes);
	}

	@Override
	public PayeezyServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		
		// Thread.dumpStack();
		
		String id = (String) serviceData.get("name");

        Map<String, Object> credentials = getCredentials(serviceData);
        
        return new PayeezyServiceInfo(id, credentials);
        
	}
	
	
	public boolean accept(Map<String,Object> serviceData) {
		//Thread.dumpStack();
		System.out.println("Got Service Data for acceptance: " + serviceData 
			+ " and name: " + serviceData.get("name"));
		String serviceName = (String)serviceData.get("name");
		String serviceLabel = (String)serviceData.get("label");
		
		boolean nameGood = (serviceName !=null && serviceName.indexOf(PayeezyServiceInfo.PAYEEZY_SCHEME) > -1);
		boolean labelGood = (serviceLabel !=null && serviceLabel.indexOf(PayeezyServiceInfo.PAYEEZY_SCHEME) > -1);
		
		System.out.println("Name is good: " + nameGood);
		System.out.println("Label is good: " + labelGood);
		
		return nameGood || labelGood;
	}


}
