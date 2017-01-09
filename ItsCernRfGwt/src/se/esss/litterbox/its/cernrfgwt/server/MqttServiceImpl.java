package se.esss.litterbox.its.cernrfgwt.server;

import java.io.File;
import java.util.Iterator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import se.esss.litterbox.its.cernrfgwt.client.MqttService;


@SuppressWarnings("serial")
public class MqttServiceImpl extends RemoteServiceServlet implements MqttService
{
	MqttServiceImpClient mqttClient;
	String[] topics = {"itsPowerMeter01/get"};
	byte[][] messages;
	public void init()
	{
		boolean cleanSession = false;
		int subscribeQos = 0;
		messages = new byte[topics.length][];
		try 
		{
//			mqttClient = new MqttServiceImpClient(this, "ItsCernRfGwt", getMqttDataPath(), cleanSession);
			mqttClient = new MqttServiceImpClient(this, "ItsCernRfGwt", "tcp://broker.shiftr.io:1883", "45357989", "bad7813039046c87", cleanSession);
			for (int ii = 0; ii < topics.length; ++ii)
			{	
				messages[ii] = "noData".getBytes();
				mqttClient.subscribe(topics[ii], subscribeQos);
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	private String getMqttDataPath() throws Exception
	{
		File tmpFile = new File(getServletContext().getRealPath("./"));
		tmpFile = new File(tmpFile.getParent());
		tmpFile = new File(tmpFile.getParent());
		return tmpFile.getPath() + "/itsmqttbroker.dat";
		
	}
	public void destroy()
	{
		try {mqttClient.unsubscribeAll();} catch (Exception e) {mqttClient = null;}
		try {mqttClient.disconnect();} catch (Exception e) {mqttClient = null;}
	}
	public void setMessage(String topic, byte[] message)
	{
		int itopic = -1;
		for (int ii = 0; ii < topics.length; ++ii) 
			if (topic.equals(topics[ii])) itopic = ii;
		if (itopic >= 0)
		{
			messages[itopic] = new byte[message.length];
			for (int ii = 0; ii < message.length; ++ii) messages[itopic][ii] = message[ii];
		}
	}
	@SuppressWarnings("rawtypes")
	@Override
	public String[][] getNameValuePairArray(boolean debug, String[][] debugResponse) throws Exception 
	{
		JSONParser parser = new JSONParser();		
		JSONObject jsonData;
		int numKeys = 0;
		for (int ii = 0; ii < topics.length; ++ii)
		{
			jsonData = (JSONObject) parser.parse(new String(messages[ii]));
			numKeys = numKeys + jsonData.keySet().size();
		}
		String[][] data = new String[numKeys][2];
		int ikey = 0;
		for (int ii = 0; ii < topics.length; ++ii)
		{
			jsonData = (JSONObject) parser.parse(new String(messages[ii]));
			Iterator iterJsonData = jsonData.keySet().iterator();
			while (iterJsonData.hasNext())
			{
				data[ikey][0] = (String) iterJsonData.next();
				data[ikey][1] = (String) jsonData.get(data[ikey][0]);
				++ikey;
			}
		}
		return data;
	}
	@SuppressWarnings("unchecked")
	@Override
	public String[] setNameValuePairArray(String[] nameValuePairArray, boolean debug, String[] debugResponse) throws Exception
	{
		JSONObject outputData = new JSONObject();
		outputData.put(nameValuePairArray[1], nameValuePairArray[2]);
// QOS of 1 will not work on super dev mode because it will try to write to server
		mqttClient.publishMessage(nameValuePairArray[0], outputData.toJSONString().getBytes(), 0, true);
		return nameValuePairArray;

	}
}
