package se.esss.litterbox.icecube.ioc;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import se.esss.litterbox.icecube.simplemqtt.SimpleMqttClient;


public class IceCubeIocTester  extends SimpleMqttClient
{
	public IceCubeIocTester(String clientIdBase, String brokerUrl, String brokerKey, String brokerSecret) throws Exception 
	{
		super(clientIdBase, brokerUrl, brokerKey, brokerSecret, false);
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
		String userName = args[0];
		String password = args[1];
		String broker = "tcp://broker.shiftr.io:1883";
		IceCubeIocTester ioCtester = new IceCubeIocTester("ItsIocTester", broker, userName, password);

		boolean retained = false;
		JSONObject outputData = new JSONObject();
//		outputData.put("rfFreq", "352.21");
//		outputData.put("dutyCycle", "0.2");
//		outputData.put("modPeriod", "0.5");
//		ioCtester.publishMessage("itsPowerMeter01/set/init", outputData.toJSONString().getBytes(), 0, retained);
//		ioCtester.subscribe("itsPowerMeter01/get", 0);

//		outputData.put("timelineSet", "1 0 0 0 0 0 0");
//		outputData.put("channelSet", "2 1 1000 2000");
//		ioCtester.publishMessage("itsClkTrans01/set/timeline", outputData.toJSONString().getBytes(), 0, retained);
//		ioCtester.publishMessage("itsClkRecvr01/set/channel", outputData.toJSONString().getBytes(), 0, retained);
	
//		outputData.put("rfPowLvl", "6");
//		outputData.put("rfPowOn", "ON");
//		ioCtester.publishMessage("itsRfSigGen01/set/rf", outputData.toJSONString().getBytes(), 1, retained);

		outputData.put("rfPowLvl", "42");
		ioCtester.publishMessage("itsPythonTest/set/rf", outputData.toJSONString().getBytes(), 0, retained);
		ioCtester.subscribe("itsPythonTest/get", 0);
		
		
	}

}
