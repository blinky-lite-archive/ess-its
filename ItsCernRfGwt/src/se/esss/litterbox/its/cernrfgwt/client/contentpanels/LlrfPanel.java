package se.esss.litterbox.its.cernrfgwt.client.contentpanels;


import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import se.esss.litterbox.its.cernrfgwt.client.EntryPointApp;
import se.esss.litterbox.its.cernrfgwt.client.googleplots.GooglePlotLoadWaiter;
import se.esss.litterbox.its.cernrfgwt.client.googleplots.TimePlotCaptionPanel;
import se.esss.litterbox.its.cernrfgwt.client.gskel.GskelSettingButtonGrid;
import se.esss.litterbox.its.cernrfgwt.client.gskel.GskelVerticalPanel;
import se.esss.litterbox.its.cernrfgwt.client.mqttdata.MqttData;


public class LlrfPanel extends GskelVerticalPanel
{
	boolean superCreated = false;
	private EntryPointApp entryPointApp;
	Button changeButton  = new Button("Change");
	Button setButton  = new Button("Set");
	Button cancelButton  = new Button("Cancel");
	boolean settingsPermitted = false;
	boolean settingsEnabled = false;
	
	private TextBox rfFreqTextBox = new TextBox();
	private TextBox rfPowLvlTextBox = new TextBox();
	private CheckBox rfPowOnCheckBox = new CheckBox();
	private Label rfPowerReading1 = new Label();
	private Label rfPowerReading2 = new Label();
	SettingButtonGrid settingButtonGrid;
	PowerMeterMqttData powerMeterMqttData;
	RFSigGenMqttData rFSigGenMqttData;
	String powerMeterMqttTopic;
	String rfSigGenMqttTopic;
	TimePlotCaptionPanel powerPlot;

	public boolean isSettingsPermitted() {return settingsPermitted;}
	public void setSettingsPermitted(boolean settingsPermitted) {this.settingsPermitted = settingsPermitted;}

