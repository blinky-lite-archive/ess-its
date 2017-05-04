package se.esss.litterbox.its.ioc;

import org.json.simple.JSONObject;

import se.esss.litterbox.icecube.ioc.serial.IceCubeSerialIoc;

public class ItsDht11Ioc extends IceCubeSerialIoc
{
	public ItsDht11Ioc(String clientId, String mqttBrokerInfoFilePath, String serialPortName) throws Exception 
	{
		super(clientId, mqttBrokerInfoFilePath, serialPortName);
	}
	@Override
	public byte[] getDataFromGizmo() 
	{
		JSONObject outputData = new JSONObject();
		String command = "tempDht11Get";
		readResponseStringFromSerial(command, 10, outputData);
		
		command = "humidDht11Get";
		readResponseStringFromSerial(command, 10, outputData);

		return outputData.toJSONString().getBytes();
	}
	@Override
	public void handleBrokerMqttMessage(String topic, byte[] message) 
	{
	}
	@Override
	public void lostMqttConnection(Throwable arg0) 
	{
		try {reconnect();} catch (Exception e) {setStatus("Error on reconnect: " + arg0.getMessage());}
	}
	public static void main(String[] args) throws Exception 
	{
		ItsDht11Ioc ioc = new ItsDht11Ioc("itsDht1101Ioc", "itsmqttbroker.dat", "/dev/rfcomm2");
		ioc.setPeriodicPollPeriodmillis(2000);
		ioc.startIoc("itsDht1101/set/#", "itsDht1101/get/cond");
	}
}
