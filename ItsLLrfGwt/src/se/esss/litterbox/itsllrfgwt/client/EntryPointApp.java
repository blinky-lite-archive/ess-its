package se.esss.litterbox.itsllrfgwt.client;

import com.google.gwt.core.client.EntryPoint;

import se.esss.litterbox.itsllrfgwt.client.contentpanels.LLrfSetupVerticalPanel;
import se.esss.litterbox.itsllrfgwt.client.contentpanels.ModulatorSetupVerticalPanel;
import se.esss.litterbox.itsllrfgwt.client.gskel.GskelSetupApp;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EntryPointApp implements EntryPoint 
{
	static final boolean debug = false;
	private GskelSetupApp setupApp;
		
	public void onModuleLoad() 
	{
		setupApp = new GskelSetupApp();
		setupApp.setDebug(debug);
		setupApp.setVersionDate("November 6, 2016 14:10");
		setupApp.setVersion("v1.0");
		setupApp.setAuthor("Dave McGinnis david.mcginnis@esss.se");
		setupApp.setLogoImage("images/essLogo.png");
		setupApp.setLogoTitle("ITS LLRF Control");
		setupApp.echoVersionInfo();
		
		new LLrfSetupVerticalPanel("LLRF", setupApp);
		new ModulatorSetupVerticalPanel("Modulator Setup", setupApp);
		
		
	}
}
