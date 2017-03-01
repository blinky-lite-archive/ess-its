package se.esss.litterbox.its.dashboardgwt.client.contentpanels;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.VerticalPanel;

import se.esss.litterbox.its.dashboardgwt.client.EntryPointApp;
import se.esss.litterbox.its.dashboardgwt.client.googleplots.GaugeCaptionPanel;
import se.esss.litterbox.its.dashboardgwt.client.gskel.GskelLoadWaiter;
import se.esss.litterbox.its.dashboardgwt.client.mqttdata.MqttData;

public class GaugeDashboard extends VerticalPanel
{
	EntryPointApp entryPointApp;
	String gaugeMqttTopic;
	String modPressTopic;
	String returnPressTopic;
	Grid gaugeGrid;
	GaugeCaptionPanel bodyFlowGaugeCaptionPanel;
	GaugeCaptionPanel tankFlowgaugeCaptionPanel;
	GaugeCaptionPanel collectorFlowGaugeCaptionPanel;
	GaugeCaptionPanel solenoidFlowGaugeCaptionPanel;
	GaugeCaptionPanel inputTempGaugeCaptionPanel;
	GaugeCaptionPanel modPressGaugeCaptionPanel;
	GaugeCaptionPanel returnPressGaugeCaptionPanel;
	GaugeCaptionPanel mod1TempGaugeCaptionPanel;
	GaugeCaptionPanel mod2TempGaugeCaptionPanel;
	GaugeMqttData gaugeMqttData;
	ModPressMqttData modPressMqttData;
	ReturnPressMqttData returnPressMqttData;
	
