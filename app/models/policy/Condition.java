package models.policy;

import java.util.Hashtable;

public class Condition {
	public String type;
	public Hashtable arguments;
	
	public void print(){
		System.out.println("condition type = " + type);
		for (Object o: arguments.values()){
			System.out.print(o.toString() + " ");
		}
		System.out.println();
	}
}
