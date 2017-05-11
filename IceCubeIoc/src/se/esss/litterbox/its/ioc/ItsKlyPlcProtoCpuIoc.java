package se.esss.litterbox.its.ioc;

import se.esss.litterbox.icecube.ioc.ItsByteGearBoxIoc;

public class ItsKlyPlcProtoCpuIoc extends ItsByteGearBoxIoc
{
	public ItsKlyPlcProtoCpuIoc(String clientId, int periodicPollPeriodmillis, String byteGearBoxUrl,
			String gizmoInetAddress, int gizmoPortNumber, String mqttBrokerInfoFilePath, int keepAliveInterval)
			throws Exception 
	{
		super(clientId, periodicPollPeriodmillis, byteGearBoxUrl, gizmoInetAddress, gizmoPortNumber, mqttBrokerInfoFilePath, keepAliveInterval);
	}
	public static void main(String[] args) throws Exception 
	{
		String iocName = "itsKlyPlcProtoCpuIoc";
		int periodicPollPeriodmillis = Integer.parseInt(args[0]);
		String byteGearBoxUrl = "https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/gearbox/klyPlcProtoCpu.json";
		String gizmoInetAddress = "192.168.1.65";
		int gizmoPortNumber = 3000;
		String mqttBrokerInfoFilePath = "itsmqttbroker.dat";
		new ItsKlyPlcProtoCpuIoc(iocName, periodicPollPeriodmillis, byteGearBoxUrl, gizmoInetAddress, gizmoPortNumber, mqttBrokerInfoFilePath, 1);
	}

}
