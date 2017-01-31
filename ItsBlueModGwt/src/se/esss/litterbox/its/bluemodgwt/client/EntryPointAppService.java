package se.esss.litterbox.its.bluemodgwt.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;



/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("entrypointapp")
public interface EntryPointAppService extends RemoteService 
{
	String[] checkIpAddress(boolean debug) throws Exception;
	String[][] getModulatorProtocols(boolean debug) throws Exception;
}
