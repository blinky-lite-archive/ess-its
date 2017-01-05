package se.esss.litterbox.icecube.ioc;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import se.esss.litterbox.icecube.simplemqtt.SimpleMqttClient;


public class IceCubeIocTester  extends SimpleMqttClient
{
	public IceCubeIocTester(String clientIdBase, String mqttBrokerInfoFilePath) throws Exception 
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
		IceCubeIocTester ioCtester = new IceCubeIocTester("ItsIocTester", "itsmqttbroker.dat");

		boolean retained = true;
		JSONObject outputData = new JSONObject();

//		outputData.put("timelineSet", "1 0 0 0 0 0 0");
//		outputData.put("channelSet", "2 1 1000 2000");
//		ioCtester.publishMessage("itsClkTrans01/set/timeline", outputData.toJSONString().getBytes(), 0, retained);
//		ioCtester.publishMessage("itsClkRecvr02/set/channel", outputData.toJSONString().getBytes(), 0, retained);
	
//		ioCtester.subscribe("itsClkTrans01/set/timeline", 0);
		
		outputData.put("rfPowOn", "ON");
		outputData.put("rfFreq", "351.0");
		outputData.put("rfPowLvl", "-2");
		ioCtester.publishMessage("itsRfSigGen01/set/rf", outputData.toJSONString().getBytes(), 0, retained);
		
	}

}
