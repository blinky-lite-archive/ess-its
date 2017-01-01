package se.esss.litterbox.its.ioc;

public class ItsEnvIoc 
{

	public static void main(String[] args) throws Exception 
	{
		String userName = args[0];
		String password = args[1];
		String broker = "tcp://broker.shiftr.io:1883";
		ItsGeigerIoc gioc = new ItsGeigerIoc("itsGeiger01Ioc", broker, userName, password, "/dev/rfcomm1");
		ItsDht11Ioc dioc = new ItsDht11Ioc("itsDht1101Ioc", broker, userName, password, "/dev/rfcomm2");
		ItsSolarMeterIoc sioc = new ItsSolarMeterIoc("itsSolarMeter01Ioc", broker, userName, password, "/dev/rfcomm3");

		gioc.setPeriodicPollPeriodmillis(2000);
		dioc.setPeriodicPollPeriodmillis(2000);
		sioc.setPeriodicPollPeriodmillis(2000);

		gioc.startIoc("itsGeiger01/set/#", "itsGeiger01/get/cpm");
		dioc.startIoc("itsDht1101/set/#", "itsDht1101/get/cond");
		sioc.startIoc("itsSolarMeter01/set/#", "itsSolarMeter01/get/cond");
	}

}
