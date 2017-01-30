package se.esss.litterbox.its.llrfgwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import se.esss.litterbox.its.llrfgwt.client.callbacks.CheckIpAddresslAsyncCallback;
import se.esss.litterbox.its.llrfgwt.client.contentpanels.LlrfPanel;
import se.esss.litterbox.its.llrfgwt.client.gskel.GskelLoadWaiter;
import se.esss.litterbox.its.llrfgwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.its.llrfgwt.client.mqttdata.MqttService;
import se.esss.litterbox.its.llrfgwt.client.mqttdata.MqttServiceAsync;

public class EntryPointApp implements EntryPoint 
{
	public GskelSetupApp setupApp;
	public final MqttServiceAsync mqttService = GWT.create(MqttService.class);
	boolean settingsPermitted;
	
	LlrfPanel llrfPanel = null;
	public void onModuleLoad() 
	{
		setupApp = new GskelSetupApp();
		setupApp.setDebug(false);
		setupApp.setVersionDate("January 30, 2017 07:34");
		setupApp.setVersion("v1.0");
		setupApp.setAuthor("Dave McGinnis david.mcginnis@esss.se");
		setupApp.setLogoImage("images/gwtLogo.jpg");
		setupApp.setLogoTitle("ITS LLRF!");
		setupApp.echoVersionInfo();

		setupApp.getEntryPointAppService().checkIpAddress(setupApp.isDebug(), new CheckIpAddresslAsyncCallback(this));		
		
	}
	public void initializeTabs(boolean settingsPermitted)
	{
		this.settingsPermitted = settingsPermitted;
		llrfPanel = new LlrfPanel("LLRF", "itsClkRecvr01/set/channel", "itsPowerMeter01/get", "itsRfSigGen01/set/rf",  this, settingsPermitted);
		new TabLoadWaiter(100, 0);
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
			if (getItask() == 0) ;
		}
		
	}
}
