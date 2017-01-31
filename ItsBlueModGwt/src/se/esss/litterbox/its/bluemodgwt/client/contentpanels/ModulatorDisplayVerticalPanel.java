package se.esss.litterbox.its.bluemodgwt.client.contentpanels;

import se.esss.litterbox.its.bluemodgwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.its.bluemodgwt.client.gskel.GskelVerticalPanel;

public class ModulatorDisplayVerticalPanel extends GskelVerticalPanel 
{
	public ModulatorDisplayVerticalPanel(String tabTitle, GskelSetupApp setupApp) 
	{
		super(tabTitle, tabTitle, setupApp);
		this.getGskelTabLayoutScrollPanel().setStyleName("ItsModSettingsPanel");
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
