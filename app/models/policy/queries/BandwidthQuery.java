package models.policy.queries;

import java.util.Hashtable;

import models.policy.CallbackURL;
import models.policy.Policy;

import datasource.LeaseData;

public class BandwidthQuery extends Query{

	long startTime = System.currentTimeMillis() -  (long) (1000 * 60 * 60  * 0.5); //last 1/2 hour;
	final long BANDWIDTH_LIMIT = 5 * 1024 * 1024;
		
	public BandwidthQuery(Policy p) {
		super(p);
		subject = p.subject;
		
		Object percent =  p.condition.arguments.get("percentage");
		
		float percentage = Float.valueOf(String.valueOf(percent));
		
		System.err.println("percentage is " + percentage);
			
		processor = new BandwidthDataProcessor((long) (percentage/100 * BANDWIDTH_LIMIT), LeaseData.sharedData().lookup(subject));
			
		callbackurls.add(new CallbackURL(p));
		query = "SQL:select sum(nbytes), daddr from Flows [timeframe] where daddr contains \"[deviceaddr]\" and saddr notcontains \"[deviceaddr]\" group by daddr";
	}

	@Override
	public void process(String data) {
		processor.process(data);
		
		if (processor.triggered()){
			processor.reset();
			
			for (CallbackURL c : callbackurls)
				c.call();
			
		}		
		
	}

	@Override
	public String toString() {
		final String s = String.format("@%016x@", startTime * 1000000);
		String thequery = query.replace("[timeframe]", "[since " + s + "]");
		thequery = thequery.replace("[deviceaddr]", LeaseData.sharedData().lookup(subject));
		return thequery;
	}

}
