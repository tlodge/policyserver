package models.policy;

import homework.PollingThread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import play.Logger;

public class CallbackURL {
	
	Runnable r;
	
	//private static final Logger logger = Logger.getLogger(CallbackURL.class.getName());

	public CallbackURL(String strurl){
		final String callbackurl = strurl;
		
		r = new Runnable(){

			@Override
			public void run() {
				try{
					URL url= new URL(callbackurl);
					Logger.info("calling url %s", url.toString());
					URLConnection c = url.openConnection();
			        BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
			        String inputLine;

			        while ((inputLine = in.readLine()) != null) 
			           Logger.info(inputLine);
			        in.close();

				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		};
	}
	
	public CallbackURL(Policy p){
	
			if (p.action.type.equals("notify")){
							
				r = new Runnable(){

					@Override
					public void run() {
						try{
							URL url= new URL("http://localhost:8080/policyserver/notify/push");
							Logger.info("calling url %s", url.toString());
							URLConnection c = url.openConnection();
					        BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
					        String inputLine;

					        while ((inputLine = in.readLine()) != null) 
					        	Logger.info(inputLine);
					        in.close();

						}catch(Exception e){
							e.printStackTrace();
						}
					}
					
				};
			}else if (p.action.type.equals("block")){
				final String subject = p.action.subject;
				
				r = new Runnable(){

					@Override
					public void run() {
						try {
							
							URL url= new URL(String.format("http://localhost:8080/policyserver/block/%s",subject));
							Logger.info("calling url %s", url.toString());
							URLConnection c = url.openConnection();
							c.setDoOutput(true);
							OutputStreamWriter out = new OutputStreamWriter(c.getOutputStream());
							out.write("string=hello");
							out.close();
							BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
							String inputLine;
							while((inputLine = in.readLine()) != null){
								Logger.info(inputLine);
							}
							in.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
					
				};
			}
	}
	
	public void call(){
		new Thread(r).run();
	}
}
