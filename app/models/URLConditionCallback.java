package models;
import java.util.HashSet;

import models.policy.Policy;


public class URLConditionCallback {

	private String subject;
	private HashSet<String> conditions;	
	private Callback action;

	public URLConditionCallback(String subject, HashSet<String> conditions, Callback action){
		this.setSubject(subject);
		this.setConditions(conditions);
		this.setCallback(action);
	}

	public void setConditions(HashSet<String> conditions) {
		this.conditions = conditions;
	}

	public HashSet<String> getConditions() {
		return conditions;
	}

	public void setCallback(Callback action) {
		this.action = action;
	}

	public Callback getCallback() {
		return action;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}
	
	
}
