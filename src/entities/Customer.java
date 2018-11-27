package entities;

public abstract class Customer {
	
	//attributes
	private String customerCode;
	private String type;
	private Person associatedPerson;
	private String customerName;
	private Address address;

	//constructor
	public Customer(String customerCode, String type, Person associatedPerson, String customerName, Address address) {
		super();
		this.customerCode = customerCode;
		this.type = type;
		this.associatedPerson = associatedPerson;
		this.customerName = customerName;
		this.address = address;
	}
	//getters & setters
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Person getAssociatedPerson() {
		return associatedPerson;
	}
	public void setAssociatedPerson(Person associatedPerson) {
		this.associatedPerson = associatedPerson;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	//abstract methods use for calculations of invoice
	public abstract void getTax(double tax);
	public abstract double getDiscount(double subTotal);
	public abstract double getAdditionalFee();
}
