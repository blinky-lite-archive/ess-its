package se.esss.litterbox.itsllrfgwt.client;


import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EntryPointAppServiceAsync 
{
	void getStringArrayFromProtocol(boolean settings, boolean debug, AsyncCallback<String[]> callback);
	void echoSettings(boolean debug, AsyncCallback<byte[]> callback);
}
