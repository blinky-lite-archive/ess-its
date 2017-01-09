package se.esss.litterbox.its.cernrfgwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import se.esss.litterbox.its.cernrfgwt.client.callbacks.CheckIpAddresslAsyncCallback;
import se.esss.litterbox.its.cernrfgwt.client.contentpanels.LlrfPanel;
import se.esss.litterbox.its.cernrfgwt.client.gskel.GskelSetupApp;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EntryPointApp implements EntryPoint 
{
	private GskelSetupApp setupApp;
	public GskelSetupApp getSetupApp() {return setupApp;}
	private final MqttServiceAsync mqttService = GWT.create(MqttService.class);
	public MqttServiceAsync getMqttService() {return mqttService;}

	public void onModuleLoad() 
	{
		setupApp = new GskelSetupApp();
		setupApp.setDebug(false);
		setupApp.setVersionDate("January 9, 2017 05:59");
		setupApp.setVersion("v1.0");
		setupApp.setAuthor("Dave McGinnis david.mcginnis@esss.se");
		setupApp.setLogoImage("images/gwtLogo.jpg");
		setupApp.setLogoTitle("ITS RF Control");
		setupApp.echoVersionInfo();
		setupApp.getEntryPointAppService().checkIpAddress(setupApp.isDebug(), new CheckIpAddresslAsyncCallback(this));		
		
	}
	public void initializeTabs(boolean settingsPermitted)
	{
		new LlrfPanel("LLRF", setupApp, mqttService, settingsPermitted);
	}
}
