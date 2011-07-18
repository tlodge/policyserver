package homework;


import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
//import java.util.logging.Logger;
import play.Logger;
import policy.PolicyManager;

import datasource.LeaseData;
import datasource.URLData;

import models.policy.queries.Query;



public class PollingThread extends Thread
{
	public static final String hwdbHost = "localhost"; //"10.2.0.2";

	//private static final Logger logger = Logger.getLogger(PollingThread.class.getName());

	private final int TIME_DELTA = 5000;
	private final JavaSRPC rpc = new JavaSRPC();


	private void init(){

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
						Logger.info("connecting to hw dbase....");
						rpc.connect(InetAddress.getByName(hwdbHost), 987);
						Logger.info("successfully connected to hw dbase....");
					}
					catch (final Exception e)
					{
						Logger.error("rpc connection error %s", e.getMessage());
					}
				}

				while (rpc.isConnected())
				{
					try
					{
						updateLeases();

						for (Query q : PolicyManager.sharedManager().activePolicies.values()){
							String query = q.toString();

							
							if (q!=null){
								
								try {
									
									String result = rpc.call(query);
									
									if (result != null)
										q.process(result);
									
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
					catch (final Exception e)
					{
						Logger.error("rpc error %s", e.getMessage());
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
			Logger.error("rpc error %s", e.getMessage());
		}
	}

	private void updateLeases() throws Exception{
		String urlQuery = LeaseData.sharedData().getQuery();
		LeaseData.sharedData().parse(rpc.call(urlQuery));
	}
}