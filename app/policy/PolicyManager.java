package policy;

import homework.JavaSRPC;
import homework.PollingThread;
import homework.handler.MonitorHandler;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import play.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import models.policy.Action;
import models.policy.Policy;
import models.policy.Condition;
import models.policy.queries.ActivityQuery;
import models.policy.queries.BandwidthQuery;
import models.policy.queries.Query;
import models.policy.queries.URLQuery;

public class PolicyManager {

	private static PolicyManager sharedManager = null;
	
	public Hashtable<String, Query> activePolicies; 

	
	private static int policyindex = 2;
	
	private PolicyManager(){
		init();
	}
	
	private void init(){
		try{
			activePolicies = new Hashtable<String, Query>();
		}catch(Exception e){
			Logger.info(e, "error creating policy manager ");
		}
	}
	
	
	public static PolicyManager sharedManager(){
		if (sharedManager == null){
			sharedManager = new PolicyManager();
		}
		return sharedManager;
	}
	
	public boolean deleteAll(){
		activePolicies.clear();
		Logger.info("REMOVED ALL INSTALLED POLICIES");
		return true;
	}
	
	/*public void remove(String policyid){
		activePolicies.remove(policyid);
		Logger.info("removed policy %s", policyid);
	}*/
	
	public void removeTriggered(){
		Iterator<Map.Entry<String,Query>> it = activePolicies.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, Query> entry = it.next();
			if (entry.getValue().triggered()){
				Logger.info("removed policy %s",entry.getValue().policyId());
				it.remove();
			}
		}
	}
	public String save (String policy){
		boolean success  = true;
		
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		
		xstream.alias("policy", Policy.class);
		
		xstream.alias("action", Action.class);
		
		xstream.alias("condition", Condition.class);
		
		Policy p = (Policy) xstream.fromXML(policy);
		
		String identity = store(p);
		
		if (identity != null){
	
			if (p.condition.type.equals("visiting")){
				activePolicies.put(identity, new URLQuery(p));
			}else if (p.condition.type.equals("bandwidth")){
				activePolicies.put(identity, new BandwidthQuery(p));
			}else if (p.condition.type.equals("timed")){
				activePolicies.put(identity, new ActivityQuery(p));
			}
		}
		
		if (success)
			return identity;
		
		return null;
	}
	
	private String store(Policy p){
		if (p.identity != null){
			return p.identity;
		}
		p.identity = String.valueOf(policyindex++);
		return p.identity;
	}
	
	
}
