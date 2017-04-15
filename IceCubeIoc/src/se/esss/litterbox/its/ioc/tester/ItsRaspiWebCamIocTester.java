package se.esss.litterbox.its.ioc.tester;

import org.json.simple.JSONObject;

import se.esss.litterbox.icecube.simplemqtt.SimpleMqttClient;

public class ItsRaspiWebCamIocTester extends SimpleMqttClient
{
	public ItsRaspiWebCamIocTester(String clientId, String mqttBrokerInfoFilePath, boolean cleanSession) throws Exception 
	{
		super(clientId, mqttBrokerInfoFilePath, cleanSession);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void newMessage(String topic, byte[] message) 
	{
		// TODO Auto-generated method stub
		
	}
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception 
	{
		ItsRaspiWebCamIocTester iocTest = new ItsRaspiWebCamIocTester("itsRaspiWebCamIocTester", "../itsmqtttestbroker.dat", false);
		boolean retained = true;
		JSONObject outputData = new JSONObject();
		outputData.put("width", "800");
		outputData.put("height", "600");
		outputData.put("rot", "180");
//		outputData.put("interval", "2000");
		outputData.put("timeout", "5000");
//		outputData.put("exp", "night");
		iocTest.publishMessage("itsIceCube08Cam/set", outputData.toJSONString().getBytes(), 0, retained);

	}


}
