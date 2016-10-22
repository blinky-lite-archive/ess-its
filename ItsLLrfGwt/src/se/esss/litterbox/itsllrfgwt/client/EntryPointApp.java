package se.esss.litterbox.itsllrfgwt.client;

import com.google.gwt.core.client.EntryPoint;

import se.esss.litterbox.itsllrfgwt.client.contentpanels.TestGskelVerticalPanel;
import se.esss.litterbox.itsllrfgwt.client.gskel.GskelSetupApp;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EntryPointApp implements EntryPoint 
{
	private GskelSetupApp setupApp;

	public void onModuleLoad() 
	{
		setupApp = new GskelSetupApp();
		setupApp.setDebug(false);
		setupApp.setVersionDate("October 22, 2016 14:33");
		setupApp.setVersion("v1.0");
		setupApp.setAuthor("Dave McGinnis david.mcginnis@esss.se");
		setupApp.setLogoImage("images/gwtLogo.jpg");
		setupApp.setLogoTitle("ITS LLRF Control");
		
		new TestGskelVerticalPanel("tab 1", setupApp);
		new TestGskelVerticalPanel("tab 2", setupApp);
		
		
	}
}
