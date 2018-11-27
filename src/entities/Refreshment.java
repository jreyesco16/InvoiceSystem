package entities;

import java.util.List;

public class Refreshment extends Service {

	//attributes
	private String name;
	private final double taxRate = .04;
	private double subTotal;
	boolean hasTicket = false;

	//constructor
	public Refreshment(String productCode, char productType, String name, double cost) {
		super(productCode, productType, cost);
		this.name = name;
	}
	//constructor that creates Refreshment when pass Refreshment object
	public Refreshment(Refreshment r) {
		super(r.getProductCode(), r.getProductType(), r.getCost());
		this.name = r.getName();
	}
	//getters & setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	//Methods used to get subTotal, tax, & total of Refreshment
	@Override
	public double getTax() {
		return this.subTotal * taxRate;
	}
	@Override
	public double computeGrandTotal() {
		return subTotal + this.getTax();
	}
	public double computeSubtotal(List<Product> product) {
		hasTicket = false;
		for(Product aProduct : product) {
			if(aProduct instanceof Ticket) {
				hasTicket = true;
			}
		}
		if(hasTicket) {	//condition for pre-order
			this.subTotal = this.getCost() * this.getNumbOfPurchase() * .95;
		}else {
			this.subTotal = this.getCost() * this.getNumbOfPurchase();
		}
		return subTotal;
	}

	//Methods for detailed print mostly used for getInvoiceReport() in Invoice.class
	@Override
	public String toString1() {
		if(hasTicket) {	//condition for pre-order
			return  this.name + "(" + this.getNumbOfPurchase() + " units @ $" + this.getCost() + "/unit with 5% off)";
		}else {
			return  this.name + " (" + this.getNumbOfPurchase() + " units @ $" + this.getCost() + "/unit)" ;
		}
	}
	@Override
	public String toString2() {	//creates dummy space used in getInvoiceReport()
		return "";
	}
}
