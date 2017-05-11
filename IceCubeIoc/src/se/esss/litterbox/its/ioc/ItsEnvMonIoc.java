package se.esss.litterbox.its.ioc;

public class ItsEnvMonIoc 
{

	public static void main(String[] args) throws Exception 
	{
		ItsGeigerIoc gioc = new ItsGeigerIoc("itsGeiger01Ioc", "itsmqttbroker.dat", "/dev/rfcomm1", 30);
//		ItsDht11Ioc dioc = new ItsDht11Ioc("itsDht1101Ioc", "itsmqttbroker.dat", "/dev/rfcomm2");
		ItsSolarMeterIoc sioc = new ItsSolarMeterIoc("itsSolarMeter01Ioc", "itsmqttbroker.dat", "/dev/rfcomm3", 30);

		gioc.setPeriodicPollPeriodmillis(2000);
//		dioc.setPeriodicPollPeriodmillis(2000);
		sioc.setPeriodicPollPeriodmillis(2000);

		gioc.startIoc("itsGeiger01/set/#", "itsGeiger01/get/cpm");
//		dioc.startIoc("itsDht1101/set/#", "itsDht1101/get/cond");
		sioc.startIoc("itsSolarMeter01/set/#", "itsSolarMeter01/get/cond");
	}

}
