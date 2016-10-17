package se.esss.litterbox.itsrasppillrfcontrol;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import se.esss.litterbox.simplemqttclient.SimpleMqttClient;

public class RemoteClient  extends SimpleMqttClient
{
	public RemoteClient(String brokerUrl, String brokerKey, String brokerSecret) 
	{
		super(brokerUrl, brokerKey, brokerSecret);
	}

	@Override
	public void newMessage(String arg0, MqttMessage arg1) 
	{
	}

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}


}
