package se.esss.litterbox.itsllrfgwt.client.contentpanels;

import java.util.ArrayList;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import se.esss.litterbox.itsllrfgwt.client.callbacks.GetModulatorProtocolslAsyncCallback;
import se.esss.litterbox.itsllrfgwt.client.callbacks.PutModulatorSettingsAsyncCallback;
import se.esss.litterbox.itsllrfgwt.client.callbacks.GetModulatorStateAsyncCallback;
import se.esss.litterbox.itsllrfgwt.client.contentpanels.icecube.IceCubeReadingDisplayListCaptionPanel;
import se.esss.litterbox.itsllrfgwt.client.contentpanels.icecube.IceCubeSettingDisplay;
import se.esss.litterbox.itsllrfgwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.itsllrfgwt.client.gskel.GskelVerticalPanel;
import se.esss.litterbox.itsllrfgwt.client.handlers.ModulatorButtonClickHandler;
import se.esss.litterbox.itsllrfgwt.shared.IceCubeDeviceList;

public class ModulatorSetupVerticalPanel extends GskelVerticalPanel 
{
	private boolean panelCreated = false;
	private boolean creatingPanel = false;
	private boolean successfulSetup = false;
	private boolean gettingModulatorState = false;
	private boolean puttingSettingsState = false;
	private boolean settingsPermitted = false;
	
	private String styleName = "modulatorSetupPanel";
	private IceCubeDeviceList settingDeviceList = null;
	private IceCubeDeviceList readingDeviceList = null;
	private ArrayList<IceCubeSettingDisplay>  settingDeviceDisplayList = new ArrayList<IceCubeSettingDisplay>();
	private ArrayList<IceCubeReadingDisplayListCaptionPanel>  iceCubeReadingDisplayListCaptionPanelList = new ArrayList<IceCubeReadingDisplayListCaptionPanel>();
	private HorizontalPanel settingsReadingsHorizontalPanel;
	private ModulatorDisplayVerticalPanel modulatorInterLocksVerticalPanel1;
	private ModulatorDisplayVerticalPanel modulatorInterLocksVerticalPanel2;
	private ModulatorDisplayVerticalPanel modulatorHVPSVerticalPanel;
	private ModulatorDisplayVerticalPanel modulatorReadbacksVerticalPanel;
	private Button[] modStateButton = new Button[4];
	private Button setModulatorSettingsButton;

	public boolean isGettingModulatorState() {return gettingModulatorState;}
	public boolean isPanelCreated() {return panelCreated;}
	public boolean isCreatingPanel() {return creatingPanel;}
	public boolean isSuccessfulSetup() {return successfulSetup;}
	public boolean isPuttingSettingsState() {return puttingSettingsState;}
	public boolean isSettingsPermitted() {return settingsPermitted;}
	public String getStyleName() {return styleName;}
	public ModulatorDisplayVerticalPanel getModulatorReadbacksVerticalPanel() {return modulatorReadbacksVerticalPanel;}
	public ModulatorDisplayVerticalPanel getModulatorHVPSVerticalPanel() {return modulatorHVPSVerticalPanel;}
	public ModulatorDisplayVerticalPanel getModulatorInterLocksVerticalPanel1() {return modulatorInterLocksVerticalPanel1;}
	public ModulatorDisplayVerticalPanel getModulatorInterLocksVerticalPanel2() {return modulatorInterLocksVerticalPanel2;}
	public HorizontalPanel getSettingsReadingsHorizontalPanel() {return settingsReadingsHorizontalPanel;}
	public IceCubeDeviceList getSettingDeviceList() {return settingDeviceList;}
	public IceCubeDeviceList getReadingDeviceList() {return readingDeviceList;}
	public ArrayList<IceCubeSettingDisplay> getSettingDeviceDisplayList() {return settingDeviceDisplayList;}
	public Button[] getModStateButton() {return modStateButton;}
	public Button getSetModulatorSettingsButton() {return setModulatorSettingsButton;}

