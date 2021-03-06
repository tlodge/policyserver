package models.policy.queries;

import homework.PollingThread;

import java.util.HashSet;
import play.Logger;

import datasource.Util;

import models.Website;

public class URLDataProcessor extends DataProcessor{

	public HashSet<String> sites;

	
	public URLDataProcessor(String[] mysites){
		sites = new HashSet<String>();
		for (String s : mysites){
			sites.add(s);
		}
	}
	
	@Override
	public void process(String data) {
		
		if (data == null)
			return;
		
		
		String[] rows = data.split("\n");
    	
		last = 0L;
    	
    	if (rows.length > 2){
    		for (int i = 2; i < rows.length; i++){
    			
    			String row[] = rows[i].split(DELIMETER);
    			final long timeLong = Util.convertTs(row[0]) + 1;
    					
				last = Math.max(timeLong, last) + 1; //to ensure we don't get sent the last record again..
    			
				if (sites.contains(row[2])){
					if (!triggered){
						Logger.info("triggering on %s", row[2]);
    					triggered = true;
					}
    			}		
    		}
    	}	
	}
}
