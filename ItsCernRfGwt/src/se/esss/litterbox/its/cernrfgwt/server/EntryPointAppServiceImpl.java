package se.esss.litterbox.its.cernrfgwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import se.esss.litterbox.its.cernrfgwt.client.EntryPointAppService;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class EntryPointAppServiceImpl extends RemoteServiceServlet implements EntryPointAppService 
{
	@Override
	public String[] gskelServerTest(String name, boolean debug, String[] debugResponse) throws Exception 
	{
		System.out.println(name);
		if (debug)
		{
			try {Thread.sleep(3000);} catch (InterruptedException e) {}
			return debugResponse;
		}
		try {Thread.sleep(3000);} catch (InterruptedException e) {}
		String[] answer = {"high", "low"};
		return answer;
	}

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

}
