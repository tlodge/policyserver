package datasource;

import java.util.ArrayList;
import java.util.Hashtable;
import play.Logger;
import models.Website;

public class LeaseData {

	private static final String DELIMETER = "\\<\\|\\>";
	private static LeaseData sharedData = null;
	private static Hashtable<String,String> mactoip;
	private long last;
	
	private LeaseData(){
		init();
	}
	
	private void init(){
		
		mactoip = new Hashtable<String, String>();
		
	}
	
	public static LeaseData sharedData(){
		if (sharedData == null){
			sharedData = new LeaseData();
		}
		return sharedData;
	}
	
	
	public String getQuery(){
		
		
		if (last == 0){
			return String.format("SQL:select timestamp, macaddr, ipaddr, action from Leases WHERE action contains \"add\" ORDER BY timestamp asc");
		}else{
			final String s = String.format("@%016x@", last * 1000000);
			return String.format("SQL:select timestamp, macaddr, ipaddr, action from Leases [since %s] WHERE action contains \"add\" ORDER BY timestamp asc", s);
		}
	}
	
	//public boolean arethesame(String macaddr, String IPAddr){
	//	return true;
	//}
	
	public String lookup(String macaddr){
		
		return mactoip.get(macaddr.toLowerCase());
		//return "10.2.2.1";
	}
	
	public void parse(String data){
		if (data == null)
			return;
		
		String[] rows = data.split("\n");
    	
    	if (rows.length > 2){
    		
    		for (int i = 2; i < rows.length; i++){
    			System.err.println(rows[i]);
    			String row[] = rows[i].split(DELIMETER);
    			last = Util.convertTs(row[0]) + 1;
    			if (row[3].equals("add")){
    				Logger.info("adding lease " + row[1].toLowerCase() + "  " + row[2]);
    				mactoip.put(row[1].toLowerCase(), row[2]);
    			}
    			
    		}
    	}	
	}
}
