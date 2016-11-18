package se.esss.litterbox.its.llrfremotecontrol;

import se.esss.litterbox.simplemqttclient.SimpleMqttSubscriber;

public class LlrfRemoteControl  extends SimpleMqttSubscriber
{
	LlrfDataJson llrfDataJson;
	public LlrfDataJson getLlrfDataJson() {return llrfDataJson;}

	public LlrfRemoteControl(String clientIdBase, String brokerUrl, String brokerKey, String brokerSecret) 
	{
		super(clientIdBase, brokerUrl, brokerKey, brokerSecret);
		llrfDataJson = new LlrfDataJson();
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
				try 
				{
					llrfDataJson.setDataFromJsonString(new String(message));
				} catch (Exception e) {setStatus("Error: " + e.getMessage());}
				getDisconnectLatch().countDown();
			}
		}
	}
	public static void main(String[] args) throws Exception 
	{
		LlrfRemoteControl llrfRemoteControl = new LlrfRemoteControl("dmcginnis427", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5");
		
		llrfRemoteControl.getLlrfDataJson().setModPulseOn(true);
		llrfRemoteControl.getLlrfDataJson().setModRepRate(0.5);
		llrfRemoteControl.getLlrfDataJson().setModRiseTime(0.3);
		llrfRemoteControl.getLlrfDataJson().setRfFreq(352.21);
		llrfRemoteControl.getLlrfDataJson().setRfPowLvl(-10.0);
		llrfRemoteControl.getLlrfDataJson().setRfPowOn(true);
		llrfRemoteControl.getLlrfDataJson().setRfPulseOn(true);
		llrfRemoteControl.getLlrfDataJson().setRfPulseWidth(0.3);
		boolean retained = false;
		llrfRemoteControl.publishMessage("its", "llrf/setup", llrfRemoteControl.getLlrfDataJson().writeJsonString().getBytes(), 0, retained);
		
		llrfRemoteControl.getLlrfDataJson().setModPulseOn(true);
		llrfRemoteControl.getLlrfDataJson().setModRepRate(1.0);
		llrfRemoteControl.getLlrfDataJson().setModRiseTime(0.6);
		llrfRemoteControl.getLlrfDataJson().setRfFreq(352.21);
		llrfRemoteControl.getLlrfDataJson().setRfPowLvl(-10.0);
		llrfRemoteControl.getLlrfDataJson().setRfPowOn(true);
		llrfRemoteControl.getLlrfDataJson().setRfPulseOn(true);
		llrfRemoteControl.getLlrfDataJson().setRfPulseWidth(1.0);
//		llrfRemoteControl.publishMessage("its", "llrf/change", llrfRemoteControl.getLlrfDataJson().writeJsonString().getBytes(), 0, retained);

		boolean cleanSession = true;
		llrfRemoteControl.subscribe("its", "llrf/send/status", 0, cleanSession);
		String noMessage = "";
		llrfRemoteControl.setDisconnectLatch(1);
		llrfRemoteControl.publishMessage("its", "llrf/ask/status", noMessage.getBytes(), 0, retained);
		llrfRemoteControl.waitforDisconnectLatch(0);
		System.out.println(llrfRemoteControl.getLlrfDataJson().writeJsonString());
	}


}
