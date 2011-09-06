package controllers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import com.ning.http.util.Base64;

import play.Logger;
import play.mvc.Controller;


public class Notify extends Controller{

	private static final String APPLICATION_KEY	 = "lyWbeu5eQk2ETyxLImcykw";
	private static final String APPLICATION_SECRET  = "TP6b3UBaTYS6MqdwJKz97g";
	private static final String URBANAIRSHIP_URL = "https://go.urbanairship.com/api/push/";
		
	public static void endpoint(String endpoint, String type){
		Logger.info(String.format("INSERTING %s %s %s ",endpoint, type, params.get("message")));
	}
		
	public static void policyclient(String policyid){
		System.err.println("notifying policyclient..." + policyid);
		sendUAMessage(String.format("\"aps\": {\"alert\": \"Policy Fired!\", \"policyid\":\"%s\"}",policyid));
	}
	
	public static void register(){

	}

	public static void tweet(){	

	}

	public static void push(){
	
		
	}

	private static String getB64Auth(){
		return "Basic " + Base64.encode((APPLICATION_KEY + ":" + APPLICATION_SECRET).getBytes());

	}

	public static void phone(){

	}

	public static void beep(){

	}
	
	private static void sendUAMessage(String message){
		try{
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(URBANAIRSHIP_URL);
			post.setHeader("Authorization", getB64Auth());
			post.setHeader(new BasicHeader("Content-Type", "application/json"));

			String json = String.format("{\"device_tokens\": [\"85e565ad0c6de5cecfc16da6a61a3d3135a98d0f0804ca450922cdfd3bbda08e\",\"9df31f0cf059866c0b49ac300e4dbc125361f990f46af363be5e41d3208f3cba\"], %s}",message);

			StringEntity s = new StringEntity(json);

			s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(s);
			
			HttpResponse response;
			response = client.execute(post);

			System.err.println(response.getEntity().toString());

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
