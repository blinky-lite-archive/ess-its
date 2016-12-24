package se.esss.litterbox.itsenvmongwt.server;

import java.util.Iterator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import se.esss.litterbox.itsenvmongwt.client.MqttService;

@SuppressWarnings("serial")
public class MqttServiceImpl extends RemoteServiceServlet implements MqttService
{
	MqttServiceImpClient mqttClient;
	byte[] solarMessage = "NotStarted".getBytes();
	String[] topics = {"geiger01/get/cpm", "dht1101/get/cond", "solarMeter01/get/cond" };
	byte[][] messages;
	String domain = "its";
	public void init()
	{
		boolean cleanSession = false;
		int subscribeQos = 0;
		messages = new byte[topics.length][];
		try 
		{
			mqttClient = new MqttServiceImpClient(this, "ItsEnvMonGwt", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5", cleanSession);
			for (int ii = 0; ii < topics.length; ++ii)
			{	
				messages[ii] = "noData".getBytes();
				mqttClient.subscribe(domain, topics[ii], subscribeQos);
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public void destroy()
	{
		try {mqttClient.unsubscribeAll();} catch (Exception e) {mqttClient = null;}
		try {mqttClient.disconnect();} catch (Exception e) {mqttClient = null;}
	}
	public void setMessage(String domain, String topic, byte[] message)
	{
		if (domain.equals(this.domain))
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
	}
	@SuppressWarnings("rawtypes")
	@Override
	public String[][] nameValuePairArray(boolean debug, String[][] debugResponse) throws Exception 
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
}
