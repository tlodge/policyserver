package homework.handler;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import datasource.LeaseData;
import datasource.URLData;

import models.Callback;
import models.URLConditionCallback;
import models.Website;
import models.policy.Action;
import models.policy.Policy;

public class URLConditionHandler{
	
	//policyid, conditionsCallback
	private Hashtable<String, URLConditionCallback> conditionsCallback;

	public URLConditionHandler(){
		conditionsCallback = new Hashtable<String,URLConditionCallback>();
	}

	public void addConditionsAction(String policyid, URLConditionCallback ca){
		conditionsCallback.put(policyid, ca);
	}

	public boolean register(Policy policy){
		
		HashSet hs = new HashSet();

		Hashtable h = policy.condition.arguments;
		
		String[] sites = (String[]) h.get("sites");
		
		for(String site : sites){
			hs.add(site);
		}
			
		URL callbackurl = null;
		
		try{
			if (policy.action.type.equals("notify")){
				callbackurl= new URL("http://localhost:9000/notify/push");			
			}else if (policy.action.type.equals("block")){
				callbackurl= new URL("http://localhost:9000/block/" + policy.action.subject);
			}else{
				System.err.println("unknown action!");
				return false;
			}
			addConditionsAction(policy.identity, new URLConditionCallback(policy.subject, hs, new Callback(callbackurl)));
			System.err.println("registed condtion event....");
			return true;
			
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		return false;
	}


	public void handleData(){
		for (Website site : URLData.sharedData().getSites()){
			checkAgainstConditions(site);
		}
	}

	private void checkAgainstConditions(Website site){
		for (URLConditionCallback ca : conditionsCallback.values()){
			System.err.println("checking site " + site.url);
			if (LeaseData.sharedData().arethesame(ca.getSubject(), site.source) && ca.getConditions().contains(site.url)){
				System.err.println("FIRED ON " + site);
				new Thread(ca.getCallback()).run();
			}
		}
	}
}
