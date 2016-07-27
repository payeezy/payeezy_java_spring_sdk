package com.firstdata.payeezy.models.transaction;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(getterVisibility= Visibility.NONE,setterVisibility= Visibility.NONE,fieldVisibility= Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class Card implements Cloneable {

	public Card(){

	}

	@JsonProperty("type")
	private String type;

	@JsonProperty("cardholder_name")
	private String name;

	@JsonProperty("card_number")
	private String number;

	@JsonProperty("exp_date")
	private String expiryDt;

	@JsonProperty("cvv")
	private String cvv;
	
	
	
	public String getType() {
		return type;
	}
	
	public Card setType(String type) {
		this.type = type;
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public Card setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getNumber() {
		return number;
	}
	
	public Card setNumber(String nmber) {
		this.number = nmber;
		return this;
	}
	
	public String getExpiryDt() {
		return expiryDt;
	}
	
	public Card setExpiryDt(String expiryDt) {
		this.expiryDt = expiryDt;
		return this;
	}
	
	public String getCvv() {
		return cvv;
	}
	
	public Card setCvv(String cvv) {
		this.cvv = cvv;
		return this;
	}

	@Override
	public Card clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (Card)super.clone();
	}

}
