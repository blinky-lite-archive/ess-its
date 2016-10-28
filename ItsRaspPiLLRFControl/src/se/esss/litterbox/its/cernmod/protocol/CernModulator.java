package se.esss.litterbox.its.cernmod.protocol;

import java.util.ArrayList;

import se.esss.litterbox.icetray.IceCubeDevice;

public class CernModulator 
{
	private static final String settingsCsvFilePath = "se/esss/litterbox/its/cernmod/protocol/CernModulatorProtocolSet.csv";
	private static final String readingsCsvFilePath = "se/esss/litterbox/its/cernmod/protocol/CernModulatorProtocolRead.csv";


	private ArrayList<IceCubeDevice> settingsList;
	private ArrayList<IceCubeDevice> readingsList;
	
	public CernModulator() throws Exception
	{
		settingsList = IceCubeDevice.readCsvFile(Thread.currentThread().getContextClassLoader().getResource(settingsCsvFilePath));		
		readingsList = IceCubeDevice.readCsvFile(Thread.currentThread().getContextClassLoader().getResource(readingsCsvFilePath));		
	}
	public byte[] getSettingData() {return IceCubeDevice.createByteArray(settingsList);}
	public void putSettingData(byte[] settingDataByteArray) throws Exception
	{
		IceCubeDevice.readByteArray(settingsList, settingDataByteArray);
	}
	public IceCubeDevice getSettingsDevice(String deviceName) throws Exception
	{
		return IceCubeDevice.getDevice(deviceName, settingsList);
	}
	public int numberOfBytesInReadingList()
	{
		return IceCubeDevice.numberOfBytesInDeviceList(readingsList);
	}
	public void putReadingData(byte[] readingDataByteArray) throws Exception
	{
		IceCubeDevice.readByteArray(readingsList, readingDataByteArray);
	}
	public byte[] getReadingData() {return IceCubeDevice.createByteArray(readingsList);}

	public static void main(String[] args) throws Exception 
	{

	}

}
