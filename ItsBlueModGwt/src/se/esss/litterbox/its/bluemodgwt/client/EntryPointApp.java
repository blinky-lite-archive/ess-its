package se.esss.litterbox.its.bluemodgwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import se.esss.litterbox.its.bluemodgwt.client.callbacks.CheckIpAddresslAsyncCallback;
import se.esss.litterbox.its.bluemodgwt.client.contentpanels.ModulatorSettingPanel;
import se.esss.litterbox.its.bluemodgwt.client.contentpanels.ModulatorWaveformPanel;
import se.esss.litterbox.its.bluemodgwt.client.gskel.GskelLoadWaiter;
import se.esss.litterbox.its.bluemodgwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.its.bluemodgwt.client.mqttdata.MqttService;
import se.esss.litterbox.its.bluemodgwt.client.mqttdata.MqttServiceAsync;

public class EntryPointApp implements EntryPoint 
{
	public GskelSetupApp setupApp;
	public final MqttServiceAsync mqttService = GWT.create(MqttService.class);
	boolean settingsPermitted;
	
	public ModulatorSettingPanel modulatorSettingPanel = null;
	public void onModuleLoad() 
	{
		setupApp = new GskelSetupApp();
		setupApp.setDebug(false);
		setupApp.setVersionDate("January 31, 2017 14:49");
		setupApp.setVersion("v1.0");
		setupApp.setAuthor("Dave McGinnis david.mcginnis@esss.se");
		setupApp.setLogoImage("images/gwtLogo.jpg");
		setupApp.setLogoTitle("ITS Big Blue!");
		setupApp.echoVersionInfo();

		setupApp.getEntryPointAppService().checkIpAddress(setupApp.isDebug(), new CheckIpAddresslAsyncCallback(this));		
		
	}
	public void initializeTabs(boolean settingsPermitted)
	{
		this.settingsPermitted = settingsPermitted;
		new TabLoadWaiter(100, 0);
	}
	private void loadModulatorPanel()
	{
		modulatorSettingPanel = new ModulatorSettingPanel("Modulator Settings", "itsCernMod/set/mod", "itsCernMod/get/mod", "itsClkRecvr02/set/channel", this, settingsPermitted);
		new TabLoadWaiter(100, 1);
	}
	private void loadModulatorWaveformPanel()
	{
		new ModulatorWaveformPanel("Mod Waveform", "itsCernMod/get/wave/w1", this, settingsPermitted);
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
			if (getItask() == 0) loadModulatorPanel();
			if (getItask() == 1) loadModulatorWaveformPanel();
		}
		
	}
}
