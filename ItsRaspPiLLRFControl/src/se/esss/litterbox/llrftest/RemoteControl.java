package se.esss.litterbox.llrftest;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import se.esss.litterbox.simplemqttclient.SimpleMqttClient;

public class RemoteControl  extends SimpleMqttClient
{
	public RemoteControl(String brokerUrl, String brokerKey, String brokerSecret) 
	{
		super(brokerUrl, brokerKey, brokerSecret);
	}

	@Override
	public void newMessage(String arg0, MqttMessage arg1) 
	{
		System.out.println("| Topic:" + arg0);
		System.out.println("| Message: " + new String(arg1.getPayload()));
	}

	public static void main(String[] args) throws Exception 
	{
		RemoteControl remoteControl = new RemoteControl("tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5");
		remoteControl.publishMessage("raspPiLLrf", "pulser/setup", "remoteControlLLrf", "-frq 10.0 -mrt 0.30 -rpw 0.1 -ech false", 0);
		remoteControl.publishMessage("raspPiLLrf", "pulser/onOff", "remoteControlLLrf", "-mod on -rf on", 0);
		remoteControl.subscribe("raspPiLLrf", "pulser/send/status", "remoteControlLLrf", 0);
		remoteControl.publishMessage("raspPiLLrf", "pulser/ask/status", "remoteControlLLrf", "", 0);
	}


}
