package se.esss.litterbox.its.cernrfgwt.client.contentpanels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

import se.esss.litterbox.its.cernrfgwt.client.EntryPointApp;
import se.esss.litterbox.its.cernrfgwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.its.cernrfgwt.client.gskel.GskelVerticalPanel;
import se.esss.litterbox.its.cernrfgwt.client.mqttdata.MqttServiceAsync;


public class LlrfPanel extends GskelVerticalPanel
{
	boolean superCreated = false;
	private boolean settingsPermitted = false;
	private EntryPointApp entryPointApp;

	public boolean isSettingsPermitted() {return settingsPermitted;}
	public void setSettingsPermitted(boolean settingsPermitted) {this.settingsPermitted = settingsPermitted;}

	public LlrfPanel(String tabTitle, EntryPointApp entryPointApp, boolean settingsPermitted) 
	{
		super(tabTitle, "llrfTabStyle", entryPointApp.setupApp);
		this.settingsPermitted = settingsPermitted;
		this.entryPointApp = entryPointApp;
		superCreated = true;
		this.getGskelTabLayoutScrollPanel().setStyleName("ItsLlrfPanel");
	}

	@Override
	public void tabLayoutPanelInterfaceAction(String message) 
	{
		if (!superCreated) return;
	}
	@Override
	public void optionDialogInterfaceAction(String choiceButtonText) 
	{
	}
	private class ActionButtonClickHandler implements ClickHandler
	{
		private LlrfPanel llrfPanel;
		Button button;
		ActionButtonClickHandler(LlrfPanel llrfPanel, Button button)
		{
			this.llrfPanel = llrfPanel;
			this.button = button;
		}
		@Override
		public void onClick(ClickEvent event) 
		{
			if (!llrfPanel.isSettingsPermitted())
			{
				button.setEnabled(false);
				llrfPanel.getStatusTextArea().addStatus("Knock it off Inigo!");
				return;
			}
			if (button.getText().equals("Add")) ;
		}

	}
}
