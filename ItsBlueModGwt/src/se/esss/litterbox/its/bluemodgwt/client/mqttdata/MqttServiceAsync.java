package se.esss.litterbox.its.bluemodgwt.client.mqttdata;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MqttServiceAsync 
{
	void getJsonArray(String topic, boolean debug, String[][] debugResponse, AsyncCallback<String[][]> callback);
	void getMessage(String topic, boolean debug, byte[] debugResponse, AsyncCallback<byte[]> callback);
	void publishMessage(String topic, byte[] message, boolean settingsEnabled, boolean debug, String debugResponse, AsyncCallback<String> callback);
	void publishJsonArray(String topic, String[][] jsonArray, boolean settingsEnabled, boolean debug,String debugResponse, AsyncCallback<String> callback);
}
