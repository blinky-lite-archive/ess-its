package se.esss.litterbox.its.cernmod;

import java.net.URL;

import se.esss.litterbox.icetray.icecubedevice.FileToStringArray;
import se.esss.litterbox.icetray.icecubedevice.IceCubeDeviceList;
import se.esss.litterbox.simplemqttclient.SimpleMqttSubscriber;

public class CernModulatorRemoteControl extends SimpleMqttSubscriber
{
	private IceCubeDeviceList cernModulatorSettingList = null;
	private IceCubeDeviceList cernModulatorReadingList = null;

	public IceCubeDeviceList getCernModulatorSettingList() {return cernModulatorSettingList;}
	public IceCubeDeviceList getCernModulatorReadingList() {return cernModulatorReadingList;}
	
	public CernModulatorRemoteControl(String clientID, String brokerUrl, String brokerKey, String brokerSecret) 
	{
		super(clientID, brokerUrl, brokerKey, brokerSecret);
	}
	public void setupDeviceLists(URL cernmodSettingProtocolUrl, URL cernmodReadingProtocolUrl) throws Exception
	{
		cernModulatorSettingList = new IceCubeDeviceList(FileToStringArray.fileToStringArray(cernmodSettingProtocolUrl));
		cernModulatorReadingList = new IceCubeDeviceList(FileToStringArray.fileToStringArray(cernmodReadingProtocolUrl));
	}
	@Override
	public void connectionLost(Throwable arg0) 
	{
	}
	@Override
	public void newMessage(String domain, String topic, byte[] message) 
	{
		setStatus(getId() + "  on domain: " + domain + " recieved message on topic: " + topic);
		if (domain.equals("its"))
		{
			if (topic.equals("cernmodulator/fromModulator/echo/set"))
			{
				try
				{
					cernModulatorSettingList.putByteArray(message);
					getDisconnectLatch().countDown();
				} catch (Exception e) {setStatus("Error: " + e.getMessage());}
			}
			if (topic.equals("cernmodulator/fromModulator/echo/read"))
			{
				try
				{
					cernModulatorReadingList.putByteArray(message);
					getDisconnectLatch().countDown();
				} catch (Exception e) {setStatus("Error: " + e.getMessage());}
			}
		}
	}
	public static void main(String[] args) throws Exception 
	{
		CernModulatorRemoteControl cernModulatorRemoteControl = new CernModulatorRemoteControl("cernModulatorRemoteControlSubscriber", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5");
		URL cernmodSettingUrl = new URL("https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolSet.csv");
		URL cernmodReadingUrl = new URL("https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolRead.csv");
		cernModulatorRemoteControl.setupDeviceLists(cernmodSettingUrl, cernmodReadingUrl);
		cernModulatorRemoteControl.subscribe("its", "cernmodulator/fromModulator/#", 0);
		
		cernModulatorRemoteControl.setStatus("Before set cathode voltage at " + cernModulatorRemoteControl.getCernModulatorSettingList().getDevice("cathode voltage").getValue());

		cernModulatorRemoteControl.getCernModulatorSettingList().getDevice("cathode voltage").setValue("37.0");
		cernModulatorRemoteControl.getCernModulatorSettingList().getDevice("hvps current").setValue("27.0");
		cernModulatorRemoteControl.getCernModulatorSettingList().getDevice("hvps power").setValue("137.0");
		cernModulatorRemoteControl.getCernModulatorSettingList().getDevice("send mon values").setValue("1");
		
		cernModulatorRemoteControl.publishMessage("cernModulatorRemoteControlSubscriberPublisher", "its", "cernmodulator/toModulator/set/read", cernModulatorRemoteControl.getCernModulatorSettingList().getByteArray(), 0);
		cernModulatorRemoteControl.setAndWaitforDisconnectLatch(0);
		String noMessage = " ";
		cernModulatorRemoteControl.publishMessage("cernModulatorRemoteControlSubscriberPublisher", "its", "cernmodulator/toModulator/echo/set", noMessage.getBytes(), 0);
		cernModulatorRemoteControl.setStatus("After set cathode voltage at " + cernModulatorRemoteControl.getCernModulatorSettingList().getDevice("cathode voltage").getValue());
	}
}
