package se.esss.litterbox.its.cernrfgwt.client.contentpanels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;

import se.esss.litterbox.its.cernrfgwt.client.MqttServiceAsync;
import se.esss.litterbox.its.cernrfgwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.its.cernrfgwt.client.gskel.GskelVerticalPanel;


public class LlrfPanel extends GskelVerticalPanel
{
	boolean superCreated = false;
	private boolean settingsPermitted = false;
	private String styleName = "ItsLlrfPanel";
	private MqttServiceAsync mqttService;

	public boolean isSettingsPermitted() {return settingsPermitted;}
	public void setSettingsPermitted(boolean settingsPermitted) {this.settingsPermitted = settingsPermitted;}
	public MqttServiceAsync getMqttService() {return mqttService;}

	public LlrfPanel(String tabTitle, GskelSetupApp setupApp, MqttServiceAsync mqttService, boolean settingsPermitted) 
	{
		super(tabTitle, tabTitle, setupApp);
		this.settingsPermitted = settingsPermitted;
		this.mqttService = mqttService;
		superCreated = true;
		this.getGskelTabLayoutScrollPanel().setStyleName(styleName);
		String[][] debugResponse = {{"key1", "val1"}, {"key2", "val2"}};
		mqttService.getNameValuePairArray(isDebug(), debugResponse, new GetNameValuePairArrayAsyncCallback(this));
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
	private static class GetNameValuePairArrayAsyncCallback implements AsyncCallback<String[][]>
	{
		LlrfPanel llrfPanel;
		GetNameValuePairArrayAsyncCallback(LlrfPanel llrfPanel)
		{
			this.llrfPanel = llrfPanel;
		}
		@Override
		public void onFailure(Throwable caught) 
		{
			llrfPanel.getStatusTextArea().addStatus("Error on GetNameValuePairArrayAsyncCallback: " +  caught.getMessage());
			
		}
		@Override
		public void onSuccess(String[][] result) 
		{
			llrfPanel.getStatusTextArea().addStatus(result[0][0] + " " + result[0][1]);
		}
	}
	private static class SetNameValuePairArrayAsyncCallback implements AsyncCallback<String[]>
	{
		LlrfPanel llrfPanel;
		SetNameValuePairArrayAsyncCallback(LlrfPanel llrfPanel)
		{
			this.llrfPanel = llrfPanel;
		}
		@Override
		public void onFailure(Throwable caught) 
		{
			llrfPanel.getStatusTextArea().addStatus("Error on SetNameValuePairArrayAsyncCallback: " +  caught.getMessage());
			
		}
		@Override
		public void onSuccess(String[] result) 
		{
			llrfPanel.getStatusTextArea().addStatus("Success in sending: " + result[0] + " " + result[0] + " " + result[0]);
		}
	}
}
