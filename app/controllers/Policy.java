package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Website;
import play.mvc.Controller;

public class Policy extends Controller {

		public static void save(){
			String policy = params.get("policy");
			System.err.println("policy is " + policy);
			/*List<Website> sites = new ArrayList<Website>();
	    	sites.add(new Website("www.news.bbc.co.uk", 12354567L));
	    	sites.add(new Website("www.google.com", 123455567L));
	    	sites.add(new Website("www.somewhereelse.co.uk", 551234567L));
	        renderJSON(sites);*/
		}
}
