package com.wipro.game.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.wipro.game.bean.LadderBean;
import com.wipro.game.bean.SnakeBean;
import com.wipro.game.util.DBUtil;

public class AdminService {
	Connection con=DBUtil.getDatabaseConnection();
	public boolean validateSnake(SnakeBean sbean){
		
		if(sbean.getHeadCell()<sbean.getTailCell() || sbean.getHeadCell()==sbean.getTailCell() || !(sbean.getHeadCell()>=2 && sbean.getTailCell()<=99) || !(sbean.getHeadCell() >=1 && sbean.getTailCell()<=77)){
			return false;
		}
		else
			return true;
	}

	public boolean existingSnake(SnakeBean sbean){
		String sql="select * from SNAKECELLS where SNAKEHEAD=? and SNAKETAIL=?";
		boolean res=false;
		try {
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setInt(1,sbean.getHeadCell());
			pstmt.setInt(2,sbean.getTailCell());
			
			ResultSet result=pstmt.executeQuery();
			if(result.next()){
				res= false;
			}
			else
				res= true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public boolean isLadder(SnakeBean sbean){

		boolean res=false;

		try{
			String sql="select * from LADDERCELLS where LADDERTOP=? OR LADDERBOTTOM=?";

			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, sbean.getHeadCell());
			pstmt.setInt(2,sbean.getHeadCell());
			ResultSet result=pstmt.executeQuery();

			sql="select * from LADDERCELLS where LADDERTOP=? OR LADDERBOTTOM=?";

			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1,sbean.getTailCell());
			pstmt.setInt(2, sbean.getTailCell());
			ResultSet result2=pstmt.executeQuery();

			if(result.next())
				res=false;
			else if(result2.next())
				res=false;
			else
				res=true;

		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}

	public String makeSnakeEntry(SnakeBean sbean){
		String res=null;
		if(sbean==null){
			return "Invalid Snake Design Request";
		}
		else if(validateSnake(sbean)==true && existingSnake(sbean)==true && isLadder(sbean)==true){
			String sql="INSERT INTO SNAKECELLS VALUES (?,?)";

			try {
				PreparedStatement pstmt=con.prepareStatement(sql);
				pstmt.setInt(1,sbean.getHeadCell());
				pstmt.setInt(2,sbean.getTailCell());

				int rowsInserted=pstmt.executeUpdate();
				if(rowsInserted>0){
					res="New Entry Made";
				}

			} catch (SQLException e) {
				res="Failed To Insert Snake";
			}
		}
		else
		{
			res="Invalid Snake Design Request";
		}
		return res;
	}

	public boolean validateLadder(LadderBean lbean){
		if(lbean.getBottomCell()>lbean.getTopCell() || lbean.getBottomCell()==lbean.getTopCell() || !(lbean.getBottomCell()>=10 && lbean.getBottomCell()<=70) || !(lbean.getTopCell()>=20 && lbean.getTopCell()<=99)){
			return false;
		}
		else
			return true;
	}

	public boolean existingLadder(LadderBean lbean){
		String sql="select * from LADDERCELLS where LADDERTOP=? OR LADDERBOTTOM=?";
		boolean res=false;
		try {
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setInt(1,lbean.getTopCell());
			pstmt.setInt(2,lbean.getBottomCell());
			ResultSet result=pstmt.executeQuery();
			if(result.next()){
				res=false;
			}
			else
				res=true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public boolean isSnakeHeadOrTail(int ladderBottom){
		String sql="select * from SNAKECELLS where SNAKEHEAD=? OR SNAKETAIL=?";

		boolean res=false;
		try{
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setInt(1,ladderBottom);
			pstmt.setInt(2,ladderBottom);
			ResultSet result=pstmt.executeQuery();

			sql="select * from SNAKECELLS where SNAKEHEAD=? OR SNAKEHEAD=?";

			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1,ladderBottom);
			pstmt.setInt(2,ladderBottom);
			ResultSet result2=pstmt.executeQuery();

			if(result.next()){
				res=false;
			}
			else if(result2.next()){
				res=false;
			}
			else{
				res=true;
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}

	public String makeLadderEntry(LadderBean lbean){
		String res=null;
		if(lbean==null){
			return "Invalid Ladder Design Request";
		}
		else if(validateLadder( lbean) && isSnakeHeadOrTail( lbean.getBottomCell()) && existingLadder( lbean) ){
			String sql="INSERT INTO LADDERCELLS VALUES (?,?)";

			try {
				PreparedStatement pstmt=con.prepareStatement(sql);
				pstmt.setInt(1,lbean.getBottomCell());
				pstmt.setInt(2,lbean.getTopCell());

				int rowsInserted=pstmt.executeUpdate();
				if(rowsInserted>0){
					res="New Entry Made";
				}

			} catch (SQLException e) {
				res="Failed To Insert Ladder";
			}
		}
		else
		{
			res="Invalid Ladder Design Request";
		}
		return res;
	}


}
