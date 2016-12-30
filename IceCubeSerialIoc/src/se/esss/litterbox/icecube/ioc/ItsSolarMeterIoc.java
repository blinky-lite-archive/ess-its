package se.esss.litterbox.icecube.ioc;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import se.esss.litterbox.icecube.serialioc.IceCubeSerialIoc;

public class ItsSolarMeterIoc extends IceCubeSerialIoc
{
	public ItsSolarMeterIoc(String domain, String brokerUrl, String brokerKey, String brokerSecret, String serialPortName) throws Exception 
	{
		super(domain, brokerUrl, brokerKey, brokerSecret, serialPortName);
	}
	@Override
	public byte[] getSerialData() 
	{
		JSONObject outputData = new JSONObject();
		String command = "photoGet";
		readResponseStringFromSerial(command, 10, outputData);
		
		command = "photoAvgGet";
		readResponseStringFromSerial(command, 10, outputData);

		command = "tempGet";
		readResponseStringFromSerial(command, 10, outputData);
		
		return outputData.toJSONString().getBytes();
	}
	@Override
	public void handleIncomingMessage(String topic, byte[] message) 
	{
		if (topic.indexOf("/set/avgPeriod") >= 0)
		{
			try
			{
				JSONParser parser = new JSONParser();		
				JSONObject jsonData = (JSONObject) parser.parse(new String(message));
				String avgPeriodSet = (String) jsonData.get("avgPeriodSet");
				writeReadSerialData("avgPeriodSet " + avgPeriodSet, 10);
			}
			catch (ParseException nfe) {}
		}
		if (topic.indexOf("/set/samplePeriod") >= 0)
		{
			try
			{
				JSONParser parser = new JSONParser();		
				JSONObject jsonData = (JSONObject) parser.parse(new String(message));
				String samplePeriod = (String) jsonData.get("samplePeriodSet");
				writeReadSerialData("samplePeriodSet " + samplePeriod, 10);
			}
			catch (ParseException nfe) {}
		}
	}
	public static void main(String[] args) throws Exception 
	{
		String userName = args[0];
		String password = args[1];
		String broker = "tcp://broker.shiftr.io:1883";
		ItsSolarMeterIoc ioc = new ItsSolarMeterIoc("itsSolarMeter01Ioc", broker, userName, password, "/dev/rfcomm3");
		ioc.setPeriodicPollPeriodmillis(2000);
		ioc.startIoc("itsSolarMeter01/set/#", "itsSolarMeter01/get/cond");
	}
}
