package se.esss.litterbox.its.cernrfgwt.client.mqttdata;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("mqtt")
public interface MqttService  extends RemoteService
{
	String[][] getJsonData(String topic, boolean debug, String[][] debugResponse) throws Exception;
	String publishJsonData(String topic, String key, String data, boolean debug, String debugResponse) throws Exception;
	byte[] getMessage(String topic, boolean debug, byte[] debugResponse) throws Exception;
	String publishMessage(String topic, byte[] message, boolean debug, String debugResponse) throws Exception;
}
