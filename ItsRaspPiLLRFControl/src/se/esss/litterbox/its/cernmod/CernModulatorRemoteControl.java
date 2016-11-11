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
	
	public CernModulatorRemoteControl(String clientIdBase, String brokerUrl, String brokerKey, String brokerSecret) 
	{
		super(clientIdBase, brokerUrl, brokerKey, brokerSecret);
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
		setStatus(getClientId() + "  on domain: " + domain + " recieved message on topic: " + topic);
		if (domain.equals("its"))
		{
			if (topic.equals("cernmodulator/fromModulator/get/set"))
			{
				try
				{
					cernModulatorSettingList.putByteArray(message);
					getDisconnectLatch().countDown();
				} catch (Exception e) {setStatus("Error: " + e.getMessage());}
			}
			if (topic.equals("cernmodulator/fromModulator/get/read"))
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
		CernModulatorRemoteControl cernModulatorRemoteControl = new CernModulatorRemoteControl("cernModulatorRemoteControl", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5");
		URL cernmodSettingUrl = new URL("https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolSet.csv");
		URL cernmodReadingUrl = new URL("https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolRead.csv");
		cernModulatorRemoteControl.setupDeviceLists(cernmodSettingUrl, cernmodReadingUrl);
		
		cernModulatorRemoteControl.setStatus("Before set cathode voltage at " + cernModulatorRemoteControl.getCernModulatorSettingList().getDevice("cathode voltage").getValue());

		cernModulatorRemoteControl.getCernModulatorSettingList().getDevice("cathode voltage").setValue("51.0");
		cernModulatorRemoteControl.getCernModulatorSettingList().getDevice("hvps current").setValue("30.0");
		cernModulatorRemoteControl.getCernModulatorSettingList().getDevice("hvps power").setValue("140.0");
		cernModulatorRemoteControl.getCernModulatorSettingList().getDevice("send mon values").setValue("1");
		
		cernModulatorRemoteControl.publishMessage("its", "cernmodulator/toModulator/put/set", cernModulatorRemoteControl.getCernModulatorSettingList().getByteArray(), 0);
		cernModulatorRemoteControl.subscribe("its", "cernmodulator/fromModulator/#", 0);
		cernModulatorRemoteControl.setDisconnectLatch(1);;
		String noMessage = " ";
		cernModulatorRemoteControl.publishMessage("its", "cernmodulator/toModulator/get/set", noMessage.getBytes(), 0);
		cernModulatorRemoteControl.waitforDisconnectLatch(0);
		cernModulatorRemoteControl.setStatus("After set cathode voltage at " + cernModulatorRemoteControl.getCernModulatorSettingList().getDevice("cathode voltage").getValue());
		
	}
}
