package se.esss.litterbox.its.archivergwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import se.esss.litterbox.its.archivergwt.client.callbacks.CheckIpAddresslAsyncCallback;
import se.esss.litterbox.its.archivergwt.client.contentpanels.RegisterTopicsPanel;
import se.esss.litterbox.its.archivergwt.client.contentpanels.TopicPlotPanel;
import se.esss.litterbox.its.archivergwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.its.archivergwt.client.mqttdata.MqttService;
import se.esss.litterbox.its.archivergwt.client.mqttdata.MqttServiceAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EntryPointApp implements EntryPoint 
{
	public GskelSetupApp setupApp;
	public final MqttServiceAsync mqttService = GWT.create(MqttService.class);
	boolean settingsPermitted;
	TopicPlotPanel topicPlotPanel = null;

	public void onModuleLoad() 
	{
		setupApp = new GskelSetupApp(this);
		setupApp.setDebug(false);
		setupApp.setVersionDate("February 23, 2017 09:28");
		setupApp.setVersion("v1.0");
		setupApp.setAuthor("Dave McGinnis david.mcginnis@esss.se");
		setupApp.setLogoImage("images/gwtLogo.jpg");
		setupApp.setLogoTitle("Its Marchiver!");
		setupApp.echoVersionInfo();
		setupApp.getEntryPointAppService().checkIpAddress(setupApp.isDebug(), new CheckIpAddresslAsyncCallback(this));		
		
	}
	public void initializeTabs(boolean settingsPermitted)
	{
		this.settingsPermitted = settingsPermitted;
		topicPlotPanel = new TopicPlotPanel(this, settingsPermitted);
		new RegisterTopicsPanel(topicPlotPanel, this, settingsPermitted);
	}
}
