package se.esss.litterbox.itsllrfgwt.client.callbacks;

import com.google.gwt.user.client.rpc.AsyncCallback;

import se.esss.litterbox.itsllrfgwt.client.contentpanels.ModulatorSetupVerticalPanel;
import se.esss.litterbox.itsllrfgwt.shared.IceCubeDeviceList;

public class GetModulatorProtocolslAsyncCallback implements AsyncCallback<String[][]>
{
	private ModulatorSetupVerticalPanel modulatorSetupVerticalPanel = null;
	public GetModulatorProtocolslAsyncCallback(ModulatorSetupVerticalPanel modulatorSetupVerticalPanel)
	{
		this.modulatorSetupVerticalPanel =  modulatorSetupVerticalPanel;
	}

	@Override
	public void onFailure(Throwable caught) 
	{
		modulatorSetupVerticalPanel.getStatusTextArea().addStatus("Failure: Getting Protocol file");
		modulatorSetupVerticalPanel.getStatusTextArea().addStatus(caught.getMessage());
		modulatorSetupVerticalPanel.setSuccessfulSetup(false); 
		modulatorSetupVerticalPanel.setPanelCreated(true);
	}

	@Override
	public void onSuccess(String[][] result) 
	{
		try 
		{
			modulatorSetupVerticalPanel.getStatusTextArea().addStatus("Success: Getting Protocol file");
			modulatorSetupVerticalPanel.setSettingDeviceList(new IceCubeDeviceList(result[0]));
			modulatorSetupVerticalPanel.setReadingDeviceList(new IceCubeDeviceList(result[1]));
			modulatorSetupVerticalPanel.setSuccessfulSetup(true);
			modulatorSetupVerticalPanel.setPanelCreated(true);
			if (modulatorSetupVerticalPanel.isCreatingPanel()) modulatorSetupVerticalPanel.setCreatingPanel(false);
			modulatorSetupVerticalPanel.getSettingsReadingsHorizontalPanel().add(modulatorSetupVerticalPanel.settingsCaptionPanel());
			modulatorSetupVerticalPanel.setupReadingsDisplayPanels();
			modulatorSetupVerticalPanel.getModulatorState();
			
		} catch (Exception e) 
		{
			modulatorSetupVerticalPanel.getStatusTextArea().addStatus(e.getMessage());
		}
		
	}
	
}
