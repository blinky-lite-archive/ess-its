package se.esss.litterbox.itsllrfgwt.shared;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class FileToStringArray 
{
	public static  String[] fileToStringArray(String filePath) throws Exception
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
	public static String[] fileToStringArray(URL filePathUrl) throws Exception
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

}
