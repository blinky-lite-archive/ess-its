package se.esss.litterbox.itsllrfgwt.client.handlers;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import se.esss.litterbox.itsllrfgwt.client.contentpanels.LLrfSetupVerticalPanel;

public class LlrfButtonClickHandler  implements ClickHandler
{
	private LLrfSetupVerticalPanel lLrfSetupVerticalPanel;
	private String buttonText = null;
	
	public LlrfButtonClickHandler(String buttonText, LLrfSetupVerticalPanel lLrfSetupVerticalPanel)
	{
		this.lLrfSetupVerticalPanel = lLrfSetupVerticalPanel;
		this.buttonText = buttonText;
	}
	
	@Override
	public void onClick(ClickEvent event) 
	{
		
		if (buttonText.equals("Refresh"))
		{
			lLrfSetupVerticalPanel.putSettings(false);
		}
		if (buttonText.equals("Set"))
		{
			lLrfSetupVerticalPanel.putSettings(false);
		}
		if (buttonText.equals("Init"))
		{
			lLrfSetupVerticalPanel.putSettings(true);
		}
		
	}

}
