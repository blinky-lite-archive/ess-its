package se.esss.litterbox.its.archivergwt.client.contentpanels;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

import se.esss.litterbox.its.archivergwt.client.EntryPointApp;
import se.esss.litterbox.its.archivergwt.client.contentpanels.RegisterTopicsPanel.TopicUpdateInterface;
import se.esss.litterbox.its.archivergwt.client.googleplots.NonUniformTraceLengthScatterPlotPanel;
import se.esss.litterbox.its.archivergwt.client.googleplots.NonUniformTraceLengthScatterPlotPanel.ScatterPlotTraceData;
import se.esss.litterbox.its.archivergwt.client.gskel.GskelVerticalPanel;
import se.esss.litterbox.its.archivergwt.shared.ArchiveJsonData;
import se.esss.litterbox.its.archivergwt.shared.ArchiveTopic;

public class TopicPlotPanel extends GskelVerticalPanel implements TopicUpdateInterface
{
	private static final int maxNumTraces = 4;

	private boolean settingsPermitted = false;
	private EntryPointApp entryPointApp;
	private ArrayList<ArchiveTopic> archiveTopicList;
	private Grid topicGrid;
	private ListBox[] topicListBox = new ListBox[maxNumTraces];
	private ListBox[] deviceListBox = new ListBox[maxNumTraces];
	private ListBox[] traceMultiplierListBox = new ListBox[maxNumTraces];
	private TextBox startDayBox;
	private Date startDate;
	private TextBox startHourBox;
	private TextBox startMinBox;
	private TextBox stopDayBox;
	private Date stopDate;
	private TextBox stopHourBox;
	private TextBox stopMinBox;
	private CheckBox[] enabledCheckBox = new CheckBox[maxNumTraces];
	private Button plotButton  = new Button("Plot");
	private ArrayList<ArchiveJsonData> archiveJsonDataList;
	private CaptionPanel plotCaptionPanel;
	private ListBox pointSizeListBox;
	private CaptionPanel plotSetupCaptionPanel;
	private Anchor[] traceFileAnchor = new Anchor[maxNumTraces];
	
	DayPickerDialogBox dayPicker;
	
