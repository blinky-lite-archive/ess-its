package se.esss.litterbox.its.llrf;

import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import se.esss.litterbox.simplemqttclient.SimpleMqttClient;

public class LlrfLocalControl extends SimpleMqttClient
{

	public LlrfLocalControl(String brokerUrl, String brokerKey, String brokerSecret) 
	{
		super(brokerUrl, brokerKey, brokerSecret);
	}
	@Override
	public void newMessage(String topic, MqttMessage mqttMessage) throws Exception
	{
		System.out.println("    Message Recieved - Topic:" + topic + " at " + new Date().toString());
		if (topic.equals("its/llrf/onOff"))
		{
			String cmd = "python /home/pi/its/llrfLocalControl/onOff.py " + new String(mqttMessage.getPayload());
			System.out.println("    Executing: " + cmd + "\n");
			SimpleMqttClient.runExternalProcess(cmd, true, true);
		}
		if (topic.equals("its/llrf/setup"))
		{
			String cmd = "python /home/pi/its/llrfLocalControl/setup.py " + new String(mqttMessage.getPayload());
			System.out.println("    Executing: " + cmd + "\n");
			SimpleMqttClient.runExternalProcess(cmd, true, true);
		}
		if (topic.equals("its/llrf/ask/status"))
		{
			String cmd = "python /home/pi/its/llrfLocalControl/read.py ";
			System.out.println("    Executing: " + cmd + "\n");
			String[] info  = SimpleMqttClient.runExternalProcess(cmd, true, true);
			this.publishMessage("raspPiLLrf", "pulser/send/status", "localControlPulser", info[0].getBytes(), 0);
		}
		
	}
	public static void main(String[] args) throws Exception 
	{
		System.out.println("Integration Test Stand LlrfLocalControl ver 1.0 David McGinnis 27-Oct-2016 09:28");
		LlrfLocalControl llrfLocalControl = new LlrfLocalControl("tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5");
		llrfLocalControl.subscribe("its", "llrf/#", "llrfLocalControl", 0);
		System.out.println("Ready for messages....");
		llrfLocalControl.setEchoInfo(false);
	}

}
