package se.esss.litterbox.itsrasppillrfcontrol;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import se.esss.litterbox.simplemqttclient.SimpleMqttClient;

public class RaspPiMqttClient extends SimpleMqttClient
{

	public RaspPiMqttClient(String brokerUrl, String brokerKey, String brokerSecret) 
	{
		super(brokerUrl, brokerKey, brokerSecret);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void newMessage(String arg0, MqttMessage arg1) 
	{
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) throws Exception 
	{
		RaspPiMqttClient raspPiLLrf = new RaspPiMqttClient("tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5");
		raspPiLLrf.subscribe("raspPiLLrf", "setup", "raspiPi", 0);
		raspPiLLrf.subscribe("raspPiLLrf", "onOff", "raspiPi", 0);
	}

}
