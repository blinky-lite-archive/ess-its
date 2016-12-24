package se.esss.litterbox.itsenvmongwt.client;


import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EntryPointAppServiceAsync 
{
	void checkIpAddress(boolean debug, AsyncCallback<String[]> callback);
}
