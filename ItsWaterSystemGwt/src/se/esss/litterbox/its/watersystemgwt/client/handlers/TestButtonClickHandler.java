package se.esss.litterbox.its.watersystemgwt.client.handlers;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import se.esss.litterbox.its.watersystemgwt.client.callbacks.TestAsyncCallback;
import se.esss.litterbox.its.watersystemgwt.client.contentpanels.TestVerticalPanel;

public class TestButtonClickHandler implements ClickHandler
{
	private TestVerticalPanel testVerticalPanel;
	public TestButtonClickHandler(TestVerticalPanel testVerticalPanel)
	{
		this.testVerticalPanel = testVerticalPanel;
	}
	@Override
	public void onClick(ClickEvent event) 
	{
		if (!testVerticalPanel.isSettingsPermitted())
		{
			testVerticalPanel.getTestButton().setEnabled(false);
			testVerticalPanel.getStatusTextArea().addStatus("Knock it off Inigo!");
			return;
		}
		String[] debugResponse = {"Yes", "No"};
		testVerticalPanel.getTestButton().setEnabled(false);
		testVerticalPanel.getEntryPointAppService().gskelServerTest("Hi There", testVerticalPanel.isDebug(), debugResponse, new TestAsyncCallback(testVerticalPanel));
	}

}
