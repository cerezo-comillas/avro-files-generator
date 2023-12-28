package edu.comillas.bigdata.avro_generator;

import net.datafaker.Faker;

@SuppressWarnings("unused")
public class MyDataObject {

	static Faker myFaker = new Faker();

	private String firstName;
	private String lastName;
	private String city;
	private String country;
	private String zipcode;
	private String streetAddress;
	private String telephone;

	
	
	
	public MyDataObject() {

       firstName     =  myFaker.name().firstName();
       lastName      =  myFaker.name().lastName() ;
       country       = myFaker.address().country() ;
       city          = myFaker.address().city() ;
       streetAddress = myFaker.address().streetAddress();
       zipcode       = myFaker.address().zipCode() ;
       telephone     = myFaker.phoneNumber().phoneNumber();
	
	}
	
	
	
}
