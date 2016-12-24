package se.esss.litterbox.itsenvmongwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MqttServiceAsync 
{
	void nameValuePairArray(boolean debug, String[][] debugResponse, AsyncCallback<String[][]> callback);
}
