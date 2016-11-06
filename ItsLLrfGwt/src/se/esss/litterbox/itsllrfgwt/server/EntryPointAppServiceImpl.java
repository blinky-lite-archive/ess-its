package se.esss.litterbox.itsllrfgwt.server;


import java.net.URL;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import se.esss.litterbox.itsllrfgwt.client.EntryPointAppService;
import se.esss.litterbox.itsllrfgwt.shared.FileToStringArray;
import se.esss.litterbox.itsllrfgwt.shared.IceCubeDeviceList;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class EntryPointAppServiceImpl extends RemoteServiceServlet implements EntryPointAppService 
{
	private String settingsListProtocolUrlString = "https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolSet.csv";
	private String readingsListProtocolUrlString = "https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolRead.csv";
	private String settingsListProtocolUrlStringDebug = "http://localhost:8080/IceCubeDeviceProtocols/protocols/CernModulatorProtocolSet.csv";
	private String readingsListProtocolUrlStringDebug = "http://localhost:8080/IceCubeDeviceProtocols/protocols/CernModulatorProtocolRead.csv";
	private String brokerUrl = "tcp://broker.shiftr.io:1883";
	private String brokerKey = "c8ac7600";
	private String brokerSecret = "1e45295ac35335a5";
	private String domain = "its";
	private String clientID = "cernModulatorWebApp";
	private int subscribeWaitTimeSecs = 10;

	
	@Override
	public String[] getStringArrayFromProtocol(boolean settings, boolean debug) throws Exception 
	{
		String urlString = null;
		if (settings) 
		{
			urlString = settingsListProtocolUrlString;
			if (debug) urlString = settingsListProtocolUrlStringDebug;
		}
		else
		{
			urlString = readingsListProtocolUrlString;
			if (debug) urlString = readingsListProtocolUrlStringDebug;
		}
		URL protocolUrl = new URL(urlString);
		String[] iceCubeDeviceListStringArray = FileToStringArray.fileToStringArray(protocolUrl);
		return iceCubeDeviceListStringArray;
	}

	@Override
	public byte[] echoSettings(boolean debug) throws Exception 
	{	
		if (!debug)
		{
			GwtMqttSubscriber gwtMqttSubscriber = new GwtMqttSubscriber(clientID, brokerUrl, brokerKey, brokerSecret);
			gwtMqttSubscriber.setupDeviceLists(new URL(settingsListProtocolUrlString), new URL(readingsListProtocolUrlString));
			gwtMqttSubscriber.subscribe(domain, "cernmodulator/fromModulator/#", 0);
			String noMessage = " ";
			gwtMqttSubscriber.setDisconnectLatch(1);
			gwtMqttSubscriber.publishMessage(domain, "cernmodulator/toModulator/get/set", noMessage.getBytes(), 0);
			gwtMqttSubscriber.waitforDisconnectLatch(subscribeWaitTimeSecs);
			return gwtMqttSubscriber.getCernModulatorSettingList().getByteArray();
		}
		else
		{
			IceCubeDeviceList settingDeviceList = new IceCubeDeviceList(FileToStringArray.fileToStringArray(new URL(settingsListProtocolUrlStringDebug)));
			return settingDeviceList.getByteArray();
		}
	}
}
