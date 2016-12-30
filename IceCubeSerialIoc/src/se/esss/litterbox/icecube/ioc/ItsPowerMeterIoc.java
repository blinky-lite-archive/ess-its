package se.esss.litterbox.icecube.ioc;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import se.esss.litterbox.icecube.usbtmcioc.IceCubeUsbtmcIoc;

public class ItsPowerMeterIoc  extends IceCubeUsbtmcIoc
{

	public ItsPowerMeterIoc(String clientId, String brokerUrl, String brokerKey, String brokerSecret,String devNickName, int vendorId, int productId) throws Exception 
	{
		super(clientId, brokerUrl, brokerKey, brokerSecret, devNickName, vendorId, productId);
		writeUsbtmcData("SYST:PRES");
		try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
	}
	@Override
	public byte[] getUsbtmcData() 
	{
		JSONObject outputData = new JSONObject();
		
		readResponseStringFromUsbtmc("FETC1?", "power1", outputData);
		readResponseStringFromUsbtmc("FETC2?", "power2", outputData);
		return outputData.toJSONString().getBytes();
	}
	@Override
	public void handleIncomingMessage(String topic, byte[] message) 
	{
		if (topic.indexOf("/set/preset") >= 0)
		{
			writeUsbtmcData("SYST:PRES");
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		}
		if (topic.indexOf("/set/init") >= 0)
		{
			try
			{
				JSONParser parser = new JSONParser();		
				JSONObject jsonData = (JSONObject) parser.parse(new String(message));
				writeUsbtmcData("SENS1:FREQ " + (String) jsonData.get("rfFreq") + " MHZ");
				writeUsbtmcData("SENS1:CORR:DCYC " + (String) jsonData.get("dutyCycle"));
				writeUsbtmcData("SENS1:CORR:DCYC:STAT ON");
				writeUsbtmcData("SENS1:POW:AVG:APER " + (String) jsonData.get("modPeriod"));
				writeUsbtmcData("SENS2:FREQ " + (String) jsonData.get("rfFreq") + " MHZ");
				writeUsbtmcData("SENS2:CORR:DCYC " + (String) jsonData.get("dutyCycle"));
				writeUsbtmcData("SENS2:CORR:DCYC:STAT ON");
				writeUsbtmcData("SENS2:POW:AVG:APER " + (String) jsonData.get("modPeriod"));
			}
			catch (ParseException nfe) {}
		}
	}
	public static void main(String[] args) throws Exception 
	{
		String userName = args[0];
		String password = args[1];
		String broker = "tcp://broker.shiftr.io:1883";
		ItsPowerMeterIoc ioc = new ItsPowerMeterIoc("itsPowerMeter01Ioc", broker, userName, password, "Power Meter", 2733, 27);
		ioc.setPeriodicPollPeriodmillis(2000);
		ioc.startIoc("itsPowerMeter01/set/#", "itsPowerMeter01/get");
	}
}
