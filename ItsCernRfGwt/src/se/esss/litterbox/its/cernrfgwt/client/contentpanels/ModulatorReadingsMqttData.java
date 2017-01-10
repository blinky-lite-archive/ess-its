package se.esss.litterbox.its.cernrfgwt.client.contentpanels;

import se.esss.litterbox.its.cernrfgwt.client.EntryPointApp;
import se.esss.litterbox.its.cernrfgwt.client.mqttdata.MqttData;

public class ModulatorReadingsMqttData extends MqttData
{

	ModulatorSettingPanel modulatorSettingPanel;
	public ModulatorReadingsMqttData(String topic, int dataType, int timerPeriodMillis, EntryPointApp entryPointApp, ModulatorSettingPanel modulatorSettingPanel) 
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
			modulatorSettingPanel.readingDeviceList.putByteArray(this.getMessage());
			for (int ii = 0; ii < modulatorSettingPanel.byteDeviceReadingDisplayListCaptionPanelList.size(); ++ii) 
			{
				modulatorSettingPanel.byteDeviceReadingDisplayListCaptionPanelList.get(ii).updateReadingsDisplayFromDevices();
			}
			
		} catch (Exception e) 
		{
			modulatorSettingPanel.getStatusTextArea().addStatus(e.getMessage());
		}
	}

}
