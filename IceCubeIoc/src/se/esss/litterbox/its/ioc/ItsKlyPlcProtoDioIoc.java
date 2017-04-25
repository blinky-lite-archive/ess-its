package se.esss.litterbox.its.ioc;

import se.esss.litterbox.icecube.ioc.ItsByteGearBoxIoc;

public class ItsKlyPlcProtoDioIoc 
{

	public static void main(String[] args) throws Exception 
	{
		String iocName = "itsKlyPlcProtoDioIoc";
		int periodicPollPeriodmillis = 1000;
		String byteGearBoxUrl = "https://aig.esss.lu.se:8443/ItsByteGearBoxServer/gearbox/klyPlcProtoDio.json";
		String gizmoInetAddress = "192.168.1.65";
		int gizmoPortNumber = 3002;
		String mqttBrokerInfoFilePath = "itsmqttbroker.dat";
		String byteGearBoxWebLoginInfo = "itsnetWebLoginInfo.dat";
		new ItsByteGearBoxIoc(iocName, periodicPollPeriodmillis, byteGearBoxUrl, gizmoInetAddress, gizmoPortNumber, mqttBrokerInfoFilePath, byteGearBoxWebLoginInfo);
	}

}
