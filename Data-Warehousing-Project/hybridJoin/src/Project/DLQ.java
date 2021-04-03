package Project;

public class DLQ {
	
    class Node{  
        String item;  
        Node previous;  
        Node next;  
    }  
    private Node head = null;
    private Node tail = null;
    
    public Node enqueue(String item){

        Node newNode = new Node();  
        newNode.item = item;
        
        // if queue is empty
        if(head == null) {  
            head = newNode;  
            tail = newNode;
            head.previous = null;  
            tail.next = null;  
        } // insert at end/tail of the queue  
        else {   
            tail.next = newNode;  
            newNode.previous = tail;  
            tail = newNode;  
            tail.next = null;  
        }    	
    	return newNode;
    }
    
    public String dequeue(){
    	
    	if(head != null){
    		return head.item;
    	}
    	return "";
    	
    }
    
    public void delete(Node node_to_delete){
    	
    	if((node_to_delete == head && node_to_delete == tail) || (head == tail && head == null) ){
    		
    		head = tail = null;
    		
    	}
    	else {
    		
        	if(node_to_delete != head && node_to_delete != tail){
            	node_to_delete.previous.next = node_to_delete.next;
            	node_to_delete.next.previous = node_to_delete.previous;    		
        	} 
        	else if (node_to_delete == tail){
            	tail = tail.previous;
            	tail.next = null;
        	}
        	else if (node_to_delete == head){
        		head = head.next;
        		head.previous = null;
        	}
    	}
    }

    
  //print all the nodes of doubly linked list  
    public void printNodes() {  
        //Node current will point to head  
        Node current = head;  
        if(head == null) {  
            System.out.println("\nDoubly linked list is empty");  
            return;  
        }  
        System.out.println("\nNodes of doubly linked list: ");  
        while(current != null) {  
            //Print each node and then go to next.  
            System.out.print(current.item + " ");  
            current = current.next;  
        }  
    }

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		if (head == tail && tail == null)
			return true;
		return false;
	}
    
    
    
}
