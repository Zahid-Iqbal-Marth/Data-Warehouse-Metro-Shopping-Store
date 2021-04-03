package Project;
import java.util.Date;

public class TransactionData {
	
	String TRANSACTION_ID;
	String PRODUCT_ID;
	String CUSTOMER_ID;
	String CUSTOMER_NAME;
	String STORE_ID;
	String STORE_NAME;
	Date T_DATE;
	int QUANTITY;
	
	TransactionData(String T_ID, String P_ID, String C_ID, String C_Name, String S_ID, String S_Name, Date T_Date, int Quantity){
		this.TRANSACTION_ID = T_ID;
		this.PRODUCT_ID = P_ID;
		this.CUSTOMER_ID = C_ID;
		this.CUSTOMER_NAME = C_Name;
		this.STORE_ID = S_ID;
		this.STORE_NAME = S_Name;
		this.T_DATE = T_Date;
		this.QUANTITY = Quantity;		
	}
	
	void display(){
		
		System.out.println(TRANSACTION_ID+" "+PRODUCT_ID+" "+CUSTOMER_ID+" "+CUSTOMER_NAME+" "+ STORE_ID+" "+STORE_NAME+" "+T_DATE+" "+QUANTITY);
		
	}
	
}
