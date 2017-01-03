package se.esss.litterbox.its.timelinegwt.client.contentpanels;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

import se.esss.litterbox.its.timelinegwt.client.MqttServiceAsync;
import se.esss.litterbox.its.timelinegwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.its.timelinegwt.client.gskel.GskelVerticalPanel;


public class TimelinePanel extends GskelVerticalPanel
{
	boolean superCreated = false;
	private boolean settingsPermitted = false;
	private String styleName = "ItsTimelinePanel";
	private MqttServiceAsync mqttService;
	private ArrayList<TimeLineEventPanel> eventList = null;
	private HorizontalPanel timeLineHorizontalPanel = new HorizontalPanel();

	public boolean isSettingsPermitted() {return settingsPermitted;}
	public void setSettingsPermitted(boolean settingsPermitted) {this.settingsPermitted = settingsPermitted;}
	public MqttServiceAsync getMqttService() {return mqttService;}

	public TimelinePanel(String tabTitle, GskelSetupApp setupApp, MqttServiceAsync mqttService, boolean settingsPermitted) 
	{
		super(tabTitle, tabTitle, setupApp);
		this.settingsPermitted = settingsPermitted;
		this.mqttService = mqttService;
		superCreated = true;
		this.getGskelTabLayoutScrollPanel().setStyleName(styleName);
		eventList = new ArrayList<TimeLineEventPanel>();
		eventList.add(new TimeLineEventPanel(1,255));
		timeLineHorizontalPanel.add(eventList.get(0));
		String[][] debugResponse = {{"key1", "val1"}, {"key2", "val2"}};
		mqttService.getNameValuePairArray(isDebug(), debugResponse, new GetNameValuePairArrayAsyncCallback(this));
		Button addButton = new Button("Add");
		Button removeButton = new Button("Remove");
		Button setButton = new Button("Set");
		addButton.setWidth("6.0em");
		addButton.setEnabled(settingsPermitted);
		addButton.addClickHandler(new ActionButtonClickHandler(this, addButton));
		removeButton.setWidth("6.0em");
		removeButton.setEnabled(settingsPermitted);
		removeButton.addClickHandler(new ActionButtonClickHandler(this, removeButton));
		setButton.setWidth("6.0em");
		setButton.setEnabled(settingsPermitted);
		setButton.addClickHandler(new ActionButtonClickHandler(this, setButton));
		Grid buttonGrid = new Grid(1, 3);
		buttonGrid.setWidth("100%");
		buttonGrid.setWidget(0, 0, addButton);
		buttonGrid.setWidget(0, 1, removeButton);
		buttonGrid.setWidget(0, 2, setButton);
		HTMLTable.CellFormatter buttonGridFormatter = buttonGrid.getCellFormatter();
		buttonGridFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		buttonGridFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		buttonGridFormatter.setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_RIGHT);

