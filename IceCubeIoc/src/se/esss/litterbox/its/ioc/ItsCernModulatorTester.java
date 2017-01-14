package se.esss.litterbox.its.ioc;

import java.net.URL;

import se.esss.litterbox.icecube.bytedevice.ByteDeviceList;
import se.esss.litterbox.icecube.simplemqtt.SimpleMqttClient;

public class ItsCernModulatorTester extends SimpleMqttClient
{
	URL cernmodSettingUrl = new URL("https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolSet.csv");
	URL cernmodReadingUrl = new URL("https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolRead.csv");
	ByteDeviceList setByteDevice;
	ByteDeviceList readByteDevice;
	public ItsCernModulatorTester(String clientId, String mqttBrokerInfoFilePath) throws Exception 
	{
		super(clientId, mqttBrokerInfoFilePath, false);
		setByteDevice = new ByteDeviceList(cernmodSettingUrl);
		readByteDevice = new ByteDeviceList(cernmodReadingUrl);
	}
	@Override
	public void newMessage(String topic, byte[] message) 
	{
		if (topic.indexOf("/get/mod") >= 0)
		{
			try 
			{
				readByteDevice.putByteArray(message);
				System.out.println("trigger t w " + readByteDevice.getDevice("trigger t w").getValue());
				System.out.println("temp tank " + readByteDevice.getDevice("temp tank").getValue());
			} catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) throws Exception 
	{
		ItsCernModulatorTester bigBlue = new ItsCernModulatorTester("ItsCernModTester", "itsmqttbroker.dat");
		
		bigBlue.setByteDevice.getDevice("cathode voltage").setValue("40.0");
		bigBlue.setByteDevice.getDevice("hvps current").setValue("30.0");
		bigBlue.setByteDevice.getDevice("hvps power").setValue("140.0");
		bigBlue.setByteDevice.getDevice("send mon values").setValue("1");
		
		boolean retained = true;
		bigBlue.publishMessage("itsCernMod/set/mod", bigBlue.setByteDevice.getByteArray(), 0, retained);
		bigBlue.subscribe("itsCernMod/get/#", 0);
	
	}

}
