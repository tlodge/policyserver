package models.policy;

public class Policy {
	
	public String identity;
	
	public String subject;
	
	public Condition condition;
	
	public Action action;
	
	public void print(){
		System.out.println("policy identity " + identity);
		System.out.println("policy subject " + subject);
		condition.print();
		action.print();
	}
}
