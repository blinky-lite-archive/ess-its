package se.esss.litterbox.its.ioc;


import java.net.URL;

import org.json.simple.JSONObject;

import se.esss.litterbox.icecube.bytedevice.ByteDeviceList;
import se.esss.litterbox.icecube.ioc.tcp.IceCubeTcpIoc;

public class ItsCernModulatorIoc extends IceCubeTcpIoc
{
	URL cernmodSettingUrl = new URL("https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolSet.csv");
	URL cernmodReadingUrl = new URL("https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolRead.csv");
	ByteDeviceList setByteDevice;
	ByteDeviceList readByteDevice;
	private byte[] settingsArray = null;
	private int numReadbackBytes = 118;
	private int numWaveformBytes = 3600;

	public ItsCernModulatorIoc(String clientId, String mqttBrokerInfoFilePath, String inetAddress, int portNumber) throws Exception 
	{
		super(clientId, mqttBrokerInfoFilePath, inetAddress, portNumber);
		setByteDevice = new ByteDeviceList(cernmodSettingUrl);
		readByteDevice = new ByteDeviceList(cernmodReadingUrl);
	}
	@SuppressWarnings("unchecked")
	@Override
	public byte[] getDataFromGizmo() 
	{
		if (settingsArray == null) return null;
		byte[] readbackData = null;
		byte[][] waveformData = new byte[5][];
		try 
		{
			setByteDevice.putByteArray(settingsArray);
			setByteDevice.getDevice("send mon values").setValue("1");
			sendBytes(setByteDevice.getByteArray());
		} 
		catch (Exception e) 
		{
			setStatus("Error in sending data: " + e.getMessage());
			return null;
		}
		try {Thread.sleep(100);} catch (InterruptedException e) {}
		try 
		{
			byte[] readData  = receiveBytes(20000);
			int numReadBytes = readData.length;
			int startByteIndex = 0;
			System.out.println("     Number of Bytes read = " + numReadBytes);
			if ((numReadBytes == 118) || (numReadBytes == 18118))
			{
				readbackData = new byte[118];
				for (int ij = 0; ij < numReadbackBytes; ++ij) readbackData[ij] = readData[ij];
				startByteIndex = 118;
				
				readByteDevice.putByteArray(readbackData);
				JSONObject outputData = new JSONObject();
				outputData.put("wtrp", readByteDevice.getDevice("wtr p").getValue());
				publishMessage("itsCernMod/get/wtrp", outputData.toJSONString().getBytes(), this.getPublishQos(), true);	
			}
			else
			{
				readbackData = null;
			}
			if (numReadBytes > 118) 
			{
				for (int ii = 0; ii < 1; ++ii)
				{
					waveformData[ii] = new byte[numWaveformBytes];
					for (int ij = 0; ij < numWaveformBytes; ++ij)
					{
						waveformData[ii][ij] = readData[startByteIndex + numWaveformBytes * ii + ij];
					}
					publishMessage("itsCernMod/get/wave/w" + Integer.toString(ii + 1), waveformData[ii], this.getPublishQos(), true);	
				}
			}
		} catch (Exception e) 
		{
			setStatus("Error in reading data: " + e.getMessage());
			return null;
		}
		
		return readbackData;
	}
	@Override
	public void handleBrokerMqttMessage(String topic, byte[] message) 
	{
		if (topic.indexOf("/set/mod") >= 0)
		{
			settingsArray = new byte[message.length];
			for (int ii = 0; ii < message.length; ++ii) settingsArray[ii] = message[ii];
		}
	}
	public static void main(String[] args) throws Exception 
	{
		ItsCernModulatorIoc ioc = new ItsCernModulatorIoc("itsCernModIoc", "itsmqttbroker.dat", "192.168.5.4", 8000);
		ioc.setPeriodicPollPeriodmillis(1000);
		ioc.startIoc("itsCernMod/set/#", "itsCernMod/get/mod");
	}

}
