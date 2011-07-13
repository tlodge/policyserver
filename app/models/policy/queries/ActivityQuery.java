package models.policy.queries;

import java.text.SimpleDateFormat;

import policy.PolicyManager;

import datasource.LeaseData;
import models.policy.CallbackURL;
import models.policy.Policy;

public class ActivityQuery extends Query {

	
	
	public ActivityQuery(Policy p) {
		super(p);
		
		String from = (String) p.condition.arguments.get("from");
		String to = (String) p.condition.arguments.get("to");
		subject = p.subject;
		System.err.println("from is " + from + " to is " + to);
		processor = new ActivityDataProcessor(from,to,LeaseData.sharedData().lookup(p.subject));
		callbackurls.add(new CallbackURL(p));
		
		query = String.format("SQL:select timestamp, saddr from Flows [timeframe] where saddr contains [deviceaddr]");		
		
	}

	@Override
	public void process(String data) {
		processor.process(data);
		
		if (processor.triggered()){
			
			for (CallbackURL c : callbackurls)
				c.call();
			
			PolicyManager.sharedManager().remove(policyid);
		}		
		
	}

	@Override
	public String toString() {
		String thequery = null;
		
		if (processor.last <= 0){
			thequery = query.replace("[timeframe]", "[range 5 seconds]");
			
		}else{
			final String s = String.format("@%016x@", processor.last * 1000000);
			thequery = query.replace("[timeframe]", "[since " + s + "]");
		}
		return thequery.replace("[deviceaddr]", String.format("\"%s\"", LeaseData.sharedData().lookup(subject)));
	}

}
