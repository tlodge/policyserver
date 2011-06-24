package homework;

import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import models.Action;
import models.URLConditionAction;
import models.URLData;



public class PollingThread extends Thread
{
	public static final String hwdbHost = "10.2.0.2";

	private static final Logger logger = Logger.getLogger(PollingThread.class.getName());

	private final int TIME_DELTA = 5000;
	private final JavaSRPC rpc = new JavaSRPC();
	
	private final URLConditionHandler urlconditionhandler = new URLConditionHandler();
	
	private void init(){
	
		HashSet<String> hs = new HashSet<String>();
		hs.add("www.drupal.org");
		try{
			//URL testurl = new URL("http://10.2.0.2/ws.v1/homework/permit/00:26:B0:5D:97:59");
			
			URL testurl = new URL("http://localhost:9000/notify/push");
			Action a = new Action(testurl);
			
			urlconditionhandler.addConditionsAction(new URLConditionAction(hs,a));
		}catch(Exception e){
			
		}
	}
	
	@Override
	public void run()
	{
		init();
		try
		{
			while (true)
			{
				if (!rpc.isConnected())
				{
					try
					{
						rpc.connect(InetAddress.getByName(hwdbHost), 987);
					}
					catch (final Exception e)
					{
						logger.log(Level.SEVERE, e.getMessage(), e);
					}
				}

				while (rpc.isConnected())
				{
					try
					{
						pollURLTable();
						urlconditionhandler.handleData();
					}
					catch (final Exception e)
					{
						logger.log(Level.SEVERE, e.getMessage(), e);
					}

					try
					{
						Thread.sleep(TIME_DELTA);
					}
					catch (final Exception e)
					{
					}
				}

				try
				{
					Thread.sleep(5000);
				}
				catch (final Exception e)
				{
				}
			}
		}
		catch (final Exception e)
		{
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	
	private void pollURLTable() throws Exception{
		String urlQuery;
		long last = URLData.sharedData().getLatestTs();
		
		if (last > 0)
		{
			final String s = String.format("@%016x@", last * 1000000);
			urlQuery = String.format("SQL:select timestamp, hst from Urls [ since %s ] order by hst asc", s);
			System.err.println(urlQuery);
		}
		else
		{
			urlQuery = String.format("SQL:select timestamp, hst from Urls [range 5 seconds] order by hst asc");
			System.err.println(urlQuery);
		}
		
		URLData.sharedData().parse(rpc.call(urlQuery));
	}
}