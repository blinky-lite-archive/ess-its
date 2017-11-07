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
		String byteGearBoxURLString = "http://se-esss-litterbox.github.io/bytegearbox/klyPlcProtoCpu.json";
		String gizmoInetAddress = "192.168.1.65";
		int gizmoPortNumber = 3000;
		String mqttBrokerInfoFilePath = "itsmqttbroker.dat";
		new ItsKlyPlcProtoCpuIoc(iocName, periodicPollPeriodmillis, byteGearBoxURLString, gizmoInetAddress, gizmoPortNumber, mqttBrokerInfoFilePath, 30);
	}

}
