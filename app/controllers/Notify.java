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
import play.mvc.Controller;


public class Notify extends Controller{

	private static final String APPLICATION_KEY	 = "lyWbeu5eQk2ETyxLImcykw";
	private static final String APPLICATION_SECRET  = "TP6b3UBaTYS6MqdwJKz97g";
	private static final String URBANAIRSHIP_URL = "https://go.urbanairship.com/api/push/";
		
	public static void register(){

	}

	public static void tweet(){	

	}

	public static void push(){
		System.err.println("in push!!!!");

		try{
			HttpClient client = new DefaultHttpClient();


			HttpPost post = new HttpPost(URBANAIRSHIP_URL);
			post.setHeader("Authorization", getB64Auth());
			post.setHeader(new BasicHeader("Content-Type", "application/json"));

			String json = "{\"device_tokens\": [\"85e565ad0c6de5cecfc16da6a61a3d3135a98d0f0804ca450922cdfd3bbda08e\"], \"aps\": {\"alert\": \"Hello!\"}}";

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

	private static String getB64Auth(){
		return "Basic " + Base64.encode((APPLICATION_KEY + ":" + APPLICATION_SECRET).getBytes());

	}

	public static void phone(){

	}

	public static void beep(){

	}

}
