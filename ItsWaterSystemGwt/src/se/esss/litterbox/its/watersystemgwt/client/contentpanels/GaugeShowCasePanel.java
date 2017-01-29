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
	GaugeCaptionPanel bodyFlowGaugeCaptionPanel;
	GaugeCaptionPanel tankFlowgaugeCaptionPanel;
	GaugeCaptionPanel collectorFlowGaugeCaptionPanel;
	GaugeCaptionPanel solenoidFlowGaugeCaptionPanel;
	GaugeCaptionPanel inputTempGaugeCaptionPanel;
	GaugeMqttData gaugeMqttData;


	public GaugeShowCasePanel(String tabTitle, String gaugeMqttTopic, GskelSetupApp setupApp) 
	{
		super(tabTitle, "gwtTab", setupApp);
		this.getGskelTabLayoutScrollPanel().setStyleName("GskelVertPanel");
		this.gaugeMqttTopic = gaugeMqttTopic;
		gaugeGrid = new Grid(1, 5);
		add(gaugeGrid);
		addGauge1();
	}
	private void addGauge1()
	{
		getStatusTextArea().addStatus("Adding Body Flow Gauge");
		bodyFlowGaugeCaptionPanel = new GaugeCaptionPanel("Body Flow", 0, 50, 30.0, 50.0, 20.0, 30.0, 0.0, 20.0, "100px", "100px");
		new GaugePlotWaiter(100, 1);
	}
	private void addGauge2()
	{
		getStatusTextArea().addStatus("Adding Tank Flow Gauge");
		gaugeGrid.setWidget(0, 0, bodyFlowGaugeCaptionPanel);
		tankFlowgaugeCaptionPanel = new GaugeCaptionPanel("Tank Flow", 0, 15, 11.0, 15.0, 9.0, 11.0, 0.0, 9.0, "100px", "100px");
		new GaugePlotWaiter(100, 2);
	}
	private void addGauge3()
	{
		getStatusTextArea().addStatus("Adding Collector Flow Gauge");
		gaugeGrid.setWidget(0, 1, tankFlowgaugeCaptionPanel);
		collectorFlowGaugeCaptionPanel = new GaugeCaptionPanel("Collector Flow", 0, 375, 300.0, 375.0, 250.0, 300.0, 0.0, 250.0, "100px", "100px");
		new GaugePlotWaiter(100, 3);
	}
	private void addGauge4()
	{
		getStatusTextArea().addStatus("Adding Solenoid Flow Gauge");
		gaugeGrid.setWidget(0, 2, collectorFlowGaugeCaptionPanel);
		solenoidFlowGaugeCaptionPanel = new GaugeCaptionPanel("Solenoid Flow", 0, 50, 30.0, 50.0, 20.0, 30.0, 0.0, 20.0, "100px", "100px");
		new GaugePlotWaiter(100, 4);
	}
	private void addGauge5()
	{
		getStatusTextArea().addStatus("Adding Input Temp Gauge");
		gaugeGrid.setWidget(0, 3, solenoidFlowGaugeCaptionPanel);
		inputTempGaugeCaptionPanel = new GaugeCaptionPanel("Input Temp", 10.0, 40.0, 10.0, 25.0, 25.0, 35.0, 35.0, 40.0, "100px", "100px");
		new GaugePlotWaiter(100, 5);
	}
	private void startMqtt()
	{
		getStatusTextArea().addStatus("Starting Gauge Mqtt");
		gaugeGrid.setWidget(0, 4, inputTempGaugeCaptionPanel);
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
			if (getItask() == 1) loaded = bodyFlowGaugeCaptionPanel.isLoaded();
			if (getItask() == 2) loaded = tankFlowgaugeCaptionPanel.isLoaded();
			if (getItask() == 3) loaded = collectorFlowGaugeCaptionPanel.isLoaded();
			if (getItask() == 4) loaded = collectorFlowGaugeCaptionPanel.isLoaded();
			if (getItask() == 5) loaded = collectorFlowGaugeCaptionPanel.isLoaded();
			return loaded;
		}
		@Override
		public void taskAfterLoad() 
		{
			if (getItask() == 1) addGauge2();
			if (getItask() == 2) addGauge3();
			if (getItask() == 3) addGauge4();
			if (getItask() == 4) addGauge5();
			if (getItask() == 5) startMqtt();
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
				bodyFlowGaugeCaptionPanel.updateReadings(Double.parseDouble(getJsonValue("body")));
				tankFlowgaugeCaptionPanel.updateReadings(Double.parseDouble(getJsonValue("tank")));
				collectorFlowGaugeCaptionPanel.updateReadings(Double.parseDouble(getJsonValue("collector")));
				solenoidFlowGaugeCaptionPanel.updateReadings(Double.parseDouble(getJsonValue("solenoid")));
				inputTempGaugeCaptionPanel.updateReadings(Double.parseDouble(getJsonValue("inputTemp")));
			}
			catch (Exception e)
			{
				getStatusTextArea().addStatus(e.getMessage());
			}
			
		}
	}

}
