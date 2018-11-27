package writer;

import java.util.List;

import entities.Invoice;
import entities.LinkedList;
import entities.Node;

public class ConsoleWriter {

	//attributes
	private double totalSubtotal;
	private double totalFee;
	private double totalTaxes;
	private double totalDiscount;
	private double finalTotal;

	//Method creates outline for Summary and Detailed report for Invoices when passed invoice List
	public void toConsole(LinkedList invoiceList) {
		System.out.println("=========================\nExecutive Summary Report\n=========================");
		System.out.printf("%-12s %-40s %-35s %11s %11s %11s %11s %11s\n" , "Invoice", "Customer" , "Salesperson", "Subtotal", "Fees", "Taxes", "Discount", "Total");
		//Loops through Invoices and prints Summary of of Each Customer Invoice
		Node aNode = invoiceList.getStart();
		for(int i = 0; i < invoiceList.getSize();i++) {
			Invoice aInvoice = (Invoice) aNode.getData();
			totalSubtotal += aInvoice.getSubTotal();
			totalFee += aInvoice.getCustomer().getAdditionalFee();
			totalTaxes += aInvoice.getTax();
			totalDiscount += aInvoice.getCustomer().getDiscount(aInvoice.getSubTotal());
			finalTotal += aInvoice.getTotal();
			aInvoice.getSummaryReport();
			aNode= aNode.getNext();
		}
		System.out.printf("%-100s", "=====================================================================================================================================================\n");
		//total for invoices printed last
		System.out.printf("%-89s $%10.2f $%10.2f $%10.2f $%10.2f $%10.2f\n" , "TOTALS", totalSubtotal, totalFee, totalTaxes, totalDiscount, finalTotal);
		//Prints detail report for each invoice
		System.out.println("\n\nIndividual Invoice Detail Reports\n==================================================");
		//loops through invoice and print details of each invoice
		aNode = invoiceList.getStart();
		for(int i = 0; i < invoiceList.getSize();i++) {
			//pull Invoice from current Node
			Invoice aInvoice = (Invoice) aNode.getData();
			aInvoice.getInvoiceReport();
			//Moves to next Node
			aNode = aNode.getNext();
		}
		System.out.println("\n======================================================================================================================");
	}
}
