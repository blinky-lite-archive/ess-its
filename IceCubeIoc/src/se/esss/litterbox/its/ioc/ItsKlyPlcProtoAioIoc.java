package se.esss.litterbox.its.ioc;

import se.esss.litterbox.icecube.ioc.ItsByteGearBoxIoc;

public class ItsKlyPlcProtoAioIoc extends ItsByteGearBoxIoc
{

	public ItsKlyPlcProtoAioIoc(String clientId, int periodicPollPeriodmillis, String byteGearBoxUrl,
			String gizmoInetAddress, int gizmoPortNumber, String mqttBrokerInfoFilePath)
			throws Exception 
	{
		super(clientId, periodicPollPeriodmillis, byteGearBoxUrl, gizmoInetAddress, gizmoPortNumber, mqttBrokerInfoFilePath);
	}
	@Override
	public void lostMqttConnection(Throwable arg0) 
	{
		try {reconnect();} catch (Exception e) {setStatus("Error on reconnect: " + arg0.getMessage());}
	}
	public static void main(String[] args) throws Exception 
	{
		String iocName = "itsKlyPlcProtoAioIoc";
		int periodicPollPeriodmillis = Integer.parseInt(args[0]);
		String byteGearBoxUrl = "https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/gearbox/klyPlcProtoAio.json";
		String gizmoInetAddress = "192.168.1.65";
		int gizmoPortNumber = 3001;
		String mqttBrokerInfoFilePath = "itsmqttbroker.dat";
		new ItsKlyPlcProtoAioIoc(iocName, periodicPollPeriodmillis, byteGearBoxUrl, gizmoInetAddress, gizmoPortNumber, mqttBrokerInfoFilePath);
	}


}
