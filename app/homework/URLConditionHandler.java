package homework;
import java.util.ArrayList;

import models.URLConditionAction;
import models.URLData;
import models.Website;

public class URLConditionHandler{
	
	
	private ArrayList<URLConditionAction> conditionsAction;
	
	private static final String DELIMETER = "\\<\\|\\>";
	
	public URLConditionHandler(){
		conditionsAction = new ArrayList<URLConditionAction>();
	}
	
	public void addConditionsAction(URLConditionAction ca){
		conditionsAction.add(ca);
	}

	public void handleData(){
		for (Website site : URLData.sharedData().getSites()){
			checkAgainstConditions(site.url);
		}
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
