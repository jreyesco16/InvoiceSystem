package entities;

public class Address {
	//attributes
	private String street;
	private String city;
	private String state;
	private String zipCode;
	private String country;

	//constructor
	public Address(String street, String city, String state, String zipCode, String country) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
	}	
	//method that retrieves Address attributes
	public Address(Address a) {
		this.street = a.getStreet();
		this.city = a.getCity();
		this.state = a.getState();
		this.zipCode = a.getZipCode();
		this.country = a.getCountry();
	}
	//getters & setters 
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
}
