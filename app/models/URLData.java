package models;

import java.util.ArrayList;


public class URLData {
	
	private static final String DELIMETER = "\\<\\|\\>";
	private static URLData sharedData = null;
	private ArrayList<Website> sites;
	private long last;
	
	private URLData(){
		init();
	}
	
	private void init(){
		try{
			sites = new ArrayList<Website>();
		}catch(Exception e){
			System.err.println("error connecting " + e.getMessage());
		}
	}
	
	public void parse(String data){
		sites.clear();
		String[] rows = data.split("\n");
    	String lastsite = "";
    	last = 0;
    	
    	if (rows.length > 2){
    		for (int i = 2; i < rows.length; i++){
    			String row[] = rows[i].split(DELIMETER);
    			final int start = row[0].indexOf('@');
				final int end = row[0].indexOf('@', start + 1);
				final String time = row[0].substring(start + 1, end);
				final long timeLong = Long.parseLong(time, 16);
				
				last = Math.max(timeLong/1000000, last) + 1; //to ensure we don't get sent the last record again..
    			
				if (!lastsite.equals(row[1])){
					System.err.println(row[0] + " " + row[1]);
    				sites.add(new Website(row[1], timeLong/1000000));
    				lastsite = row[1];
    			}
    		}
    	}	
	}
	
	public ArrayList<Website> getSites(){
		return sites;
	}
	
	public long getLatestTs(){
		return last;
	}
	
	public static URLData sharedData(){
		if (sharedData == null){
			sharedData = new URLData();
		}
		return sharedData;
	}
}
