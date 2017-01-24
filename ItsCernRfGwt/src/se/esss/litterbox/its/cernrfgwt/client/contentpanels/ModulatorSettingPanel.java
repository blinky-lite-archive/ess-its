package se.esss.litterbox.its.cernrfgwt.client.contentpanels;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import se.esss.litterbox.its.cernrfgwt.client.EntryPointApp;
import se.esss.litterbox.its.cernrfgwt.client.bytedevice.ByteDevice;
import se.esss.litterbox.its.cernrfgwt.client.bytedevice.ByteDeviceList;
import se.esss.litterbox.its.cernrfgwt.client.bytedevice.ByteDeviceReadingDisplayListCaptionPanel;
import se.esss.litterbox.its.cernrfgwt.client.bytedevice.ByteDeviceSettingDisplay;
import se.esss.litterbox.its.cernrfgwt.client.gskel.GskelSettingButtonGrid;
import se.esss.litterbox.its.cernrfgwt.client.gskel.GskelVerticalPanel;
import se.esss.litterbox.its.cernrfgwt.client.mqttdata.MqttData;

public class ModulatorSettingPanel extends GskelVerticalPanel
{
	public EntryPointApp entryPointApp;
	public boolean settingsPermitted = false;
	public boolean puttingSettingsState = false;

	public ByteDeviceList settingDeviceList = null;
	public ByteDeviceList readingDeviceList = null;
	public ArrayList<ByteDeviceSettingDisplay>  settingDeviceDisplayList = new ArrayList<ByteDeviceSettingDisplay>();
	public ArrayList<ByteDeviceReadingDisplayListCaptionPanel>  byteDeviceReadingDisplayListCaptionPanelList = new ArrayList<ByteDeviceReadingDisplayListCaptionPanel>();
	public Button[] modStateButton = new Button[4];
	public boolean readyForData = false;
	private HorizontalPanel settingsReadingsHorizontalPanel;
	public ModulatorDisplayVerticalPanel modulatorInterLocksVerticalPanel1;
	public ModulatorDisplayVerticalPanel modulatorInterLocksVerticalPanel2;
	public ModulatorDisplayVerticalPanel modulatorHVPSVerticalPanel;
	public ModulatorDisplayVerticalPanel modulatorReadbacksVerticalPanel;
	public String modSettingTopic;
	public String modReadingTopic;
	String modIceCubetimerMqttTopic;
	ModulatorReadingsMqttData modulatorReadingsMqttData;
	ModulatorSettingsMqttData modulatorSettingsMqttData;
	SettingButtonGrid settingButtonGrid;
	Label badInterlockLabel = new Label("Interlocks Clear");
	
	public ModulatorSettingPanel(String tabTitle, String modSettingTopic, String modReadingTopic, String modIceCubetimerMqttTopic, EntryPointApp entryPointApp, boolean settingsPermitted) 
	{
		super(tabTitle, "modTabStyle", entryPointApp.setupApp);
		this.settingsPermitted = settingsPermitted;
		this.getGskelTabLayoutScrollPanel().setStyleName("ItsModSettingsPanel");
		this.entryPointApp = entryPointApp;
		this.modSettingTopic = modSettingTopic;
		this.modReadingTopic = modReadingTopic;
		this.modIceCubetimerMqttTopic = modIceCubetimerMqttTopic;
		getEntryPointAppService().getModulatorProtocols(getSetupApp().isDebug(), new GetModulatorProtocolslAsyncCallback());
		modulatorReadbacksVerticalPanel = new ModulatorDisplayVerticalPanel("Modulator Readbacks", getSetupApp());
		modulatorHVPSVerticalPanel = new ModulatorDisplayVerticalPanel("Modulator HVPS", getSetupApp());
		modulatorInterLocksVerticalPanel1 = new ModulatorDisplayVerticalPanel("Modulator Interlocks 1", getSetupApp());
		modulatorInterLocksVerticalPanel2 = new ModulatorDisplayVerticalPanel("Modulator Interlocks 2", getSetupApp());
	}
	public void initPanels()
	{
		settingsReadingsHorizontalPanel = new HorizontalPanel();
		add(settingsReadingsHorizontalPanel);
		settingsReadingsHorizontalPanel.add(settingsCaptionPanel());
		readyForData = true;
		setupReadingsDisplayPanels();
		badInterlockLabel.setWidth("100%");
//		badInterlockLabel.setHeight("3.0em");
		badInterlockLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		badInterlockLabel.setStyleName("modInterlockLabelGood");
		CaptionPanel interlockCaptionPanel = new CaptionPanel("Interlock Status");
		interlockCaptionPanel.add(badInterlockLabel);
		String[] timingChannelName = {"ModTrg", "CH2", "CH3", "CH4"};
		VerticalPanel vp1 = new VerticalPanel();
		vp1.add(interlockCaptionPanel);
		vp1.add(new IceCubeTimerPanel("MOD Timer", modIceCubetimerMqttTopic, timingChannelName, settingsPermitted, entryPointApp));
		settingsReadingsHorizontalPanel.add(vp1);
		modulatorReadingsMqttData = new ModulatorReadingsMqttData();
		modulatorSettingsMqttData = new ModulatorSettingsMqttData();
	}
	@Override
	public void tabLayoutPanelInterfaceAction(String message) {}
	@Override
	public void optionDialogInterfaceAction(String choiceButtonText) {}
	public CaptionPanel settingsCaptionPanel()
	{
		Grid settingGrid = new Grid(settingDeviceList.numDevices() + 1, 5);
		settingGrid.setWidget(0, 0, new Label("Name"));
		settingGrid.setWidget(0, 1, new Label("Min"));
		settingGrid.setWidget(0, 2, new Label("Value"));
		settingGrid.setWidget(0, 3, new Label("Max"));
		settingGrid.setWidget(0, 4, new Label("Comment"));
		for (int ii = 0; ii < settingDeviceList.numDevices(); ++ ii) 
		{
			try {settingDeviceDisplayList.add(new ByteDeviceSettingDisplay(settingDeviceList.getDevice(ii), settingGrid, ii + 1));} 
			catch (Exception e) {getStatusTextArea().addStatus(e.getMessage());}
		}
		
		HTMLTable.CellFormatter formatter = settingGrid.getCellFormatter();
		formatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);	

