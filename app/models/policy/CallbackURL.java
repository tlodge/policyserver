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
	
	
	private String generateMessage(Policy p){
		
		String message = String.format("Hello %s, device %s", p.action.subject, p.subject);
		
		if (p.condition.type.equals("visiting")){
			message +=  " visited one of the following: ";
			String[] mysites = (String[]) p.condition.arguments.get("sites");
			for (String s : mysites)
				message += s + " ";
		}else if (p.condition.type.equals("bandwidth")){
			message += " has used " + p.condition.arguments.get("percentage") + " percent of the bandwidth limit ";
		}else if (p.condition.type.equals("timed")){
			message += " was used between " + p.condition.arguments.get("from") + " and " + p.condition.arguments.get("to");
		}
		
		System.err.println("message is " + message);
		return message;
	}
	
	public CallbackURL(Policy ptmp){
			
		final Policy p = ptmp;
		
			if (p.action.type.equals("notify")){
				
				final String subtype = p.action.arguments[0];	
				final String endpoint = p.action.subject;
				/* add back policyserver in url */
				r = new Runnable(){

					@Override
					public void run() {
						try{
							System.err.println("subtype is " + subtype);
							System.err.println("end point is " + endpoint);
							
							String strurl = (String.format("http://127.0.0.1:8080/policyserver/notify/%s/%s", endpoint, subtype));
							//String strurl = (String.format("http://127.0.0.1:8080/notify/%s/%s", endpoint, subtype));
							
							System.err.println("CALLING URL " + strurl);
							
							URL url= new URL(strurl);
							//URL url= new URL(String.format("http://127.0.0.1:8080/notify/%s/%s", endpoint, subtype));
							
							URLConnection connection = url.openConnection();
							
							connection.setDoOutput(true);

							OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
							
							//p.print();
							
							out.write(String.format("message=%s", generateMessage(p)));
							
							out.close();

							BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

							String decodedString;

							while ((decodedString = in.readLine()) != null) {
								System.out.println(decodedString);
							}
								
							
							in.close();
							
							
							/*URL url= new URL("http://localhost:8080/policyserver/notify/push/");
							Logger.info("calling url %s", url.toString());
							URLConnection c = url.openConnection();
					        BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
					        String inputLine;

					        while ((inputLine = in.readLine()) != null) 
					        	Logger.info(inputLine);
					        in.close();
							 */
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
		new Thread(r).start();
	}
}
