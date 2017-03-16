package se.esss.litterbox.its.dashboardgwt.client.contentpanels;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import se.esss.litterbox.its.dashboardgwt.client.EntryPointApp;
import se.esss.litterbox.its.dashboardgwt.client.googleplots.ThermCaptionPanel;
import se.esss.litterbox.its.dashboardgwt.client.gskel.GskelLoadWaiter;
import se.esss.litterbox.its.dashboardgwt.client.mqttdata.MqttData;

public class GaugeDashboard extends VerticalPanel
{
	EntryPointApp entryPointApp;
	ThermCaptionPanel[] statPanel = new ThermCaptionPanel[21];
	private int plotLoadTimeMs = 100;
	private String red = "#ff0000";
	private String yellow = "#ffff00";
	private String green = "#00ff00";
	private String plotWidth = "95px";
	private String plotHeight = "170px";
	private int fontPixelSize = 15;
	private int chartAreaPixelLeftOffset = 55;
	private int chartAreaPixelWidth = 20;
	private double chartAreaOpacity = 0.5;
	private String chartAreaBackgroundColor = "#aaaaaa";
	private String plotBackgroundColor = "#0095CD";
	private String gridlineColor = "#000000";
	int rfSystemOffset = 7;
	int modOffset = 14;
	int waterOffset = 0;
	Date startTime = new Date();
	
