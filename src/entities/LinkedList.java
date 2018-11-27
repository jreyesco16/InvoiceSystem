package entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LinkedList{

	//attributes
	private Node start;
	private Node end;
	private int size;

	//construcotor
	public LinkedList() {
		this.start=null;
		this.end=null;
		this.size=0;
	}

	//getters and setters
	public boolean isEmpty() {
		return start == null;
	}
	public int getSize() {
		return size;
	}	
	public Node getStart() {
		return start;
	}
	public void setStart(Node start) {
		this.start = start;
	}
	public Node getEnd() {
		return end;
	}
	public void setEnd(Node end) {
		this.end = end;
	}
	public void setSize(int size) {
		this.size = size;
	}
	//adds object to LinkedList    ONLY CREATES LIST ON INVOICES
	public void addToList(Invoice invoice){	//Invoice object added so list is incremented by 1
		size++;
		Node current = this.start;
		Node previousNode = null;
		Node newNode = new Node(invoice);
		int currentCompare;
		//If List is empty
		if (this.isEmpty()) {
			this.start = newNode;
		} else {
			for (int i = 0; i < size - 1; i++) {
				// Goes at the beginning
				if (i == 0) {
					//checks if the invoice total is bigger or smaller than the invoice total in the linked list
					currentCompare = invoice.compare((Invoice) current.getData(), invoice);
					if (currentCompare < 0) {
						this.start = newNode;
						this.start.setNextNode(current);
						i = size;
					} else if (currentCompare == 0) {
						current.setNextNode(newNode);
						i = size;
					}
					// Goes at the end
				} else if (i == (size - 2)) {
					//checks if the invoice total is bigger or smaller than the invoice total in the linked list
					currentCompare = invoice.compare((Invoice) current.getData(), invoice);
					// case for adding Node after last element of list
					if (currentCompare > 0) { 
						this.end = newNode;
						current.setNextNode(this.end);
						i = size;
					} else if (currentCompare < 0) {
						previousNode.setNextNode(newNode);
						newNode.setNextNode(current);
						i = size;
					} else {
						this.end = newNode;
						current.setNextNode(this.end);
						i = size;
					}
					// goes in the middle
				} else {
					//checks if the invoice total is bigger or smaller than the invoice total in the linked list
					currentCompare = invoice.compare((Invoice) current.getData(), invoice);
					if (currentCompare < 0) {
						previousNode.setNextNode(newNode);
						newNode.setNextNode(current);
						i = size;
					} else if (currentCompare == 0) {
						current.setNextNode(newNode);
						i = size;
					}
				}
				previousNode = current;
				current = current.getNext();
			}
		}
	}	
	public Object getThing(String code) {
		//Initialized the Person that will have to be returned
		Object o = null;
		//aNode will be the Node used to loop through LinkedList
		Node aNode = this.start;
		//loops through LinkedList
		for(int i = 0; i < this.size; i++){
			//condition when passed an InvoiceCode
			if(aNode.getData() instanceof Invoice) {
				//gets Data(Person) from current Node
				Invoice invoice = (Invoice) aNode.getData();
				if(code.equals(invoice.getSalesPerson().getPersonCode())) {	//condition when PersonCode equals one of the PersonCodes in LinkedList
					o = (Person) aNode.getData();
				}else if(code.equals(invoice.getCustomer().getCustomerCode())) {	//condition when CustomerCode equals on of the CustomerCodes in LinkedList
					o = (Customer) invoice.getCustomer();
				}else if(!(code.equals(invoice.getSalesPerson().getPersonCode()) || code.equals(invoice.getCustomer().getCustomerCode()))) {	//condition for getting product
					List<Product> product = invoice.getProduct();
					for(Product p: product) {
						if(p.getProductCode().equals(code)) {
							o = p;
						}
					}
				}
			}else if(aNode.getData() instanceof Person) {	//condition for PersonCode
				Person p = (Person)aNode.getData();	
				if(p.getPersonCode().equals(code)) {
					o = p;
				}
			}else if(aNode.getData() instanceof Customer) {
				Customer c = (Customer)aNode.getData();
				if(c.getCustomerCode().equals(code)) {
					o = c;
				}
			}else {
				System.out.println("Code enterd invalid");
			}
			//moves to next Node
			aNode = aNode.getNext();
		}
		return o;
	}
	//For testing, converts object from Node to a string   MAKE STRING A STRINGBUILDER
	public StringBuilder linkedListString() {
		StringBuilder linkString = new StringBuilder();
		Node aNode = this.start;
		for(int i = 0; i < this.size; i++){
			String string = aNode.getData().toString();
			linkString.append(string+"\n");
			aNode = aNode.getNext();
		}
		return linkString;
	}
}
