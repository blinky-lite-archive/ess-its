package se.esss.litterbox.its.bluemodgwt.client;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EntryPointAppServiceAsync 
{
	void checkIpAddress(boolean debug, AsyncCallback<String[]> callback);
	void getModulatorProtocols(boolean debug, AsyncCallback<String[][]> callback);
}
