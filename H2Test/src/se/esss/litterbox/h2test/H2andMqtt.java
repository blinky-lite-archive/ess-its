package se.esss.litterbox.h2test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Date;

import se.esss.litterbox.icecube.simplemqtt.SimpleMqttClient;

public class H2andMqtt extends SimpleMqttClient
{
    
    private Connection dbConnection = null;
    private int id = 1;
	
	public H2andMqtt(String clientIdBase, String mqttBrokerInfoFilePath, String databasePath) throws Exception
	{
		super(clientIdBase, mqttBrokerInfoFilePath, false);
		getDBConnection(databasePath, "", "");
	}
    private  void getDBConnection(String databasePath, String dbUser, String dbPassword) throws Exception
    {
        dbConnection = null;
        Class.forName("org.h2.Driver");
        dbConnection = DriverManager.getConnection("jdbc:h2:" + databasePath, dbUser, dbPassword);
    }
	public void createTable() throws Exception
	{
        String createQuery = "CREATE TABLE ITSTOPICS(tod long primary key, topic varchar(255), payload varchar(511))";
        PreparedStatement createPreparedStatement = null;
        dbConnection.setAutoCommit(false);
        
        createPreparedStatement = dbConnection.prepareStatement(createQuery);
        createPreparedStatement.executeUpdate();
        createPreparedStatement.close();
        dbConnection.commit();
		
	}
	public void closeConnection() throws Exception
	{
		dbConnection.close();
	}
	@Override
	public void newMessage(String topic, byte[] message) 
	{
		try
		{
	        PreparedStatement insertPreparedStatement = null;
	        String insertQuery = "INSERT INTO ITSTOPICS" + "(tod, topic, payload) values" + "(?,?,?)";
	        insertPreparedStatement = dbConnection.prepareStatement(insertQuery);
	        insertPreparedStatement.setLong(1, new Date().getTime());
	        insertPreparedStatement.setString(2, topic);
	        insertPreparedStatement.setBytes(3, message);
	        insertPreparedStatement.executeUpdate();
	        insertPreparedStatement.close();
	        dbConnection.commit();
	        System.out.println("Writing " + topic + " into record " + Integer.toString(id));
	        ++id;
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	public static void main(String[] args)  throws Exception
	{
		H2andMqtt h2m = null;
		try
		{
			h2m = new H2andMqtt("itsArchiver", "./itsmqttbroker.dat", "./itsDB");
//			h2m.createTable();
			h2m.subscribe("itsGeiger01/get/cpm", 0);
			h2m.subscribe("itsSolarMeter01/get/cond", 0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
 //       finally {h2m.closeConnection();}

	}

}
