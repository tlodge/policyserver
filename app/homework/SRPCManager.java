package homework;

import java.io.IOException;
import java.net.InetAddress;

public class SRPCManager {
	private static SRPCManager sharedManager = null;
	private static JavaSRPC srpcclient;
	
	private SRPCManager(){
		init();
	}
	
	private void init(){
		try{
			System.err.println("initing the srpc client");
			srpcclient = new JavaSRPC();
			srpcclient.connect(InetAddress.getByName("192.168.9.1"), 987);
			System.err.println("successfully connected!");
		}catch(Exception e){
			System.err.println("error connecting " + e.getMessage());
		}
	}
	
	public static SRPCManager sharedManager(){
		if (sharedManager == null){
			sharedManager = new SRPCManager();
		}
		return sharedManager;
	}
	
	public  String query(String query){
		if (!srpcclient.isConnected()){
			init();
		}
		try {
			if (srpcclient.isConnected())
				return srpcclient.call(query);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
