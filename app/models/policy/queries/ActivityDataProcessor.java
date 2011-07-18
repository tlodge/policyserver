package models.policy.queries;

import homework.PollingThread;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import datasource.Util;
import play.Logger;

public class ActivityDataProcessor extends DataProcessor {

	//private static final Logger logger = Logger.getLogger(ActivityDataProcessor.class.getName());

	static SimpleDateFormat df = new SimpleDateFormat("HH:mm");
	static Calendar calendar = GregorianCalendar.getInstance();
	private long froms;
	private long tos;
	
	public ActivityDataProcessor (String f, String t, String ipaddr){
		try{
			Date from = df.parse(f);
			Date to = df.parse(t);
			
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
		
		if (data == null)
			return;
		String[] rows = data.split("\n");
		last = 0;
		
    	if (rows.length > 2){
    		for (int i = 2; i < rows.length; i++){
    			String row[] = rows[i].split(DELIMETER);
    			final long timeLong = Util.convertTs(row[0]);
    			
    			last = Math.max(timeLong, last) + 1; //to ensure we don't get sent the last record again..
    			
				if (withinTimeRange(timeLong)){
					if (!triggered){
						Logger.info("activity policy: triggering");
    					triggered = true;
					}
    			}		
    		}
    	}	
		
	}

	
	public boolean withinTimeRange(long ts){
		calendar.setTime(new Date(ts));
		int tohour = calendar.get(Calendar.HOUR_OF_DAY);
		int tominute = calendar.get(Calendar.MINUTE);
		int toseconds = calendar.get(Calendar.SECOND);
		
		long routersecondsfrommidnight = (tohour * 3600) + (tominute * 60) + toseconds;
		
		
		if (tos > froms){
			boolean toreturn = (routersecondsfrommidnight >= froms && routersecondsfrommidnight <= tos);
			return toreturn;
		}else{
			boolean toreturn = !(routersecondsfrommidnight > tos && routersecondsfrommidnight < froms);
			return toreturn;
		}
	}
}