	public void setGettingModulatorState(boolean gettingModulatorState) {this.gettingModulatorState = gettingModulatorState;}
	public void setPanelCreated(boolean panelCreated) {this.panelCreated = panelCreated;}
	public void setCreatingPanel(boolean creatingPanel) {this.creatingPanel = creatingPanel;}
	public void setSuccessfulSetup(boolean successfulSetup) {this.successfulSetup = successfulSetup;}
	public void setPuttingSettingsState(boolean puttingSettingsState) {this.puttingSettingsState = puttingSettingsState;}
	public void setSettingsPermitted(boolean settingsPermitted) {this.settingsPermitted = settingsPermitted;}
	public void setSettingDeviceList(IceCubeDeviceList settingDeviceList) {this.settingDeviceList = settingDeviceList;}
	public void setReadingDeviceList(IceCubeDeviceList readingDeviceList) {this.readingDeviceList = readingDeviceList;}
	public void setSettingDeviceDisplayList(ArrayList<IceCubeSettingDisplay> settingDeviceDisplayList) {this.settingDeviceDisplayList = settingDeviceDisplayList;}
	public void setModulatorInterLocksVerticalPanel1(ModulatorDisplayVerticalPanel modulatorInterLocksVerticalPanel1) {this.modulatorInterLocksVerticalPanel1 = modulatorInterLocksVerticalPanel1;}
	public void setModulatorInterLocksVerticalPanel2(ModulatorDisplayVerticalPanel modulatorInterLocksVerticalPanel2) {this.modulatorInterLocksVerticalPanel2 = modulatorInterLocksVerticalPanel2;}
	public void setModulatorHVPSVerticalPanel(ModulatorDisplayVerticalPanel modulatorHVPSVerticalPanel) {this.modulatorHVPSVerticalPanel = modulatorHVPSVerticalPanel;}
	public void setModulatorReadbacksVerticalPanel(ModulatorDisplayVerticalPanel modulatorReadbacksVerticalPanel) {this.modulatorReadbacksVerticalPanel = modulatorReadbacksVerticalPanel;}

	public ModulatorSetupVerticalPanel(String tabTitle, GskelSetupApp setupApp, boolean settingsPermitted) 
	{
		super(tabTitle, tabTitle, setupApp);
		this.getGskelTabLayoutScrollPanel().setStyleName(styleName);
		this.settingsPermitted = settingsPermitted;
		ReadSetttingsTimer rst = new ReadSetttingsTimer();
		rst.scheduleRepeating(1000);
	}

	@Override
	public void tabLayoutPanelInterfaceAction(String message) 
	{
		if (!panelCreated) 
		{
			initializePanel();
		}
		else
		{
			updateSettingsFromReadbacks();
		}
	}

