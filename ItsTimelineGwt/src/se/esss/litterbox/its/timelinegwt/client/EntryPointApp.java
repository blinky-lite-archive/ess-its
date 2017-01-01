package se.esss.litterbox.its.timelinegwt.client;

import com.google.gwt.core.client.EntryPoint;

import se.esss.litterbox.its.timelinegwt.client.callbacks.CheckIpAddresslAsyncCallback;
import se.esss.litterbox.its.timelinegwt.client.contentpanels.TestVerticalPanel;
import se.esss.litterbox.its.timelinegwt.client.gskel.GskelSetupApp;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EntryPointApp implements EntryPoint 
{
	private GskelSetupApp setupApp;
	public GskelSetupApp getSetupApp() {return setupApp;}

	public void onModuleLoad() 
	{
		setupApp = new GskelSetupApp();
		setupApp.setDebug(false);
		setupApp.setVersionDate("July 19, 2016 14:33");
		setupApp.setVersion("v1.0");
		setupApp.setAuthor("Dave McGinnis david.mcginnis@esss.se");
		setupApp.setLogoImage("images/gwtLogo.jpg");
		setupApp.setLogoTitle("ITS Timeline");
		setupApp.echoVersionInfo();
		setupApp.getEntryPointAppService().checkIpAddress(setupApp.isDebug(), new CheckIpAddresslAsyncCallback(this));		
		
	}
	public void initializeTabs(boolean settingsPermitted)
	{
		new TestVerticalPanel("tab 1", setupApp, settingsPermitted);
		new TestVerticalPanel("tab 2", setupApp, settingsPermitted);
	}
}
