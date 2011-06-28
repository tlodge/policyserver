package homework.handler;

import java.net.URL;

import models.Callback;
import models.URLConditionCallback;
import models.policy.Policy;


public class ConditionHandler {

	private static ConditionHandler sharedHandler = null;

	public enum TableType{URL, BANDWIDTH, LEASE, LINK};
	
	private final URLConditionHandler urlConditionHandler = new URLConditionHandler();
	
	private ConditionHandler(){
		init();
	}

	private void init(){
		try{
			
		}catch(Exception e){
			System.err.println("error connecting " + e.getMessage());
		}
	}

	public static ConditionHandler sharedHandler(){
		if (sharedHandler == null){
			sharedHandler = new ConditionHandler();
		}
		return sharedHandler;
	}
	
	public void handleData(TableType t){
		if (t == TableType.URL){
			urlConditionHandler.handleData();
		}
	}
	
	public boolean registerPolicy(Policy policy){
		if (policy.condition.type.equals("visiting")){
			System.err.println("REGISTERING VISITING POLICY...");
			return urlConditionHandler.register(policy);
		}
		return true;
	}
}
