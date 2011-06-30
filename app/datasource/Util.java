package datasource;

public class Util {

	public static long convertTs(String ts){
		final int start = ts.indexOf('@');
		final int end = ts.indexOf('@', start + 1);
		final String time = ts.substring(start + 1, end);
		return  Long.parseLong(time, 16) / 1000000;
	}
}
