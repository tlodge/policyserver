package models.policy.queries;

import java.util.Hashtable;

import policy.PolicyManager;

import models.policy.CallbackURL;
import models.policy.Policy;

import datasource.LeaseData;

public class BandwidthQuery extends Query{

	
	//public static final long timeRange = System.currentTimeMillis() -  (long) (1000 * 60 * 5); //last 5 minutes;

	//HACK FOR NOW...
	public static final String subnet = "10.2.0.";
	
	public static final int timeRange = (60 * 5); //last 5 minutes;

	public static final long BANDWIDTH_LIMIT = 5 * 1024 * 1024;
		
	/*
	 * This query gets all incoming bytes for each machine that is not from the local subnet
	 */
	public BandwidthQuery(Policy p) {
		
		super(p);
		
		subject = p.subject;
		
		Object percent =  p.condition.arguments.get("percentage");
		
		float percentage = Float.valueOf(String.valueOf(percent));
			
		processor = new BandwidthDataProcessor((long) (percentage/100 * BANDWIDTH_LIMIT), LeaseData.sharedData().lookup(subject));
			
		callbackurls.add(new CallbackURL(p));
		query = "SQL:select sum(nbytes), daddr from Flows [timeframe] where daddr contains \"[deviceaddr]\" and saddr notcontains \"[deviceaddr]\" group by daddr";
	}

	@Override
	public void process(String data) {
		processor.process(data);
		
		if (processor.triggered()){
			
			for (CallbackURL c : callbackurls)
				c.call();
			
			triggered = true;
			//PolicyManager.sharedManager().remove(policyid);
		}		
		
	}


	@Override
	public String toString() {
		//final String s = String.format("@%016x@", timeRange * 1000000);
		String thequery = query.replace("[timeframe]", "[range " + timeRange + " seconds]");
		//String ipaddr = null;
		
		//if ( (ipaddr = LeaseData.sharedData().lookup(subject)) != null){
			thequery = thequery.replace("[deviceaddr]", subnet);//ipaddr);
			return thequery;
		//}
		//return null;
	}

}
