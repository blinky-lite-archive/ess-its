package se.esss.litterbox.itsllrfgwt.client;


import com.google.gwt.user.client.rpc.AsyncCallback;

import se.esss.litterbox.itsllrfgwt.shared.LlrfData;

public interface EntryPointAppServiceAsync 
{
	void getModulatorProtocols(boolean debug, AsyncCallback<String[][]> callback);
	void getModulatorState(boolean debug, AsyncCallback<byte[][]> callback);
	void putModulatorSettings(byte[] settingsByteArray, boolean debug, AsyncCallback<String> callback);
	void getLlrfState(boolean debug, AsyncCallback<LlrfData> callback);
	void putLlrfSettings(LlrfData llrfData, boolean initSettings, boolean debug, AsyncCallback<String> callback);
	void checkIpAddress(boolean debug, AsyncCallback<String[]> callback);
}
