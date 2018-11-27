package entities;

//Abstract class that contains methods used by all services(Refreshments & ParkingPass)
public abstract class Service extends Product {
	public Service(String productCode, char productType, double cost) {
		super(productCode, productType, cost);
	}
	//Abstract Methods for by Refreshments & ParkingPass
	public abstract double getTax();
	public abstract double computeGrandTotal();
}
