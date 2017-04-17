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
		this.readOffset = (int) jsonObject.get("readByteOff");
		this.writeOffset = (int) jsonObject.get("writeByteOff");
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
	public void loadReadDataFromByteArray(byte[] byteArray)
	{
		for (int ii = 0; ii < readToothList.size(); ++ii)
		{
			readToothList.get(ii).loadDataFromByteArray(byteArray, readOffset);
		}
	}
	public void loadWriteDataFromByteArray(byte[] byteArray)
	{
		for (int ii = 0; ii < writeToothList.size(); ++ii)
		{
			writeToothList.get(ii).loadDataFromByteArray(byteArray, writeOffset);
		}
	}
	public void loadReadDataIntoByteArray(byte[] byteArray) 
	{
		for (int ii = 0; ii < readToothList.size(); ++ii)
		{
			readToothList.get(ii).loadDataIntoByteArray(byteArray, writeOffset);
		}
	}
	public void loadWriteDataIntoByteArray(byte[] byteArray) 
	{
		for (int ii = 0; ii < writeToothList.size(); ++ii)
		{
			writeToothList.get(ii).loadDataIntoByteArray(byteArray, writeOffset);
		}
	}
	@SuppressWarnings("unchecked")
	public JSONObject getJsonObject()
	{
		JSONObject outputData = new JSONObject();
		outputData.put("name", name);
		outputData.put("readByteOff", readOffset);
		outputData.put("writeByteOff", writeOffset);
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
}
