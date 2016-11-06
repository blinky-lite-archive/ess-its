package se.esss.litterbox.itsllrfgwt.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("entrypointapp")
public interface EntryPointAppService extends RemoteService 
{
	String[] getStringArrayFromProtocol(boolean settings, boolean debug) throws Exception;
	byte[]   echoSettings(boolean debug) throws Exception;
}
