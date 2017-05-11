package se.esss.litterbox.its.ioc;

import se.esss.litterbox.icecube.ioc.ItsByteGearBoxIoc;

public class ItsKlyPlcProtoPsuIoc extends ItsByteGearBoxIoc
{
	public ItsKlyPlcProtoPsuIoc(String clientId, int periodicPollPeriodmillis, String byteGearBoxUrl,
			String gizmoInetAddress, int gizmoPortNumber, String mqttBrokerInfoFilePath, int keepAliveInterval)
			throws Exception 
	{
		super(clientId, periodicPollPeriodmillis, byteGearBoxUrl, gizmoInetAddress, gizmoPortNumber, mqttBrokerInfoFilePath, keepAliveInterval);
	}

	public static void main(String[] args) throws Exception 
	{
		String iocName = "itsKlyPlcProtoPsuIoc";
		int periodicPollPeriodmillis  = Integer.parseInt(args[0]);
		String byteGearBoxUrl = "https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/gearbox/klyPlcProtoPsu.json";
		String gizmoInetAddress = "192.168.1.65";
		int gizmoPortNumber = 3003;
		String mqttBrokerInfoFilePath = "itsmqttbroker.dat";
		new ItsKlyPlcProtoPsuIoc(iocName, periodicPollPeriodmillis, byteGearBoxUrl, gizmoInetAddress, gizmoPortNumber, mqttBrokerInfoFilePath, 30);
	}

}
