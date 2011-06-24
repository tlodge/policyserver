package policy;

import homework.JavaSRPC;
import homework.SRPCManager;

import java.net.InetAddress;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import models.policy.Action;
import models.policy.Policy;
import models.policy.Condition;

public class PolicyManager {

	private static PolicyManager sharedManager = null;
	
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
	
	public boolean save (String policy){
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.alias("policy", Policy.class);
		xstream.alias("action", Action.class);
		xstream.alias("condition", Condition.class);
		Policy p = (Policy) xstream.fromXML(policy);
		System.err.println(p.identity);
		
		return true;
	}
}
