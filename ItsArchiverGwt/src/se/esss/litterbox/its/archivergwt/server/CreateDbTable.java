package se.esss.litterbox.its.archivergwt.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class CreateDbTable 
{
	public static final int JSONDATA = 1;
	public static final int BYTEDATA = 2;
	public static void createTables() throws Exception
	{
		System.out.println("Creating tables...");
        Connection dbConnection = DriverManager.getConnection("jdbc:h2:" + "./war/itsDB/itsDB", "", "");
        String createQuery = "CREATE TABLE ITSARCHIVE(tod long primary key, topic varchar(255), datatype int, payload varchar(511))";
        PreparedStatement createPreparedStatement = null;
        dbConnection.setAutoCommit(false);
        
        createPreparedStatement = dbConnection.prepareStatement(createQuery);
        createPreparedStatement.executeUpdate();
        createPreparedStatement.close();
        
        createQuery = "CREATE TABLE ITSTOPICS(id int primary key, topic varchar(255), datatype int, period int, toc long, tolw long)";
        createPreparedStatement = dbConnection.prepareStatement(createQuery);
        createPreparedStatement.executeUpdate();
        createPreparedStatement.close();
        
        dbConnection.commit();
        dbConnection.close();
		System.out.println("...Finished Creating Tables");
	}
	public static void addTopicToTable(String topic, int datatype, int period) throws Exception
	{
        System.out.println("Adding topic " + topic);
        Connection dbConnection = DriverManager.getConnection("jdbc:h2:" + "./war/itsDB/itsDB", "", "");
        String selectQuery = "select * from ITSTOPICS where topic=?";
        PreparedStatement selectPreparedStatement = dbConnection.prepareStatement(selectQuery);
        selectPreparedStatement.setString(1, topic);
        ResultSet rs = selectPreparedStatement.executeQuery();
        if (rs.next())
        {
            selectPreparedStatement.close();
            dbConnection.close();
            System.out.println("Topic: " + topic + " already exists");
            dbConnection.close();
            return;
        }
		
        selectQuery = "select * from ITSTOPICS";
        selectPreparedStatement = dbConnection.prepareStatement(selectQuery);
        rs = selectPreparedStatement.executeQuery();
        int id = 0;
        while (rs.next()) 
        {
        	id = rs.getInt("id");
            System.out.println("id: " + rs.getInt("id") + " topic: " + rs.getString("topic") + " datatype: " + rs.getString("datatype") + " period: " + rs.getInt("period") + " toc: " + new Date(rs.getLong("toc")).toString() + " tolw: " + new Date(rs.getLong("tolw")).toString());
        }
        selectPreparedStatement.close();
        id = id + 1;

        String insertQuery = "INSERT INTO ITSTOPICS" + "(id, topic, datatype, period, toc, tolw) values" + "(?,?,?,?,?,?)";
        PreparedStatement insertPreparedStatement = dbConnection.prepareStatement(insertQuery);
        insertPreparedStatement.setInt(1, id);
        insertPreparedStatement.setString(2, topic);
        insertPreparedStatement.setInt(3, datatype);
        insertPreparedStatement.setInt(4, period);
        insertPreparedStatement.setLong(5, new Date().getTime());
        insertPreparedStatement.setLong(6, 0);
        insertPreparedStatement.executeUpdate();
        insertPreparedStatement.close();

        dbConnection.commit();
        dbConnection.close();
        System.out.println("Topic: " + topic + " added to id: " + id);
	}
	public static void removeTopicFromTable(String topic) throws Exception
	{
        System.out.println("Deleting topic " + topic);
        Connection dbConnection = DriverManager.getConnection("jdbc:h2:" + "./war/itsDB/itsDB", "", "");
        String deleteQuery = "delete from ITSTOPICS where topic=?";
        PreparedStatement deletePreparedStatement = dbConnection.prepareStatement(deleteQuery);
        deletePreparedStatement.setString(1, topic);
        deletePreparedStatement.execute();
        deletePreparedStatement.close();
        
        dbConnection.commit();
        dbConnection.close();
        System.out.println("Topic: " + topic + " deleted.");
	}
	public static void main(String[] args) throws Exception 
	{
        Class.forName("org.h2.Driver");
		createTables();
        addTopicToTable("itsGeiger01/get/cpm", JSONDATA, 10);
        addTopicToTable("itsSolarMeter01/get/cond", JSONDATA, 10);
//        removeTopicFromTable("itsSolarMeter01/get/cond");
	}

}
