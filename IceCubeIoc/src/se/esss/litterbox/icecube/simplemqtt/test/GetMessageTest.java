package se.esss.litterbox.icecube.simplemqtt.test;

import se.esss.litterbox.icecube.simplemqtt.SimpleMqttClient;

public class GetMessageTest  extends SimpleMqttClient
{
	public GetMessageTest(String clientID, String brokerUrl, String brokerKey, String brokerSecret, boolean cleanSession) throws Exception 
	{
		super(clientID, brokerUrl, brokerKey, brokerSecret, cleanSession);
	}

	@Override
	public void newMessage(String topic, byte[] message) 
	{
		String command = new String(message);
		setStatus(getClientId() + "  got this message: " + command + " at topic " + topic);
/*		if (domain.equals("test"))
		{
			if (command.equals("unsubscribe"))
			{
				try 
				{
					unsubscribe(domain, topic);
				} catch (Exception e) {setStatus(e.getMessage());}
			}
			if (command.equals("disconnect"))
			{
				try 
				{
					getDisconnectLatch().countDown();
				} catch (Exception e) {setStatus(e.getMessage());}
			}
			if (command.equals("publish"))
			{
				String newMessage = command + " " + "a new message";
				try 
				{
					publishMessage("test", "things/out", newMessage.getBytes(), 0, false);
				} catch (Exception e) {e.printStackTrace();}
			}
		}
*/	}
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
		GetMessageTest getMessageTest = new GetMessageTest("dmcginnis427", broker, userName, password, false);
//		getMessageTest.setDisconnectLatch(1);
		getMessageTest.subscribe("test/things1", 0);
		getMessageTest.subscribe("test/things2", 0);
//		getMessageTest.waitforDisconnectLatch(0);
		Thread.sleep(10000);
		getMessageTest.disconnect();
		Thread.sleep(10000);
		getMessageTest.reconnect();
	}



}
