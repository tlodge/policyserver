package models.policy;

public class Action {
	public String subject;
	public String type;
	public String[] arguments;
	
	public void print(){
		
		System.out.println("action subject: " + subject);
		
		System.out.println("action type " + type);
		
		System.out.println("arguments = ");
		
		for (String arg: arguments){
			System.out.print(" " + arg);
		}
		System.out.println();
	}
}
