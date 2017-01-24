package se.esss.litterbox.its.cernrfgwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import se.esss.litterbox.its.cernrfgwt.client.callbacks.CheckIpAddresslAsyncCallback;
import se.esss.litterbox.its.cernrfgwt.client.contentpanels.LlrfPanel;
import se.esss.litterbox.its.cernrfgwt.client.contentpanels.ModulatorSettingPanel;
import se.esss.litterbox.its.cernrfgwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.its.cernrfgwt.client.mqttdata.MqttService;
import se.esss.litterbox.its.cernrfgwt.client.mqttdata.MqttServiceAsync;

public class EntryPointApp implements EntryPoint 
{
	public GskelSetupApp setupApp;
	public final MqttServiceAsync mqttService = GWT.create(MqttService.class);
	
	ModulatorSettingPanel modulatorSettingPanel = null;
	public void onModuleLoad() 
	{
		setupApp = new GskelSetupApp();
		setupApp.setDebug(false);
		setupApp.setVersionDate("January 19, 2017 14:38");
		setupApp.setVersion("v1.0");
		setupApp.setAuthor("Dave McGinnis david.mcginnis@esss.se");
		setupApp.setLogoImage("images/gwtLogo.jpg");
		setupApp.setLogoTitle("ITS RF Control!");
		setupApp.echoVersionInfo();

		setupApp.getEntryPointAppService().checkIpAddress(setupApp.isDebug(), new CheckIpAddresslAsyncCallback(this));		
		
	}
	public void initializeTabs(boolean settingsPermitted)
	{
		new LlrfPanel("LLRF", "itsClkRecvr01/set/channel", "itsPowerMeter01/get", "itsRfSigGen01/set/rf",  this, settingsPermitted);
		modulatorSettingPanel = new ModulatorSettingPanel("Modulator Settings", "itsCernMod/set/mod", "itsCernMod/get/mod", "itsClkRecvr02/set/channel", this, settingsPermitted);
	}
}
