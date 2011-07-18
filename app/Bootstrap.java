import java.util.logging.Level;

import homework.PollingThread;
import java.util.logging.Logger;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;


@OnApplicationStart
public class Bootstrap extends Job{
	public void doJob(){
		
		new PollingThread().start();
		
	}
}