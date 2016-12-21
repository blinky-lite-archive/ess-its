package se.esss.litterbox.icecube.serialioc;

import org.json.simple.JSONObject;

public class ItsDht11Ioc extends IceCubeSerialIoc
{
	public ItsDht11Ioc(String domain, String clientIdBase, String brokerUrl, String brokerKey, String brokerSecret, String serialPortName) throws Exception 
	{
		super(domain, clientIdBase, brokerUrl, brokerKey, brokerSecret, serialPortName);
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
		ItsDht11Ioc ioc = new ItsDht11Ioc("its", "ItsDht1101Ioc", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5", "/dev/rfcomm2");
		ioc.setPeriodicPollPeriodmillis(2000);
		ioc.startIoc("dht1101/set/#", "dht1101/get/cond");
	}
}
