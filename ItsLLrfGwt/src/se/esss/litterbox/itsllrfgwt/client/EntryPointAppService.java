package se.esss.litterbox.itsllrfgwt.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import se.esss.litterbox.itsllrfgwt.shared.LlrfData;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("entrypointapp")
public interface EntryPointAppService extends RemoteService 
{
	String[][] getModulatorProtocols(boolean debug) throws Exception;
	byte[][]   getModulatorState(boolean debug) throws Exception;
	String putModulatorSettings(byte[] settingsByteArray, boolean debug) throws Exception; 
	LlrfData getLlrfState(boolean debug) throws Exception;
	String putLlrfSettings(LlrfData llrfData, boolean initSettings, boolean debug) throws Exception; 
	
}
