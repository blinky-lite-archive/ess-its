package se.esss.litterbox.icecube.serialioc;

import se.esss.litterbox.simplemqttclient.SimpleMqttSubscriber;

public class IceCubeSerialIocTester  extends SimpleMqttSubscriber
{

	public IceCubeSerialIocTester(String clientIdBase, String brokerUrl, String brokerKey, String brokerSecret) 
	{
		super(clientIdBase, brokerUrl, brokerKey, brokerSecret);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newMessage(String arg0, String arg1, byte[] arg2) 
	{
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) throws Exception 
	{
		IceCubeSerialIocTester ioCtester = new IceCubeSerialIocTester("ItsGeiger01IocTester", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5");
		boolean retained = true;
		String data = "99";
		ioCtester.publishMessage("its", "geiger01/set/avg", data.getBytes(), 0, retained);

	}

}
