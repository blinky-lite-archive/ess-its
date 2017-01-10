package se.esss.litterbox.its.cernrfgwt.server;

import java.io.File;
import java.util.Iterator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import se.esss.litterbox.its.cernrfgwt.client.mqttdata.MqttService;


@SuppressWarnings("serial")
public class MqttServiceImpl extends RemoteServiceServlet implements MqttService
{
	MqttServiceImpClient mqttClient;
	String[] topics = {"itsPowerMeter01/get", "itsCernMod/get/mod", "itsCernMod/set/mod", "itsClkRecvr01/set/channel", "itsClkRecvr02/set/channel", "itsRfSigGen01/set/rf"};
	byte[][] messages;
	public void init()
	{
		boolean cleanSession = false;
		int subscribeQos = 0;
		messages = new byte[topics.length][];
		try 
		{
//			mqttClient = new MqttServiceImpClient(this, "ItsCernRfGwt", getMqttDataPath(), cleanSession);
			mqttClient = new MqttServiceImpClient(this, "ItsCernRfGwt", "tcp://broker.shiftr.io:1883", "xx", "xx", cleanSession);
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
		int itopic = getTopicIndex(topic);
		if (itopic >= 0)
		{
			messages[itopic] = new byte[message.length];
			for (int ii = 0; ii < message.length; ++ii) messages[itopic][ii] = message[ii];
		}
	}
	private int getTopicIndex(String topic)
	{
		int itopic = -1;
		for (int ii = 0; ii < topics.length; ++ii) 
			if (topic.equals(topics[ii])) itopic = ii;
		return itopic;
		
	}
	@SuppressWarnings("rawtypes")
	@Override
	public String[][] getJsonData(String topic, boolean debug, String[][] debugResponse) throws Exception  
	{
		int itopic = getTopicIndex(topic);
		if (itopic < 0) throw new Exception(topic + " not found");
		JSONParser parser = new JSONParser();		
		JSONObject jsonData;
		try {jsonData = (JSONObject) parser.parse(new String(messages[itopic]));} 
		catch (ParseException e) {throw new Exception("Cannot JSON parse the data on " + topic);}
		int numKeys = jsonData.keySet().size();
		String[][] data = new String[numKeys][2];
		int ikey = 0;
		try {jsonData = (JSONObject) parser.parse(new String(messages[itopic]));} 
		catch (ParseException e) {throw new Exception("Cannot JSON parse the data on " + topic);}
		Iterator iterJsonData = jsonData.keySet().iterator();
		while (iterJsonData.hasNext())
		{
			data[ikey][0] = (String) iterJsonData.next();
			data[ikey][1] = (String) jsonData.get(data[ikey][0]);
			++ikey;
		}
		return data;
	}
	@SuppressWarnings("unchecked")
	@Override
	public String publishJsonData(String topic, String key, String data, boolean debug, String debugResponse) throws Exception
	{
		JSONObject outputData = new JSONObject();
		outputData.put(key, data);
		mqttClient.publishMessage(topic, outputData.toJSONString().getBytes(), 0, true);
		return "ok";

	}
	@Override
	public byte[] getMessage(String topic, boolean debug, byte[] debugResponse) throws Exception
	{
		int itopic = getTopicIndex(topic);
		if (itopic < 0) throw new Exception(topic + " not found");
		if (messages[itopic].length < 1) throw new Exception("Zero length byte array on " + topic);
		return messages[itopic];
	}
	@Override
	public String publishMessage(String topic, byte[] message, boolean debug, String debugResponse) throws Exception
	{
		mqttClient.publishMessage(topic, message, 0, true);
		return "ok";
	}
}
