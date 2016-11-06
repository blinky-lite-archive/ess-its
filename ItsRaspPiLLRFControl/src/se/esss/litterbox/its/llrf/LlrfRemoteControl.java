package se.esss.litterbox.its.llrf;

import se.esss.litterbox.simplemqttclient.SimpleMqttSubscriber;

public class LlrfRemoteControl  extends SimpleMqttSubscriber
{

	public LlrfRemoteControl(String clientIdBase, String brokerUrl, String brokerKey, String brokerSecret) 
	{
		super(clientIdBase, brokerUrl, brokerKey, brokerSecret);
	}
	@Override
	public void connectionLost(Throwable arg0) {}
	@Override
	public void newMessage(String domain, String topic, byte[] message) 
	{
		setStatus(getClientId() + "  on domain: " + domain + " recieved message on topic: " + topic);
		if (domain.equals("its"))
		{
			if (topic.equals("llrf/send/status"))
			{
				setStatus("LLRF Status: " + new String(message));
				getDisconnectLatch().countDown();
			}
		}
	}
	public static void main(String[] args) throws Exception 
	{
		LlrfRemoteControl llrfRemoteControl = new LlrfRemoteControl("dmcginnis427", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5");
		String message = "-frq 10.0 -mrt 0.60 -rpw 0.1 -ech false";
		llrfRemoteControl.publishMessage("its", "llrf/setup", message.getBytes(), 0);
		message = "-mod on -rf on";
		llrfRemoteControl.publishMessage("its", "llrf/onOff", message.getBytes(), 0);
		llrfRemoteControl.subscribe("its", "llrf/send/status", 0);
		message = "";
		llrfRemoteControl.setDisconnectLatch(1);
		llrfRemoteControl.publishMessage("its", "llrf/ask/status", message.getBytes(), 0);
		llrfRemoteControl.waitforDisconnectLatch(0);
	}


}
