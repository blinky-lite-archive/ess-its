package se.esss.litterbox.its.ioc;


import se.esss.litterbox.icecube.ioc.tcp.IceCubeTcpIoc;

public class ItsCernModulatorIoc extends IceCubeTcpIoc
{
	private byte[] settingsArray = null;
	private int numReadBytes = 118;

	public ItsCernModulatorIoc(String clientId, String mqttBrokerInfoFilePath, String inetAddress, int portNumber) throws Exception 
	{
		super(clientId, mqttBrokerInfoFilePath, inetAddress, portNumber);
	}
	@Override
	public byte[] getDataFromGizmo() 
	{
		if (settingsArray == null) return null;
		byte[] readingData = null;
		try 
		{
			sendBytes(settingsArray);
		} 
		catch (Exception e) 
		{
			setStatus("Error in sending data: " + e.getMessage());
			return null;
		}
		try {Thread.sleep(100);} catch (InterruptedException e) {}
		try 
		{
			readingData  = receiveBytes(numReadBytes);
		} catch (Exception e) 
		{
			setStatus("Error in reading data: " + e.getMessage());
			return null;
		}
		return readingData;
	}
	@Override
	public void handleBrokerMqttMessage(String topic, byte[] message) 
	{
		if (topic.indexOf("/set/mod") >= 0)
		{
			settingsArray = new byte[message.length];
			for (int ii = 0; ii < message.length; ++ii) settingsArray[ii] = message[ii];
		}
	}
	public static void main(String[] args) throws Exception 
	{
		ItsCernModulatorIoc ioc = new ItsCernModulatorIoc("itsCernModIoc", "itsmqttbroker.dat", "192.168.5.4", 8000);
		ioc.setPeriodicPollPeriodmillis(1000);
		ioc.startIoc("itsCernMod/set/#", "itsCernMod/get/mod");
	}

}
