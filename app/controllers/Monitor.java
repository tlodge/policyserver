package controllers;

import homework.SRPCManager;

import java.util.ArrayList;
import java.util.List;

import models.Sites;
import models.URLData;
import models.Website;


import play.mvc.Controller;

public class Monitor extends Controller {

	private static final String DELIMETER = "\\<\\|\\>";
	private static String last = "";
	
    public static void web(String device) {
    	
    	//String since = params.get("since");
    	//String limit = params.get("limit");
    	System.err.println("latest sites size for x is " + URLData.sharedData().getSites().size());
        renderJSON(URLData.sharedData().getSites(/*device*/));
    }

    
    public static void bandwidth(){
    	
    }
}

