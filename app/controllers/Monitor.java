package controllers;


import java.util.ArrayList;
import java.util.List;

import datasource.URLData;

import models.Sites;
import models.Website;
import models.policy.queries.BandwidthQuery;
import homework.handler.MonitorHandler;

import play.mvc.Controller;

public class Monitor extends Controller {

	
	
	
    public static void web(String device) {
    	
    	/*ArrayList<Website> sites = new ArrayList<Website>();
    	sites.add(new Website("aa:bb:cc:dd:ee:ff", "www.google.com", System.currentTimeMillis()));
    	renderJSON(sites);*/
    	
    	renderJSON(MonitorHandler.sharedHandler().getLatestSites(device));
    }

    
    public static void bandwidth(String device){
    	//renderJSON(new Long[]{0L, 0L, BandwidthQuery.BANDWIDTH_LIMIT});
    	renderJSON(MonitorHandler.sharedHandler().getLatestBandwidth(device));
    }
    
    public static void activity(String device){
    	//renderJSON(new Long[]{System.currentTimeMillis(),0L});
    	Long[] l = MonitorHandler.sharedHandler().getLatestActivity(device);  
    	renderJSON(l);
    }
}

