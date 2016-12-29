package se.esss.litterbox.icecube.ioc;

import org.json.simple.JSONObject;

import se.esss.litterbox.icecube.serialioc.IceCubeSerialIoc;

public class ItsDht11Ioc extends IceCubeSerialIoc
{
	public ItsDht11Ioc(String domain, String brokerUrl, String brokerKey, String brokerSecret, String serialPortName) throws Exception 
	{
		super(domain, brokerUrl, brokerKey, brokerSecret, serialPortName);
	}
	@Override
	public byte[] getSerialData() 
	{
		JSONObject outputData = new JSONObject();
		String command = "tempDht11Get";
		readResponseStringFromSerial(command, 10, outputData);
		
		command = "humidDht11Get";
		readResponseStringFromSerial(command, 10, outputData);

		return outputData.toJSONString().getBytes();
	}
	@Override
	public void handleIncomingMessage(String topic, byte[] message) 
	{
	}
	public static void main(String[] args) throws Exception 
	{
		ItsDht11Ioc ioc = new ItsDht11Ioc("itsDht1101Ioc", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5", "/dev/rfcomm2");
		ioc.setPeriodicPollPeriodmillis(2000);
		ioc.startIoc("itsDht1101/set/#", "itsDht1101/get/cond");
	}
}
