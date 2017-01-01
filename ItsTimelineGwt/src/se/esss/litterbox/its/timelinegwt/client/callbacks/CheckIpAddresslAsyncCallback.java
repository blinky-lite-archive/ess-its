package se.esss.litterbox.its.timelinegwt.client.callbacks;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import se.esss.litterbox.its.timelinegwt.client.EntryPointApp;

public class CheckIpAddresslAsyncCallback implements AsyncCallback<String[]>
{
	EntryPointApp entryPointApp;
	public CheckIpAddresslAsyncCallback(EntryPointApp entryPointApp)
	{
		this.entryPointApp =  entryPointApp;
	}

	@Override
	public void onFailure(Throwable caught) 
	{
		entryPointApp.getSetupApp().getStatusTextArea().addStatus("Error in getting IP Address of client. Error: " + caught.getMessage());
	}

	@Override
	public void onSuccess(String[] result) 
	{
		entryPointApp.getSetupApp().getStatusTextArea().addStatus("IP address = " + result[0]);
		boolean ipOkay = Boolean.parseBoolean(result[1]);
		if (!ipOkay)
		{
			entryPointApp.getSetupApp().getMessageDialog().setImageUrl("images/warning.jpg");
			entryPointApp.getSetupApp().getMessageDialog().setMessage("Warning", "Outside user: You can look but not touch", true);
			WaitTimer waitTimer = new WaitTimer();
			waitTimer.scheduleRepeating(200);
			waitTimer.run();
		}
		else
		{
			entryPointApp.initializeTabs(true);
		}
		
	}
	private class WaitTimer extends Timer
	{
		@Override
		public void run() 
		{
			if (!entryPointApp.getSetupApp().getMessageDialog().isShowing())
			{
				entryPointApp.initializeTabs(false);
				cancel();
			}

		}
		
	}
	
}
