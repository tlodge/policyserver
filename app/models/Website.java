package models;

public class Website {
	public String 	source;
	public String 	url;
	public Long 	timestamp;
	
	public Website(String source, String url, Long timestamp){
		this.source = source;
		this.url = url;
		this.timestamp = timestamp;
	}
}