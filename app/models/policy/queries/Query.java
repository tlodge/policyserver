package models.policy.queries;

import homework.PollingThread;

import java.util.ArrayList;


import models.policy.CallbackURL;
import models.policy.Policy;
import play.Logger;

public abstract class Query {

	protected ArrayList<CallbackURL> callbackurls = null;
	protected String policyid = null;
	protected String query = null;
	protected String subject;
	protected DataProcessor processor;
	protected boolean triggered;
	
	public Query(Policy p){
		Logger.info("Registering policy id " + p.identity);
		this.policyid = p.identity;
		callbackurls = new ArrayList<CallbackURL>();
		
		/*
		 * By default, will send a push message to the device.
		 */
		callbackurls.add(new CallbackURL(String.format("http://localhost:8080/policyserver/notify/policyclient/%s",p.identity)));
		//callbackurls.add(new CallbackURL(String.format("http://localhost:8080/notify/policyclient/%s",p.identity)));
		
		triggered = false;
	}
	
	public String policyId(){
		return policyid;
	}
	
	public boolean triggered(){
		return triggered;
	}
	
	public abstract void process (String data);
		
	public abstract String toString();
	
}
