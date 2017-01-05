package se.esss.litterbox.its.ioc;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import se.esss.litterbox.icecube.serialioc.IceCubeSerialIoc;

public class ItsClkReceiverIoc  extends IceCubeSerialIoc
{

	public ItsClkReceiverIoc(String clientId, String mqttBrokerInfoFilePath, String serialPortName) throws Exception 
	{
		super(clientId, mqttBrokerInfoFilePath, serialPortName);
		// TODO Auto-generated constructor stub
	}
	@Override
	public byte[] getSerialData() 
	{
/*		JSONObject outputData = new JSONObject();
		String command = "signalGet";
		readResponseStringFromSerial(command, 10, outputData);

		return outputData.toJSONString().getBytes();
*/
		return null;
	}

	@Override
	public void handleIncomingMessage(String topic, byte[] message) 
	{
		if (topic.indexOf("/set/address") >= 0)
		{
			try
			{
				JSONParser parser = new JSONParser();		
				JSONObject jsonData = (JSONObject) parser.parse(new String(message));
				String addressSet = (String) jsonData.get("addressSet");
				writeReadSerialData("addressSet " + addressSet, 10);
			}
			catch (ParseException nfe) {}
		}
		if (topic.indexOf("/set/channel") >= 0)
		{
			try
			{
				JSONParser parser = new JSONParser();		
				JSONObject jsonData = (JSONObject) parser.parse(new String(message));
				String channelSet = (String) jsonData.get("channelSet");
				writeReadSerialData("channelSet " + channelSet, 10);
			}
			catch (ParseException nfe) {}
		}
	}
	public static void main(String[] args) throws Exception 
	{
		if (args.length != 1)
		{
			System.out.println("Usage java -jar ItsClkRecvr.jar mainTopic");
			System.exit(1);
		}
		String mainTopic = args[0];
		ItsClkReceiverIoc ioc = new ItsClkReceiverIoc(mainTopic + "Ioc", "itsmqttbroker.dat", "/dev/ttyACM0");
		ioc.setPeriodicPollPeriodmillis(2000);
		ioc.startIoc(mainTopic + "/set/#", mainTopic + "/get/signal");
	}

}
