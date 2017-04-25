package se.esss.litterbox.its.ioc;

import se.esss.litterbox.icecube.ioc.ItsByteGearBoxIoc;

public class ItsKlyPlcProtoCpuIoc 
{
	public static void main(String[] args) throws Exception 
	{
		String iocName = "itsKlyPlcProtoCpuIoc";
		int periodicPollPeriodmillis = 1000;
		String byteGearBoxUrl = "https://aig.esss.lu.se:8443/ItsByteGearBoxServer/gearbox/klyPlcProtoCpu.json";
		String gizmoInetAddress = "192.168.1.65";
		int gizmoPortNumber = 3000;
		String mqttBrokerInfoFilePath = "itsmqttbroker.dat";
		String byteGearBoxWebLoginInfo = "itsnetWebLoginInfo.dat";
		new ItsByteGearBoxIoc(iocName, periodicPollPeriodmillis, byteGearBoxUrl, gizmoInetAddress, gizmoPortNumber, mqttBrokerInfoFilePath, byteGearBoxWebLoginInfo);
	}

}
