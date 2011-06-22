package models;
import java.util.HashSet;


public class URLConditionAction {

	private HashSet<String> conditions;	
	private Action action;

	public URLConditionAction(HashSet<String> conditions, Action action){
		this.setConditions(conditions);
		this.setAction(action);
	}

	public void setConditions(HashSet<String> conditions) {
		this.conditions = conditions;
	}

	public HashSet<String> getConditions() {
		return conditions;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Action getAction() {
		return action;
	}
	
	
}
