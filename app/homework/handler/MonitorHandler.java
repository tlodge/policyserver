package homework.handler;

import models.policy.Policy;


public class MonitorHandler {

	private static MonitorHandler sharedHandler = null;

	private MonitorHandler(){
		init();
	}

	private void init(){
		try{

		}catch(Exception e){
			System.err.println("error connecting " + e.getMessage());
		}
	}

	public static MonitorHandler sharedHandler(){
		if (sharedHandler == null){
			sharedHandler = new MonitorHandler();
		}
		return sharedHandler;
	}

	public static boolean registerPolicy(Policy policy){
		return true;
	}

}
