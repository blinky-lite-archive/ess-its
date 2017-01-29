package se.esss.litterbox.its.watersystemgwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import se.esss.litterbox.its.watersystemgwt.client.callbacks.CheckIpAddresslAsyncCallback;
import se.esss.litterbox.its.watersystemgwt.client.contentpanels.GaugeShowCasePanel;
import se.esss.litterbox.its.watersystemgwt.client.gskel.GskelLoadWaiter;
import se.esss.litterbox.its.watersystemgwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.its.watersystemgwt.client.mqttdata.MqttService;
import se.esss.litterbox.its.watersystemgwt.client.mqttdata.MqttServiceAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EntryPointApp implements EntryPoint 
{
	public GskelSetupApp setupApp;
	public final MqttServiceAsync mqttService = GWT.create(MqttService.class);
	boolean settingsPermitted;

	public void onModuleLoad() 
	{
		setupApp = new GskelSetupApp(this);
		setupApp.setDebug(false);
		setupApp.setVersionDate("January 29, 2017 09:40");
		setupApp.setVersion("v2.0");
		setupApp.setAuthor("Dave McGinnis david.mcginnis@esss.se");
		setupApp.setLogoImage("images/gwtLogo.jpg");
		setupApp.setLogoTitle("Its Thirsty!");
		setupApp.echoVersionInfo();
		setupApp.getEntryPointAppService().checkIpAddress(setupApp.isDebug(), new CheckIpAddresslAsyncCallback(this));		
		
	}
	public void initializeTabs(boolean settingsPermitted)
	{
		this.settingsPermitted = settingsPermitted;
		loadTab1();
	}
	private void loadTab1()
	{
		new GaugeShowCasePanel("Gauges", "itsWaterSystem/get", setupApp);
		new TabLoadWaiter(500, 1);
	}
	class TabLoadWaiter extends GskelLoadWaiter
	{
		public TabLoadWaiter(int loopTimeMillis, int itask) {super(loopTimeMillis, itask);}
		@Override
		public boolean isLoaded() 
		{
			return true;
		}
		@Override
		public void taskAfterLoad() 
		{
			if (getItask() == 1) ;
		}
	}
}
