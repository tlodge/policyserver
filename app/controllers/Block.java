package controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import play.mvc.Controller;

public class Block extends Controller{

	public static void block(String device){
		
		System.err.println("BLOCKING DEVICE " + device);
		
		try{
		
			URL url = new URL("http://10.2.0.2/ws.v1/homework/deny/" + device);
			
			URLConnection connection = url.openConnection();
			
			connection.setDoOutput(true);

			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			
			out.write("block=block");
			
			out.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String decodedString;

			while ((decodedString = in.readLine()) != null) {
				System.out.println(decodedString);
			}
				
			
			in.close();
			
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
	}

	public static void status(){


	}

	public static void unblock(){


	}
}
