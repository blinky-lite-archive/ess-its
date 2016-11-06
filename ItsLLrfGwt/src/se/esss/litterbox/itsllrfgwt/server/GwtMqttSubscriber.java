package se.esss.litterbox.itsllrfgwt.server;

import java.net.URL;

import se.esss.litterbox.itsllrfgwt.shared.FileToStringArray;
import se.esss.litterbox.itsllrfgwt.shared.IceCubeDeviceList;
import se.esss.litterbox.simplemqttclient.SimpleMqttSubscriber;

public class GwtMqttSubscriber extends SimpleMqttSubscriber
{
	private IceCubeDeviceList cernModulatorSettingList = null;
	private IceCubeDeviceList cernModulatorReadingList = null;

	public IceCubeDeviceList getCernModulatorSettingList() {return cernModulatorSettingList;}
	public IceCubeDeviceList getCernModulatorReadingList() {return cernModulatorReadingList;}

	public GwtMqttSubscriber(String clientIdBase, String brokerUrl, String brokerKey, String brokerSecret) 
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
		URL cernmodSettingUrl = new URL("https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolSet.csv");
		URL cernmodReadingUrl = new URL("https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolRead.csv");
		GwtMqttSubscriber gwtMqttSubscriber = new GwtMqttSubscriber("cernModulatorGwtRemoteControl", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5");
		gwtMqttSubscriber.setupDeviceLists(cernmodSettingUrl, cernmodReadingUrl);
		gwtMqttSubscriber.subscribe("its", "cernmodulator/fromModulator/#", 0);
		System.out.println(gwtMqttSubscriber.getCernModulatorSettingList().getDevice("cathode voltage").csvLine());
		String noMessage = " ";
		gwtMqttSubscriber.setDisconnectLatch(1);
		gwtMqttSubscriber.publishMessage("its", "cernmodulator/toModulator/get/set", noMessage.getBytes(), 0);
		gwtMqttSubscriber.waitforDisconnectLatch(0);
		System.out.println(gwtMqttSubscriber.getCernModulatorSettingList().getDevice("cathode voltage").csvLine());
	}
}
