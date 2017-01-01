package se.esss.litterbox.its.envmongwt.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("mqtt")
public interface MqttService  extends RemoteService
{
	String[][] getNameValuePairArray(boolean debug, String[][] debugResponse) throws Exception;
	String[] setNameValuePairArray(String[] nameValuePairArray, boolean debug, String[] debugResponse) throws Exception;
}
