package se.esss.litterbox.its.watersystemgwt.client;


import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EntryPointAppServiceAsync 
{
	void checkIpAddress(boolean debug, AsyncCallback<String[]> callback);
	void gskelServerTest(String name, boolean debug, String[] debugResponse, AsyncCallback<String[]> callback);
}
