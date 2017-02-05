package se.esss.litterbox.its.archivergwt.shared;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ArchiveTopic implements Serializable
{
	public static final int JSONDATA = 1;
	public static final int BYTEDATA = 2;
	
	private String topic = null;
	private long writePeriodMilli = 0;
	private long timeOfCreationMilli = 0;
	private long timeOfLastWriteMilli = 0;
	private String[][] jsonData = null;
	private byte[] message = null;
	private int dataType = 0;
	private int index;

	public String getTopic() {return topic;}
	public long getWritePeriodMilli() {return writePeriodMilli;}
	public long getTimeOfCreationMilli() {return timeOfCreationMilli;}
	public long getTimeOfLastWriteMilli() {return timeOfLastWriteMilli;}
	public String[][] getJsonData() {return jsonData;}
	public byte[] getMessage() {return message;}
	public int getDataType() {return dataType;}
	public int getIndex() {return index;}
	
	public void setTopic(String topic) {this.topic = topic;}
	public void setWritePeriodMilli(long writePeriodMilli) {this.writePeriodMilli = writePeriodMilli;}
	public void setTimeOfCreationMilli(long timeOfCreationMilli) {this.timeOfCreationMilli = timeOfCreationMilli;}
	public void setTimeOfLastWriteMilli(long timeOfLastWriteMilli) {this.timeOfLastWriteMilli = timeOfLastWriteMilli;}
	public void setJsonData(String[][] jsonData) {this.jsonData = jsonData;}
	public void setMessage(byte[] message) 
	{
		this.message = new byte[message.length];
		for (int ii = 0; ii < message.length; ++ii) this.message[ii] = message[ii];
	}
	public void setDataType(int dataType) {this.dataType = dataType;}
	public void setIndex(int index) {this.index = index;}
	
	public ArchiveTopic()
	{
		
	}
	public static ArchiveTopic getArchiveTopic(String topic, ArrayList<ArchiveTopic> archiveTopicList)
	{
		for (int ii = 0; ii < archiveTopicList.size(); ++ii)
		{
			if (archiveTopicList.get(ii).getTopic().equals(topic)) return archiveTopicList.get(ii);
		}
		return null;
	}
	public static ArchiveTopic getArchiveTopic(int index, ArrayList<ArchiveTopic> archiveTopicList)
	{
		for (int ii = 0; ii < archiveTopicList.size(); ++ii)
		{
			if (archiveTopicList.get(ii).getIndex() == index) return archiveTopicList.get(ii);
		}
		return null;
	}
	public String getJsonValue(String key) throws Exception
	{
		for (int ii = 0; ii < jsonData.length; ++ii)
		{
			if (jsonData[ii][0].equals(key)) return jsonData[ii][1];
		}
		throw new Exception(key + " not found");
	}
	public static void deleteArchiveTopic(String topic, ArrayList<ArchiveTopic> archiveTopicList)
	{
		for (int ii = 0; ii < archiveTopicList.size(); ++ii)
		{
			if (archiveTopicList.get(ii).getTopic().equals(topic))
			{
				archiveTopicList.remove(ii);
				return;
			}
		}
	}
	public static void deleteArchiveTopic(int index, ArrayList<ArchiveTopic> archiveTopicList)
	{
		for (int ii = 0; ii < archiveTopicList.size(); ++ii)
		{
			if (archiveTopicList.get(ii).getIndex() == index)
			{
				archiveTopicList.remove(ii);
				return;
			}
		}
	}
}
