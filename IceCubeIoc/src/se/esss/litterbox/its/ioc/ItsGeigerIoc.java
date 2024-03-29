package se.esss.litterbox.its.ioc;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import se.esss.litterbox.icecube.ioc.serial.IceCubeSerialIoc;

public class ItsGeigerIoc extends IceCubeSerialIoc
{
	public ItsGeigerIoc(String domain, String mqttBrokerInfoFilePath, String serialPortName, int keepAliveInterval) throws Exception 
	{
		super(domain, mqttBrokerInfoFilePath, serialPortName, keepAliveInterval);
	}
	@Override
	public byte[] getDataFromGizmo() 
	{
		JSONObject outputData = new JSONObject();
		String command = "cpmGet";
		readResponseStringFromSerial(command, 10, outputData);

		return outputData.toJSONString().getBytes();
	}
	@Override
	public void handleBrokerMqttMessage(String topic, byte[] message) 
	{
		if (topic.indexOf("/set/avg") >= 0)
		{
			try
			{
				JSONParser parser = new JSONParser();		
				JSONObject jsonData = (JSONObject) parser.parse(new String(message));
				String avgSet = (String) jsonData.get("avgSet");
				writeReadSerialData("avgSet " + avgSet, 10);
			}
			catch (ParseException nfe) {}
		}
	}
	public static void main(String[] args) throws Exception 
	{
		ItsGeigerIoc ioc = new ItsGeigerIoc("itsGeiger01Ioc",  "itsmqttbroker.dat", "/dev/rfcomm1", 30);
		ioc.setPeriodicPollPeriodmillis(2000);
		ioc.startIoc("itsGeiger01/set/#", "itsGeiger01/get/cpm");
	}
}
