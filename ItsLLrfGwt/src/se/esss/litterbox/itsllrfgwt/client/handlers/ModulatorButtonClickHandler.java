package se.esss.litterbox.itsllrfgwt.client.handlers;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import se.esss.litterbox.itsllrfgwt.client.contentpanels.ModulatorSetupVerticalPanel;

public class ModulatorButtonClickHandler  implements ClickHandler
{
	private ModulatorSetupVerticalPanel modulatorSetupVerticalPanel;
	private String buttonText = null;
	
	public ModulatorButtonClickHandler(String buttonText, ModulatorSetupVerticalPanel modulatorSetupVerticalPanel)
	{
		this.modulatorSetupVerticalPanel = modulatorSetupVerticalPanel;
		this.buttonText = buttonText;
	}
	
	@Override
	public void onClick(ClickEvent event) 
	{
		if (!modulatorSetupVerticalPanel.isSettingsPermitted())
		{
			for (int ii = 0; ii < 4; ++ii)
			{
				modulatorSetupVerticalPanel.getModStateButton()[ii].setEnabled(false);
			}
			modulatorSetupVerticalPanel.getSetModulatorSettingsButton().setEnabled(false);
			modulatorSetupVerticalPanel.getStatusTextArea().addStatus("Knock it off Inigo!");
			return;
		}
		if (buttonText.equals("Set"))
		{
		}
		if (buttonText.equals("Reset"))
		{
			modulatorSetupVerticalPanel.getSettingDeviceDisplayList().get(0).getEnabledCheckBox().setValue(true);
			modulatorSetupVerticalPanel.getStatusTextArea().addStatus("Reseting Modulator");
		}
		if (buttonText.equals("Off"))
		{
			modulatorSetupVerticalPanel.getSettingDeviceDisplayList().get(3).getSettingTextBox().setText("0");
			modulatorSetupVerticalPanel.getStatusTextArea().addStatus("Turning Modulator Off");
		}
		if (buttonText.equals("StandBy"))
		{
			modulatorSetupVerticalPanel.getSettingDeviceDisplayList().get(3).getSettingTextBox().setText("1");
			modulatorSetupVerticalPanel.getStatusTextArea().addStatus("Putting Modulator in Standby");
		}
		if (buttonText.equals("On"))
		{
			modulatorSetupVerticalPanel.getSettingDeviceDisplayList().get(3).getSettingTextBox().setText("3");
			modulatorSetupVerticalPanel.getStatusTextArea().addStatus("Turning Modulator On");
		}
		modulatorSetupVerticalPanel.putSettings();
		
	}

}
