package se.esss.litterbox.its.envmongwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import se.esss.litterbox.its.envmongwt.client.EntryPointAppService;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class EntryPointAppServiceImpl extends RemoteServiceServlet implements EntryPointAppService 
{

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
