package com.wipro.game.service;
import com.wipro.game.bean.LadderBean;
import com.wipro.game.bean.RegisterBean;
import com.wipro.game.bean.SnakeBean;
public class MainClass
{
public static void main(String[] args)
{
	//Test 1: User Registration with Valid Data
	RegisterBean rbean = new RegisterBean();
	rbean.setPlayerName("Mari");
	RegistrationService robj = new RegistrationService();
	System.out.println(robj.registerPlayer(rbean));
	
	//Test 2: User Registration with In-Valid Data
	RegisterBean rbean1 = new RegisterBean();
	rbean1.setPlayerName(null);
	RegistrationService robj1 = new RegistrationService();
	System.out.println(robj1.registerPlayer(rbean1));
	
	//Test 3: Creating a snake (Valid Data)
	SnakeBean sbean = new SnakeBean();
	sbean.setHeadCell(40);
	sbean.setTailCell(20);
	AdminService aObj = new AdminService();
	System.out.println(aObj.makeSnakeEntry(sbean));
	
	//Test 4: Creating a snake (In-Valid Data)
	SnakeBean sbean1 = new SnakeBean();
	sbean1.setHeadCell(10);
	sbean1.setTailCell(10);
	System.out.println(aObj.makeSnakeEntry(sbean1));
	
	//Test 5: Creating a ladder (Valid Data)
	LadderBean lbean = new LadderBean();
	lbean.setTopCell(40);
	lbean.setBottomCell(11);
	System.out.println(aObj.makeLadderEntry(lbean));
	
	//Test 6: Playing the game
	PlayerService ps = new PlayerService();
	ps.playGame("PL1064", "PL1065");
	}
	}
