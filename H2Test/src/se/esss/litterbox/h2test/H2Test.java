package se.esss.litterbox.h2test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class H2Test 
{

	public static void main(String[] args) throws Exception 
	{
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection("jdbc:h2:itsDB","teststand","4016");
		Statement st = conn.createStatement();
//		st.execute("create table itstopics(name varchar(20))");
		System.out.println("Table create successfully");
	}

}
