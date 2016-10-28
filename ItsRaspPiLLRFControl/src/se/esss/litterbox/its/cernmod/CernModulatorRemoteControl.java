package se.esss.litterbox.its.cernmod;

import java.util.Date;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import se.esss.litterbox.simplemqttclient.SimpleMqttClient;

public class CernModulatorRemoteControl  extends SimpleMqttClient
{
	private CernModulator cernModulator = null;

	public CernModulator getCernModulator() {return cernModulator;}

	public CernModulatorRemoteControl(String brokerUrl, String brokerKey, String brokerSecret) throws Exception 
	{
		super(brokerUrl, brokerKey, brokerSecret);
		cernModulator = new CernModulator();
	}

	@Override
	public void newMessage(String topic, MqttMessage mqttMessage) throws Exception 
	{
		System.out.println("    Message Recieved - Topic:" + topic + " at " + new Date().toString());
		if (topic.equals("its/cernmodulator/fromModulator/echo/set"))
		{
			cernModulator.putSettingData(mqttMessage.getPayload());
			byte[] data = cernModulator.getSettingData();
			for (int ii = 0; ii < data.length; ++ii) System.out.println("Byte = " + ii + " value = " + (data[ii] & 0xff));
			unsubscribe("cernmodulator/fromModulator/#");
			 			
		}
		if (topic.equals("its/cernmodulator/fromModulator/echo/read"))
		{
			cernModulator.putReadingData(mqttMessage.getPayload());			
			byte[] data = cernModulator.getReadingData();
			for (int ii = 0; ii < data.length; ++ii) System.out.println("Byte = " + ii + " value = " + (data[ii] & 0xff));
			unsubscribe("cernmodulator/fromModulator/#");
		}
	}

	public static void main(String[] args) throws Exception 
	{
		CernModulatorRemoteControl cernModulatorRemoteControl = new CernModulatorRemoteControl("tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5");
		cernModulatorRemoteControl.subscribe("its", "cernmodulator/fromModulator/#", "cernModulatorRemoteControl", 0);
		cernModulatorRemoteControl.getCernModulator().getSettingsDevice("cathode voltage").setValue("38.0");
		cernModulatorRemoteControl.getCernModulator().getSettingsDevice("hvps current").setValue("28.0");
		cernModulatorRemoteControl.getCernModulator().getSettingsDevice("hvps power").setValue("138.0");
		cernModulatorRemoteControl.getCernModulator().getSettingsDevice("send mon values").setValue("1");

//		cernModulatorRemoteControl.publishMessage("its", "cernmodulator/toModulator/set/read", "cernModulatorRemoteControl", cernModulatorRemoteControl.getCernModulator().getSettingData(), 0);
		String noMessage = " ";
		cernModulatorRemoteControl.publishMessage("its", "cernmodulator/toModulator/echo/set", "cernModulatorRemoteControl", noMessage.getBytes(), 0);

	}

}
