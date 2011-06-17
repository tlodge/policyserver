package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Sites;
import models.Website;


import play.mvc.Controller;

public class Monitor extends Controller {

    public static void web(String device) {
    	
    	String since = params.get("since");
    	String limit = params.get("limit");
    	
    	System.err.println("since = " + since);
    	System.err.println("limit = " + limit);
    	System.err.println("device = " + device);
    	
    	List<Website> sites = new ArrayList<Website>();
    	sites.add(new Website("www.news.bbc.co.uk", 12354567L));
    	sites.add(new Website("www.google.com", 123455567L));
    	sites.add(new Website("www.somewhereelse.co.uk", 551234567L));
        renderJSON(sites);
    }

    
    public static void bandwidth(){
    	
    }
}

