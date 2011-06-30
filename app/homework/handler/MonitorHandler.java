package homework.handler;

import homework.JavaSRPC;
import homework.PollingThread;
import datasource.Util;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import datasource.LeaseData;

import models.Website;
import models.policy.Policy;


public class MonitorHandler {

	protected static final String DELIMETER = "\\<\\|\\>";
	
	private static MonitorHandler sharedHandler = null;

	public static final String hwdbHost = "10.2.0.2";

	private static final Logger logger = Logger.getLogger(MonitorHandler.class.getName());

	private final JavaSRPC rpc = new JavaSRPC();
	
	ArrayList<Website> sites = new ArrayList<Website>();
	private long lastsiterequest = 0L;
	private long lastbwrequest = 0L;
	private long lastactivityrequest = 0L;
	
	private MonitorHandler(){
		init();
	}

	private void init(){
		try{

			try
			{
				System.err.println("connectiong to hw dbase....");
				rpc.connect(InetAddress.getByName(hwdbHost), 987);
				System.err.println("scuccessfully connected to db...");
			}
			catch (final Exception e)
			{
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}catch(Exception e){
			System.err.println("error connecting " + e.getMessage());
		}
	}

	public static MonitorHandler sharedHandler(){
		if (sharedHandler == null){
			System.err.println("creating new monitor handler");
			sharedHandler = new MonitorHandler();
		}
		return sharedHandler;
	}

	public Long getLatestBandwidth(String device){
		String query = null;
		
		try {
			String ipaddr = LeaseData.sharedData().lookup(device);
			
			if (lastbwrequest > 0){
				final String s = String.format("@%016x@", lastbwrequest * 1000000);
				query = String.format("SQL:select timestamp, sum(nbytes), daddr from Flows [since %s] where daddr contains \"%s\" and saddr notcontains \"%s\" group by daddr",s,ipaddr, ipaddr);		
			}else{
				query = String.format("SQL:select timestamp, sum(nbytes), daddr from Flows [range 5 seconds] where daddr contains \"%s\" and saddr notcontains \"%s\" group by daddr", ipaddr, ipaddr);

			}
			
			System.err.println(query);
			
			return processbwidth(rpc.call(query), ipaddr);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0L;
	}
	
	public ArrayList<Website> getLatestSites(String device){
		System.err.println("getting latest siste...");
		sites.clear();
		String ipaddr = LeaseData.sharedData().lookup(device);
		
		try {
			if (lastsiterequest > 0){
				final String s = String.format("@%016x@", lastsiterequest * 1000000);
				String query = String.format("SQL:select timestamp, saddr, hst from Urls [ since %s ] where saddr contains \"%s\" order by hst asc", s, ipaddr);
				processsites(rpc.call(query));
				
			}else{
				String query = String.format("SQL:select timestamp, saddr, hst from Urls [range 5 seconds] where saddr contains \"%s\" order by hst asc", ipaddr);
				processsites(rpc.call(query));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sites;
	}
	
	public long getLatestActivity(String device){
		//return 0 if no activity
		//return timestamp if there is activity....
		String query = null;
		
		try {
			String ipaddr = LeaseData.sharedData().lookup(device);
			
			if (lastactivityrequest > 0){
				final String s = String.format("@%016x@", lastactivityrequest * 1000000);
				query = String.format("SQL:select timestamp, saddr from Flows [since %s] where saddr contains \"%s\"",s,ipaddr);		
			}else{
				query = String.format("SQL:select timestamp, saddr from Flows [range 5 seconds] where saddr contains \"%s\"", ipaddr);

			}
			
			System.err.println("qry = " + query);
			
			return processactivity(rpc.call(query), ipaddr);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0L;
	}
	
	private long processactivity(String data, String ipaddr){
		lastactivityrequest = 0L;
		String[] rows = data.split("\n");
		if (rows.length > 2){
			System.err.println("activity - got " + rows[rows.length-1]);
			String row[] = rows[rows.length-1].split(DELIMETER);
			lastactivityrequest = Util.convertTs(row[0]) + 1;
			return lastactivityrequest;
		}
		return 0L;
	}
	
	private long processbwidth(String data, String ipaddr){
		String[] rows = data.split("\n");
    	
		
		lastbwrequest = 0L;
		
    	if (rows.length > 2){
    		
    		for (int i = 2; i < rows.length; i++){
    
    			String row[] = rows[i].split(DELIMETER);
    			if (row[2].equals(ipaddr)){
    				System.err.println("bwidth - got " + row[1]);
    				lastbwrequest = Util.convertTs(row[0]) + 1;
    				return Long.valueOf(row[1]);
    			}
    		}
    	}	
    	
    	return 0L;
	}
	
	private void processsites(String data){
		String[] rows = data.split("\n");
    	String lastsite = "";
    	String lastsrc  = "";
    	
    	lastsiterequest = 0L;
    	
    	if (rows.length > 2){
    		for (int i = 2; i < rows.length; i++){
    			String row[] = rows[i].split(DELIMETER);
    			
    			/*final int start = row[0].indexOf('@');
				final int end = row[0].indexOf('@', start + 1);
				final String time = row[0].substring(start + 1, end);
				final long timeLong = Long.parseLong(time, 16);*/
    			
    			
    			final long timeLong = Util.convertTs(row[0]);
    			
				lastsiterequest = Math.max(timeLong, lastsiterequest) + 1; //to ensure we don't get sent the last record again..
    			
				if (! (lastsite.equals(row[2]) && lastsrc.equals(row[1]))){
    				sites.add(new Website(row[1], row[2], timeLong));
    				lastsite = row[2];
    				lastsrc  = row[1];
    			}
    		}
    	}	
	}
	
	
}
