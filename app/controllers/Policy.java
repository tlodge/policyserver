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
			String identity = PolicyManager.sharedManager().save(policy);
			String success = identity == null ? "failure" : "success";
	        renderJSON(new ResponseMessage(success, identity));
		}
}
