import homework.PollingThread;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;


@OnApplicationStart
public class Bootstrap extends Job{
	public void doJob(){
		System.err.println("monitoring hw data");
		new PollingThread().start();
	}
}