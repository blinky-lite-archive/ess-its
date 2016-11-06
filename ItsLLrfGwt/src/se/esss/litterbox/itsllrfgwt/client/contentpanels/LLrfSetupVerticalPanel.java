package se.esss.litterbox.itsllrfgwt.client.contentpanels;

import se.esss.litterbox.itsllrfgwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.itsllrfgwt.client.gskel.GskelVerticalPanel;


public class LLrfSetupVerticalPanel extends GskelVerticalPanel
{
	boolean superCreated = false;

	public LLrfSetupVerticalPanel(String tabTitle, GskelSetupApp setupApp) 
	{
		super(tabTitle, tabTitle, setupApp);
		superCreated = true;
	}

	@Override
	public void tabLayoutPanelInterfaceAction(String message) 
	{
		if (!superCreated) return;
		getStatusTextArea().addStatus("Tab " + this.getTabValue() + " " + message);
	}
	@Override
	public void optionDialogInterfaceAction(String choiceButtonText) 
	{
	}
}
