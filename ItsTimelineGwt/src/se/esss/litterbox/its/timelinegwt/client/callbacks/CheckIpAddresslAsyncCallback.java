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
		entryPointApp.setupApp.getStatusTextArea().addStatus("Error in getting IP Address of client. Error: " + caught.getMessage());
	}

	@Override
	public void onSuccess(String[] result) 
	{
		entryPointApp.setupApp.getStatusTextArea().addStatus("User Name = " + result[0]);
		boolean ipOkay = Boolean.parseBoolean(result[1]);
		if (!ipOkay)
		{
			entryPointApp.setupApp.getMessageDialog().setImageUrl("images/warning.jpg");
			entryPointApp.setupApp.getMessageDialog().setMessage("Warning", result[0] + ": You can look but not touch", true);
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
			if (!entryPointApp.setupApp.getMessageDialog().isShowing())
			{
				entryPointApp.initializeTabs(false);
				cancel();
			}

		}
		
	}
	
}
