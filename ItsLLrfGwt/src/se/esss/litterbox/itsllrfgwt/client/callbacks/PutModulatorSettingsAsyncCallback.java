package se.esss.litterbox.itsllrfgwt.client.callbacks;

import com.google.gwt.user.client.rpc.AsyncCallback;

import se.esss.litterbox.itsllrfgwt.client.contentpanels.ModulatorSetupVerticalPanel;

public class PutModulatorSettingsAsyncCallback implements AsyncCallback<String>
{
	private ModulatorSetupVerticalPanel modulatorSetupVerticalPanel = null;
	public PutModulatorSettingsAsyncCallback(ModulatorSetupVerticalPanel modulatorSetupVerticalPanel) 
	{
		this.modulatorSetupVerticalPanel =  modulatorSetupVerticalPanel;
	}
	@Override
	public void onFailure(Throwable caught) 
	{
		modulatorSetupVerticalPanel.getSetupApp().getMessageDialog().hide();
		modulatorSetupVerticalPanel.getStatusTextArea().addStatus("Failure: Putting Modulator Settings");
		modulatorSetupVerticalPanel.getStatusTextArea().addStatus(caught.getMessage());
		modulatorSetupVerticalPanel.setPuttingSettingsState(false);
	}
	@Override
	public void onSuccess(String result) 
	{
		try 
		{
			modulatorSetupVerticalPanel.getSetupApp().getMessageDialog().hide();
			modulatorSetupVerticalPanel.setPuttingSettingsState(false);
			modulatorSetupVerticalPanel.getStatusTextArea().addStatus("Success: Putting Modulator Settings");
			modulatorSetupVerticalPanel.getModulatorState();
		} catch (Exception e) 
		{
			modulatorSetupVerticalPanel.getStatusTextArea().addStatus(e.getMessage());
		}
	}

}
