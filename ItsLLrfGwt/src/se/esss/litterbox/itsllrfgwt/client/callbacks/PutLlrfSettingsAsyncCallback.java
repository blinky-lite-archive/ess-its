package se.esss.litterbox.itsllrfgwt.client.callbacks;

import com.google.gwt.user.client.rpc.AsyncCallback;

import se.esss.litterbox.itsllrfgwt.client.contentpanels.LLrfSetupVerticalPanel;

public class PutLlrfSettingsAsyncCallback implements AsyncCallback<String>
{
	private LLrfSetupVerticalPanel lLrfSetupVerticalPanel = null;
	public PutLlrfSettingsAsyncCallback(LLrfSetupVerticalPanel lLrfSetupVerticalPanel) 
	{
		this.lLrfSetupVerticalPanel =  lLrfSetupVerticalPanel;
	}
	@Override
	public void onFailure(Throwable caught) 
	{
		lLrfSetupVerticalPanel.getStatusTextArea().addStatus("Failure: Changing LLRF Settings");
		lLrfSetupVerticalPanel.getStatusTextArea().addStatus(caught.getMessage());
	}
	@Override
	public void onSuccess(String result) 
	{
		try 
		{
			lLrfSetupVerticalPanel.getStatusTextArea().addStatus("Success: Changing LLRF Settings");
			lLrfSetupVerticalPanel.getLlrfState();
		} catch (Exception e) 
		{
			lLrfSetupVerticalPanel.getStatusTextArea().addStatus(e.getMessage());
		}
	}

}
