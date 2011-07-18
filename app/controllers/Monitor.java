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

