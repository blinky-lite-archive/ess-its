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
	public void newMessage(String arg1, byte[] arg2) 
	{
		System.out.println("Received topic: " + arg1 + " with message: " + new String(arg2));
	}
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception 
	{
		IceCubeSerialIocTester ioCtester = new IceCubeSerialIocTester("ItsIocTester", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5");
		boolean retained = true;
		String data = "2 1 10000 20000";
		JSONObject outputData = new JSONObject();
		outputData.put("channelSet", data);

		ioCtester.publishMessage("itsClkRecvr01/set/channel", outputData.toJSONString().getBytes(), 0, retained);
//		ioCtester.subscribe("itsClkRecvr01/set/channel", 0);
//		ioCtester.subscribe("itsClkRecvr01/get/signal", 0);

	}

}
