package se.esss.litterbox.itsllrfgwt.client.contentpanels;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import se.esss.litterbox.itsllrfgwt.client.contentpanels.icecube.IceCubeSettingDisplay;
import se.esss.litterbox.itsllrfgwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.itsllrfgwt.client.gskel.GskelVerticalPanel;
import se.esss.litterbox.itsllrfgwt.shared.IceCubeDeviceList;

public class ModulatorSetupVerticalPanel extends GskelVerticalPanel 
{
	boolean superCreated = false;
	private IceCubeDeviceList settingDeviceList = null;
	private ArrayList<IceCubeSettingDisplay>  settingDeviceDisplayList = new ArrayList<IceCubeSettingDisplay>();
	boolean successfulSetup = false;

	public ModulatorSetupVerticalPanel(String tabTitle, GskelSetupApp setupApp) 
	{
		super(tabTitle, tabTitle, setupApp);
		getStatusTextArea().addStatus("Getting Setting Device Protocol file");
		getEntryPointAppService().getStringArrayFromProtocol(true, getSetupApp().isDebug(), new ArrayListFromProtocolAsyncCallback(this));
		HorizontalPanel settingsPanel = new HorizontalPanel();
		add(settingsPanel);
	}

	@Override
	public void tabLayoutPanelInterfaceAction(String message) 
	{
		if (!superCreated) return;
//		getStatusTextArea().addStatus("Tab " + this.getTabValue() + " " + message);
		upDateSettings();
	}

	@Override
	public void optionDialogInterfaceAction(String choiceButtonText) 
	{
		// TODO Auto-generated method stub

	}
	private void upDateSettings()
	{
		if(successfulSetup)
		{
			getStatusTextArea().addStatus("Getting Settings Echo");
			getEntryPointAppService().echoSettings(getSetupApp().isDebug(), new EchoSettingsAsyncCallback(this));

		}
	}
	private void setupSettingsDisplayPanel()
	{
		Grid settingGrid = new Grid(settingDeviceList.numDevices() + 1, 5);
		settingGrid.setWidget(0, 0, new Label("Name"));
		settingGrid.setWidget(0, 1, new Label("Min"));
		settingGrid.setWidget(0, 2, new Label("Value"));
		settingGrid.setWidget(0, 3, new Label("Max"));
		settingGrid.setWidget(0, 4, new Label("Comment"));
		for (int ii = 0; ii < settingDeviceList.numDevices(); ++ ii) 
		{
			try {settingDeviceDisplayList.add(new IceCubeSettingDisplay(settingDeviceList.getDevice(ii), settingGrid, ii + 1));} 
			catch (Exception e) {getStatusTextArea().addStatus(e.getMessage());}
		}
		
		HTMLTable.CellFormatter formatter = settingGrid.getCellFormatter();
		formatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);		
		add(settingGrid);
		
	}
	public void refreshSettingsDisplay()
	{
		for (int ii = 0; ii < settingDeviceDisplayList.size(); ++ii) settingDeviceDisplayList.get(ii).refreshSetting();
	}
	public static class ArrayListFromProtocolAsyncCallback implements AsyncCallback<String[]>
	{
		private ModulatorSetupVerticalPanel modulatorSetupVerticalPanel = null;
		public ArrayListFromProtocolAsyncCallback(ModulatorSetupVerticalPanel modulatorSetupVerticalPanel)
		{
			this.modulatorSetupVerticalPanel =  modulatorSetupVerticalPanel;
		}

		@Override
		public void onFailure(Throwable caught) 
		{
			modulatorSetupVerticalPanel.getStatusTextArea().addStatus("Failure: Getting Protocol file");
			modulatorSetupVerticalPanel.getStatusTextArea().addStatus(caught.getMessage());
			modulatorSetupVerticalPanel.successfulSetup = false;
			modulatorSetupVerticalPanel.superCreated = true;
		}

		@Override
		public void onSuccess(String[] result) 
		{
			try 
			{
				modulatorSetupVerticalPanel.getStatusTextArea().addStatus("Success: Getting Protocol file");
				modulatorSetupVerticalPanel.settingDeviceList = new IceCubeDeviceList(result);
				modulatorSetupVerticalPanel.successfulSetup = true;
				modulatorSetupVerticalPanel.superCreated = true;

//				modulatorSetupVerticalPanel.getStatusTextArea().addStatus(modulatorSetupVerticalPanel.settingDeviceList.getDevice("cathode voltage").csvLine());
				modulatorSetupVerticalPanel.setupSettingsDisplayPanel();
				modulatorSetupVerticalPanel.upDateSettings();
				
			} catch (Exception e) 
			{
				modulatorSetupVerticalPanel.getStatusTextArea().addStatus(e.getMessage());
			}
			
		}
		
	}
	public static class EchoSettingsAsyncCallback implements AsyncCallback<byte[]>
	{
		private ModulatorSetupVerticalPanel modulatorSetupVerticalPanel = null;
		public EchoSettingsAsyncCallback(ModulatorSetupVerticalPanel modulatorSetupVerticalPanel)
		{
			this.modulatorSetupVerticalPanel =  modulatorSetupVerticalPanel;
		}
		@Override
		public void onFailure(Throwable caught) 
		{
			modulatorSetupVerticalPanel.getStatusTextArea().addStatus("Failure: Getting Settings Echo");
			modulatorSetupVerticalPanel.getStatusTextArea().addStatus(caught.getMessage());
		}
		@Override
		public void onSuccess(byte[] result) 
		{
			try 
			{
				modulatorSetupVerticalPanel.getStatusTextArea().addStatus("Success: Getting Settings Echo");
				modulatorSetupVerticalPanel.settingDeviceList.putByteArray(result);
				modulatorSetupVerticalPanel.refreshSettingsDisplay();
//				modulatorSetupVerticalPanel.getStatusTextArea().addStatus(modulatorSetupVerticalPanel.settingDeviceList.getDevice("cathode voltage").csvLine());
			} catch (Exception e) 
			{
				modulatorSetupVerticalPanel.getStatusTextArea().addStatus(e.getMessage());
			}
			
		}
	}
}
