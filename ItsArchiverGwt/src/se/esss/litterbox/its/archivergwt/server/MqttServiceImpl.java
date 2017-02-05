package se.esss.litterbox.its.archivergwt.server;

import java.io.File;
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
import se.esss.litterbox.its.archivergwt.shared.ArchiveTopic;


@SuppressWarnings("serial")
public class MqttServiceImpl extends RemoteServiceServlet implements MqttService
{
	private MqttServiceImpClient mqttClient;
	private boolean mqttClientInitialized = false;
	private ArrayList<ArchiveTopic> archiveTopicList = new ArrayList<ArchiveTopic>();
	private Connection dbConnection;
	
	public void init()
	{
 		boolean cleanSession = false;
		try 
		{
			Class.forName("org.h2.Driver");
			String dbDirPath = getServletContext().getRealPath("itsDB");
	        dbConnection = DriverManager.getConnection("jdbc:h2:" + dbDirPath + "/itsDB", "", "");
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
