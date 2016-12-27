package se.esss.litterbox.icecube.serialioc;

import org.json.simple.JSONObject;

import se.esss.litterbox.simplemqttclient.SimpleMqttClient;


public class IceCubeSerialIocTester  extends SimpleMqttClient
{
	public IceCubeSerialIocTester(String clientIdBase, String brokerUrl, String brokerKey, String brokerSecret) throws Exception 
	{
		super(clientIdBase, brokerUrl, brokerKey, brokerSecret, false);
	}
	@Override
	public void connectionLost(Throwable arg0) {}
	@Override
	public void newMessage(String arg1, byte[] arg2) {}
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception 
	{
		IceCubeSerialIocTester ioCtester = new IceCubeSerialIocTester("ItsIocTester", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5");
		boolean retained = true;
		String data = "200";
		JSONObject outputData = new JSONObject();
		outputData.put("samplePeriodSet", data);

		ioCtester.publishMessage("solarMeter01/set/samplePeriod", outputData.toJSONString().getBytes(), 0, retained);

	}

}
