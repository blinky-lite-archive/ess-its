package se.esss.litterbox.its.archivergwt.client.contentpanels;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import se.esss.litterbox.its.archivergwt.client.EntryPointApp;
import se.esss.litterbox.its.archivergwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.its.archivergwt.client.gskel.GskelVerticalPanel;
import se.esss.litterbox.its.archivergwt.shared.ArchiveTopic;

public class RegisterTopicsPanel extends GskelVerticalPanel
{
	private boolean settingsPermitted = false;
	private EntryPointApp entryPointApp;
	private Grid topicGrid;
	private CaptionPanel actionCaptionPanel;
	private CaptionPanel addTopicCaptionPanel;
	private CaptionPanel deleteTopicCaptionPanel;
	private TextBox addTopicTextBox = new TextBox();
	private TextBox addTopicPeriodTextBox = new TextBox();
	private RadioButton jsondataRadioButton = new RadioButton("dataTypeRadioGroup", "JSONDATA");
	private TextBox deleteTopicTextBox = new TextBox();
	private int lastTopicIndex = 0;

	public RegisterTopicsPanel(GskelSetupApp setupApp,  EntryPointApp entryPointApp, boolean settingsPermitted) 
	{
		super("Register Topics", "noTabStyle", setupApp);
		this.getGskelTabLayoutScrollPanel().setStyleName("GskelVertPanel");
		this.settingsPermitted = settingsPermitted;
		this.entryPointApp = entryPointApp;
		entryPointApp.mqttService.getTopicList(entryPointApp.setupApp.isDebug(), null, new GetTopicListCallback());
		TopicListTimer tlt = new TopicListTimer();
		tlt.scheduleRepeating(10000);
		HorizontalPanel hp1 = new HorizontalPanel();
		setupActionPanel();
		hp1.add(actionCaptionPanel);
		topicGrid = new Grid(1,7);
		hp1.add(topicGrid);
		add(hp1);
	}
	private void updateTopicGrid(ArrayList<ArchiveTopic> archiveTopicList)
	{
		topicGrid.resizeRows(archiveTopicList.size() + 1);
		topicGrid.setBorderWidth(1);
		RowFormatter rf  = topicGrid.getRowFormatter();
		CellFormatter cf = topicGrid.getCellFormatter();
		
		topicGrid.setWidget(0, 0, new Label("ID"));
		topicGrid.setWidget(0, 1, new Label("Topic"));
		topicGrid.setWidget(0, 2, new Label("Type"));
		topicGrid.setWidget(0, 3, new Label("Period"));
		topicGrid.setWidget(0, 4, new Label("Created"));
		topicGrid.setWidget(0, 5, new Label("Last Written"));
		topicGrid.setWidget(0, 6, new Label("Payload"));
		for (int ii = 0; ii < 7; ++ii)
		{
			cf.setHorizontalAlignment(0, ii, HasHorizontalAlignment.ALIGN_CENTER);
			cf.setVerticalAlignment(0, ii, HasVerticalAlignment.ALIGN_MIDDLE);	
		}
		rf.setStyleName(0, "TopicGridHeaderRow");
		lastTopicIndex = 0;
		for (int ii = 0; ii < archiveTopicList.size(); ++ii)
		{
			rf.setStyleName(ii + 1, "TopicGridDataRow");
			String datatypeString = "JSONDATA";
			if (archiveTopicList.get(ii).getDataType() == ArchiveTopic.BYTEDATA) datatypeString = "BYTEDATA";
			topicGrid.setWidget(ii + 1, 0, new Label(Integer.toString(archiveTopicList.get(ii).getIndex())));
			topicGrid.setWidget(ii + 1, 1, new Label(archiveTopicList.get(ii).getTopic()));
			topicGrid.setWidget(ii + 1, 2, new Label(datatypeString));
			topicGrid.setWidget(ii + 1, 3, new Label(Long.toString(archiveTopicList.get(ii).getWritePeriodMilli() / 1000)));
			topicGrid.setWidget(ii + 1, 4, new Label(new Date(archiveTopicList.get(ii).getTimeOfCreationMilli()).toString()));
			topicGrid.setWidget(ii + 1, 5, new Label(new Date(archiveTopicList.get(ii).getTimeOfLastWriteMilli()).toString()));
			topicGrid.setWidget(ii + 1, 6, new Label(new String(archiveTopicList.get(ii).getMessage())));
			if (archiveTopicList.get(ii).getIndex() > lastTopicIndex) lastTopicIndex = archiveTopicList.get(ii).getIndex();
		}
	}
	private void setupActionPanel()
	{
		Button addTopicButton = new Button("Add Topic");
		addTopicButton.addClickHandler(new TopicActionButtonClickHandler("Add"));
		Grid addTopicGrid = new Grid(3,2);
		addTopicGrid.setWidget(0, 0, new Label("Topic"));
		addTopicGrid.setWidget(1, 0, new Label("Type"));
		addTopicGrid.setWidget(2, 0, new Label("Period"));
		addTopicTextBox.setText("topic");
		addTopicGrid.setWidget(0, 1, addTopicTextBox);
		HorizontalPanel dataTypeChoicePanel = new HorizontalPanel();
	    RadioButton bytedataRadioButton = new RadioButton("dataTypeRadioGroup", "BYTEDATA");
	    jsondataRadioButton.setValue(true);
	    bytedataRadioButton.setValue(false);
	    dataTypeChoicePanel.add(jsondataRadioButton);
	    dataTypeChoicePanel.add(bytedataRadioButton);
		addTopicGrid.setWidget(1, 1, dataTypeChoicePanel);
		addTopicPeriodTextBox.setText("10");
		addTopicGrid.setWidget(2, 1, addTopicPeriodTextBox);
		VerticalPanel addTopicVerticalPanel = new VerticalPanel();
		addTopicVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		addTopicVerticalPanel.add(addTopicButton);
		addTopicVerticalPanel.add(addTopicGrid);
		addTopicCaptionPanel = new CaptionPanel("Add Topic");
		addTopicCaptionPanel.add(addTopicVerticalPanel);
		
		
		Button deleteTopicButton = new Button("Delete Topic");
		deleteTopicButton.addClickHandler(new TopicActionButtonClickHandler("Delete"));
		Grid deleteTopicGrid = new Grid(1,2);
		deleteTopicGrid.setWidget(0, 0, new Label("Index"));
		deleteTopicTextBox.setText("-1");
		deleteTopicGrid.setWidget(0, 1, deleteTopicTextBox);
		VerticalPanel deleteTopicVerticalPanel = new VerticalPanel();
		deleteTopicVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		deleteTopicVerticalPanel.add(deleteTopicButton);
		deleteTopicVerticalPanel.add(deleteTopicGrid);
		deleteTopicCaptionPanel = new CaptionPanel("Delete Topic");
		deleteTopicCaptionPanel.add(deleteTopicVerticalPanel);
	
		
		VerticalPanel vp1 = new VerticalPanel();
		vp1.add(addTopicCaptionPanel);
		vp1.add(deleteTopicCaptionPanel);
		actionCaptionPanel = new CaptionPanel("Actions");
		actionCaptionPanel.add(vp1);
		actionCaptionPanel.setVisible(settingsPermitted);
	}
	private void addTopic()
	{
		entryPointApp.setupApp.getStatusTextArea().addStatus("Adding topic: " + addTopicTextBox.getText().trim());
		try
		{
			actionCaptionPanel.setVisible(false);
			ArchiveTopic at = new ArchiveTopic();
			int datatype = ArchiveTopic.BYTEDATA;
			if (jsondataRadioButton.getValue()) datatype = ArchiveTopic.JSONDATA;
			at.setDataType(datatype);
			at.setIndex(lastTopicIndex + 1);
			at.setTimeOfCreationMilli(new Date().getTime());
			at.setTimeOfLastWriteMilli(0);
			at.setTopic(addTopicTextBox.getText().trim());
			long period = Long.parseLong(addTopicPeriodTextBox.getText());
			at.setWritePeriodMilli(period * 1000);
			entryPointApp.mqttService.addTopic(at, settingsPermitted, entryPointApp.setupApp.isDebug(), null, new GetTopicListCallback());
			
		} catch (NumberFormatException nfe)
		{
			entryPointApp.setupApp.getStatusTextArea().addStatus("Failure on adding topic: " + nfe.getMessage());
			actionCaptionPanel.setVisible(true);
		}
		
	}
	private void deleteTopic()
	{
		actionCaptionPanel.setVisible(false);
		entryPointApp.setupApp.getStatusTextArea().addStatus("Deleteing topic index: " + deleteTopicTextBox.getText().trim());
		try
		{
			int index = Integer.parseInt(deleteTopicTextBox.getText().trim());
			entryPointApp.mqttService.deleteTopic(index, settingsPermitted, entryPointApp.setupApp.isDebug(), null, new GetTopicListCallback());
		} catch (NumberFormatException nfe)
		{
			entryPointApp.setupApp.getStatusTextArea().addStatus("Failure on deleting topic: " + nfe.getMessage());
			actionCaptionPanel.setVisible(true);
		}
	}
	@Override
	public void tabLayoutPanelInterfaceAction(String message) {}

	@Override
	public void optionDialogInterfaceAction(String choiceButtonText) {}

	class TopicListTimer extends Timer
	{
		@Override
		public void run() 
		{
			entryPointApp.mqttService.getTopicList(entryPointApp.setupApp.isDebug(), null, new GetTopicListCallback());
		}
	}
	class GetTopicListCallback implements AsyncCallback<ArrayList<ArchiveTopic>>
	{

		@Override
		public void onFailure(Throwable caught) 
		{
			entryPointApp.setupApp.getStatusTextArea().addStatus("Failure: test Callback");
			entryPointApp.setupApp.getStatusTextArea().addStatus(caught.getMessage());
			actionCaptionPanel.setVisible(settingsPermitted);
		}

		@Override
		public void onSuccess(ArrayList<ArchiveTopic> result) 
		{
			actionCaptionPanel.setVisible(settingsPermitted);
			updateTopicGrid(result);
		}
	}
	class TopicActionButtonClickHandler implements ClickHandler
	{
		String action;
		TopicActionButtonClickHandler(String action)
		{
			this.action = action;
		}
		@Override
		public void onClick(ClickEvent event) 
		{
			if (action.equals("Add")) addTopic();
			if (action.equals("Delete")) deleteTopic();
			
		}
	}
}
