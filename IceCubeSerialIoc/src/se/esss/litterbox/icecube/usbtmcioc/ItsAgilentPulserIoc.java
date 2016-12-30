package se.esss.litterbox.icecube.usbtmcioc;


public class ItsAgilentPulserIoc extends IceCubeUsbtmcIoc
{

	public ItsAgilentPulserIoc(String clientId, String brokerUrl, String brokerKey, String brokerSecret,String devNickName, int vendorId, int productId) throws Exception 
	{
		super(clientId, brokerUrl, brokerKey, brokerSecret, devNickName, vendorId, productId);
		// TODO Auto-generated constructor stub
	}
	@Override
	public byte[] getUsbtmcData() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void handleIncomingMessage(String topic, byte[] message) 
	{
		if (topic.indexOf("/set/init") >= 0)
		{
			writeUsbtmcData("OUTPUT1 0");
			writeUsbtmcData("OUTPUT2 0");
			writeUsbtmcData("*RST");
			writeUsbtmcData("OUTP OFF");
			writeUsbtmcData("SOURCE1:APPLY:PULSE 14,3,1.5");
			writeUsbtmcData("SOURCE1:FUNCTION:PULSE:WIDTH 0.001");
			writeUsbtmcData("SOURCE2:APPLY:PULSE 14,1,0.5");
			writeUsbtmcData("SOURCE2:FUNCTION:PULSE:WIDTH 0.0005");
			writeUsbtmcData("SOURCE2:PHASE:SYNCHRONIZE");
			writeUsbtmcData("OUTPUT1 1");
			writeUsbtmcData("OUTPUT2 1");
		}
	}
	public static void main(String[] args) throws Exception 
	{
		String userName = args[0];
		String password = args[1];
		String broker = "tcp://broker.shiftr.io:1883";
		ItsAgilentPulserIoc ioc = new ItsAgilentPulserIoc("itsAigPulse01Ioc", broker, userName, password, "Pulse Gen", 2391, 11271);
		ioc.setPeriodicPollPeriodmillis(2000);
		ioc.startIoc("itsAigPulse01/set/#", "itsAigPulse01/get");
	}
}
