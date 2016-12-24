package se.esss.litterbox.itsenvmongwt.server;

import se.esss.litterbox.simplemqttclient.SimpleMqttClient;

public class GetMessageTest  extends SimpleMqttClient
{
	public GetMessageTest(String clientID, String brokerUrl, String brokerKey, String brokerSecret, boolean cleanSession) throws Exception 
	{
		super(clientID, brokerUrl, brokerKey, brokerSecret, cleanSession);
	}

	@Override
	public void connectionLost(Throwable arg0) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newMessage(String domain, String topic, byte[] message) 
	{
		System.out.println(topic + ": " + new String(message));
	}
	public static void main(String[] args) throws Exception  
	{
		GetMessageTest getMessageTest = new GetMessageTest("GwtMqttTester", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5", true);
		String[] topics = {"geiger01/get/cpm", "dht1101/get/cond", "solarMeter01/get/cond" };
		for (int ii = 0; ii < topics.length; ++ii)
		{	
			getMessageTest.subscribe("its", topics[ii], 0);
		}
		Thread.sleep(5000);
		System.out.println("hi There");
//		getMessageTest.unsubscribeAll();
		getMessageTest.disconnect();
	}


}
