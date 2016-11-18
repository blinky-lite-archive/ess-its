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
		String settingsListProtocolUrlString = "https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolSet.csv";
		String readingsListProtocolUrlString = "https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolRead.csv";
		String brokerUrl = "tcp://broker.shiftr.io:1883";
		String brokerKey = "c8ac7600";
		String brokerSecret = "1e45295ac35335a5";
		String domain = "its";
		String clientID = "cernModulatorWebApp";
		int subscribeWaitTimeSecs = 10;
		
		boolean cleanSession = true;
		boolean retained = false;
		GwtMqttSubscriber gwtMqttSubscriber = new GwtMqttSubscriber(clientID, brokerUrl, brokerKey, brokerSecret);
		gwtMqttSubscriber.setupDeviceLists(new URL(settingsListProtocolUrlString), new URL(readingsListProtocolUrlString));
		gwtMqttSubscriber.subscribe(domain, "cernmodulator/fromModulator/#", 0, cleanSession);
		String noMessage = " ";
		gwtMqttSubscriber.setDisconnectLatch(1);
		gwtMqttSubscriber.publishMessage(domain, "cernmodulator/toModulator/get/set", noMessage.getBytes(), 0, retained);
		gwtMqttSubscriber.waitforDisconnectLatch(subscribeWaitTimeSecs);
		byte[][] setReadData = new byte[2][];
		setReadData[0] = gwtMqttSubscriber.getCernModulatorSettingList().getByteArray();
		gwtMqttSubscriber.subscribe(domain, "cernmodulator/fromModulator/#", 0, cleanSession);
		gwtMqttSubscriber.setDisconnectLatch(1);
		gwtMqttSubscriber.publishMessage(domain, "cernmodulator/toModulator/get/read", noMessage.getBytes(), 0, retained);
		gwtMqttSubscriber.waitforDisconnectLatch(subscribeWaitTimeSecs);
		setReadData[1] = gwtMqttSubscriber.getCernModulatorReadingList().getByteArray();

		gwtMqttSubscriber.getCernModulatorSettingList().putByteArray(setReadData[0]);
		gwtMqttSubscriber.getCernModulatorReadingList().putByteArray(setReadData[1]);

		for (int ii = 0; ii < gwtMqttSubscriber.getCernModulatorSettingList().numDevices(); ++ii) 
			System.out.println(gwtMqttSubscriber.getCernModulatorSettingList().getDevice(ii).getName() + " " + gwtMqttSubscriber.getCernModulatorSettingList().getDevice(ii).getValue());
		for (int ii = 0; ii < gwtMqttSubscriber.getCernModulatorReadingList().numDevices(); ++ii) 
			System.out.println(gwtMqttSubscriber.getCernModulatorReadingList().getDevice(ii).getName() + " " + gwtMqttSubscriber.getCernModulatorReadingList().getDevice(ii).getValue());

	}
}
