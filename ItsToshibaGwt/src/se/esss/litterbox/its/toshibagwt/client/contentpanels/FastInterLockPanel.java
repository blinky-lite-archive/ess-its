package se.esss.litterbox.its.toshibagwt.client.contentpanels;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.ColumnFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import se.esss.litterbox.its.toshibagwt.client.EntryPointApp;
import se.esss.litterbox.its.toshibagwt.client.bytegearboxservice.ByteGearBoxData;
import se.esss.litterbox.its.toshibagwt.client.gskel.GskelSettingButtonGrid;
import se.esss.litterbox.its.toshibagwt.client.gskel.GskelVerticalPanel;
import se.esss.litterbox.its.toshibagwt.client.mqttdata.MqttData;
import se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt.ByteGearGwt;

public class FastInterLockPanel extends GskelVerticalPanel
{
	Button resetButton;
	Button pinSwitchButton;
	Button arcTestButton;
	double reflPowLvl;
	boolean pinSwitchOn = false;
	boolean arcDetTrip = false;
	boolean aftDetTrip = false;
	boolean reflPowerTrip = false;
	boolean updateSettings = true;
	ByteGearGwt cpuByteGear = null;
	ByteGearBoxData cpuByteGearBoxData = null;
	Image reflPowerTripImage = null;
	Image arcDetTripImage = null;
	Image aftDetTripImage = null;
	TextBox reflPowerTripLevelTextbox = new TextBox();


