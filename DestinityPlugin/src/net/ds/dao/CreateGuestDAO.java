package net.ds.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import net.jhl.dao.GuestDAO;
import net.jhl.objects.Guest;


public class CreateGuestDAO extends SQLDAO{

	public CreateGuestDAO() {
		
	}
	
	private String propertyCode;
	
	

	public String getPropertyCode() {
		return propertyCode;
	}

	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}
	
	public static void main(String[] args) throws SQLException {
		
		CreateGuestDAO dao = new CreateGuestDAO();
		
		GuestDAO guestDAo = new GuestDAO();
		
		ArrayList<Guest> guestList = guestDAo.dynamicSearch("uuid", "0d4ea1bd-e22a-4293-9f2b-4866ddc7aed0");
		
		boolean b = dao.create(guestList);
		
		System.out.print(b);
	}
	
	public boolean create(ArrayList<Guest> list) throws SQLException
	{
	      Connection conn = getConnection(getPropertyCode());
	      CallableStatement cstmt = null;
	      
	      int _count 			= 0;
	      int squeryExecuted 	= 0;
	      
	      try
	      {
	    	  while((list.size() - _count) > 0)
	    	  {
	    		  Guest _obj = list.get(_count);
	    		  int x = 1;
		    	  cstmt = conn.prepareCall("{call dbo.Jet_InsertGuestProfile(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		    	  cstmt.setString(x, "T");	//Null not allowed
		    	  cstmt.setString(++x, "J"); //Null not allowed
		    	  cstmt.setString(++x, desGuestTitle(_obj.getPersonalDetails().getTitle()));
		    	  cstmt.setString(++x, _obj.getPersonalDetails().getFirstName());
		    	  cstmt.setString(++x, _obj.getPersonalDetails().getLastName());
		    	  cstmt.setString(++x, "NF"); //@Profilename --No match //Null not allowed (title " " + firstname + " " + lastname)
		    	  cstmt.setString(++x, _obj.getContactDetails().getPostalAddress());
		    	  cstmt.setString(++x, null); //@address2 --No match
		    	  cstmt.setString(++x, null); //@city --No match);
		    	  cstmt.setString(++x, null); //@postalAddress1 --No match
		    	  cstmt.setString(++x, null); //@postalAddress2 --No match
		    	  cstmt.setString(++x, null); //@billingAddress1 --No match
		    	  cstmt.setString(++x, null); //@billingAddress2 --No match
		    	  cstmt.setString(++x,_obj.getPersonalDetails().getCountryId());
		    	  cstmt.setString(++x, null); //@salutation --No match designation ref t
		    	  cstmt.setString(++x, null); //@VIP --No match vipstatus table
		    	  cstmt.setString(++x, null); //@nationality --No match
		    	  cstmt.setString(++x, null); //@company --No match
		    	  cstmt.setString(++x, _obj.getPersonalDetails().getDateOfBirth());
		    	  cstmt.setString(++x, _obj.getPersonalDetails().getPassportNo());
		    	  cstmt.setString(++x, null); //@guestStatus --No match
		    	  cstmt.setString(++x, null); //@designation --No match 
		    	  cstmt.setBoolean(++x, desGuestIsActive(_obj.getStatus()));
		    	  cstmt.setString(++x, _obj.getContactDetails().getLandNo());
		    	  cstmt.setString(++x, null); //@telephoneNo2 --No match
		    	  cstmt.setString(++x, null); //@telephoneNo3 --No match
		    	  cstmt.setString(++x, _obj.getContactDetails().getEmail());
		    	  cstmt.setString(++x, null); //@email2 --No match
		    	  cstmt.setString(++x, null); //@email3 --No match
		    	  cstmt.setString(++x, _obj.getRemarks());
		    	  cstmt.setString(++x, null); //@guestImagePath --No match
		    	  cstmt.setString(++x, "I"); //@operation should be char 'I'
		    	  cstmt.setString(++x, null); //@SkinColor --No match
		    	  cstmt.setString(++x, null); //@HairColor --No match
		    	  cstmt.setString(++x, null); //@EyeColor -- No match
		    	  
		    	  
		    	  squeryExecuted = cstmt.executeUpdate();
		    	  _count++;
	    	  }
	    	  
	    	  conn.commit();
	    	  
	      }
	      catch (SQLException e) 
	      {
	    	  rollBack(conn);
	      }
	      return checkRowUpdate(squeryExecuted);
	}
	
	public String desGuestTitle(String title){ //--------YG------Guest Title in Destinity format-------
		/*switch(title){
			case "Mr":
				return "01";
			case "Ms":
				return "02";
			case "Miss":
				return "03";
			case "Mrs":
				return "04";
			case "Dr":
				return "05";
			case "Ven":
				return "06";
			case "Capt":
				return "07";
		}*/
		if(title.equals("Mr")){
			return "01";
		}else if(title.equals("Ms")){
			return "02";
		}else if(title.equals("Mrs")){
			return "03";
		}else if(title.equals("Miss")){
			return "04";
		}else if(title.equals("Prof")){
			return "05";
		}else if(title.equals("Dr")){
			return "06";
		}else if(title.equals("Capt")){
			return "07";
		}
		return "N/A";
	}
	 public boolean desGuestIsActive(String string){ //--------YG------Status in Destinity format-------
		 if(string.equals("ACTIVE")){
			 return true;
		 }else{
			 return false;
		 }
	 }

}
