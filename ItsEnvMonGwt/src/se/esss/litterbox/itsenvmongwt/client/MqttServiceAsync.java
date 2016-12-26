package se.esss.litterbox.itsenvmongwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MqttServiceAsync 
{
	void getNameValuePairArray(boolean debug, String[][] debugResponse, AsyncCallback<String[][]> callback);
	void setNameValuePairArray(String[] nameValuePairArray, boolean debug, String[] debugResponse, AsyncCallback<String[]> callback);
}
