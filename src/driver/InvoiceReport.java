package driver;


import com.ceg.ext.InvoiceData;

import reader.DBReader;
import writer.ConsoleWriter;

public class InvoiceReport {

	public static void main(String[] args) {
		
		//FOR ASSIGNMENT 5
		//Creates the SQL Reader object
		DBReader dbr = new DBReader();	
		
		//Creates a ConsoleWriter to print invoiceList
		ConsoleWriter wr = new ConsoleWriter();	
		
		//prints Summary and Detailed report for invoices
		wr.toConsole(dbr.getInvoice());		
		
		//Testing InvoiceData
		InvoiceData id = new InvoiceData();	
	}
}
