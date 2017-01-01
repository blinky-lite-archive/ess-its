package se.esss.litterbox.its.timelinegwt.client.callbacks;

import com.google.gwt.user.client.rpc.AsyncCallback;

import se.esss.litterbox.its.timelinegwt.client.contentpanels.TestVerticalPanel;

public class TestAsyncCallback implements AsyncCallback<String[]>
{
	private TestVerticalPanel testVerticalPanel;
	public TestAsyncCallback(TestVerticalPanel testVerticalPanel)
	{
		this.testVerticalPanel = testVerticalPanel;
	}
	@Override
	public void onFailure(Throwable caught) 
	{
		testVerticalPanel.getTestButton().setEnabled(true);
		testVerticalPanel.getMessageDialog().setMessage("Error on Call back", caught.getMessage(), true);
		
	}
	@Override
	public void onSuccess(String[] result) 
	{
		testVerticalPanel.getTestButton().setEnabled(true);
		testVerticalPanel.getOptionDialog().setOption("Server Call back ", "Result", result[0], result[1], testVerticalPanel);
		
	}

}
