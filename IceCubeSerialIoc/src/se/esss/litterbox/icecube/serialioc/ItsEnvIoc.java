package se.esss.litterbox.icecube.serialioc;

public class ItsEnvIoc 
{

	public static void main(String[] args) throws Exception 
	{
		ItsGeigerIoc gioc = new ItsGeigerIoc("itsGeiger01Ioc", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5", "/dev/rfcomm1");
		ItsDht11Ioc dioc = new ItsDht11Ioc("itsDht1101Ioc", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5", "/dev/rfcomm2");
		ItsSolarMeterIoc sioc = new ItsSolarMeterIoc("itsSolarMeter01Ioc", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5", "/dev/rfcomm3");

		gioc.setPeriodicPollPeriodmillis(2000);
		dioc.setPeriodicPollPeriodmillis(2000);
		sioc.setPeriodicPollPeriodmillis(2000);

		gioc.startIoc("itsGeiger01/set/#", "itsGeiger01/get/cpm");
		dioc.startIoc("itsDht1101/set/#", "itsDht1101/get/cond");
		sioc.startIoc("itsSolarMeter01/set/#", "itsSolarMeter01/get/cond");
	}

}
