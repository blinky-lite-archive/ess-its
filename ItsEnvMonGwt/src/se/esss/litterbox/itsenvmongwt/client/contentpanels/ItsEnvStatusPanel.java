package se.esss.litterbox.itsenvmongwt.client.contentpanels;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import se.esss.litterbox.itsenvmongwt.client.MqttServiceAsync;
import se.esss.litterbox.itsenvmongwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.itsenvmongwt.client.gskel.GskelVerticalPanel;


public class ItsEnvStatusPanel extends GskelVerticalPanel
{
	private MqttServiceAsync mqttService;
	private String styleName = "ItsEnvStatusPanel";
	private Label[][] readings = new Label[6][2];
	ItsEnvMonGauge[] itsEnvMonGauge = new ItsEnvMonGauge[7];
	HorizontalPanel gaugePanel;
	int iguageLoad = 0;
	double timeSecs = 0.0;
	

	public ItsEnvStatusPanel(String tabTitle, GskelSetupApp setupApp, MqttServiceAsync mqttService) 
	{
		super(tabTitle, tabTitle, setupApp);
		this.mqttService = mqttService;
		this.getGskelTabLayoutScrollPanel().setStyleName(styleName);
		for (int ii = 0; ii < readings.length; ++ii)
		{
			readings[ii][0] = new Label("Name ");
			readings[ii][1] = new Label("Value");
		}
		setupGauges();
		add(readingsCaptionPanel());
		gaugePanel = new HorizontalPanel();
		add(gaugePanel);
		
		LoadGaugesTimer lgt = new LoadGaugesTimer(this);
		iguageLoad = 0;
		lgt.scheduleRepeating(1000);
		
	}
	public CaptionPanel readingsCaptionPanel()
	{
		Grid readGrid = new Grid(7, 2);
		HTMLTable.CellFormatter formatter = readGrid.getCellFormatter();
		formatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);	
		formatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);	
		for (int ii = 0; ii < readings.length; ++ii)
		{
			formatter.setHorizontalAlignment(ii + 1, 0, HasHorizontalAlignment.ALIGN_LEFT);
			formatter.setVerticalAlignment(ii + 1, 0, HasVerticalAlignment.ALIGN_MIDDLE);
			formatter.setHorizontalAlignment(ii + 1, 1, HasHorizontalAlignment.ALIGN_RIGHT);
			formatter.setVerticalAlignment(ii + 1, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		}
		readGrid.setWidget(0, 0, new Label("Name "));
		readGrid.setWidget(0, 1, new Label("Value"));
		String labelWidth = "6.0em";
		for (int ii = 0; ii < readings.length; ++ii)
		{
			readings[ii][0].setWidth(labelWidth);
			readings[ii][1].setWidth(labelWidth);
			readings[ii][1].setStyleName("readings");
			readGrid.setWidget(ii + 1, 0, readings[ii][0]);
			readGrid.setWidget(ii + 1, 1, readings[ii][1]);
		}
		CaptionPanel readingsCaptionPanel = new CaptionPanel("Readings");
		readingsCaptionPanel.add(readGrid);
		readingsCaptionPanel.setWidth("14.0em");
		return readingsCaptionPanel;
	}
	private void setupGauges()
	{
		itsEnvMonGauge[0] = new ItsEnvMonGauge();
		itsEnvMonGauge[0].setTitle("cpm");
		itsEnvMonGauge[0].setMinMax(5, 30);
		itsEnvMonGauge[0].setYellowMinMax(20,25);
		itsEnvMonGauge[0].setGreenMinMax(5,20);
		itsEnvMonGauge[0].setRedMinMax(25,30);
		
		itsEnvMonGauge[1] = new ItsEnvMonGauge();
		itsEnvMonGauge[1].setTitle("tempDht11");
		itsEnvMonGauge[1].setMinMax(5, 30);
		itsEnvMonGauge[1].setYellowMinMax(5,14);
		itsEnvMonGauge[1].setGreenMinMax(14,23);
		itsEnvMonGauge[1].setRedMinMax(23,30);
		
		itsEnvMonGauge[2] = new ItsEnvMonGauge();
		itsEnvMonGauge[2].setTitle("humidDh11");
		itsEnvMonGauge[2].setMinMax(10, 90);
		itsEnvMonGauge[2].setYellowMinMax(10,20);
		itsEnvMonGauge[2].setGreenMinMax(20,50);
		itsEnvMonGauge[2].setRedMinMax(50,100);
		
		itsEnvMonGauge[3] = new ItsEnvMonGauge();
		itsEnvMonGauge[3].setTitle("temp");
		itsEnvMonGauge[3].setMinMax(5, 30);
		itsEnvMonGauge[3].setYellowMinMax(5,14);
		itsEnvMonGauge[3].setGreenMinMax(14,23);
		itsEnvMonGauge[3].setRedMinMax(23,30);
		
		itsEnvMonGauge[4] = new ItsEnvMonGauge();
		itsEnvMonGauge[4].setTitle("photoAvg");
		itsEnvMonGauge[4].setMinMax(0, 1000);
		itsEnvMonGauge[4].setYellowMinMax(0,300);
		itsEnvMonGauge[4].setGreenMinMax(300,650);
		itsEnvMonGauge[4].setRedMinMax(650,1000);
		
		itsEnvMonGauge[5] = new ItsEnvMonGauge();
		itsEnvMonGauge[5].setTitle("photo");
		itsEnvMonGauge[5].setMinMax(0, 1000);
		itsEnvMonGauge[5].setYellowMinMax(0,300);
		itsEnvMonGauge[5].setGreenMinMax(300,650);
		itsEnvMonGauge[5].setRedMinMax(650,1000);

		itsEnvMonGauge[6] = new ItsEnvMonGauge();
		itsEnvMonGauge[6].setTitle("time");
		itsEnvMonGauge[6].setMinMax(0, 60);
		itsEnvMonGauge[6].setYellowMinMax(58,59);
		itsEnvMonGauge[6].setGreenMinMax(0,58);
		itsEnvMonGauge[6].setRedMinMax(59,60);
	}
	private void updateReadings(String[][] readingfromServer)
	{
		for (int ii = 0; ii < readingfromServer.length; ++ii)
		{
			readings[ii][0].setText(readingfromServer[ii][0]);
			readings[ii][1].setText(readingfromServer[ii][1]);
			itsEnvMonGauge[ii].setValue(Double.parseDouble(readingfromServer[ii][1]));
			itsEnvMonGauge[ii].draw();
		}
		itsEnvMonGauge[6].setValue(timeSecs);
		itsEnvMonGauge[6].draw();
	}
	@Override
	public void tabLayoutPanelInterfaceAction(String message) {}
	@Override
	public void optionDialogInterfaceAction(String choiceButtonText) {}
	public static class FetchReadingsTimer extends Timer
	{
		ItsEnvStatusPanel itsEnvStatusPanel;
		public FetchReadingsTimer(ItsEnvStatusPanel itsEnvStatusPanel) {this.itsEnvStatusPanel = itsEnvStatusPanel;}
		@Override
		public void run() 
		{
			String[][] debugResponse = {{"key1", "val1"}, {"key2", "val2"}};
			itsEnvStatusPanel.mqttService.nameValuePairArray(itsEnvStatusPanel.isDebug(), debugResponse, new MqttServiceNameValuePairArrayAsyncCallback(itsEnvStatusPanel));
		}
	}
	public static class LoadGaugesTimer extends Timer
	{
		ItsEnvStatusPanel itsEnvStatusPanel;
		public LoadGaugesTimer(ItsEnvStatusPanel itsEnvStatusPanel){this.itsEnvStatusPanel = itsEnvStatusPanel;}
		@Override
		public void run() 
		{
			if (itsEnvStatusPanel.iguageLoad < 7)
			{
				itsEnvStatusPanel.itsEnvMonGauge[itsEnvStatusPanel.iguageLoad].initialize();
				itsEnvStatusPanel.gaugePanel.add(itsEnvStatusPanel.itsEnvMonGauge[itsEnvStatusPanel.iguageLoad]);
				++itsEnvStatusPanel.iguageLoad;
			}
			else
			{
				FetchReadingsTimer frt = new FetchReadingsTimer(itsEnvStatusPanel);
				frt.scheduleRepeating(1000);
				this.cancel();
			}

		}
	}

	public static class MqttServiceNameValuePairArrayAsyncCallback implements AsyncCallback<String[][]>
	{
		ItsEnvStatusPanel itsEnvStatusPanel;
		MqttServiceNameValuePairArrayAsyncCallback(ItsEnvStatusPanel itsEnvStatusPanel)
		{
			this.itsEnvStatusPanel = itsEnvStatusPanel;
		}
		@Override
		public void onFailure(Throwable caught) 
		{
			itsEnvStatusPanel.getMessageDialog().setMessage("Error on Call back", caught.getMessage(), true);
			
		}
		@Override
		public void onSuccess(String[][] result) 
		{
			itsEnvStatusPanel.updateReadings(result);
			itsEnvStatusPanel.timeSecs = itsEnvStatusPanel.timeSecs + 1.0;
			if (itsEnvStatusPanel.timeSecs > 60.0) itsEnvStatusPanel.timeSecs = 1.0;
//			for (int ii = 0; ii < result.length; ++ii) 
//				itsEnvStatusPanel.getStatusTextArea().addStatus(result[ii][0] + " " + result[ii][1]);
		}
	}
}
