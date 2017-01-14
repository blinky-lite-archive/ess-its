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

import se.esss.litterbox.its.timelinegwt.client.EntryPointApp;
import se.esss.litterbox.its.timelinegwt.client.gskel.GskelVerticalPanel;
import se.esss.litterbox.its.timelinegwt.client.mqttdata.MqttData;


public class TimelinePanel extends GskelVerticalPanel
{
	boolean superCreated = false;
	private boolean settingsPermitted = false;
	private String styleName = "ItsTimelinePanel";
	private ArrayList<TimeLineEventPanel> eventList = null;
	private HorizontalPanel timeLineHorizontalPanel = new HorizontalPanel();
	private EntryPointApp entryPointApp;
	String mqtttopic;
	TimelineMqttData timelineMqttData;
	String oldTimelineString = "";
	Button middleButton = new Button("Change");
	Button addButton = new Button("Add");
	Button setButton = new Button("Set");

	public TimelinePanel(String tabTitle, String mqtttopic, EntryPointApp entryPointApp, boolean settingsPermitted) 
	{
		super(tabTitle, tabTitle, entryPointApp.setupApp);
		this.settingsPermitted = settingsPermitted;
		this.entryPointApp = entryPointApp;
		this.mqtttopic = mqtttopic;
		superCreated = true;
		this.getGskelTabLayoutScrollPanel().setStyleName(styleName);
		eventList = new ArrayList<TimeLineEventPanel>();
		eventList.add(new TimeLineEventPanel(1,255));

		timeLineHorizontalPanel.add(eventList.get(0));
		addButton.setWidth("6.0em");
		addButton.addClickHandler(new ActionButtonClickHandler(addButton));
		addButton.setVisible(false);
		middleButton.setWidth("6.0em");
		middleButton.setEnabled(settingsPermitted);
		middleButton.addClickHandler(new ActionButtonClickHandler(middleButton));
		setButton.setWidth("6.0em");
		setButton.addClickHandler(new ActionButtonClickHandler(setButton));
		setButton.setVisible(false);
		Grid buttonGrid = new Grid(1, 3);
		buttonGrid.setWidth("100%");
		buttonGrid.setWidget(0, 0, addButton);
		buttonGrid.setWidget(0, 1, middleButton);
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
		timelineMqttData = new TimelineMqttData();
	}
	private void setEnabled(boolean enabled)
	{
		addButton.setVisible(enabled);
		setButton.setVisible(enabled);
		middleButton.setText("Change");
		if (enabled) middleButton.setText("Remove");
		for (int ii = 0; ii < eventList.size(); ++ii)
		{
			eventList.get(ii).setEnabled(enabled);
		}
	}
	private void createTimeLine()
	{
		if (middleButton.getText().equals("Remove")) return;
		try 
		{
			String timelineString = timelineMqttData.getJsonValue("timelineSet");
			timelineString = timelineString.trim();
			if (oldTimelineString.equals(timelineString)) return;
			oldTimelineString = timelineString;
			for (int ii = 0; ii < eventList.size(); ++ii)
				timeLineHorizontalPanel.remove(eventList.get(ii));
			String[] splits = timelineString.split(" ");
			eventList = new ArrayList<TimeLineEventPanel>();
			for (int ii = 0; ii < splits.length; ++ii)
			{
				eventList.add(new TimeLineEventPanel(ii + 1, Integer.parseInt(splits[ii])));
				timeLineHorizontalPanel.add(eventList.get(ii));
			}
			setEnabled(false);
		} catch (Exception e) {entryPointApp.setupApp.getStatusTextArea().addStatus(e.getMessage());}
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
			String[][] jsonArray = new String[1][2];
			jsonArray[0][0] = "timelineSet";
			jsonArray[0][1] = eventListString;
			entryPointApp.mqttService.publishJsonArray(mqtttopic, jsonArray, settingsPermitted, entryPointApp.setupApp.isDebug(), "ok", new TimelinePublishSettingsCallback());
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
		Button button;
		ActionButtonClickHandler(Button button)
		{
			this.button = button;
		}
		@Override
		public void onClick(ClickEvent event) 
		{
			if (button.getText().equals("Add")) addEvent();
			if (button.getText().equals("Remove")) removeEvent();
			if (button.getText().equals("Set")) setTimeLine();
			if (button.getText().equals("Change"))setEnabled(true);
		}

	}
	class TimelineMqttData extends MqttData
	{
		public TimelineMqttData() 
		{
			super(mqtttopic, MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try 
			{
				createTimeLine();
			} catch (Exception e) 
			{
				entryPointApp.setupApp.getStatusTextArea().addStatus(e.getMessage());
			}
			
		}
	}
	class TimelinePublishSettingsCallback implements AsyncCallback<String>
	{
		@Override
		public void onFailure(Throwable caught) 
		{
			entryPointApp.setupApp.getStatusTextArea().addStatus("Failure: Setting new Timeline");
			entryPointApp.setupApp.getStatusTextArea().addStatus(caught.getMessage());
			setEnabled(false);
		}

		@Override
		public void onSuccess(String result) 
		{
			entryPointApp.setupApp.getStatusTextArea().addStatus("Success: Setting new Timeline");
			setEnabled(false);
		}
		
	}
}
