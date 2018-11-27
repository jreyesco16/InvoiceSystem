package entities;

public abstract class Product {

	//attributes
	private String productCode;
	private char productType;
	private double cost;
	private int numbOfPurchase;
	//constructor
	public Product(String productCode, char productType, double cost) {
		super();
		this.productCode = productCode;
		this.productType = productType;
		this.cost = cost;
	}
	//gettes & setters
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public char getProductType() {
		return productType;
	}
	public void setProductType(char productType) {
		this.productType = productType;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public void setNumbOfPuchase(int numbOfPurchase) {
		this.numbOfPurchase = numbOfPurchase;
	}
	public int getNumbOfPurchase() {
		return this.numbOfPurchase;
	}
	
	//Abstract methods used by child classes
	public abstract String toString1();
	public abstract String toString2();
	public abstract double getTax();
	public abstract double computeGrandTotal();
}
