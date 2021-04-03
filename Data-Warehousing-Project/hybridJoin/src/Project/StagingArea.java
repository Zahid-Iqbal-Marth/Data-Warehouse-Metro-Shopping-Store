package Project;


//hashmaps.remove(key)

// project Imports / custom
import Project.DLQ.Node;
import Project.DLQ;
import Project.TransactionData;
import Project.MasterData;











import java.util.List;
//Imports for SQL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// ArrayList import
import java.util.ArrayList;



import java.util.Arrays;
import java.sql.Date;  



import java.util.StringTokenizer;










//Imports for apache hashmaps
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.util.Map.Entry;

public class StagingArea {

	class HashMapEntry {
		
		TransactionData td = null;
		Node pointer_to_queue = null;
	}
	
	
	DLQ queue = new DLQ();
    MultiValuedMap<String, HashMapEntry> hashmaps = new ArrayListValuedHashMap<>();
    ArrayList<MasterData> master_data = new ArrayList<MasterData>();
	
	int max_hashmaps_entries = 1000;
	int max_masterdata_buffer_size = 20;
	int starting_index = 0;
	int fifty_records = 0;
	
	public void read_transaction_data(Statement stmt, String table){

		
		// enter query and read data
		
		try {
			
			// add condition of how many are read and how many we'll read next time
			int size = max_hashmaps_entries - hashmaps.size();
			String query = "SELECT * FROM Transactions LIMIT " + String.valueOf(this.starting_index) +","+ String.valueOf(size) +";";
			ResultSet results=stmt.executeQuery(query);
			while(results.next()){ 
				
				String transaction_id = results.getString(1);
				String product_id = results.getString(2);
				String customer_id = results.getString(3);
				String customer_name = results.getString(4);
				String store_id = results.getString(5);
				String store_name = results.getString(6);
				Date t_date = results.getDate(7);
				int quantity = results.getInt(8);
				
				TransactionData td = new TransactionData(transaction_id, product_id, customer_id, customer_name, store_id, store_name, t_date, quantity);

				Node newNode = this.queue.enqueue(product_id);
				
				HashMapEntry hme = new HashMapEntry();
				
				hme.td = td;
				hme.pointer_to_queue = newNode;
				
				this.hashmaps.put(product_id, hme);
				
				this.starting_index += 1;
			}
			

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public int read_master_data(Statement stmt, String table, String p_id){
		

		int md_record_index = master_data.size();
		try {
			

			// add condition of how many are read and how many we'll read next time

			int size = max_masterdata_buffer_size - master_data.size();
			StringTokenizer st1 = new StringTokenizer(p_id, "-"); 
 
			String _ = st1.nextToken();
			int min_id = Integer.parseInt(st1.nextToken());
			String max_id = "P-"+String.valueOf(min_id + size - 1);

			
			
			String query = "SELECT * FROM Masterdata WHERE product_id BETWEEN '" + p_id +"' and '"+ max_id +"' ;";
			ResultSet results=stmt.executeQuery(query);
			while(results.next()){ 
				
				String product_id = results.getString(1);
				String product_name = results.getString(2);
				String supplier_id = results.getString(3);
				String supplier_name = results.getString(4);
				float price = results.getFloat(5);
				
				MasterData md = new MasterData(product_id, product_name, supplier_id, supplier_name, price);

				this.master_data.add(md);
			}
			

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return md_record_index;
	}
	
	public List<Object>  setup_database_conn(String database){
		
		Statement stmt = null;
		Connection con = null;
		try{  
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/" + database;
			con=DriverManager.getConnection(url, "root", "root");  
			stmt=con.createStatement();  

		}
		catch(Exception e){ System.out.println(e);}  
		
		return Arrays.asList(stmt, con);
	}
	
	
	
	public void send_data_to_metro_DW(TransactionData td, MasterData masterData, Statement metro_DW_statement) {
		// TODO Auto-generated method stub
		
		if (this.fifty_records < 50){
			this.fifty_records += 1;
			System.out.println("Record : " + this.fifty_records);
			td.display();
			masterData.display();
			
		}
		
		List<String> quries = new ArrayList<String>();
		quries.add("CALL metro_DW.INSERT_PRODUCT('" + masterData.PRODUCT_ID + "', \"" + masterData.PRODUCT_NAME + "\" );");
		quries.add("CALL metro_DW.INSERT_CUSTOMER('" + td.CUSTOMER_ID + "', \"" + td.CUSTOMER_NAME + "\" );");
		quries.add("CALL metro_DW.INSERT_SUPPLIER('" + masterData.SUPPLIER_ID + "', \"" + masterData.SUPPLIER_NAME + "\" );");
		quries.add("CALL metro_DW.INSERT_STORE('" + td.STORE_ID + "', \"" + td.STORE_NAME + "\" );");
		
		
	
		
		quries.add("CALL metro_DW.INSERT_TDATE('" + td.T_DATE + "', DAY('" + td.T_DATE + "') " + ", DAYOFWEEK('" + td.T_DATE + "') " + ", MONTH('" + td.T_DATE + "')" + ", QUARTER('" + td.T_DATE + "') " + ", YEAR('" + td.T_DATE + "'));" );
		
		
		quries.add("INSERT INTO metro_DW.SALE VALUES('" + td.TRANSACTION_ID + "', '"  + td.CUSTOMER_ID + "', '" + td.PRODUCT_ID + "', '" + masterData.SUPPLIER_ID + "', '"  + td.STORE_ID + "', '"   + td.T_DATE + "', "  + td.QUANTITY + ", "  + masterData.PRICE + ", "  + td.QUANTITY*masterData.PRICE  + ");");
		
		
		try {
			for(int x = 0; x < quries.size(); x += 1){
//				System.out.println(x);
				metro_DW_statement.execute(quries.get(x));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		System.out.println(td.PRODUCT_ID + masterData.PRODUCT_ID); 
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		StagingArea SA = new StagingArea();
		
		//data_base conn
		List<Object> db_connection_vars = SA.setup_database_conn("metro_db");
		
		Statement db_statement = (Statement) db_connection_vars.get(0);
		Connection db_conn = (Connection) db_connection_vars.get(1);
		
		//metro_DW conn
		List<Object> metro_DW_connection_vars = SA.setup_database_conn("metro_dw");
		
		Statement metro_DW_statement = (Statement) metro_DW_connection_vars.get(0);
		Connection metro_DW_conn = (Connection) metro_DW_connection_vars.get(1);
	
		
		SA.read_transaction_data(db_statement, "TRANSACTIONS");
		
	
		while(!SA.queue.isEmpty()){
			
			String product_id = SA.queue.dequeue();
			int md_record_index = SA.read_master_data(db_statement, "MASTERDATA", product_id);
			 
			//1
			//iter over all master data values
			// if matched then search in hashmap with same key and 
			// then attach/join master data and hashmap values and send it to 
			// send_data_to_metro_DW() function.
			//after that remove the entry from queue too.
	        
			boolean found = false;
			//iter over all master data values
            for (String key: SA.hashmaps.keySet()) {
                
                if (key == product_id){ //if found
                	
                	found = true;
                	
                	for (HashMapEntry entry: SA.hashmaps.get(key)) {
                		// then attach/join master data and hashmap values and send it to metro_DW 
                		SA.send_data_to_metro_DW(entry.td, SA.master_data.get(md_record_index), metro_DW_statement);
                		SA.queue.delete(entry.pointer_to_queue);
                		//after that remove the entry from queue .
                    }
                    if (found){
                    	//remove the entry from hashmaps.
                		SA.hashmaps.remove(key);
                		SA.master_data.remove(md_record_index);
                		break;
                    }
                }

            }
			
			
			//2
			//now iter over remaining  hashmaps values 
			// match it with master data buffer values and if found
			//join and sent it to send_data_to_metro_DW() function.
			//after that remove the entry from queue too.
			
	        found = false;
	      //now iter over remaining  hashmaps values 
            for (String key: SA.hashmaps.keySet()) {
                
            	for (int i = 0; i < SA.master_data.size(); i++){          	
            		// match it with master data buffer values
	                if (key == SA.master_data.get(i).PRODUCT_ID){
	                	//if found
	                	found = true;
	                	
	                	for (HashMapEntry entry: SA.hashmaps.get(key)) {
	                		//join and sent it to send_data_to_metro_DW() function.
	                		SA.send_data_to_metro_DW(entry.td, SA.master_data.get(i), metro_DW_statement);
	                		SA.queue.delete(entry.pointer_to_queue); //after that remove the entry from queue
	                    } 	            
	                	
	                	if(found){ // remove the entry from masterdata buffer and hashmaps
		            		SA.master_data.remove(i);
		            		SA.hashmaps.remove(key);
		            		break;
	                	}
	                	
	                }
            	}
              
            }
	        
	        
			SA.read_transaction_data(db_statement, "TRANSACTIONS");
			
			
		}



		try {
			db_conn.close();
			metro_DW_conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
