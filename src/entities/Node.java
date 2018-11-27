package entities;

public class Node {

	//attributes
	private Object obj;
	private Node nextNode;

	//constructor
	public Node(Object obj) {

		this.nextNode = null;
		this.obj = obj;
	}

	//getters and setters
	public void setNextNode(Node nextNode) {
		this.nextNode = nextNode;
	}
	public Node getNext() {
		return this.nextNode;
	}
	public Object getData() {
		return this.obj;
	}
}