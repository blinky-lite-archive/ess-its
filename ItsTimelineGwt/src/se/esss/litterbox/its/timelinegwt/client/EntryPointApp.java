package se.esss.litterbox.its.timelinegwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import se.esss.litterbox.its.timelinegwt.client.callbacks.CheckIpAddresslAsyncCallback;
import se.esss.litterbox.its.timelinegwt.client.contentpanels.TimelinePanel;
import se.esss.litterbox.its.timelinegwt.client.gskel.GskelSetupApp;

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
		setupApp.setVersionDate("January 1, 2017 11:02");
		setupApp.setVersion("v1.0");
		setupApp.setAuthor("Dave McGinnis david.mcginnis@esss.se");
		setupApp.setLogoImage("images/gwtLogo.jpg");
		setupApp.setLogoTitle("ITS Timeline");
		setupApp.echoVersionInfo();
		setupApp.getEntryPointAppService().checkIpAddress(setupApp.isDebug(), new CheckIpAddresslAsyncCallback(this));		
		
	}
	public void initializeTabs(boolean settingsPermitted)
	{
		new TimelinePanel("Timeline", setupApp, mqttService, settingsPermitted);
	}
}