	public GaugeDashboard(EntryPointApp entryPointApp) 
	{
		this.entryPointApp = entryPointApp;
		setStyleName("GskelVertPanel");
		
		CaptionPanel rfStatusCaptionPanel = new CaptionPanel("RF System Status");
		HorizontalPanel rfStatusHorizontalPanel = new HorizontalPanel();
		rfStatusCaptionPanel.add(rfStatusHorizontalPanel);
		add(rfStatusCaptionPanel);
		
		String[] rfSysPlotTitle = {"RF Power", "CPM", "Cath V", "Trig W", "Trig. Duty", "Trig Freq", "State"};
		double[] rfSysMinValue = {0.0, 0.0, 0.0, 500, 0.0, 0.0, 0.0 };
		double[][] rfSysMaxValue = {
				{400,   800, 1200},
				{ 25,    40,   70},
				{ 70,   115,  140},
				{1000, 2000, 3000},
				{ 1.0,  4.0,  6.0},
				{  5,    15,   20},
				{  1,     3,    4}};
		String[][] rfSysColor = { 
				{yellow, green, red},
				{green, yellow, red},
				{yellow, green, green},
				{green, green, green},
				{green, green, green},
				{green, green, green},
				{green, green, green}};

		for (int ii = 0; ii < 7; ++ii)
		{
			statPanel[ii + rfSystemOffset] = new ThermCaptionPanel(rfSysPlotTitle[ii], entryPointApp);
			statPanel[ii + rfSystemOffset].getThermPlotPanel().setMinValue(rfSysMinValue[ii]);
			statPanel[ii + rfSystemOffset].getThermPlotPanel().setMax(rfSysMaxValue[ii]);
			statPanel[ii + rfSystemOffset].getThermPlotPanel().setColor(rfSysColor[ii]);
			statPanel[ii + rfSystemOffset].getThermPlotPanel().setPlotWidth(plotWidth);
			statPanel[ii + rfSystemOffset].getThermPlotPanel().setPlotHeight(plotHeight);
			statPanel[ii + rfSystemOffset].getThermPlotPanel().setFontPixelSize(fontPixelSize);
			statPanel[ii + rfSystemOffset].getThermPlotPanel().setChartAreaPixelLeftOffset(chartAreaPixelLeftOffset);
			statPanel[ii + rfSystemOffset].getThermPlotPanel().setChartAreaPixelWidth(chartAreaPixelWidth);
			statPanel[ii + rfSystemOffset].getThermPlotPanel().setChartAreaOpacity(chartAreaOpacity);
			statPanel[ii + rfSystemOffset].getThermPlotPanel().setChartAreaBackgroundColor(chartAreaBackgroundColor);
			statPanel[ii + rfSystemOffset].getThermPlotPanel().setPlotBackgroundColor(plotBackgroundColor);
			statPanel[ii + rfSystemOffset].getThermPlotPanel().setGridlineColor(gridlineColor);
			rfStatusHorizontalPanel.add(statPanel[ii + rfSystemOffset]);
		}

		CaptionPanel modStatusCaptionPanel = new CaptionPanel("Modulator Status");
		HorizontalPanel modStatusHorizontalPanel = new HorizontalPanel();
		modStatusCaptionPanel.add(modStatusHorizontalPanel);
		add(modStatusCaptionPanel);
		
		String[] modPlotTitle = {"Mod Press", "Mod1 Temp", "Mod2 Temp", "Mod3 Temp", "Mod V", "Mod I", "Mod P"};
		double[] modMinValue = {0.0, 10, 10, 10, 0.0, 0.0, 0.0 };
		double[][] modMaxValue = {
				{  4,     6,    8},
				{ 30,    40,   60},
				{ 30,    40,   60},
				{ 30,    40,   60},
				{  3,     5,    6},
				{  6,    10,   15},
				{ 50,   100,  140}};
		String[][] modColor = { 
				{yellow, green, red},
				{green, yellow, red},
				{green, yellow, red},
				{green, yellow, red},
				{yellow, green, red},
				{yellow, green, red},
				{yellow, green, red}};

		for (int ii = 0; ii < 7; ++ii)
		{
			statPanel[ii + modOffset] = new ThermCaptionPanel(modPlotTitle[ii], entryPointApp);
			statPanel[ii + modOffset].getThermPlotPanel().setMinValue(modMinValue[ii]);
			statPanel[ii + modOffset].getThermPlotPanel().setMax(modMaxValue[ii]);
			statPanel[ii + modOffset].getThermPlotPanel().setColor(modColor[ii]);
			statPanel[ii + modOffset].getThermPlotPanel().setPlotWidth(plotWidth);
			statPanel[ii + modOffset].getThermPlotPanel().setPlotHeight(plotHeight);
			statPanel[ii + modOffset].getThermPlotPanel().setFontPixelSize(fontPixelSize);
			statPanel[ii + modOffset].getThermPlotPanel().setChartAreaPixelLeftOffset(chartAreaPixelLeftOffset);
			statPanel[ii + modOffset].getThermPlotPanel().setChartAreaPixelWidth(chartAreaPixelWidth);
			statPanel[ii + modOffset].getThermPlotPanel().setChartAreaOpacity(chartAreaOpacity);
			statPanel[ii + modOffset].getThermPlotPanel().setChartAreaBackgroundColor(chartAreaBackgroundColor);
			statPanel[ii + modOffset].getThermPlotPanel().setPlotBackgroundColor(plotBackgroundColor);
			statPanel[ii + modOffset].getThermPlotPanel().setGridlineColor(gridlineColor);
			modStatusHorizontalPanel.add(statPanel[ii + modOffset]);
		}

		CaptionPanel waterStatusCaptionPanel = new CaptionPanel("Water System Status");
		HorizontalPanel waterStatusHorizontalPanel = new HorizontalPanel();
		waterStatusCaptionPanel.add(waterStatusHorizontalPanel);
		add(waterStatusCaptionPanel);
		
		String[] waterSysPlotTitle = {"Body Flow", "Tank Flow", "Collector Flow", "Solenoid Flow", "Rtn Press.", "Input Temp", "Hall Temp"};
		double[] waterSysMinValue = {0.0, 0.0, 0.0, 0.0, 0.0, 10.0, 10.0 };
		double[][] waterSysMaxValue = {
				{ 13,    18,   30},
				{  9,    11,   15},
				{250,   300,  375},
				{ 25,    30,   50},
				{  0.5,   1.5,  3},
				{ 17,    30,   40},
				{ 17,    30,   40}};
		String[][] waterSysColor = { 
				{red, yellow, green},
				{red, yellow, green},
				{red, yellow, green},
				{red, yellow, green},
				{yellow, green, red},
				{yellow, green, red},
				{yellow, green, red}};
		for (int ii = 0; ii < 7; ++ii)
		{
			statPanel[ii + waterOffset] = new ThermCaptionPanel(waterSysPlotTitle[ii], entryPointApp);
			statPanel[ii + waterOffset].getThermPlotPanel().setMinValue(waterSysMinValue[ii]);
			statPanel[ii + waterOffset].getThermPlotPanel().setMax(waterSysMaxValue[ii]);
			statPanel[ii + waterOffset].getThermPlotPanel().setColor(waterSysColor[ii]);
			statPanel[ii + waterOffset].getThermPlotPanel().setPlotWidth(plotWidth);
			statPanel[ii + waterOffset].getThermPlotPanel().setPlotHeight(plotHeight);
			statPanel[ii + waterOffset].getThermPlotPanel().setFontPixelSize(fontPixelSize);
			statPanel[ii + waterOffset].getThermPlotPanel().setChartAreaPixelLeftOffset(chartAreaPixelLeftOffset);
			statPanel[ii + waterOffset].getThermPlotPanel().setChartAreaPixelWidth(chartAreaPixelWidth);
			statPanel[ii + waterOffset].getThermPlotPanel().setChartAreaOpacity(chartAreaOpacity);
			statPanel[ii + waterOffset].getThermPlotPanel().setChartAreaBackgroundColor(chartAreaBackgroundColor);
			statPanel[ii + waterOffset].getThermPlotPanel().setPlotBackgroundColor(plotBackgroundColor);
			statPanel[ii + waterOffset].getThermPlotPanel().setGridlineColor(gridlineColor);
			waterStatusHorizontalPanel.add(statPanel[ii + waterOffset]);
		}
		addStatPanel(0);
	}
	private void addStatPanel(int ipanel)
	{
		statPanel[ipanel].initialize();
		new GaugePlotWaiter(plotLoadTimeMs, ipanel);
		GWT.log(Integer.toString(ipanel));

	}
	private void startMqtt()
	{
		GWT.log("Starting MQTT");
		new WaterMqttData(entryPointApp);
		new ModPressMqttData(entryPointApp);
		new ReturnPressMqttData(entryPointApp);
		new RfPowerMqttData(entryPointApp);
		new GeigerMqttData(entryPointApp);
		new ModPower1MqttData(entryPointApp);
		new ModPower2MqttData(entryPointApp);
		new ModStateMqttData(entryPointApp);
		new SolarMeterMqttData(entryPointApp);
	}
	class GaugePlotWaiter extends GskelLoadWaiter
	{
		public GaugePlotWaiter(int loopTimeMillis, int itask) 
		{
			super(loopTimeMillis, itask);

		}
		@Override
		public boolean isLoaded() 
		{
			boolean loaded = false;
			for (int ii = 0; ii < 21; ++ii)
				if (getItask() == ii) loaded = statPanel[ii].isLoaded();
			return loaded;
		}
		@Override
		public void taskAfterLoad() 
		{
			for (int ii = 0; ii < 20; ++ii)
				if (getItask() == ii) addStatPanel(ii + 1);
			if (getItask() == 20) startMqtt();
		}
		
	}
	class WaterMqttData extends MqttData
	{

