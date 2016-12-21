package se.esss.litterbox.icecube.serialioc;

public class ItsGeigerDht11SolarIoc 
{

	public static void main(String[] args) throws Exception 
	{
		ItsGeigerIoc gioc = new ItsGeigerIoc("its", "ItsGeiger01Ioc", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5", "/dev/rfcomm1");
		ItsDht11Ioc dioc = new ItsDht11Ioc("its", "ItsDht1101Ioc", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5", "/dev/rfcomm2");
		ItsSolarMeterIoc sioc = new ItsSolarMeterIoc("its", "ItsSolarMeter01Ioc", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5", "/dev/rfcomm3");

		gioc.setPeriodicPollPeriodmillis(2000);
		dioc.setPeriodicPollPeriodmillis(2000);
		sioc.setPeriodicPollPeriodmillis(2000);

		gioc.startIoc("geiger01/set/#", "geiger01/get/cpm");
		dioc.startIoc("dht1101/set/#", "dht1101/get/cond");
		sioc.startIoc("solarMeter01/set/#", "solarMeter01/get/cond");
	}

}