		CaptionPanel actionCaptionPanel = new CaptionPanel("Action");
		actionCaptionPanel.setWidth("30.0em");
		actionCaptionPanel.add(buttonGrid);
		add(actionCaptionPanel);
		CaptionPanel eventCaptionPanel = new CaptionPanel("Events");
		eventCaptionPanel.setWidth("10.0em");
		eventCaptionPanel.add(timeLineHorizontalPanel);
		add(eventCaptionPanel);
	}
	private void createTimeLine(String timelineString)
	{
		for (int ii = 0; ii < eventList.size(); ++ii)
			timeLineHorizontalPanel.remove(eventList.get(ii));
		String splitter = timelineString.trim();
		String[] splits = splitter.split(" ");
		eventList = new ArrayList<TimeLineEventPanel>();
		for (int ii = 0; ii < splits.length; ++ii)
		{
			eventList.add(new TimeLineEventPanel(ii + 1, Integer.parseInt(splits[ii])));
			timeLineHorizontalPanel.add(eventList.get(ii));
		}
	}
	private void addEvent()
	{
		eventList.add(new TimeLineEventPanel(eventList.size() + 1, 0));
		timeLineHorizontalPanel.add(eventList.get(eventList.size() - 1));
	}
	private void removeEvent()
	{
		if (eventList.size() < 2) return;
		TimeLineEventPanel event = eventList.get(eventList.size() - 1);
		timeLineHorizontalPanel.remove(event);
		eventList.remove(event);
	}
	private void setTimeLine()
	{
		try 
		{
			String eventListString = eventList.get(0).readEvent();
			if (eventList.size() > 1)
			{
				for (int ii = 1; ii < eventList.size(); ++ii)
				{
					eventListString = eventListString + " " + eventList.get(ii).readEvent();
				}
			}
			getStatusTextArea().addStatus("Sending Timeline " + eventListString);
			String[] sendData = new String[3];
			sendData[0] = "itsClkTrans01/set/timeline";
			sendData[1] = "timelineSet";
			sendData[2] = eventListString;
			String[] debugResponse = {"key 1", "key 2", "key3"};
			mqttService.setNameValuePairArray(sendData, isDebug(), debugResponse, new SetNameValuePairArrayAsyncCallback(this));
		} catch (Exception e) 
		{
			getMessageDialog().setImageUrl("images/warning.jpg");
			getMessageDialog().setMessage("Warning", "Improper event in time line", true);
		}
		
	}

	@Override
	public void tabLayoutPanelInterfaceAction(String message) 
	{
		if (!superCreated) return;
	}
	@Override
	public void optionDialogInterfaceAction(String choiceButtonText) 
	{
	}
	private class ActionButtonClickHandler implements ClickHandler
	{
		private TimelinePanel timelinePanel;
		Button button;
		ActionButtonClickHandler(TimelinePanel timelinePanel, Button button)
		{
			this.timelinePanel = timelinePanel;
			this.button = button;
		}
		@Override
		public void onClick(ClickEvent event) 
		{
			if (!timelinePanel.isSettingsPermitted())
			{
				button.setEnabled(false);
				timelinePanel.getStatusTextArea().addStatus("Knock it off Inigo!");
				return;
			}
			if (button.getText().equals("Add")) timelinePanel.addEvent();
			if (button.getText().equals("Remove")) timelinePanel.removeEvent();
			if (button.getText().equals("Set")) timelinePanel.setTimeLine();
		}

	}
	private static class GetNameValuePairArrayAsyncCallback implements AsyncCallback<String[][]>
	{
		TimelinePanel timelinePanel;
		GetNameValuePairArrayAsyncCallback(TimelinePanel timelinePanel)
		{
			this.timelinePanel = timelinePanel;
		}
		@Override
		public void onFailure(Throwable caught) 
		{
			timelinePanel.getStatusTextArea().addStatus("Error on GetNameValuePairArrayAsyncCallback: " +  caught.getMessage());
			
		}
		@Override
		public void onSuccess(String[][] result) 
		{
			timelinePanel.getStatusTextArea().addStatus(result[0][0] + " " + result[0][1]);
			timelinePanel.createTimeLine(result[0][1]);
		}
	}
	private static class SetNameValuePairArrayAsyncCallback implements AsyncCallback<String[]>
	{
		TimelinePanel timelinePanel;
		SetNameValuePairArrayAsyncCallback(TimelinePanel timelinePanel)
		{
			this.timelinePanel = timelinePanel;
		}
		@Override
		public void onFailure(Throwable caught) 
		{
			timelinePanel.getStatusTextArea().addStatus("Error on SetNameValuePairArrayAsyncCallback: " +  caught.getMessage());
			
		}
		@Override
		public void onSuccess(String[] result) 
		{
			timelinePanel.getStatusTextArea().addStatus("Success in sending: " + result[0] + " " + result[0] + " " + result[0]);
		}
	}
}
