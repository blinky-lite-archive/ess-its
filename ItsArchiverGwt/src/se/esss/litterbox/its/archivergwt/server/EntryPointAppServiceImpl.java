package se.esss.litterbox.its.archivergwt.server;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import se.esss.litterbox.its.archivergwt.client.EntryPointAppService;

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
		String userName = getThreadLocalRequest().getUserPrincipal().getName();
		boolean userOkay = getThreadLocalRequest().isUserInRole("webAppSettingsPermitted");
		String[] returnData = new String[2];
		returnData[0] = userName;
		returnData[1] = Boolean.toString(userOkay);
		return returnData;
	}

}
