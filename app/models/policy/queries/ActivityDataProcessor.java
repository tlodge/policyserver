package models.policy.queries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import datasource.Util;

public class ActivityDataProcessor extends DataProcessor {

	static SimpleDateFormat df = new SimpleDateFormat("HH:mm");
	static Calendar calendar = GregorianCalendar.getInstance();
	private long froms;
	private long tos;
	
	public ActivityDataProcessor (String f, String t, String ipaddr){
		try{
			Date from = df.parse(f);
			Date to = df.parse(t);
			System.err.println("SET FROM TO " + f + " and set TO To " + t);
			
			calendar.setTime(from);
			int fromhour = calendar.get(Calendar.HOUR_OF_DAY);
			int fromminute = calendar.get(Calendar.MINUTE);
			froms = (fromhour * 3600) + (fromminute * 60);
			
			calendar.setTime(to);
			int tohour = calendar.get(Calendar.HOUR_OF_DAY);
			int tominute = calendar.get(Calendar.MINUTE);
			tos = (tohour * 3600) + (tominute * 60);
		}
		catch(Exception e){
			
		}
	}
	
	@Override
	public void process(String data) {
		
		String[] rows = data.split("\n");
		last = 0;
		
    	if (rows.length > 2){
    		for (int i = 2; i < rows.length; i++){
    			String row[] = rows[i].split(DELIMETER);
    			final long timeLong = Util.convertTs(row[0]);
    			
    			last = Math.max(timeLong, last) + 1; //to ensure we don't get sent the last record again..
    			
				if (withinTimeRange(timeLong)){
					if (!triggered){
						System.err.println("triggering!");
    					triggered = true;
					}
    			}		
    		}
    	}	
		
	}

	
	public boolean withinTimeRange(long ts){
		//long secondselapsed = (ts/1000) % (24  * 3600);
		calendar.setTime(new Date(ts));
		int tohour = calendar.get(Calendar.HOUR_OF_DAY);
		int tominute = calendar.get(Calendar.MINUTE);
		int toseconds = calendar.get(Calendar.SECOND);
		
		long routersecondsfrommidnight = (tohour * 3600) + (tominute * 60) + toseconds;
		
		//System.err.println("FLOW ROUTER DATE" + new Date(ts).toString());
		//System.err.println("TimeStamp of this machine..")
		if (tos > froms){
			//System.err.println("toseconds (" + tos + ") is greater than from seconds (" + froms + ") and rsfm is " + routersecondsfrommidnight);
			boolean toreturn = (routersecondsfrommidnight >= froms && routersecondsfrommidnight <= tos);
			//System.err.println("returning " + toreturn);
			return toreturn;
		}else{
			//System.err.println("toseconds (" + tos + ") is LESS than from seconds (" + froms + ") and rsfm is " + routersecondsfrommidnight);
			boolean toreturn = !(routersecondsfrommidnight > tos && routersecondsfrommidnight < froms);
			//System.err.println("returning " + toreturn);
			return toreturn;
		}
	}
}
