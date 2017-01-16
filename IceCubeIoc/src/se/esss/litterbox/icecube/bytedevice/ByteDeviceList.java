package se.esss.litterbox.icecube.bytedevice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class ByteDeviceList implements Serializable
{
	private static final long serialVersionUID = 1L;
	private ArrayList<ByteDevice> deviceList;
	
	public ByteDeviceList() throws Exception
	{
		deviceList = new ArrayList<ByteDevice>();		
	}
	public ByteDeviceList(String[] csvLineArray) throws Exception
	{
		deviceList = new ArrayList<ByteDevice>();		
		for (int ii = 0; ii < csvLineArray.length; ++ii) deviceList.add(new ByteDevice(csvLineArray[ii]));
	}
	public ByteDeviceList(URL filePathUrl) throws Exception
	{
		deviceList = new ArrayList<ByteDevice>();	
		String[] csvLineArray = fileToStringArray(filePathUrl);
		for (int ii = 0; ii < csvLineArray.length; ++ii) deviceList.add(new ByteDevice(csvLineArray[ii]));
	}
	public ByteDeviceList(String filePath) throws Exception
	{
		deviceList = new ArrayList<ByteDevice>();	
		String[] csvLineArray = fileToStringArray(filePath);
		for (int ii = 0; ii < csvLineArray.length; ++ii) deviceList.add(new ByteDevice(csvLineArray[ii]));
	}
	private String[] fileToStringArray(URL filePathUrl) throws Exception
	{
		ArrayList<String> deviceList = new ArrayList<String>();
		BufferedReader br = new BufferedReader( new InputStreamReader(filePathUrl.openStream()));
		br.readLine(); // read header
		String line;
		while ((line = br.readLine()) != null) 
		{
			deviceList.add(line);
		}
		br.close();
		String[] csvLineArray = new  String[deviceList.size()];
		for (int ii = 0; ii < deviceList.size(); ++ii) csvLineArray[ii] = deviceList.get(ii);
		return csvLineArray;
	}
	private String[] fileToStringArray(String filePath) throws Exception
	{
		ArrayList<String> deviceList = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		br.readLine(); // read header
		String line;
		while ((line = br.readLine()) != null) 
		{
			deviceList.add(line);
		}
		br.close();
		String[] csvLineArray = new  String[deviceList.size()];
		for (int ii = 0; ii < deviceList.size(); ++ii) csvLineArray[ii] = deviceList.get(ii);
		return csvLineArray;
	}
	public byte[] getByteArray()
	{
		int numDevices = deviceList.size();
		int numBytes = numberOfBytes();
		byte[] byteArray = new byte[numBytes];
		for (int ii = 0; ii < numBytes; ++ii) byteArray[ii] = 0;
		for (int idev = 0; idev <  numDevices; ++idev)
		{
			ByteDevice iceCubeDevice = deviceList.get(idev);
			if (iceCubeDevice.getType().toLowerCase().equals("bool"))
			{
//				byteArray[iceCubeDevice.getByteStart() - 1] = (byte) (byteArray[iceCubeDevice.getByteStart() - 1] | (Integer.parseInt(iceCubeDevice.getValue()) << iceCubeDevice.getByteStart()));
				byteArray[iceCubeDevice.getByteStart() - 1] = (byte) (byteArray[iceCubeDevice.getByteStart() - 1] | (Integer.parseInt(iceCubeDevice.getValue()) << iceCubeDevice.getBitLocation()));
			}
			else if (iceCubeDevice.getType().toLowerCase().equals("byte"))
			{
				byteArray[iceCubeDevice.getByteStart() - 1] = Byte.parseByte(iceCubeDevice.getValue());
			}
			else if (iceCubeDevice.getType().toLowerCase().equals("int"))
			{
				int value = Integer.parseInt(iceCubeDevice.getValue()) * iceCubeDevice.getMultiplier();
				byte[] twoBytes = intToByte(value);
				byteArray[iceCubeDevice.getByteStart() - 1] = twoBytes[0];
				byteArray[iceCubeDevice.getByteStart()] = twoBytes[1];
				
			}
			else if (iceCubeDevice.getType().toLowerCase().equals("float"))
			{
				double value = Double.parseDouble(iceCubeDevice.getValue()) * ((double) iceCubeDevice.getMultiplier());
				int ivalue = (int) value;
				byte[] twoBytes = intToByte(ivalue);
				byteArray[iceCubeDevice.getByteStart() - 1] = twoBytes[0];
				byteArray[iceCubeDevice.getByteStart()] = twoBytes[1];
			}
		}
				
		return byteArray;
	}
	public static byte[] intToByte(int itarget)
	{
		
	    int n = 0;
	    int goal = 0xFF << (8 * n);
	    int b0 = (itarget & goal) >> (8 * n);
	    
	    n = 1;
	    goal = 0xFF << (8 * n);
	    int b1 = (itarget & goal) >> (8 * n);
	    
	    byte[] twoBytes = new byte[2];
	    twoBytes[0] = (byte) b0;
	    twoBytes[1] = (byte) b1;
	    
	    return twoBytes;
	}
	public static int getBit(byte myByte, int position)
	{
	   return (myByte >> position) & 1;
	}
	public  int numberOfBytes()
	{
		int numDevices = deviceList.size();
		ByteDevice lastDevice = deviceList.get(numDevices - 1);
		int numBytesRequired = lastDevice.getByteStart();
		if (!lastDevice.getType().toLowerCase().equals("bool")) numBytesRequired = numBytesRequired + 1;
		return numBytesRequired;
	}
	public  void putByteArray(byte[] byteArray) throws Exception
	{
		int numDevices = deviceList.size();
		int numBytesRequired = numberOfBytes();
		if (byteArray.length != numBytesRequired) throw new Exception("Byte array size does not match device list requirements");
		for (int idev = 0; idev <  numDevices; ++idev)
		{
			ByteDevice iceCubeDevice = deviceList.get(idev);
			if (iceCubeDevice.getType().toLowerCase().equals("bool"))
			{
				iceCubeDevice.setValue(Integer.toString(getBit(byteArray[iceCubeDevice.getByteStart() - 1], iceCubeDevice.getBitLocation())));
			}
			else if (iceCubeDevice.getType().toLowerCase().equals("byte"))
			{
				iceCubeDevice.setValue(Integer.toString(byteArray[iceCubeDevice.getByteStart() - 1] & 0xff));
			}
			else if (iceCubeDevice.getType().toLowerCase().equals("int"))
			{
				int byte1 = (byteArray[iceCubeDevice.getByteStart() - 1] & 0xff);
				int byte2 = (byteArray[iceCubeDevice.getByteStart()    ] & 0xff);
				int value = (byte2 * 256) + byte1;
				value = value / iceCubeDevice.getMultiplier();
				iceCubeDevice.setValue(Integer.toString(value));
			}
			else if (iceCubeDevice.getType().toLowerCase().equals("float"))
			{
				int byte1 = (byteArray[iceCubeDevice.getByteStart() - 1] & 0xff);
				int byte2 = (byteArray[iceCubeDevice.getByteStart()    ] & 0xff);
				double value = (double) ((byte2 * 256) + byte1);
				value = value / ((double) iceCubeDevice.getMultiplier());
				iceCubeDevice.setValue(Double.toString(value));
			}
//			System.out.println(md.csvLine());
		}		
	}
	public  ByteDevice getDevice(String deviceName) throws Exception
	{
		int idevice = 0;
		int numDevices = deviceList.size();
		while (idevice < numDevices)
		{
			if (deviceList.get(idevice).getName().equals(deviceName))
			{
				 return deviceList.get(idevice);
			}
			else
			{
				idevice = idevice + 1;
			}
		}
		throw new Exception("Device \"" + deviceName + "\" not found");
		
	}
	public int numDevices() {return deviceList.size();}
	public ByteDevice getDevice(int ii) throws Exception
	{
		if ((ii < 0) || (ii >=  numDevices()) ) throw new Exception("Device index :" + ii + " does not exist");
		return deviceList.get(ii);
	}
	


}
