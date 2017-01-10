package se.esss.litterbox.its.cernrfgwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import se.esss.litterbox.its.cernrfgwt.client.callbacks.CheckIpAddresslAsyncCallback;
import se.esss.litterbox.its.cernrfgwt.client.contentpanels.LlrfPanel;
import se.esss.litterbox.its.cernrfgwt.client.contentpanels.ModulatorReadingsMqttData;
import se.esss.litterbox.its.cernrfgwt.client.contentpanels.ModulatorSettingPanel;
import se.esss.litterbox.its.cernrfgwt.client.contentpanels.ModulatorSettingsMqttData;
import se.esss.litterbox.its.cernrfgwt.client.contentpanels.PowerMeterMqttData;
import se.esss.litterbox.its.cernrfgwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.its.cernrfgwt.client.mqttdata.MqttData;
import se.esss.litterbox.its.cernrfgwt.client.mqttdata.MqttService;
import se.esss.litterbox.its.cernrfgwt.client.mqttdata.MqttServiceAsync;

public class EntryPointApp implements EntryPoint 
{
	public GskelSetupApp setupApp;
	public final MqttServiceAsync mqttService = GWT.create(MqttService.class);
	
	PowerMeterMqttData powerMeter = null;
	ModulatorSettingsMqttData modSettings = null;
	ModulatorReadingsMqttData modReadings = null;
	
	ModulatorSettingPanel modulatorSettingPanel = null;
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
/*		llrfTimer = new MqttData("itsClkRecvr01/set/channel", MqttData.JSONDATA, 1000, this);
		modTimer = new MqttData("itsClkRecvr02/set/channel", MqttData.JSONDATA, 1000, this);
		rfSigGen = new MqttData("itsRfSigGen01/set/rf", MqttData.JSONDATA, 1000, this);
		modReadings = new MqttData("itsCernMod/get/mod", MqttData.BYTEDATA, 1000, this);
		modSettings = new MqttData("itsCernMod/set/mod", MqttData.BYTEDATA, 1000, this);
*/		
		new LlrfPanel("LLRF", this, settingsPermitted);
		modulatorSettingPanel = new ModulatorSettingPanel("Modulator Settings", this, settingsPermitted);
		
		powerMeter = new PowerMeterMqttData("itsPowerMeter01/get", MqttData.JSONDATA, 1000, this);
		modSettings = new ModulatorSettingsMqttData("itsCernMod/set/mod", MqttData.BYTEDATA, 1000, this, modulatorSettingPanel);
		modReadings = new ModulatorReadingsMqttData("itsCernMod/get/mod", MqttData.BYTEDATA, 1000, this, modulatorSettingPanel);
	}
}
