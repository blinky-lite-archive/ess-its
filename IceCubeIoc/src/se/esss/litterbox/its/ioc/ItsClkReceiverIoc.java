package se.esss.litterbox.its.ioc;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import se.esss.litterbox.icecube.ioc.serial.IceCubeSerialIoc;

public class ItsClkReceiverIoc  extends IceCubeSerialIoc
{

	public ItsClkReceiverIoc(String clientId, String mqttBrokerInfoFilePath, String serialPortName) throws Exception 
	{
		super(clientId, mqttBrokerInfoFilePath, serialPortName);
	}
	@Override
	public byte[] getDataFromGizmo() 
	{
/*		JSONObject outputData = new JSONObject();
		String command = "signalGet";
		readResponseStringFromSerial(command, 10, outputData);

		return outputData.toJSONString().getBytes();
*/
		return null;
	}

	@Override
	public void handleBrokerMqttMessage(String topic, byte[] message) 
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
				for (int ii  = 0; ii < 4; ++ii)
				{
					String chan = Integer.toString(ii + 1);
					String channelData = (String) jsonData.get("channel" + chan);
					writeReadSerialData("channelSet " + chan + " " + channelData, 10);
					try {Thread.sleep(500);} catch (InterruptedException e) {}
				}
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
