package entities;

//abstact class extends Product used by SeasonPass & MovieTicket
public abstract class Ticket extends Product {
	//consturtor
	public Ticket(String productCode, char productType, double cost) {
		super(productCode, productType, cost);
	}
	//abstract method used by SeasonPass & MovieTicket
	public abstract double getTax();
	public abstract double computeGrandTotal();
}