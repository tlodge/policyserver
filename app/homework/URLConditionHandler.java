package homework;
import java.util.ArrayList;

import models.URLConditionAction;

public class URLConditionHandler{
	
	
	private ArrayList<URLConditionAction> conditionsAction;
	private static final String DELIMETER = "\\<\\|\\>";
	
	public URLConditionHandler(){
		conditionsAction = new ArrayList<URLConditionAction>();
	}
	
	public void addConditionsAction(URLConditionAction ca){
		conditionsAction.add(ca);
	}

	public long handleData(String data){
		String[] rows = data.split("\n");
    	String last = "";
    	long ts = 0;
    	
    	if (rows.length > 2){
    		for (int i = 2; i < rows.length; i++){
    			
    			String row[] = rows[i].split(DELIMETER);
    			
    			if (!last.equals(row[1])){
    				final int start = row[0].indexOf('@');
    				final int end = row[0].indexOf('@', start + 1);
    				final String time = row[0].substring(start + 1, end);
    				final long timeLong = Long.parseLong(time, 16);
    				checkAgainstConditions(row[1]);
    				ts = timeLong/1000000;
    				last = row[1];
    			}
    		}
    	}	
    	return ts;
	}
	
	private void checkAgainstConditions(String site){
		for (URLConditionAction ca : conditionsAction){
			if (ca.getConditions().contains(site)){
				System.err.println("FIRED ON " + site);
				new Thread(ca.getAction()).run();
			}
		}
	}
}
