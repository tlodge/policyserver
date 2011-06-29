package models.policy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class CallbackURL {
	
	Runnable r;
	
	public CallbackURL(Policy p){
	
			if (p.action.type.equals("notify")){
							
				r = new Runnable(){

					@Override
					public void run() {
						try{
							URL url= new URL("http://localhost:9000/notify/push");
							System.err.println("calling url " + url.toString());
							URLConnection c = url.openConnection();
					        BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
					        String inputLine;

					        while ((inputLine = in.readLine()) != null) 
					            System.out.println(inputLine);
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
							
							URL url= new URL(String.format("http://localhost:9000/block/%s",subject));
							System.err.println("calling url " + url.toString());
							URLConnection c = url.openConnection();
							c.setDoOutput(true);
							OutputStreamWriter out = new OutputStreamWriter(c.getOutputStream());
							out.write("string=hello");
							out.close();
							BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
							String inputLine;
							while((inputLine = in.readLine()) != null){
								System.out.println(inputLine);
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