	public TopicPlotPanel(EntryPointApp entryPointApp, boolean settingsPermitted) 
	{
		super("Topic Plotter", "noTabStyle", entryPointApp.setupApp);
		this.getGskelTabLayoutScrollPanel().setStyleName("GskelVertPanel");
		this.settingsPermitted = settingsPermitted;
		this.entryPointApp = entryPointApp;
		
		topicGrid = new Grid(maxNumTraces + 1, 6);
		topicGrid.setWidget(0, 0, new Label("Trace"));
		topicGrid.setWidget(0, 1, new Label("Topic"));
		topicGrid.setWidget(0, 2, new Label("Device"));
		topicGrid.setWidget(0, 3, new Label("Mult."));
		topicGrid.setWidget(0, 4, new Label("Enabled"));
		topicGrid.setWidget(0, 5, new Label("CSV"));

		topicGrid.setBorderWidth(1);
		CellFormatter cf = topicGrid.getCellFormatter();

		for (int ii = 0; ii < maxNumTraces; ++ii)
		{
			topicGrid.setWidget(ii + 1, 0, new Label(Integer.toString(ii + 1)));
			topicListBox[ii] = new ListBox();
			topicListBox[ii].setWidth("15.0em");
			topicGrid.setWidget(ii + 1, 1, topicListBox[ii]);
			topicListBox[ii].addChangeHandler(new TopicSelectedHandler(ii));
			deviceListBox[ii] = new ListBox();
			deviceListBox[ii].setWidth("10.0em");
			topicGrid.setWidget(ii + 1, 2, deviceListBox[ii]);
			traceMultiplierListBox[ii] = new ListBox();
			traceMultiplierListBox[ii].addItem("0.001");
			traceMultiplierListBox[ii].addItem("0.01");
			traceMultiplierListBox[ii].addItem("0.1");
			traceMultiplierListBox[ii].addItem("1");
			traceMultiplierListBox[ii].addItem("10");
			traceMultiplierListBox[ii].addItem("100");
			traceMultiplierListBox[ii].addItem("1000");
			traceMultiplierListBox[ii].setWidth("5.0em");
			traceMultiplierListBox[ii].setItemSelected(3, true);
			topicGrid.setWidget(ii + 1, 3, traceMultiplierListBox[ii]);
			enabledCheckBox[ii] = new CheckBox();
			enabledCheckBox[ii].setValue(false);
			topicGrid.setWidget(ii + 1, 4, enabledCheckBox[ii]);
			traceFileAnchor[ii] = new Anchor("csv");
			traceFileAnchor[ii].addClickHandler(new DownLoadClickHandler("traceData/trace" + Integer.toString(ii + 1) + ".csv"));
			topicGrid.setWidget(ii + 1, 5, traceFileAnchor[ii]);
			traceFileAnchor[ii].setVisible(false);
			
		}
		for (int irow = 0; irow <= maxNumTraces; ++irow)
		{
			for (int icol = 0; icol < 5; ++icol)
			{
				cf.setHorizontalAlignment(irow, icol, HasHorizontalAlignment.ALIGN_CENTER);
				cf.setVerticalAlignment(irow, icol, HasVerticalAlignment.ALIGN_MIDDLE);	

			}
		}
		plotButton.addClickHandler(new PlotButtonClickHandler());
		dayPicker = new DayPickerDialogBox();
		dayPicker.hide();
		Grid timeGrid = new Grid(2, 7);
		timeGrid.setBorderWidth(1);
		timeGrid.setWidget(0, 0, new Label("Start Day"));
		timeGrid.setWidget(0, 1, new Label("Start Hour"));
		timeGrid.setWidget(0, 2, new Label("Start Min"));
		timeGrid.setWidget(0, 3, new Label("Stop Day"));
		timeGrid.setWidget(0, 4, new Label("Stop Hour"));
		timeGrid.setWidget(0, 5, new Label("Stop Min"));
		timeGrid.setWidget(0, 6, new Label("Point Size"));
		startDayBox = new TextBox();
		startDayBox.setWidth("6.0em");
		timeGrid.setWidget(1, 0, startDayBox);
		startDayBox.addClickHandler(new StartDayClickHandler());
		startHourBox = new TextBox();
		startHourBox.setText("0");
		startHourBox.setWidth("4.0em");
		timeGrid.setWidget(1, 1, startHourBox);
		startMinBox = new TextBox();
		startMinBox.setText("0");
		startMinBox.setWidth("4.0em");
		timeGrid.setWidget(1, 2, startMinBox);
		stopDayBox = new TextBox();
		stopDayBox.setWidth("6.0em");
		timeGrid.setWidget(1, 3, stopDayBox);
		stopDayBox.addClickHandler(new StopDayClickHandler());
		stopHourBox = new TextBox();
		stopHourBox.setText("0");
		stopHourBox.setWidth("4.0em");
		timeGrid.setWidget(1, 4, stopHourBox);
		stopMinBox = new TextBox();
		stopMinBox.setText("0");
		stopMinBox.setWidth("4.0em");
		timeGrid.setWidget(1, 5, stopMinBox);
		pointSizeListBox = new ListBox();
		for (int ii = 1; ii < 11; ++ii) pointSizeListBox.addItem(Integer.toString(ii));
		timeGrid.setWidget(1, 6, pointSizeListBox);
		CellFormatter plotButtonGridCellFormatter = timeGrid.getCellFormatter();
		for (int irow = 0; irow < 2; ++irow)
		{
			for (int icol = 0; icol < 7; ++icol)
			{
				plotButtonGridCellFormatter.setHorizontalAlignment(irow, icol, HasHorizontalAlignment.ALIGN_CENTER);
				plotButtonGridCellFormatter.setVerticalAlignment(irow, icol, HasVerticalAlignment.ALIGN_MIDDLE);	
			}
		}
		startDate = new Date(0);
		stopDate = new Date(1);
		
		VerticalPanel vp1 = new VerticalPanel();
		vp1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vp1.add(timeGrid);
		vp1.add(topicGrid);
		vp1.add(plotButton);
		plotSetupCaptionPanel = new CaptionPanel("Plot Setup");
		plotSetupCaptionPanel.add(vp1);
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.add(plotSetupCaptionPanel);
		plotCaptionPanel = new CaptionPanel("Archive Plot");
		plotCaptionPanel.setVisible(false);
		hp1.add(plotCaptionPanel);
		add(hp1);
		
	}
	@Override
	public void tabLayoutPanelInterfaceAction(String message) {}
	@Override
	public void optionDialogInterfaceAction(String choiceButtonText) {}
	@Override
	public void topicUpdateInterfaceAction(ArrayList<ArchiveTopic> archiveTopicList) 
	{
		this.archiveTopicList = archiveTopicList;
		for (int ii = 0; ii < maxNumTraces; ++ii)
		{
			topicListBox[ii].clear();
			topicListBox[ii].addItem("");
			for (int it = 0; it < archiveTopicList.size(); ++it)
			{
				if (archiveTopicList.get(it).getDataType() == ArchiveTopic.JSONDATA)
				{
					topicListBox[ii].addItem(archiveTopicList.get(it).getTopic());
				}
			}
		}
	}
	class TopicSelectedHandler implements ChangeHandler
	{
		int itrace;
		TopicSelectedHandler(int itrace)
		{
			this.itrace = itrace;
		}
		@Override
		public void onChange(ChangeEvent event) 
		{
//			entryPointApp.setupApp.getStatusTextArea().addStatus(topicListBox[itrace].getItemText(topicListBox[itrace].getSelectedIndex()));
			String topic = topicListBox[itrace].getItemText(topicListBox[itrace].getSelectedIndex());
			deviceListBox[itrace].clear();
			if (topic.length() > 0)
			{
				ArchiveTopic at = ArchiveTopic.getArchiveTopic(topic, archiveTopicList);
				if (at != null)
				{
					deviceListBox[itrace].addItem("");
					for (int ii = 0; ii < at.getJsonData().length; ++ii)
					{
						deviceListBox[itrace].addItem(at.getJsonData()[ii][0]);
					}
				}
			}
		}
	}
	class StartDayClickHandler implements ClickHandler
	{
		@Override
		public void onClick(ClickEvent event) 
		{
			dayPicker.setPopupPosition(startDayBox.getAbsoluteLeft(), startDayBox.getAbsoluteTop());
			dayPicker.getInstructLabel().setText("Pick Start Day");
			dayPicker.setDayTextBox(startDayBox);
			dayPicker.setPickedDate(startDate);
			dayPicker.show();
			
		}
	}
	class StopDayClickHandler implements ClickHandler
	{
		@Override
		public void onClick(ClickEvent event) 
		{
			dayPicker.setPopupPosition(stopDayBox.getAbsoluteLeft(), stopDayBox.getAbsoluteTop());
			dayPicker.getInstructLabel().setText("Pick Stop Day");
			dayPicker.setDayTextBox(stopDayBox);
			dayPicker.setPickedDate(stopDate);
			dayPicker.show();
			
		}
	}
	class PlotButtonClickHandler  implements ClickHandler
	{
		@Override
		public void onClick(ClickEvent event) 
		{
			archiveJsonDataList = new ArrayList<ArchiveJsonData>();
			long hour = Long.parseLong(startHourBox.getText());
			long min = Long.parseLong(startMinBox.getText());
			long startT = startDate.getTime() + (hour * 3600 * 1000) + (min * 60 * 1000);
			hour = Long.parseLong(stopHourBox.getText());
			min = Long.parseLong(stopMinBox.getText());
			long stopT = stopDate.getTime() + (hour * 3600 * 1000) + (min * 60 * 1000);
			if (stopT < startT)
			{
				entryPointApp.setupApp.getStatusTextArea().addStatus("Error on getting plot data: Stop time < Start time");
				return;
			}
			for (int irow = 0; irow < maxNumTraces; ++irow)
			{
				if (enabledCheckBox[irow].getValue())
				{
					if (topicListBox[irow].getSelectedItemText().length() > 1)
					{
						if (deviceListBox[irow].getSelectedItemText().length() > 1)
						{
							try
							{
								ArchiveJsonData ajd = new ArchiveJsonData();
								ajd.setTopic(topicListBox[irow].getSelectedItemText());
								ajd.setDeviceName(deviceListBox[irow].getSelectedItemText());
								ajd.setStartTime(startT);
								ajd.setStopTime(stopT);
								ajd.setMultiplier(Double.parseDouble(traceMultiplierListBox[irow].getSelectedItemText()));
								ajd.setTrace(irow + 1);
								archiveJsonDataList.add(ajd);
								
							}
							catch (Exception e)
							{
								entryPointApp.setupApp.getStatusTextArea().addStatus("Failure on getting plot data: " + e.getMessage());
								
							}
						}
					}
				}
			}
			if (archiveJsonDataList.size() > 0)
			{
				plotCaptionPanel.setVisible(false);
				plotSetupCaptionPanel.setVisible(false);
				entryPointApp.mqttService.getArchiveData(archiveJsonDataList, settingsPermitted, entryPointApp.setupApp.isDebug(), null, new GetArchiveDataCallback());
				for (int itrace = 0; itrace < maxNumTraces; ++ itrace) traceFileAnchor[itrace].setVisible(false);
			}
		}
		
	}
	class GetArchiveDataCallback implements AsyncCallback<ArrayList<ArchiveJsonData>>
	{

