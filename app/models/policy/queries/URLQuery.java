package models.policy.queries;

import play.Logger;
import policy.PolicyManager;
import models.policy.CallbackURL;
import models.policy.Policy;
import datasource.LeaseData;

public class URLQuery extends Query{
	
	
	public URLQuery(Policy p){
		super(p);
		String[] sites = (String[]) p.condition.arguments.get("sites");
		subject = p.subject;
		processor = new URLDataProcessor(sites);
		callbackurls.add(new CallbackURL(p));
		query = "SQL:select timestamp, saddr, hst from Urls [timeframe] where saddr contains [deviceaddr] order by hst asc";
	}
	
	@Override
	public void process (String data){
		
		/*
		 * added for testing..
		 */
		if (data == null){
			processor.triggered = true;
		}
		
		processor.process(data);
		
		Logger.info("Processing policy %s", policyid);
		
		if (processor.triggered()){
			
			Logger.info("POLICY %s TRIGGERED", policyid);
			triggered = true;
			
			for (CallbackURL c : callbackurls)
				c.call();
			
			
			//PolicyManager.sharedManager().remove(policyid);
		}		
	}

	@Override
	public String toString() {
		String thequery = null;
		
		if (processor.last <= 0){
			thequery = query.replace("[timeframe]", "[range 6 seconds]");
			
		}else{
			final String s = String.format("@%016x@", processor.last * 1000000);
			thequery = query.replace("[timeframe]", "[since " + s + "]");
		}
		
		String ipaddr = null;
		
		if ( (ipaddr = LeaseData.sharedData().lookup(subject)) != null){
			return thequery.replace("[deviceaddr]", String.format("\"%s\"", ipaddr));
		}
		
		return null;
	}
}
