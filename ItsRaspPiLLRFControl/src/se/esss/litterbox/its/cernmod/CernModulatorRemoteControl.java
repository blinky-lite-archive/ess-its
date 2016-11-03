package se.esss.litterbox.its.cernmod;

import java.net.URL;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import se.esss.litterbox.icetray.icecubedevice.FileToStringArray;
import se.esss.litterbox.icetray.icecubedevice.IceCubeDeviceList;
import se.esss.litterbox.simplemqttclient.SimpleMqttClient;

public class CernModulatorRemoteControl  extends SimpleMqttClient
{
	private IceCubeDeviceList cernModulatorSettingList = null;
	private IceCubeDeviceList cernModulatorReadingList = null;

	public IceCubeDeviceList getCernModulatorSettingList() {return cernModulatorSettingList;}
	public IceCubeDeviceList getCernModulatorReadingList() {return cernModulatorReadingList;}
	CountDownLatch latch = null;

	public CernModulatorRemoteControl(String brokerUrl, String brokerKey, String brokerSecret, URL cernmodSettingProtocolUrl, URL cernmodReadingProtocolUrl) throws Exception 
	{
		super(brokerUrl, brokerKey, brokerSecret);
		cernModulatorSettingList = new IceCubeDeviceList(FileToStringArray.fileToStringArray(cernmodSettingProtocolUrl));
		cernModulatorReadingList = new IceCubeDeviceList(FileToStringArray.fileToStringArray(cernmodReadingProtocolUrl));
	}

	@Override
	public void newMessage(String topic, MqttMessage mqttMessage) throws Exception 
	{
		System.out.println("    Message Recieved - Topic:" + topic + " at " + new Date().toString());
		if (topic.equals("its/cernmodulator/fromModulator/echo/set"))
		{
			cernModulatorSettingList.putByteArray(mqttMessage.getPayload());
			byte[] data = cernModulatorSettingList.getByteArray();
			for (int ii = 0; ii < data.length; ++ii) System.out.println("Byte = " + ii + " value = " + (data[ii] & 0xff));
			latch.countDown();
//			unsubscribe("cernmodulator/fromModulator/#");
			 			
		}
		if (topic.equals("its/cernmodulator/fromModulator/echo/read"))
		{
			cernModulatorReadingList.putByteArray(mqttMessage.getPayload());
			byte[] data = cernModulatorReadingList.getByteArray();
			for (int ii = 0; ii < data.length; ++ii) System.out.println("Byte = " + ii + " value = " + (data[ii] & 0xff));
			unsubscribe("cernmodulator/fromModulator/#");
		}
	}

	public static void main(String[] args) throws Exception 
	{
		URL cernmodSettingUrl = new URL("https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolSet.csv");
		URL cernmodReadingUrl = new URL("https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolRead.csv");
		CernModulatorRemoteControl cernModulatorRemoteControl = new CernModulatorRemoteControl("tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5", cernmodSettingUrl, cernmodReadingUrl);
		cernModulatorRemoteControl.subscribe("its", "cernmodulator/fromModulator/#", "cernModulatorRemoteControl", 0);
		cernModulatorRemoteControl.getCernModulatorSettingList().getDevice("cathode voltage").setValue("38.0");
		cernModulatorRemoteControl.getCernModulatorSettingList().getDevice("hvps current").setValue("28.0");
		cernModulatorRemoteControl.getCernModulatorSettingList().getDevice("hvps power").setValue("138.0");
		cernModulatorRemoteControl.getCernModulatorSettingList().getDevice("send mon values").setValue("1");

		cernModulatorRemoteControl.publishMessage("its", "cernmodulator/toModulator/set/read", "cernModulatorRemoteControl", cernModulatorRemoteControl.getCernModulatorSettingList().getByteArray(), 0);
		String noMessage = " ";
		cernModulatorRemoteControl.latch = new CountDownLatch(1);
		cernModulatorRemoteControl.publishMessage("its", "cernmodulator/toModulator/echo/set", "cernModulatorRemoteControl", noMessage.getBytes(), 0);
		cernModulatorRemoteControl.latch.await();
		System.out.println("Booger");

	}

}
