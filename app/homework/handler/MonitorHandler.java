package homework.handler;

import homework.JavaSRPC;
import homework.PollingThread;
import datasource.Util;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;

import play.Logger;

import datasource.LeaseData;

import models.Website;
import models.policy.Policy;
import models.policy.queries.BandwidthQuery;


public class MonitorHandler {


	protected static final String DELIMETER = "\\<\\|\\>";

	private static MonitorHandler sharedHandler = null;

	public static final String hwdbHost = "localhost";// "10.2.0.14";//  //"10.2.0.2"; 

	//private static final Logger logger = Logger.getLogger(MonitorHandler.class.getName());

	private final JavaSRPC rpc = new JavaSRPC();

	ArrayList<Website> sites = new ArrayList<Website>();
	private long lastsiterequest = 0L;
	private long lastbwrequest = 0L;
	private long lastactivity = 0L;

	private MonitorHandler(){
		init();
	}

	private void init(){
		try{

			try
			{
				Logger.info("connectiong to hw dbase....");
				rpc.connect(InetAddress.getByName(hwdbHost), 987);
				Logger.info("monitor: successfully connected to db...");
			}
			catch (final Exception e)
			{
				Logger.error(e, "Monitor handler init");
			}
		}catch(Exception e){
			Logger.error(e, "error connecting ");
		}
	}

	public static MonitorHandler sharedHandler(){
		if (sharedHandler == null){
			System.err.println("creating new monitor handler");
			sharedHandler = new MonitorHandler();
		}
		return sharedHandler;
	}

	
	public void addNotificationRecord(String endpoint, String type, String message){
		String query = String.format("SQL: insert into NotificationRequest values (\"%s\",\"%s\",\"%s\")", endpoint,type,message);
		Logger.info("calling query %s", query);
		try{
			String s = rpc.call(query);
			Logger.info(s);
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	
	public Long[] getLatestBandwidth(String device){
		
		String sinceLastQuery = null;
		String withinTimeRangeQuery = null;
		
		try {

			String ipaddr = LeaseData.sharedData().lookup(device);

			if (ipaddr != null){
				if (lastbwrequest > 0){
					//Logger.info("last bw request is %s" , new Date(lastbwrequest).toString());
					final String s 		 = String.format("@%016x@", lastbwrequest * 1000000);
					sinceLastQuery 		 = String.format("SQL:select timestamp, sum(nbytes), daddr from Flows [since %s] where daddr contains \"%s\" and saddr notcontains \"%s\" group by daddr",s,BandwidthQuery.subnet, BandwidthQuery.subnet);		
				}else{
					sinceLastQuery = String.format("SQL:select timestamp, sum(nbytes), daddr from Flows [range 5 seconds] where daddr contains \"%s\" and saddr notcontains \"%s\" group by daddr", BandwidthQuery.subnet, BandwidthQuery.subnet);
				}
				withinTimeRangeQuery =  String.format("SQL:select timestamp, sum(nbytes), daddr from Flows [range %d seconds] where daddr contains \"%s\" and saddr notcontains \"%s\" group by daddr", BandwidthQuery.timeRange,  BandwidthQuery.subnet, BandwidthQuery.subnet);
				
				//Logger.info("BW since last query %s", sinceLastQuery);
				//Logger.info("BW withinTimeRange query %s", withinTimeRangeQuery);
				
				
				String tr = rpc.call(withinTimeRangeQuery);
				String sl = rpc.call(sinceLastQuery);
				
				Long sinceLastBytes = processbandwidth(sl, ipaddr);
				
				Long timeRangeBytes = processbandwidth(tr, ipaddr);
				
				return new Long[]{sinceLastBytes, timeRangeBytes, BandwidthQuery.BANDWIDTH_LIMIT};
			
			}else{
				Logger.info("no lease record for device %s", device);
			}

		} catch (IOException e) {
			Logger.error(e, "error getting bandwidth");
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return new Long[]{0L, 0L, BandwidthQuery.BANDWIDTH_LIMIT};
	}

	public ArrayList<Website> getLatestSites(String device){
		sites.clear();
		String ipaddr = LeaseData.sharedData().lookup(device);

		if (ipaddr != null){
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
				Logger.error(e, "error getting latest sites");
				e.printStackTrace();
			}
		}else{
			Logger.info("no lease record for device %s", device);
		}
		return sites;
	}

	public Long[] getLatestActivity(String device){

		String query = null;

		try {
			String ipaddr = LeaseData.sharedData().lookup(device);
			if (ipaddr != null){
				if (lastactivity > 0){
					final String s = String.format("@%016x@", lastactivity * 1000000);
					query = String.format("SQL:select timestamp, saddr from Flows [since %s] where saddr contains \"%s\"",s,ipaddr);		
				}else{
					query = String.format("SQL:select timestamp, saddr from Flows [range 5 seconds] where saddr contains \"%s\"", ipaddr);

				}

				//System.err.println("qry = " + query);

				return processactivity(rpc.call(query), ipaddr);
			}else{
				Logger.info("no lease record for device %s", device);
			}
		} catch (IOException e) {
			Logger.error(e, "getting latest activity");

			//e.printStackTrace();
		}
		return new Long[]{System.currentTimeMillis(),0L};
	}

	private Long[] processactivity(String data, String ipaddr){


		lastactivity = 0L;

		if (data != null){
			String[] rows = data.split("\n");

			if (rows.length > 2){
				String row[] = rows[rows.length-1].split(DELIMETER);
				lastactivity = Util.convertTs(row[0]) + 1;
			}
		}

		return new Long[]{System.currentTimeMillis(),lastactivity};
	}

	private long processbandwidth(String data, String ipaddr){
		if (data != null){

			String[] rows = data.split("\n");


			lastbwrequest = 0L;

			if (rows.length > 2){

				for (int i = 2; i < rows.length; i++){
					Logger.info("%s", rows[i]);
					String row[] = rows[i].split(DELIMETER);
					if (row[2].equals(ipaddr)){
						
						lastbwrequest = Util.convertTs(row[0]) + 1;
						return Long.valueOf(row[1]);
					}
				}
			}	
		}
		return 0L;
	}

	private void processsites(String data){

		if (data == null)
			return;

		String[] rows = data.split("\n");
		String lastsite = "";
		String lastsrc  = "";

		lastsiterequest = 0L;

		if (rows.length > 2){
			for (int i = 2; i < rows.length; i++){
				String row[] = rows[i].split(DELIMETER);

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
