package se.esss.litterbox.icecube.serialioc;

public class ItsGeigerIoc extends IceCubeSerialIoc
{
	public ItsGeigerIoc(String domain, String clientIdBase, String brokerUrl, String brokerKey, String brokerSecret, String serialPortName) throws Exception 
	{
		super(domain, clientIdBase, brokerUrl, brokerKey, brokerSecret, serialPortName);
	}
	@Override
	public byte[] getSerialData() 
	{
		String readData = writeReadSerialData("cpmGet", 10);
		int ispot = readData.indexOf("cpmGet");
		String data = "0";
		if (ispot >= 0)
		{
			data = readData.substring(6, readData.length());
			data = data.trim();
		}
		return data.getBytes();
	}
	@Override
	public void handleIncomingMessage(String topic, byte[] message) 
	{
	}
	public static void main(String[] args) throws Exception 
	{
		ItsGeigerIoc ioc = new ItsGeigerIoc("its", "ItsGeiger01Ioc", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5", "/dev/rfcomm1");
		ioc.startIoc("geiger01/set/#", "geiger01/get/cpm");
	}
}
