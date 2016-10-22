package se.esss.litterbox.itsllrfgwt.client;


import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EntryPointAppServiceAsync 
{
	void gskelServerTest(String input, boolean debug, String[] debugResponse, AsyncCallback<String[]> callback) ;
}
