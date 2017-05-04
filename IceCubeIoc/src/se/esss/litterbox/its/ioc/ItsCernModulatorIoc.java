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
				outputData.put("volts", readByteDevice.getDevice("v").getValue());
				outputData.put("current", readByteDevice.getDevice("i").getValue());
				outputData.put("power", readByteDevice.getDevice("p").getValue());
				outputData.put("vline1", readByteDevice.getDevice("v line 1").getValue());
				outputData.put("vline2", readByteDevice.getDevice("v line 2").getValue());
				outputData.put("vline3", readByteDevice.getDevice("v line 3").getValue());
				outputData.put("iline1", readByteDevice.getDevice("i line 1").getValue());
				outputData.put("iline2", readByteDevice.getDevice("i line 2").getValue());
				outputData.put("iline3", readByteDevice.getDevice("i line 3").getValue());
				outputData.put("pline1", readByteDevice.getDevice("p line 1").getValue());
				outputData.put("pline2", readByteDevice.getDevice("p line 2").getValue());
				outputData.put("pline3", readByteDevice.getDevice("p line 3").getValue());
				publishMessage("itsCernMod/get/power1", outputData.toJSONString().getBytes(), this.getPublishQos(), true);	
				
				outputData = new JSONObject();
				outputData.put("biasV", readByteDevice.getDevice("bias v").getValue());
				outputData.put("biasI", readByteDevice.getDevice("bias i").getValue());
				outputData.put("biasP", readByteDevice.getDevice("bias p").getValue());
				outputData.put("capVChg", readByteDevice.getDevice("cap v charge").getValue());
				outputData.put("ilr", readByteDevice.getDevice("ilr fast dump lock il countdown").getValue());
				outputData.put("primV", readByteDevice.getDevice("prim v sample const").getValue());
				outputData.put("primI", readByteDevice.getDevice("prim i sample const").getValue());
				outputData.put("primP", readByteDevice.getDevice("prim p sample const").getValue());
				outputData.put("cathV", readByteDevice.getDevice("cath v sample const").getValue());
				outputData.put("cathI", readByteDevice.getDevice("cath i sample const").getValue());
				outputData.put("cathP", readByteDevice.getDevice("cath p sample const").getValue());
				publishMessage("itsCernMod/get/power2", outputData.toJSONString().getBytes(), this.getPublishQos(), true);	
				
				outputData = new JSONObject();
				outputData.put("tcab", readByteDevice.getDevice("temp cabinet").getValue());
				outputData.put("tcabin", readByteDevice.getDevice("temp cabinet inlet").getValue());
				outputData.put("tcabouth", readByteDevice.getDevice("temp cabinet outlet hvps").getValue());
				outputData.put("tcaboutm", readByteDevice.getDevice("temp cabinet outlet machine").getValue());
				outputData.put("tmod1", readByteDevice.getDevice("temp module 1").getValue());
				outputData.put("tmod2", readByteDevice.getDevice("temp module 2").getValue());
				outputData.put("tmod3", readByteDevice.getDevice("temp module 3").getValue());
				outputData.put("wtrp", readByteDevice.getDevice("wtr p").getValue());
				outputData.put("tIgct", readByteDevice.getDevice("temp igct").getValue());
				outputData.put("tUs", readByteDevice.getDevice("temp us").getValue());
				outputData.put("tLs", readByteDevice.getDevice("temp ls bouncer").getValue());
				outputData.put("tTank", readByteDevice.getDevice("temp tank").getValue());
				outputData.put("tOil", readByteDevice.getDevice("temp oil").getValue());
				publishMessage("itsCernMod/get/temp", outputData.toJSONString().getBytes(), this.getPublishQos(), true);	
				
				outputData = new JSONObject();
				outputData.put("freq", readByteDevice.getDevice("trigger f").getValue());
				outputData.put("pulseWidth", readByteDevice.getDevice("trigger t w").getValue());
				outputData.put("duty", readByteDevice.getDevice("trigger duty").getValue());
				outputData.put("stateOn", readByteDevice.getDevice("state on").getValue());
				outputData.put("stateOff", readByteDevice.getDevice("state off").getValue());
				publishMessage("itsCernMod/get/state", outputData.toJSONString().getBytes(), this.getPublishQos(), true);	
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
	@SuppressWarnings("unchecked")
	@Override
	public void handleBrokerMqttMessage(String topic, byte[] message) 
	{
		if (topic.equals("itsCernMod/set/mod"))
		{
			settingsArray = new byte[message.length];
			for (int ii = 0; ii < message.length; ++ii) settingsArray[ii] = message[ii];
			try
			{
				setByteDevice.putByteArray(settingsArray);
				setByteDevice.getDevice("send mon values").setValue("1");
				JSONObject outputData = new JSONObject();
				outputData.put("state", setByteDevice.getDevice("state").getValue());
				outputData.put("pulseWidth", setByteDevice.getDevice("trigger pulsewidth").getValue());
				outputData.put("cathV", setByteDevice.getDevice("cathode voltage").getValue());
				outputData.put("hvpsI", setByteDevice.getDevice("hvps current").getValue());
				outputData.put("hvpsP", setByteDevice.getDevice("hvps power").getValue());
				outputData.put("hvpsCathStepUp", setByteDevice.getDevice("hvps cathode voltage step up").getValue());
				outputData.put("hvpsCathStepDn", setByteDevice.getDevice("hvps cathode voltage step down").getValue());
				outputData.put("biasV", setByteDevice.getDevice("bias voltage").getValue());
				outputData.put("biasI", setByteDevice.getDevice("bias current").getValue());
				outputData.put("klyVlim", setByteDevice.getDevice("limit voltage klystron").getValue());
				outputData.put("priVlim", setByteDevice.getDevice("limit current primary").getValue());
				outputData.put("klyIlim", setByteDevice.getDevice("limit current klystron").getValue());
				publishMessage("itsCernMod/get/settings", outputData.toJSONString().getBytes(), this.getPublishQos(), true);	
				
			}
			catch (Exception e)
			{
				setStatus("Error in reading setting topic: " + e.getMessage());
			}
			

		}
	}
	@Override
	public void lostMqttConnection(Throwable arg0) 
	{
		try {reconnect();} catch (Exception e) {setStatus("Error on reconnect: " + arg0.getMessage());}
	}
	public static void main(String[] args) throws Exception 
	{
		ItsCernModulatorIoc ioc = new ItsCernModulatorIoc("itsCernModIoc", "itsmqttbroker.dat", "192.168.5.4", 8000);
		ioc.setPeriodicPollPeriodmillis(1000);
		ioc.startIoc("itsCernMod/set/#", "itsCernMod/get/mod");
	}

}
