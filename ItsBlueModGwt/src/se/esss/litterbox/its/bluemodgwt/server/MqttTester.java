package se.esss.litterbox.its.bluemodgwt.server;

import java.net.URL;
import se.esss.litterbox.icecube.bytedevice.ByteDeviceList;
import se.esss.litterbox.icecube.simplemqtt.SimpleMqttClient;


public class MqttTester  extends SimpleMqttClient
{
	URL cernmodSettingUrl = new URL("https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolSet.csv");
	URL cernmodReadingUrl = new URL("https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolRead.csv");
	ByteDeviceList setByteDevice;
	ByteDeviceList readByteDevice;
	public MqttTester(String clientIdBase, String mqttBrokerInfoFilePath) throws Exception 
	{
		super(clientIdBase, mqttBrokerInfoFilePath, false);
		setByteDevice = new ByteDeviceList(cernmodSettingUrl);
		readByteDevice = new ByteDeviceList(cernmodReadingUrl);
	}
	@Override
	public void connectionLost(Throwable arg0) {}
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
				e.printStackTrace();
			}
		}
		if (topic.indexOf("/set/mod") >= 0)
		{
			try 
			{
				setByteDevice.putByteArray(message);
				System.out.println("cathode voltage " + setByteDevice.getDevice("cathode voltage").getValue());
				System.out.println("trigger pulsewidth " + setByteDevice.getDevice("trigger pulsewidth").getValue());
				System.out.println("hvps current " + setByteDevice.getDevice("hvps current").getValue());
				System.out.println("hvps power " + setByteDevice.getDevice("hvps power").getValue());
				System.out.println("state " + setByteDevice.getDevice("state").getValue());
				System.out.println("reset " + setByteDevice.getDevice("reset").getValue());
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
	}
	public static void main(String[] args) throws Exception 
	{
		MqttTester mqttTester = new MqttTester("ItsCernModTester", "../itsmqttbroker.dat");
		
		mqttTester.setByteDevice.getDevice("cathode voltage").setValue("40.0");
		mqttTester.setByteDevice.getDevice("trigger pulsewidth").setValue("2800");
		mqttTester.setByteDevice.getDevice("hvps current").setValue("25.0");
		mqttTester.setByteDevice.getDevice("hvps power").setValue("140.0");
		mqttTester.setByteDevice.getDevice("send mon values").setValue("1");
		mqttTester.setByteDevice.getDevice("send pulse data").setValue("0");
		
//		bigBlue.setByteDevice.getDevice("state").setValue("0"); // 0 off; 1 standby; 3 on
//		bigBlue.setByteDevice.getDevice("reset").setValue("0"); // 0 off; 1 reset
		boolean retained = true;
		mqttTester.publishMessage("itsCernMod/set/mod", mqttTester.setByteDevice.getByteArray(), 0, retained);

		
		
	}

}
