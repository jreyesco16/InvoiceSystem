package entities;

import org.joda.time.DateTime;

public class MovieTicket extends Ticket {
	//attributes
	private DateTime dateTime;
	private String name;
	private Address address;
	private String screenNo;
	private final double taxRate = .06;
	private double subTotal;

	//constructor
	public MovieTicket(String productCode, char productType, DateTime dateTime, String movieName, Address address,
			String screenNo, double cost) {
		super(productCode, productType, cost);
		this.dateTime = dateTime;
		this.name = movieName;
		this.address = address;
		this.screenNo = screenNo;
	}

	//constructor MovieTicket when passed MovieTicket object 
	public MovieTicket(MovieTicket mt) {
		super(mt.getProductCode(), mt.getProductType(), mt.getCost());
		this.dateTime = new DateTime(mt.getDateTime());
		this.name = mt.getName();
		this.address = new Address(mt.getAddress());
		this.screenNo = mt.getScreenNo();
	}

	//getters & setters
	public DateTime getDateTime() {
		return dateTime;
	}
	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public String getScreenNo() {
		return screenNo;
	}
	public void setScreenNo(String screenNo) {
		this.screenNo = screenNo;
	}
	@Override
	public double getTax() {
		return this.computeSubtotal() * taxRate;
	}
	@Override
	public double computeGrandTotal() {
		return this.computeSubtotal() + this.getTax();
	}
	//Method computes the subTotal for MovieTicket
	public double computeSubtotal() {
		// if the day is on tuesday or thursday then 7% discount
		if (dateTime.getDayOfWeek() == 2 || dateTime.getDayOfWeek() == 4) {
			this.subTotal = this.getCost() * this.getNumbOfPurchase() * .93;
			return this.subTotal;
		} else {
			this.subTotal = this.getCost() * this.getNumbOfPurchase();
			return this.subTotal;
		}
	}
	//Method returns MovieTicket Date in detailed print
	@Override
	public String toString1() {
		return "MovieTicket" + " '" + this.name + "' @ " + this.address.getStreet();	
	}
	//Method returns MovieTicket Date in detailed print
	@Override
	public String toString2() {
		//Condition for Tues or Thurs
		if (dateTime.getDayOfWeek() == 2 || dateTime.getDayOfWeek() == 4) {
			return dateTime.toString("MMM") + " " + dateTime.getDayOfMonth() + "," + dateTime.getYear() + " " + dateTime.getHourOfDay() + ":" + dateTime.getMinuteOfHour() + "(" + this.getNumbOfPurchase() + " units @ $" + this.getCost() + "/unit - Tue/Thu 7% off)";
		} else {
			return dateTime.toString("MMM") + " " + dateTime.getDayOfMonth() + "," + dateTime.getYear() + " " + dateTime.getHourOfDay() + ":" + dateTime.getMinuteOfHour() + "(" + this.getNumbOfPurchase() + " units @ $" + this.getCost() + "/unit)";
		}
	}
}