package models;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class Callback implements Runnable{

	private URL url;
	
	public Callback(URL url){
		this.url = url;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			out.write("string=hello");
			out.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			while((inputLine = in.readLine()) != null){
				System.out.println(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("executing action " + url);
	}
}
