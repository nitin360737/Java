package com.wipro.game.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	public static Connection getDatabaseConnection(){
		Connection con=null;
		String user="admin",pass="admin";
		
		try{
		Class.forName("oracle.jdbc.driver.OracleDriver");
			
		con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",user,pass);
			
		}catch(SQLException e){
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		finally{
			if(con==null){
				System.out.println("Connection failure");
			}
		}
		
		return con;
	}
}
