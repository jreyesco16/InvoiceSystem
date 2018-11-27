package entities;

import java.util.ArrayList;
import java.util.Arrays;

public class Person {

	//attributes
	private String personCode;
	private String firstName;
	private String lastName;
	private Address address;
	private ArrayList<String> emailAddress;

	//constructor
	public Person(String personCode, String firstName, String lastName, Address address, ArrayList<String> emailAddress) {
		this.personCode = personCode;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.emailAddress = emailAddress;
	}
	//constructor without emails
	public Person(String personCode, String firstName, String lastName, Address address) {
		this.personCode = personCode;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}
	
	//getters & setters
	public String getPersonCode() {
		return personCode;
	}
	public void setPersonCode(String personCode) {
		this.personCode = personCode;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public ArrayList<String> getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(ArrayList<String> emailAddress) {
		this.emailAddress = emailAddress;
	}
}
