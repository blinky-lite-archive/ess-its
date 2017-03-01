package se.esss.litterbox.its.dashboardgwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import se.esss.litterbox.its.dashboardgwt.client.contentpanels.GaugeDashboard;
import se.esss.litterbox.its.dashboardgwt.client.mqttdata.MqttService;
import se.esss.litterbox.its.dashboardgwt.client.mqttdata.MqttServiceAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EntryPointApp implements EntryPoint 
{
	public final MqttServiceAsync mqttService = GWT.create(MqttService.class);
	public final EntryPointAppServiceAsync entryPointAppService = GWT.create(EntryPointAppService.class);
	boolean settingsPermitted;

	public void onModuleLoad() 
	{
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.add(new GaugeDashboard("itsWaterSystem/get", "itsCernMod/get/temp", "itspressureMeter/get", this));
		Frame shiftrFrame = new Frame("https://shiftr.io/dmcginnis427/ess-integration-test-stand/embed?zoom=1");
		shiftrFrame.setSize(705 + "px", 705 + "px");
		hp1.add(shiftrFrame);
		RootLayoutPanel.get().add(hp1);
	}
}
