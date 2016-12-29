package se.esss.litterbox.icecube.ioc;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import se.esss.litterbox.icecube.serialioc.IceCubeSerialIoc;

public class ItsClkReceiverIoc  extends IceCubeSerialIoc
{

	public ItsClkReceiverIoc(String clientId, String brokerUrl, String brokerKey, String brokerSecret, String serialPortName) throws Exception 
	{
		super(clientId, brokerUrl, brokerKey, brokerSecret, serialPortName);
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
		ItsClkReceiverIoc ioc = new ItsClkReceiverIoc("itsClkRecvr01Ioc", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5", "/dev/ttyACM0");
		ioc.setPeriodicPollPeriodmillis(2000);
		ioc.startIoc("itsClkRecvr01/set/#", "itsClkRecvr01/get/signal");
	}

}
