package se.esss.litterbox.icecube.simplemqtt.test;

import se.esss.litterbox.icecube.simplemqtt.SimpleMqttClient;

public class SendMessageTest extends SimpleMqttClient
{

	public SendMessageTest(String clientID, String brokerUrl, String brokerKey, String brokerSecret, boolean cleanSession) throws Exception 
	{
		super(clientID, brokerUrl, brokerKey, brokerSecret, cleanSession);
	}
	@Override
	public void newMessage(String topic, byte[] message) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void lostMqttConnection(Throwable arg0) 
	{
		try {reconnect();} catch (Exception e) {setStatus("Error on reconnect: " + arg0.getMessage());}
	}
	public static void main(String[] args) throws Exception  
	{
		String userName = args[0];
		String password = args[1];
		String broker = "tcp://broker.shiftr.io:1883";
		SendMessageTest sendMessageTest = new SendMessageTest("dmcginnis427", broker, userName, password, true);
//		String[] message = {"Hej 8", "unsubscribe","publish", "disconnect"};
//		sendMessageTest.publishMessage("test", "things", message[3].getBytes(), 0, true);
		sendMessageTest.publishMessage("test/things1", "hi1".getBytes(), 0, true);
		sendMessageTest.publishMessage("test/things2", "hi2".getBytes(), 0, true);
		sendMessageTest.publishMessage("test/things3", "hi3".getBytes(), 0, true);
		sendMessageTest.disconnect();
	}


}
