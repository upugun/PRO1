package net.ds.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import net.ds.value.object.DesOccupancy;
import net.jhl.objects.AgentDetails;
import net.jhl.objects.Guest;
import net.jhl.objects.Reservation;
import net.jwt.value.objects.QuerryObject;
import net.jwt.value.objects.User;


public class DestinityFetchDAO extends SQLDAO{

	public DestinityFetchDAO() {
		
	}
	
	private String propertyCode;
	
	

	public String getPropertyCode() {
		return propertyCode;
	}

	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}

	public void setLogedInUser(User logedInUser) {
		super.logedInUser = logedInUser;
	}
	public static void main(String[] args)throws Exception {
	
		DestinityFetchDAO dao = new DestinityFetchDAO();
		
		QuerryObject q = new QuerryObject();
		q.setCondition2("2015-11-01");
		q.setCondition3("2015-11-18");
//		q.setCondition3("A");
//		q.setCondition4(null);
		User u = new User();
		u.setUuid("1f240a92-c772-4b26-9446-37f926881a27");
		u.setUserName("Nisal");
		
		dao.setLogedInUser(u);
		ArrayList<DesOccupancy> desList = dao.getOccupancyDetails(q);
		
		System.out.println(desList.size());
		
	}
	
	public ArrayList<Guest>  getBookingDetails(QuerryObject q) throws SQLException
	{
	      Connection conn 			= getConnection(getPropertyCode());
	      CallableStatement cstmt 	= null;
	      ResultSet rs	 			= null;
	      ArrayList<Guest> returnList = new ArrayList<Guest>();
	      
	      try
	      {
	    	  cstmt = conn.prepareCall("{call dbo.JET_GetBookingdetails(?,?,?,?)}");
	    	  cstmt.setString(1, q.getCondition1());
	    	  cstmt.setString(2, q.getCondition2());
	    	  cstmt.setString(3, null);
	    	  cstmt.setString(4, null);
	    	  
	    	  cstmt.execute();
	    	  conn.commit();
	    	  rs = cstmt.getResultSet();
	          
	    	  while (rs.next()) {
	    		  returnList.add(objectmappingForBookingDetails(rs));
	           }
	    	  
	      }
	      catch (SQLException e) 
	      {
	    	  e.getMessage().toString();
	    	  rollBack(conn);
	      }
	      finally
	      {
	    	  closeAllConnections(rs, cstmt, conn);
	      }
	      return returnList;
	}
	
	public ArrayList<Reservation> getReservationDetails(QuerryObject q) throws SQLException
	{
	      Connection conn 			= getConnection(getPropertyCode());
	      CallableStatement cstmt 	= null;
	      ResultSet rs				= null;
	      ArrayList<Reservation> returnList = new ArrayList<Reservation>();
	      try
	      {
	    	  cstmt = conn.prepareCall("{call dbo.JET_GetReservationdetails(?,?,?,?,?)}");
	    	  cstmt.setString(1, q.getCondition1());
	    	  cstmt.setString(2, q.getCondition2());
	    	  cstmt.setString(3, q.getCondition3());
	    	  cstmt.setString(4, null);
	    	  cstmt.setString(5, null);
	    	  
	    	  cstmt.execute();
	    	  conn.commit();
	    	  rs = cstmt.getResultSet();
	          
	    	  while (rs.next()) {
	    		  returnList.add(objectmappingForReservationDetails(rs));
	           }
	    	  
	      }
	      catch (SQLException e) 
	      {
	    	  rollBack(conn);
	      }
	      finally
	      {
	    	  closeAllConnections(rs, cstmt, conn);
	      }
	      return returnList;
	}
	
	public ArrayList<DesOccupancy> getOccupancyDetails(QuerryObject q) throws Exception
	{
		  Connection conn 			= getConnection(getPropertyCode());
	      CallableStatement cstmt 	= null;
	      ResultSet rs	 			= null;
	      ArrayList<DesOccupancy> returnList = new ArrayList<DesOccupancy>();
	      try
	      {
	    	  	String dateFrom 		= formateDateString(q.getCondition2());
		  		String dateTo 			= formateDateString(q.getCondition3());
		  		
		  		Date startDate		 	= stringToDate(dateFrom);
		  		
		  		int dateGap			= calculateNumberOfDaysBetween(startDate, stringToDate(dateTo));
		  		
		  		
		  		for(int i=0; i<dateGap; i++)
		  		{
		  			String checkInDate 			 = addDaysToDate(startDate, i, "yyyy-MM-dd");
		  			
		  			QuerryObject	q2	=	new QuerryObject();
		  			q2.setCondition1(checkInDate);
		  			
		  			ArrayList<DesOccupancy> occList = occupancyDynamicSearch(q2);
		  			
		  			for(int j=0; j<occList.size(); j++)
		  			{
		  				occList.get(j).setOccupancyDate(checkInDate);
		  				returnList.add(occList.get(j));
		  			}
		  		}
	    	  
	      }
	      catch (SQLException e) 
	      {
	    	  rollBack(conn);
	    	  throw new Exception(e.toString());
	      }
	      finally
	      {
	    	  closeAllConnections(rs, cstmt, conn);
	      }
	      return returnList;
	} 
	
	private ArrayList<DesOccupancy> occupancyDynamicSearch(QuerryObject q) throws Exception
	{
		  Connection conn 			= getConnection(getPropertyCode());
	      CallableStatement cstmt 	= null;
	      ResultSet rs	 			= null;
	      
	      ArrayList<DesOccupancy> returnList = new ArrayList<DesOccupancy>();
	      try
	      {
	    	  cstmt = conn.prepareCall("{call dbo.JetX_RoomOccupancy(?)}");
	    	  cstmt.setString(1, q.getCondition1());
	    	  cstmt.execute();
	    	  conn.commit();
	    	  rs = cstmt.getResultSet();
	          
	    	  while (rs.next()) {
	    		  returnList.add(objectmappingForOccupancyDetails(rs));
	           }
	    	  
	      }
	      catch (Exception e) 
	      {
	    	  rollBack(conn);
	    	  throw new Exception(e.toString());
	      }
	      finally
	      {
	    	  closeAllConnections(rs, cstmt, conn);
	      }
	      return returnList;
	}
	
	private Reservation objectmappingForReservationDetails(ResultSet rs)
	{
		Reservation obj = new Reservation();
		try
		{
			Guest guest = new Guest();
			//destinity res no 1
			obj.setReservationType(rs.getString(2));
			// guest status 3
			guest.getPersonalDetails().setLastName(rs.getString(4));
			guest.getPersonalDetails().setFirstName(rs.getString(5));
			guest.getPersonalDetails().setTitle(rs.getString(6));
			obj.getTimeStamp().setCreatedByTimeStamp(rs.getString(7)); /** TODO: convert dateformat*/
			// res time 8
			guest.getPersonalDetails().setPassportNo(rs.getString(9));
			//status 10
			guest.getContactDetails().setLandNo(rs.getString(11));
			guest.getContactDetails().setFaxNo(rs.getString(12));
			guest.getContactDetails().setEmail(rs.getString(13));
			guest.getContactDetails().setPostalAddress(rs.getString(14));
			guest.getPersonalDetails().setCountryId(rs.getString(15));
			// company 16
			// visit purpose 17
			obj.getArrivalDetails().setCheckInDate(rs.getString(18)); /** TODO: convert dateformat*/
			obj.getDepartureDetails().setCheckOutDate(rs.getString(19)); /** TODO: convert dateformat*/
			obj.getMealDetails().setArrivalMeal(rs.getString(20));
			obj.getMealDetails().setLeaveMeal(rs.getString(21));
			obj.getMealDetails().setMealBasis(rs.getString(22));
			obj.setNoOfSglRooms(rs.getInt(23));
			obj.setNoOfDblRooms(rs.getInt(24));
			obj.setNoOfTplRooms(rs.getInt(25));
			// suits 26
			obj.setNoOfAdults(rs.getInt(27));
			obj.setNoOfChildren(rs.getInt(28));
			obj.setNoOfInfants(rs.getInt(29));
			obj.setNoOfInfants(rs.getInt(30));
			// res status 31
			// remindConf 32
			// remindOn 33
			// remindBy 34
			// airport pickup 35
			//pickup date 36
			// pickup time 37
			// flight No 38
			obj.setRemarks(rs.getString(39));
			obj.getTimeStamp().setCreatedByName(rs.getString(40));
			obj.getTimeStamp().setUpdatedByName(rs.getString(41));
			obj.getTimeStamp().setUpdatedByTimeStamp(rs.getString(42));
			obj.setOperatorId(rs.getString(43));
			obj.setNumberOfNights(rs.getInt(44));
			//allotment no 45
			obj.setMarketId(rs.getString(46));
			// expect time 47
			// grcNo 48
			// discount 49
			// district 50
			// airLine 51
			// class 52
			// ArrLoc 53
			// Alias 54
			// Flight Time 55
			//FIT_Agent 56
			// Street 57
			// city 58
			// country 59
			// add_country 60
			// pass_iss_date 61
			// Pass_iss_place 62
			//Pass_validUpto 63
			guest.getPersonalDetails().setDateOfBirth(rs.getString(64));
			guest.getContactDetails().setMobileNo(rs.getString(65));
			// arrMode 66
			// ArrMode_remarks 67
			// Exp_dep_time 68
			// Tour Operator 69
			// voucherNo 70
			// HOResNo 71
			// Upload 72
			// rateCode 73
			// packageCode 74
			// noFreeDays 75
			// bRemarks 76
			//SysDate 77
			// compRes 78
			obj.setGuestId(rs.getString(79));
			
			AgentDetails agent = new AgentDetails();
			agent.setAgentName(rs.getString(80));
			
//			AgentMarkets market = new AgentMarkets();
//			market.setDescription(rs.getString(81));
			
			obj.setGuestDetails(guest);
			obj.setAgentDetails(agent);
			
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return obj;
	}
	
	private Guest objectmappingForBookingDetails(ResultSet rs)
	{
		Guest obj = new Guest();
		try
		{
			//resNo
			//resType
			//obj.setReservationType(rs.getString(2));
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return obj;
	}
	private DesOccupancy objectmappingForOccupancyDetails(ResultSet rs) throws Exception
	{
		DesOccupancy obj = new DesOccupancy();
		try
		{
			obj.setRoomCategoryId(rs.getString(1));
			obj.setRoomCategoryName(rs.getString(2));
			obj.setTotalRooms(rs.getInt(3));
			obj.setOccupiedRooms(rs.getInt(4));
			obj.setAvailableRooms(rs.getInt(5));
			obj.setOutOfOrderRooms(rs.getInt(6));
			obj.setOccupancyPercentage(calculateOccupancyPercentage(obj.getTotalRooms(), obj.getAvailableRooms()));
			//obj.setOccupancyDate(rs.getString(7));// convert date to jwh
		}
		catch (Exception e) {
			throw new Exception(e.getMessage().toString());
		}
		
		return obj;
	}
	private double calculateOccupancyPercentage(int xTotalRooms, int xAvailableRooms){
		double x = (convertIntToDouble(xAvailableRooms)/convertIntToDouble(xTotalRooms))*100.0;
		return roundDecimal(x);
	}
}
