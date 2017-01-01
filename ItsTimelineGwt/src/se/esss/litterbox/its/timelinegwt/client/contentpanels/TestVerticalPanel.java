package se.esss.litterbox.its.timelinegwt.client.contentpanels;

import com.google.gwt.user.client.ui.Button;

import se.esss.litterbox.its.timelinegwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.its.timelinegwt.client.gskel.GskelVerticalPanel;
import se.esss.litterbox.its.timelinegwt.client.handlers.TestButtonClickHandler;


public class TestVerticalPanel extends GskelVerticalPanel
{
	boolean superCreated = false;
	private Button testButton = new Button("Test");
	private boolean settingsPermitted = false;

	public Button getTestButton() {return testButton;}

	public boolean isSettingsPermitted() {return settingsPermitted;}
	public void setSettingsPermitted(boolean settingsPermitted) {this.settingsPermitted = settingsPermitted;}

	public TestVerticalPanel(String tabTitle, GskelSetupApp setupApp, boolean settingsPermitted) 
	{
		super(tabTitle, tabTitle, setupApp);
		this.settingsPermitted = settingsPermitted;
		add(testButton);
		testButton.setEnabled(settingsPermitted);
		testButton.addClickHandler(new TestButtonClickHandler(this));
		superCreated = true;
	}

	@Override
	public void tabLayoutPanelInterfaceAction(String message) 
	{
		if (!superCreated) return;
		getStatusTextArea().addStatus("Tab " + this.getTabValue() + " " + message);
		getSetupApp().getMessageDialog().setImageUrl("images/gwtLogo.jpg");
		getMessageDialog().setMessage("Tab", this.getTabValue() + " " + message, true);
//		getStatusTextArea().addStatus("Tab " + "booger" + " " + message);
//		getMessageDialog().setMessage("Tab", "booger" + " " + message, true);
	}
	@Override
	public void optionDialogInterfaceAction(String choiceButtonText) 
	{
		getStatusTextArea().addStatus("You chose " + choiceButtonText);
		getSetupApp().getMessageDialog().setImageUrl("images/gwtLogo.jpg");
		getMessageDialog().setMessage("Choice", "You chose " + choiceButtonText, true);
	}
}
