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
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

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
	String timelineMqtttopic;
	String eventFreqMqtttopic;
	TimelineMqttData timelineMqttData;
	String oldTimelineString = "";
	Button middleButton = new Button("Change");
	Button addButton = new Button("Add");
	Button setButton = new Button("Set");
	TextBox eventFreqTextBox = new TextBox();

	public TimelinePanel(String tabTitle, String timelineMqtttopic, String eventFreqMqtttopic, EntryPointApp entryPointApp, boolean settingsPermitted) 
	{
		super(tabTitle, tabTitle, entryPointApp.setupApp);
		this.settingsPermitted = settingsPermitted;
		this.entryPointApp = entryPointApp;
		this.timelineMqtttopic = timelineMqtttopic;
		this.eventFreqMqtttopic = eventFreqMqtttopic;
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
		
		HorizontalPanel eventFreqHp = new HorizontalPanel();
		eventFreqHp.setWidth("15.0em");
		eventFreqHp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		eventFreqHp.add(new Label("Event Frequency "));
		eventFreqTextBox.setWidth("3.0em");
		eventFreqTextBox.setEnabled(false);
		eventFreqHp.add(eventFreqTextBox);
		eventFreqHp.add(new Label(" Hz"));
		CaptionPanel eventFreqCp = new CaptionPanel();
		eventFreqCp.setWidth("30.0em");
		eventFreqCp.add(eventFreqHp);

		CaptionPanel actionCaptionPanel = new CaptionPanel("Action");
		actionCaptionPanel.setWidth("30.0em");
		actionCaptionPanel.add(buttonGrid);
		add(actionCaptionPanel);
		CaptionPanel eventCaptionPanel = new CaptionPanel("Events");
		eventCaptionPanel.setWidth("10.0em");
		eventCaptionPanel.add(timeLineHorizontalPanel);
		add(eventFreqCp);
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
		eventFreqTextBox.setEnabled(enabled);
	}
	private void createTimeLine()
	{
		if (middleButton.getText().equals("Remove")) return;
		setEnabled(false);
		try 
		{
			eventFreqTextBox.setText(timelineMqttData.getJsonValue("freqSet"));
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
			String[][] jsonArray = new String[2][2];
			jsonArray[0][0] = "timelineSet";
			jsonArray[0][1] = eventListString;
			
			double  eventFreq = Double.parseDouble(eventFreqTextBox.getText());
			if (eventFreq < 1.0) throw new Exception("Event Frequency less than 1 Hz");
			if (eventFreq > 20.0) throw new Exception("Event Frequency greater than 20 Hz");
			jsonArray[1][0] = "freqSet";
			jsonArray[1][1] = Double.toString(eventFreq);
			
			entryPointApp.mqttService.publishJsonArray(timelineMqtttopic, jsonArray, settingsPermitted, entryPointApp.setupApp.isDebug(), "ok", new PublishSettingsCallback("Setting new Timeline"));
		} catch (Exception e) 
		{
			entryPointApp.setupApp.getStatusTextArea().addStatus(e.getMessage());
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
			if (button.getText().equals("Set")) {setTimeLine();}
			if (button.getText().equals("Change"))setEnabled(true);
		}

	}
	class TimelineMqttData extends MqttData
	{
		public TimelineMqttData() 
		{
			super(timelineMqtttopic, MqttData.JSONDATA, 1000, entryPointApp);
		}
		@Override
		public void doSomethingWithData() 
		{
			try {createTimeLine();} catch (Exception e) {entryPointApp.setupApp.getStatusTextArea().addStatus(e.getMessage());}
		}
	}
	class PublishSettingsCallback implements AsyncCallback<String>
	{
		String message;
		PublishSettingsCallback(String message)
		{
			this.message = message;
		}
		@Override
		public void onFailure(Throwable caught) 
		{
			entryPointApp.setupApp.getStatusTextArea().addStatus("Failure: " + message);
			entryPointApp.setupApp.getStatusTextArea().addStatus(caught.getMessage());
			setEnabled(false);
		}
		@Override
		public void onSuccess(String result) 
		{
			entryPointApp.setupApp.getStatusTextArea().addStatus("Success: " + message);
			setEnabled(false);
		}
		
	}
}
