package net.ds.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLXML;
import java.sql.Types;
import java.util.ArrayList;

import net.ds.value.object.DestinityReservation;
import net.jhl.dao.ReservationDAO;
import net.jhl.objects.Reservation;
import net.jwt.value.objects.User;


public class CreateReservationDAO extends SQLDAO{
	
	private static String res_sp_name = "dbo.Jet_InsertThisReservation";
	
	private String propertyCode;
	
	

	public String getPropertyCode() {
		return propertyCode;
	}

	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}

	public CreateReservationDAO() {
		
	}
	
	public static void main(String[] args) throws Exception{
		
//		CreateReservationDAO 
//		dao = new CreateReservationDAO();
//		
//		User user = new User();
//		user.setUserName("admin");
//		user.setUuid("213123");
//		
//		ReservationDAO resDAO = new ReservationDAO();
//		resDAO.setLogedInUser(user);
//		
//		QuerryObject q = new QuerryObject();
//		q.setSearchCode("uuid");
//		q.setCondition1("84914322-5BEE-8E2C-C53F-E8F007D8C3D2");
//		
//		ArrayList<Reservation> resList = resDAO.dynamicSearchExpand(q);
//		
//		dao.create(resList);
	}
	
	
	public boolean create(ArrayList<DestinityReservation> list) throws Exception
	{
	      Connection conn = getConnection(getPropertyCode());
	      CallableStatement cstmt = null;
	      
	      ArrayList<Reservation> resList = new ArrayList<Reservation>();
	      
	      int _count 			= 0;
	      int squeryExecuted 	= 0;
	      try
	      {
	    	  while((list.size() - _count) > 0)
	    	  {
	    		  DestinityReservation _obj = list.get(_count);
		    	  cstmt = conn.prepareCall("{call "+res_sp_name+"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		    	  
		    	  SQLXML xmlVar = conn.createSQLXML();
		    	  xmlVar.setString(_obj.getRoomXml());
		    	  
//		    	  Date adate = formateDate(_obj.getArrivalDetails().getCheckInDate());
//		    	  Date ddate = formateDate(_obj.getDepartureDetails().getCheckOutDate());
//		    	  java.sql.Date sqlaDate = new java.sql.Date(adate.getTime());
//		    	  java.sql.Date sqldDate = new java.sql.Date(ddate.getTime());
		    	  
		    	  
		    	  cstmt.setString(1, _obj.getDesResNo()); // only if update
		    	  cstmt.setString(2, _obj.getTitle());
		    	  cstmt.setString(3, _obj.getFirstName());
		    	  cstmt.setString(4, _obj.getLastName());
		    	  cstmt.setString(5, _obj.getPassportNo());
		    	  cstmt.setString(6, _obj.getEmail());
		    	  cstmt.setString(7, _obj.getLandNo());
		    	  cstmt.setString(8, _obj.getArrivalMeal()); // arrival meal
		    	  cstmt.setString(9, _obj.getLeaveMeal()); // leave meal
		    	  cstmt.setString(10, _obj.getAddress());
		    	  cstmt.setString(11, _obj.getCountry()); // country
		    	  cstmt.setDate(12, _obj.getArrivalDate());
		    	  cstmt.setDate(13, _obj.getDepartureDate());
		    	  cstmt.setInt(14, _obj.getNoOfAdults());// adult
		    	  cstmt.setInt(15, _obj.getNoOfChildren());// children
		    	  cstmt.setString(16, _obj.getRemarks()); // res remarks
		    	  cstmt.setString(17, _obj.getPackageCode()); // package code
		    	  cstmt.setString(18, _obj.getAgentId()); // agent ref (agent id)
		    	  cstmt.setString(19, _obj.getMarketCode()); // market code
		    	  cstmt.setString(20, _obj.getRateCodeId()); // rateCode 
		    	  cstmt.setString(21, _obj.getHotelCode()); // hotel code (not important) 
		    	  cstmt.setSQLXML(22, xmlVar); //destinity rooms
		    	  cstmt.setString(23, _obj.getChildSupplement()); //arrange as destinity child sup -- no need
		    	  cstmt.setString(24, _obj.getMealBasis());//meal plan 1st day
		    	  cstmt.setString(25, _obj.getCreateStatus()); // id I - insert U- update
		    	  cstmt.setString(26, _obj.getResStatus()); // resStatus //
		    	  cstmt.setString(27, _obj.getUserId()); // strUserId 
		    	  cstmt.setString(28, _obj.getGuestId()); // profileCode (guest id) 
		    	  cstmt.setString(29, _obj.getVoucherNo()); // voucherNo 
		    	  cstmt.setString(30, _obj.getTourNo()); // tourNo 
		    	  cstmt.setString(31, _obj.getSegment());
		    	  cstmt.setString(32, _obj.getNatianaliity());
		    	  cstmt.setString(33, _obj.getBookingType());
		    	  cstmt.setBoolean(34, _obj.isComplementaryType());
		    	  cstmt.setString(35, _obj.getGuestType());
		    	  cstmt.registerOutParameter(36, Types.CHAR);
		    	  
		    	  String destinityResNo = "NA";
		    	  
		    	  ResultSet rs = cstmt.executeQuery(); // Not update, you're returning a ResultSet.
		    	  
		    	  if (rs.next()) 
		    	  {
		    		  destinityResNo =rs.getString(1);
		    		  
		    		  Reservation res = new Reservation();
		    		  res.setUuid(_obj.getOtiumResId());
		    		  res.setDesResNo(destinityResNo);
		    		  res.setDesUploadStatus(true);
		    		  
		    		  resList.add(res);
		    	  }
		    	  
		    	  System.out.println("Destinity Reservation No" + destinityResNo);
		    	  
		    	  _count++;
	    	  }
	    	  conn.commit();
	    	  
	    	  //update destinity reservation no here
	    	  updateDesResNo(resList);
	    	  //update the upload status as uploaded
	    	  updateDesUploadStatus(resList);
	      }
	      catch (Exception e) 
	      {
	    	  rollBack(conn);
	    	  //update the upload status fail
	    	  updateDesUploadStatus(resList);
	    	  throw new Exception(e.getMessage().toString());
	      }
	      finally{
	    	  closeDBConnection(conn);
	      }
	      return checkRowUpdate(squeryExecuted);
	}
	
	private void updateDesResNo(ArrayList<Reservation> list)
	{
		
		try
		{
			User user = new User();
			user.setUserName("admin");
			user.setUuid("213123");
			
			ReservationDAO resDAO = new ReservationDAO();
			resDAO.setLogedInUser(user);
			
			resDAO.updateDesResNo(list);
		}
		catch (Exception e) {
			System.out.println("Error in CreateReservationDAO.updateDesResNo()");
		}
		
		
	}
	
	private void updateDesUploadStatus(ArrayList<Reservation> list)
	{
		
		try
		{
			User user = new User();
			user.setUserName("admin");
			user.setUuid("213123");
			
			ReservationDAO resDAO = new ReservationDAO();
			resDAO.setLogedInUser(user);
			
			resDAO.updateDesUploadStatus(list);
		}
		catch (Exception e) {
			System.out.println("Error in CreateReservationDAO.updateDesUploadStatus()");
		}
		
		
	}

}