	public FastInterLockPanel(EntryPointApp entryPointApp) 
	{
		super(true, entryPointApp);
		try 
		{
			cpuByteGearBoxData = entryPointApp.getByteGearBoxData("klyPlcProtoCpu");
			cpuByteGear = cpuByteGearBoxData.getByteGearBoxGwt().getByteGear("CPU_CONF");
		} catch (Exception e) {GWT.log(e.getMessage());}
		
		Grid buttonGrid = new Grid(4,2);
		CellFormatter buttonGridCellFormatter = buttonGrid.getCellFormatter();
		ColumnFormatter buttonGridColumnFormatter = buttonGrid.getColumnFormatter();
		buttonGridColumnFormatter.setWidth(1, "200px");
		
		resetButton = new Button("Reset");
		resetButton.setStyleName("stateButtonReset");
		resetButton.setSize("8.0em", "2.0em");
		resetButton.setEnabled(entryPointApp.getSetup().isSettingsPermitted());
		resetButton.addClickHandler(new ResetButtonClickHandler(this));

		pinSwitchButton = new Button("Pin Switch");
		pinSwitchButton.setStyleName("stateButtonPreConTrue");
		pinSwitchButton.setSize("8.0em", "2.0em");
		pinSwitchButton.setEnabled(entryPointApp.getSetup().isSettingsPermitted());
		pinSwitchButton.addClickHandler(new PinSwitchButtonClickHandler(this));

		arcTestButton = new Button("Arc Test");
		arcTestButton.setStyleName("stateButtonOff");
		arcTestButton.setSize("8.0em", "2.0em");
		arcTestButton.setEnabled(entryPointApp.getSetup().isSettingsPermitted());
		arcTestButton.addClickHandler(new ArcTestButtonClickHandler(this));
		
		HorizontalPanel reflPowerTripPanel = new HorizontalPanel();
		reflPowerTripPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		reflPowerTripImage = new Image("images/redlight.png");
		reflPowerTripImage.setSize("3.0em", "3.0em");
		reflPowerTripPanel.add(new Label("Reflected Power Trip  : "));
		reflPowerTripPanel.add(reflPowerTripImage);

		HorizontalPanel arcDetTripPanel = new HorizontalPanel();
		arcDetTripPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		arcDetTripImage = new Image("images/redlight.png");
		arcDetTripImage.setSize("3.0em", "3.0em");
		arcDetTripPanel.add(new Label("CERN Arc Det. Trip  : "));
		arcDetTripPanel.add(arcDetTripImage);
		
		HorizontalPanel aftDetTripPanel = new HorizontalPanel();
		aftDetTripPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		aftDetTripImage = new Image("images/redlight.png");
		aftDetTripImage.setSize("3.0em", "3.0em");
		aftDetTripPanel.add(new Label("AFT Arc Det. Trip  : "));
		aftDetTripPanel.add(aftDetTripImage);
		
		HorizontalPanel rfTripLevelPanel = new HorizontalPanel();
		rfTripLevelPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		reflPowerTripLevelTextbox.setWidth("4.0em");
		reflPowerTripLevelTextbox.setEnabled(false);
		rfTripLevelPanel.add(new Label("Refl Power Trip Level  : "));
		rfTripLevelPanel.add(reflPowerTripLevelTextbox);

		buttonGrid.setWidget(0, 0, resetButton);
		buttonGrid.setWidget(1, 0, pinSwitchButton);
		buttonGrid.setWidget(2, 0, arcTestButton);
		buttonGrid.setWidget(0, 1, reflPowerTripPanel);
		buttonGrid.setWidget(1, 1, arcDetTripPanel);
		buttonGrid.setWidget(2, 1, aftDetTripPanel);
		buttonGrid.setWidget(3, 0, rfTripLevelPanel);
		buttonGrid.setWidget(3, 1, new ReflPowerTripLvlButtonGrid());
		for (int ii = 0; ii < 4; ++ii)
		{
			buttonGridCellFormatter.setHorizontalAlignment(ii, 0, HasHorizontalAlignment.ALIGN_LEFT);
			buttonGridCellFormatter.setHorizontalAlignment(ii, 1, HasHorizontalAlignment.ALIGN_RIGHT);
			
		}
		buttonGridCellFormatter.setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
		CaptionPanel cp = new CaptionPanel("Fast Interlock");
		cp.setCaptionHTML("<font size=\"+2\">Fast Interlock</font>");
		cp.add(buttonGrid);
		add(cp);
		new FastInterlockMqttData(this);
	}
	private void setCpuState(String state)
	{
		try
		{
			cpuByteGear.getWriteByteTooth("RESET").setValue("false");
			cpuByteGear.getWriteByteTooth("OFF_CMD").setValue("false");
			cpuByteGear.getWriteByteTooth("AUX_CMD").setValue("false");
			cpuByteGear.getWriteByteTooth("FIL_CMD").setValue("false");
			cpuByteGear.getWriteByteTooth("STBY_CMD").setValue("false");
			cpuByteGear.getWriteByteTooth("HV_CMD").setValue("false");
			cpuByteGear.getWriteByteTooth("RF_CMD").setValue("false");
			cpuByteGear.getWriteByteTooth("TEST_ALL_AD").setValue("false");
			cpuByteGear.getWriteByteTooth(state).setValue("true");
			cpuByteGear.getWriteByteTooth("WR_DATA").setValue("true");
			cpuByteGearBoxData.setWriteData(cpuByteGear);
		} catch (Exception e) {GWT.log(e.getMessage());}
	}
	static class ResetButtonClickHandler  extends Timer implements ClickHandler, AsyncCallback<String>
	{
		FastInterLockPanel fastInterLockPanel;
		ResetButtonClickHandler(FastInterLockPanel fastInterLockPanel)
		{
			this.fastInterLockPanel = fastInterLockPanel;
		}
		boolean buttonPushed = false;
		@Override
		public void onClick(ClickEvent event) 
		{
			fastInterLockPanel.updateSettings = false;
			fastInterLockPanel.resetButton.setStyleName("stateButtonPressed");
			buttonPushed = true;
			fastInterLockPanel.setCpuState("RESET");
			this.schedule(5000);
		}
		@Override
		public void run() 
		{
			fastInterLockPanel.resetButton.setStyleName("stateButtonReset");
			buttonPushed = false;
			this.cancel();
			String[][] jsonArray = new String[1][2];
			jsonArray[0][0] = "reset";
			jsonArray[0][1] = "TRUE";
			fastInterLockPanel.getEntryPointApp().getSetup().getMqttService().publishJsonArray("toshibaFastInterlock/set", jsonArray, fastInterLockPanel.getEntryPointApp().getSetup().isSettingsPermitted(), this);
			fastInterLockPanel.updateSettings = true;
		}
		@Override
		public void onFailure(Throwable caught) {GWT.log("Error on FastInterlock reset: " + caught.getMessage());}
		@Override
		public void onSuccess(String result) {}
		
	}
	static class PinSwitchButtonClickHandler  extends Timer implements ClickHandler, AsyncCallback<String>
	{
		FastInterLockPanel fastInterLockPanel;
		PinSwitchButtonClickHandler(FastInterLockPanel fastInterLockPanel)
		{
			this.fastInterLockPanel = fastInterLockPanel;
		}
		boolean buttonPushed = false;
		@Override
		public void onClick(ClickEvent event) 
		{
			fastInterLockPanel.updateSettings = false;
			fastInterLockPanel.pinSwitchButton.setStyleName("stateButtonPressed");
			buttonPushed = true;
			String[][] jsonArray = new String[1][2];
			jsonArray[0][0] = "pinSwitch";
			jsonArray[0][1] = "ON";
			if (fastInterLockPanel.pinSwitchOn) jsonArray[0][1] = "OFF";
			fastInterLockPanel.getEntryPointApp().getSetup().getMqttService().publishJsonArray("toshibaFastInterlock/set", jsonArray, fastInterLockPanel.getEntryPointApp().getSetup().isSettingsPermitted(), this);
			this.schedule(1000);
		}
		@Override
		public void run() 
		{
			if (fastInterLockPanel.pinSwitchOn) 
			{
				fastInterLockPanel.pinSwitchButton.setStyleName("stateButtonPreConTrue");
			}
			else
			{
				fastInterLockPanel.pinSwitchButton.setStyleName("stateButtonActMatch");
			}
			buttonPushed = false;
			this.cancel();
			fastInterLockPanel.updateSettings = true;
		}
		@Override
		public void onFailure(Throwable caught) {GWT.log("Error on Pin Switch: " + caught.getMessage());}
		@Override
		public void onSuccess(String result) {}
		
	}
	static class ArcTestButtonClickHandler  extends Timer implements ClickHandler, AsyncCallback<String>
	{
		FastInterLockPanel fastInterLockPanel;
		ArcTestButtonClickHandler(FastInterLockPanel fastInterLockPanel)
		{
			this.fastInterLockPanel = fastInterLockPanel;
		}
		boolean buttonPushed = false;
		@Override
		public void onClick(ClickEvent event) 
		{
			fastInterLockPanel.updateSettings = false;
			fastInterLockPanel.arcTestButton.setStyleName("stateButtonPressed");
			buttonPushed = true;
			fastInterLockPanel.setCpuState("TEST_ALL_AD");
			
			this.schedule(1000);
		}
		@Override
		public void run() 
		{
			fastInterLockPanel.arcTestButton.setStyleName("stateButtonOff");
			buttonPushed = false;
			this.cancel();
			fastInterLockPanel.updateSettings = true;
		}
		@Override
		public void onFailure(Throwable caught) {GWT.log("Error on Arc Test: " + caught.getMessage());}
		@Override
		public void onSuccess(String result) {}
		
	}
	static class FastInterlockMqttData extends MqttData
	{
		FastInterLockPanel fastInterLockPanel;
		public FastInterlockMqttData(FastInterLockPanel fastInterLockPanel) 
		{
			super("toshibaFastInterlock/get", MqttData.JSONDATA, 1000, fastInterLockPanel.getEntryPointApp());
			this.fastInterLockPanel = fastInterLockPanel;
		}
		@Override
		public void doSomethingWithData() 
		{
//			"reflPowLvl":"0.143", "pinSwitch":"ON", "trip":"TRUE", "tripType":"arcDet"
			if (!fastInterLockPanel.updateSettings) return;
			try
			{
				fastInterLockPanel.reflPowLvl = Double.parseDouble(getJsonValue("reflPowLvl"));
				fastInterLockPanel.reflPowerTripLevelTextbox.setText(Double.toString(fastInterLockPanel.reflPowLvl));
				fastInterLockPanel.pinSwitchOn = false;
				if (getJsonValue("pinSwitch").equals("ON")) fastInterLockPanel.pinSwitchOn = true;
				if (fastInterLockPanel.pinSwitchOn)
				{
					fastInterLockPanel.pinSwitchButton.setStyleName("stateButtonActMatch");
				}
				else
				{
					fastInterLockPanel.pinSwitchButton.setStyleName("stateButtonPreConTrue");
				}
				fastInterLockPanel.aftDetTrip = false;
				fastInterLockPanel.arcDetTrip = false;
				fastInterLockPanel.reflPowerTrip = false;
				if (getJsonValue("trip").equals("TRUE"))
				{
					if (getJsonValue("tripType").equals("arcDet")) fastInterLockPanel.arcDetTrip = true;
					if (getJsonValue("tripType").equals("aftDet")) fastInterLockPanel.aftDetTrip = true;
					if (getJsonValue("tripType").equals("reflPower")) fastInterLockPanel.reflPowerTrip = true;
				}
				if (fastInterLockPanel.reflPowerTrip)
				{
					fastInterLockPanel.reflPowerTripImage.setUrl("images/redlight.png");
				}
				else
				{
					fastInterLockPanel.reflPowerTripImage.setUrl("images/greenlight.png");
				}
				if (fastInterLockPanel.arcDetTrip)
				{
					fastInterLockPanel.arcDetTripImage.setUrl("images/redlight.png");
				}
				else
				{
					fastInterLockPanel.arcDetTripImage.setUrl("images/greenlight.png");
				}
				if (fastInterLockPanel.aftDetTrip)
				{
					fastInterLockPanel.aftDetTripImage.setUrl("images/redlight.png");
				}
				else
				{
					fastInterLockPanel.aftDetTripImage.setUrl("images/greenlight.png");
				}
				
			}catch (Exception e) {GWT.log(e.getMessage());}
			
		}
		
	}
	class ReflPowerTripLvlButtonGrid extends GskelSettingButtonGrid implements AsyncCallback<String>
	{

		public ReflPowerTripLvlButtonGrid() 
		{
			super(getEntryPointApp().getSetup().isSettingsPermitted());
		}
		@Override
		public void enableSettingsInput(boolean enabled) 
		{
			reflPowerTripLevelTextbox.setEnabled(enabled);
			updateSettings = !enabled;
			
		}
		@Override
		public void doSettings() 
		{
			reflPowLvl = Double.parseDouble(reflPowerTripLevelTextbox.getText());
			String[][] jsonArray = new String[1][2];
			jsonArray[0][0] = "reflPowLvl";
			jsonArray[0][1] = Double.toString(reflPowLvl);
			getEntryPointApp().getSetup().getMqttService().publishJsonArray("toshibaFastInterlock/set", jsonArray, getEntryPointApp().getSetup().isSettingsPermitted(), this);
		}
		@Override
		public void onFailure(Throwable caught) {GWT.log("Error on setting refl power level: " + caught.getMessage());}
		@Override
		public void onSuccess(String result) {}
		
	}

}
