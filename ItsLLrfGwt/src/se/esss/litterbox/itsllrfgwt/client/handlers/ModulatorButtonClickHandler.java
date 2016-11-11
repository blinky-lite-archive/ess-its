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
		
		if (buttonText.equals("Refresh"))
		{
			modulatorSetupVerticalPanel.getModulatorState();
		}
		if (buttonText.equals("Set"))
		{
			modulatorSetupVerticalPanel.putSettings();
		}
		
	}

}
