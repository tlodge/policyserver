package models.policy.queries;



public class BandwidthDataProcessor extends DataProcessor {

	private long triggerbytes;
	private String ipaddr;
	
	public BandwidthDataProcessor(long tb, String ipaddr){
		System.err.println("setting trigger bytes to " + tb);
		this.triggerbytes = tb;	
		this.ipaddr = ipaddr;
	}
	
	@Override
	public void process(String data) {
		String[] rows = data.split("\n");
    	
		last = 0;
    	
    	if (rows.length > 2){
    		
    		for (int i = 2; i < rows.length; i++){
    			System.err.println(rows[i]);
    			String row[] = rows[i].split(DELIMETER);
    			if (row[1].equals(ipaddr)){
    				if (Long.valueOf(row[0]) >= triggerbytes){
    					System.err.println("bandwidth triggered....");
    					triggered = true;
    				}
    			}
    		}
    	}	
	}
}