	public GaugeDashboard(String gaugeMqttTopic, String modPressTopic, String returnPressTopic, EntryPointApp entryPointApp) 
	{
		this.gaugeMqttTopic = gaugeMqttTopic;
		this.modPressTopic = modPressTopic;
		this.returnPressTopic = returnPressTopic;
		this.entryPointApp = entryPointApp;
		setStyleName("GskelVertPanel");
		gaugeGrid = new Grid(3, 3);
		add(gaugeGrid);
		addGauge1();
	}
	private void addGauge1()
	{
		bodyFlowGaugeCaptionPanel = new GaugeCaptionPanel("Body Flow", "Body Flow", 0, 30, 18.0, 30.0, 13.0, 18.0, 0.0, 13.0, "100px", "100px");
		new GaugePlotWaiter(200, 1);
	}
	private void addGauge2()
	{
		gaugeGrid.setWidget(0, 0, bodyFlowGaugeCaptionPanel);
		tankFlowgaugeCaptionPanel = new GaugeCaptionPanel("Tank Flow", "Tank Flow", 0, 15, 11.0, 15.0, 9.0, 11.0, 0.0, 9.0, "100px", "100px");
		new GaugePlotWaiter(200, 2);
	}
	private void addGauge3()
	{
		gaugeGrid.setWidget(0, 1, tankFlowgaugeCaptionPanel);
		collectorFlowGaugeCaptionPanel = new GaugeCaptionPanel("Collector Flow", "Collector Flow", 0, 375, 300.0, 375.0, 250.0, 300.0, 0.0, 250.0, "100px", "100px");
		new GaugePlotWaiter(200, 3);
	}
	private void addGauge4()
	{
		gaugeGrid.setWidget(0, 2, collectorFlowGaugeCaptionPanel);
		solenoidFlowGaugeCaptionPanel = new GaugeCaptionPanel("Solenoid Flow", "Solenoid Flow", 0, 50, 30.0, 50.0, 25.0, 30.0, 0.0, 25.0, "100px", "100px");
		new GaugePlotWaiter(200, 4);
	}
	private void addGauge5()
	{
		gaugeGrid.setWidget(1, 0, solenoidFlowGaugeCaptionPanel);
		modPressGaugeCaptionPanel = new GaugeCaptionPanel("Mod Press.", "Mod Press.", 0.0, 8.0, 4.0, 6.0, 0.0, 4.0, 6.0, 8.0, "100px", "100px");
		new GaugePlotWaiter(200, 5);
	}
	private void addGauge6()
	{
		gaugeGrid.setWidget(1, 1, modPressGaugeCaptionPanel);
		returnPressGaugeCaptionPanel = new GaugeCaptionPanel("Rtn Press.", "Rtn Press.", 0.0, 3.0, 0.5, 1.5, 0.0, 0.5, 1.5, 3.0, "100px", "100px");
		new GaugePlotWaiter(200, 6);
	}
	private void addGauge7()
	{
		gaugeGrid.setWidget(1, 2, returnPressGaugeCaptionPanel);
		inputTempGaugeCaptionPanel = new GaugeCaptionPanel("Input Temp", "Input Temp", 10.0, 40.0, 10.0, 25.0, 25.0, 35.0, 35.0, 40.0, "100px", "100px");
		new GaugePlotWaiter(200, 7);
	}
	private void addGauge8()
	{
		gaugeGrid.setWidget(2, 0, inputTempGaugeCaptionPanel);
		mod1TempGaugeCaptionPanel = new GaugeCaptionPanel("Mod1 Temp.", "Mod1 Temp.", 10.0, 60.0, 10.0, 30.0, 30.0, 45.0, 45.0, 60.0, "100px", "100px");
		new GaugePlotWaiter(200, 8);
	}
	private void addGauge9()
	{
		gaugeGrid.setWidget(2, 1, mod1TempGaugeCaptionPanel);
		mod2TempGaugeCaptionPanel = new GaugeCaptionPanel("Mod2 Temp.", "Mod2 Temp.", 10.0, 60.0, 10.0, 30.0, 30.0, 45.0, 45.0, 60.0, "100px", "100px");
		new GaugePlotWaiter(200, 9);
	}
	private void startMqtt()
	{
		gaugeGrid.setWidget(2, 2, mod2TempGaugeCaptionPanel);
		gaugeMqttData = new GaugeMqttData(entryPointApp);
		modPressMqttData = new ModPressMqttData(entryPointApp);
		returnPressMqttData = new ReturnPressMqttData(entryPointApp);
	}
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
			if (getItask() == 4) loaded = solenoidFlowGaugeCaptionPanel.isLoaded();
			if (getItask() == 5) loaded = modPressGaugeCaptionPanel.isLoaded();
			if (getItask() == 6) loaded = returnPressGaugeCaptionPanel.isLoaded();
			if (getItask() == 7) loaded = inputTempGaugeCaptionPanel.isLoaded();
			if (getItask() == 8) loaded = mod1TempGaugeCaptionPanel.isLoaded();
			if (getItask() == 9) loaded = mod2TempGaugeCaptionPanel.isLoaded();
			return loaded;
		}
		@Override
		public void taskAfterLoad() 
		{
			if (getItask() == 1) addGauge2();
			if (getItask() == 2) addGauge3();
			if (getItask() == 3) addGauge4();
			if (getItask() == 4) addGauge5();
			if (getItask() == 5) addGauge6();
			if (getItask() == 6) addGauge7();
			if (getItask() == 7) addGauge8();
			if (getItask() == 8) addGauge9();
			if (getItask() == 9) startMqtt();
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
			}
			
		}
	}
	class ModPressMqttData extends MqttData
	{

		public ModPressMqttData(EntryPointApp entryPointApp) 
		{
			super(modPressTopic, MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try
			{
				modPressGaugeCaptionPanel.updateReadings(Double.parseDouble(getJsonValue("wtrp")));
				mod1TempGaugeCaptionPanel.updateReadings(Double.parseDouble(getJsonValue("tmod1")));
				mod2TempGaugeCaptionPanel.updateReadings(Double.parseDouble(getJsonValue("tmod2")));
			}
			catch (Exception e)
			{
			}
			
		}
	}
	class ReturnPressMqttData extends MqttData
	{

		public ReturnPressMqttData(EntryPointApp entryPointApp) 
		{
			super(returnPressTopic, MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try
			{
				returnPressGaugeCaptionPanel.updateReadings(Double.parseDouble(getJsonValue("pressure")));
			}
			catch (Exception e)
			{
			}
			
		}
	}

}
