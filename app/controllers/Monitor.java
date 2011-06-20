package controllers;

import homework.SRPCManager;

import java.util.ArrayList;
import java.util.List;

import models.Sites;
import models.Website;


import play.mvc.Controller;

public class Monitor extends Controller {

	private static final String DELIMETER = "\\<\\|\\>";
	private static String last = "";
	
    public static void web(String device) {
    	
    	String since = params.get("since");
    	String limit = params.get("limit");
    	
    	//System.err.println("since = " + since);
    	//System.err.println("limit = " + limit);
    	//System.err.println("device = " + device);
    	
    	String s = SRPCManager.sharedManager().query("SQL:select  timestamp, hst from Urls [range 10 seconds] WHERE saddr = \"192.168.9.76\" order by hst asc");
    	 
    	//String s = SRPCManager.sharedManager().query("SQL:select  timestamp, hst from Urls [range 60 seconds]");
    	
    	String[] rows = s.split("\n");
    	System.err.println("there are "  + rows.length  +  " rows");
    	
    	//ignore the first two rows (SUCCESS and table colnames);
    	List<Website> sites = new ArrayList<Website>();
    	
    	if (rows.length > 2){
    		for (int i = 2; i < rows.length; i++){
    			
    			String row[] = rows[i].split(DELIMETER);
    			
    			if (!last.equals(row[1])){
    				System.err.println("====>" + row[0] + " " + row[1]);
    				final int start = row[0].indexOf('@');
    				final int end = row[0].indexOf('@', start + 1);
    				final String time = row[0].substring(start + 1, end);
    				final long timeLong = Long.parseLong(time, 16);
    				sites.add(new Website(row[1], timeLong/1000000));
    				last = row[1];
    			}
    			
    			
    		}
    	}
    	/*
    	sites.add(new Website("www.news.bbc.co.uk", 12354567L));
    	sites.add(new Website("www.google.com", 123455567L));
    	sites.add(new Website("www.somewhereelse.co.uk", 551234567L));*/
        renderJSON(sites);
    }

    
    public static void bandwidth(){
    	
    }
}

