package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Website;
import models.messages.ResponseMessage;
import play.mvc.Controller;
import policy.PolicyManager;

public class Policy extends Controller {

		public static void save(){
			String policy = params.get("policy");
			System.err.println("policy is " + policy);
			boolean success = PolicyManager.sharedManager().save(policy);
	        renderJSON(new ResponseMessage(success));
		}
}
