package se.esss.litterbox.itsllrfgwt.server;


import java.net.URL;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import se.esss.litterbox.its.llrfremotecontrol.LlrfRemoteControl;
import se.esss.litterbox.itsllrfgwt.client.EntryPointAppService;
import se.esss.litterbox.itsllrfgwt.shared.FileToStringArray;
import se.esss.litterbox.itsllrfgwt.shared.IceCubeDeviceList;
import se.esss.litterbox.itsllrfgwt.shared.LlrfData;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class EntryPointAppServiceImpl extends RemoteServiceServlet implements EntryPointAppService 
{
	private String settingsListProtocolUrlString = "https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolSet.csv";
	private String readingsListProtocolUrlString = "https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolRead.csv";
	private String brokerUrl = "tcp://broker.shiftr.io:1883";
	private String brokerKey = "c8ac7600";
	private String brokerSecret = "1e45295ac35335a5";
	private String domain = "its";
	private String clientID = "itsRfWebApp";
	private int subscribeWaitTimeSecs = 10;

	
	@Override
	public String[][] getModulatorProtocols(boolean debug) throws Exception 
	{
		String[][] modulatorProtocol = new String[2][];
		modulatorProtocol[0] = FileToStringArray.fileToStringArray(new URL(settingsListProtocolUrlString));
		modulatorProtocol[1] = FileToStringArray.fileToStringArray(new URL(readingsListProtocolUrlString));
		return modulatorProtocol;
	}

	@Override
	public byte[][] getModulatorState(boolean debug) throws Exception 
	{	
		if (!debug)
		{
			byte[][] setReadData = new byte[2][];
			GwtMqttSubscriber gwtMqttSubscriber = new GwtMqttSubscriber(clientID, brokerUrl, brokerKey, brokerSecret);
			gwtMqttSubscriber.setupDeviceLists(new URL(settingsListProtocolUrlString), new URL(readingsListProtocolUrlString));
			gwtMqttSubscriber.subscribe(domain, "cernmodulator/fromModulator/#", 0);
			String noMessage = " ";
			gwtMqttSubscriber.setDisconnectLatch(1);
			gwtMqttSubscriber.publishMessage(domain, "cernmodulator/toModulator/get/set", noMessage.getBytes(), 0);
			gwtMqttSubscriber.waitforDisconnectLatch(subscribeWaitTimeSecs);
			setReadData[0] =  gwtMqttSubscriber.getCernModulatorSettingList().getByteArray();

			gwtMqttSubscriber.subscribe(domain, "cernmodulator/fromModulator/#", 0);
			gwtMqttSubscriber.setDisconnectLatch(1);
			gwtMqttSubscriber.publishMessage(domain, "cernmodulator/toModulator/get/read", noMessage.getBytes(), 0);
			gwtMqttSubscriber.waitforDisconnectLatch(subscribeWaitTimeSecs);
			setReadData[1] =  gwtMqttSubscriber.getCernModulatorReadingList().getByteArray();
			return setReadData;
		}
		else
		{
			byte[][] setReadData = new byte[2][];
			IceCubeDeviceList settingDeviceList = new IceCubeDeviceList(FileToStringArray.fileToStringArray(new URL(settingsListProtocolUrlString)));
			IceCubeDeviceList readingDeviceList = new IceCubeDeviceList(FileToStringArray.fileToStringArray(new URL(readingsListProtocolUrlString)));
			setReadData[0] = settingDeviceList.getByteArray();
			setReadData[1] = readingDeviceList.getByteArray();
			return setReadData;
		}
	}
	@Override
	public String putModulatorSettings(byte[] settingsByteArray, boolean debug) throws Exception 
	{	
		if (!debug)
		{
			GwtMqttSubscriber gwtMqttSubscriber = new GwtMqttSubscriber(clientID, brokerUrl, brokerKey, brokerSecret);
			gwtMqttSubscriber.setupDeviceLists(new URL(settingsListProtocolUrlString), new URL(readingsListProtocolUrlString));
			gwtMqttSubscriber.publishMessage(domain, "cernmodulator/toModulator/put/set", settingsByteArray, 0);
			return "ok";
		}
		else
		{
			return "ok";
		}
		
	}
	@Override
	public LlrfData getLlrfState(boolean debug) throws Exception 
	{
		if (!debug)
		{
			LlrfRemoteControl llrfRemoteControl = new LlrfRemoteControl(clientID, brokerUrl, brokerKey, brokerSecret);
			llrfRemoteControl.subscribe("its", "llrf/send/status", 0);
			String noMessage = "";
			llrfRemoteControl.setDisconnectLatch(1);
			llrfRemoteControl.publishMessage("its", "llrf/ask/status", noMessage.getBytes(), 0);
			llrfRemoteControl.waitforDisconnectLatch(0);
			LlrfData llrfData = new LlrfData();
			llrfData.setRfFreq(llrfRemoteControl.getLlrfDataJson().getRfFreq());
			llrfData.setRfPowLvl(llrfRemoteControl.getLlrfDataJson().getRfPowLvl()) ;
			llrfData.setRfPowOn(llrfRemoteControl.getLlrfDataJson().isRfPowOn()) ;
			llrfData.setRfPulseWidth(llrfRemoteControl.getLlrfDataJson().getRfPulseWidth()) ;
			llrfData.setRfPulseOn(llrfRemoteControl.getLlrfDataJson().isRfPulseOn()) ;
			llrfData.setModRiseTime(llrfRemoteControl.getLlrfDataJson().getModRiseTime()) ;
			llrfData.setModRepRate(llrfRemoteControl.getLlrfDataJson().getModRepRate()) ;
			llrfData.setModPulseOn(llrfRemoteControl.getLlrfDataJson().isModPulseOn()) ;
			llrfData.setRfPowRead(llrfRemoteControl.getLlrfDataJson().getRfPowRead()) ;
			
			return llrfData;
		}
		else
		{
			LlrfData llrfData = new LlrfData();
			llrfData.setRfFreq(123.3);
			return llrfData;
		}
	}
	@Override
	public String putLlrfSettings(LlrfData llrfData, boolean initSettings, boolean debug) throws Exception 
	{
		if (!debug)
		{
			LlrfRemoteControl llrfRemoteControl = new LlrfRemoteControl(clientID, brokerUrl, brokerKey, brokerSecret);
			llrfRemoteControl.getLlrfDataJson().setRfFreq(llrfData.getRfFreq());
			llrfRemoteControl.getLlrfDataJson().setRfPowLvl(llrfData.getRfPowLvl());
			llrfRemoteControl.getLlrfDataJson().setRfPowOn(llrfData.isRfPowOn());
			llrfRemoteControl.getLlrfDataJson().setRfPulseWidth(llrfData.getRfPulseWidth());
			llrfRemoteControl.getLlrfDataJson().setRfPulseOn(llrfData.isRfPulseOn());
			llrfRemoteControl.getLlrfDataJson().setModRiseTime(llrfData.getModRiseTime());
			llrfRemoteControl.getLlrfDataJson().setModRepRate(llrfData.getModRepRate());
			llrfRemoteControl.getLlrfDataJson().setModPulseOn(llrfData.isModPulseOn());
			
			if (initSettings)
			{
				llrfRemoteControl.publishMessage("its", "llrf/setup", llrfRemoteControl.getLlrfDataJson().writeJsonString().getBytes(), 0);
			}
			else
			{
				llrfRemoteControl.publishMessage("its", "llrf/change", llrfRemoteControl.getLlrfDataJson().writeJsonString().getBytes(), 0);
			}
			return "ok";
		}
		else
		{
			return "ok";
		}
	}
}
