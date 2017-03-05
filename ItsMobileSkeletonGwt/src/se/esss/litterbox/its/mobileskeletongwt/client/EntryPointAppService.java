package se.esss.litterbox.its.mobileskeletongwt.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("entrypointapp")
public interface EntryPointAppService extends RemoteService 
{
	String[] gskelServerTest(String name) throws Exception;
	String[] checkIpAddress() throws Exception;
}
