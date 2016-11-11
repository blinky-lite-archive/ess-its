package se.esss.litterbox.itsllrfgwt.client.callbacks;

import com.google.gwt.user.client.rpc.AsyncCallback;

import se.esss.litterbox.itsllrfgwt.client.contentpanels.ModulatorSetupVerticalPanel;

public class GetModulatorStateAsyncCallback implements AsyncCallback<byte[][]>
{
	private ModulatorSetupVerticalPanel modulatorSetupVerticalPanel = null;
	public GetModulatorStateAsyncCallback(ModulatorSetupVerticalPanel modulatorSetupVerticalPanel)
	{
		this.modulatorSetupVerticalPanel =  modulatorSetupVerticalPanel;
	}
	@Override
	public void onFailure(Throwable caught) 
	{
		modulatorSetupVerticalPanel.getStatusTextArea().addStatus("Failure: Getting last known modulator state");
		modulatorSetupVerticalPanel.getStatusTextArea().addStatus(caught.getMessage());
		modulatorSetupVerticalPanel.setGettingModulatorState(false);
	}
	@Override
	public void onSuccess(byte[][] result) 
	{
		try 
		{
			modulatorSetupVerticalPanel.getStatusTextArea().addStatus("Success: Getting last known modulator state");
			modulatorSetupVerticalPanel.getSettingDeviceList().putByteArray(result[0]);
			modulatorSetupVerticalPanel.updateSettingsDisplayFromDevices();
			modulatorSetupVerticalPanel.getReadingDeviceList().putByteArray(result[1]);
			modulatorSetupVerticalPanel.updateReadingsDisplayFromDevices();
			modulatorSetupVerticalPanel.setGettingModulatorState(false);
		} catch (Exception e) 
		{
			modulatorSetupVerticalPanel.getStatusTextArea().addStatus(e.getMessage());
		}
		
	}
}
