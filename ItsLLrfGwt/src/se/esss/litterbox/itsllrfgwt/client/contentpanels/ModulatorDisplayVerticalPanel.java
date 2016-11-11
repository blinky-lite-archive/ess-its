package se.esss.litterbox.itsllrfgwt.client.contentpanels;

import se.esss.litterbox.itsllrfgwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.itsllrfgwt.client.gskel.GskelVerticalPanel;

public class ModulatorDisplayVerticalPanel extends GskelVerticalPanel 
{
	public ModulatorDisplayVerticalPanel(String tabTitle, GskelSetupApp setupApp) 
	{
		super(tabTitle, tabTitle, setupApp);
	}
	@Override
	public void tabLayoutPanelInterfaceAction(String message) 
	{
	}
	@Override
	public void optionDialogInterfaceAction(String choiceButtonText) 
	{
	}
}