		@Override
		public void onFailure(Throwable caught) 
		{
			entryPointApp.setupApp.getStatusTextArea().addStatus("Failure: GetArchiveDataCallback");
			entryPointApp.setupApp.getStatusTextArea().addStatus(caught.getMessage());
			plotSetupCaptionPanel.setVisible(true);
		}
		@Override
		public void onSuccess(ArrayList<ArchiveJsonData> result) 
		{
			plotSetupCaptionPanel.setVisible(true);
//			int ilast = result.get(0).getDeviceData().length - 1;
//			entryPointApp.setupApp.getStatusTextArea().addStatus(Integer.toString(ilast) + " " + Double.toString(result.get(0).getTimeData()[ilast])  + " " + Double.toString(result.get(0).getDeviceData()[ilast]));
			try
			{
				plotCaptionPanel.setVisible(false);
				plotCaptionPanel.clear();

				String title = "Data from " + DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(new Date(result.get(0).getStartTime())) + " to " + DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(new Date(result.get(0).getStopTime()));
				ScatterPlotTraceData[] scatterPlotTraceDataArray = new ScatterPlotTraceData[result.size()];
				String[] legend = new String[result.size()];
				for (int ii = 0; ii < result.size(); ++ii)
				{
					scatterPlotTraceDataArray[ii] = new ScatterPlotTraceData(result.get(ii).getTimeData(), result.get(ii).getMultipliedDeviceData());
					legend[ii] = result.get(ii).getDeviceName();
					if (result.get(ii).getMultiplier() != 1.0 ) legend[ii] = legend[ii] + " x " + Double.toString(result.get(ii).getMultiplier());
					traceFileAnchor[result.get(ii).getTrace() - 1].setVisible(true);
				}
				int pointSize = Integer.parseInt(pointSizeListBox.getSelectedItemText());
				NonUniformTraceLengthScatterPlotPanel plot = new NonUniformTraceLengthScatterPlotPanel(scatterPlotTraceDataArray, title, "Time (Hours)", "Value", legend, "1200px", "800px", pointSize);
				plot.initialize();
				plotCaptionPanel.add(plot);
				plotCaptionPanel.setVisible(true);
			}
			catch (Exception e)
			{
				entryPointApp.setupApp.getStatusTextArea().addStatus("Failure: Setting up plot Panel " + e.getMessage());
			}
		}
		
	}
	class DownLoadClickHandler implements ClickHandler
	{
		String link;
		DownLoadClickHandler(String link)
		{
			this.link = link;
		}
		@Override
		public void onClick(ClickEvent event) {
			
//			Window.open(link, "_blank", "enabled");
			Window.open(link, "_blank", "");
		}
		
	}

}
