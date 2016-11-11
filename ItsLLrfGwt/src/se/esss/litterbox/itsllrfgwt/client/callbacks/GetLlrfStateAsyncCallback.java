package se.esss.litterbox.itsllrfgwt.client.callbacks;

import com.google.gwt.user.client.rpc.AsyncCallback;

import se.esss.litterbox.itsllrfgwt.client.contentpanels.LLrfSetupVerticalPanel;
import se.esss.litterbox.itsllrfgwt.shared.LlrfData;

public class GetLlrfStateAsyncCallback implements AsyncCallback<LlrfData>
{
	private LLrfSetupVerticalPanel lLrfSetupVerticalPanel = null;
	public GetLlrfStateAsyncCallback(LLrfSetupVerticalPanel lLrfSetupVerticalPanel)
	{
		this.lLrfSetupVerticalPanel =  lLrfSetupVerticalPanel;
	}
	@Override
	public void onFailure(Throwable caught) 
	{
		lLrfSetupVerticalPanel.getStatusTextArea().addStatus("Failure: Getting last known LLRF state");
		lLrfSetupVerticalPanel.getStatusTextArea().addStatus(caught.getMessage());
		lLrfSetupVerticalPanel.setGettingLlrfState(false);
	}
	@Override
	public void onSuccess(LlrfData llrfData) 
	{
		try 
		{
			lLrfSetupVerticalPanel.getStatusTextArea().addStatus("Success: Getting last known LLRF state");
			lLrfSetupVerticalPanel.setLlrfData(llrfData);
			lLrfSetupVerticalPanel.updateSettingDisplay();
			lLrfSetupVerticalPanel.setGettingLlrfState(false);
		} catch (Exception e) 
		{
			lLrfSetupVerticalPanel.getStatusTextArea().addStatus(e.getMessage());
		}
		
	}
}
