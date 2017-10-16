package se.esss.litterbox.its.ioc;

import org.json.simple.JSONObject;

import se.esss.litterbox.icecube.ioc.ItsByteGearBoxIoc;

public class ItsKlyPlcProtoPsuIoc extends ItsByteGearBoxIoc
{
	public ItsKlyPlcProtoPsuIoc(String clientId, int periodicPollPeriodmillis, String byteGearBoxUrl,
			String gizmoInetAddress, int gizmoPortNumber, String mqttBrokerInfoFilePath, int keepAliveInterval)
			throws Exception 
	{
		super(clientId, periodicPollPeriodmillis, byteGearBoxUrl, gizmoInetAddress, gizmoPortNumber, mqttBrokerInfoFilePath, keepAliveInterval);
	}
	@SuppressWarnings("unchecked")
	@Override
	public byte[] getDataFromGizmo() 
	{
		byte[] gizmoReadData = super.getDataFromGizmo();
		
		try
		{
			getByteGearBox().setReadData(gizmoReadData);
			JSONObject outputData = new JSONObject();
			outputData.put("filV", getByteGearBox().getByteGear("FILAMENT").getReadByteTooth("VMON").getValue());
			outputData.put("filI", getByteGearBox().getByteGear("FILAMENT").getReadByteTooth("IMON").getValue());
			outputData.put("filW", getByteGearBox().getByteGear("FILAMENT").getReadByteTooth("WMON").getValue());
			outputData.put("sol1V", getByteGearBox().getByteGear("SOLENOID1").getReadByteTooth("VMON").getValue());
			outputData.put("sol1I", getByteGearBox().getByteGear("SOLENOID1").getReadByteTooth("IMON").getValue());
			outputData.put("sol2V", getByteGearBox().getByteGear("SOLENOID2").getReadByteTooth("VMON").getValue());
			outputData.put("sol2I", getByteGearBox().getByteGear("SOLENOID2").getReadByteTooth("IMON").getValue());
			publishMessage(getByteGearBox().getTopic() + "/json01", outputData.toJSONString().getBytes(), this.getPublishQos(), true);	
			
		}catch (Exception e) {setStatus("Error: " + e.getMessage());}
		
		return gizmoReadData;
	}
	public static void main(String[] args) throws Exception 
	{
		String iocName = "itsKlyPlcProtoPsuIoc";
		int periodicPollPeriodmillis  = Integer.parseInt(args[0]);
		String byteGearBoxURLString = "http://se-esss-litterbox.github.io/bytegearbox/klyPlcProtoPsu.json";
		String gizmoInetAddress = "192.168.1.65";
		int gizmoPortNumber = 3003;
		String mqttBrokerInfoFilePath = "itsmqttbroker.dat";
		new ItsKlyPlcProtoPsuIoc(iocName, periodicPollPeriodmillis, byteGearBoxURLString, gizmoInetAddress, gizmoPortNumber, mqttBrokerInfoFilePath, 30);
	}

}
