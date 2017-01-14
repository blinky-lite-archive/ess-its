package se.esss.litterbox.its.timelinegwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import se.esss.litterbox.its.timelinegwt.client.callbacks.CheckIpAddresslAsyncCallback;
import se.esss.litterbox.its.timelinegwt.client.contentpanels.TimelinePanel;
import se.esss.litterbox.its.timelinegwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.its.timelinegwt.client.mqttdata.MqttService;
import se.esss.litterbox.its.timelinegwt.client.mqttdata.MqttServiceAsync;

public class EntryPointApp implements EntryPoint 
{
	public GskelSetupApp setupApp;
	public final MqttServiceAsync mqttService = GWT.create(MqttService.class);

	public void onModuleLoad() 
	{
		setupApp = new GskelSetupApp();
		setupApp.setDebug(false);
		setupApp.setVersionDate("January 12, 2017 15:45");
		setupApp.setVersion("v1.0");
		setupApp.setAuthor("Dave McGinnis david.mcginnis@esss.se");
		setupApp.setLogoImage("images/gwtLogo.jpg");
		setupApp.setLogoTitle("ITS Time!");
		setupApp.echoVersionInfo();
		setupApp.getEntryPointAppService().checkIpAddress(setupApp.isDebug(), new CheckIpAddresslAsyncCallback(this));		
		
	}
	public void initializeTabs(boolean settingsPermitted)
	{
		new TimelinePanel("Timeline", "itsClkTrans01/set/timeline",this, settingsPermitted);
	}
}
