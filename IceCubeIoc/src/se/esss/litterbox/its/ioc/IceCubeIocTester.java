package se.esss.litterbox.its.ioc;

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

		outputData.put("powerSet", "0");
		ioCtester.publishMessage("itsClkTrans01/set/power", outputData.toJSONString().getBytes(), 0, retained);

		outputData.put("timelineSet", "1 1 1 1 1 1 1");
		outputData.put("freqSet", "14");
//		ioCtester.publishMessage("itsClkTrans01/set/timeline", outputData.toJSONString().getBytes(), 0, retained);

//		outputData.put("power", "1");
//		ioCtester.publishMessage("homeSingleRelayIOC/set/power", outputData.toJSONString().getBytes(), 0, retained);
	
		outputData.put("channel1", "1 1000 1010");
		outputData.put("channel2", "1 1190 3790");
		outputData.put("channel3", "1 1250 1260");
		outputData.put("channel4", "0 1000 2000");
//		ioCtester.publishMessage("itsClkRecvr01/set/channel", outputData.toJSONString().getBytes(), 0, retained);

		outputData.put("channel1", "1 100 1000");
		outputData.put("channel2", "1 100 1000");
		outputData.put("channel3", "0 1000 2000");
		outputData.put("channel4", "0 1000 2000");
//		ioCtester.publishMessage("itsClkRecvr02/set/channel", outputData.toJSONString().getBytes(), 0, retained);

//		ioCtester.subscribe("itsRfSigGen01/set/rf", 0);
		
		outputData.put("rfPowOn", "OFF");
		outputData.put("rfFreq", "353.0");
		outputData.put("rfPowLvl", "2.5");
//		ioCtester.publishMessage("itsRfSigGen01/set/rf", outputData.toJSONString().getBytes(), 0, retained);
		
	}

}
