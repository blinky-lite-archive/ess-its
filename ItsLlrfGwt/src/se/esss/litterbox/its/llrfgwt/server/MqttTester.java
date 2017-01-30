package se.esss.litterbox.its.llrfgwt.server;

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
		MqttTester mqttTester = new MqttTester("MqttLlrfTesterIoc", "../itsmqttbroker.dat");

		boolean retained = true;
		JSONObject outputData = new JSONObject();

		outputData.put("channel1", "1 2000 2010");
		outputData.put("channel2", "1 2190 4790");
		outputData.put("channel3", "1 2250 2260");
		outputData.put("channel4", "1 1100 2000");
		mqttTester.publishMessage("itsClkRecvr01/set/channel", outputData.toJSONString().getBytes(), 0, retained);

		outputData.put("rfPowOn", "OFF");
		outputData.put("rfFreq", "353.0");
		outputData.put("rfPowLvl", "2.5");
		mqttTester.publishMessage("itsRfSigGen01/set/rf", outputData.toJSONString().getBytes(), 0, retained);

		
		outputData.put("power1", "42.5");
		outputData.put("power1", "39.6");
		mqttTester.publishMessage("itsPowerMeter01/get", outputData.toJSONString().getBytes(), 0, retained);
	}

}
