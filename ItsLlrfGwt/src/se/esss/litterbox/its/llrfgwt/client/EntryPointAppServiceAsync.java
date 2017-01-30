package se.esss.litterbox.its.llrfgwt.client;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EntryPointAppServiceAsync 
{
	void checkIpAddress(boolean debug, AsyncCallback<String[]> callback);
}
