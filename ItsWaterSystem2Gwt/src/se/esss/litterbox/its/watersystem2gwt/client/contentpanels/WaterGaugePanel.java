package se.esss.litterbox.its.watersystem2gwt.client.contentpanels;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Grid;

import se.esss.litterbox.its.watersystem2gwt.client.EntryPointApp;
import se.esss.litterbox.its.watersystem2gwt.client.googleplots.GaugeCaptionPanel;
import se.esss.litterbox.its.watersystem2gwt.client.gskel.GskelLoadWaiter;
import se.esss.litterbox.its.watersystem2gwt.client.gskel.GskelVerticalPanel;
import se.esss.litterbox.its.watersystem2gwt.client.mqttdata.MqttData;

public class WaterGaugePanel extends GskelVerticalPanel
{
	private Grid gaugeGrid;
	private GaugeCaptionPanel[] gaugeCaptionPanel = new GaugeCaptionPanel[9];
	private String gaugePanelWidth = "175px";
	private String gaugePanelHeight = "175px";
	private int loadWait = 200;

	public WaterGaugePanel(EntryPointApp entryPointApp) 
	{
		super(false, entryPointApp);
		gaugeGrid = new Grid(2, 5);
		add(gaugeGrid);
		gaugeCaptionPanel[0] = new GaugeCaptionPanel("Body Flow", "Body Flow", 0, 30, 18.0, 30.0, 13.0, 18.0, 0.0, 13.0, gaugePanelWidth, gaugePanelHeight);
		gaugeCaptionPanel[1] = new GaugeCaptionPanel("Tank Flow", "Tank Flow", 0, 15, 11.0, 15.0, 9.0, 11.0, 0.0, 9.0, gaugePanelWidth, gaugePanelHeight);
		gaugeCaptionPanel[2] = new GaugeCaptionPanel("Collector Flow", "Collector Flow", 0, 375, 300.0, 375.0, 250.0, 300.0, 0.0, 250.0, gaugePanelWidth, gaugePanelHeight);
		gaugeCaptionPanel[3] = new GaugeCaptionPanel("Solenoid Flow", "Solenoid Flow", 0, 50, 30.0, 50.0, 25.0, 30.0, 0.0, 25.0, gaugePanelWidth, gaugePanelHeight);
		gaugeCaptionPanel[4] = new GaugeCaptionPanel("Rtn Press.", "Rtn Press.", 0.0, 3.0, 0.5, 1.5, 0.0, 0.5, 1.5, 3.0, gaugePanelWidth, gaugePanelHeight);
		gaugeCaptionPanel[5] = new GaugeCaptionPanel("Input Temp", "Input Temp", 10.0, 40.0, 10.0, 25.0, 25.0, 35.0, 35.0, 40.0, gaugePanelWidth, gaugePanelHeight);
		gaugeCaptionPanel[6] = new GaugeCaptionPanel("Mod Press.", "Mod Press.", 0.0, 8.0, 4.0, 6.0, 0.0, 4.0, 6.0, 8.0, gaugePanelWidth, gaugePanelHeight);
		gaugeCaptionPanel[7] = new GaugeCaptionPanel("Mod1 Temp.", "Mod1 Temp.", 10.0, 60.0, 10.0, 30.0, 30.0, 45.0, 45.0, 60.0, gaugePanelWidth, gaugePanelHeight);
		gaugeCaptionPanel[8] = new GaugeCaptionPanel("Mod2 Temp.", "Mod2 Temp.", 10.0, 60.0, 10.0, 30.0, 30.0, 45.0, 45.0, 60.0, gaugePanelWidth, gaugePanelHeight);
		gaugeCaptionPanel[9] = new GaugeCaptionPanel("Mod3 Temp.", "Mod3 Temp.", 10.0, 60.0, 10.0, 30.0, 30.0, 45.0, 45.0, 60.0, gaugePanelWidth, gaugePanelHeight);
		for (int irow  = 0; irow < 2; ++irow)
			for (int icol = 0; icol < 5; ++icol) gaugeGrid.setWidget(irow, icol, gaugeCaptionPanel[irow * 5 + icol]);
		addGauge(0);
	}
	private void addGauge(int igauge)
	{
		gaugeCaptionPanel[igauge].initialize();
		new GaugePlotWaiter(loadWait, igauge);
	}
	private void startMqtt()
	{
		new WaterSystemMqttData(getEntryPointApp());
		new ModTempMqttData(getEntryPointApp());
		new ReturnPressMqttData(getEntryPointApp());
	}
	class GaugePlotWaiter extends GskelLoadWaiter
	{
		public GaugePlotWaiter(int loopTimeMillis, int itask) {super(loopTimeMillis, itask);}
		@Override
		public boolean isLoaded() 
		{
			boolean loaded = false;
			for (int ii = 0; ii < 10; ++ii)
				if (getItask() == ii) loaded = gaugeCaptionPanel[ii].isLoaded();
			return loaded;
		}
		@Override
		public void taskAfterLoad() 
		{
			for (int ii = 0; ii < 9; ++ii)
				if (getItask() == ii) addGauge(ii + 1);
			if (getItask() == 9) startMqtt();
		}
		
	}
	class WaterSystemMqttData extends MqttData
	{

		public WaterSystemMqttData(EntryPointApp entryPointApp) 
		{
			super("itsWaterSystem/get", MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try
			{
				gaugeCaptionPanel[0].updateReadings(Double.parseDouble(getJsonValue("body")));
				gaugeCaptionPanel[1].updateReadings(Double.parseDouble(getJsonValue("tank")));
				gaugeCaptionPanel[2].updateReadings(Double.parseDouble(getJsonValue("collector")));
				gaugeCaptionPanel[3].updateReadings(Double.parseDouble(getJsonValue("solenoid")));
				gaugeCaptionPanel[5].updateReadings(Double.parseDouble(getJsonValue("inputTemp")));
				this.getEntryPointApp().getDateLabel().setText(new Date().toString());
			}
			catch (Exception e)
			{
				GWT.log(e.getMessage());
			}
			
		}
	}
	class ModTempMqttData extends MqttData
	{

		public ModTempMqttData(EntryPointApp entryPointApp) 
		{
			super("itsCernMod/get/temp", MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try
			{
				gaugeCaptionPanel[6].updateReadings(Double.parseDouble(getJsonValue("wtrp")));
				gaugeCaptionPanel[7].updateReadings(Double.parseDouble(getJsonValue("tmod1")));
				gaugeCaptionPanel[8].updateReadings(Double.parseDouble(getJsonValue("tmod2")));
				gaugeCaptionPanel[9].updateReadings(Double.parseDouble(getJsonValue("tmod3")));
			}
			catch (Exception e)
			{
				GWT.log(e.getMessage());
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
				gaugeCaptionPanel[4].updateReadings(Double.parseDouble(getJsonValue("pressure")));
			}
			catch (Exception e)
			{
				GWT.log(e.getMessage());
			}
			
		}
	}

}
