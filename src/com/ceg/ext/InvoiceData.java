package com.ceg.ext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import entities.DatabaseConnection;
/*
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 * 15 methods in total, add more if required.
 * Dont change any method signatures or the package name.
 * 
 */
public class InvoiceData {
	/**
	 * 1. Method that removes every person record from the database
	 */
	public static void removeAllPersons() {
		//querys for removing all Persons from Database
		String deletefromEmailPersonIDKeys = "Update Email set PersonID = null;"; 
		String deletefromCustomerPersonIDKeys = "Update Customer set PersonID = null;";
		String deletefromInvoicePersonIDkKeys = "Update Invoice set PersonID = null;";
		String deleteAllPeople = "Delete from Person;";
		//Connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(deletefromEmailPersonIDKeys);
			stmt.executeUpdate(deletefromCustomerPersonIDKeys);
			stmt.executeUpdate(deletefromInvoicePersonIDkKeys);
			stmt.executeUpdate(deleteAllPeople);
			//cleans up SQL environment and returns invoiceList
			stmt.close();
			conn.close();
		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}
	}
	/**
	 * 2. Method to add a person record to the database with the provided data.
	 * 
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addPerson(String personCode, String firstName, String lastName, String street, String city, String state, String zip, String country) {
		//Query to add Person
		String addPerson = ("Insert into Person(PersonCode, FirstName, LastName, AddressID) value(?,?,?,?);");
		//Connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			//First must check if Person already exists within database
			String personCheck = ("Select PersonID from Person where PersonCode like ? and FirstName like ? and LastName like ? and AddressID like ?;");
			PreparedStatement check = conn.prepareStatement(personCheck);
			check.setString(1, personCode);
			check.setString(2, firstName);
			check.setString(3, lastName);
			int AddressID = getAddressID(street,city,state,zip,country);
			check.setInt(4, AddressID);
			ResultSet rs = check.executeQuery();
			if(rs.next()) {
				//cleans sql enviroment
				check.close();
				conn.close();
				rs.close();
			}else {
				//appends query to hold correct information
				PreparedStatement ps = conn.prepareStatement(addPerson);
				ps.setString(1,personCode);
				ps.setString(2,firstName);
				ps.setString(3,lastName);
				ps.setInt(4,AddressID);
				//executes Query
				ps.executeUpdate();
				//cleans up SQL environment
				ps.close();
				rs.close();
				conn.close();
			}
		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}		
	}
	/**
	 * 3. Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 * 
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {
		//Query to add for find Person
		String findPerson = ("Select PersonID from Person where PersonCode like ?;");
		//Connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			//first must check if the email already exists with in system
			String emailCheck = ("Select EmailID from Email where PersonEmail like  ?;");
			PreparedStatement ps = conn.prepareStatement(emailCheck);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				//cleans up SQL environment
				ps.close();
				rs.close();
				conn.close();
			}else {
				//Get the PersonID from personCode passed
				ps = conn.prepareStatement(findPerson);
				ps.setString(1, personCode);
				rs = ps.executeQuery();
				//gets PersonID of personCode passed
				rs.next();
				int PersonID = rs.getInt(1);
				//Query to add email to Person
				String emailQuery = ("Insert into Email(PersonID,PersonEmail) value (?,?);");
				ps = conn.prepareStatement(emailQuery);
				ps.setInt(1, PersonID);
				ps.setString(2, email);
				//execute Query to add email to Person
				ps.executeUpdate();
				//cleans up SQL environment
				ps.close();
				rs.close();
				conn.close();
			}
		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}

	}
	/**
	 * 4. Method that removes every customer record from the database
	 */
	public static void removeAllCustomers() {
		//querys for removing all Customers from Database
		String queryKeys = ("Update Invoice set CustomerID = null;");  
		String deleteAllCustomers = ("Delete from Customer;");
		//Connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(queryKeys);
			stmt.executeUpdate(deleteAllCustomers);
			//cleans up SQL environment
			stmt.close();
			conn.close();
		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}
	}
	public static void addCustomer(String customerCode, String customerType, String primaryContactPersonCode,String name, String street, String city, String state, String zip, String country) {
		//query to add Customer
		String addCustomer = ("Insert into Customer(PersonID, CustomerCode,CustomerType,CustomerName,AddressID) value(?,?,?,?,?);");
		//Connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			//Fist must get PersonID of primaryContactPersonCode
			String getPersonID = ("Select PersonID from Person where PersonCode like ?;");
			PreparedStatement ps = conn.prepareStatement(getPersonID);
			ps.setString(1,primaryContactPersonCode );
			ResultSet rs = ps.executeQuery();
			rs.next();
			int PersonID = rs.getInt(1); 
			//Then check if Customer exists within system
			String check = ("Select CustomerID from Customer where CustomerCode like ? and PersonID like ? and AddressID like ?;");
			ps = conn.prepareStatement(check);
			ps.setString(1, customerCode);
			ps.setInt(2, PersonID);
			int AddressID = getAddressID(street,city,state,zip,country);
			ps.setInt(3, AddressID);
			rs = ps.executeQuery();
			//if a CustomerID was found we know that this Customer already exists within system
			if(rs.next()) {
				//cleans up SQL environment
				rs.close();
				ps.close();
				conn.close();
			}else {	//Customer not found in DB must be added
				//adds the Customer to DB
				ps = conn.prepareStatement(addCustomer);
				ps.setInt(1, PersonID);
				ps.setString(2, customerCode);
				ps.setString(3, customerType);
				ps.setString(4, name);
				ps.setInt(5,AddressID);
				//executes query
				ps.executeUpdate();
				//cleans up SQL environment
				ps.close();
				rs.close();
				conn.close();
			}
		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}
	}
	/**
	 * 5. Removes all product records from the database
	 */
	public static void removeAllProducts() {
		//querys for removing all Products from Database
		String queryKeys = ("Update InvoiceProduct set ProductID = null;");  
		String deleteAllProduct = ("Delete from Product;");
		//Connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(queryKeys);
			stmt.executeUpdate(deleteAllProduct);
			//cleans up SQL environment and returns invoiceList
			stmt.close();
			conn.close();
		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}
	}
	/**
	 * 6. Adds an movieTicket record to the database with the provided data.
	 */
	public static void addMovieTicket(String productCode, String dateTime, String movieName, String street, String city,String state, String zip, String country, String screenNo, double pricePerUnit) {
		//query that adds MovieTicket
		String addMovieTicket = ("Insert into Product(ProductCode,ProductType,MovieDate,ProductName,AddressID,ScreeNO,Cost) value(?,?,?,?,?,?,?);");
		//query that checks if MovieTicket already exists
		String check = ("Select ProductID from Product where ProductCode like ? and ProductType like ? and ProductName like ? and AddressID like ?; ");
		//Connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			//first check if MovieTicket already exists
			PreparedStatement ps = conn.prepareStatement(check);
			ps.setString(1, productCode);
			ps.setString(2,"M");
			ps.setString(3, movieName);
			int AddressID = getAddressID(street, city,state,zip,country);
			ps.setInt(4,AddressID);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {	//ticket already in system
				//cleans up SQL environment
				ps.close();
				rs.close();
				conn.close();
			}else {	//adds new ticket into system
				//appends query to hold correct information
				ps = conn.prepareStatement(addMovieTicket);
				ps.setString(1, productCode);
				ps.setString(2, "M");
				ps.setString(3, dateTime);
				ps.setString(4, movieName);
				ps.setInt(5, AddressID);
				ps.setString(6,screenNo);
				ps.setDouble(7, pricePerUnit);
				//executes query
				ps.executeUpdate();
				//cleans up SQL environment
				ps.close();
				rs.close();
				conn.close(); 
			}
		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}	
	}
	/**
	 * 7. Adds a seasonPass record to the database with the provided data.
	 */
	public static void addSeasonPass(String productCode, String name, String seasonStartDate, String seasonEndDate,	double cost) {
		//query that adds SeasonPass
		String addSeasonPass = ("Insert into Product(ProductCode,ProductType,ProductName,StartDate,EndDate,Cost) value (?,?,?,?,?,?);");
		//query to check if seasonPass already exists within system
		String check = ("Select ProductID from Product where ProductCode like ? and ProductType like ? and ProductName like ?;");
		//Connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			//first check if SeasonPass already exists in system
			PreparedStatement ps = conn.prepareStatement(check);
			ps.setString(1, productCode);
			ps.setString(2, "S");
			ps.setString(3, name);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) { //SeasonPass found within system
				//cleans up SQL environment
				rs.close();
				ps.close();
				conn.close();
			}else { //new seasonPass will have to be add to BD
				//appends query to hold correct information
				ps = conn.prepareStatement(addSeasonPass);
				ps.setString(1, productCode);
				ps.setString(2, "S");
				ps.setString(3, name);
				ps.setString(4, seasonStartDate);
				ps.setString(5, seasonEndDate);
				ps.setDouble(6, cost);
				//executes query
				ps.executeUpdate();
				//cleans up SQL environment
				ps.close();
				rs.close();
				conn.close();
			}
		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}	
	}
	/**
	 * 8. Adds a ParkingPass record to the database with the provided data.
	 */
	public static void addParkingPass(String productCode, double parkingFee) {
		//query for add ParkingPass
		String addParkingPass = ("Insert into Product(ProductCode,ProductName,ProductType,Cost) value(?,?,?,?);");
		//query that checks if ParkingPass already exists within system
		String check = ("Select ProductID from Product where ProductCode like ?;");
		//Connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			//first must check if the season pass already exists in system
			PreparedStatement ps = conn.prepareStatement(check);
			ps.setString(1, productCode);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {	//ParkingPass exists within system
				//cleans up SQL environment
				ps.close();
				rs.close();
				conn.close();
			}else {	//parkingPasss to be added to system
				//appends query to hold correct information
				ps = conn.prepareStatement(addParkingPass);
				ps.setString(1, productCode);
				ps.setString(2,"ParkingPass");
				ps.setString(3, "P");
				ps.setDouble(4, parkingFee);
				//executes Query
				ps.executeUpdate();
				//cleans up SQL environment
				ps.close();
				conn.close();
				rs.close();
			}
		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}
	}
	/**
	 * 9. Adds a refreshment record to the database with the provided data.
	 */
	public static void addRefreshment(String productCode, String name, double cost) {
		//Query to add refreshment
		String addRefreshment = ("Insert Product(ProductCode,ProductType,ProductName,Cost) value (?,?,?,?);");
		//Query checks if refreshment already exists within DB
		String check = ("Select ProductID from Product where ProductCode like ? and ProductType like ? and ProductName like ?;");
		//Connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			//first checks if Refreshment is already found within system
			PreparedStatement ps = conn.prepareStatement(check);
			ps.setString(1, productCode);
			ps.setString(2, "R");
			ps.setString(3,name);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {	//Refreshment found in System
				//cleans SQL environment
				rs.close();
				ps.close();
				conn.close();
			}else { //Refreshment needs to be added to system
				//appends query to hold correct information
				ps = conn.prepareStatement(addRefreshment);
				ps.setString(1, productCode);
				ps.setString(2, "R");
				ps.setString(3, name);
				ps.setDouble(4, cost);
				//executes query
				ps.executeUpdate();
				//cleans up SQL environment
				ps.close();
				rs.close();
				conn.close();
			}
		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}			
	}
	/**
	 * 10. Removes all invoice records from the database
	 */
	public static void removeAllInvoices() {
		//querys for removing all Invoices from Database
		String queryKeys = ("Update Invoice,InvoiceProduct set CustomerID = null, PersonID = null, InvoiceProduct.InvoiceID = null;");  
		String deleteAllInvoice = ("Delete from Invoice;");
		//Connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(queryKeys);
			stmt.executeUpdate(deleteAllInvoice);
			//cleans up SQL environment and returns invoiceList
			stmt.close();
			conn.close();
		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}
	}
	/**
	 * 11. Adds an invoice record to the database with the given data.
	 */
	public static void addInvoice(String invoiceCode, String customerCode, String salesPersonCode, String invoiceDate) {
		//query for adding Invoice
		String addInvoice = ("Insert into Invoice(InvoiceCode,CustomerID,PersonID,InvoiceDate) value(?,?,?,?);");
		//query checks if invoice already exists within system
		String check = ("Select InvoiceID from Invoice inner join Customer on Invoice.CustomerID = Customer.CustomerID inner join Person on Invoice.PersonID = Person.PersonID where InvoiceCode like ? and CustomerCode like ? and PersonCode like ? and InvoiceDate like ?;");
		//Connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			//first must check if invoice already exists
			PreparedStatement ps = conn.prepareStatement(check);
			ps.setString(1,invoiceCode);
			ps.setString(2,customerCode);
			ps.setString(3, salesPersonCode);
			ps.setString(4, invoiceDate);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) { //Invoice found within system
				//cleans up SQL environment
				rs.close();
				ps.close();
				conn.close();
			}else {//new invoice must be created
				//appends query to hold corect information
				ps = conn.prepareStatement(addInvoice);
				ps.setString(1, invoiceCode);
				PreparedStatement ps2 = conn.prepareStatement("Select CustomerID from Customer where CustomerCode like ?;");
				ps2.setString(1, customerCode);
				rs = ps2.executeQuery();
				rs.next();
				int CustomerID = rs.getInt(1);
				ps.setInt(2, CustomerID);
				ps2 = conn.prepareStatement("Select PersonID from Person where PersonCode like ?");
				ps2.setString(1, salesPersonCode);
				rs = ps2.executeQuery();
				rs.next();
				int PersonID = rs.getInt(1);
				ps.setInt(3, PersonID);
				ps.setString(4,invoiceDate);
				//executes Query
				ps.executeUpdate();
				//cleans up SQL environment
				rs.close();
				ps.close();
				conn.close();
			}

		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}
	}
	/**
	 * 12. Adds a particular movieticket (corresponding to <code>productCode</code>
	 * to an invoice corresponding to the provided <code>invoiceCode</code> with
	 * the given number of units
	 */
	public static void addMovieTicketToInvoice(String invoiceCode, String productCode, int quantity) {
		//Query that adds product to invoice
		String addProduct = ("Insert into InvoiceProduct(InvoiceID,ProductID,NumbOfPurchases,TicketCode) value(?,?,?,?);");
		//Query checks if product is already exists within system
		String check = ("Select InvoiceCode,ProductName from InvoiceProduct inner join Invoice on InvoiceProduct.InvoiceID = Invoice.InvoiceID inner join Product on InvoiceProduct.ProductID = Product.ProductID where InvoiceCode like ? and ProductCode like ?;");
		//Connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			//first must check if the invoice already has this MovieTicket
			PreparedStatement ps = conn.prepareStatement(check);
			ps.setString(1,invoiceCode);
			ps.setString(2, productCode);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {	//product already found in invoice
				//cleans up SQL environment
				ps.close();
				rs.close();
				conn.close();
			}else {	//product added to Invoice
				//first gets InvoiceID & ProductID
				String getInvoiceID = ("Select InvoiceID from Invoice where InvoiceCode like '%"+invoiceCode+"%';");
				String getProductID= ("Select ProductID from Product where ProductCode like '%"+productCode+"%';");
				Statement stmt = conn.createStatement();
				rs = stmt.executeQuery(getInvoiceID);
				rs.next();
				int InvoiceID = rs.getInt(1);
				rs = stmt.executeQuery(getProductID);
				rs.next();
				int ProductID = rs.getInt(1);
				//appends Query that adds product to Invoice
				ps = conn.prepareStatement(addProduct);
				ps.setInt(1, InvoiceID);
				ps.setInt(2,ProductID);
				ps.setInt(3, quantity);
				ps.setString(4,"");
				//executes query
				ps.executeUpdate();
				//cleans up SQL environment
				stmt.close();
				ps.close();
				rs.close();
				conn.close();
			}
		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}
	}
	/*
	 * 13. Adds a particular seasonpass (corresponding to <code>productCode</code>
	 * to an invoice corresponding to the provided <code>invoiceCode</code> with
	 * the given begin/end dates
	 */
	public static void addSeasonPassToInvoice(String invoiceCode, String productCode, int quantity) {
		//Query that adds product to invoice
		String addProduct = ("Insert into InvoiceProduct(InvoiceID,ProductID,NumbOfPurchases,TicketCode) value(?,?,?,?);");
		//Query checks if product is already exists within system
		String check = ("Select InvoiceCode,ProductName from InvoiceProduct inner join Invoice on InvoiceProduct.InvoiceID = Invoice.InvoiceID inner join Product on InvoiceProduct.ProductID = Product.ProductID where InvoiceCode like ? and ProductCode like ?;");		
		//Connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			//first must check if the invoice already has this MovieTicket
			PreparedStatement ps = conn.prepareStatement(check);
			ps.setString(1,invoiceCode);
			ps.setString(2, productCode);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {	//product already found in invoice
				//cleans up SQL environment
				ps.close();
				rs.close();
				conn.close();
			}else {	//product added to Invoice
				//first gets InvoiceID & ProductID
				String getInvoiceID = ("Select InvoiceID from Invoice where InvoiceCode like '%"+invoiceCode+"%';");
				String getProductID= ("Select ProductID from Product where ProductCode like '%"+productCode+"%';");
				Statement stmt = conn.createStatement();
				rs = stmt.executeQuery(getInvoiceID);
				rs.next();
				int InvoiceID = rs.getInt(1);
				rs = stmt.executeQuery(getProductID);
				rs.next();
				int ProductID = rs.getInt(1);
				//appends Query that adds product to Invoice
				ps = conn.prepareStatement(addProduct);
				ps.setInt(1, InvoiceID);
				ps.setInt(2,ProductID);
				ps.setInt(3, quantity);
				ps.setString(4,"");
				//executes query
				ps.executeUpdate();
				//cleans up SQL environment
				stmt.close();
				ps.close();
				rs.close();
				conn.close();
			}
		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}
	}
	/**
	 * 14. Adds a particular ParkingPass (corresponding to <code>productCode</code> to an 
	 * invoice corresponding to the provided <code>invoiceCode</code> with the given
	 * number of quantity.
	 * NOTE: ticketCode may be null
	 */
	public static void addParkingPassToInvoice(String invoiceCode, String productCode, int quantity, String ticketCode) {
		//Query that adds product to invoice
		String addProduct = ("Insert into InvoiceProduct(InvoiceID,ProductID,NumbOfPurchases,TicketCode) value(?,?,?,?);");
		//Query checks if product is already exists within system
		String check = ("Select InvoiceCode,ProductName from InvoiceProduct inner join Invoice on InvoiceProduct.InvoiceID = Invoice.InvoiceID inner join Product on InvoiceProduct.ProductID = Product.ProductID where InvoiceCode like ? and ProductCode like ?;");		
		//Connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			//first must check if the invoice already has this MovieTicket
			PreparedStatement ps = conn.prepareStatement(check);
			ps.setString(1,invoiceCode);
			ps.setString(2, productCode);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {	//product already found in invoice	
				//cleans up SQL environment
				ps.close();
				rs.close();
				conn.close();
			}else {	//product added to Invoice
				//first gets InvoiceID & ProductID
				String getInvoiceID = ("Select InvoiceID from Invoice where InvoiceCode like '%"+invoiceCode+"%';");
				String getProductID= ("Select ProductID from Product where ProductCode like '%"+productCode+"%';");
				Statement stmt = conn.createStatement();
				rs = stmt.executeQuery(getInvoiceID);
				rs.next();
				int InvoiceID = rs.getInt(1);
				rs = stmt.executeQuery(getProductID);
				rs.next();
				int ProductID = rs.getInt(1);
				//appends Query that adds product to Invoice
				ps = conn.prepareStatement(addProduct);
				ps.setInt(1, InvoiceID);
				ps.setInt(2,ProductID);
				ps.setInt(3, quantity);
				ps.setString(4,ticketCode);
				//executes query
				ps.executeUpdate();
				//cleans up SQL environment
				stmt.close();
				ps.close();
				rs.close();
				conn.close();
			}
		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}
	}
	/**
	 * 15. Adds a particular refreshment (corresponding to <code>productCode</code> to an 
	 * invoice corresponding to the provided <code>invoiceCode</code> with the given
	 * number of quantity. 
	 */
	public static void addRefreshmentToInvoice(String invoiceCode, String productCode, int quantity) {
		//Query that adds product to invoice
		String addProduct = ("Insert into InvoiceProduct(InvoiceID,ProductID,NumbOfPurchases,TicketCode) value(?,?,?,?);");
		//Query checks if product is already exists within system
		String check = ("Select InvoiceCode,ProductName from InvoiceProduct inner join Invoice on InvoiceProduct.InvoiceID = Invoice.InvoiceID inner join Product on InvoiceProduct.ProductID = Product.ProductID where InvoiceCode like ? and ProductCode like ?;");		
		//Connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			//first must check if the invoice already has this MovieTicket
			PreparedStatement ps = conn.prepareStatement(check);
			ps.setString(1,invoiceCode);
			ps.setString(2, productCode);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {	//product already found in invoice
				//cleans up SQL environment
				ps.close();
				rs.close();
				conn.close();
			}else {	//product added to Invoice
				//first gets InvoiceID & ProductID
				String getInvoiceID = ("Select InvoiceID from Invoice where InvoiceCode like '%"+invoiceCode+"%';");
				String getProductID= ("Select ProductID from Product where ProductCode like '%"+productCode+"%';");
				Statement stmt = conn.createStatement();
				rs = stmt.executeQuery(getInvoiceID);
				rs.next();
				int InvoiceID = rs.getInt(1);
				rs = stmt.executeQuery(getProductID);
				rs.next();
				int ProductID = rs.getInt(1);
				//appends Query that adds product to Invoice
				ps = conn.prepareStatement(addProduct);
				ps.setInt(1, InvoiceID);
				ps.setInt(2,ProductID);
				ps.setInt(3, quantity);
				ps.setString(4,"");
				//executes query
				ps.executeUpdate();
				//cleans up SQL environment
				stmt.close();
				ps.close();
				rs.close();
				conn.close();
			}

		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}
	}
	 //Returns AddressID of for address passed
	static int getAddressID(String street, String city, String state, String zip, String country){
		//AddressID initialized
		int AddressID = 0;
		//String to get AddressID in db
		String addressQuery = ("Select AddressID from Address inner join Country on Address.CountryID=Country.CountryID where Street like ? and Zip like ? and CountryName like ?;");
		//String to create new Address
		//Connection to SQL dataBase
		Connection conn = DatabaseConnection.getConnection();
		try {
			//PrepatedStatement gets Address of address passed
			PreparedStatement ps = conn.prepareStatement(addressQuery);
			ps.setString(1, street);
			ps.setString(2, zip);
			ps.setString(3, country);
			ResultSet rs = ps.executeQuery();
			//Checks if a CountryID has been returned, if CountryID if null then a new Country must be created
			if(rs.next()){
				AddressID = rs.getInt(1);
				//cleans up SQL environment
				ps.close();
			}else {	//no AddressID exists for address that was passed, new one must be created
				//first must check if country exists, sets CountryID
				int CountryID = 0;
				Statement stmt = conn.createStatement();
				rs = stmt.executeQuery("Select CountryID from Country where CountryName like '%"+country+"%';");
				if(rs.next()) {
					CountryID = rs.getInt(1);
				}else {
					//adds new CountryName to Coutry Table
					String addCountry =("Insert into Country(CountryName) value (?);");
					ps = conn.prepareStatement(addCountry);
					ps.setString(1, country);
					ps.executeUpdate();
					//gets CountryID of new CoutryName created
					String getCountryID = ("Select CountryID from Country where CountryName like ?;");
					ps = conn.prepareStatement(getCountryID);
					ps.setString(1,country);
					rs = ps.executeQuery();
					rs.next();
					CountryID = rs.getInt(1); //stores new CountryID
				}
				//creates the AddressID of new Address created
				String createNewAddress = ("Insert into Address(Street,City,State,Zip,CountryID) value (?,?,?,?,?);");
				ps = conn.prepareStatement(createNewAddress);
				ps.setString(1, street);
				ps.setString(2, city);
				ps.setString(3, state);
				ps.setString(4, zip);
				ps.setInt(5, CountryID);
				ps.executeUpdate();
				//gets AddressID of address created above
				ps = conn.prepareStatement(addressQuery);
				ps.setString(1, street);
				ps.setString(2, zip);
				ps.setString(3, country);
				rs = ps.executeQuery();
				rs.next();
				AddressID = rs.getInt(1); //saves the AddessID just created
				//cleans up SQL environment
				stmt.close();
			}
			//cleans up SQL environment
			ps.close();
			rs.close();
			conn.close();
		}
		//catches error, invalid Query
		catch(SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);	
		}
		return AddressID; //return appropriate AddressID
	}
}