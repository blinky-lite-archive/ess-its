package se.esss.litterbox.its.llrf;

import se.esss.litterbox.simplemqttclient.SimpleMqttSubscriber;

public class LlrfRemoteControl  extends SimpleMqttSubscriber
{

	public LlrfRemoteControl(String clientID, String brokerUrl, String brokerKey, String brokerSecret) 
	{
		super(clientID, brokerUrl, brokerKey, brokerSecret);
	}
	@Override
	public void connectionLost(Throwable arg0) {}
	@Override
	public void newMessage(String domain, String topic, byte[] message) 
	{
		setStatus(getId() + "  on domain: " + domain + " recieved message on topic: " + topic);
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
		LlrfLocalControl llrfLocalControl = new LlrfLocalControl("llrfRemoteControlSubscriber", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5");
		String message = "-frq 10.0 -mrt 0.60 -rpw 0.1 -ech false";
		llrfLocalControl.publishMessage("llrfRemoteControlSubscriberPublisher", "its", "llrf/setup", message.getBytes(), 0);
		message = "-mod on -rf on";
		llrfLocalControl.publishMessage("llrfRemoteControlSubscriberPublisher", "its", "llrf/onOff", message.getBytes(), 0);
		llrfLocalControl.setAndWaitforDisconnectLatch(0);
		llrfLocalControl.subscribe("its", "llrf/send/status", 0);
		message = "";
		llrfLocalControl.publishMessage("llrfRemoteControlSubscriberPublisher", "its", "llrf/ask/status", message.getBytes(), 0);
	}


}
