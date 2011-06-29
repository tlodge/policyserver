package datasource;

import java.util.ArrayList;

import models.Website;

public class LeaseData {

	private static final String DELIMETER = "\\<\\|\\>";
	private static LeaseData sharedData = null;
	private long last;
	
	private LeaseData(){
		init();
	}
	
	private void init(){
		try{
			
		}catch(Exception e){
			System.err.println("error connecting " + e.getMessage());
		}
	}
	
	public static LeaseData sharedData(){
		if (sharedData == null){
			sharedData = new LeaseData();
		}
		return sharedData;
	}
	
	public boolean arethesame(String macaddr, String IPAddr){
		return true;
	}
	
	public String lookup(String macaddr){
		return "10.2.0.1";
	}
	
	public void parse(String data){
	
	}
}
