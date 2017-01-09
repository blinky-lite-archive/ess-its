package se.esss.litterbox.icecube.ioc.serial;

import org.json.simple.JSONObject;

import se.esss.litterbox.icecube.ioc.IceCubePeriodicPollIoc;

public abstract class IceCubeSerialIoc extends IceCubePeriodicPollIoc
{
	private SerialReadWrite serialReadWrite;


	public IceCubeSerialIoc(String clientId, String mqttBrokerInfoFilePath, String serialPortName) throws Exception 
	{
		super(clientId, mqttBrokerInfoFilePath);
		serialReadWrite = new SerialReadWrite(serialPortName);
	}
	public String writeReadSerialData(String command, int timeoutsecs)
	{
		String data = "";
		try 
		{
			data =  serialReadWrite.writeReadStringData(command, timeoutsecs);
			setStatus("Recieved from Serialport: " + data);
		} catch (Exception e) 
		{
//			e.printStackTrace();
			setStatus("Unable to read serial port.");
		}
		return data;
	}
	@SuppressWarnings("unchecked")
	public void readResponseStringFromSerial(String command, int timeOutSec, JSONObject outputData)
	{
		String readData = writeReadSerialData(command, 10);
		int ispot = readData.indexOf(command);
		String data = "0";
		if (ispot >= 0)
		{
			data = readData.substring(command.length(), readData.length());
			data = data.trim();
		}
		outputData.put(command, data);
		return;
	}
}
