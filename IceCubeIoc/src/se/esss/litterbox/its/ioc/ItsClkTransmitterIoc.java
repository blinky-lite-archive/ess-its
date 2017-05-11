package se.esss.litterbox.its.ioc;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import se.esss.litterbox.icecube.ioc.serial.IceCubeSerialIoc;

public class ItsClkTransmitterIoc extends IceCubeSerialIoc
{

	public ItsClkTransmitterIoc(String clientId, String mqttBrokerInfoFilePath, String serialPortName, int keepAliveInterval) throws Exception 
	{
		super(clientId, mqttBrokerInfoFilePath, serialPortName, keepAliveInterval);
		// TODO Auto-generated constructor stub
	}
	@Override
	public byte[] getDataFromGizmo() 
	{
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
		if (topic.indexOf("/set/power") >= 0)
		{
			try
			{
				JSONParser parser = new JSONParser();		
				JSONObject jsonData = (JSONObject) parser.parse(new String(message));
				String powerSet = (String) jsonData.get("powerSet");
				writeReadSerialData("powerSet " + powerSet, 10);
			}
			catch (ParseException nfe) {}
		}
		if (topic.indexOf("/set/timeline") >= 0)
		{
			try
			{
				JSONParser parser = new JSONParser();		
				JSONObject jsonData = (JSONObject) parser.parse(new String(message));
				String timelineSet = (String) jsonData.get("timelineSet");
				writeReadSerialData("timelineSet " + timelineSet, 10);
				String freqSet = (String) jsonData.get("freqSet");
				writeReadSerialData("freqSet " + freqSet, 10);
			}
			catch (ParseException nfe) {}
		}
	}
	public static void main(String[] args) throws Exception 
	{
		ItsClkTransmitterIoc ioc = new ItsClkTransmitterIoc("itsClkTrans01Ioc", "itsmqttbroker.dat", "/dev/ttyACM0", 30);
		ioc.setPeriodicPollPeriodmillis(2000);
		ioc.startIoc("itsClkTrans01/set/#", "itsClkTrans01/get/");
	}


}
