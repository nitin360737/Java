package com.wipro.game.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import com.wipro.game.util.DBUtil;

public class PlayerService {
	Connection con=DBUtil.getDatabaseConnection();

	public boolean validatePlayer(String playerID){
		String sql="select * from GAMEPLAYERS where PLAYERID=?";
		boolean res=false;
		try {
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setString(1,playerID);

			ResultSet result=pstmt.executeQuery();
			if(result.next()){
				res=true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public int rollDice(){
		int min=1,max=6;

		Random r=new Random();
	return r.nextInt((max-min)+1)+min;
	}

	public boolean isSnake(int currentCell){
		String sql="select * from SNAKECELLS where SNAKEHEAD=?";
		boolean res=false;
		try{
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setInt(1,currentCell);
			ResultSet result=pstmt.executeQuery();
			if(result.next()){
				res=true;
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}

	public boolean isLadder(int currentCell){
		String sql="select * from LADDERCELLS where LADDERBOTTOM=?";
		boolean res=false;
		PreparedStatement pstmt=null;
		ResultSet result=null;
		try{
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1,currentCell);
			result=pstmt.executeQuery();
			if(result.next()){
				res=true;
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			try { pstmt.close(); } catch (Exception ignore) { }
			try {result.close(); }catch(Exception ignore) { }
		}
		return res;
	}

	public void playGame(String playerOneID, String playerTwoID){
		String sql="select * from GAMEPLAYERS where PLAYERID=?";
		//Below Code will check whether both the users are already registered or not
		PreparedStatement pstmt=null;
		ResultSet result=null;
		try{
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,playerOneID);
			result=pstmt.executeQuery();

			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,playerTwoID);
			ResultSet result2=pstmt.executeQuery();

			if(result.next()==false && result2.next()==false){
				System.out.println("Player Not Registered");
			}
			else{
				
				int i=1;
				String player;
				
				while(currentCell(playerOneID) < 100 && currentCell(playerTwoID) < 100){
					if(i%2==1){
						player=playerOneID;
					}
					else{
						player=playerTwoID;
					}
					int rno=rollDice();
					int newCell=rno+currentCell(player);
					updateRecords(player,newCell,rno);
					
					
					
					if(isSnake(newCell)==true){
						sql="select SNAKETAIL from SNAKECELLS where SNAKEHEAD=?";
						pstmt=con.prepareStatement(sql);
						pstmt.setInt(1,newCell);
						
						result=pstmt.executeQuery();
						if(result.next()){
							System.out.println(player+" hits snake and reached the cell "+result.getInt(1));
							updateRecords(player,result.getInt(1),0);
						}
					}
					if(isLadder(newCell)==true){
						sql="select LADDERTOP from LADDERCELLS where LADDERBOTTOM=?";
						pstmt=con.prepareStatement(sql);
						pstmt.setInt(1,newCell);
						 
						result=pstmt.executeQuery();
						if(result.next()){
							System.out.println(player+" hits ladder and reached the cell"+result.getInt(1));
							updateRecords(player, result.getInt(1),0);
						}
					}
					
					i++;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			try { pstmt.close(); } catch (Exception ignore) { }
			try {result.close(); }catch(Exception ignore) { }
		}
	}

	public int currentCell(String playerID){
		String sql="select CURRENTCELL from GAMEPLAYERS where PLAYERID=?";
		int cell=0;
		PreparedStatement pstmt=null;
		ResultSet result=null;

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1,playerID);
			result=pstmt.executeQuery();
			result.next();
			cell=result.getInt("CURRENTCELL");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try { pstmt.close(); }catch(Exception ignore) {}
			try { result.close(); }catch(Exception ignore) {}
		}
		return cell;
	}
	
	public void updateRecords(String playerID,int cellno,int rno){
		String sql="UPDATE GAMEPLAYERS SET ISPLAYTING=?,CURRENTCELL=? WHERE playerID=?";
		if(rno!=0)
			System.out.println(playerID+" rolls dice and obtained "+rno);
		
		if(cellno==100) {
			System.out.println("Player "+playerID+" Won the Game.");
			System.exit(0);
		}
		
		if(cellno <= 100) {
			int prevCell=currentCell(playerID);
			PreparedStatement pstmt;
			int recordsInserted;
			try{
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1,1);
				pstmt.setInt(2,cellno);
				pstmt.setString(3,playerID);
				
				recordsInserted=pstmt.executeUpdate();
				
				if(recordsInserted>0){
					System.out.println(playerID+" moves from "+prevCell+" to the "+cellno);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
}