		public WaterMqttData(EntryPointApp entryPointApp) 
		{
			super("itsWaterSystem/get", MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try
			{
				statPanel[0 + waterOffset].updateReadings(Double.parseDouble(getJsonValue("body")));
				statPanel[1 + waterOffset].updateReadings(Double.parseDouble(getJsonValue("tank")));
				statPanel[2 + waterOffset].updateReadings(Double.parseDouble(getJsonValue("collector")));
				statPanel[3 + waterOffset].updateReadings(Double.parseDouble(getJsonValue("solenoid")));
				statPanel[5 + waterOffset].updateReadings(Double.parseDouble(getJsonValue("inputTemp")));
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
			super("itsCernMod/get/temp", MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try
			{
				statPanel[0 + modOffset].updateReadings(Double.parseDouble(getJsonValue("wtrp")));
				statPanel[1 + modOffset].updateReadings(Double.parseDouble(getJsonValue("tmod1")));
				statPanel[2 + modOffset].updateReadings(Double.parseDouble(getJsonValue("tmod2")));
				statPanel[3 + modOffset].updateReadings(Double.parseDouble(getJsonValue("tmod3")));
			}
			catch (Exception e)
			{
			}
			
		}
	}
	class ModPower1MqttData extends MqttData
	{

		public ModPower1MqttData(EntryPointApp entryPointApp) 
		{
			super("itsCernMod/get/power1", MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try
			{
				statPanel[4 + modOffset].updateReadings(Double.parseDouble(getJsonValue("volts")));
				statPanel[5 + modOffset].updateReadings(Double.parseDouble(getJsonValue("current")));
				statPanel[6 + modOffset].updateReadings(Double.parseDouble(getJsonValue("power")));
			}
			catch (Exception e)
			{
			}
			
		}
	}
	class ModPower2MqttData extends MqttData
	{

		public ModPower2MqttData(EntryPointApp entryPointApp) 
		{
			super("itsCernMod/get/power2", MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try
			{
				statPanel[2 + rfSystemOffset].updateReadings(Double.parseDouble(getJsonValue("cathV")));
			}
			catch (Exception e)
			{
			}
			
		}
	}
	class RfPowerMqttData extends MqttData
	{

		public RfPowerMqttData(EntryPointApp entryPointApp) 
		{
			super("itsPowerMeter01/get", MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try
			{
				double wattRead = Math.pow(10.0, (Double.parseDouble(getJsonValue("power2")) - 60.0) / 10.0);
				statPanel[0 + rfSystemOffset].updateReadings(wattRead);
				entryPointApp.klystronPower = wattRead;
			}
			catch (Exception e)
			{
			}
			
		}
	}
	class GeigerMqttData extends MqttData
	{

		public GeigerMqttData(EntryPointApp entryPointApp) 
		{
			super("itsGeiger01/get/cpm", MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try
			{
				statPanel[1 + rfSystemOffset].updateReadings(Double.parseDouble(getJsonValue("cpmGet")));
			}
			catch (Exception e)
			{
			}
			
		}
	}
	class ModStateMqttData extends MqttData
	{

		public ModStateMqttData(EntryPointApp entryPointApp) 
		{
			super("itsCernMod/get/state", MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try
			{
				statPanel[3 + rfSystemOffset].updateReadings(Double.parseDouble(getJsonValue("pulseWidth")));
				statPanel[4 + rfSystemOffset].updateReadings(Double.parseDouble(getJsonValue("duty")));
				statPanel[5 + rfSystemOffset].updateReadings(Double.parseDouble(getJsonValue("freq")));
				statPanel[6 + rfSystemOffset].updateReadings(Double.parseDouble(getJsonValue("stateOn")));
				entryPointApp.dutyFactor = Double.parseDouble(getJsonValue("duty"));
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
			super("itspressureMeter/get", MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try
			{
				statPanel[4 + waterOffset].updateReadings(Double.parseDouble(getJsonValue("pressure")));
			}
			catch (Exception e)
			{
			}
			
		}
	}
	class SolarMeterMqttData extends MqttData
	{

		public SolarMeterMqttData(EntryPointApp entryPointApp) 
		{
			super("itsSolarMeter01/get/cond", MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try
			{
				statPanel[6 + waterOffset].updateReadings(Double.parseDouble(getJsonValue("tempGet")));
				Date now = new Date();
				int secs = ((int)(now.getTime() - startTime.getTime()) )/ 1000;
				if (secs > 60)
				{
					secs = 60;
					startTime = new Date(now.getTime());
				}
//				statPanel[6 + waterOffset].updateReadings((double) secs);
			}
			catch (Exception e)
			{
			}
			
		}
	}

}
