package se.esss.litterbox.itsllrfgwt.client;

import com.google.gwt.core.client.EntryPoint;

import se.esss.litterbox.itsllrfgwt.client.contentpanels.LLrfSetupVerticalPanel;
import se.esss.litterbox.itsllrfgwt.client.contentpanels.ModulatorDisplayVerticalPanel;
import se.esss.litterbox.itsllrfgwt.client.contentpanels.ModulatorSetupVerticalPanel;
import se.esss.litterbox.itsllrfgwt.client.gskel.GskelSetupApp;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EntryPointApp implements EntryPoint 
{
	static final boolean debug = false;
	private GskelSetupApp setupApp;
	private ModulatorSetupVerticalPanel modulatorSetupVerticalPanel;
	private ModulatorDisplayVerticalPanel modulatorHVPSVerticalPanel;
	private ModulatorDisplayVerticalPanel modulatorReadbacksVerticalPanel;
	private ModulatorDisplayVerticalPanel modulatorInterLocksVerticalPanel1;
	private ModulatorDisplayVerticalPanel modulatorInterLocksVerticalPanel2;
		
	public void onModuleLoad() 
	{
		setupApp = new GskelSetupApp();
		setupApp.setDebug(debug);
		setupApp.setVersionDate("November 10, 2016 11:44");
		setupApp.setVersion("v1.3");
		setupApp.setAuthor("Dave McGinnis david.mcginnis@esss.se");
		setupApp.setLogoImage("images/essLogo.png");
		setupApp.setLogoTitle("ITS LLRF Control");
		setupApp.echoVersionInfo();
		
		new LLrfSetupVerticalPanel("LLRF", setupApp);
		modulatorSetupVerticalPanel = new ModulatorSetupVerticalPanel("Modulator Setup", setupApp);
		modulatorReadbacksVerticalPanel = new ModulatorDisplayVerticalPanel("Modulator Readbacks", setupApp);
		modulatorHVPSVerticalPanel = new ModulatorDisplayVerticalPanel("Modulator HVPS", setupApp);
		modulatorInterLocksVerticalPanel1 = new ModulatorDisplayVerticalPanel("Modulator Interlocks 1", setupApp);
		modulatorInterLocksVerticalPanel2 = new ModulatorDisplayVerticalPanel("Modulator Interlocks 2", setupApp);
		modulatorSetupVerticalPanel.setModulatorReadbacksVerticalPanel(modulatorReadbacksVerticalPanel);
		modulatorSetupVerticalPanel.setModulatorHVPSVerticalPanel(modulatorHVPSVerticalPanel);
		modulatorSetupVerticalPanel.setModulatorInterLocksVerticalPanel1(modulatorInterLocksVerticalPanel1);
		modulatorSetupVerticalPanel.setModulatorInterLocksVerticalPanel2(modulatorInterLocksVerticalPanel2);
		
	}
}
