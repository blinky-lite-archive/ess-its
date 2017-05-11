package se.esss.litterbox.icecube.simplemqtt.test;

import se.esss.litterbox.icecube.simplemqtt.SimpleMqttClient;

public class SendMessageTest extends SimpleMqttClient
{

	public SendMessageTest(String clientIdBase, String mqttBrokerInfoFilePath, int keepAliveInterval) throws Exception 
	{
		super(clientIdBase, mqttBrokerInfoFilePath, false, keepAliveInterval);
	}
	@Override
	public void newMessage(String topic, byte[] message) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void lostMqttConnection(Throwable arg0) 
	{
//		try {reconnect();} catch (Exception e) {setStatus("Error on reconnect: " + arg0.getMessage());}
	}
	public static void main(String[] args) throws Exception  
	{
		SendMessageTest sendMessageTest = new SendMessageTest("ItsIocTestSender", "itsmqtttestbroker.dat", 1);
//		String[] message = {"Hej 8", "unsubscribe","publish", "disconnect"};
//		sendMessageTest.publishMessage("test", "things", message[3].getBytes(), 0, true);
		sendMessageTest.publishMessage("test/things1", "hi1".getBytes(), 0, true);
		sendMessageTest.publishMessage("test/things2", "hi2".getBytes(), 0, true);
		sendMessageTest.publishMessage("test/things3", "hi3".getBytes(), 0, true);
		sendMessageTest.disconnect();
	}


}
