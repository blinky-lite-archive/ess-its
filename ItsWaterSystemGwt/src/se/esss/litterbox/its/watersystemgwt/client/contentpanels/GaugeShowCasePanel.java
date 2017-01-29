package se.esss.litterbox.its.watersystemgwt.client.contentpanels;

import com.google.gwt.user.client.ui.Grid;

import se.esss.litterbox.its.watersystemgwt.client.EntryPointApp;
import se.esss.litterbox.its.watersystemgwt.client.googleplots.GaugeCaptionPanel;
import se.esss.litterbox.its.watersystemgwt.client.gskel.GskelLoadWaiter;
import se.esss.litterbox.its.watersystemgwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.its.watersystemgwt.client.gskel.GskelVerticalPanel;
import se.esss.litterbox.its.watersystemgwt.client.mqttdata.MqttData;

public class GaugeShowCasePanel extends GskelVerticalPanel
{
	String gaugeMqttTopic;
	Grid gaugeGrid;
	GaugeCaptionPanel gaugeCaptionPanel1;
	GaugeCaptionPanel gaugeCaptionPanel2;
	GaugeCaptionPanel gaugeCaptionPanel3;
	GaugeMqttData gaugeMqttData;


	public GaugeShowCasePanel(String tabTitle, String gaugeMqttTopic, GskelSetupApp setupApp) 
	{
		super(tabTitle, "gwtTab", setupApp);
		this.getGskelTabLayoutScrollPanel().setStyleName("GskelVertPanel");
		this.gaugeMqttTopic = gaugeMqttTopic;
		gaugeGrid = new Grid(1, 3);
		add(gaugeGrid);
		addGauge1();
	}
	private void addGauge1()
	{
		getStatusTextArea().addStatus("Adding Gauge1");
		gaugeCaptionPanel1 = new GaugeCaptionPanel("testDevice01", 0.0, 30.0, 30.0, 60.0, 60.0, 100.0, "100px", "100px");
		new GaugePlotWaiter(100, 1);
	}
	private void addGauge2()
	{
		getStatusTextArea().addStatus("Adding Gauge2");
		gaugeGrid.setWidget(0, 0, gaugeCaptionPanel1);
		gaugeCaptionPanel2 = new GaugeCaptionPanel("testDevice02", 0.0, 30.0, 30.0, 60.0, 60.0, 100.0, "100px", "100px");
		new GaugePlotWaiter(100, 2);
	}
	private void addGauge3()
	{
		getStatusTextArea().addStatus("Adding Gauge3");
		gaugeGrid.setWidget(0, 1, gaugeCaptionPanel2);
		gaugeCaptionPanel3 = new GaugeCaptionPanel("testDevice03", 0.0, 30.0, 30.0, 60.0, 60.0, 100.0, "100px", "100px");
		new GaugePlotWaiter(100, 3);
	}
	private void startMqtt()
	{
		getStatusTextArea().addStatus("Starting Gauge Mqtt");
		gaugeGrid.setWidget(0, 2, gaugeCaptionPanel3);
		gaugeMqttData = new GaugeMqttData(getEntryPointApp());
	}
	@Override
	public void tabLayoutPanelInterfaceAction(String message) {}
	@Override
	public void optionDialogInterfaceAction(String choiceButtonText) {}
	class GaugePlotWaiter extends GskelLoadWaiter
	{
		public GaugePlotWaiter(int loopTimeMillis, int itask) {super(loopTimeMillis, itask);}
		@Override
		public boolean isLoaded() 
		{
			boolean loaded = false;
			if (getItask() == 1) loaded = gaugeCaptionPanel1.isLoaded();
			if (getItask() == 2) loaded = gaugeCaptionPanel2.isLoaded();
			if (getItask() == 3) loaded = gaugeCaptionPanel3.isLoaded();
			return loaded;
		}
		@Override
		public void taskAfterLoad() 
		{
			if (getItask() == 1) addGauge2();
			if (getItask() == 2) addGauge3();
			if (getItask() == 3) startMqtt();
		}
		
	}
	class GaugeMqttData extends MqttData
	{

		public GaugeMqttData(EntryPointApp entryPointApp) 
		{
			super(gaugeMqttTopic, MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try
			{
				gaugeCaptionPanel1.updateReadings(Double.parseDouble(getJsonValue("testDevice01")));
				gaugeCaptionPanel2.updateReadings(Double.parseDouble(getJsonValue("testDevice02")));
				gaugeCaptionPanel3.updateReadings(Double.parseDouble(getJsonValue("testDevice03")));
			}
			catch (Exception e)
			{
				getStatusTextArea().addStatus(e.getMessage());
			}
			
		}
	}

}
