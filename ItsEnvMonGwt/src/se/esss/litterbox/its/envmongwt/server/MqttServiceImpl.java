package se.esss.litterbox.its.envmongwt.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import se.esss.litterbox.its.envmongwt.client.MqttService;

@SuppressWarnings("serial")
public class MqttServiceImpl extends RemoteServiceServlet implements MqttService
{
	MqttServiceImpClient mqttClient;
	byte[] solarMessage = "NotStarted".getBytes();
	String[] topics = {"itsGeiger01/get/cpm", "itsDht1101/get/cond", "itsSolarMeter01/get/cond" };
	byte[][] messages;
	String domain = "its";
	public void init()
	{
		boolean cleanSession = false;
		int subscribeQos = 0;
		messages = new byte[topics.length][];
		try 
		{
			JSONObject mqttdata = getMqttData();
			String broker = (String) mqttdata.get("broker") + ":" + (String) mqttdata.get("brokerport");
			mqttClient = new MqttServiceImpClient(this, "ItsEnvMonGwt", broker, (String) mqttdata.get("key"), (String) mqttdata.get("secret"), cleanSession);
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
	private JSONObject getMqttData() throws Exception
	{
		File tmpFile = new File(getServletContext().getRealPath("./"));
		tmpFile = new File(tmpFile.getParent());
		tmpFile = new File(tmpFile.getParent());
		String line;
	    InputStream fis = new FileInputStream(tmpFile.getPath() + "/itsmqttbroker.dat");
	    InputStreamReader isr = new InputStreamReader(fis);
	    BufferedReader br = new BufferedReader(isr);
	    line = br.readLine();
	    br.close();
		return (JSONObject) new JSONParser().parse(line);
		
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
		mqttClient.publishMessage(nameValuePairArray[0], outputData.toJSONString().getBytes(), 0, false);
		return nameValuePairArray;

	}
}
