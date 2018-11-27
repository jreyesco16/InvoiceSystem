package entities;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class SeasonPass extends Ticket {
	//attributes
	private String name;
	private DateTime startDate;
	private DateTime endDate;
	private final double taxRate = .06;
	private double subTotal;
	private Days daysBetween;
	private Days proRated;
	
	//constructor
	public SeasonPass(String productCode, char productType, String name, DateTime startDate, DateTime endDate, double cost) {
		super(productCode, productType, cost);
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	//consttuctor creates SeasonPass when passed SeasonPass object
	public SeasonPass(SeasonPass sp) {
		super(sp.getProductCode(), sp.getProductType(), sp.getCost());
		this.name = sp.getName();
		this.startDate = new DateTime(sp.getStartDate());
		this.endDate = new DateTime(sp.getEndDate());
	}
	//getters & setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}
	public DateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}
	//Methods get subTotal, tax, & total for SeasonPass
	@Override
	public double getTax() {
		return this.subTotal * taxRate;
	}
	@Override
	public double computeGrandTotal() {
		return this.getTax() + this.subTotal;
	}
	public double computeSubtotal(DateTime invoiceDate) {
		if (invoiceDate.isBefore(this.getStartDate()) || invoiceDate.isEqual(this.getStartDate())) {
			subTotal = (this.getCost() * this.getNumbOfPurchase()) + (8 * this.getNumbOfPurchase());
			return subTotal;
		} else {	//condition for SeasonPasses that fall after Invoice Date
			daysBetween = Days.daysBetween(invoiceDate, this.endDate);
			proRated = Days.daysBetween(this.getStartDate(), this.endDate);
			Double proRate = (double) proRated.getDays();
			subTotal = (this.getCost() * this.getNumbOfPurchase() * (daysBetween.getDays()/proRate))
					+ (8 * this.getNumbOfPurchase());
			return subTotal;
		}
	}
	//Method for detail prints mostly used by getInvoiceReport() in Invoice.class
	@Override
	public String toString1() {
		return "Season Pass - " + this.name ;
	}
	@Override
	public String toString2() {
		if (daysBetween == null) {
			return "(" + this.getNumbOfPurchase() + " units @ $"+ this.getCost() + "/unit + $8 fee/unit)";
		}else {	//condition for SeasonPass who start date are before Invoice Date
			return "(" + this.getNumbOfPurchase() + " units @ $"+ this.getCost()+ "/unit prorated " + daysBetween.getDays() + "/" + proRated.getDays() + " days + $8 fee/unit)";
		}
	}
}
