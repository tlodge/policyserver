package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Website;
import models.messages.ResponseMessage;
import play.mvc.Controller;
import policy.PolicyManager;
import play.Logger;

public class Policy extends Controller {

		public static void delete(){
			
			renderJSON(PolicyManager.sharedManager().deleteAll());
		}
		
		public static void save(){
			String policy = params.get("policy");
			Logger.info("policy is %s", policy);
			String identity = PolicyManager.sharedManager().save(policy);
			String success = identity == null ? "failure" : "success";
	        renderJSON(new ResponseMessage(success, identity));
		}
}
