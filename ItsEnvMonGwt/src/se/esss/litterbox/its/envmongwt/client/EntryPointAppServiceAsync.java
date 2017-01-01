package se.esss.litterbox.its.envmongwt.client;


import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EntryPointAppServiceAsync 
{
	void checkIpAddress(boolean debug, AsyncCallback<String[]> callback);
}
