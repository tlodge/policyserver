package controllers;


import java.util.ArrayList;
import java.util.List;

import datasource.URLData;

import models.Sites;
import models.Website;
import homework.handler.MonitorHandler;

import play.mvc.Controller;

public class Monitor extends Controller {

	
	
	
    public static void web(String device) {
    	
    	//ArrayList<Website> sites = new ArrayList<Website>();
    	//sites.add(new Website("aa:bb:cc:dd:ee:ff", "www.google.com", System.currentTimeMillis()));
    	renderJSON(MonitorHandler.sharedHandler().getLatestSites(device));
    }

    
    public static void bandwidth(String device){
    	
    	renderJSON(MonitorHandler.sharedHandler().getLatestBandwidth(device));
    }
    
    public static void activity(String device){
    	Long[] l = MonitorHandler.sharedHandler().getLatestActivity(device);  
    	renderJSON(l);
    }
}

