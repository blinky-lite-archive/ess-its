package se.esss.litterbox.icecube.bytegearbox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ByteGearBox 
{
	String broker = "";
	int brokerPort = -1;
	String topic = "";
	int readByteLength = 0;
	int writeByteLength = 0;
	ArrayList<ByteGear> byteGearList = new ArrayList<ByteGear>();
	
	public String getBroker() {return broker;}
	public int getBrokerPort() {return brokerPort;}
	public String getTopic() {return topic;}
	public int getReadByteLength() {return readByteLength;}
	public int getWriteByteLength() {return writeByteLength;}
	public ArrayList<ByteGear> getByteGearList() {return byteGearList;}

	public ByteGearBox(String broker, int brokerPort, String topic, int readByteLength, int writeByteLength)
	{
		this.broker = broker;
		this.brokerPort = brokerPort;
		this.topic = topic;
		this.readByteLength = readByteLength;
		this.writeByteLength = writeByteLength;
	}
	@SuppressWarnings("unchecked")
	public ByteGearBox(JSONObject jsonObject)
	{
		this.broker = (String) jsonObject.get("broker");
		this.brokerPort  = (int) jsonObject.get("brokerport");
		this.topic  = (String) jsonObject.get("topic");
		this.readByteLength  = (int) jsonObject.get("readByteLength");
		this.writeByteLength  = (int) jsonObject.get("writeByteLength");
		JSONArray byteGearArray = (JSONArray) jsonObject.get("byteGears");
        Iterator<JSONObject> iterator = byteGearArray.iterator();
        while (iterator.hasNext()) 
        {
        	byteGearList.add(new ByteGear(iterator.next()));
        }
	}
	@SuppressWarnings("unchecked")
	public ByteGearBox(URL url) throws Exception
	{
	  
		InputStream is = url.openStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) 
	    {
	      sb.append((char) cp);
	    }
		is.close();
	    JSONObject jsonObject  = (JSONObject) new JSONParser().parse(sb.toString());
		this.broker = (String) jsonObject.get("broker");
		this.brokerPort  = (int) ((long) jsonObject.get("brokerport"));
		
		this.topic  = (String) jsonObject.get("topic");
		this.readByteLength  = (int) ((long) jsonObject.get("readByteLength"));
		this.writeByteLength  = (int) ((long) jsonObject.get("writeByteLength"));
		JSONArray byteGearArray = (JSONArray) jsonObject.get("byteGears");
        Iterator<JSONObject> iterator = byteGearArray.iterator();
        while (iterator.hasNext()) 
        {
        	byteGearList.add(new ByteGear(iterator.next()));
        }

	}
	public ByteGearBox(String filePath) throws Exception
	{
		this((JSONObject) new JSONParser().parse(new FileReader(filePath)));
	}
	public ByteGear getByteGear(String name) throws Exception
	{
		for (int ii = 0; ii < byteGearList.size(); ++ii)
		{
			if (byteGearList.get(ii).getName().equals(name)) return byteGearList.get(ii);
		}
		throw new Exception("ByteGear " + name + " not found.");
	}
	public void loadReadDataFromByteArray(byte[] readByteArray)
	{
		for (int ii = 0; ii < byteGearList.size(); ++ii)
		{
			byteGearList.get(ii).loadReadDataFromByteArray(readByteArray);
		}
	}
	public void loadWriteDataFromByteArray(byte[] writeByteArray)
	{
		for (int ii = 0; ii < byteGearList.size(); ++ii)
		{
			byteGearList.get(ii).loadWriteDataFromByteArray(writeByteArray);
		}
	}
	public void loadReadDataIntoByteArray(byte[] readByteArray) 
	{
		for (int ii = 0; ii < byteGearList.size(); ++ii)
		{
			byteGearList.get(ii).loadReadDataIntoByteArray(readByteArray);
		}
	}
	public void loadWriteDataIntoByteArray(byte[] writeByteArray) 
	{
		for (int ii = 0; ii < byteGearList.size(); ++ii)
		{
			byteGearList.get(ii).loadWriteDataIntoByteArray(writeByteArray);
		}
	}
	@SuppressWarnings("unchecked")
	public JSONObject getJsonObject()
	{
		JSONObject outputData = new JSONObject();
		outputData.put("broker", broker);
		outputData.put("brokerport", brokerPort);
		outputData.put("topic", topic);
		outputData.put("readByteLength", readByteLength);
		outputData.put("writeByteLength", writeByteLength);
        JSONArray byteGearArray = new JSONArray();
        for (int ii = 0; ii < byteGearList.size(); ++ii)
        {
        	byteGearArray.add(byteGearList.get(ii).getJsonObject());
        }
		outputData.put("byteGears", byteGearArray);
		return outputData;
	}
	public void writeToFile(String filePath, boolean pretty) throws Exception
	{
		String outputString = getJsonObject().toJSONString();
		if (pretty)
		{
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine scriptEngine = manager.getEngineByName("JavaScript");
			scriptEngine.put("jsonString", outputString);
			scriptEngine.eval("result = JSON.stringify(JSON.parse(jsonString), null, 2)");
			outputString = (String) scriptEngine.get("result");

		}
		FileWriter file = new FileWriter(filePath);
		file.write(outputString);
        file.flush();
        file.close();
	}
	public static void main(String[] args) throws Exception 
	{
		URL url = new URL("https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/gearbox/klyPlcProtoAio.json");
		ByteGearBox byteGearBox = new ByteGearBox(url);
		byteGearBox.writeToFile("test.json", false);
	}
}
