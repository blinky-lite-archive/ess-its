package se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ByteGearGwt implements Serializable
{
	private String name;
	private int readOffset = 0;
	private int writeOffset = 0;
	private ArrayList<ByteToothGwt> readToothList = new  ArrayList<ByteToothGwt>();
	private ArrayList<ByteToothGwt> writeToothList = new  ArrayList<ByteToothGwt>();
	
	public ArrayList<ByteToothGwt> getReadToothList() {return readToothList;}
	public ArrayList<ByteToothGwt> getWriteToothList() {return writeToothList;}
	public String getName() {return name;}
	public int getReadOffset() {return readOffset;}
	public int getWriteOffset() {return writeOffset;}
	public int getNumReadTooth() {return readToothList.size();}
	public int getNumWriteTooth() {return writeToothList.size();}
	
	public ByteGearGwt() {}
	public ByteGearGwt(String name, int readOffset, int writeOffset)
	{
		this.name = name;
		this.readOffset = readOffset;
		this.writeOffset = writeOffset;
	}
	public ByteToothGwt getReadByteTooth(String name) throws Exception
	{
		for (int ii = 0; ii < readToothList.size(); ++ii)
		{
			if (readToothList.get(ii).getName().equals(name)) return readToothList.get(ii);
		}
		throw new Exception("Read ByteTooth " + name + " not found.");
	}
	public ByteToothGwt getWriteByteTooth(String name) throws Exception
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
			readToothList.get(ii).getData(byteArray, writeOffset);
		}
	}
	public void getWriteData(byte[] byteArray) 
	{
		for (int ii = 0; ii < writeToothList.size(); ++ii)
		{
			writeToothList.get(ii).getData(byteArray, writeOffset);
		}
	}
}
