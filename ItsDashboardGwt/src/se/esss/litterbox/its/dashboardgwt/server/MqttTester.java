package se.esss.litterbox.its.dashboardgwt.server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import se.esss.litterbox.icecube.simplemqtt.SimpleMqttClient;


public class MqttTester  extends SimpleMqttClient
{
	public MqttTester(String clientIdBase, String mqttBrokerInfoFilePath) throws Exception 
	{
		super(clientIdBase, mqttBrokerInfoFilePath, false);
	}
	@Override
	public void connectionLost(Throwable arg0) {}
	@Override
	public void newMessage(String arg1, byte[] arg2) 
	{
		System.out.println("Received topic: " + arg1 + " with message: " + new String(arg2));
		if (arg1.indexOf("/get/info") >= 0)
		{
			try
			{
				JSONParser parser = new JSONParser();		
				JSONObject jsonData = (JSONObject) parser.parse(new String(arg2));
				String key1 = (String) jsonData.get("key1");
				System.out.println("key1 = " + key1);
			}
			catch (ParseException nfe) {nfe.printStackTrace();}
		}
	}
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception 
	{
		MqttTester mqttTester = new MqttTester("MqttTesterIoc", "../itsmqttbroker.dat");

		boolean retained = true;
		JSONObject outputData = new JSONObject();

		outputData.put("pressure", "1.0");
		mqttTester.publishMessage("itspressureMeter/get", outputData.toJSONString().getBytes(), 0, retained);

		
		
	}

}
