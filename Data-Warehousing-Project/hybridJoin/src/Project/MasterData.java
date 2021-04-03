package Project;

public class MasterData {
	
	String PRODUCT_ID;
	String PRODUCT_NAME;
	String SUPPLIER_ID;
	String SUPPLIER_NAME;
	float PRICE;
	
	MasterData(String P_ID, String P_Name, String S_ID, String S_Name, float Price){
	
		this.PRODUCT_ID = P_ID;
		this.PRODUCT_NAME = P_Name;
		this.SUPPLIER_ID = S_ID;
		this.SUPPLIER_NAME = S_Name;
		this.PRICE = Price;		
		
	}
	
	void display(){
		
		System.out.println(PRODUCT_ID+" "+PRODUCT_NAME+" "+SUPPLIER_ID+" "+SUPPLIER_NAME+" "+ PRICE);
		
	}

}
