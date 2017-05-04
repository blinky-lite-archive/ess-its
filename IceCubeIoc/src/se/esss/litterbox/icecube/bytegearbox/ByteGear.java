package se.esss.litterbox.icecube.bytegearbox;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ByteGear 
{
	private String name;
	private int readOffset = 0;
	private int writeOffset = 0;
	private ArrayList<ByteTooth> readToothList = new  ArrayList<ByteTooth>();
	private ArrayList<ByteTooth> writeToothList = new  ArrayList<ByteTooth>();
	
	public ArrayList<ByteTooth> getReadToothList() {return readToothList;}
	public ArrayList<ByteTooth> getWriteToothList() {return writeToothList;}
	public String getName() {return name;}
	public int getReadOffset() {return readOffset;}
	public int getWriteOffset() {return writeOffset;}
	public int getNumReadTooth() {return readToothList.size();}
	public int getNumWriteTooth() {return writeToothList.size();}
	
	public ByteGear(String name, int readOffset, int writeOffset)
	{
		this.name = name;
		this.readOffset = readOffset;
		this.writeOffset = writeOffset;
	}
	@SuppressWarnings("unchecked")
	public ByteGear(JSONObject jsonObject)
	{
		this.name = (String) jsonObject.get("name");
		this.readOffset = Integer.parseInt((String) jsonObject.get("readByteOff"));
		this.writeOffset = Integer.parseInt((String) jsonObject.get("writeByteOff"));
		JSONArray readList = (JSONArray) jsonObject.get("readToothList");
		JSONArray writeList = (JSONArray) jsonObject.get("writeToothList");
        Iterator<JSONObject> iterator = readList.iterator();
        while (iterator.hasNext()) 
        {
        	readToothList.add(new ByteTooth(iterator.next()));
        }
        iterator = writeList.iterator();
        while (iterator.hasNext()) 
        {
        	writeToothList.add(new ByteTooth(iterator.next()));
        }
	}
	public ByteTooth getReadByteTooth(String name) throws Exception
	{
		for (int ii = 0; ii < readToothList.size(); ++ii)
		{
			if (readToothList.get(ii).getName().equals(name)) return readToothList.get(ii);
		}
		throw new Exception("Read ByteTooth " + name + " not found.");
	}
	public ByteTooth getWriteByteTooth(String name) throws Exception
	{
		for (int ii = 0; ii < writeToothList.size(); ++ii)
		{
			if (writeToothList.get(ii).getName().equals(name)) return writeToothList.get(ii);
		}
		throw new Exception("Write ByteTooth " + name + " not found.");
	}
	public void setReadData(byte[] byteArray)
	{
		for (int ii = 0; ii < readToothList.size(); ++ii)
		{
			readToothList.get(ii).setData(byteArray, readOffset);
//			System.out.println(ii + " name = " + name + " " + readToothList.get(ii).printData());
		}
	}
	public void setWriteData(byte[] byteArray)
	{
		for (int ii = 0; ii < writeToothList.size(); ++ii)
		{
			writeToothList.get(ii).setData(byteArray, writeOffset);
		}
	}
	public void getReadData(byte[] byteArray) 
	{
		for (int ii = 0; ii < readToothList.size(); ++ii)
		{
			readToothList.get(ii).getData(byteArray, readOffset);
		}
	}
	public void getWriteData(byte[] byteArray) 
	{
		for (int ii = 0; ii < writeToothList.size(); ++ii)
		{
			writeToothList.get(ii).getData(byteArray, writeOffset);
		}
	}
	@SuppressWarnings("unchecked")
	public JSONObject getJsonObject()
	{
		JSONObject outputData = new JSONObject();
		outputData.put("name", name);
		outputData.put("readByteOff", Integer.toString(readOffset));
		outputData.put("writeByteOff", Integer.toString(writeOffset));
        JSONArray readList = new JSONArray();
        JSONArray writeList = new JSONArray();
        for (int ii = 0; ii < readToothList.size(); ++ii)
        {
        	readList.add(readToothList.get(ii).getJsonObject());
        }
        for (int ii = 0; ii < writeToothList.size(); ++ii)
        {
        	writeList.add(writeToothList.get(ii).getJsonObject());
        }
		outputData.put("readToothList", readList);
		outputData.put("writeToothList", writeList);
		return outputData;
	}
	public String[] printReadData()
	{
		String[] readDataStringArray = new String[1 + readToothList.size()];
		readDataStringArray[0] = "Name = " + name + " readByteOff = " + Integer.toBinaryString(readOffset);
        for (int ii = 0; ii < readToothList.size(); ++ii)
        {
        	readDataStringArray[ii + 1] = "\t" + (ii + 1) + "\t" + readToothList.get(ii).printData();
        }
        return readDataStringArray;
	}
	public String[] printWriteData()
	{
		String[] writeDataStringArray = new String[1 + writeToothList.size()];
		writeDataStringArray[0] = "Name = " + name + " writeByteOff = " + Integer.toBinaryString(writeOffset);
        for (int ii = 0; ii < writeToothList.size(); ++ii)
        {
        	writeDataStringArray[ii + 1] = "\t" + (ii + 1) + "\t" + writeToothList.get(ii).printData();
        }
        return writeDataStringArray;
	}
}
