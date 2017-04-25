package se.esss.litterbox.icecube.bytegearbox;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.eclipse.paho.client.mqttv3.internal.websocket.Base64;
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
	Date lastWriteDataUpdate = new Date((long) 0);
	Date lastReadDataUpdate = new Date((long) 0);
	Date lastDataUpdate = new Date((long) 0);
	
	public String getBroker() {return broker;}
	public int getBrokerPort() {return brokerPort;}
	public String getTopic() {return topic;}
	public int getReadByteLength() {return readByteLength;}
	public int getWriteByteLength() {return writeByteLength;}
	public ArrayList<ByteGear> getByteGearList() {return byteGearList;}
	public int getNumByteGear() {return byteGearList.size();}

	public Date getLastWriteDataUpdate() {return lastWriteDataUpdate;}
	public Date getLastReadDataUpdate() {return lastReadDataUpdate;}
	public Date getLastDataUpdate() {return lastDataUpdate;}
	
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
		this.brokerPort  = Integer.parseInt((String) jsonObject.get("brokerport"));
		this.topic  = (String) jsonObject.get("topic");
		this.readByteLength  = Integer.parseInt((String) jsonObject.get("readByteLength"));
		this.writeByteLength  = Integer.parseInt((String) jsonObject.get("writeByteLength"));
		JSONArray byteGearArray = (JSONArray) jsonObject.get("byteGears");
        Iterator<JSONObject> iterator = byteGearArray.iterator();
        while (iterator.hasNext()) 
        {
        	byteGearList.add(new ByteGear(iterator.next()));
        }
	}
	public ByteGearBox(URL url) throws Exception
	{
		this(url, null);
	}
	@SuppressWarnings("unchecked")
	public ByteGearBox(URL url, String itsnetWebLoginInfoPath) throws Exception
	{
		URLConnection uc = url.openConnection();
		if (itsnetWebLoginInfoPath != null)
		{
		    InputStream fis = new FileInputStream(itsnetWebLoginInfoPath);
		    InputStreamReader isr = new InputStreamReader(fis);
		    BufferedReader br = new BufferedReader(isr);
		    String line = br.readLine();
		    br.close();
		    JSONObject itsnetWebLoginInfo = (JSONObject) new JSONParser().parse(line);

			String userpass = (String) itsnetWebLoginInfo.get("username") + ":" + (String) itsnetWebLoginInfo.get("key");
			String basicAuth = "Basic " + new String(Base64.encodeBytes(userpass.getBytes()));
			uc.setRequestProperty ("Authorization", basicAuth);
		}
	  
		InputStream is = uc.getInputStream();
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
		this.brokerPort  = Integer.parseInt((String) jsonObject.get("brokerport"));
		
		this.topic  = (String) jsonObject.get("topic");
		this.readByteLength  = Integer.parseInt((String) jsonObject.get("readByteLength"));
		this.writeByteLength  = Integer.parseInt((String) jsonObject.get("writeByteLength"));
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
		for (int ii = 0; ii < byteGearList.size(); ++ii)
		{
			byteGearList.get(ii).getReadData(readByteArray);
		}
		return readByteArray;
	}
	public byte[] getWriteData() 
	{
		byte[] writeByteArray = new byte[writeByteLength];
		for (int ii = 0; ii < byteGearList.size(); ++ii)
		{
			byteGearList.get(ii).getWriteData(writeByteArray);
		}
		return writeByteArray;
	}
	@SuppressWarnings("unchecked")
	public JSONObject getJsonObject()
	{
		JSONObject outputData = new JSONObject();
		outputData.put("broker", broker);
		outputData.put("brokerport", Integer.toString(brokerPort));
		outputData.put("topic", topic);
		outputData.put("readByteLength", Integer.toString(readByteLength));
		outputData.put("writeByteLength", Integer.toString(writeByteLength));
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
		URL url = new URL("https://aig.esss.lu.se:8443/ItsByteGearBoxServer/gearbox/klyPlcProtoCpu.json");
		ByteGearBox byteGearBox = new ByteGearBox(url, "itsnetWebLoginInfo.dat");
//		ByteGearBox byteGearBox = new ByteGearBox("klyPlcProtoCpu.json");
//		URL url = new URL("https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/gearbox/klyPlcProtoCpu.json");
//		ByteGearBox byteGearBox = new ByteGearBox(url);
		byteGearBox.writeToFile("test.json", true);
	}
}
