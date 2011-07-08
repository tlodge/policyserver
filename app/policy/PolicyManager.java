package policy;

import homework.JavaSRPC;
import homework.SRPCManager;
import homework.handler.MonitorHandler;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Hashtable;

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
			System.err.println("error creating policy manager " + e.getMessage());
		}
	}
	
	public static PolicyManager sharedManager(){
		if (sharedManager == null){
			sharedManager = new PolicyManager();
		}
		return sharedManager;
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
