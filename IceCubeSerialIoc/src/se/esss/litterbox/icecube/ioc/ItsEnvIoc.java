package se.esss.litterbox.icecube.ioc;

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
		ItsClkReceiverIoc crioc = new ItsClkReceiverIoc("itsClkRecvr01Ioc", broker, userName, password, "/dev/ttyACM0");
		ItsPowerMeterIoc pmioc = new ItsPowerMeterIoc("itsPowerMeter01Ioc", broker, userName, password, "Power Meter", 2733, 27);
		ItsRfSigGenIoc rfioc = new ItsRfSigGenIoc("itsRfSigGen01Ioc", broker, userName, password, "RF Sig Gen", 2733, 72);

		gioc.setPeriodicPollPeriodmillis(2000);
		dioc.setPeriodicPollPeriodmillis(2000);
		sioc.setPeriodicPollPeriodmillis(2000);
		crioc.setPeriodicPollPeriodmillis(1000);
		pmioc.setPeriodicPollPeriodmillis(1000);
		rfioc.setPeriodicPollPeriodmillis(1000);

		gioc.startIoc("itsGeiger01/set/#", "itsGeiger01/get/cpm");
		dioc.startIoc("itsDht1101/set/#", "itsDht1101/get/cond");
		sioc.startIoc("itsSolarMeter01/set/#", "itsSolarMeter01/get/cond");
		crioc.startIoc("itsClkRecvr01/set/#", "itsClkRecvr01/get/signal");
		pmioc.startIoc("itsPowerMeter01/set/#", "itsPowerMeter01/get");
		rfioc.startIoc("itsRfSigGen01/set/#", "itsRfSigGen01/get");
	}

}