	public LlrfPanel(String tabTitle, String llrfIceCubetimerMqttTopic, String powerMeterMqttTopic, String rfSigGenMqttTopic, EntryPointApp entryPointApp, boolean settingsPermitted) 
	{
		super(tabTitle, "llrfTabStyle", entryPointApp.setupApp);
		this.settingsPermitted = settingsPermitted;
		this.entryPointApp = entryPointApp;
		this.powerMeterMqttTopic = powerMeterMqttTopic;
		this.rfSigGenMqttTopic = rfSigGenMqttTopic;
		superCreated = true;
		this.getGskelTabLayoutScrollPanel().setStyleName("ItsLlrfPanel");
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.add(settingsCaptionPanel());
		String[] timingChannelName = {"ScpTrg", "RFGate", "PMTrg", "CH4"};
		hp1.add(new IceCubeTimerPanel("LLRF Timer", llrfIceCubetimerMqttTopic, timingChannelName, settingsPermitted, entryPointApp));
		String[] plotLegend = {"Input","Output"};
		powerPlot = new TimePlotCaptionPanel(500, "Klystron Power", "dBm", plotLegend, "600px", "400px");
		hp1.add(powerPlot);
		add(hp1);
		new powerPlotWaiter(50);
	}
	private void loadMqttData()
	{
		powerMeterMqttData = new PowerMeterMqttData();
		rFSigGenMqttData = new RFSigGenMqttData();
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
	private CaptionPanel settingsCaptionPanel()
	{
		Grid settingGrid = new Grid(6, 3);
		HTMLTable.CellFormatter formatter = settingGrid.getCellFormatter();
		formatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);	
		
		String textBoxWidth = "3.0em";
		
		rfFreqTextBox.setSize(textBoxWidth, "1.0em");
		rfPowLvlTextBox.setSize(textBoxWidth, "1.0em");
		rfPowerReading1.setSize(textBoxWidth, "1.0em");
		rfPowerReading2.setSize(textBoxWidth, "1.0em");

		for (int irow = 0; irow < 6; ++irow)
		{
			formatter.setHorizontalAlignment(irow, 1, HasHorizontalAlignment.ALIGN_CENTER);
			formatter.setVerticalAlignment(irow, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		}
		
		settingGrid.setWidget(0, 0, new Label("Name"));
		settingGrid.setWidget(0, 1, new Label("Value"));
		settingGrid.setWidget(0, 2, new Label("Unit"));
		
		settingGrid.setWidget(1, 0, new Label("RF Frequency"));
		settingGrid.setWidget(1, 1, rfFreqTextBox);
		settingGrid.setWidget(1, 2, new Label("MHz"));
		
		settingGrid.setWidget(2, 0, new Label("RF Power Level"));
		settingGrid.setWidget(2, 1, rfPowLvlTextBox);
		settingGrid.setWidget(2, 2, new Label("dBm"));
		
		settingGrid.setWidget(3, 0, new Label("RF Power On"));
		settingGrid.setWidget(3, 1, rfPowOnCheckBox);
		settingGrid.setWidget(3, 2, new Label(""));
				
		settingGrid.setWidget(4, 0, new Label("RF Power Reading 1"));
		settingGrid.setWidget(4, 1, rfPowerReading1);
		settingGrid.setWidget(4, 2, new Label("dBm"));
		
		settingGrid.setWidget(5, 0, new Label("RF Power Reading 2"));
		settingGrid.setWidget(5, 1, rfPowerReading2);
		settingGrid.setWidget(5, 2, new Label("dBm"));

		CaptionPanel settingsCaptionPanel = new CaptionPanel("Settings");
		VerticalPanel vp1 = new VerticalPanel();
		settingButtonGrid = new SettingButtonGrid(settingsPermitted);
		vp1.add(settingButtonGrid);
		vp1.add(settingGrid);
		settingsCaptionPanel.add(vp1);
		enableInput(false);
		return settingsCaptionPanel;
		
	}
	private void enableInput(boolean enabled)
	{
		rfFreqTextBox.setEnabled(enabled);
		rfPowLvlTextBox.setEnabled(enabled);
		rfPowOnCheckBox.setEnabled(enabled);
	}
	private void updateRfSigGenSettings()
	{
		if (settingButtonGrid.isSettingsEnabled()) return;
		try 
		{
			rfFreqTextBox.setText(rFSigGenMqttData.getJsonValue("rfFreq"));
			rfPowLvlTextBox.setText(rFSigGenMqttData.getJsonValue("rfPowLvl"));
			String rfPowerStateText = rFSigGenMqttData.getJsonValue("rfPowOn");
			rfPowerStateText = rfPowerStateText.trim();
			boolean rfPowerState = false;
			if (rfPowerStateText.equals("ON")) rfPowerState = true;
			rfPowOnCheckBox.setValue(rfPowerState);
		} catch (Exception e) 
		{
			entryPointApp.setupApp.getStatusTextArea().addStatus(e.getMessage());
		}
	}
	private void updatePowerMeterReadings()
	{
		try
		{
			double[] powerRead = new double[2];
			powerRead[0] = Double.parseDouble(powerMeterMqttData.getJsonValue("power1"));
			powerRead[1] = Double.parseDouble(powerMeterMqttData.getJsonValue("power2"));
			rfPowerReading1.setText(NumberFormat.getFormat("###.##").format(powerRead[0]));
			rfPowerReading2.setText(NumberFormat.getFormat("###.##").format(powerRead[1]));
			powerPlot.updateReadings(powerRead);
			
		}
		catch(Exception e)
		{
			entryPointApp.setupApp.getStatusTextArea().addStatus(e.getMessage());
		}
	}
	private void setLlrf() throws Exception
	{
		String[][] jsonArray = new String[3][2];
		double rfFreq;
		try
		{rfFreq = Double.parseDouble(rfFreqTextBox.getText());
		}catch (NumberFormatException nfe) {throw new Exception("Rf Frequency is not a number");}
		jsonArray[0][0] = "rfFreq";
		jsonArray[0][1] = Double.toString(rfFreq);
		double rfPowLvl;
		try
		{rfPowLvl = Double.parseDouble(rfPowLvlTextBox.getText());
		}catch (NumberFormatException nfe) {throw new Exception("Rf Power Level is not a number");}
		jsonArray[1][0] = "rfPowLvl";
		jsonArray[1][1] = Double.toString(rfPowLvl);
		String rfOn = "OFF";
		if (rfPowOnCheckBox.getValue()) rfOn = "ON";
		jsonArray[2][0] = "rfPowOn";
		jsonArray[2][1] = rfOn;
		entryPointApp.mqttService.publishJsonArray(rfSigGenMqttTopic, jsonArray, settingsPermitted, entryPointApp.setupApp.isDebug(), "ok", new LlrfPublishSettingsCallback());
		enableInput(false);		
	}
	class SettingButtonGrid extends GskelSettingButtonGrid
	{

		public SettingButtonGrid(boolean settingsPermitted) 
		{
			super(settingsPermitted);
		}

		@Override
		public void enableSettingsInput(boolean enabled) 
		{
			enableInput(enabled);
		}

		@Override
		public void doSettings() 
		{
			try 
			{
				setLlrf();
			} catch (Exception e) 
			{
				entryPointApp.setupApp.getStatusTextArea().addStatus(e.getMessage());
			}
		}
		
	}
	class PowerMeterMqttData extends MqttData
	{
		public PowerMeterMqttData() 
		{
			super(powerMeterMqttTopic, MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try 
			{
				updatePowerMeterReadings();
			} catch (Exception e) 
			{
				entryPointApp.setupApp.getStatusTextArea().addStatus(e.getMessage());
			}
			
		}
	}
	class RFSigGenMqttData extends MqttData
	{
		public RFSigGenMqttData() 
		{
			super(rfSigGenMqttTopic, MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try 
			{
				updateRfSigGenSettings();
			} catch (Exception e) 
			{
				entryPointApp.setupApp.getStatusTextArea().addStatus(e.getMessage());
			}
			
		}
	}
	class LlrfPublishSettingsCallback implements AsyncCallback<String>
	{
		@Override
		public void onFailure(Throwable caught) 
		{
			entryPointApp.setupApp.getStatusTextArea().addStatus("Failure: Putting LLRF Settings");
			entryPointApp.setupApp.getStatusTextArea().addStatus(caught.getMessage());
		}
		@Override
		public void onSuccess(String result) 
		{
			entryPointApp.setupApp.getStatusTextArea().addStatus("Success: Putting LLRF Settings");
		}
	}
	class powerPlotWaiter extends GooglePlotLoadWaiter
	{
		public powerPlotWaiter(int loopTimeMillis) {super(loopTimeMillis);}
		@Override
		public boolean isLoaded() {return powerPlot.isLoaded();}
		@Override
		public void taskAfterLoad() {loadMqttData();}
		
	}
}
