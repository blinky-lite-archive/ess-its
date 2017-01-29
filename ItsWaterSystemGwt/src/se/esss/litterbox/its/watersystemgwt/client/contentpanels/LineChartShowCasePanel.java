package se.esss.litterbox.its.watersystemgwt.client.contentpanels;


import se.esss.litterbox.its.watersystemgwt.client.EntryPointApp;
import se.esss.litterbox.its.watersystemgwt.client.googleplots.LineChartCaptionPanel;
import se.esss.litterbox.its.watersystemgwt.client.gskel.GskelLoadWaiter;
import se.esss.litterbox.its.watersystemgwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.its.watersystemgwt.client.gskel.GskelVerticalPanel;
import se.esss.litterbox.its.watersystemgwt.client.mqttdata.MqttData;

public class LineChartShowCasePanel extends GskelVerticalPanel
{
	String lineChartMqttTopic;
	LineChartCaptionPanel lineChartCaptionPanel;
	LineChartMqttData lineChartMqttData;

	public LineChartShowCasePanel(String tabTitle, String lineChartMqttTopic, GskelSetupApp setupApp) 
	{
		super(tabTitle, "gwtTab", setupApp);
		this.getGskelTabLayoutScrollPanel().setStyleName("GskelVertPanel");
		this.lineChartMqttTopic = lineChartMqttTopic;
		addLineChart();
	}
	private void addLineChart()
	{
		getStatusTextArea().addStatus("Adding LineChart");
		int numPts = 5;
		String title = "LineChart";
		String xaxisLabel = "Time (fortnights)";
		String yaxisLabel  = "Mass (slugs)";
		String[] lineChartLegend  = {"Thingy 1", "Thingy 2"};
		lineChartCaptionPanel = new LineChartCaptionPanel(numPts, title, xaxisLabel, yaxisLabel, lineChartLegend, "600px", "400px");
		lineChartCaptionPanel.setWidth("600px");
		new LineChartPlotWaiter(100, 1);
	}
	private void startMqtt()
	{
		getStatusTextArea().addStatus("Starting LineChart Mqtt");
		add(lineChartCaptionPanel);
		lineChartMqttData = new LineChartMqttData(getEntryPointApp());
	}
	@Override
	public void tabLayoutPanelInterfaceAction(String message) {}
	@Override
	public void optionDialogInterfaceAction(String choiceButtonText) {}

	class LineChartPlotWaiter extends GskelLoadWaiter
	{
		public LineChartPlotWaiter(int loopTimeMillis, int itask) {super(loopTimeMillis, itask);}
		@Override
		public boolean isLoaded() 
		{
			boolean loaded = false;
			if (getItask() == 1) loaded = lineChartCaptionPanel.isLoaded();
			return loaded;
		}
		@Override
		public void taskAfterLoad() 
		{
			if (getItask() == 1) startMqtt();
		}
		
	}
	class LineChartMqttData extends MqttData
	{

		public LineChartMqttData(EntryPointApp entryPointApp) 
		{
			super(lineChartMqttTopic, MqttData.BYTEDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try
			{
				String messageString = new String(getMessage());
				String[] dataString = messageString.split(",");
				double[] xaxisData = new double[5];
				double[][] yaxisData = new double[2][5];
				for (int ii = 0; ii < 5; ++ii)
				{
					xaxisData[ii] = Double.parseDouble(dataString[ii]);
					yaxisData[0][ii] = Double.parseDouble(dataString[ii + 5]);
					yaxisData[1][ii] = Double.parseDouble(dataString[ii + 10]);
				}
				lineChartCaptionPanel.updateReadings(xaxisData, yaxisData);
				
			}
			catch (Exception e)
			{
				getStatusTextArea().addStatus(e.getMessage());
			}
			
		}
	}
}
