package se.esss.litterbox.its.cernrfgwt.server;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import se.esss.litterbox.its.cernrfgwt.client.EntryPointAppService;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class EntryPointAppServiceImpl extends RemoteServiceServlet implements EntryPointAppService 
{
	private String settingsListProtocolUrlString = "https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolSet.csv";
	private String readingsListProtocolUrlString = "https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolRead.csv";
	@Override
	public String[] checkIpAddress(boolean debug) throws Exception 
	{
		String[] okIpAddresses = {"130.235.82.5", "192.168.0.105", "127.0.0.1"};
		String ip = getThreadLocalRequest().getRemoteAddr();
		boolean ipOkay = false;
		for (int ii = 0; ii < okIpAddresses.length; ++ii)
		{
			if (ip.equals(okIpAddresses[ii])) ipOkay = true;
		}
		String[] returnData = new String[2];
		returnData[0] = ip;
		returnData[1] = Boolean.toString(ipOkay);
		return returnData;
	}
	@Override
	public String[][] getModulatorProtocols(boolean debug) throws Exception 
	{
		String[][] modulatorProtocol = new String[2][];
		modulatorProtocol[0] = fileToStringArray(new URL(settingsListProtocolUrlString));
		modulatorProtocol[1] = fileToStringArray(new URL(readingsListProtocolUrlString));
		return modulatorProtocol;
	}

	private String[] fileToStringArray(URL filePathUrl) throws Exception
	{
		ArrayList<String> deviceList = new ArrayList<String>();
		BufferedReader br = new BufferedReader( new InputStreamReader(filePathUrl.openStream()));
		br.readLine(); // read header
		String line;
		while ((line = br.readLine()) != null) 
		{
			deviceList.add(line);
		}
		br.close();
		String[] csvLineArray = new  String[deviceList.size()];
		for (int ii = 0; ii < deviceList.size(); ++ii) csvLineArray[ii] = deviceList.get(ii);
		return csvLineArray;
	}

}
