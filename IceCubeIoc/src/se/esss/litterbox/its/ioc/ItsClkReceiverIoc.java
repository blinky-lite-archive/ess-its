package se.esss.litterbox.its.ioc;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import se.esss.litterbox.icecube.ioc.serial.IceCubeSerialIoc;

public class ItsClkReceiverIoc  extends IceCubeSerialIoc
{
	JSONObject setChannelJsonData = null;
	String mainTopic;

	public ItsClkReceiverIoc(String mainTopic, String mqttBrokerInfoFilePath, String serialPortName, int keepAliveInterval) throws Exception 
	{
		super(mainTopic + "Ioc", mqttBrokerInfoFilePath, serialPortName, keepAliveInterval);
		this.mainTopic = mainTopic;
	}
	@SuppressWarnings("unchecked")
	private void initialize() throws Exception
	{
		setChannelJsonData = new JSONObject();
		for (int ii  = 0; ii < 4; ++ii)
		{
			String chan = Integer.toString(ii + 1);
			setChannelJsonData.put("channel" + chan, "1 100 200");
		}
		publishMessage(mainTopic + "/set/channel", setChannelJsonData.toJSONString().getBytes(), 0, true);
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
				setChannelJsonData = (JSONObject) parser.parse(new String(message));
				for (int ii  = 0; ii < 4; ++ii)
				{
					String chan = Integer.toString(ii + 1);
					String channelData = (String) setChannelJsonData.get("channel" + chan);
					writeReadSerialData("channelSet " + chan + " " + channelData, 10);
					try {Thread.sleep(500);} catch (InterruptedException e) {}
				}
				publishChanParameters();
			}
			catch (ParseException nfe) {} 
		}
		if (topic.indexOf("/get/channel") >= 0)
		{
			try 
			{
				publishMessage(mainTopic + "/echo/channel", setChannelJsonData.toJSONString().getBytes(), 0, true);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
			publishChanParameters();
		}
	}
	@SuppressWarnings("unchecked")
	private void publishChanParameters()
	{
		JSONObject outputData = new JSONObject();
		for (int ii  = 0; ii < 4; ++ii)
		{
			String chan = Integer.toString(ii + 1);
			String channelData = (String) setChannelJsonData.get("channel" + chan);
			
			String[] channelDataSplit = channelData.split(" ");
			int startTime = Integer.parseInt(channelDataSplit[1]);
			int stopTime = Integer.parseInt(channelDataSplit[2]);
			outputData.put("chanStart" + chan, Integer.toString(startTime));
			outputData.put("chanWidth" + chan, Integer.toString(stopTime - startTime));
		}
		try {publishMessage(mainTopic + "/archive/channel", outputData.toJSONString().getBytes(), 0, true);}catch (Exception e) {e.printStackTrace();}
	}
	public static void main(String[] args) throws Exception 
	{
		if (args.length != 1)
		{
			System.out.println("Usage java -jar ItsClkRecvr.jar mainTopic");
			System.exit(1);
		}
		String mainTopic = args[0];
		ItsClkReceiverIoc ioc = new ItsClkReceiverIoc(mainTopic, "itsmqttbroker.dat", "/dev/ttyACM0", 30);
		ioc.setPeriodicPollPeriodmillis(2000);
		ioc.startIoc(mainTopic + "/#", mainTopic + "/get/signal");
		ioc.initialize();
	}

}
