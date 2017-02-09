package se.esss.litterbox.its.archivergwt.server;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import se.esss.litterbox.its.archivergwt.client.mqttdata.MqttService;
import se.esss.litterbox.its.archivergwt.shared.ArchiveJsonData;
import se.esss.litterbox.its.archivergwt.shared.ArchiveTopic;


@SuppressWarnings("serial")
public class MqttServiceImpl extends RemoteServiceServlet implements MqttService
{
	private MqttServiceImpClient mqttClient;
	private boolean mqttClientInitialized = false;
	private ArrayList<ArchiveTopic> archiveTopicList = new ArrayList<ArchiveTopic>();
	private Connection dbConnection;
	private boolean readOnly = false;
	
	public void init()
	{
 		boolean cleanSession = false;
		try 
		{
			Class.forName("org.h2.Driver");
			String dbDirPath = getServletContext().getRealPath("itsDB");
			if (readOnly)
			{
				dbConnection = DriverManager.getConnection("jdbc:h2:" + dbDirPath + "/itsDB;ACCESS_MODE_DATA=r", "", "");
			}
			else
			{
				dbConnection = DriverManager.getConnection("jdbc:h2:" + dbDirPath + "/itsDB", "", "");
			}
			mqttClient = new MqttServiceImpClient(this, "ItsArchiverWebApp", getMqttDataPath(), cleanSession);
			mqttClientInitialized = true;
			getTopics();
			for (int ii = 0; ii < archiveTopicList.size(); ++ii)
			{
//				System.out.println(archiveTopicList.get(ii).getTopic());
				mqttClient.subscribe(archiveTopicList.get(ii).getTopic(), 0);
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	private void getTopics() throws Exception
	{
		archiveTopicList = new ArrayList<ArchiveTopic>();
		String selectQuery = "select * from ITSTOPICS";
        PreparedStatement selectPreparedStatement = dbConnection.prepareStatement(selectQuery);
        ResultSet rs = selectPreparedStatement.executeQuery();
        while (rs.next()) 
        {
        	ArchiveTopic at = new ArchiveTopic();
        	at.setIndex(rs.getInt("id"));
        	at.setTopic(rs.getString("topic"));
        	at.setDataType(rs.getInt("datatype"));
        	at.setTimeOfCreationMilli(rs.getLong("toc"));
        	at.setTimeOfLastWriteMilli(rs.getLong("tolw"));
        	at.setWritePeriodMilli((long) rs.getInt("period") * 1000);
        	archiveTopicList.add(at);
        }
        selectPreparedStatement.close();
	}
	private String getMqttDataPath() throws Exception
	{
		File tmpFile = new File(getServletContext().getRealPath("./"));
		tmpFile = new File(tmpFile.getParent());
		tmpFile = new File(tmpFile.getParent());
		return tmpFile.getPath() + "/itsmqttbroker.dat";
		
	}
	public void destroy()
	{
		try {dbConnection.close();} catch (Exception e) {dbConnection = null;}
		if (!mqttClientInitialized) return;
		try {mqttClient.unsubscribeAll();} catch (Exception e) {mqttClient = null;}
		try {mqttClient.disconnect();} catch (Exception e) {mqttClient = null;}
	}
	public void setMessage(String topic, byte[] message)
	{
//    	System.out.println("Got: " + topic);
		ArchiveTopic at = ArchiveTopic.getArchiveTopic(topic, archiveTopicList);
		if (at == null) return;
		Date now = new Date();
		if ((now.getTime() - at.getTimeOfLastWriteMilli()) > at.getWritePeriodMilli())
		{
	        try 
	        {
//    	    	System.out.println("Writing to DB: " + topic + " at " + now.toString());
	        	at.setTimeOfLastWriteMilli(now.getTime());
	        	at.setMessage(message);
	        	if (at.getDataType() == ArchiveTopic.JSONDATA) at.setJsonData(getJsonArray(message));
	    		if (!readOnly)
	    		{
		        	PreparedStatement insertPreparedStatement = null;
		        	String insertQuery = "INSERT INTO ITSARCHIVE" + "(tod, topic, datatype, payload) values" + "(?,?,?,?)";
		        	insertPreparedStatement = dbConnection.prepareStatement(insertQuery);
		        	insertPreparedStatement.setLong(1, now.getTime());
		        	insertPreparedStatement.setString(2, topic);
		        	insertPreparedStatement.setInt(3, at.getDataType());
		        	insertPreparedStatement.setBytes(4, message);
		        	insertPreparedStatement.executeUpdate();
		        	insertPreparedStatement.close();
		        	dbConnection.commit();
	    		}
	        }
	        catch (Exception e)
	        {
	        	System.out.println("Error writing topic to database: " + e.getMessage());
	        }
		}
	}
	@SuppressWarnings("rawtypes")
	private String[][] getJsonArray(byte[] message) throws Exception  
	{
		JSONParser parser = new JSONParser();		
		JSONObject jsonData;
		try {jsonData = (JSONObject) parser.parse(new String(message));} 
		catch (ParseException e) {throw new Exception("Cannot JSON parse the data" + e.getMessage());}
		int numKeys = jsonData.keySet().size();
		String[][] data = new String[numKeys][2];
		int ikey = 0;
		try {jsonData = (JSONObject) parser.parse(new String(message));} 
		catch (ParseException e) {throw new Exception("Cannot JSON parse the data" + e.getMessage());}
		Iterator iterJsonData = jsonData.keySet().iterator();
		while (iterJsonData.hasNext())
		{
			data[ikey][0] = (String) iterJsonData.next();
			data[ikey][1] = (String) jsonData.get(data[ikey][0]);
			++ikey;
		}
		return data;
	}
	private void getArchiveJsonData(ArchiveJsonData ajd) throws Exception
	{
        String selectQuery = "select * from ITSARCHIVE where topic=? and tod>=? and tod<?";
        PreparedStatement selectPreparedStatement = null;
        selectPreparedStatement = dbConnection.prepareStatement(selectQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        selectPreparedStatement.setString(1, ajd.getTopic());
        selectPreparedStatement.setLong(2, ajd.getStartTime());
        selectPreparedStatement.setLong(3, ajd.getStopTime());
        ResultSet rs = selectPreparedStatement.executeQuery();
        int ncount = 0;
        while (rs.next()) {++ncount;}
        if (ncount < 1) throw new Exception("No data found for requested time period and topic");
        double[] deviceData = new double[ncount];
        double[] timeData = new double[ncount];
        rs.beforeFirst();
        rs.next();
        if (rs.getInt("datatype") !=  ArchiveTopic.JSONDATA) throw new Exception("Requested data not JSON data");
        String[][] jsonData = getJsonArray(rs.getBytes("payload"));
        int ikey = -1;
        for (int ii = 0; ii < jsonData.length; ++ii)
        {
        	if (jsonData[ii][0].equals(ajd.getDeviceName())) ikey = ii;
        }
        if (ikey < 0) throw new Exception("JSON device not found in query");
        int icount = 0;
        rs.beforeFirst();
        while (rs.next())
        {
        	jsonData = getJsonArray(rs.getBytes("payload"));
        	try{deviceData[icount] = Double.parseDouble(jsonData[ikey][1]);}
        	catch (NumberFormatException nfe) {throw new Exception("JSON Data not a number");}
        	timeData[icount] = (double) (((rs.getLong("tod") - ajd.getStartTime())) / 1000);
        	timeData[icount] = timeData[icount] / 3600.0;
 //       	System.out.println(icount + " " + jsonData[ikey][0] + " " + jsonData[ikey][1] + " " + deviceData[icount] + " " + timeData[icount]);
        	++icount;
        }
        selectPreparedStatement.close();
        ajd.setDeviceData(deviceData);
        ajd.setTimeData(timeData);
        writeTraceDataCsvFile(ajd);
 	}
	private void writeTraceDataCsvFile(ArchiveJsonData ajd) throws Exception
	{
		if(readOnly) return;
		String traceFilePath = getServletContext().getRealPath("traceData/trace" + Integer.toString(ajd.getTrace()) + ".csv");
		PrintWriter pw = new PrintWriter(traceFilePath);
		pw.println("topic," + ajd.getTopic());
		pw.println("device," + ajd.getDeviceName());
		pw.println("startTime," + new Date(ajd.getStartTime()).toString());
		pw.println("stopTime," + new Date(ajd.getStopTime()).toString());
		pw.println("time (hours),value");
		for (int ii = 0; ii < ajd.getTimeData().length; ++ii)
		{
			pw.println(Double.toString(ajd.getTimeData()[ii]) + "," + Double.toString(ajd.getDeviceData()[ii]));
		}
		pw.close();
	}
	@Override
	public ArrayList<ArchiveJsonData> getArchiveData(ArrayList<ArchiveJsonData> archiveJsonDataList, boolean settingsEnabled, boolean debug, String debugResponse) throws Exception
	{
		for (int ii = 0; ii < archiveJsonDataList.size(); ++ii) getArchiveJsonData(archiveJsonDataList.get(ii));
		return archiveJsonDataList;
	}
	@Override
	public String publishMessage(String topic, byte[] message, boolean settingsEnabled, boolean debug, String debugResponse) throws Exception
	{
		return "ok";
	}
	@Override
	public ArrayList<ArchiveTopic> getTopicList(boolean debug, String debugResponse) 
	{
		return archiveTopicList;
	}
	@Override
	public ArrayList<ArchiveTopic> addTopic(ArchiveTopic archiveTopic, boolean settingsEnabled, boolean debug, String debugResponse) throws Exception
	{
		if (readOnly) throw new Exception("Database in READONLY");
		ArchiveTopic checkArchiveTopic = ArchiveTopic.getArchiveTopic(archiveTopic.getTopic(), archiveTopicList);
		if (checkArchiveTopic != null) throw new Exception(archiveTopic.getTopic() + " already exists");
		archiveTopicList.add(archiveTopic);
        String insertQuery = "INSERT INTO ITSTOPICS" + "(id, topic, datatype, period, toc, tolw) values" + "(?,?,?,?,?,?)";
        PreparedStatement insertPreparedStatement = dbConnection.prepareStatement(insertQuery);
        insertPreparedStatement.setInt(1, archiveTopic.getIndex());
        insertPreparedStatement.setString(2, archiveTopic.getTopic());
        insertPreparedStatement.setInt(3, archiveTopic.getDataType());
        insertPreparedStatement.setInt(4, ((int) archiveTopic.getWritePeriodMilli()) / 1000);
        insertPreparedStatement.setLong(5, archiveTopic.getTimeOfCreationMilli());
        insertPreparedStatement.setLong(6, archiveTopic.getTimeOfLastWriteMilli());
        insertPreparedStatement.executeUpdate();
        insertPreparedStatement.close();

        dbConnection.commit();
        mqttClient.subscribe(archiveTopic.getTopic(), 0);
		
		return archiveTopicList;
	}
	@Override
	public ArrayList<ArchiveTopic> deleteTopic(int index, boolean settingsEnabled, boolean debug, String debugResponse) throws Exception
	{
		if (readOnly) throw new Exception("Database in READONLY");
		ArchiveTopic checkArchiveTopic = ArchiveTopic.getArchiveTopic(index, archiveTopicList);
		if (checkArchiveTopic == null) throw new Exception("Topic " + index + " does not exist");
        mqttClient.unsubscribe(checkArchiveTopic.getTopic());
        
        String deleteQuery = "delete from ITSTOPICS where topic=?";
        PreparedStatement deletePreparedStatement = dbConnection.prepareStatement(deleteQuery);
        deletePreparedStatement.setString(1, checkArchiveTopic.getTopic());
        deletePreparedStatement.execute();
        deletePreparedStatement.close();
        
        dbConnection.commit();
        ArchiveTopic.deleteArchiveTopic(index, archiveTopicList);

		return archiveTopicList;
	}
}
