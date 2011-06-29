package models.policy.queries;

import java.util.HashSet;

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
		
		String[] rows = data.split("\n");
    	
		last = 0;
    	
    	if (rows.length > 2){
    		for (int i = 2; i < rows.length; i++){
    			String row[] = rows[i].split(DELIMETER);
    			
    			final int start = row[0].indexOf('@');
				final int end = row[0].indexOf('@', start + 1);
				final String time = row[0].substring(start + 1, end);
				final long timeLong = Long.parseLong(time, 16);
				
				last = Math.max(timeLong/1000000, last) + 1; //to ensure we don't get sent the last record again..
    			
				if (sites.contains(row[2])){
					if (!triggered)
						System.err.println("triggering on " + row[2]);
    				triggered = true;
    			}		
    		}
    	}	
	}
}
