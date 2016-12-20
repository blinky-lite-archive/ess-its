package se.esss.litterbox.itsllrfgwt.client.callbacks;

import com.google.gwt.user.client.rpc.AsyncCallback;

import se.esss.litterbox.itsllrfgwt.client.contentpanels.LLrfSetupVerticalPanel;
import se.esss.litterbox.itsllrfgwt.shared.LlrfData;

public class GetLlrfStateAsyncCallback implements AsyncCallback<LlrfData>
{
	private LLrfSetupVerticalPanel lLrfSetupVerticalPanel = null;
	private boolean alsoSettingsUpdate;
	public GetLlrfStateAsyncCallback(LLrfSetupVerticalPanel lLrfSetupVerticalPanel, boolean alsoSettingsUpdate)
	{
		this.lLrfSetupVerticalPanel =  lLrfSetupVerticalPanel;
		this.alsoSettingsUpdate =  alsoSettingsUpdate;
	}
	@Override
	public void onFailure(Throwable caught) 
	{
		lLrfSetupVerticalPanel.getSetupApp().getMessageDialog().hide();
		lLrfSetupVerticalPanel.getStatusTextArea().addStatus("Failure: Getting last known LLRF state");
		lLrfSetupVerticalPanel.getStatusTextArea().addStatus(caught.getMessage());
		lLrfSetupVerticalPanel.setGettingLlrfState(false);
	}
	@Override
	public void onSuccess(LlrfData llrfData) 
	{
		try 
		{
//			lLrfSetupVerticalPanel.getStatusTextArea().addStatus("Success: Getting last known LLRF state");
			lLrfSetupVerticalPanel.setLlrfData(llrfData);
			lLrfSetupVerticalPanel.updateSettingDisplay(alsoSettingsUpdate);
			lLrfSetupVerticalPanel.setGettingLlrfState(false);
//			lLrfSetupVerticalPanel.getSetupApp().getMessageDialog().hide();
		} catch (Exception e) 
		{
			lLrfSetupVerticalPanel.getStatusTextArea().addStatus(e.getMessage());
		}
		
	}
}
