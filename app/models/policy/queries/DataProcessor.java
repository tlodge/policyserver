package models.policy.queries;

abstract class DataProcessor {

	protected static final String DELIMETER = "\\<\\|\\>";
	protected boolean triggered = false;
	protected long last = 0L;
	
	public long getLast(){
		return last;
	}
	
	public boolean triggered(){
		return triggered;
	}
		
	public void reset(){
		triggered = false;
	}
	
	public abstract void process(String data);
}
