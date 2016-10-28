package se.esss.litterbox.its.llrf;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import se.esss.litterbox.simplemqttclient.SimpleMqttClient;

public class LlrfRemoteControl  extends SimpleMqttClient
{
	public LlrfRemoteControl(String brokerUrl, String brokerKey, String brokerSecret) 
	{
		super(brokerUrl, brokerKey, brokerSecret);
	}

	@Override
	public void newMessage(String topic, MqttMessage mqttMessage) 
	{
		System.out.println("| Topic:" + topic);
		System.out.println("| Message: " + new String(mqttMessage.getPayload()));
	}

	public static void main(String[] args) throws Exception 
	{
		LlrfRemoteControl llrfRemoteControl = new LlrfRemoteControl("tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5");
		String message = "-frq 10.0 -mrt 0.60 -rpw 0.1 -ech false";
		llrfRemoteControl.publishMessage("its", "llrf/setup", "llrfRemoteControl", message.getBytes(), 0);
		message = "-mod on -rf on";
		llrfRemoteControl.publishMessage("its", "llrf/onOff", "llrfRemoteControl", message.getBytes(), 0);
		llrfRemoteControl.subscribe("its", "llrf/send/status", "llrfRemoteControl", 0);
		message = "";
		llrfRemoteControl.publishMessage("its", "llrf/ask/status", "llrfRemoteControl", message.getBytes(), 0);
	}


}
