package se.esss.litterbox.icecube.simplemqtt.test;

import se.esss.litterbox.icecube.simplemqtt.SimpleMqttClient;

public class GetMessageTest  extends SimpleMqttClient
{
	public GetMessageTest(String clientIdBase, String mqttBrokerInfoFilePath, int keepAliveInterval) throws Exception 
	{
		super(clientIdBase, mqttBrokerInfoFilePath, false, keepAliveInterval);
	}

	@Override
	public void newMessage(String topic, byte[] message) 
	{
		String command = new String(message);
		setStatus(getClientId() + "  got this message: " + command + " at topic " + topic);
	}
	@Override
	public void lostMqttConnection(Throwable arg0) 
	{
		try {reconnect();} catch (Exception e) {setStatus("Error on reconnect: " + arg0.getMessage());}
	}
	public static void main(String[] args) throws Exception  
	{
		GetMessageTest getMessageTest = new GetMessageTest("ItsIocTestReceiver", "itsmqtttestbroker.dat", 1);
		getMessageTest.subscribe("test/things1", 0);
		getMessageTest.subscribe("test/things2", 0);
	}



}
