package se.esss.litterbox.its.envmongwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import se.esss.litterbox.its.envmongwt.client.callbacks.CheckIpAddresslAsyncCallback;
import se.esss.litterbox.its.envmongwt.client.contentpanels.StatusPanel;
import se.esss.litterbox.its.envmongwt.client.gskel.GskelSetupApp;

public class EntryPointApp implements EntryPoint 
{
	public GskelSetupApp setupApp;
	public GskelSetupApp getSetupApp() {return setupApp;}

	private final MqttServiceAsync mqttService = GWT.create(MqttService.class);
	public MqttServiceAsync getMqttService() {return mqttService;}

	public void onModuleLoad() 
	{
		setupApp = new GskelSetupApp(this);
		setupApp.setDebug(false);
		setupApp.setVersionDate("December 28, 2016 13:59");
		setupApp.setVersion("v1.0");
		setupApp.setAuthor("Dave McGinnis david.mcginnis@esss.se");
		setupApp.setLogoImage("images/gwtLogo.jpg");
		setupApp.setLogoTitle("ITS Alive!");
		setupApp.echoVersionInfo();
		setupApp.getEntryPointAppService().checkIpAddress(setupApp.isDebug(), new CheckIpAddresslAsyncCallback(this));
		
		

//		VerticalPanel vp1 = new VerticalPanel();
//		vp1.add(new GaugeExample());
//		RootLayoutPanel.get().add(vp1);
	}
	public void initializeTabs(boolean settingsPermitted)
	{
		new StatusPanel("Monitor", setupApp, mqttService, settingsPermitted);
	}
}
