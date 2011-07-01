package models.policy.queries;

import java.util.ArrayList;

import models.policy.CallbackURL;
import models.policy.Policy;

public abstract class Query {

	protected ArrayList<CallbackURL> callbackurls = null;
	protected String policyid = null;
	protected String query = null;
	protected String subject;
	protected DataProcessor processor;
	
	public Query(Policy p){
		System.err.println("Registering policy id " + p.identity);
		this.policyid = p.identity;
		callbackurls = new ArrayList<CallbackURL>();
		callbackurls.add(new CallbackURL(String.format("http://localhost:9000/notify/policyclient/%s",p.identity)));
	}
	
	public abstract void process (String data);
		
	public abstract String toString();
	
}
