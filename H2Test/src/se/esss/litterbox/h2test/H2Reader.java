package se.esss.litterbox.h2test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

public class H2Reader 
{
	private Connection dbConnection = null;

    private  void getDBConnection(String databasePath, String dbUser, String dbPassword) throws Exception
    {
        dbConnection = null;
        Class.forName("org.h2.Driver");
        dbConnection = DriverManager.getConnection("jdbc:h2:" + databasePath, dbUser, dbPassword);
    }
    private void readData(String topic, Date startDate, Date stopDate) throws Exception
    {
    	getDBConnection("./itsDB", "", "");
        String selectQuery = "select * from ITSTOPICS where topic=? and tod>=? and tod<?";
        PreparedStatement selectPreparedStatement = null;
        selectPreparedStatement = dbConnection.prepareStatement(selectQuery);
        selectPreparedStatement.setString(1, topic);
        selectPreparedStatement.setLong(2, startDate.getTime());
        selectPreparedStatement.setLong(3, stopDate.getTime());
        ResultSet rs = selectPreparedStatement.executeQuery();
        while (rs.next()) 
        {
            System.out.println("Date " + new Date(rs.getLong("tod")).toString() + " Topic " + rs.getString("topic") + " Payload " + new String(rs.getBytes("payload")));
        }
        selectPreparedStatement.close();
        closeConnection();
    	
    }
    private static Date getDate(int year, int month, int day, int hour, int min, int sec )
    {
    	Calendar c = Calendar.getInstance();
    	c.set(year, month - 1, day, hour, min, sec);  
    	return c.getTime();
    }
	public void closeConnection() throws Exception
	{
		dbConnection.close();
	}
	public static void main(String[] args) throws Exception 
	{
		Date startDate = getDate(2017, 2, 3, 9, 47, 0 );
		Date stopDate  = getDate(2017, 2, 3, 9, 50, 0 );
		new H2Reader().readData("itsGeiger01/get/cpm", startDate, stopDate);

	}

}
