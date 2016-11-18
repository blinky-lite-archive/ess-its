package se.esss.litterbox.itsllrfgwt.client.contentpanels;

import se.esss.litterbox.itsllrfgwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.itsllrfgwt.client.gskel.GskelVerticalPanel;

public class ModulatorDisplayVerticalPanel extends GskelVerticalPanel 
{
	ModulatorSetupVerticalPanel modulatorSetupVerticalPanel;
	public ModulatorDisplayVerticalPanel(String tabTitle, GskelSetupApp setupApp, ModulatorSetupVerticalPanel modulatorSetupVerticalPanel) 
	{
		super(tabTitle, tabTitle, setupApp);
		this.modulatorSetupVerticalPanel = modulatorSetupVerticalPanel;
		this.getGskelTabLayoutScrollPanel().setStyleName(modulatorSetupVerticalPanel.getStyleName());
	}
	@Override
	public void tabLayoutPanelInterfaceAction(String message) 
	{
		modulatorSetupVerticalPanel.initializePanel();
	}
	@Override
	public void optionDialogInterfaceAction(String choiceButtonText) 
	{
	}
}
