package se.esss.litterbox.its.bluemodgwt.client.contentpanels;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;

import se.esss.litterbox.its.bluemodgwt.client.EntryPointApp;
import se.esss.litterbox.its.bluemodgwt.client.googleplots.LineChartCaptionPanel;
import se.esss.litterbox.its.bluemodgwt.client.gskel.GskelLoadWaiter;
import se.esss.litterbox.its.bluemodgwt.client.gskel.GskelVerticalPanel;
import se.esss.litterbox.its.bluemodgwt.client.mqttdata.MqttData;

public class ModulatorWaveformPanel extends GskelVerticalPanel
{
	boolean settingsPermitted = false;
	EntryPointApp entryPointApp;
	String modWaveformMqttTopic;
	boolean readyForData = false;
	CernModulatorWaveForm cernModulatorWaveForm = null;
	LineChartCaptionPanel vprimPlotPanel;
	LineChartCaptionPanel iprimPlotPanel;
	LineChartCaptionPanel vcathPlotPanel;
	LineChartCaptionPanel icathPlotPanel;
	ModulatorWaveformMqttData modulatorWaveformMqttData;
	Grid plotGrid;
	String plotWidth = "600px";
	String plotHeight = "400px";
	Label noPulseDataLabel = new Label("Pulse Data Not enabled. Enable in Modulator settings Tab");
	
	public ModulatorWaveformPanel(String tabTitle, String modWaveformMqttTopic, EntryPointApp entryPointApp, boolean settingsPermitted) 
	{
		super(tabTitle, "modWaveFormTabStyle", entryPointApp.setupApp);
		this.getGskelTabLayoutScrollPanel().setStyleName("ItsLlrfPanel");
		this.settingsPermitted = settingsPermitted;
		this.entryPointApp = entryPointApp;
		this.modWaveformMqttTopic = modWaveformMqttTopic;
		plotGrid = new Grid(2, 2);
		add(noPulseDataLabel);
		add(plotGrid);
		addVprimPlotPanel();
	}
	private void addVprimPlotPanel()
	{
		String[] vprimPlotPanelLegend = {"Vp"};
		vprimPlotPanel = new LineChartCaptionPanel(300, "Primary Voltage", "Time (uS)", "Voltage (kV)", vprimPlotPanelLegend, plotWidth, plotHeight);
		new WaveformPlotWaiter(50, 0);
	}
	private void addIPrimPlotPanel()
	{
		plotGrid.setWidget(1, 0, vprimPlotPanel);
		String[] iprimPlotPanelLegend = {"Ip"};
		iprimPlotPanel = new LineChartCaptionPanel(300, "Primary Current", "Time (uS)", "Current (Amps)", iprimPlotPanelLegend, plotWidth, plotHeight);
		new WaveformPlotWaiter(50, 1);
	}
	private void addVcathPlotPanel()
	{
		plotGrid.setWidget(1, 1, iprimPlotPanel);
		String[] vcathPlotPanelLegend = {"Vc"};
		vcathPlotPanel = new LineChartCaptionPanel(300, "Cathode Voltage", "Time (uS)", "Voltage (kV)", vcathPlotPanelLegend, plotWidth, plotHeight);
		new WaveformPlotWaiter(50, 2);
	}
	private void addIcathPlotPanel()
	{
		plotGrid.setWidget(0, 0, vcathPlotPanel);
		String[] icathPlotPanelLegend = {"Ic"};
		icathPlotPanel = new LineChartCaptionPanel(300, "Cathode Current", "Time (uS)", "Current (Amps)", icathPlotPanelLegend, plotWidth, plotHeight);
		new WaveformPlotWaiter(50, 3);
	}
	private void startMqtt()
	{
		plotGrid.setWidget(0, 1, icathPlotPanel);
		readyForData = true;
		modulatorWaveformMqttData = new ModulatorWaveformMqttData();
	}
	@Override
	public void tabLayoutPanelInterfaceAction(String message) 
	{
		if (entryPointApp.modulatorSettingPanel.isPulseDataEnabled())
		{
			plotGrid.setVisible(true);
			noPulseDataLabel.setVisible(false);
		}
		else
		{
			plotGrid.setVisible(false);
			noPulseDataLabel.setVisible(true);
			getStatusTextArea().addStatus("Pulse Data Not enabled. Enable in Modulator settings Tab");

		}
	}
	@Override
	public void optionDialogInterfaceAction(String choiceButtonText) {}

	class ModulatorWaveformMqttData extends MqttData
	{
		public ModulatorWaveformMqttData() 
		{
			super(modWaveformMqttTopic, MqttData.BYTEDATA, 1000, entryPointApp);
		}
		@Override
		public void doSomethingWithData() 
		{
			if (!readyForData) return;
			cernModulatorWaveForm = new CernModulatorWaveForm(getMessage());
			double[][] yaxisData = new double[1][];
			yaxisData[0] = cernModulatorWaveForm.getVprim();
			vprimPlotPanel.updateReadings(cernModulatorWaveForm.getTimeStamp(), yaxisData);
			yaxisData[0] = cernModulatorWaveForm.getIprim();
			iprimPlotPanel.updateReadings(cernModulatorWaveForm.getTimeStamp(), yaxisData);
			yaxisData[0] = cernModulatorWaveForm.getVcath();
			vcathPlotPanel.updateReadings(cernModulatorWaveForm.getTimeStamp(), yaxisData);
			yaxisData[0] = cernModulatorWaveForm.getIcath();
			icathPlotPanel.updateReadings(cernModulatorWaveForm.getTimeStamp(), yaxisData);
		}

	}
	class WaveformPlotWaiter extends GskelLoadWaiter
	{
		public WaveformPlotWaiter(int loopTimeMillis, int itask) {super(loopTimeMillis, itask);}
		@Override
		public boolean isLoaded() 
		{
			boolean loaded = false;
			if (getItask() == 0) loaded = vprimPlotPanel.isLoaded();
			if (getItask() == 1) loaded = iprimPlotPanel.isLoaded();
			if (getItask() == 2) loaded = vcathPlotPanel.isLoaded();
			if (getItask() == 3) loaded = icathPlotPanel.isLoaded();
			return loaded;
		}
		@Override
		public void taskAfterLoad() 
		{
			if (getItask() == 0) addIPrimPlotPanel();
			if (getItask() == 1) addVcathPlotPanel();
			if (getItask() == 2) addIcathPlotPanel();
			if (getItask() == 3) startMqtt();
		}
		
	}
}
