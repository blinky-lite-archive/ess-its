package se.esss.litterbox.its.ioc;

import org.json.simple.JSONObject;

import se.esss.litterbox.icecube.ioc.ItsByteGearBoxIoc;

public class ItsKlyPlcProtoAioIoc extends ItsByteGearBoxIoc
{

	public ItsKlyPlcProtoAioIoc(String clientId, int periodicPollPeriodmillis, String byteGearBoxUrl,
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
			outputData.put("ipI", getByteGearBox().getByteGear("KLY_IP_ISn_Current").getReadByteTooth("EGU").getValue());
			outputData.put("oilSurT", getByteGearBox().getByteGear("KLY_Oil_TSn_SurfTemp").getReadByteTooth("EGU").getValue());
			outputData.put("solInT", getByteGearBox().getByteGear("KLY_Sol_TSn_WatInletTemp").getReadByteTooth("EGU").getValue());
			outputData.put("solOutT", getByteGearBox().getByteGear("KLY_Sol_TSn_WatOutletTemp").getReadByteTooth("EGU").getValue());
			outputData.put("solSurT", getByteGearBox().getByteGear("KLY_Sol_TSn_SurfTemp").getReadByteTooth("EGU").getValue());
			outputData.put("winOutT", getByteGearBox().getByteGear("KLY_Win_TSn_WatOutletTemp").getReadByteTooth("EGU").getValue());
			outputData.put("winSlvT", getByteGearBox().getByteGear("KLY_Win_TSn_WatOutletSleeveTemp").getReadByteTooth("EGU").getValue());
			outputData.put("collTopT", getByteGearBox().getByteGear("KLY_Coll_TSn_TopTemp").getReadByteTooth("EGU").getValue());
			outputData.put("colEdgeT", getByteGearBox().getByteGear("KLY_Coll_TSn_EdgeTemp").getReadByteTooth("EGU").getValue());
			outputData.put("bodyInT", getByteGearBox().getByteGear("KLY_Body_TSn_WatInletTemp").getReadByteTooth("EGU").getValue());
			outputData.put("bodyOutT", getByteGearBox().getByteGear("KLY_Body_TSn_WatOutletTemp").getReadByteTooth("EGU").getValue());
			publishMessage(getByteGearBox().getTopic() + "/json01", outputData.toJSONString().getBytes(), this.getPublishQos(), true);	
			
		}catch (Exception e) {setStatus("Error: " + e.getMessage());}
		
		return gizmoReadData;
	}
	public static void main(String[] args) throws Exception 
	{
		String iocName = "itsKlyPlcProtoAioIoc";
		int periodicPollPeriodmillis = Integer.parseInt(args[0]);
		String byteGearBoxFilePath = "klyPlcProtoAio.json";
		String gizmoInetAddress = "192.168.1.65";
		int gizmoPortNumber = 3001;
		String mqttBrokerInfoFilePath = "itsmqttbroker.dat";
		new ItsKlyPlcProtoAioIoc(iocName, periodicPollPeriodmillis, byteGearBoxFilePath, gizmoInetAddress, gizmoPortNumber, mqttBrokerInfoFilePath, 30);
	}


}
