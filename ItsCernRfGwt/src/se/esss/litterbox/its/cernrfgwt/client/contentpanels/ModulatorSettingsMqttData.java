package se.esss.litterbox.its.cernrfgwt.client.contentpanels;

import se.esss.litterbox.its.cernrfgwt.client.EntryPointApp;
import se.esss.litterbox.its.cernrfgwt.client.mqttdata.MqttData;

public class ModulatorSettingsMqttData extends MqttData
{
	ModulatorSettingPanel modulatorSettingPanel;
	public ModulatorSettingsMqttData(String topic, int dataType, int timerPeriodMillis, EntryPointApp entryPointApp, ModulatorSettingPanel modulatorSettingPanel) 
	{
		super(topic, dataType, timerPeriodMillis, entryPointApp);
		this.modulatorSettingPanel = modulatorSettingPanel;
	}
	@Override
	public void doSomethingWithData() 
	{
		if (!modulatorSettingPanel.readyForData) return;
		try 
		{
			modulatorSettingPanel.settingDeviceList.putByteArray(this.getMessage());
			for (int ii = 0; ii < modulatorSettingPanel.settingDeviceList.numDevices(); ++ ii) 
			{
//				modulatorSettingPanel.getStatusTextArea().addStatus(modulatorSettingPanel.settingDeviceList.getDevice(ii).getValue());
				modulatorSettingPanel.settingDeviceDisplayList.get(ii).updateSettingReadbackFromDevice();
				if (modulatorSettingPanel.resetSettings)
				{
					modulatorSettingPanel.settingDeviceDisplayList.get(ii).updateSettingFromReadback();
				}
				modulatorSettingPanel.settingDeviceDisplayList.get(ii).checkReadingMatchSetting();
				
			}
			modulatorSettingPanel.resetSettings = false;
		} catch (Exception e) 
		{
			modulatorSettingPanel.getStatusTextArea().addStatus(e.getMessage());
		}
	}

}
