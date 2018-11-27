package entities;

import java.util.List;

public class ParkingPass extends Service {
	//attributes
	private final double taxRate = .04;
	private int numbFree = 0;
	private int numbPurchase = 0;
	private String connectCode = null;
	private String numbPrintFree;
	private double subTotal;

	//constructor
	public ParkingPass(String productCode, char productType, double cost) {
		super(productCode, productType, cost);
	}
	//constructor that creates ParkingPass when ParkingPass is passed
	public ParkingPass(ParkingPass pp) {
		super(pp.getProductCode(), pp.getProductType(), pp.getCost());
	}
	//getters & setters
	public int getNumbFree() {
		return numbFree;
	}
	public void setNumbFree(int numbFree) {
		this.numbFree = numbFree;
	}
	public int getNumbPurchase() {
		return numbPurchase;
	}
	public void setNumbPurchase(int numbPurchase) {
		this.numbPurchase = numbPurchase;
	}
	public String getConnectCode() {
		return connectCode;
	}
	public void setConnectCode(String connectCode) {
		this.connectCode = connectCode;
	}
	public String getNumbPrintFree() {
		return numbPrintFree;
	}
	public void setNumbPrintFree(String numbPrintFree) {
		this.numbPrintFree = numbPrintFree;
	}
	public double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	public double getTaxRate() {
		return taxRate;
	}

	//set the number of free ParkingPass purchases
	public void setNumbFreePass(String aProductCode, List<Product> product) {
		for(Product aProduct : product) {
			if(aProduct.getProductCode().equals(aProductCode)) {
				int numbForDiscount = aProduct.getNumbOfPurchase();
				numbPurchase = this.getNumbOfPurchase();
				connectCode = aProductCode;
				if(numbForDiscount <= numbPurchase) {
					numbFree = this.getNumbOfPurchase() - aProduct.getNumbOfPurchase();
					numbPrintFree = "" + numbFree;
				}else {
					numbPrintFree = "" + this.getNumbOfPurchase();
					numbFree = 0;
				}
				this.setNumbOfPuchase(numbFree);
			}
		}
	}
	//Methods get the subTotal, tax, & total for ParkingPass
	@Override
	public double getTax() {
		return this.subTotal * taxRate;
	}
	@Override
	public double computeGrandTotal() {
		return this.subTotal + this.getTax();
	}
	public double computeSubtotal() {
		this.subTotal = this.getCost() * this.getNumbOfPurchase();
		return this.subTotal;
	}
	//Detailed print for Parking pass units, mostly used by getInvoiceReport() in Invoice.class
	@Override
	public String toString1() {
		if(!(getConnectCode() == null)) {
			return  "ParkingPass " + connectCode + " (" + this.numbPurchase + " units @ $" + this.getCost() + "/unit with " + (this.numbPurchase - Integer.parseInt(this.numbPrintFree)) + " free)";
		}else {
			return  "ParkingPass (" + this.getNumbOfPurchase() + " units @ $" + this.getCost() + "/unit)" ;
		}
	}
	//Detail print when ParkingPass has now Parking Pass waves
	@Override
	public String toString2() {
		return "";
	}
}
