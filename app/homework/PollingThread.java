package homework;

import java.net.InetAddress;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import models.Action;
import models.URLConditionAction;



public class PollingThread extends Thread
{
	public static final String hwdbHost = "10.2.0.2";
	// public static final String hwdbHost = "192.168.9.1";

	private static final String searchString = "<|>Artifact App<|>USB<|>";

	private static long last = 0L;
	
	private static final Logger logger = Logger.getLogger(PollingThread.class.getName());

	private final int TIME_DELTA = 5000;
	private final JavaSRPC rpc = new JavaSRPC();
	private final boolean nox = true;

	public static boolean trayPlugged = false;

	private final URLConditionHandler urlconditionhandler = new URLConditionHandler();
	
	private void init(){
		HashSet<String> hs = new HashSet<String>();
		hs.add("www.drupal.org");
		try{
			URL testurl = new URL("http://10.2.0.2/ws.v1/homework/permit/00:26:B0:5D:97:59");
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
						
						last = urlconditionhandler.handleData(updateURLs());
						
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

	
	private String updateURLs() throws Exception{
		String urlQuery;
		
		if (last > 0)
		{
			final String s = String.format("@%016x@", last * 1000000);
			urlQuery = String.format("SQL:select timestamp, hst from Urls [ since %s ] order by hst asc", s);
		}
		else
		{
			urlQuery = String.format("SQL:select timestamp, hst from Urls [range 5 seconds] order by hst asc");
		}
		return rpc.call(urlQuery);
	}
	
	
	private void updateLeases() throws Exception
	{
		String leaseQuery;
		if (last > 0)
		{
			final String s = String.format("@%016x@", last * 1000000);
			leaseQuery = String.format("SQL:select * from Leases [ since %s ]", s);
		}
		else
		{
			leaseQuery = String.format("SQL:select * from Leases");
		}
		final String leaseResults = rpc.call(leaseQuery);
		logger.info(leaseResults);
	}

	private void updateLinks() throws Exception
	{
		String linkQuery;
		if (last > 0)
		{
			final String s = String.format("@%016x@", last * 1000000);
			linkQuery = String.format("SQL:select * from Links [ since %s ]", s);
		}
		else
		{
			linkQuery = String.format("SQL:select * from Links");
		}
		final String linkResults = rpc.call(linkQuery);

		logger.info(linkResults);
	}
}