	@Override
	public void optionDialogInterfaceAction(String choiceButtonText) 
	{
		// TODO Auto-generated method stub

	}
	public void initializePanel()
	{
		if (creatingPanel) return;
		if (!panelCreated)
		{
			creatingPanel = true;
			getStatusTextArea().addStatus("Setting up Modulator Panel");
			getStatusTextArea().addStatus("Getting Setting Device Protocol file");
			getEntryPointAppService().getModulatorProtocols(getSetupApp().isDebug(), new GetModulatorProtocolslAsyncCallback(this));
			settingsReadingsHorizontalPanel = new HorizontalPanel();
			add(settingsReadingsHorizontalPanel);
			Window.addResizeHandler(new ModulatorSetupVerticalPanelResizeHandler());
		}
	}
	public void getModulatorState()
	{
		if (gettingModulatorState) return;
		if(successfulSetup)
		{
//			getSetupApp().getMessageDialog().setImageUrl("images/wait.png");
//			this.getSetupApp().getMessageDialog().setMessage("Meddelande", "Vänta - Hämtar inställningar", false);
			gettingModulatorState = true;
//			getStatusTextArea().addStatus("Getting last known modulator state");
			getEntryPointAppService().getModulatorState(getSetupApp().isDebug(), new GetModulatorStateAsyncCallback(this));

		}
	}
	public void putSettings()
	{
		if (puttingSettingsState) return;
		if(successfulSetup)
		{
			getSetupApp().getMessageDialog().setImageUrl("images/wait.png");
			getSetupApp().getMessageDialog().setMessage("Meddelande", "Vänta - Ställer inställningar", false);
			puttingSettingsState = true;
			getStatusTextArea().addStatus("Putting Settings to modulator");
			for (int ii = 0; ii < settingDeviceDisplayList.size(); ++ii) settingDeviceDisplayList.get(ii).updateDeviceFromSettingDisplay();
			getEntryPointAppService().putModulatorSettings(getSettingDeviceList().getByteArray(), getSetupApp().isDebug(), new PutModulatorSettingsAsyncCallback(this));
		}
	}
	public CaptionPanel settingsCaptionPanel()
	{
		Grid settingGrid = new Grid(settingDeviceList.numDevices() + 1, 6);
		settingGrid.setWidget(0, 0, new Label("Name"));
		settingGrid.setWidget(0, 1, new Label("Min"));
		settingGrid.setWidget(0, 2, new Label("Value"));
		settingGrid.setWidget(0, 3, new Label("Readback"));
		settingGrid.setWidget(0, 4, new Label("Max"));
		settingGrid.setWidget(0, 5, new Label("Comment"));
		for (int ii = 0; ii < settingDeviceList.numDevices(); ++ ii) 
		{
			try {settingDeviceDisplayList.add(new IceCubeSettingDisplay(settingDeviceList.getDevice(ii), settingGrid, ii + 1));} 
			catch (Exception e) {getStatusTextArea().addStatus(e.getMessage());}
		}
		
		HTMLTable.CellFormatter formatter = settingGrid.getCellFormatter();
		formatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);	

		setModulatorSettingsButton = new Button("Set");
		setModulatorSettingsButton.addClickHandler(new ModulatorButtonClickHandler("Set", this));
		setModulatorSettingsButton.setEnabled(settingsPermitted);

		setModulatorSettingsButton.setWidth("10.0em");
		HorizontalPanel buttonPanel = new HorizontalPanel();

		buttonPanel.setWidth("100%");
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		buttonPanel.add(setModulatorSettingsButton);
		
