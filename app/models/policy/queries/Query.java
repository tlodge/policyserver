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
		this.policyid = p.identity;
		callbackurls = new ArrayList<CallbackURL>();
	}
	
	public abstract void process (String data);
		
	public abstract String toString();
	
}
