package se.esss.litterbox.itsenvmongwt.server;

import java.util.Iterator;

import org.json.simple.JSONObject;

public class JsonTester 
{

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) 
	{
		JSONObject outputData = new JSONObject();	
		outputData.put("key1", "value1");
		outputData.put("key2", "value2");
		outputData.put("key3", "value3");
		outputData.put("key4", "value4");
		outputData.put("key5", "value5");
		outputData.put("key6", "value6");
		Iterator x = outputData.keySet().iterator();
		int numKeys = outputData.keySet().size();
		String[][] data = new String[numKeys][2];
		int icount = 0;
		while (x.hasNext())
		{
			data[icount][0] = (String) x.next();
			data[icount][1] = (String) outputData.get(data[icount][0]);
			System.out.println(icount + " " + data[icount][0] + " " + data[icount][1]);
			++icount;
		}
	}

}
