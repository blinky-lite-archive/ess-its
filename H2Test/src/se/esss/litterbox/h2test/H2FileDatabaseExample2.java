package se.esss.litterbox.h2test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.h2.tools.DeleteDbFiles;

public class H2FileDatabaseExample2 
{

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:./itsDB";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) throws Exception 
    {
        try 
        {
            DeleteDbFiles.execute("./", "itsDB", true);
            insertWithPreparedStatement();

        } catch (SQLException e) { e.printStackTrace();}
    }

    // H2 SQL Prepared Statement Example
    private static void insertWithPreparedStatement() throws SQLException 
    {
        Connection connection = getDBConnection();
        PreparedStatement createPreparedStatement = null;
        PreparedStatement insertPreparedStatement = null;
        PreparedStatement selectPreparedStatement = null;

        String CreateQuery = "CREATE TABLE ITSTOPICS(id int primary key, tod long, topic varchar(255), payload varchar(511))";
        String InsertQuery = "INSERT INTO ITSTOPICS" + "(id, tod, topic, payload) values" + "(?,?,?,?)";
        String SelectQuery = "select * from ITSTOPICS";
        try 
        {
            connection.setAutoCommit(false);
           
            createPreparedStatement = connection.prepareStatement(CreateQuery);
            createPreparedStatement.executeUpdate();
            createPreparedStatement.close();
            
            String payload = "topic payload 1";
            insertPreparedStatement = connection.prepareStatement(InsertQuery);
            insertPreparedStatement.setInt(1, 1);
            insertPreparedStatement.setLong(2, new Date().getTime());
            insertPreparedStatement.setString(3, "its/test/get");
            insertPreparedStatement.setBytes(4, payload.getBytes());
            insertPreparedStatement.executeUpdate();
            insertPreparedStatement.close();
           
            payload = "topic payload 2";
            insertPreparedStatement = connection.prepareStatement(InsertQuery);
            insertPreparedStatement.setInt(1, 2);
            insertPreparedStatement.setLong(2, new Date().getTime());
            insertPreparedStatement.setString(3, "its/test/set");
            insertPreparedStatement.setBytes(4, payload.getBytes());
            insertPreparedStatement.executeUpdate();
            insertPreparedStatement.close();
            
            
            selectPreparedStatement = connection.prepareStatement(SelectQuery);
            ResultSet rs = selectPreparedStatement.executeQuery();
            System.out.println("H2 Database inserted through PreparedStatement");
            while (rs.next()) 
            {
                System.out.println("Id " + rs.getInt("id") + " Date " + new Date(rs.getLong("tod")).toString() + " Topic " + rs.getString("topic") + " Payload " + new String(rs.getBytes("payload")));
            }
            selectPreparedStatement.close();
           
            connection.commit();
        } 
        catch (SQLException e) {System.out.println("Exception Message " + e.getLocalizedMessage());} 
        catch (Exception e) {e.printStackTrace();} 
        finally {connection.close();}
    }

    private static Connection getDBConnection() 
    {
        Connection dbConnection = null;
        try {Class.forName(DB_DRIVER);} catch (ClassNotFoundException e) {System.out.println(e.getMessage());}
        try 
        {
        	dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {System.out.println(e.getMessage());}
        return dbConnection;
    }
}