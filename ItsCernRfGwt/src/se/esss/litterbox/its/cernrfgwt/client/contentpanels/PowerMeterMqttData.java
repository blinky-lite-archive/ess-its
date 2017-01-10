package se.esss.litterbox.its.cernrfgwt.client.contentpanels;

import se.esss.litterbox.its.cernrfgwt.client.EntryPointApp;
import se.esss.litterbox.its.cernrfgwt.client.mqttdata.MqttData;

public class PowerMeterMqttData extends MqttData
{

	public PowerMeterMqttData(String topic, int dataType, int timerPeriodMillis, EntryPointApp entryPointApp) 
	{
		super(topic, dataType, timerPeriodMillis, entryPointApp);
	}
	@Override
	public void doSomethingWithData() 
	{
//		getEntryPointApp().setupApp.getStatusTextArea().addStatus(getJsonData()[0][0] + " " + getJsonData()[0][1]);
		
	}

}
