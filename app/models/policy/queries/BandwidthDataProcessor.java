package models.policy.queries;

import homework.PollingThread;

import play.Logger;



public class BandwidthDataProcessor extends DataProcessor {

	private long triggerbytes;
	private String ipaddr;
	

	public BandwidthDataProcessor(long tb, String ipaddr){
		this.triggerbytes = tb;	
		this.ipaddr = ipaddr;
	}
	
	@Override
	public void process(String data) {
		String[] rows = data.split("\n");
    	
		last = 0;
    	
    	if (rows.length > 2){
    		
    		for (int i = 2; i < rows.length; i++){
    			System.err.println(rows[i]);
    			String row[] = rows[i].split(DELIMETER);
    			if (row[1].equals(ipaddr)){
    				if (Long.valueOf(row[0]) >= triggerbytes){
    					Logger.info("bandwidth triggered");
    					triggered = true;
    				}
    			}
    		}
    	}	
	}
}
