package entities;

import java.util.List;

import org.joda.time.DateTime;

public class Invoice implements Comparator<Invoice>{

	//attributes
	private String invoiceCode;
	private DateTime invoiceDate;
	private Customer customer;
	private Person salesPerson;
	private List<Product> product;
	private String salePersonFullName;
	private String personAssociated;

	//construtor
	public Invoice(String invoiceCode, DateTime invoiceDate, Customer customer, Person salesPerson, List<Product> product) {
		this.invoiceCode = invoiceCode;
		this.invoiceDate = invoiceDate;
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.product = product;
		this.salePersonFullName = this.salesPerson.getLastName() + ", " + this.salesPerson.getFirstName();
		this.personAssociated = this.customer.getAssociatedPerson().getLastName() + ", " + this.customer.getAssociatedPerson().getFirstName();
	}

	//getters & setters
	public String getInvoiceCode() {
		return invoiceCode;
	}
	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}
	public DateTime getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(DateTime invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Person getSalesPerson() {
		return salesPerson;
	}
	public void setSalesPerson(Person salesPerson) {
		this.salesPerson = salesPerson;
	}
	public List<Product> getProduct() {
		return product;
	}
	public void setProduct(List<Product> product) {
		this.product = product;
	}
	//Gets subTotal for invoice 
	public double getSubTotal() {
		double subTotal = 0;	//here subTotal is added too

		//loops through entire product List
		for (Product aProduct : this.product) {

			if (aProduct instanceof MovieTicket) {	//condition for MovieTicket
				double subTotalCost = ((MovieTicket) aProduct).computeSubtotal();
				subTotal += subTotalCost;

			} else if (aProduct instanceof ParkingPass) {	//condition for ParkingPass
				double subTotalCost = ((ParkingPass) aProduct).computeSubtotal();
				subTotal += subTotalCost;

			} else if (aProduct instanceof SeasonPass) {	//condition for SeasonPass
				// Is different
				double subTotalCost = ((SeasonPass) aProduct).computeSubtotal(invoiceDate);
				subTotal += subTotalCost;

			} else if (aProduct instanceof Refreshment) {	//condition for Refreshment
				double subTotalCost = ((Refreshment) aProduct).computeSubtotal(this.product);
				subTotal += subTotalCost;
			}
		}
		return subTotal;
	}
	//Gets tax for invoice
	public double getTax() {
		double tax = 0;	//here tax is added to

		//loops through product List and adds up all the taxes
		for(Product aProduct : this.product) {
			tax += aProduct.getTax();
		}
		this.customer.getTax(tax);	//Depending if student or general there will be a difference in taxes
		return tax;	
	}
	//Gets total for invoice
	public double getTotal() {
		double total = 0;	//here total is added to

		//loops through product List and adds up all the totals
		for(Product aProduct: this.product) {
			total += aProduct.computeGrandTotal();
		}
		total += customer.getAdditionalFee() + customer.getDiscount(this.getSubTotal());	//Depending if student or general they will have different totals
		return total;
	}
	//method prints the summary report for invoice
	public void getSummaryReport() {
		System.out.printf("%-12s %-40s %-35s $%10.2f $%10.2f $%10.2f $%10.2f $%10.2f\n", this.getInvoiceCode(), this.customer.getCustomerName() + " [" + this.customer.getType() + "]" ,
				this.salePersonFullName, this.getSubTotal(), this.customer.getAdditionalFee(), this.getTax(),
				this.customer.getDiscount(this.getSubTotal()), this.getTotal());
	}
	//method prints the detailed report for invoice
	public void getInvoiceReport() {
		double total = 0;
		System.out.printf(

				//prints sales person then customers information
				"%-15s %-10s \n========================\n%-13s %-50s\n %-14s\n\t%-50s\n\t%-10s\n\t%-50s\n\t%-100s\n\t%-100s\n%-100s",
				"Invoice", this.getInvoiceCode(), "Salesperson: ", this.salePersonFullName, "Customer Info:",
				this.customer.getCustomerName() + " (" + this.customer.getCustomerCode() + ")",
				"[" + this.customer.getType() + "]", this.personAssociated, this.customer.getAddress().getStreet(),
				this.customer.getAddress().getCity() + " " + this.customer.getAddress().getState() + " "
						+ this.customer.getAddress().getZipCode() + " " + this.customer.getAddress().getCountry(),
				"-------------------------------------------");
		System.out.printf("\n%-10s %-67s %12s %9s %13s\n", "Code", "Item", "SubTotal", "Tax", "Total");
		//Then we print details for each of the product
		for (int i = 0; i < product.size(); i++) {
			total += this.product.get(i).computeGrandTotal();
			if (product.get(i) instanceof MovieTicket) {
				System.out.printf("%-10s %-68s$ %10.2f $%8.2f $%12.2f \n%-10s %-20s\n",this.product.get(i).getProductCode(), this.product.get(i).toString1(),  ((MovieTicket)this.product.get(i)).computeSubtotal(),
						this.product.get(i).getTax(), this.product.get(i).computeGrandTotal(), " " ,this.product.get(i).toString2());
			} else if (product.get(i) instanceof ParkingPass) {
				System.out.printf("%-10s %-68s$ %10.2f $%8.2f $%12.2f %-20s\n",this.product.get(i).getProductCode(), this.product.get(i).toString1(),  ((ParkingPass)this.product.get(i)).computeSubtotal(),
						this.product.get(i).getTax(), this.product.get(i).computeGrandTotal(), this.product.get(i).toString2());
			} else if (product.get(i) instanceof SeasonPass) {
				System.out.printf("%-10s %-68s$ %10.2f $%8.2f $%12.2f \n%-10s %-20s\n",this.product.get(i).getProductCode(), this.product.get(i).toString1(),  ((SeasonPass) this.product.get(i)).computeSubtotal(invoiceDate),
						this.product.get(i).getTax(), this.product.get(i).computeGrandTotal(), " ",this.product.get(i).toString2());
			} else if (product.get(i) instanceof Refreshment) {
				System.out.printf("%-10s %-68s$ %10.2f $%8.2f $%12.2f %-20s\n",this.product.get(i).getProductCode(), this.product.get(i).toString1(),  ((Refreshment) this.product.get(i)).computeSubtotal(this.product),
						this.product.get(i).getTax(), this.product.get(i).computeGrandTotal(), this.product.get(i).toString2());
			}

		}
		System.out.printf("%-78s %-30s", "","====================================\n");
		//prints the subTotal, tax, & total for all products
		System.out.printf("%-79s$ %10.2f $%8.2f $%12.2f\n", "Sub-Totals", this.getSubTotal(), this.getTax(), total);
		if (this.customer instanceof Student) {
			System.out.printf("%-101s $%12.2f\n%-101s $%12.2f\n%-101s $%12.2f\n %-100s\n", "DISCOUNT ( 8% STUDENT & NO TAX )",
					this.customer.getDiscount(this.getSubTotal()), "ADDITIONAL FEE (Student)",
					this.customer.getAdditionalFee(), "Total", this.getTotal(), "\n\t\tThankyou for your purchase!\n");
		} else if (this.customer instanceof General) {
			System.out.printf("%-101s $%12.2f\n %-100s\n", "Total", this.getTotal(), "\n\t\tThankyou for your purchase!\n");
		}
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((invoiceCode == null) ? 0 : invoiceCode.hashCode());
		result = prime * result + ((invoiceDate == null) ? 0 : invoiceDate.hashCode());
		result = prime * result + ((personAssociated == null) ? 0 : personAssociated.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((salePersonFullName == null) ? 0 : salePersonFullName.hashCode());
		result = prime * result + ((salesPerson == null) ? 0 : salesPerson.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Invoice other = (Invoice) obj;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (invoiceCode == null) {
			if (other.invoiceCode != null)
				return false;
		} else if (!invoiceCode.equals(other.invoiceCode))
			return false;
		if (invoiceDate == null) {
			if (other.invoiceDate != null)
				return false;
		} else if (!invoiceDate.equals(other.invoiceDate))
			return false;
		if (personAssociated == null) {
			if (other.personAssociated != null)
				return false;
		} else if (!personAssociated.equals(other.personAssociated))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (salePersonFullName == null) {
			if (other.salePersonFullName != null)
				return false;
		} else if (!salePersonFullName.equals(other.salePersonFullName))
			return false;
		if (salesPerson == null) {
			if (other.salesPerson != null)
				return false;
		} else if (!salesPerson.equals(other.salesPerson))
			return false;
		return true;
	}
	@Override
	public int compare(Invoice invoice1, Invoice invoice2) {
		if(invoice1.getTotal() > invoice2.getTotal()) {
			return 1;
		}else if(invoice1.getTotal() < invoice2.getTotal()) {
			return -1;
		}else {
			return 0;
		}
	}
}
