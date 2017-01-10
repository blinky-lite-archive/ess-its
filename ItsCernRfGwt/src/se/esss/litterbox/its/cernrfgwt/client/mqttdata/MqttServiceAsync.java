package se.esss.litterbox.its.cernrfgwt.client.mqttdata;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MqttServiceAsync 
{
	void getJsonData(String topic, boolean debug, String[][] debugResponse, AsyncCallback<String[][]> callback);
	void publishJsonData(String topic, String key, String data, boolean debug, String  debugResponse, AsyncCallback<String> callback);
	void getMessage(String topic, boolean debug, byte[] debugResponse, AsyncCallback<byte[]> callback);
	void publishMessage(String topic, byte[] message, boolean debug, String debugResponse, AsyncCallback<String> callback);
}
