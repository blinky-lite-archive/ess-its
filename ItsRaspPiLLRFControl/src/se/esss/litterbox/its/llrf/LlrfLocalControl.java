package se.esss.litterbox.its.llrf;

import se.esss.litterbox.its.utilities.Utilities;
import se.esss.litterbox.simplemqttclient.SimpleMqttSubscriber;

public class LlrfLocalControl extends SimpleMqttSubscriber
{

	public LlrfLocalControl(String clientIdBase, String brokerUrl, String brokerKey, String brokerSecret) 
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
			if (topic.equals("llrf/onOff"))
			{
				String cmd = "python /home/pi/its/llrfLocalControl/onOff.py " + new String(message);
				setStatus("Executing: " + cmd );
				try {Utilities.runExternalProcess(cmd, true, true);} catch (Exception e) {setStatus("Error: " + e.getMessage());}
			}
			if (topic.equals("llrf/setup"))
			{
				String cmd = "python /home/pi/its/llrfLocalControl/setup.py " + new String(message);
				setStatus("Executing: " + cmd );
				try {Utilities.runExternalProcess(cmd, true, true);} catch (Exception e) {setStatus("Error: " + e.getMessage());}
			}
			if (topic.equals("llrf/ask/status"))
			{
				String cmd = "python /home/pi/its/llrfLocalControl/read.py ";
				setStatus("Executing: " + cmd );
				try 
				{
					String[] info = Utilities.runExternalProcess(cmd, true, true);
					publishMessage(domain, "llrf/send/status", info[0].getBytes(), 0);
				} catch (Exception e) {setStatus("Error: " + e.getMessage());}
			}
		}		
	}
	public static void main(String[] args) throws Exception 
	{
		System.out.println("Integration Test Stand LlrfLocalControl ver 1.1 David McGinnis 05-Nov-2016 13:39");
		LlrfLocalControl llrfLocalControl = new LlrfLocalControl("llrfLocalControl", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5");
		llrfLocalControl.subscribe("its", "llrf/#", 0);
		llrfLocalControl.setStatus("Ready for messages");
	}


}
