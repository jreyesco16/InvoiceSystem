package entities;

public class Student extends Customer {
	//attribute
	private double tax = 0;
	//constructor
	public Student(String customerCode, String type, Person associatedPerson, String customerName, Address address) {
		super(customerCode, type, associatedPerson, customerName, address);
	}
	//Methods gets tax, discounts, fees for student invoices
	@Override
	public void getTax(double tax) {
		this.tax = tax;
	}
	@Override
	public double getDiscount(double subTotal) {
		return (-1 * ((0.08 * subTotal) + this.tax));
	}
	//Students will fee'd(made this word up) on top of there total
	@Override
	public double getAdditionalFee() {	
		return 6.75;
	}
}