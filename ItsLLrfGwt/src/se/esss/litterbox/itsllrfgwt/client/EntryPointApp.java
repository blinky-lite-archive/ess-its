package se.esss.litterbox.itsllrfgwt.client;

import com.google.gwt.core.client.EntryPoint;

import se.esss.litterbox.itsllrfgwt.client.callbacks.CheckIpAddresslAsyncCallback;
import se.esss.litterbox.itsllrfgwt.client.contentpanels.LLrfSetupVerticalPanel;
import se.esss.litterbox.itsllrfgwt.client.contentpanels.ModulatorDisplayVerticalPanel;
import se.esss.litterbox.itsllrfgwt.client.contentpanels.ModulatorSetupVerticalPanel;
import se.esss.litterbox.itsllrfgwt.client.gskel.GskelSetupApp;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EntryPointApp implements EntryPoint 
{
	static final boolean debug = true;
	private GskelSetupApp setupApp;

	private ModulatorSetupVerticalPanel modulatorSetupVerticalPanel;
	private ModulatorDisplayVerticalPanel modulatorHVPSVerticalPanel;
	private ModulatorDisplayVerticalPanel modulatorReadbacksVerticalPanel;
	private ModulatorDisplayVerticalPanel modulatorInterLocksVerticalPanel1;
	private ModulatorDisplayVerticalPanel modulatorInterLocksVerticalPanel2;
		
	public GskelSetupApp getSetupApp() {return setupApp;}

	public void onModuleLoad() 
	{
		setupApp = new GskelSetupApp();
		setupApp.setDebug(debug);
		setupApp.setVersionDate("November 30, 2016 11:49");
		setupApp.setVersion("v1.8");
		setupApp.setAuthor("Dave McGinnis david.mcginnis@esss.se");
		setupApp.setLogoImage("images/essLogo.png");
		setupApp.setLogoTitle("ITS LLRF Control");
		setupApp.echoVersionInfo();
		setupApp.getEntryPointAppService().checkIpAddress(setupApp.isDebug(), new CheckIpAddresslAsyncCallback(this));
		
	}
	public void initializeTabs(boolean settingsPermitted)
	{
		new LLrfSetupVerticalPanel("LLRF", setupApp, settingsPermitted);
		modulatorSetupVerticalPanel = new ModulatorSetupVerticalPanel("Modulator Setup", setupApp, settingsPermitted);
		modulatorReadbacksVerticalPanel = new ModulatorDisplayVerticalPanel("Modulator Readbacks", setupApp, modulatorSetupVerticalPanel);
		modulatorHVPSVerticalPanel = new ModulatorDisplayVerticalPanel("Modulator HVPS", setupApp, modulatorSetupVerticalPanel);
		modulatorInterLocksVerticalPanel1 = new ModulatorDisplayVerticalPanel("Modulator Interlocks 1", setupApp, modulatorSetupVerticalPanel);
		modulatorInterLocksVerticalPanel2 = new ModulatorDisplayVerticalPanel("Modulator Interlocks 2", setupApp, modulatorSetupVerticalPanel);
		modulatorSetupVerticalPanel.setModulatorReadbacksVerticalPanel(modulatorReadbacksVerticalPanel);
		modulatorSetupVerticalPanel.setModulatorHVPSVerticalPanel(modulatorHVPSVerticalPanel);
		modulatorSetupVerticalPanel.setModulatorInterLocksVerticalPanel1(modulatorInterLocksVerticalPanel1);
		modulatorSetupVerticalPanel.setModulatorInterLocksVerticalPanel2(modulatorInterLocksVerticalPanel2);
	}
}