		CaptionPanel actionsCaptionPanel = new CaptionPanel("Actions");
		actionsCaptionPanel.add(buttonPanel);
		CaptionPanel settingsCaptionPanel = new CaptionPanel("Settings");
		settingsCaptionPanel.add(settingGrid);
		VerticalPanel vp1 = new VerticalPanel();
		vp1.add(actionsCaptionPanel);
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
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("General Interlocks", readingDeviceList, 0, 39));	//0
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("HVPS Status", readingDeviceList, 40, 66));			//1
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("HVPS Kept Errors", readingDeviceList, 67, 110));	//2
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("Bias", readingDeviceList, 111, 117));				//3
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("Water Pressure", readingDeviceList, 118, 119));	//4
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("Temperature", readingDeviceList, 120, 134));		//5
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("Flow Switches", readingDeviceList, 135, 138));		//6
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("Mixed", readingDeviceList, 139, 143));				//7
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("Panels", readingDeviceList, 144, 152));			//8
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("Interlock Rack", readingDeviceList, 153, 177));	//9
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("Trigger", readingDeviceList, 178, 183));			//10
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("States", readingDeviceList, 184, 186));			//11
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("HVPS", readingDeviceList, 187, 205));				//12
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("Mixed", readingDeviceList, 206, 208));				//13
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("Bias", readingDeviceList, 209, 211));				//14
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("Water", readingDeviceList, 212, 212));				//15
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("Temperature", readingDeviceList, 213, 217));		//16
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("Mixed", readingDeviceList, 218, 219));				//17
			iceCubeReadingDisplayListCaptionPanelList.add(new IceCubeReadingDisplayListCaptionPanel("Samples", readingDeviceList, 220, 232));			//18
			VerticalPanel vp1 = new VerticalPanel();
			vp1.add(modulatorStateCaptionPanel());
			vp1.add(iceCubeReadingDisplayListCaptionPanelList.get(11));
			vp1.add(iceCubeReadingDisplayListCaptionPanelList.get(18));

			settingsReadingsHorizontalPanel.add(vp1);
			
			VerticalPanel interlockVerticalPanel1 = new VerticalPanel();
			interlockVerticalPanel1.add(iceCubeReadingDisplayListCaptionPanelList.get(3));
			interlockVerticalPanel1.add(iceCubeReadingDisplayListCaptionPanelList.get(4));
			interlockVerticalPanel1.add(iceCubeReadingDisplayListCaptionPanelList.get(5));
			VerticalPanel interlockVerticalPanel2 = new VerticalPanel();
			interlockVerticalPanel2.add(iceCubeReadingDisplayListCaptionPanelList.get(6));
			interlockVerticalPanel2.add(iceCubeReadingDisplayListCaptionPanelList.get(7));
			interlockVerticalPanel2.add(iceCubeReadingDisplayListCaptionPanelList.get(10));
			HorizontalPanel interlockHorizontalPanel = new HorizontalPanel();
			interlockHorizontalPanel.add(iceCubeReadingDisplayListCaptionPanelList.get(0));
			interlockHorizontalPanel.add(interlockVerticalPanel1);
			interlockHorizontalPanel.add(interlockVerticalPanel2);
			modulatorInterLocksVerticalPanel1.add(interlockHorizontalPanel);
			
			HorizontalPanel interlockHorizontalPanel2 = new HorizontalPanel();
			interlockHorizontalPanel2.add(iceCubeReadingDisplayListCaptionPanelList.get(8));
			interlockHorizontalPanel2.add(iceCubeReadingDisplayListCaptionPanelList.get(9));
			modulatorInterLocksVerticalPanel2.add(interlockHorizontalPanel2);

			HorizontalPanel hvpsHorizontalPanel = new HorizontalPanel();
			hvpsHorizontalPanel.add(iceCubeReadingDisplayListCaptionPanelList.get(12));
			hvpsHorizontalPanel.add(iceCubeReadingDisplayListCaptionPanelList.get(1));
			hvpsHorizontalPanel.add(iceCubeReadingDisplayListCaptionPanelList.get(2));
			modulatorHVPSVerticalPanel.add(hvpsHorizontalPanel);
			
			VerticalPanel readbackVerticalPanel1 = new VerticalPanel();
			readbackVerticalPanel1.add(iceCubeReadingDisplayListCaptionPanelList.get(13));
			readbackVerticalPanel1.add(iceCubeReadingDisplayListCaptionPanelList.get(14));
			readbackVerticalPanel1.add(iceCubeReadingDisplayListCaptionPanelList.get(15));
			VerticalPanel readbackVerticalPanel2 = new VerticalPanel();
			readbackVerticalPanel2.add(iceCubeReadingDisplayListCaptionPanelList.get(16));
			readbackVerticalPanel2.add(iceCubeReadingDisplayListCaptionPanelList.get(17));
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
			modStateButton[ibut].addClickHandler(new ModulatorButtonClickHandler(buttonLabel[ibut], this));
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
	public void updateSettingReadbacksFromDevices()
	{
		for (int ii = 0; ii < settingDeviceDisplayList.size(); ++ii) 
		{
			settingDeviceDisplayList.get(ii).updateSettingReadbackFromDevice();
		}
	}
	public void updateSettingsFromReadbacks()
	{
		for (int ii = 0; ii < settingDeviceDisplayList.size(); ++ii) 
		{
			settingDeviceDisplayList.get(ii).updateSettingFromReadback();
		}
	}
	public void updateReadingsDisplayFromDevices()
	{
		for (int ii = 0; ii < iceCubeReadingDisplayListCaptionPanelList.size(); ++ii) 
		{
			iceCubeReadingDisplayListCaptionPanelList.get(ii).updateReadingsDisplayFromDevices();
		}
	}
	public class ModulatorSetupVerticalPanelResizeHandler implements ResizeHandler
	{
		@Override
		public void onResize(ResizeEvent event) 
		{
			updateSettingsFromReadbacks();
		}
	}
	public class ReadSetttingsTimer extends Timer
	{
		@Override
		public void run() 
		{
			if (puttingSettingsState) return;
			getModulatorState();
		}
		
	}
}
