package se.esss.litterbox.itsllrfgwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import se.esss.litterbox.itsllrfgwt.client.EntryPointAppService;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class EntryPointAppServiceImpl extends RemoteServiceServlet implements EntryPointAppService 
{
	@Override
	public String[] gskelServerTest(String name, boolean debug, String[] debugResponse) throws Exception 
	{
		return null;
	}

}
