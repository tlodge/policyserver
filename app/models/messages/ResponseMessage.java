package models.messages;

public class ResponseMessage {
	
	boolean result;
	String message;
	
	public ResponseMessage(boolean result, String message){
		this.result 	= result;
		this.message 	= message;
	}
}
