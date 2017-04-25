package se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("serial")
public class ByteGearBoxGwt implements Serializable
{
	String broker = "";
	int brokerPort = -1;
	String topic = "";
	int readByteLength = 0;
	int writeByteLength = 0;
	ArrayList<ByteGearGwt> byteGearList = new ArrayList<ByteGearGwt>();
	Date lastWriteDataUpdate = new Date((long) 0);
	Date lastReadDataUpdate = new Date((long) 0);
	Date lastDataUpdate = new Date((long) 0);
	
	public String getBroker() {return broker;}
	public int getBrokerPort() {return brokerPort;}
	public String getTopic() {return topic;}
	public int getReadByteLength() {return readByteLength;}
	public int getWriteByteLength() {return writeByteLength;}
	public ArrayList<ByteGearGwt> getByteGearList() {return byteGearList;}
	public int getNumByteGear() {return byteGearList.size();}

	public Date getLastWriteDataUpdate() {return lastWriteDataUpdate;}
	public Date getLastReadDataUpdate() {return lastReadDataUpdate;}
	public Date getLastDataUpdate() {return lastDataUpdate;}

	public ByteGearBoxGwt() {}
	public ByteGearBoxGwt(String broker, int brokerPort, String topic, int readByteLength, int writeByteLength)
	{
		this.broker = broker;
		this.brokerPort = brokerPort;
		this.topic = topic;
		this.readByteLength = readByteLength;
		this.writeByteLength = writeByteLength;
	}
	public ByteGearGwt getByteGear(String name) throws Exception
	{
		for (int ii = 0; ii < byteGearList.size(); ++ii)
		{
			if (byteGearList.get(ii).getName().equals(name)) return byteGearList.get(ii);
		}
		throw new Exception("ByteGear " + name + " not found.");
	}
	public void setReadData(byte[] readByteArray)
	{
		for (int ii = 0; ii < byteGearList.size(); ++ii)
		{
			byteGearList.get(ii).setReadData(readByteArray);
		}
		lastReadDataUpdate = new Date();
		lastDataUpdate.setTime(lastReadDataUpdate.getTime());
	}
	public void setWriteData(byte[] writeByteArray)
	{
		for (int ii = 0; ii < byteGearList.size(); ++ii)
		{
			byteGearList.get(ii).setWriteData(writeByteArray);
		}
		lastWriteDataUpdate = new Date();
		lastDataUpdate.setTime(lastWriteDataUpdate.getTime());
	}
	public byte[] getReadData() 
	{
		byte[] readByteArray = new byte[readByteLength];
		for (int ii = 0; ii < readByteLength; ++ii) readByteArray[ii] = 0;
		for (int ii = 0; ii < byteGearList.size(); ++ii)
		{
			byteGearList.get(ii).getReadData(readByteArray);
		}
		return readByteArray;
	}
	public byte[] getWriteData() 
	{
		byte[] writeByteArray = new byte[writeByteLength];
		for (int ii = 0; ii < writeByteLength; ++ii) writeByteArray[ii] = 0;
		for (int ii = 0; ii < byteGearList.size(); ++ii)
		{
			byteGearList.get(ii).getWriteData(writeByteArray);
		}
		return writeByteArray;
	}
}
