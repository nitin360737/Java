package com.wipro.game.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.wipro.game.bean.RegisterBean;
import com.wipro.game.util.DBUtil;

public class RegistrationService {
	
	Connection con=DBUtil.getDatabaseConnection();
	PreparedStatement pstmt;
	
	public boolean generatePlayerID(RegisterBean rbean){
		String seq=null;
		boolean status=true;
		String generate_seq="select PLAYER_ID_SEQ.NEXTVAL from dual";
		try {
			PreparedStatement pst=con.prepareStatement(generate_seq);
			synchronized(this){
				ResultSet rs=pst.executeQuery();
				if(rs.next()){
					seq="PL"+String.valueOf(rs.getInt(1));
					rbean.setPlayerID(seq);
			}
				}
		} catch (SQLException e) {
			status=false;
			e.printStackTrace();
		}
		return status;
	}
	
	public boolean validatePlayerName(RegisterBean rbean){
		if(rbean.getPlayerName().isEmpty() || rbean.getPlayerName().length()<2){
			return false;
		}
		else{
			rbean.setPlayerName(rbean.getPlayerName().toUpperCase());
			return true;
		}	
	}
	
	public String registerPlayer(RegisterBean rbean){
		if(rbean==null || rbean.getPlayerName()==null)
			return "Inputs Missing";
		else{
			String result=null;
			if(generatePlayerID(rbean)==true && validatePlayerName(rbean)==true){
				String sql="INSERT INTO GAMEPLAYERS VALUES (?,?,?,?)";
				
				try {
					pstmt=con.prepareStatement(sql);
					pstmt.setString(1,rbean.getPlayerID());
					pstmt.setString(2,rbean.getPlayerName());
					pstmt.setInt(3,0);
					pstmt.setInt(4,0);
					
					int rowsInserted=pstmt.executeUpdate();
					if(rowsInserted>0){
						result="User Registered Successfully";
					}
				} catch (SQLException e) {
					result="Registration Failed";
					e.printStackTrace();
				}
			}
			else{
				result="Invalid Inputs, Registration Failed";
			}
			return result;
		}
	}
	
	
}