		settingButtonGrid = new SettingButtonGrid(settingsPermitted);

		
		CaptionPanel settingsCaptionPanel = new CaptionPanel("Settings");
		settingsCaptionPanel.add(settingGrid);
		VerticalPanel vp1 = new VerticalPanel();
		vp1.add(settingButtonGrid);
		vp1.add(settingsCaptionPanel);
		CaptionPanel actionsSettingsCaptionPanel = new CaptionPanel("Modulator Settings");
		actionsSettingsCaptionPanel.add(vp1);
		settingGrid.getRowFormatter().setVisible(1, false);
		settingGrid.getRowFormatter().setVisible(2, false);
		settingGrid.getRowFormatter().setVisible(3, false);
		settingGrid.getRowFormatter().setVisible(4, false);
		return actionsSettingsCaptionPanel;
		
	}
	public void setupReadingsDisplayPanels() 
	{
		try
		{
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("General Interlocks", readingDeviceList, 0, 39));		//0
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("HVPS Status", readingDeviceList, 40, 66));			//1
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("HVPS Kept Errors", readingDeviceList, 67, 110));		//2
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("Bias", readingDeviceList, 111, 117));				//3
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("Water Pressure", readingDeviceList, 118, 119));		//4
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("Temperature", readingDeviceList, 120, 134));			//5
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("Flow Switches", readingDeviceList, 135, 138));		//6
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("Mixed", readingDeviceList, 139, 143));				//7
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("Panels", readingDeviceList, 144, 152));				//8
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("Interlock Rack", readingDeviceList, 153, 177));		//9
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("Trigger", readingDeviceList, 178, 183));				//10
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("States", readingDeviceList, 184, 186));				//11
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("HVPS", readingDeviceList, 187, 205));				//12
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("Mixed", readingDeviceList, 206, 208));				//13
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("Bias", readingDeviceList, 209, 211));				//14
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("Water", readingDeviceList, 212, 212));				//15
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("Temperature", readingDeviceList, 213, 217));			//16
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("Mixed", readingDeviceList, 218, 219));				//17
			byteDeviceReadingDisplayListCaptionPanelList.add(new ByteDeviceReadingDisplayListCaptionPanel("Samples", readingDeviceList, 220, 232));				//18
			VerticalPanel vp1 = new VerticalPanel();
			vp1.add(modulatorStateCaptionPanel());
			vp1.add(byteDeviceReadingDisplayListCaptionPanelList.get(11));
			vp1.add(byteDeviceReadingDisplayListCaptionPanelList.get(18));

			settingsReadingsHorizontalPanel.add(vp1);
			
			VerticalPanel interlockVerticalPanel1 = new VerticalPanel();
			interlockVerticalPanel1.add(byteDeviceReadingDisplayListCaptionPanelList.get(3));
			interlockVerticalPanel1.add(byteDeviceReadingDisplayListCaptionPanelList.get(4));
			interlockVerticalPanel1.add(byteDeviceReadingDisplayListCaptionPanelList.get(5));
			VerticalPanel interlockVerticalPanel2 = new VerticalPanel();
			interlockVerticalPanel2.add(byteDeviceReadingDisplayListCaptionPanelList.get(6));
			interlockVerticalPanel2.add(byteDeviceReadingDisplayListCaptionPanelList.get(7));
			interlockVerticalPanel2.add(byteDeviceReadingDisplayListCaptionPanelList.get(10));
			HorizontalPanel interlockHorizontalPanel = new HorizontalPanel();
			interlockHorizontalPanel.add(byteDeviceReadingDisplayListCaptionPanelList.get(0));
			interlockHorizontalPanel.add(interlockVerticalPanel1);
			interlockHorizontalPanel.add(interlockVerticalPanel2);
			modulatorInterLocksVerticalPanel1.add(interlockHorizontalPanel);
			
			HorizontalPanel interlockHorizontalPanel2 = new HorizontalPanel();
			interlockHorizontalPanel2.add(byteDeviceReadingDisplayListCaptionPanelList.get(8));
			interlockHorizontalPanel2.add(byteDeviceReadingDisplayListCaptionPanelList.get(9));
			modulatorInterLocksVerticalPanel2.add(interlockHorizontalPanel2);

			HorizontalPanel hvpsHorizontalPanel = new HorizontalPanel();
			hvpsHorizontalPanel.add(byteDeviceReadingDisplayListCaptionPanelList.get(12));
			hvpsHorizontalPanel.add(byteDeviceReadingDisplayListCaptionPanelList.get(1));
			hvpsHorizontalPanel.add(byteDeviceReadingDisplayListCaptionPanelList.get(2));
			modulatorHVPSVerticalPanel.add(hvpsHorizontalPanel);
			
			VerticalPanel readbackVerticalPanel1 = new VerticalPanel();
			readbackVerticalPanel1.add(byteDeviceReadingDisplayListCaptionPanelList.get(13));
			readbackVerticalPanel1.add(byteDeviceReadingDisplayListCaptionPanelList.get(14));
			readbackVerticalPanel1.add(byteDeviceReadingDisplayListCaptionPanelList.get(15));
			VerticalPanel readbackVerticalPanel2 = new VerticalPanel();
			readbackVerticalPanel2.add(byteDeviceReadingDisplayListCaptionPanelList.get(16));
			readbackVerticalPanel2.add(byteDeviceReadingDisplayListCaptionPanelList.get(17));
			HorizontalPanel readbackHorizontalPanel = new HorizontalPanel();
			readbackHorizontalPanel.add(readbackVerticalPanel1);
			readbackHorizontalPanel.add(readbackVerticalPanel2);
			modulatorReadbacksVerticalPanel.add(readbackHorizontalPanel);
		}
		catch (Exception e) {getStatusTextArea().addStatus("Error: " + e.getMessage());}
		
	}
	private CaptionPanel modulatorStateCaptionPanel()
	{
		String[] buttonLabel = {"Reset","Off","StandBy","On"};
		Grid modulatorStateGrid = new Grid(1, 4);
		HTMLTable.CellFormatter formatter = modulatorStateGrid.getCellFormatter();
		for (int ibut = 0; ibut < 4; ++ibut)
		{
			modStateButton[ibut] = new Button(buttonLabel[ibut]); 
			modStateButton[ibut].addClickHandler(new ModulatorButtonClickHandler(buttonLabel[ibut]));
			modStateButton[ibut].setEnabled(settingsPermitted);
			modulatorStateGrid.setWidget(0, ibut, modStateButton[ibut]);
			modStateButton[ibut].setWidth("100%");
			formatter.setHorizontalAlignment(0, ibut, HasHorizontalAlignment.ALIGN_CENTER);
			formatter.setVerticalAlignment(0, ibut, HasVerticalAlignment.ALIGN_MIDDLE);
			formatter.setWidth(0, ibut, "20%");
		}

		CaptionPanel modulatorStateCaptionPanel = new CaptionPanel("Modulator State");
		modulatorStateCaptionPanel.add(modulatorStateGrid);
		modulatorStateGrid.setWidth("100%");
		return modulatorStateCaptionPanel;
	}
	private void setModStateButtonsStatus()
	{
		if(!readyForData) return;
		try
		{
			for (int ib = 0; ib < 3; ++ib)
			{
				int state = Integer.parseInt(readingDeviceList.getDevice(184 + ib).getValue());
				modStateButton[ib + 1].setStyleName("modStateButton" + Integer.toString(state));
				modStateButton[ib + 1].setHeight("2.0em");
				boolean buttonEnabled = settingsPermitted;
				if (state == 3) buttonEnabled = false;
				modStateButton[ib + 1].setEnabled(buttonEnabled);
			}
		} catch (Exception e) {getStatusTextArea().addStatus("Error: " + e.getMessage());}
	}
	private void putSettings()
	{
		if (puttingSettingsState) return;
		if(readyForData)
		{
			getSetupApp().getMessageDialog().setImageUrl("images/wait.png");
			getSetupApp().getMessageDialog().setMessage("Meddelande", "Vänta - Ställer inställningar", false);
			puttingSettingsState = true;
			getStatusTextArea().addStatus("Putting Settings to modulator");
			for (int ii = 0; ii < settingDeviceDisplayList.size(); ++ii) settingDeviceDisplayList.get(ii).updateDeviceFromSettingDisplay();
			entryPointApp.mqttService.publishMessage("itsCernMod/set/mod", settingDeviceList.getByteArray(), settingsPermitted, getSetupApp().isDebug(), "ok", new PutModulatorSettingsAsyncCallback());
		}
	}
	private void changeState(boolean changeReset, int stateValue, boolean idleState) 
	{
		if (puttingSettingsState) return;
		if(!readyForData) return;
		puttingSettingsState = true;
		try
		{
			if (changeReset)
			{
				boolean reset = false;
				if (stateValue > 0) reset = true;
				settingDeviceDisplayList.get(0).getEnabledCheckBox().setValue(reset);
			}
			else
			{
				settingDeviceDisplayList.get(3).getSettingTextBox().setText(Integer.toString(stateValue));
			}
			for (int ii = 0; ii < settingDeviceDisplayList.size(); ++ii) settingDeviceDisplayList.get(ii).updateDeviceFromSettingDisplay();
			entryPointApp.mqttService.publishMessage("itsCernMod/set/mod", settingDeviceList.getByteArray(), settingsPermitted, getSetupApp().isDebug(), "ok", new StateChangeCallback(idleState, changeReset));
		} catch (Exception e){getStatusTextArea().addStatus(e.getMessage());}
	}
	private void enableInput(boolean enabled)
	{
		for (int ii = 0; ii < settingDeviceList.numDevices(); ++ ii) 
		{
			settingDeviceDisplayList.get(ii).getSettingTextBox().setEnabled(enabled);
		}
	}
	private void checkNumOfInterlockFaults() throws Exception
	{
		int numOfInterlockFaults = 0;
		for (int ii = 0; ii < readingDeviceList.numDevices(); ++ii)
		{
			ByteDevice byteDevice = readingDeviceList.getDevice(ii);
			if (readingDeviceList.getDevice(ii).getType().equals("bool"))
			{
				int val = Integer.parseInt(byteDevice.getValue());
				int min = Integer.parseInt(byteDevice.getMin());
				int max = Integer.parseInt(byteDevice.getMax());
				boolean interlockOk = true;
				if (val < min) interlockOk = false;
				if (val > max) interlockOk = false;
				if (!interlockOk)
				{
					++numOfInterlockFaults; 
//					getStatusTextArea().addStatus(byteDevice.getName() + " bad interlock " + Integer.toString(ii));
				}
			}
		}
		if (numOfInterlockFaults == 0)
		{
			badInterlockLabel.setStyleName("modInterlockLabelGood");
			badInterlockLabel.setText("Interlocks Clear");
		}
		else
		{
			badInterlockLabel.setStyleName("modInterlockLabelBad");
			badInterlockLabel.setText(Integer.toString(numOfInterlockFaults) + " Bad Interlocks");
		}

		return;
	}
	private class ModulatorButtonClickHandler  implements ClickHandler
	{
		private String buttonText = null;
		
		public ModulatorButtonClickHandler(String buttonText)
		{
			this.buttonText = buttonText;
		}
		
		@Override
		public void onClick(ClickEvent event) 
		{
			if (buttonText.equals("Reset"))
			{
				getStatusTextArea().addStatus("Resetting Modulator");
				changeState(true, 1, false);
			}
			if (buttonText.equals("Off"))
			{
				getStatusTextArea().addStatus("Turning Modulator Off");
				changeState(false, 0, false);
			}
			if (buttonText.equals("StandBy"))
			{
				getStatusTextArea().addStatus("Putting Modulator in Standby");
				changeState(false, 1, false);
			}
			if (buttonText.equals("On"))
			{
				settingDeviceDisplayList.get(3).getSettingTextBox().setText("3");
				getStatusTextArea().addStatus("Turning Modulator On");
				changeState(false, 3, false);
			}
		}

	}
	public class GetModulatorProtocolslAsyncCallback implements AsyncCallback<String[][]>
	{
		@Override
		public void onFailure(Throwable caught) 
		{
			getStatusTextArea().addStatus("Failure: Getting Protocol file");
			getStatusTextArea().addStatus(caught.getMessage());
		}
		@Override
		public void onSuccess(String[][] result) 
		{
			try 
			{
				getStatusTextArea().addStatus("Success: Getting Protocol file");
				settingDeviceList = new ByteDeviceList(result[0]);
				readingDeviceList = new ByteDeviceList(result[1]);
				initPanels();
				
			} catch (Exception e) 
			{
				getStatusTextArea().addStatus(e.getMessage());
			}
		}
	}
	public class PutModulatorSettingsAsyncCallback implements AsyncCallback<String>
	{
		@Override
		public void onFailure(Throwable caught) 
		{
			getSetupApp().getMessageDialog().hide();
			getStatusTextArea().addStatus("Failure: Putting Modulator Settings");
			getStatusTextArea().addStatus(caught.getMessage());
			puttingSettingsState = false;
		}
		@Override
		public void onSuccess(String result) 
		{
			try 
			{
				getSetupApp().getMessageDialog().hide();
				puttingSettingsState = false;
				getStatusTextArea().addStatus("Success: Putting Modulator Settings");
			} catch (Exception e) 
			{
				getStatusTextArea().addStatus(e.getMessage());
			}
		}

	}
	class ModulatorReadingsMqttData extends MqttData
	{

		public ModulatorReadingsMqttData() 
		{
			super(modReadingTopic, MqttData.BYTEDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			if (!readyForData) return;
			try 
			{
				readingDeviceList.putByteArray(this.getMessage());
				for (int ii = 0; ii < byteDeviceReadingDisplayListCaptionPanelList.size(); ++ii) 
				{
					byteDeviceReadingDisplayListCaptionPanelList.get(ii).updateReadingsDisplayFromDevices();
				}
				setModStateButtonsStatus();
				checkNumOfInterlockFaults();
				
			} catch (Exception e) 
			{
				getStatusTextArea().addStatus(e.getMessage());
			}
		}

	}
	class ModulatorSettingsMqttData extends MqttData
	{
		public ModulatorSettingsMqttData() 
		{
			super(modSettingTopic, MqttData.BYTEDATA, 1000, entryPointApp);
		}
		@Override
		public void doSomethingWithData() 
		{
			if (!readyForData) return;
			if (settingButtonGrid.isSettingsEnabled()) return;
			try 
			{
				settingDeviceList.putByteArray(this.getMessage());
				for (int ii = 0; ii < settingDeviceList.numDevices(); ++ ii) 
				{
//					modulatorSettingPanel.getStatusTextArea().addStatus(modulatorSettingPanel.settingDeviceList.getDevice(ii).getValue());
					settingDeviceDisplayList.get(ii).updateSettingFromDevice();
					
				}
			} catch (Exception e) 
			{
				getStatusTextArea().addStatus(e.getMessage());
			}
		}

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
				putSettings();
			} catch (Exception e) 
			{
				entryPointApp.setupApp.getStatusTextArea().addStatus(e.getMessage());
			}
		}
		
	}
	class StateChangeCallback extends Timer implements AsyncCallback<String> 
	{
		boolean idleState;
		boolean changeReset;
		
		StateChangeCallback(boolean idleState, boolean changeReset)
		{
			this.idleState = idleState;
			this.changeReset = changeReset;
		}

		@Override
		public void onFailure(Throwable caught) 
		{
			puttingSettingsState = false;
			getStatusTextArea().addStatus("Failure: Putting Modulator Settings");
			getStatusTextArea().addStatus(caught.getMessage());
		}

		@Override
		public void onSuccess(String result) 
		{
			if (!idleState)
			{
				this.schedule(1000);
			}
			else
			{
				puttingSettingsState = false;
			}
		}
		@Override
		public void run() 
		{
			puttingSettingsState = false;
			if (changeReset)
			{
				changeState(changeReset, 0, true);
			}
			else
			{
				changeState(changeReset, 4, true);
			}
			this.cancel();
			
		}
		
	}

}
