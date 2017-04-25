package se.esss.litterbox.its.ioc;

import se.esss.litterbox.icecube.ioc.ItsByteGearBoxIoc;

public class ItsKlyPlcProtoPsuIoc 
{

	public static void main(String[] args) throws Exception 
	{
		String iocName = "itsKlyPlcProtoPsuIoc";
		int periodicPollPeriodmillis = 1000;
		String byteGearBoxUrl = "https://aig.esss.lu.se:8443/ItsByteGearBoxServer/gearbox/klyPlcProtoPsu.json";
		String gizmoInetAddress = "192.168.1.65";
		int gizmoPortNumber = 3003;
		String mqttBrokerInfoFilePath = "itsmqttbroker.dat";
		String byteGearBoxWebLoginInfo = "itsnetWebLoginInfo.dat";
		new ItsByteGearBoxIoc(iocName, periodicPollPeriodmillis, byteGearBoxUrl, gizmoInetAddress, gizmoPortNumber, mqttBrokerInfoFilePath, byteGearBoxWebLoginInfo);
	}

}
