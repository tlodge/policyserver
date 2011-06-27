package policy;

import homework.JavaSRPC;
import homework.SRPCManager;
import homework.handler.ConditionHandler;
import homework.handler.MonitorHandler;
import homework.handler.URLConditionHandler;

import java.net.InetAddress;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import models.policy.Action;
import models.policy.Policy;
import models.policy.Condition;

public class PolicyManager {

	private static PolicyManager sharedManager = null;
	private static int policyindex = 0;
	
	private PolicyManager(){
		init();
	}
	
	private void init(){
		try{
			
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
		boolean success  = false;
		
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		
		xstream.alias("policy", Policy.class);
		
		xstream.alias("action", Action.class);
		
		xstream.alias("condition", Condition.class);
		
		Policy p = (Policy) xstream.fromXML(policy);
		
		//if already have an identity then update the policy and save it
		
		String identity = store(p);
		//take the conditions and register them with the conditionActionhandler
		System.err.println("identity is " + identity);
		
		System.err.println("policy is null is " + (p==null));
		if (identity != null){
			System.err.println("regstering policy with condition handler");
			success = ConditionHandler.sharedHandler().registerPolicy(p);
		}
		//update the associated monitor handlers
		if (success){
			success = MonitorHandler.sharedHandler().registerPolicy(p);
		}
		
		if (success)
			return identity;
		
		return null;
	}
	
	private String store(Policy p){
		if (p.identity != null){
			return p.identity;
		}
		return String.valueOf(policyindex++);
	}
}
