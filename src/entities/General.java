package entities;

public class General extends Customer {

	//constructor
	public General(String customerCode, String type, Person associatedPerson, String customerName,Address address) {
		super(customerCode, type, associatedPerson, customerName, address);
	}

	//methods used to retrieve data from costumers
	@Override
	public void getTax(double tax) {							//No tax,discount, or additional fee since 
		tax = 0;											    //general customer have no privileges in 
	}															//this phase
	@Override
	public double getDiscount(double subTotal) {
		return 0.0;
	}
	@Override
	public double getAdditionalFee() {
		return 0.0;
	}
}