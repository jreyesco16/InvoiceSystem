package reader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import entities.Address;
import entities.Customer;
import entities.DatabaseConnection;
import entities.General;
import entities.Invoice;
import entities.LinkedList;
import entities.MovieTicket;
import entities.ParkingPass;
import entities.Person;
import entities.Product;
import entities.Refreshment;
import entities.SeasonPass;
import entities.Student;

public class DBReader {
	//creates Person object from SQL database for given PersonCode
	public Person getDetailedPerson(String PersonCode) {
		//SQL line that gets person, gets all columns for person
		String query = ("Select Person.PersonID,PersonCode,FirstName,LastName,Address.AddressID,Street,City,State,Zip,CountryName,EmailID,PersonEmail from Person left join Address on Person.AddressID = Address.AddressID left join Email on Person.PersonID = Email.PersonID inner join Country on Address.CountryID=Country.CountryID where PersonCode like ?");
		//Connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			//PrepatedStatement uses PersonCode string passed to method, inserts PersonCode to query in "?" place
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, PersonCode);
			ResultSet rs = ps.executeQuery();
			//ResultSet rs must be sorted through
			if(rs != null) {
				rs.next();	//moves next step from rs cursor to access data
				//will not get PersonID since it not necessary for this Program
				String personCode = rs.getString(2); //gets PersonCode
				String FirstName = rs.getString(3); //gets FirstName
				String LastName = rs.getString(4); //gets LastName
				//Person's Address attributes
				String Street = rs.getString(6); 
				String City = rs.getString(7);
				String State = rs.getString(8);
				String Zip = rs.getString(9);
				String Country = rs.getString(10);
				//Person's Emails atrributes
				ArrayList<String>Email = new ArrayList<>();
				int size = 0;
				do {
					Email.add(rs.getString(12));
				}while(rs.next());
				//creates Address using Person's Address attributes
				Address a = new Address(Street,City,State,Zip,Country);
				//Creates the Person that will be returned
				Person p = new Person(personCode, FirstName, LastName, a, Email);
				//cleans up environment and returns LinkedList of Persons
				rs.close();	
				conn.close();
				ps.close();
				return p;
			}
			//return nothing if incorrect data found
			rs.close();	
			conn.close();
			ps.close();
			return null;
		}
		//catches error Query creates and error
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	//creates Customer using when Passed Customer's CustomerCode
	public Customer getDetailedCustomer(String customerCode) {
		//intialized Customer
		Customer c = null;
		//Query that gets all attributes for Customers
		String query = ("Select CustomerID,PersonCode,CustomerCode,CustomerType,CustomerName,Street,City,State,Zip,CountryName from Customer left join Person on Customer.PersonID=Person.PersonID left join Address on Customer.AddressID=Address.AddressID left join Country on Address.CountryID=Country.CountryID where CustomerCode like ?");
		//connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			//PrepatedStatement uses PersonCode string passed to method, inserts PersonCode to query in "?" place
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, customerCode);
			ResultSet rs = ps.executeQuery();
			//ResultSet rs must be sorted through
			if(rs != null) {
				rs.next();
				//PersonCode for Customer account
				String PersonCode = rs.getString(2);
				//Attributes from Customer
				String CustomerCode = rs.getString(3);
				String CustomerType = rs.getString(4);
				String CustomerName = rs.getString(5);
				//Customer Address
				String street = rs.getString(6);
				String city = rs.getString(7);
				String state = rs.getString(8);
				String zip = rs.getString(9);
				String country = rs.getString(10);
				//creates the Person
				Person p = getDetailedPerson(PersonCode); //(Person)personList.getThing(PersonCode);
				//creates the Address for Person;
				Address a = new Address(street, city,state,zip,country);
				//creates the Customer, depending on their type
				if(CustomerType.equals("Student")) {				//condtion for student Customer
					c = new Student(CustomerCode,CustomerType,p,CustomerName,a);	
				}else {												//condtion for general Customer
					c = new General(CustomerCode, CustomerType,p,CustomerName, a);
				}
				//cleans up SQL environment & returns Customer
				conn.close();
				rs.close();
				ps.close();
				return c;
			}
			//No Customer was created due to internal error, cleans up SQL environment
			conn.close();
			rs.close();
			ps.close();
			return null;
		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}
	}
	//gets Product from dataBase with matching ProductCode
	public Product getDetailedProduct(String productCode) {
		//initialized product string
		Product p = null;
		//SQL string to get all details for a Product
		String query = ("Select ProductType, ProductCode,ProductName,Product.Cost,ScreeNO,MovieDate,Street,City,State,Zip,CountryName, StartDate,EndDate from Product left join Address on Product.AddressID=Address.AddressID left join Country on Address.CountryID=Country.CountryID where ProductCode like ?");
		//connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			//PrepatedStatement uses PersonCode string passed to method, inserts PersonCode to query in "?" place
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, productCode);
			ResultSet rs = ps.executeQuery();
			//extract data from the Result Set
			if(rs != null){
				rs.next();
				//Common attributes for all Products
				String temp = rs.getString(1);				
				char ProductType = temp.charAt(0);
				String ProductCode = rs.getString(2);
				String ProductName = rs.getString(3);
				double ProductCost = rs.getDouble(4);
				//condition that creates MovieTicket
				if(ProductType=='M'){	//condition for MovieTicket
					//Attributes for MovieTicket
					String ScreeNO = rs.getString(5);
					DateTimeFormatter movieDateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");	//parse MovieDates to DateTime object
					DateTime movieDate = movieDateFormat.parseDateTime(rs.getString(6));
					String street = rs.getString(7);
					String city = rs.getString(8);
					String state = rs.getString(9);
					String zip = rs.getString(10);
					String country = rs.getString(11);
					Address a = new Address(street, city,state,zip,country);
					p = new MovieTicket(ProductCode,ProductType,movieDate,ProductName,a,ScreeNO,ProductCost);	//movieTicket created
				}
				//condition that creates SeaonPass
				if(ProductType == 'S') {
					//Attributes for SeasonPass
					DateTimeFormatter seasonPassDateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
					DateTime StartDate = seasonPassDateFormat.parseDateTime(rs.getString(12));
					DateTime EndDate = seasonPassDateFormat.parseDateTime(rs.getString(13));
					p = new SeasonPass(ProductCode, ProductType,ProductName,StartDate,EndDate,ProductCost);	//SeasonPass product created
				}
				//condition that creates Refreshments
				if(ProductType == 'R'){
					p = new Refreshment(ProductCode,ProductType,ProductName,ProductCost);	//Refreshment created
				}
				//condition that created ParkingPass
				if(ProductType == 'P') {
					p = new ParkingPass(ProductCode,ProductType,ProductCost);	//ParkingPass created
				}
				//cleans up SQL environment and return Product
				rs.close();
				ps.close();
				conn.close();
				return p;	
			}
			//cleans up SQL environment & return not product, Result Set wasn't created due to an internal error
			rs.close();
			ps.close();
			conn.close();
			return null;
		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}
	}
	//create Invoice object from invoiceCode Passed
	public Invoice getDetailedInvoice(String invoiceCode) {
		//initialized Product to be returned
		Invoice invoice= null;
		//ArrayList that store product for Invoice
		ArrayList<Product>product = new ArrayList<>();
		//Query for getting all product for an Invoice
		String query = ("Select InvoiceCode,InvoiceDate,CustomerCode,PersonCode,ProductCode,TicketCode,NumbOfPurchases from Invoice inner join Customer on Invoice.CustomerID=Customer.CustomerID inner join Person on Invoice.PersonID=Person.PersonID left join InvoiceProduct on Invoice.InvoiceID=InvoiceProduct.InvoiceID left join Product on InvoiceProduct.ProductID=Product.ProductID where InvoiceCode like ?");
		//connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			//creates the ResultSet with all InvoiceCodes
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ResultSet rs = ps.executeQuery();
			//loops through ResultSet and adds all InvoiceCodes to invoiceCode Array
			if(rs != null) {
				rs.next();
				//attributes for invoice
				String InvoiceCode = rs.getString(1);
				String temp = rs.getString(2);
				DateTimeFormatter date = DateTimeFormat.forPattern("yyyy-MM-dd");
				DateTime InvoiceDate = date.parseDateTime(temp);
				Customer c = getDetailedCustomer(rs.getString(3));
				Person p = getDetailedPerson(rs.getString(4));	
				//loop that add all products to ArrayList
				do{
					Product prod = getDetailedProduct(rs.getString(5));
					//sets the Ticket that waves parking passes for ParkingPass 
					if(prod instanceof ParkingPass) {
						prod = (ParkingPass)prod;
						((ParkingPass) prod).setConnectCode(rs.getString(6));
					}
					//sets number of purchases for each product purchased
					prod.setNumbOfPuchase(rs.getInt(7));
					product.add(prod);
				}while(rs.next());
				//loop determines the amount of free passes for the parking passes
				for(Product pTemp: product) {
					if(pTemp instanceof ParkingPass) {
						((ParkingPass) pTemp).setNumbFreePass(((ParkingPass) pTemp).getConnectCode(),product);
					}
				}
				//creates invoice with given data
				invoice = new Invoice(InvoiceCode,InvoiceDate,c,p,product);
				//cleans up environment & and returns Invoice
				ps.close();
				rs.close();
				conn.close();
				return invoice;
			}
			//cleans up SQL environment & result set wasn't created do to an internal error
			ps.close();
			rs.close();
			conn.close();
			return null;
		}
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	//Creates Invoice Object for all Invoices in dataBase and stores then in ADT List
	public LinkedList getInvoice() {
		//initialized LinkedList
		LinkedList invoiceList = new LinkedList();
		//Query that gets all InoivceCodes
		String query = ("Select InvoiceCode from Invoice");
		//connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			//creates the Result Set for all InvoiceCodes
			Statement stmt = conn.createStatement();
			ResultSet rs= stmt.executeQuery(query);
			//loop creates Invoices using all InvoiceCodes in dataBase
			if(rs!=null) {
				while(rs.next()){
					String InvoiceCode = rs.getString(1);
					Invoice invoice = getDetailedInvoice(InvoiceCode);
					invoiceList.addToList(invoice);;
				}
				//cleans up SQL environment and returns invoiceList
				stmt.close();
				rs.close();
				conn.close();
				return invoiceList;
			}
			//cleans up SQL environment & returns null since no result set was created due to internal error
			stmt.close();
			rs.close();
			conn.close();
			return null;
		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}
	}
}
